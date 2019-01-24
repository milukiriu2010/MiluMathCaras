package milu.kiriu2010.milumathcaras.gui.draw.curve.spiral

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -------------------------------------------------------------------------------------
// 対数螺旋/ベルヌーイの螺旋
// -------------------------------------------------------------------------------------
//   x = r * cos(t) = a * exp(b*t) * cos(t)
//   y = r * sin(t) = a * exp(b*t) * sin(t)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Logarithmic_spiral
// https://www.mathcurve.com/courbes2d.gb/logarithmic/logarithmic.shtml
// -------------------------------------------------------------------------------------
// 画像を回転させることで螺旋を回転する
// -------------------------------------------------------------------------------------
class LogarithmicSpiral02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 対数螺旋の変数a
    // ---------------------------------
    private var a = 2f
    // ---------------------------------
    // 対数螺旋の変数b
    // ---------------------------------
    private var b = 0.14f

    // -------------------------------
    // 対数螺旋の回転角度(変数tに相当)
    // -------------------------------
    private var angle = 0f
    private var angleMax = 1080f

    // -------------------------------
    // 対数螺旋の回転方向
    // -------------------------------
    // +1:内に収束するようにみえる
    // -1:外に広がるようにみえる
    // -------------------------------
    private var sign = +1f

    // -------------------------------
    // 対数螺旋の描画点リスト
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
    // 第１引数:対数螺旋の回転角度
    // 第２引数:対数螺旋の変数a
    // 第３引数:対数螺旋の変数b
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 対数螺旋の回転角度
        angle = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 対数螺旋の回転角度
                0 -> angle = fl
                // 対数螺旋の変数a
                1 -> a = fl
                // 対数螺旋の変数b
                2 -> b = fl
            }
        }

        // 対数螺旋の描画点リストを生成
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 対数螺旋を回転する
                rotatePath()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                // 最初と最後は1秒後に描画
                if (angle == angleMax || angle == 0f) {
                    handler.postDelayed(runnable, 1000)
                }
                // 100msごとに描画
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

        (0..2240 step 5).forEach {
            val x = a*exp(b*it.toFloat()*PI/180f)*cos(it.toFloat()*PI/180f)
            val y = a*exp(b*it.toFloat()*PI/180f)*sin(it.toFloat()*PI/180f)
            pointLst.add(MyPointF(x.toFloat(),y.toFloat()))
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // 対数螺旋を回転する
    private fun rotatePath() {
        angle += 20f

        // ３周したら、
        // ・元の角度に戻す
        // ・回転方向を変更する
        if ( angle > angleMax ) {
            angle = 0f
            sign = -1f*sign
        }
        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)


        // 原点(0,0)の位置
        // = (左右中央,上下中央)
        val x0 = intrinsicWidth/2f
        val y0 = intrinsicHeight/2f

        // 原点(x0,y0)を中心に対数螺旋を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 対数螺旋を描く
        val path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            val x1 = myPointF.x
            val y1 = myPointF.y
            if ( index == 0 ) {
                path.moveTo(x1.toFloat(),y1.toFloat())
            }
            else {
                path.lineTo(x1.toFloat(),y1.toFloat())
            }
        }
        canvas.drawPath(path,linePaint)

        // 座標を元に戻す
        canvas.restore()

        // 描画点を回転させるのではなく、
        // 画像を回転させてみる方法
        val matrix = Matrix()
        matrix.postRotate(sign*angle,x0,y0)
        imageBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas2 = Canvas(imageBitmap)
        canvas2.drawBitmap(tmpBitmap,matrix,backPaint)

        // 枠を描画(画像を回転させるので、回転してから枠を描くこととした
        canvas2.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)
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