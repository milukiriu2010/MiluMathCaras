package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.spiral.logarithmic

import android.graphics.*
import android.os.Handler
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -------------------------------------------------------------------------------------
// 対数螺旋/ベルヌーイの螺旋上に円を描く
// -------------------------------------------------------------------------------------
//   x = r * cos(t) = a * exp(b*t) * cos(t)
//   y = r * sin(t) = a * exp(b*t) * sin(t)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Logarithmic_spiral
// https://www.mathcurve.com/courbes2d.gb/logarithmic/logarithmic.shtml
// -------------------------------------------------------------------------------------
// 画像を回転させることで螺旋を回転する
// -------------------------------------------------------------------------------------
class LogarithmicSpiralCircle02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 対数螺旋の変数a
    // ---------------------------------
    private var a = 75f
    // ---------------------------------
    // 対数螺旋の変数b
    // ---------------------------------
    private var b = 1f

    // -------------------------------
    // 対数螺旋の回転方向
    // -------------------------------
    // +1:内に収束するようにみえる
    // -1:外に広がるようにみえる
    // -------------------------------
    private var sign = +1f

    // -------------------------------
    // 対数螺旋の回転角度(変数tに相当)
    // -------------------------------
    private var angle = 0f
    private var angleMax = 360f

    // -------------------------------
    // 対数螺旋上の描画円リスト
    // -------------------------------
    val circleLst = mutableListOf<Circle>()

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
    // 対数螺旋を描くペイント
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

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // values
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 対数螺旋の描画点リストを生成
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 対数螺旋を回転する
                    rotatePath()
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
    // 描画中に呼び出すコールバックを設定
    // -------------------------------------
    override fun setNotifyCallback(notifyCallback: NotifyCallback) {
        this.notifyCallback = notifyCallback
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
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(event: MotionEvent) {
        //Log.d(javaClass.simpleName,"Touch:x[${x}]y[${y}]" )

        // タッチすると、回転方向を変更する
        if ( event.action == MotionEvent.ACTION_DOWN ) {
            sign = -sign
        }
    }

    // -------------------------------
    // 対数螺旋の描画点リストを生成
    // -------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        circleLst.clear()

        //val d = 15
        val d = 30
        (0 until 360 step d).forEach { id1 ->
            val t1 = id1.toFloat()
            (0..360 step 5).forEach { id2 ->
                val t2 = id2.toFloat()
                val circleR = Circle()
                    .apply {
                    c.x = a*exp(t2*PI/180f).toFloat() * MyMathUtil.cosf(t1+t2)
                    c.y = a*exp(t2*PI/180f).toFloat() * MyMathUtil.sinf(t1+t2)
                }
                circleR.color = Color.RED
                circleLst.add(circleR)

                // X軸だけ反転させて描画
                val circleB = Circle()
                    .apply {
                    c.x = -a*exp(t2*PI/180f).toFloat() * MyMathUtil.cosf(t1+t2)
                    c.y = a*exp(t2*PI/180f).toFloat() * MyMathUtil.sinf(t1+t2)
                }
                circleB.color = Color.BLUE
                circleLst.add(circleB)

            }
        }
    }

    // 対数螺旋を回転する
    private fun rotatePath() {
        angle += 20f

        // ３周したら、
        // ・元の角度に戻す
        // ・回転方向を変更する
        if ( angle > angleMax ) {
            angle = 0f
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // 枠を描画(画像を回転させるので、回転してから枠を描くこととした
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 原点(0,0)の位置
        // = (左右中央,上下中央)
        val x0 = intrinsicWidth/2f
        val y0 = intrinsicHeight/2f

        // 原点(x0,y0)を中心に対数螺旋を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 対数螺旋を描く
        val path = Path()
        circleLst.forEachIndexed { _, circle ->
            val x = circle.c.x * MyMathUtil.cosf(sign*angle) - circle.c.y * MyMathUtil.sinf(sign*angle)
            val y = circle.c.x * MyMathUtil.sinf(sign*angle) + circle.c.y * MyMathUtil.cosf(sign*angle)

            linePaint.color = circle.color
            canvas.drawCircle(x,y,circle.r,linePaint)
        }
        canvas.drawPath(path,linePaint)

        // 座標を元に戻す
        canvas.restore()

        // テンポラリを実体に反映
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)

        /*
        // 描画点を回転させるのではなく、
        // 画像を回転させてみる方法
        val matrix = Matrix()        matrix.postRotate(sign*angle,x0,y0)
        imageBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas2 = Canvas(imageBitmap)
        canvas2.drawBitmap(tmpBitmap,matrix,backPaint)

        // 枠を描画(画像を回転させるので、回転してから枠を描くこととした
        canvas2.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)
         */
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

    // 円
    data class Circle(
        // 中心
        val c: MyPointF = MyPointF(),
        // 半径
        val r: Float = 10f,
        // 色
        var color: Int = Color.RED
    )
}