package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.triangle.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 三角形⇔六角形03
// -----------------------------------------------------------------
// (1) 三角形
// (2) 三角形＋台形
// (3) 六角形＋平行四辺形
// (4) 六角形
// ⇒ (1)
// -----------------------------------------------------------------
// (1) 三角形の一辺 = 2*sqrt(3)*a
//     三角形の半径 = 2*a
// -----------------------------------------------------------------
// 2019.10.08
// -----------------------------------------------------------------
class Triangle2Hexagon03Drawable: MyDrawable() {

    // 描画パターン
    enum class ModePtn {
        PTN1,
        PTN2
    }

    // 現在の描画パターン
    private var modeNow = ModePtn.PTN1

    // (1) 三角形の一辺
    private val a1a = 50f*sqrt(3f)
    // (1) 三角形の半径
    private val a1r = a1a/sqrt(3f)

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = a1a*4f
    private val margin = 0f

    // 描画数
    private val splitN = 5

    // 移動比率
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private var ratioDv = 0.05f
    private val ratios = floatArrayOf(0.2f,0.4f,0.6f,0.8f,1f)

    // 描画する多角形リスト
    private val polygons = mutableListOf<Polygon>()

    // "描画点の初期位置設定"をしたかどうか
    private var isInitialized = false

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
    // 頂点を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
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
    // 可変変数 values の引数位置による意味合い
    //
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // "描画点の初期位置設定"をしたかどうか
        isInitialized = false
        // 描画点の初期位置設定
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
                    // 描画点の初期位置設定
                    createPath()
                    // 描画点を移動する
                    movePath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 300)
                    /*
                    if ( ratios.contains(ratioNow) ) {
                        handler.postDelayed(runnable, 800)
                    }
                    else {
                        handler.postDelayed(runnable, 200)
                    }

                     */
                }
                // "停止"状態のときは、更新されないよう処理をスキップする
                else {
                    handler.postDelayed(runnable, 200)
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
    // 描画点の初期位置設定
    // -------------------------------
    private fun createPath() {
        if ( ( isInitialized ) and ( ratioNow < ratioMax ) ) return

        ratioNow = ratioMin

        // 現在のモードを変更
        modeNow = when (modeNow) {
            ModePtn.PTN1 -> ModePtn.PTN1
            else -> ModePtn.PTN1
        }

        // 多角形のパスを初期化
        if (modeNow == ModePtn.PTN1) {
             initPathPtn1()
        }


        if ( isInitialized == false ) isInitialized = true
    }

    // -------------------------------
    // 多角形のパスを初期化(その１)
    // -------------------------------
    private fun initPathPtn1() {
        polygons.clear()

        val polygon0 = Polygon().also { pl ->
            (0..2).forEach { j ->
                val jj = 90f + 120f*j.toFloat()
                val x = a1r * MyMathUtil.cosf(jj)
                val y = a1r * MyMathUtil.sinf(jj)
                pl.ps.add(MyPointF(x,y))
            }
        }

        (0..splitN).forEach { _ ->
            val polygon1 = polygon0.copy()
            //polygon1.ratio = ratios[i]
            polygons.add(polygon1)
        }
    }

    // -------------------------------
    // 三角形を移動する
    // -------------------------------
    private fun movePath() {
        ratioNow += ratioDv
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
        // = (中央,中央)
        val x0 = intrinsicWidth.toFloat()*0.5f
        val y0 = intrinsicHeight.toFloat()*0.5f

        canvas.save()
        canvas.translate(x0,y0)

        // 描画
        if (modeNow == ModePtn.PTN1) {
            drawPtn1(canvas)
        }

        canvas.restore()

        // これまでの描画はテンポラリなので、実体にコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // -------------------------------
    // 描画(パターン１)
    // -------------------------------
    private fun drawPtn1(canvas: Canvas) {
        (0..splitN).forEach { i ->
            val ii = i.toFloat()
            val polygon = polygons[i]

            (0..5).forEach { j ->
                val jj = j.toFloat() * 60f
                val x0 = ii * a1r * MyMathUtil.cosf(jj+360f*ratioNow)
                val y0 = ii * a1r * MyMathUtil.sinf(jj+360f*ratioNow)

                canvas.save()
                canvas.translate(x0,y0)

                val path = Path()
                polygon.ps.reversed().forEachIndexed { id, p1 ->
                    val p2 = p1.copy().rotate(-120f*ratioNow)

                    if (id == 0) {
                        path.moveTo(p2.x,p2.y)
                    }
                    else {
                        path.lineTo(p2.x,p2.y)
                    }
                }
                path.close()
                canvas.drawPath(path,linePaint)

                canvas.restore()
            }

        }
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

    // --------------------------------------
    // 多角形
    // --------------------------------------
    private data class Polygon(
        // 頂点の位置
        var ps: MutableList<MyPointF> = mutableListOf(),
        // 移動比率
        var ratio: Float = 0f
    )
}
