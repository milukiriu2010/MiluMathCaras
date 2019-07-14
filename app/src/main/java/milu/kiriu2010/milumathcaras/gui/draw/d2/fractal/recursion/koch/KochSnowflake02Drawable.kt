package milu.kiriu2010.milumathcaras.gui.draw.d2.fractal.recursion.koch

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyTurtle
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -----------------------------------------------
// コッホ雪片02
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
class KochSnowflake02Drawable: MyDrawable() {
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
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  4回描くと、それ以降は違いがわからないので6回としている
    // --------------------------------------------------------
    private val nMax = 6

    // ----------------------------------------
    // コッホ雪片の描画Turtle
    // ----------------------------------------
    private val myTurtle = MyTurtle()

    /*
    // ----------------------------------------
    // コッホ雪片の描画点リスト
    // ----------------------------------------
    private val pointLst = mutableListOf<MyPointF>()
    */

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
                    // 再帰レベルを１つ増やす
                    incrementLevel()
                    // コッホ雪片を構築
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

    // コッホ雪片を構築
    private fun createPath() {
        /*
        // コッホ雪片の描画点リストをクリアする
        pointLst.clear()
        */

        // ----------------------------------------
        // コッホ雪片を描くための最初のステップ
        // ----------------------------------------
        createCurveInit(nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // ----------------------------------------
    // コッホ雪片を描くための初期処理
    // ----------------------------------------
    private fun createCurveInit(n: Int) {
        // 描画Turtleを初期化する
        myTurtle.clear()
        if ( n < 0 ) return

        // 移動距離
        val dv = side/3f.pow(nNow)

        // ------------------------------
        // コッホ雪片の初期位置
        // ------------------------------
        val a = MyPointF(0f,0f)

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
        createCurveA(nNow)
        // タートル右120度
        myTurtle.turn(120f)
        // コッホ雪片をパターンAで描画
        createCurveA(nNow)
        // タートル右120度
        myTurtle.turn(120f)
        // コッホ雪片をパターンAで描画
        createCurveA(nNow)
    }

    // -------------------------------------
    // コッホ雪片をパターンAで描画
    // -------------------------------------
    private fun createCurveA( n: Int ) {
        if ( n > 0 ) {
            // パターンA
            createCurveA(n-1)
            // 左60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1)
            // 右120度
            myTurtle.turn(120f)
            // パターンA
            createCurveA(n-1)
            // 左60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1)
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
        // 原点(0,0)の位置 => 正三角形が中央にくるようにする
        //  = (マージン,マージン+"領域の高さ-正三角形の高さ"/2)
        // と、思ったがレベル１の正三角形分下に表示されることがわかったのでコメントアウト
        // ---------------------------------------------------------------------
        // val ymargin = margin+(side-side*sqrt(3f)/2f)/2f
        // ---------------------------------------------------------------------
        // 原点(0,0)の位置 => コッホ曲線が中央に来るようにしたい
        //   よって、
        //   "描画領域の高さ－コッホ曲線の高さ"/2＋再帰レベル１の正三角形の高さ
        //   をY座標のマージンとする
        //
        //   コッホ曲線の高さ=正三角形の高さ＋再帰レベル１の正三角形の高さ
        // ---------------------------------------------------------------------
        val kochH = side*sqrt(3f)/2f + side/2f/sqrt(3f)
        val ymargin = (intrinsicHeight - kochH)/2f + side/2f/sqrt(3f)

        canvas.save()
        //Log.d(javaClass.simpleName, "ymargin[$ymargin]kochH[$kochH]intrinsicHeight[$intrinsicHeight]")
        canvas.translate(margin, ymargin)

        /*
        //Log.d(javaClass.simpleName,"===============================")
        // コッホ雪片を描画
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            if ( index == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // コッホ雪片を描画
        // 1536色のグラデーション
        val bunchSize = myTurtle.pLst.size
        var myPointF2: MyPointF? = null
        myTurtle.pLst.forEachIndexed { index, myPointF1 ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            if ( myPointF2 != null ) {
                val color = myColor.create(index,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
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
