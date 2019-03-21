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
// レムニスケート曲線
// -------------------------------------------------------------------------------------
//   x = a * cos(t)/(1+sin(t)*sin(t))
//   y = a * sin(t)*cos(t)/(1+sin(t)*sin(t))
// -------------------------------------------------------------------------------------
// http://mathworld.wolfram.com/Lemniscate.html
// -------------------------------------------------------------------------------------
class Lemniscate02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // レムニスケート曲線の変数a
    // ---------------------------------
    private var a = side/2f/5f

    // ----------------------------------
    // レムニスケート曲線の位相(変数tに相当)
    // ----------------------------------
    private val split = 72
    private var t = 0f
    private var tD = 360f/split.toFloat()
    private var tM = 360f * 20f

    // -------------------------------
    // レムニスケート曲線の描画点リスト
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
    // レムニスケート曲線を描くペイント
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

    // -------------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -------------------------------------------
    // values
    // 第１引数:レムニスケート曲線の変数tの初期値
    // -------------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 第１引数:レムニスケート曲線の変数tの初期値
        var tI = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 第１引数:レムニスケート曲線の変数tの初期値
                0 -> tI = fl
            }
        }

        // レムニスケート曲線の描画点リストを生成
        while ( t < tI ) {
            createPath()
            increment()
        }
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // レムニスケート曲線の描画点リストを生成
                    createPath()
                    // レムニスケート曲線の媒介変数tの値を増加
                    increment()
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

    // ---------------------------------------
    // レムニスケート曲線の描画点リストを生成
    // ---------------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        if ( t == 0f ) pointLst.clear()

        // 媒介変数tを360で割り、何周目か判定する
        val tI = t.toInt()
        val n = tI/360
        val na = n%5

        // 媒介変数aの大きさを周回数によって変更
        val aN = a * (na+1).toFloat()

        val cos = MyMathUtil.cosf(t+90f)
        val sin = MyMathUtil.sinf(t+90f)
        val x = aN*cos/(1f+sin*sin)
        val y = aN*sin*cos/(1f+sin*sin)

        // 5回転する度に
        // レムニスケート曲線を45度回転する
        val tt = (n/5).toFloat()*45f
        val xx = x * MyMathUtil.cosf(tt) - y * MyMathUtil.sinf(tt)
        val yy = x * MyMathUtil.sinf(tt) + y * MyMathUtil.cosf(tt)

        pointLst.add(MyPointF(xx,yy))
    }

    // ---------------------------------------
    // レムニスケート曲線の媒介変数tの値を増加
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
        // レムニスケート曲線を描く
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

        /*
        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // レムニスケート曲線を描く
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { id, myPointF1 ->
            if ( myPointF2 != null ) {
                val id = (id+t.toInt())%bunchSize
                val color = myColor.create(id,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
        }
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // レムニスケート曲線を描く
        // 1536色のグラデーション
        val bunchSize = split
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { id, myPointF1 ->
            if ( myPointF2 != null ) {
                val color = myColor.create(id%bunchSize,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
        }

        /*
        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // レムニスケート曲線を描く
        // 1536色のグラデーション
        val bunchSize = 20
        val path = Path()
        pointLst.forEachIndexed { id, myPointF ->
            when (id%split) {
                0 -> {
                    path.reset()
                    path.moveTo(myPointF.x,myPointF.y)
                }
                split-1 -> {
                    path.lineTo(myPointF.x,myPointF.y)
                    path.close()
                }
                else -> {
                    path.lineTo(myPointF.x,myPointF.y)
                }
            }
            val color = myColor.create(id%split,bunchSize)
            linePaint.color = color.toInt()
            canvas.drawPath(path,linePaint)
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