package milu.kiriu2010.milumathcaras.gui.draw.d2.fractal.recursion.dragon

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyTurtle
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.pow
import kotlin.math.sqrt

// --------------------------------------------------
// Lévy C
// --------------------------------------------------
// https://en.wikipedia.org/wiki/L%C3%A9vy_C_curve
// --------------------------------------------------
// タートルを使って描画する
// --------------------------------------------------
// Start: F
// +F--F+
// --------------------------------------------------
// F: 進む
//    再帰レベルが増えるたびに1/sqrt(2)の移動距離になる
// +: 右へ45度
// -: 左へ45度
// --------------------------------------------------
// 2019.06.06
// --------------------------------------------------
class LevyCurve01Drawable: MyDrawable() {
    // -------------------------------------
    // 描画領域
    // -------------------------------------
    // Lévy C曲線は
    // 正方形を２分割するので２の階乗を選ぶ
    //   = 1024 = 2^10
    // -------------------------------------
    private val side = 1024f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  7回以上描くと、塗りつぶされていしまうので6回としている
    // --------------------------------------------------------
    private val nMax = 15

    // ----------------------------------------
    // 描画に使うタートル
    // ----------------------------------------
    private val myTurtle = MyTurtle()

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

    // -------------------------------------
    // Lévy C曲線を描くペイント
    // -------------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

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
    // 第１引数:再帰レベル(整数)
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 再帰レベル
        nNow = 0
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 再帰レベル
                0 -> nNow = fl.toInt()
            }
        }

        // Lévy C曲線を構築
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()


        // 描画に使うスレッド
        if (isKickThread) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 再帰レベルを１つ増やす
                    incrementLevel()
                    // Lévy C曲線を構築
                    createPath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 最初と最後は1秒後に描画
                    if ( nNow == nMax || nNow == 0 ) {
                        handler.postDelayed(runnable, 1000)
                    }
                    // 500msごとに描画
                    else {
                        handler.postDelayed(runnable, 500)
                    }
                }
                // "停止"状態のときは、更新されないよう処理をスキップする
                else {
                    handler.postDelayed(runnable, 100)
                }
            }
            handler.postDelayed(runnable, 1000)
        }
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

    // Lévy C曲線を構築
    private fun createPath() {
        // ----------------------------------------
        // Lévy C曲線を描くための最初のステップ
        // ----------------------------------------
        createCurveInit(nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // ----------------------------------------
    // Lévy C曲線を描くための最初のステップ
    // ----------------------------------------
    private fun createCurveInit(n: Int) {
        // 描画用Turtleを初期化する
        myTurtle.clear()
        if ( n < 0 ) return

        // 比率
        val ratio = 0.5f

        // 移動距離
        val dv = side*ratio/(sqrt(2f).pow(n))
        // 初期位置
        val a = MyPointF().also {
            it.x = (1f-ratio)*0.5f*side
            it.y = 0f
        }

        // 亀の初期設定
        myTurtle.addPoint(a).apply {
            // 移動距離
            d = dv
            // 初期角度
            t = 0f
        }

        // パターンAで描画
        createCurveA(n)
    }

    // -------------------------------------
    // Lévy C曲線をパターンAで描画
    // -------------------------------------
    private fun createCurveA( n: Int ) {
        if ( n > 1 ) {
            // 右45度
            myTurtle.turn(45f)
            // パターンA
            createCurveA(n-1)
            // 左90度
            myTurtle.turn(-90f)
            // パターンA
            createCurveA(n-1)
            // 右45度
            myTurtle.turn(45f)
        }
        else if ( n == 1 ) {
            // 右45度
            // 移動
            // 左90度
            // 移動
            // 右45度
            myTurtle.turn(45f)
                .move()
                .turn(-90f)
                .move()
                .turn(45f)
        }
        else {
            myTurtle.move()
        }
    }

    // -------------------------------------
    // 再帰レベルを１つ増やす
    // -------------------------------------
    private fun incrementLevel() {
        nNow++
        // 最大値を超えたら０に戻す
        if ( nNow > nMax ) {
            nNow = 0
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), framePaint)

        // ---------------------------------------------------------------------
        // 原点(0,0)の位置
        //  = (マージン,マージン＋α)
        // ---------------------------------------------------------------------
        canvas.save()
        canvas.translate(margin, margin+intrinsicHeight*0.2f)

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // Lévy C曲線を描画
        // 1536色のグラデーション
        val bunchSize = myTurtle.pLst.size
        var myPointF2: MyPointF? = null
        myTurtle.pLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                val color = myColor.create(index,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
        }

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画はテンポラリなので、実体に描画する
        val matrix = Matrix()
        matrix.postScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
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
