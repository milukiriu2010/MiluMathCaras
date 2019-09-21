package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.spiral.logarithmic

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// 現状の実装だと、塗りつぶし部分が"曲線の四角"でなく"直線の四角"になってしまう
// -------------------------------------------------------------------------------------
// 対数螺旋間に四角形を描く
// -------------------------------------------------------------------------------------
//   x = r * cos(t) = a * exp(b*t) * cos(t)
//   y = r * sin(t) = a * exp(b*t) * sin(t)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Logarithmic_spiral
// https://www.mathcurve.com/courbes2d.gb/logarithmic/logarithmic.shtml
// https://66.media.tumblr.com/f1f28291bf2eacdac80c62b3a92283ad/tumblr_oocpijI2bh1r2geqjo1_540.gif
// -------------------------------------------------------------------------------------
// 2019.09.18
// -------------------------------------------------------------------------------------
class LogarithmicSpiralFriends01Drawable: MyDrawable() {

    private enum class ModePtr {
        PTN1,
        PTN2
    }

    // 現在のモード
    private var modeNow = ModePtr.PTN2

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // ---------------------------------
    // 対数螺旋の変数a
    // ---------------------------------
    private var a = 10f
    // ---------------------------------
    // 対数螺旋の変数b
    // ---------------------------------
    private var b = 1f

    // -------------------------------
    // 対数螺旋の回転角度(変数tに相当)
    // -------------------------------
    private var angleNow = 0
    private var angleDv = 12

    // 螺旋の数
    private var spiralN = 24

    // -------------------------------
    // 対数螺旋上の描画点リスト
    // -------------------------------
    val pointLst = mutableListOf<MyPointF>()

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
        color = Color.BLACK
        style = Paint.Style.FILL
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

    // -------------------------------
    // 対数螺旋の描画点リストを生成
    // -------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        val d = 360/spiralN
        // 螺旋の回転
        (0 until 360 step d).forEach { id1 ->
            val t1 = id1.toFloat()
            // 螺旋の分割
            (0 until 360).forEach { id2 ->
                val t2 = id2.toFloat()
                val aA = a*exp(b*t2*PI/180f).toFloat()
                val p = MyPointF().apply {
                    x = aA * MyMathUtil.cosf(t1+t2)
                    y = aA * MyMathUtil.sinf(t1+t2)
                }
                pointLst.add(p)
            }
        }
    }

    // 対数螺旋を回転する
    private fun rotatePath() {
        angleNow++
        if ( angleNow == angleDv) {
            angleNow = 0
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
        val x0 = intrinsicWidth * 0.5f
        val y0 = intrinsicHeight * 0.5f

        // 原点(x0,y0)を中心に対数螺旋を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 対数螺旋を描く
        when (modeNow) {
            ModePtr.PTN1 -> drawPTN1(canvas)
            ModePtr.PTN2 -> drawPTN2(canvas)
        }

        // 座標を元に戻す
        canvas.restore()

        // テンポラリを実体に反映
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // -------------------------------
    // 螺旋を描くパターン１
    // -------------------------------
    private fun drawPTN1(canvas: Canvas) {
        var bw = 0
        (0 until spiralN).forEach loopA@{ i ->
            val k = i*360
            val l = if (i == (spiralN-1)) -360 else k

            val s = when (i%2) {
                0 -> angleNow
                else -> {
                    bw--
                    angleDv - angleNow
                }
            }

            (s until 360 step angleDv).forEach loopB@{ j ->
                if ((j+angleDv) >= 360) {
                    return@loopB
                }

                val p1 = pointLst[k+j]
                val p2 = pointLst[k+j+angleDv]
                val p3 = pointLst[l+j+angleDv+360]
                val p4 = pointLst[l+j+360]

                linePaint.color = when (bw%2) {
                    0     -> Color.BLACK
                    else -> Color.WHITE
                }

                val path = Path()
                path.moveTo(p1.x,p1.y)
                path.lineTo(p2.x,p2.y)
                path.lineTo(p3.x,p3.y)
                path.lineTo(p4.x,p4.y)
                path.close()
                canvas.drawPath(path,linePaint)

                bw++
            }
        }
    }

    // -------------------------------
    // 螺旋を描くパターン２
    // -------------------------------
    private fun drawPTN2(canvas: Canvas) {
        var bw = 0
        (0 until spiralN).forEach loopA@{ i ->
            val k1 = i*360
            val k2 = (i+1)*360
            val l1 = if (i == (spiralN-1)) -360 else k1
            val l2 = if (i >= (spiralN-2)) -360 else k1

            (0 until 360 step angleDv).forEach loopB@{ j ->
                if ((j+angleDv) >= 360) {
                    return@loopB
                }

                val p11 = pointLst[k1+j]
                val p12 = pointLst[k1+j+angleDv]
                val p13 = pointLst[l1+j+angleDv+360]
                val p14 = pointLst[l1+j+360]

                val p21 = pointLst[l1+j+360]
                val p22 = pointLst[l1+j+angleDv+360]
                val p23 = pointLst[l2+j+angleDv+720]
                val p24 = pointLst[l2+j+720]

                val p01 = p11.lerp(p21,angleNow.toFloat(),angleDv.toFloat())
                val p02 = p12.lerp(p22,angleNow.toFloat(),angleDv.toFloat())
                val p03 = p13.lerp(p23,angleNow.toFloat(),angleDv.toFloat())
                val p04 = p14.lerp(p24,angleNow.toFloat(),angleDv.toFloat())

                linePaint.color = when (bw%2) {
                    0     -> Color.WHITE
                    else -> Color.BLACK
                }

                val path = Path()
                path.moveTo(p01.x,p01.y)
                path.lineTo(p02.x,p02.y)
                path.lineTo(p03.x,p03.y)
                path.lineTo(p04.x,p04.y)
                path.close()
                canvas.drawPath(path,linePaint)

                bw++
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
}
