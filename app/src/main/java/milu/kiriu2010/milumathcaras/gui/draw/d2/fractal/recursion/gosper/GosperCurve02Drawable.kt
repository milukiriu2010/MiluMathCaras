package milu.kiriu2010.milumathcaras.gui.draw.d2.fractal.recursion.gosper

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyTurtle
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -----------------------------------------------
// ゴスパー曲線02
// -----------------------------------------------
// Axiom: A
// A: A-B--B+A++AA+B-
// B: +A-BB--B-A++A+B
// -----------------------------------------------
// A,B: 進む
// +  : 左へ60度
// -  : 右へ60度
// -----------------------------------------------
// https://en.wikipedia.org/wiki/Gosper_curve
// https://www.mathcurve.com/fractals/gosper/gosper.shtml
// http://ecademy.agnesscott.edu/~lriddle/ifs/ksnow/flowsnake.htm
// -----------------------------------------------
// 2019.06.03
// -----------------------------------------------
class GosperCurve02Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    private val side = 1000f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  5回描くと、遅い＆線で埋め尽くされるので、4回で終了
    // --------------------------------------------------------
    private val nMax = 5

    // ----------------------------------------
    // ゴスパー島の回転角度
    // ----------------------------------------
    // arcsin(sqrt(3)/(2*sqrt(7))) = 19.1066
    // ----------------------------------------
    private val angle = asin( sqrt(3f)/(2f*sqrt(7f)) ).toFloat()*180f/ PI.toFloat()

    // -----------------------------------------------------
    // ゴスパー曲線を描く際に基準となる正六角形の一辺の長さ
    //   r=2*sqrt(7)a
    // -----------------------------------------------------
    private var r = side/2f

    // -----------------------------------------------------
    // 係数b(=移動距離)=r*sqrt(21)/7
    // -----------------------------------------------------
    private var bc = sqrt(21f)/7f

    // -----------------------------------------------------
    // 係数c(=次の正六角形との相似比)=1/sqrt(7)
    // -----------------------------------------------------
    private var cc = 1f/sqrt(7f)

    // ----------------------------------------
    // ゴスパー曲線の描画Turtle
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

    // -------------------------------
    // ゴスパー島を描くペイント
    // -------------------------------
    private val linePaintA = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    // -------------------------------
    // ゴスパー曲線を描くペイント
    // -------------------------------
    private val linePaintB = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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

        // ゴスパー島を構築
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
                    // ゴスパー島を構築
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

    // ゴスパー島を構築
    private fun createPath() {
        // ----------------------------------------
        // ゴスパー曲線を描くための最初のステップ
        // ----------------------------------------
        createCurveInit(nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // ----------------------------------------
    // ゴスパー曲線を描くための初期処理
    // ----------------------------------------
    private fun createCurveInit(n: Int) {
        // ゴスパー曲線描画Turtleを初期化する
        myTurtle.clear()
        if ( n < 0 ) return

        // 移動距離
        val dv = bc*r*cc.pow(nNow)

        // 初期位置
        val a = MyPointF().also {
            it.x = r
            it.y = 0f
        }

        // ---------------------------------
        // ゴスパー曲線を描く亀に以下を設定
        // ・初期位置
        // ・移動距離
        // ・初期角度
        // ---------------------------------
        myTurtle.addPoint(a).apply {
            d = dv
            t = 240f+angle*(nNow.toFloat()-0.5f)
        }

        // ゴスパー曲線をパターンAで描画
        createCurveA(n)
    }

    // -------------------------------------
    // ゴスパー曲線をパターンAで描画
    // -------------------------------------
    private fun createCurveA( n: Int ) {
        if ( n > 0 ) {
            // パターンA
            createCurveA(n-1)
            // 右60度
            myTurtle.turn(-60f)
            // パターンB
            createCurveB(n-1)
            // 右120度
            myTurtle.turn(-120f)
            // パターンB
            createCurveB(n-1)
            // 左60度
            myTurtle.turn(60f)
            // パターンA
            createCurveA(n-1)
            // 左120度
            myTurtle.turn(120f)
            // パターンA
            createCurveA(n-1)
            // パターンA
            createCurveA(n-1)
            // 左60度
            myTurtle.turn(60f)
            // パターンB
            createCurveB(n-1)
            // 右60度
            myTurtle.turn(-60f)
        }
        else {
            // --------------------------
            // 亀の軌跡
            // --------------------------
            // ・移動
            // ・右60度
            // ・移動
            // ・右120度
            // ・移動
            // ・左60度
            // ・移動
            // ・左120度
            // ・移動
            // ・移動
            // ・左60度
            // ・移動
            // ・右60度
            // --------------------------
            myTurtle.move()
                .turn(-60f)
                .move()
                .turn(-120f)
                .move()
                .turn(60f)
                .move()
                .turn(120f)
                .move()
                .move()
                .turn(60f)
                .move()
                .turn(-60f)
        }
    }

    // -------------------------------------
    // ゴスパー曲線をパターンBで描画
    // -------------------------------------
    private fun createCurveB( n: Int ) {
        if ( n > 0 ) {
            // 左60度
            myTurtle.turn(60f)
            // パターンA
            createCurveA(n-1)
            // 右60度
            myTurtle.turn(-60f)
            // パターンB
            createCurveB(n-1)
            // パターンB
            createCurveB(n-1)
            // 右120度
            myTurtle.turn(-120f)
            // パターンB
            createCurveB(n-1)
            // 右60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1)
            // 左120度
            myTurtle.turn(120f)
            // パターンA
            createCurveA(n-1)
            // 左60度
            myTurtle.turn(60f)
            // パターンB
            createCurveB(n-1)
        }
        else {
            // --------------------------
            // 亀の軌跡
            // --------------------------
            // ・左60度
            // ・移動
            // ・右60度
            // ・移動
            // ・移動
            // ・右120度
            // ・移動
            // ・右60度
            // ・移動
            // ・左120度
            // ・移動
            // ・移動
            // ・左60度
            // ・移動
            // --------------------------
            myTurtle.turn(60f)
                .move()
                .turn(-60f)
                .move()
                .move()
                .turn(-120f)
                .move()
                .turn(-60f)
                .move()
                .turn(120f)
                .move()
                .turn(60f)
                .move()
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
        //  = (左右中央,上下中央)
        // ---------------------------------------------------------------------
        canvas.save()
        canvas.translate(intrinsicWidth/2f, intrinsicHeight/2f)

        // ゴスパー曲線を描画
        // 赤で描画
        if ( nNow <= 3 ) {
            val pathB = Path()
            myTurtle.pLst.forEachIndexed { index, myPointF ->
                //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
                if ( index == 0 ) {
                    pathB.moveTo(myPointF.x,myPointF.y)
                }
                else {
                    pathB.lineTo(myPointF.x,myPointF.y)
                }
            }
            canvas.drawPath(pathB,linePaintB)
        }
        // ゴスパー曲線を描画
        // 虹色で描画
        else {
            // 色インスタンス作成
            val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

            // ゴスパー曲線を描画
            val bunchSize = myTurtle.pLst.size
            var myPointF2: MyPointF? = null
            myTurtle.pLst.forEachIndexed { index, myPointF1 ->
                //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
                if ( myPointF2 != null ) {
                    val color = myColor.create(index,bunchSize)
                    linePaintB.color = color.toInt()
                    canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaintB)
                }
                myPointF2 = myPointF1
            }
        }


        // 座標を元に戻す
        canvas.restore()

        // これまでの描画はテンポラリなので実体にコピーする
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
        linePaintA.alpha = alpha
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaintA.colorFilter = colorFilter
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
