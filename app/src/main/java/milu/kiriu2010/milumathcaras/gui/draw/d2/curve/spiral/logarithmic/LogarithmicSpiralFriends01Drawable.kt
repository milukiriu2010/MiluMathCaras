package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.spiral.logarithmic

import android.graphics.*
import android.os.Handler
import android.view.MotionEvent
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

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // ---------------------------------
    // 対数螺旋の変数a
    // ---------------------------------
    private var a = 75f
    // ---------------------------------
    // 対数螺旋の変数b
    // ---------------------------------
    private var b = 1f

    // -------------------------------
    // 対数螺旋の回転角度(変数tに相当)
    // -------------------------------
    private var angleMax = 360f
    private var angleDv = 12f
    private var angleN = (angleMax/angleDv).toInt()

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

        val d = 15
        // 螺旋の回転
        (0 until 360 step d).forEach { id1 ->
            val t1 = id1.toFloat()
            // 螺旋の分割
            (0 until 360 step angleDv.toInt()).forEach { id2 ->
                val t2 = id2.toFloat()
                val aA = a*exp(t2*PI/180f).toFloat()
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
        var p2: MyPointF? = null
        val pSize = pointLst.size
        pointLst.forEachIndexed { idA, p1 ->
            if ( p2 != null ) {
                // ----------------------------------------
                // 現在の描画点
                // ----------------------------------------
                val p1x = p1.x
                val p1y = p1.y

                // ----------------------------------------
                // １つ前の描画点
                // ----------------------------------------
                val p2x = p2!!.x
                val p2y = p2!!.y

                // ----------------------------------------
                // 前列の描画点
                // ----------------------------------------
                val idB = when {
                    (idA < angleN) -> pSize-angleN+idA
                    else -> idA-angleN
                }
                val p3 = pointLst[idB]
                val p3x = p3.x
                val p3y = p3.y

                val p4 = pointLst[idB-1]
                val p4x = p4.x
                val p4y = p4.y

                /*
                linePaint.color = when ((idA/angleN)%2) {
                    0 -> Color.RED
                    else -> Color.BLUE
                }

                 */


                linePaint.color = when ((idA/angleN)%2) {
                    // 奇数番目の螺旋
                    0 -> {
                        when (idA%2) {
                            0 -> Color.BLACK
                            else -> Color.WHITE
                        }
                    }
                    // 偶数番目の螺旋
                    else -> {
                        when (idA%2) {
                            1 -> Color.BLACK
                            else -> Color.WHITE
                        }
                    }
                }

                /* 花みたい
                linePaint.color =
                    when (idA%2) {
                        0 -> Color.BLACK
                        else -> Color.WHITE
                    }

                 */

                val path = Path()
                path.moveTo(p1x,p1y)
                path.lineTo(p2x,p2y)
                path.lineTo(p4x,p4y)
                path.lineTo(p3x,p3y)
                path.close()
                canvas.drawPath(path,linePaint)
            }
            p2 = when (idA%angleN) {
                angleN-1 -> null
                else -> p1
            }
        }

        // 座標を元に戻す
        canvas.restore()

        // テンポラリを実体に反映
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
