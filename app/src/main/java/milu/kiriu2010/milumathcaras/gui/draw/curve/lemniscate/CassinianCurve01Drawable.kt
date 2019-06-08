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
// カッシニアン曲線
// -------------------------------------------------------------------------------------
//   x = a * sqrt(cos(nt)+sqrt(1-sin(nt)^2) * cos(t)
//   y = a * sqrt(cos(nt)+sqrt(1-sin(nt)^2) * sin(t)
// -------------------------------------------------------------------------------------
//   x = a * sqrt(cos(nt)-sqrt(1-sin(nt)^2) * cos(t)
//   y = a * sqrt(cos(nt)-sqrt(1-sin(nt)^2) * sin(t)
// -------------------------------------------------------------------------------------
// http://www.mathcurve.com/courbes2d.gb/cassinienne/cassinienne.shtml
// -------------------------------------------------------------------------------------
class CassinianCurve01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 500f
    private val margin = 10f

    // ---------------------------------
    // カッシニアン曲線の変数a
    // ---------------------------------
    private val aLst = arrayListOf<Float>(100f,90f,80f,70f,60f,50f)

    // ---------------------------------
    // カッシニアン曲線の変数b
    // ---------------------------------
    private val bLst = arrayListOf<Float>(50f,60f,70f,80f,90f,100f)

    // ---------------------------------
    // カッシニアン曲線の変数n
    // ---------------------------------
    private var n = 3f

    // 隣同士の描画点が、この距離以上離れていたら、線を引かない
    val eps = 50f

    // ----------------------------------
    // カッシニアン曲線の位相(変数tに相当)
    // ----------------------------------
    private var angle = 0f
    private var angleDv  = 5f
    private var angleMax = 360f

    // -------------------------------
    // カッシニアン曲線の描画点リスト
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
    // カッシニアン曲線を描くペイント
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

    // -------------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -------------------------------------------
    // values
    // 第１引数:カッシニアン曲線の変数nの初期値
    // -------------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 第１引数:カッシニアン曲線の変数nの初期値
                0 -> n = fl
            }
        }

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
                    // カッシニアン曲線の描画点リストを生成
                    //createPath()
                    // カッシニアン曲線の媒介変数tの値を増加
                    increment()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    if (angle == 0f) {
                        handler.postDelayed(runnable, 1000)
                    }
                    else {
                        handler.postDelayed(runnable, 10)
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
    // カッシニアン曲線の描画点リストを生成
    // ---------------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        aLst.forEachIndexed { id, a ->
            val b = bLst[id]
            (0 until 360).forEach {
                val t = it.toFloat()
                val cos = MyMathUtil.cosf(t)
                val sin = MyMathUtil.sinf(t)
                val cosn = MyMathUtil.cosf(n*t)
                val sinn = MyMathUtil.sinf(n*t)

                val e2n = (b/a).pow(2f*n)
                var p = e2n-sinn*sinn
                if ( p < 0f ) p = 0f
                var q = cosn+sqrt(p)
                if ( q < 0f ) q = 0f

                val x = a*sqrt(q)*cos
                val y = a*sqrt(q)*sin
                pointLst.add(MyPointF(x,y))
            }

            (0 until 360).forEach {
                val t = it.toFloat()
                val cos = MyMathUtil.cosf(t)
                val sin = MyMathUtil.sinf(t)
                val cosn = MyMathUtil.cosf(n*t)
                val sinn = MyMathUtil.sinf(n*t)

                val e2n = (b/a).pow(2f*n)
                var p = e2n-sinn*sinn
                if ( p < 0f ) p = 0f
                var q = cosn-sqrt(p)
                if ( q < 0f ) q = 0f

                val x = a*sqrt(q)*cos
                val y = a*sqrt(q)*sin
                pointLst.add(MyPointF(x,y))
            }
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // ---------------------------------------
    // カッシニアン曲線の媒介変数tの値を増加
    // ---------------------------------------
    private fun increment() {
        angle += angleDv
        if ( angle >= angleMax ) {
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

        // 原点(x0,y0)を中心に対数螺旋を描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // カッシニアン曲線を描く
        // 赤色で線を描く
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            when (index) {
                0 -> path.moveTo(myPointF.x,myPointF.y)
                else -> path.lineTo(myPointF.x,myPointF.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // カッシニアン曲線を描く
        // 1536色のグラデーション
        //val bunchSize = pointLst.size/aLst.size
        val bunchSize = 360
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { id, myPointF1 ->
            if ( myPointF2 != null ) {
                if ( myPointF1.distance(myPointF2!!) < eps ) {
                    val color = myColor.create((id+angle.toInt())%bunchSize,bunchSize)
                    linePaint.color = color.toInt()
                    canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
                }
            }
            if ( id == bunchSize ) {
                myPointF2 = null
            }
            else {
                myPointF2 = myPointF1
            }
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