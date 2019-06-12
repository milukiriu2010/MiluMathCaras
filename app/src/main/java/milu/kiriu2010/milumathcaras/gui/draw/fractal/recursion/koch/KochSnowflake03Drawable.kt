package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.koch

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyTurtle
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -----------------------------------------------
// コッホ雪片03
// -----------------------------------------------
// だんだん拡大する
// -----------------------------------------------
// コッホ曲線一辺
// Axiom: F
// F: F+F--F+F
// -----------------------------------------------
// コッホ雪片とするには
// F--F--F
// -----------------------------------------------
// F: 進む
// +: 左へ60度
// -: 右へ60度
// -----------------------------------------------
// https://en.wikipedia.org/wiki/Koch_snowflake
// -----------------------------------------------
// 2019.06.11
// -----------------------------------------------
class KochSnowflake03Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    // "線分を3等分し、中央は正三角形を生成"
    // をレベルごとに繰り返すので
    // 6の累乗にしている
    // = 1296 = 6^4
    // ---------------------------------
    private val side = 1296f
    // ----------------------------------------------
    // コッホ雪片のレベル１の高さ分マージンをとる
    //   = side/2/sqrt(3)
    // ----------------------------------------------
    private val margin = side/2f/sqrt(3f)

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 4

    // ----------------------------------------
    // スケール
    // ----------------------------------------
    private var scaleMax = 1f
    private var scaleMin = 0f
    private var scaleNow = scaleMin
    private var scaleDv  = 0.1f

    // ----------------------------------------
    // コッホ雪片の描画Turtleリスト
    // ----------------------------------------
    private val myTurtleLst = mutableListOf<MyTurtle>()

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
    // コッホ雪片を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
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
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // コッホ雪片を構築
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
                    // スケールを拡大する
                    incrementScale()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 10)
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

    // コッホ雪片を構築
    private fun createPath() {
        // ----------------------------------------
        // コッホ雪片を描くための最初のステップ
        // ----------------------------------------
        (5..30 step 5).forEach {
            val myTurtle = createCurveInit(0.1f*it.toFloat())
            myTurtleLst.add(myTurtle)
        }
    }

    // ----------------------------------------
    // コッホ雪片を描くための初期処理
    // ----------------------------------------
    private fun createCurveInit(scale: Float): MyTurtle {
        val myTurtle = MyTurtle()

        // 移動距離
        val dv = scale * side/3f.pow(nNow)

        // ------------------------------
        // コッホ雪片の初期位置
        // ------------------------------
        val a = MyPointF().apply {
            x = -side*0.5f*scale
            y = -side/3f*scale
        }

        // ---------------------------------
        // コッホ雪片を描く亀に以下を設定
        // ・初期位置
        // ・移動距離
        // ・初期角度
        // ---------------------------------
        myTurtle.addPoint(a).apply {
            d = dv
            t = 0f
        }

        // コッホ雪片をパターンAで描画
        createCurveA(nNow, myTurtle)
        // タートル右120度
        myTurtle.turn(120f)
        // コッホ雪片をパターンAで描画
        createCurveA(nNow, myTurtle)
        // タートル右120度
        myTurtle.turn(120f)
        // コッホ雪片をパターンAで描画
        createCurveA(nNow, myTurtle)

        return myTurtle
    }

    // -------------------------------------
    // コッホ雪片をパターンAで描画
    // -------------------------------------
    private fun createCurveA( n: Int, myTurtle: MyTurtle ) {
        if ( n > 0 ) {
            // パターンA
            createCurveA(n-1, myTurtle)
            // 左60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1, myTurtle)
            // 右120度
            myTurtle.turn(120f)
            // パターンA
            createCurveA(n-1, myTurtle)
            // 左60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1, myTurtle)
        }
        else if ( n == 1 ){
            // --------------------------
            // 亀の軌跡
            // --------------------------
            // ・移動
            // ・左60度
            // ・移動
            // ・右120度
            // ・移動
            // ・左60度
            // ・移動
            // --------------------------
            myTurtle.move()
                .turn(-60f)
                .move()
                .turn(120f)
                .move()
                .turn(-60f)
                .move()
        }
        else if ( n == 0 ) {
            myTurtle.move()
        }
    }

    // -------------------------------------
    // スケールを拡大する
    // -------------------------------------
    private fun incrementScale() {
        scaleNow += scaleDv
        if ( scaleNow > scaleMax ) {
            scaleNow = scaleMin
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
        //  = (左右中央,上下中央)
        // ---------------------------------------------------------------------
        canvas.save()
        canvas.translate(intrinsicWidth*0.5f,intrinsicHeight*0.5f)

        // コッホ雪片を描画
        myTurtleLst.forEachIndexed { id1, myTurtle ->
            if ( id1 == 0 ) {
                val path1 = Path()
                myTurtle.pLst.forEachIndexed { id2, myPointF ->
                    if ( id2 == 0 ) {
                        path1.moveTo(scaleNow*myPointF.x,scaleNow*myPointF.y)
                    }
                    else {
                        path1.lineTo(scaleNow*myPointF.x,scaleNow*myPointF.y)
                    }
                }
                path1.close()
                canvas.drawPath(path1,linePaint)
            }

            val scale = when (id1) {
                // 1～2倍
                0 -> 1f + scaleNow
                // 1～1.5倍
                1 -> 1f + scaleNow * 0.5f
                // 1～1.333倍
                2 -> 1f + scaleNow * 1f/3f
                else -> 1f + scaleNow * 1f/(id1+1).toFloat()
            }

            val path2 = Path()
            myTurtle.pLst.forEachIndexed { id2, myPointF ->
                if ( id2 == 0 ) {
                    path2.moveTo(scale*myPointF.x,scale*myPointF.y)
                }
                else {
                    path2.lineTo(scale*myPointF.x,scale*myPointF.y)
                }
            }
            path2.close()
            canvas.drawPath(path2,linePaint)
        }

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
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun setColorFilter(colorFilter: ColorFilter?) {
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
