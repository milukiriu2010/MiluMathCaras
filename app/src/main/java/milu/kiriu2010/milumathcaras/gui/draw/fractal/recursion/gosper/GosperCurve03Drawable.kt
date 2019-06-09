package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.gosper

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyTurtle
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -----------------------------------------------
// ゴスパー曲線03
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
class GosperCurve03Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    private val side = 1000f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 2
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

    // ゴスパー曲線上を移動する点の位相位置
    //   0.0-1.0の値を保持
    private val phaseLst = mutableListOf<Float>()
    private val phaseDv1 = 0.05f
    private val phaseDv2 = 0.001f

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
    // ゴスパー曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    // -------------------------------
    // ドットを描くペイント
    // -------------------------------
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 黒ペンに小さい円のパス
    // -------------------------------
    private val dotPath = Path().apply {
        // CW:時計回り
        addCircle(0f, 0f, 8f, Path.Direction.CW)
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
                    // 位相を移動する
                    phaseShift()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 100)
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

        (0..19).forEach {
            phaseLst.add(phaseDv1*it.toFloat())
        }
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
    // 位相を移動する
    // -------------------------------------
    private fun phaseShift() {
        phaseLst.forEachIndexed { id, fl ->
            var phase = fl + phaseDv2
            if ( phase >= 1f ) {
                phase = 0f
            }
            phaseLst[id] = phase
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
        val path = Path()
        myTurtle.pLst.forEachIndexed { index, myPointF ->
            if ( index == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        canvas.drawPath(path,linePaint)

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path,false)

        phaseLst.forEach {
            dotPaint.pathEffect = PathDashPathEffect(dotPath,
                pathMeasure.length,
                pathMeasure.length*it,
                PathDashPathEffect.Style.TRANSLATE
                )
            canvas.drawPath(path,dotPaint)
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
