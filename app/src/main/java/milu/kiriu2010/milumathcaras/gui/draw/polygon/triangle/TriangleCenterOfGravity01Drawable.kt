package milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle

import android.graphics.*
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ------------------------------------------------------
// 三角形の重心
// ------------------------------------------------------
// 定義
//   ３本の中線(頂点と向かい合う辺の中点を結んだ線)の交点
// ------------------------------------------------------
// 重心のベクトル
//   G = (A+B+C)/3
// 重心は各中線を2:1に内分する
// ------------------------------------------------------
class TriangleCenterOfGravity01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------
    // 描画する三角形の頂点リスト
    // -------------------------------
    private val pointLst = mutableListOf<MyPointF>()

    // -------------------------------
    // タッチでつかんだ頂点
    // -------------------------------
    private var grabPoint: MyPointF? = null
    // -------------------------------
    // タッチ点
    // -------------------------------
    private var touchPoint: MyPointF? = null

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
    // 三角形を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // -------------------------------
    // 三角形の頂点と中点を結ぶ線
    // -------------------------------
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = DashPathEffect(floatArrayOf(10f,10f),0f)
    }

    // -------------------------------
    // 三角形の重心を描く
    // -------------------------------
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
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
        // 三角形を生成
        createPolygon()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()
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


    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(event: MotionEvent) {

        // タッチ点に一番近い頂点を動かす
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //Log.d(javaClass.simpleName, "Down")
                touchPoint = MyPointF(event.x,event.y)
                // なんかタッチ位置とずれているから、これで対応
                val basePoint = MyPointF().also {
                    it.x = 2f*event.x - event.rawX
                    it.y = 2f*event.y - event.rawY
                }
                var d = Float.MAX_VALUE
                pointLst.forEachIndexed { id, myPointF ->
                    val dV = myPointF.distance(MyPointF(basePoint.x,basePoint.y))
                    //Log.d(javaClass.simpleName, "d[$dV]X[${myPointF.x}]Y[${myPointF.y}]")
                    if ( dV < d ) {
                        d = dV
                        grabPoint = myPointF
                    }
                }
                //Log.d(javaClass.simpleName, "down:gx[${grabPoint?.x}]gy[${grabPoint?.y}]")
            }
            MotionEvent.ACTION_MOVE -> {
                //Log.d(javaClass.simpleName, "move1:gx[${grabPoint?.x}]gy[${grabPoint?.y}]")
                grabPoint?.let {
                    it.x = it.x + (event.x - touchPoint!!.x )
                    it.y = it.y + (event.y - touchPoint!!.y )
                }
                //Log.d(javaClass.simpleName, "move2:gx[${grabPoint?.x}]gy[${grabPoint?.y}]")
                touchPoint = MyPointF(event.x,event.y)
            }
            MotionEvent.ACTION_UP -> {
                //Log.d(javaClass.simpleName, "up1:gx[${grabPoint?.x}]gy[${grabPoint?.y}]")
                grabPoint?.let {
                    it.x = it.x + (event.x - touchPoint!!.x )
                    it.y = it.y + (event.y - touchPoint!!.y )
                }
                //Log.d(javaClass.simpleName, "up2:gx[${grabPoint?.x}]gy[${grabPoint?.y}]")
                grabPoint = null
                touchPoint = null
            }
        }
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()
    }

    // -------------------------------
    // 三角形を生成
    // -------------------------------
    private fun createPolygon() {
        pointLst.clear()

        // 頂点A
        val a = MyPointF().apply {
            x = ((side/3f).toInt()..(side*2f/3f).toInt()).random().toFloat()
            y = (0..(side/2f).toInt()).random().toFloat()
        }

        // 頂点B
        val b = MyPointF().apply {
            x = (0..(side/3f).toInt()).random().toFloat()
            y = ((side/2f).toInt()..side.toInt()).random().toFloat()
        }

        // 頂点C
        val c = MyPointF().apply {
            x = ((side*2f/3f).toInt()..side.toInt()).random().toFloat()
            y = ((side/2f).toInt()..side.toInt()).random().toFloat()
        }

        pointLst.add(a)
        pointLst.add(b)
        pointLst.add(c)
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
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin

        // 原点(x0,y0)を中心に円・三角形を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 三角形を描く
        val path = Path()
        pointLst.forEachIndexed { id, myPointF ->
            when (id) {
                0 -> path.moveTo(myPointF.x,myPointF.y)
                else -> path.lineTo(myPointF.x,myPointF.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)

        // 頂点Aと中点BCを結ぶ
        // 頂点Bと中点CAを結ぶ
        // 頂点Cと中点ABを結ぶ
        val idx = arrayOf(0,1,2)
        idx.forEach { id ->
            // 中点として使う頂点のインデックス
            val ids = idx.filter { it != id }
            // 中点
            val p = pointLst[ids[0]].mid(pointLst[ids[1]])
            // 頂点と中点を結ぶ
            canvas.drawLine(pointLst[id].x,pointLst[id].y,p.x,p.y,dotPaint)
        }

        // 重心
        val g = pointLst[0].cog(pointLst[1],pointLst[2])
        canvas.drawCircle(g.x,g.y,20f,centerPaint)

        // 座標を元に戻す
        canvas.restore()

        // テンポラリ描画を実体用ビットマップに描画する
        val matrix = Matrix()
        matrix.postScale(1f,1f)
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