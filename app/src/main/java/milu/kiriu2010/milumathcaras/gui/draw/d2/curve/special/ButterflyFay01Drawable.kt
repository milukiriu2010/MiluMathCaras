package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.special

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.exp
import kotlin.math.pow

// -------------------------------------------------------------------------------------
// 蝶々 T.Fay 01
// -------------------------------------------------------------------------------------
//   x = a*(exp(cos(t))-2*cos(4t)+sin(t/12)^5)*cos(t)
//   y = a*(exp(cos(t))-2*cos(4t)+sin(t/12)^5)*sin(t)
// -------------------------------------------------------------------------------------
// https://www.mathcurve.com/courbes2d/ornementales/ornementales.shtml
// -------------------------------------------------------------------------------------
// 2019.05.25
// -------------------------------------------------------------------------------------
class ButterflyFay01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // ハートの変数a
    // ---------------------------------
    private var a = side/10f

    // ----------------------------------
    // ハートの位相(変数tに相当)
    // ----------------------------------
    private var angle = 0f
    private var angleMax = 360f

    // -------------------------------
    // ハートの描画点リスト
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
    // ハートを描くペイント
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

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // values
    // 第１引数:
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {

        // ハートの描画点リストを生成
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
                    // ハートの描画点リストを生成
                    createPath()
                    // ハートを回転する
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

    // -----------------------------------
    // ハートの描画点リストを生成
    // -----------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        (0..360 step 1).forEach {
            val t = it.toFloat()
            val cos1 = MyMathUtil.cosf(t)
            val cos4 = MyMathUtil.cosf(4*t)
            val exp_cos = exp(cos1)
            val sin1 = MyMathUtil.sinf(t)
            val sin_12_5 = MyMathUtil.sinf(t/12).pow(5f)
            val x = a*(exp_cos-2*cos4+sin_12_5)*cos1
            val y = a*(exp_cos-2*cos4+sin_12_5)*sin1
            pointLst.add(MyPointF(x,y))
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // ハートを回転する
    private fun rotatePath() {
        angle += 5f

        // ・元の角度に戻す
        if ( angle > angleMax ) {
            angle = 0f
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

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 原点(0,0)の位置
        // = (左右中央,上下中央)
        val x0 = intrinsicWidth/2f
        val y0 = intrinsicHeight/2f

        // X軸を描画(上下中央)
        canvas.save()
        canvas.translate(0f,y0)
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画(左右中央)
        canvas.save()
        canvas.translate(x0,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // 原点(x0,y0)を中心にハートを描く
        canvas.save()
        canvas.translate(x0,y0)

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // ハートを描く
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                val id = (index+angle.toInt())%bunchSize
                val color = myColor.create(id,bunchSize)
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