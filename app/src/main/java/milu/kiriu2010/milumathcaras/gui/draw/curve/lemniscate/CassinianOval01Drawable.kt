package milu.kiriu2010.milumathcaras.gui.draw.curve.lemniscate

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -------------------------------------------------------------------------------------
// カッシーニの卵形線
// -------------------------------------------------------------------------------------
//   x = a * sqrt(cos(2t)+sqrt((b/a)^4-sin(2t)^2) * cos(t)
//   y = a * sqrt(cos(2t)+sqrt((b/a)^4-sin(2t)^2) * sin(t)
// -------------------------------------------------------------------------------------
//   x = a * sqrt(cos(2t)-sqrt((b/a)^4-sin(2t)^2) * cos(t)
//   y = a * sqrt(cos(2t)-sqrt((b/a)^4-sin(2t)^2) * sin(t)
// -------------------------------------------------------------------------------------
// (1) a=b ⇒ レムニスケート
// (2) a<b ⇒ ２つのループ
// (3) a>b ⇒ １つのループ
// -------------------------------------------------------------------------------------
// https://www.mathcurve.com/courbes2d.gb/cassini/cassini.shtml
// https://en.wikipedia.org/wiki/Cassini_oval
// -------------------------------------------------------------------------------------
class CassinianOval01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // カッシーニの卵形線の変数a
    // ---------------------------------
    private var a = side/2f
    // ---------------------------------
    // カッシーニの卵形線の変数b
    // ---------------------------------
    private var b = side/2f

    // ----------------------------------
    // カッシーニの卵形線の位相
    // ----------------------------------
    private var angle = 0f
    private var angleMax = 720f

    // ---------------------------------
    // カッシーニの卵形線の描画点リスト
    // ---------------------------------
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

    // ---------------------------------
    // カッシーニの卵形線を描くペイント
    // ---------------------------------
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
    // 第１引数:カッシーニの卵形線の変数a
    // 第２引数:カッシーニの卵形線の変数b
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // カッシーニの卵形線の変数a
                0 -> a = fl
                // カッシーニの卵形線の変数b
                1 -> b = fl
            }
        }

        // カッシーニの卵形線の描画点リストを生成
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
                    // カッシーニの卵形線の描画点リストを生成
                    createPath()
                    // カッシーニの卵形線を回転する
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

    // ---------------------------------------
    // カッシーニの卵形線の描画点リストを生成
    // ---------------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        (0..360 step 1).forEach {
            val t = it.toFloat()+angle
            val x = a * sqrt(MyMathUtil.cosf(2f*t)+sqrt((b/a).pow(4f)-MyMathUtil.sinf(2f*t).pow(2f))) * MyMathUtil.cosf(t)
            val y = a * sqrt(MyMathUtil.cosf(2f*t)+sqrt((b/a).pow(4f)-MyMathUtil.sinf(2f*t).pow(2f))) * MyMathUtil.sinf(t)
            pointLst.add(MyPointF(x,y))
        }
        if ( b < a ) {
            (0..360 step 1).forEach {
                val t = it.toFloat()+angle
                val x =
                    a * sqrt(MyMathUtil.cosf(2f * t) - sqrt((b / a).pow(4f) - MyMathUtil.sinf(2f * t).pow(2f))) *
                            MyMathUtil.cosf(t)
                val y =
                    a * sqrt(MyMathUtil.cosf(2f * t) - sqrt((b / a).pow(4f) - MyMathUtil.sinf(2f * t).pow(2f))) *
                            MyMathUtil.sinf(t)
                pointLst.add(MyPointF(x, y))
            }
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // リサージュ曲線を回転する
    private fun rotatePath() {
        angle += 5f

        // ２回転したら
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

        // 原点(x0,y0)を中心に描く
        canvas.save()
        canvas.translate(x0,y0)

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // リサージュ曲線を描く
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { id, myPointF1 ->
            if ( myPointF2 != null ) {
                val color = myColor.create(id,bunchSize)
                //Log.d(javaClass.simpleName,"index[$index]bunchSize[$bunchSize]color[${"0x%08x".format(color)}]")
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = if ( id == 360 ) null else myPointF1
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