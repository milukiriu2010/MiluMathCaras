package milu.kiriu2010.milumathcaras.gui.draw.d2.circle.circles

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// --------------------------------------------
// 花火01
// --------------------------------------------
// 2019.07.02
// --------------------------------------------
class Fireworks01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // 大円の半径
    private val rB = side*0.4f
    // 入れ替え円の半径
    private val rC = rB*0.5f
    // "入れ替え円の中心⇔大円中心"の距離
    private val rD = rC*sqrt(3f)
    // 描画円の半径
    private val rE = rB*0.1f

    // 初期角度
    private val initAngles = floatArrayOf(
        300f, 330f,   0f,  30f,
         60f,  90f, 120f, 150f,
        180f, 210f, 240f, 270f
    )
    // 色位置
    private var cPos = 0

    // -------------------------------
    // 円の回転角度
    // -------------------------------
    private var angleMax = 360f
    private var angleNow = 0f

    // -------------------------------------
    // 円リスト
    // -------------------------------------
    private val circleLst = mutableListOf<Circle>()

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
    // 円を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // 色リスト
    private val colors = intArrayOf(
        Color.MAGENTA,
        Color.CYAN,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        0xffff8000.toInt(),
        Color.RED
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

    // -----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -----------------------------------------
    // 可変変数 values の引数位置による意味合い
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 円を生成
        createCircle()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 円を移動する
                    moveCircle()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 50)
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

    // -------------------------------
    // 円を生成
    // -------------------------------
    private fun createCircle() {
        circleLst.clear()

        (0..11).forEach { i ->
            val ii = i.toFloat()
            val x = rD * MyMathUtil.cosf((ii+1f)*30f)
            val y = rD * MyMathUtil.sinf((ii+1f)*30f)
            val circle = Circle().also {
                it.t = initAngles[i]
                it.cC = MyPointF(x,y)
            }
            circleLst.add(circle)
        }

    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        angleNow += 5f
        cPos++
        if ( angleNow >= angleMax ) {
            angleNow = 0f
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 原点(0,0)の位置
        // = (左右中央,上下中央)
        val x0 = (intrinsicWidth/2).toFloat()
        val y0 = (intrinsicHeight/2).toFloat()

        // 原点(x0,y0)を中心に円を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 円を描く
        val start = 2
        (0..6).forEach { j ->
            val scale = (j+start).toFloat()*0.1f
            circleLst.forEach { circle ->
                canvas.save()
                canvas.translate(scale*circle.cC.x,scale*circle.cC.y)

                val x = scale*rC * MyMathUtil.cosf(circle.t+angleNow)
                val y = scale*rC * MyMathUtil.sinf(circle.t+angleNow)
                linePaint.color = colors[(j+cPos)%colors.size]
                canvas.drawCircle(x,y,scale*rE,linePaint)

                canvas.restore()
            }
        }

        canvas.restore()

        // これまでの描画はテンポラリ領域につき、実体にコピーする
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

    // ---------------------------------
    // 円
    // ---------------------------------
    data class Circle (
        // ---------------------------------
        // 角度
        // ---------------------------------
        var t: Float = 0f,
        // ---------------------------------
        // 入れ替えの中心点
        // ---------------------------------
        var cC: MyPointF = MyPointF(),
        // ---------------------------------
        // 入れ替えの中心点
        // ---------------------------------
        var color: Int = Color.BLACK
    )
}
