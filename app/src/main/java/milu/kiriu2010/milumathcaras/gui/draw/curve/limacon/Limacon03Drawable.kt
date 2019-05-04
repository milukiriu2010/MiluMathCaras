package milu.kiriu2010.milumathcaras.gui.draw.curve.limacon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -------------------------------------------------------------------------------------
// パスカルの蝸牛形
// -------------------------------------------------------------------------------------
//   x = a * (cos(k*t)+b*cos(t)) * cos(t)
//   y = a * (cos(k*t)+b*cos(t)) * sin(t)
// -------------------------------------------------------------------------------------
// https://www.mathcurve.com/courbes2d.gb/limacon/limacon.shtml
// -------------------------------------------------------------------------------------
class Limacon03Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // パスカルの蝸牛形の変数a
    // ---------------------------------
    private var a = side/5f

    // ---------------------------------
    // パスカルの蝸牛形の変数b
    // ---------------------------------
    private var b = 0.5f

    // ---------------------------------
    // パスカルの蝸牛形の変数k
    // ---------------------------------
    private var k = 0.5f

    // ----------------------------------
    // パスカルの蝸牛形の位相(変数tに相当)
    // ----------------------------------
    private var angle = 0f
    private var angleMax = 360f

    // -------------------------------
    // パスカルの蝸牛形の描画点リスト
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
    // パスカルの蝸牛形を描くペイント
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
    // 第１引数:パスカルの蝸牛形の変数b
    // 第２引数:パスカルの蝸牛形の変数k
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // パスカルの蝸牛形の変数b
                0 -> b = fl
                // パスカルの蝸牛形の変数k
                1 -> k = fl
            }
        }

        // パスカルの蝸牛形の描画点リストを生成
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        /*
        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // パスカルの蝸牛形の描画点リストを生成
                    createPath()
                    // パスカルの蝸牛形を回転する
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
        */
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
        if ( this::runnable.isInitialized == false ) return
        // 描画に使うスレッドを解放する
        handler.removeCallbacks(runnable)
    }

    // -----------------------------------
    // パスカルの蝸牛形の描画点リストを生成
    // -----------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        // ------------------------------------------------
        // 係数tを変化させて描画点を生成
        // ------------------------------------------------
        // kの値によって、曲線が閉じるtが違うので
        // ちゃんと計算したほうがよいと思うが
        // k=0.7とか1.7の対応が大変そうなので
        // 簡便のため3600としている
        // ------------------------------------------------
        // 以下参考
        //   k=0.5 => t=720
        //   k=0.7 => t=2520たぶん
        //     360*7,360*10
        //     252,360の最小公倍数
        //   k=0.9 => t=3600
        //     400=360/0.9
        //     400,360の最小公倍数
        //   k=1.5 => t=720
        //     240=360/1.5
        //     240,360の最小公倍数
        //   k=1.8 => t=1800
        //     200=360/1.8
        //     200,360の最小公倍数
        (0..3600 step 1).forEach {
            val f = it.toFloat()
            val cos = MyMathUtil.cosf(f)
            val sin = MyMathUtil.sinf(f)
            val cosk = MyMathUtil.cosf(k*f)
            val x = a*(cosk+b*cos)*cos
            val y = a*(cosk+b*cos)*sin
            pointLst.add(MyPointF(x,y))
        }
        /*
        (0..360 step 1).forEach {
            val f = it.toFloat()
            val cos = MyMathUtil.cosf(f)
            val sin = MyMathUtil.sinf(f)
            val cosk = MyMathUtil.cosf(k*f)
            val x = a*(cosk+b*cos)*cos
            val y = a*(cosk+b*cos)*sin
            pointLst.add(MyPointF(x,y))
        }
        */

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        //notifyCallback?.receive(angle)
    }

    // パスカルの蝸牛形を回転する
    private fun rotatePath() {
        angle += 5f

        // ２回転したら
        // ・元の角度に戻す
        if ( angle > angleMax ) {
            angle = 0f
        }
        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        //notifyCallback?.receive(angle)
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
        // = (左:右=1:3,上下中央)
        val x0 = margin+side/4f
        val y0 = intrinsicHeight/2f

        // X軸を描画(上下中央)
        canvas.save()
        canvas.translate(0f,y0)
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画("マージン＋半径分"右に移動)
        canvas.save()
        canvas.translate(x0,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // 原点(x0,y0)を中心にパスカルの蝸牛形を描く
        canvas.save()
        canvas.translate(x0,y0)

        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
        }


        /*
        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // パスカルの蝸牛形を描く
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
        */

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