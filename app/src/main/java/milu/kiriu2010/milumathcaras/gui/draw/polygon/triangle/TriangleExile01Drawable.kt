package milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// -------------------------------------------
// 三角形でEXILE
// -------------------------------------------
// 三角形を円内で回転させる
// -------------------------------------------
class TriangleExile01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 円の半径
    // ---------------------------------
    private val r = side/2f

    // -------------------------------
    // 描画する三角形のリスト
    // -------------------------------
    private val polygonLst = mutableListOf<Triangle>()

    // -------------------------------
    // 三角形の回転角度
    // -------------------------------
    private var angleMax = 1080f

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
    private val linePaintFrame = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
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

    // ---------------------------------------
    // 描画する三角形のリストを生成
    // ---------------------------------------
    init {
        val a0 = MyPointF(r*cos(0f*PI/180f).toFloat(),r*sin(0f*PI/180f).toFloat())
        val b0 = MyPointF(r*cos(120f*PI/180f).toFloat(),r*sin(120f*PI/180f).toFloat())
        val c0 = MyPointF(r*cos(240f*PI/180f).toFloat(),r*sin(240f*PI/180f).toFloat())
        val d0 = MyPointF(r*cos(180f*PI/180f).toFloat(),r*sin(180f*PI/180f).toFloat())

        val colorLst = intArrayOf(
            0xffffffff.toInt(),
            0xffff0000.toInt(),
            0xff00ff00.toInt(),
            0xff0000ff.toInt()
        )


        colorLst.forEachIndexed { index, color0 ->
            polygonLst.add(Triangle().apply {
                a = a0
                b = b0
                c = c0
                if ( index == 1 ) c = d0
                color = color0
            })
        }
    }

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // values
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 三角形頂点Aを移動
                movePoint()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                /*
                // 最初と最後は1秒後に描画
                if (angle == angleMax || angle == 0f) {
                    handler.postDelayed(runnable, 1000)
                }
                // 100msごとに描画
                else {
                    handler.postDelayed(runnable, 100)
                }
                */
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
    // 三角形頂点Aを移動
    // -------------------------------
    private fun movePoint() {
        val cnt = polygonLst.size
        val phase = 20f

        //Log.d(javaClass.simpleName,"================================")
        polygonLst.forEachIndexed { index, polygon ->
            when (index) {
                0 -> polygon.angle += phase
                else -> {
                    if ( polygonLst[index-1].angle > (polygon.angle+phase) ) {
                        polygon.angle += phase
                    }
                }
            }
            //Log.d(javaClass.simpleName,"index[$index]angle[${polygon.angle}]")

            polygon.a.x = r*cos(polygon.angle*PI/180f).toFloat()
            polygon.a.y = r*sin(polygon.angle*PI/180f).toFloat()
            Log.d(javaClass.simpleName,"id[$index]a[${polygon.a}]angle[${polygon.angle}]")
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

        // 円を描く
        canvas.drawCircle(0f,0f,r,linePaintFrame)

        // 三角形を描く
        //Log.d(javaClass.simpleName,"================================")
        polygonLst.reversed().forEach { polygon ->
            val path = Path()
            path.moveTo(polygon.a.x,polygon.a.y)
            path.lineTo(polygon.b.x,polygon.b.y)
            path.lineTo(polygon.c.x,polygon.c.y)
            path.close()

            //Log.d(javaClass.simpleName,"angle[${polygon.angle}]color[${polygon.color}]")
            //Log.d(javaClass.simpleName,"a[${polygon.a}]angle[${polygon.angle}]")

            linePaint.color = polygon.color
            canvas.drawPath(path,linePaint)
            canvas.drawPath(path,linePaintFrame)
        }


        // 座標を元に戻す
        canvas.restore()

        // 頂点Aを真上にする
        val matrix = Matrix()
        matrix.setRotate(-90f)
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

    private class Triangle{
        // 三角形の頂点A
        lateinit var a: MyPointF
        // 三角形の頂点B
        lateinit var b: MyPointF
        // 三角形の頂点C
        lateinit var c: MyPointF
        // 三角形の色
        var color: Int = 0xff000000.toInt()
        // 三角形の頂点Aの回転角度
        var angle: Float = 0f
    }
}