package milu.kiriu2010.milumathcaras.gui.draw.curve.lemniscate

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
class CassinianOval02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 300f
    private val margin = 10f

    // ---------------------------------
    // 最大周回数
    // ---------------------------------
    private val nMax = 20f

    // ---------------------------------
    // カッシーニの卵形線の変数a
    // ---------------------------------
    private var a = 100f
    private var bMin = 90f
    private var bMax = 110f
    /*
    private var a = 90f
    private var bMin = 60f
    private var bMax = 120f
    */
    /*
    private var a = 100f
    private var bMin = 50f
    private var bMax = 150f
    */

    // ---------------------------------
    // カッシーニの卵形線の変数b
    // ---------------------------------
    private var b = bMin
    private var bv = (bMax-bMin)/nMax

    // ----------------------------------
    // カッシーニの卵形線の位相(変数tに相当)
    // ----------------------------------
    private val split = 72
    private var t = 0f
    private var tD = 360f/split.toFloat()
    private var tM = 360f * nMax

    // -------------------------------
    // カッシーニの卵形線の描画点リスト
    // -------------------------------
    val pointLst = mutableListOf<MyPointF>()

    // 特異点
    val specialPoint = MyPointF(1000f,1000f)

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
    // カッシーニの卵形線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 1f
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
    // 第１引数:カッシーニの卵形線の変数tの初期値
    // -------------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 第１引数:カッシーニの卵形線の変数tの初期値
        var tI = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 第１引数:カッシーニの卵形線の変数tの初期値
                0 -> tI = fl
            }
        }

        /*
        // カッシーニの卵形線の描画点リストを生成
        while ( t < tI ) {
            createPath()
            increment()
        }
        */
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
                    // カッシーニの卵形線の媒介変数tの値を増加
                    increment()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 10)
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
        if ( t == 0f ) pointLst.clear()

        val tI = t.toInt()
        // 媒介変数tを360で割り、周回数を求める
        val n = tI/360
        // 媒介変数tの360で割った余りを描画角度とする
        val tN = (tI%360).toFloat()

        // 媒介変数bの大きさを周回数によって変更
        b = bMin + bv * n.toFloat()

        val cos1 = MyMathUtil.cosf(tN)
        val sin1 = MyMathUtil.sinf(tN)
        val cos2 = MyMathUtil.cosf(2f*tN)
        val sin2 = MyMathUtil.sinf(2f*tN)

        val pow1 = b/a*b/a*b/a*b/a-sin2*sin2
        if ( pow1 > 0f ) {
            val sqrt1 = sqrt(pow1)
            val plus1 = cos2 + sqrt1
            if ( plus1 > 0f ) {
                val r1 = a*sqrt(plus1 )
                val x1 = r1 * cos1
                val y1 = r1 * sin1
                pointLst.add(MyPointF(x1,y1))
                Log.d(javaClass.simpleName,"t[$t]tN[$tN]r1[$r1]x1[$x1]y[$y1]s1[$sqrt1]p1[$pow1]")
            }
        }
        else {
            pointLst.add(specialPoint)
        }
        /*
        if (b < a) {
            val r2 = a*sqrt(cos2-sqrt((b/a).pow(4f)-sin2.pow(2f)))
            val x2 = r2 * cos1
            val y2 = r2 * sin1
            pointLst.add(MyPointF(x2,y2))
        }
        */
        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(t)
    }

    // ---------------------------------------
    // カッシーニの卵形線の媒介変数tの値を増加
    // ---------------------------------------
    private fun increment() {
        t += tD
        if ( t > tM ) {
            t = 0f
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
        val x0 = intrinsicWidth/2f
        val y0 = intrinsicHeight/2f

        // 原点(x0,y0)を中心に対数螺旋を描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // カッシーニの卵形線を描く
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

        // カッシーニの卵形線を描く
        // 1536色のグラデーション
        val bunchSize = split
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { id, myPointF1 ->
            if ( ( myPointF2 != null ) and (myPointF1 != specialPoint) ) {
                val color = myColor.create(id%bunchSize,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            if (myPointF1 == specialPoint) {
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