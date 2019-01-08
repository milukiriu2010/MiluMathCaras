package milu.kiriu2010.milumathcaras.gui.draw.fractal.tree

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// ----------------------------------------------------
// 樹木曲線
// ----------------------------------------------------
// https://qiita.com/kmtoki/items/b94892771132e30aa1a2
// ----------------------------------------------------
class TreeCurve01Drawable: MyDrawable() {
    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1024f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  4回描くと、それ以降は違いがわからないので6回としている
    // --------------------------------------------------------
    private val nMax = 10

    // ----------------------------------------
    // 現在の枝の長さ：次の枝の長さの比率
    // ----------------------------------------
    private var lenRatio = 0.7f

    // ----------------------------------------
    // 現在の枝に対し、次の枝の角度
    // ----------------------------------------
    private var angleN = 60f

    // ----------------------------------------
    // 樹木曲線の描画点リスト
    // ----------------------------------------
    private val pointLst = mutableListOf<MyPointF>()

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

    // -------------------------------
    // 樹木曲線を描くペイント
    // -------------------------------
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

        // 樹木曲線を構築
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()


        // 描画に使うスレッド
        if (isKickThread) {
            runnable = Runnable {
                // 再帰レベルを１つ増やす
                incrementLevel()
                // 樹木曲線を構築
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

    // 樹木曲線を構築
    private fun createPath() {
        // 樹木曲線の描画点リストをクリアする
        pointLst.clear()

        // -------------------------
        // 樹木曲線の頂点
        // -------------------------
        val a = MyPointF(side/2f,0f)
        val b = MyPointF(side/2f,side/4f)

        // 次レベルの樹木曲線の描画点を求める
        calNextLevel(a,b,nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }


    // -------------------------------------
    // 次レベルの樹木曲線の描画点を求める
    // -------------------------------------
    private fun calNextLevel(a: MyPointF, b: MyPointF, n: Int) {
        pointLst.add(a)
        pointLst.add(b)

        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if (n <= 0) {
            return
        }

        // -----------------------------------------------------
        // 次の枝の長さ"B-C" = 現在の枝"A-B"の長さ×lenRatio
        // 次の枝の長さ"B-D" = 現在の枝"A-B"の長さ×lenRatio
        // -----------------------------------------------------
        val lenNext = sqrt((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y))*lenRatio

        // -----------------------------------------------------
        // 現在の枝"A-B"の角度
        // -----------------------------------------------------
        val angleAB = when {
            // -------------------------
            // BがAより右下(正:正)
            // 0 ～ 90度
            // -------------------------
            (b.x >= a.x) and (b.y >= a.y) -> {
                atan((b.y-a.y)/(b.x-a.x)) *180f/ PI
            }
            // -----------------------------------------------------------------------
            // BがAより左下(負:正)
            // 90 ～ 180度
            // -----------------------------------------------------------------------
            // atanは"-π/2 ～+π/2"を返すが、
            // 360度形式にしたいのと、左右が反転しているので180度からマイナスしている
            // -----------------------------------------------------------------------
            (b.x < a.x) and (b.y >= a.y) -> {
                180f - atan((b.y-a.y)/(a.x-b.x)) *180f/ PI
            }
            // -----------------------------------------------------------------------
            // BがAより左上(負:負)
            // 180 ～ 270度
            // -----------------------------------------------------------------------
            // atanは"-π/2 ～+π/2"を返すが、
            // 360度形式にしたいのと、上下左右が反転しているので180度を加算している
            // -----------------------------------------------------------------------
            (b.x < a.x) and (b.y < a.y) -> {
                atan((a.y-b.y)/(a.x-b.x)) *180f/ PI + 180f
            }
            // ------------------------------------------------------------------
            // BがAより右上(正:負)
            // 270 ～ 360度
            // ------------------------------------------------------------------
            // atanは"-π/2 ～+π/2"を返すが360度形式にしたいので360度足している
            // ------------------------------------------------------------------
            else -> {
                atan((b.y-a.y)/(b.x-a.x)) *180f/ PI + 360f
            }
        }

        // -----------------------------------------------------
        // 次の枝の角度"B-C" = 現在の枝"A-B"の角度 - angleN
        // -----------------------------------------------------
        val angleBC = angleAB - angleN

        // -----------------------------------------------------
        // 次の枝の角度"B-D" = 現在の枝"A-B"の角度 + angleN
        // -----------------------------------------------------
        val angleBD = angleAB + angleN

        // 描画点C
        val cx = b.x + lenNext*cos(angleBC*PI/180f).toFloat()
        val cy = b.y + lenNext*sin(angleBC*PI/180f).toFloat()
        val c = MyPointF(cx,cy)

        // 描画点D
        val dx = b.x + lenNext*cos(angleBD*PI/180f).toFloat()
        val dy = b.y + lenNext*sin(angleBD*PI/180f).toFloat()
        val d = MyPointF(dx,dy)

        // 次レベルの樹木曲線を描画
        calNextLevel(b,c,n-1)
        calNextLevel(b,d,n-1)
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

        canvas.save()
        // ---------------------------------------------------------------------
        // 原点(0,0)の位置
        //  = (マージン,マージン)
        // ---------------------------------------------------------------------
        canvas.translate(margin,margin)

        // 樹木曲線を描画
        val path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            if ( index%2 == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        canvas.drawPath(path,linePaint)

        // 座標を元に戻す
        canvas.restore()


        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
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