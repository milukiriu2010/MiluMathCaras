package milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// ---------------------------------------------
// 頂点が少ない多角形を頂点が多い多角形内で回転させる
// ---------------------------------------------
class PolygonInPolygon01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 多角形が内接する円の半径
    // ---------------------------------
    private val r = side/2f

    // ---------------------------------
    // 頂点が多い多角形(回転しない方の多角形)
    // ---------------------------------
    private val polygonA = Polygon().apply {
        vertexCnt = 4
    }

    // ---------------------------------
    // 頂点が少ない多角形(回転する方の多角形)
    // ---------------------------------
    private val polygonB = Polygon().apply {
        vertexCnt = 3
    }

    // ---------------------------------
    // 回転の中心となる頂点のインデックス
    // ---------------------------------
    private var vertexC = 1

    // ---------------------------------
    // 回転の角度
    // ---------------------------------
    private var angle = 0f
    private var angleDiv = 0f
    private var angleMax = 0f

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
    // 多角形を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
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
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:回転しない多角形の頂点の数
    // 第２引数:回転する　多角形の頂点の数
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 可変変数 values を初期値として、このクラスで使う変数に当てはめる
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 回転しない多角形の頂点の数
                0 -> polygonA.vertexCnt = fl.toInt()
                // 回転する　多角形の頂点の数
                1 -> polygonB.vertexCnt = fl.toInt()
            }
        }

        // 多角形の頂点の初期位置設定
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 多角形を回転する
                rotatePolygonB()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                handler.postDelayed(runnable, 100)
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
    // 多角形の頂点の初期位置設定
    // -------------------------------
    private fun createPath() {
        polygonA.pointLst.clear()
        polygonB.pointLst.clear()

        // ---------------------------
        // 回転しない多角形の内角を設定
        // ---------------------------
        polygonA.calInternalAngle()

        // ---------------------------
        // 回転しない多角形の頂点を設定
        // ---------------------------
        (0 until polygonA.vertexCnt).forEach {
            val tA = 2f*PI*it.toFloat()/polygonA.vertexCnt.toFloat()
            val xA = r*cos(tA).toFloat()
            val yA = r*sin(tA).toFloat()
            polygonA.pointLst.add(MyPointF(xA,yA))
        }
        // ---------------------------
        // 回転しない多角形の１辺の長さ
        // ---------------------------
        polygonA.calLen()

        // ---------------------------
        // 回転しない多角形の内角を設定
        // ---------------------------
        polygonB.calInternalAngle()
        // ---------------------------
        // 回転する多角形の１辺の長さ
        // ---------------------------
        polygonB.len = polygonA.len
        // 回転する多角形の辺同士の角度
        //val t = 180f-(360f/polygonB.vertexCnt.toFloat())
        // ---------------------------
        // 回転する多角形の頂点の
        // １番めと２番めは
        // 回転しない多角形の頂点と同一
        // ---------------------------
        polygonB.pointLst.add(polygonA.pointLst[0].copy())
        polygonB.pointLst.add(polygonA.pointLst[1].copy())
        // ---------------------------
        // 回転する多角形の頂点を設定
        // ---------------------------
        (2 until polygonB.vertexCnt).forEach {
            // 2つ前の頂点
            val v0 = polygonB.pointLst[it-2]
            // 1つ前の頂点
            val v1 = polygonB.pointLst[it-1]

            // 前の辺の角度
            val tP = MyMathUtil.getAngle(v0,v1)
            // 次の頂点
            val tB = ((360f-polygonB.internalAngle)+(tP+180f))*2f*PI/360f
            val x2 = v1.x + polygonA.len*cos(tB).toFloat()
            val y2 = v1.y + polygonA.len*sin(tB).toFloat()
            polygonB.pointLst.add(MyPointF(x2,y2))
        }

        // 回転の最大角度
        //  = 回転しない多角形の内角 - 回転する多角形の内角
        angleMax = polygonA.internalAngle - polygonB.internalAngle
        // 回転にあたり刻む角度
        angleDiv = angleMax/5f
    }

    // -------------------------------
    // 多角形を回転する
    // -------------------------------
    private fun rotatePolygonB() {
        angle += angleDiv

        // 回転の中心となる頂点
        val c = polygonB.pointLst[vertexC]

        polygonB.pointLst.forEachIndexed label@{ index, myPointF ->
            if (index == vertexC) return@label

            val x0 = myPointF.x - c.x
            val y0 = myPointF.y - c.y
            //val len0 = polygonB.len
            val len0 = sqrt(x0*x0+y0*y0)
            val angle0 = MyMathUtil.getAngle(c,myPointF)

            //val angle1 = ((360f-polygonB.internalAngle)+angle0-angle)*2f*PI/180f
            val angle1 = (angle0-angle)*PI/180f
            val x1 = c.x + len0 * cos(angle1).toFloat()
            val y1 = c.y + len0 * sin(angle1).toFloat()
            myPointF.x = x1
            myPointF.y = y1
        }

        // 最大角度まで達したら、回転に使う頂点を隣に移す
        if ( angle == angleMax ) {
            vertexC = (vertexC+1)%polygonB.vertexCnt
            angle = 0f
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
        val x0 = (intrinsicWidth/2).toFloat()
        val y0 = (intrinsicHeight/2).toFloat()

        // 原点(x0,y0)を中心に円・三角形を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 回転しない多角形を描く
        val pathA = Path()
        polygonA.pointLst.forEachIndexed { index, myPointF ->
            when (index) {
                0 -> pathA.moveTo(myPointF.x,myPointF.y)
                else -> pathA.lineTo(myPointF.x,myPointF.y)
            }

        }
        pathA.close()
        canvas.drawPath(pathA,linePaint)

        // 回転する多角形を描く
        val pathB = Path()
        polygonB.pointLst.forEachIndexed { index, myPointF ->
            when (index) {
                0 -> pathB.moveTo(myPointF.x,myPointF.y)
                else -> pathB.lineTo(myPointF.x,myPointF.y)
            }

        }
        pathB.close()
        canvas.drawPath(pathB,linePaint)

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.setScale(1f,-1f)
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

    private class Polygon{
        // 多角形の頂点の数
        var vertexCnt = 3
        // 多角形の内角
        var internalAngle = 60f
        // 多角形の一辺の長さ
        var len = 0f
        // 多角形の頂点リスト
        val pointLst: MutableList<MyPointF> = mutableListOf()

        // 多角形の頂点の数から内角を計算する
        fun calInternalAngle() {
            internalAngle = 180f-(360f/vertexCnt.toFloat())
        }

        // 多角形の一辺の長さを頂点リストから計算する
        fun calLen() {
            val x0 = pointLst[0].x
            val y0 = pointLst[0].y
            val x1 = pointLst[1].x
            val y1 = pointLst[1].y
            len = sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))
        }
    }
}