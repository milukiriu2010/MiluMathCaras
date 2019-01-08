package milu.kiriu2010.milumathcaras.gui.draw.complex.mandelbrot

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.math.Complex
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------------------------------
// マンデルブロ―集合
// https://en.wikipedia.org/wiki/Mandelbrot_set
// https://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html
// https://introcs.cs.princeton.edu/java/32class/
// ---------------------------------------------------------------------
// Zn+1 = Zn^2 + c
// Z0 = 0
// n=>無限大で発散しないという条件を満たす複素数c全体が作る集合
// ---------------------------------------------------------------------
// X軸:実数 -1.5～0.5
// Y軸:虚数 -1.5～1.5
// の描画例をよくみかける
// ---------------------------------------------------------------------
// 描画領域を正方形にしたいので、
// ここでは、
// X軸:実数 -1.5～0.5
// Y軸:虚数 -1.0～1.0
// とする
// ---------------------------------------------------------------------
// 計算に時間がかかるので、
// "X軸 1pixelに対し Y軸 2000 pixelスキャン"
// を1スレッドの単位とする
// ---------------------------------------------------------------------
class Mandelbrot01Drawable: MyDrawable() {
    // -------------------------------
    // 描画領域
    // -------------------------------
    //  1 pixel = 0.001単位で描画
    // -------------------------------
    private val side = 2000f
    private val margin = 50f

    // -------------------------------------
    // 複素数の描画レンジ
    // -------------------------------------
    // 実数部最小値
    private var xrMin = -1.5f
    // 実数部最大値
    private var xrMax = 0.5f
    // 虚数部最小値
    private var yiMin = -1.0f
    // 虚数部最大値
    private var yiMax = 1.0f

    // -------------------------------
    // 複素数を計算する際の粒度
    // -------------------------------
    private var psU = 0.001f
    // ----------------------------------
    // 描画の粒度
    // ----------------------------------
    //  psU=0.1   => 100 pixelずつ描画
    //  psU=0.01  =>  10 pixelずつ描画
    //  psU=0.001 =>   1 pixelずつ描画
    // ----------------------------------
    //private var nU = side/((xrMax-xrMin)/psU)
    private var nU = side*psU/(xrMax-xrMin)
    // ----------------------------------
    // X軸の現在描画位置
    // ----------------------------------
    private var nX = 0f

    // -------------------------------------
    // 発散と判断するまでの計算回数(最大値)
    // -------------------------------------
    private var nMax = 100

    // ---------------------------------------------------------------------
    // 描画領域として使うビットマップ
    // ---------------------------------------------------------------------
    // 画面にタッチするとdrawが呼び出されるようなのでビットマップに描画する
    // ---------------------------------------------------------------------
    private lateinit var imageBitmap: Bitmap
    private val tmpBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)

    // -------------------------------
    // 枠に使うペイント
    // -------------------------------
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // -------------------------------
    // バックグラウンドに使うペイント
    // -------------------------------
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // マンデルブロ―集合を描くペイント
    // ---------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
    }

    // ------------------------------------
    // 描画に使う色一覧
    // 発散に達した回数によって色を変える
    // ------------------------------------
    // とりえあずレインボーカラー+白にしている
    // ------------------------------------
    val colorLst = intArrayOf(
        // 0:red
        Color.parseColor("#ffff0000"),
        // 1:orange
        Color.parseColor("#ffff7f00"),
        // 2:yellow
        Color.parseColor("#ffffff00"),
        // 3:green
        Color.parseColor("#ff00ff00"),
        // 4:blue
        Color.parseColor("#ff0000ff"),
        // 5:cyan
        Color.parseColor("#ff00ffff"),
        // 6:indigo
        Color.parseColor("#ff6f00ff"),
        // 7:violet
        Color.parseColor("#ffff00ff"),
        // 8:white
        Color.parseColor("#ffffffff")
    )

    // -------------------------------------
    // 描画中に呼び出すコールバックを設定
    // -------------------------------------
    private var notifyCallback: NotifyCallback? = null

    // ---------------------------------------
    // 別スレッドで描画するためのハンドラ
    // ---------------------------------------
    val handler = Handler()

    // ---------------------------------------
    // 描画に使うスレッド
    // ---------------------------------------
    private lateinit var runnable: Runnable

    // ----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // ----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:複素数を計算する際の粒度
    //          1未満の小数を計算するが
    //          描画領域が1000x1000なので
    //          0.1,0.01,0.001を想定
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 複素数を計算する際の粒度
        psU = 0.001f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 複素数を計算する際の粒度
                0 -> psU = fl
            }
        }
        // ----------------------------------
        // 描画の粒度
        // ----------------------------------
        //  psU=0.1   => 100 pixelずつ描画
        //  psU=0.01  =>  10 pixelずつ描画
        //  psU=0.001 =>   1 pixelずつ描画
        // ----------------------------------
        //nU = side/((xrMax-xrMin)/psU)
        nU = side*psU/(xrMax-xrMin)
        // ----------------------------------
        // X軸の現在描画位置
        // ----------------------------------
        nX = 0f

        // -------------------------------
        // ビットマップに枠のみ描画
        // -------------------------------
        initBitmap()

        // 計算に時間がかかるため、常にスレッドを起動することとする
        runnable = Runnable {
            // 虚数部をスキャン
            scanImagenary()
            // 描画
            invalidateSelf()

            // X軸を全スキャンしていない場合は、スレッドを起動する
            if ( nX <= side ) {
                handler.postDelayed(runnable, 10)
            }
            // X軸を全スキャンしたら、スレッドを解放する
            else {
                // 描画に使うスレッドを解放する
                handler.removeCallbacks(runnable)
            }
        }
        handler.postDelayed(runnable, 10)

    }

    // -------------------------------------
    // CalculationCallback
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    override fun calStop() {
        // 描画に使うスレッドを解放する
        handler.removeCallbacks(runnable)
    }

    // -------------------------------------
    // CalculationCallback
    // 描画中に呼び出すコールバックを設定
    // -------------------------------------
    override fun setNotifyCallback(notifyCallback: NotifyCallback) {
        this.notifyCallback = notifyCallback
    }

    // -------------------------------
    // ビットマップに枠のみ描画
    // -------------------------------
    private fun initBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), framePaint)

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // -------------------------------
    // 虚数部を１列スキャン
    // -------------------------------
    private fun scanImagenary() {
        //Log.d(javaClass.simpleName,"=== nX[$nX] ==================================")

        val canvas = Canvas(tmpBitmap)
        canvas.save()
        // 原点(0,0)を移動する
        //   = (マージン+Xの描画点,マージン)
        canvas.translate(margin+nX.toFloat(),margin)
        // 色の数
        val cntColor = colorLst.size
        // -------------------------------------------------------
        // 実数部
        // -------------------------------------------------------
        //   Doubleの場合、0.001単位で計算すると、
        //   0.5000001など、きれいな数字になってくれないので、
        //   BigDecimalにしている。
        // -------------------------------------------------------
        //val x0 = xrMin.toDouble() + psU.toDouble()*nX.toDouble()/nU.toDouble()
        val x0 = xrMin.toBigDecimal() + psU.toBigDecimal()*nX.toBigDecimal()/nU.toBigDecimal()
        (0..side.toInt() step nU.toInt()).forEach {
            // 虚数部
            //val y0 = yiMin.toDouble() + psU.toDouble()*it.toDouble()/nU.toDouble()
            val y0 = yiMin.toBigDecimal() + psU.toBigDecimal()*it.toBigDecimal()/nU.toBigDecimal()
            // 複素数
            val z0 = Complex(x0.toDouble(),y0.toDouble())
            // "複素数の２乗+c"が発散するかチェック
            val n = chkDiverge(z0)
            // 描画点の色を設定する
            linePaint.color = when {
                // 発散に達した回数によって色を変える
                (n >= 0) -> colorLst[n%cntColor]
                // 発散しない場合、黒
                else -> Color.BLACK
            }
            // -------------------------------------------
            // 原点(0,0)を描画点の位置に移動する
            // X座標は,既に移動しているので0のままでよい
            // -------------------------------------------
            canvas.translate(0f,nU.toFloat())
            // ---------------------------------------------------
            // 1 pixel単位のときはdrawPointがよいが
            // サムネイルは粒度が荒いので、四角形で書くこととした
            // ---------------------------------------------------
            canvas.drawRect(RectF(0f,0f,nU.toFloat(),nU.toFloat()),linePaint)
        }
        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
        canvas.drawBitmap(tmpBitmap,0f,0f,backPaint)

        //　スキャンが完了したX座標を通知する
        notifyCallback?.receive(x0.toFloat())

        // スキャンが終わったら、次にスキャンするX座標を１つ右に移動
        nX += nU
    }

    // -------------------------------------
    // "複素数の２乗+c"が発散するかチェック
    // -------------------------------------
    // return
    //   発散したときの計算回数を返す
    //   発散しない場合-1を返す
    // -------------------------------------
    private fun chkDiverge(z0: Complex): Int {
        var z = z0
        (1..nMax).forEach { t ->
            z = z.times(z).plus(z0)
            // -------------------------------------------
            // 絶対値が2を超える場合は、発散するとみなす。
            // -------------------------------------------
            if (z.abs() > 2.0) return t
        }
        return -1
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun draw(canvas: Canvas) {
        // 描画用ビットマップがインスタンス化されていなければ描画はスキップする
        if ( this::imageBitmap.isInitialized == false ) return

        canvas.drawBitmap(imageBitmap,0f,0f,framePaint)
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicWidth(): Int = (side+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (side+margin*2).toInt()
}
