package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.triangle

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -------------------------------------------
// 回転しながら三角形を合体
// -------------------------------------------
// https://66.media.tumblr.com/094d3547b2ba27003dff3eaae387a225/tumblr_n13h7v4Ov01r2geqjo1_500.gif
// -------------------------------------------
class TriangleUnite01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------
    // 描画する三角形のリスト
    // -------------------------------
    private val polygonLst = mutableListOf<Triangle>()
    private val polygonOrgLst = mutableListOf<Triangle>()

    // -------------------------------
    // 三角形の回転角度
    // -------------------------------
    private var angleInit = 60f
    private var angle = angleInit
    private var angleMin = 0f
    private val angleDv = 5f
    private val dv = angleInit/angleDv

    // -------------------------------
    // 三角形のスケーリング
    // -------------------------------
    private var scaleInit = 1f
    private var scale = scaleInit
    private var scaleMin = 0f
    private var scaleDv = (scaleInit-scaleMin)/dv

    // -------------------------------
    // 三角形の移動量
    // -------------------------------
    private var transInit = 0f
    private var trans = transInit
    private var transMax = 1f
    private var transDv = (transMax-transInit)/dv

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

    init {
        val rr = side/4f

        // 大将となる三角形
        val triangle0 = Triangle().apply {
            // 三角形の頂点位置を初期設定
            iniVertex( rX = rr, tX = 90f, reX = rr/2f )
        }
        polygonOrgLst.add(triangle0)

        // 子分となる三角形
        (0..2).forEach { i ->
            val triangle1 = Triangle().apply {
                // 中心位置(最初)
                val c = MyPointF().apply {
                    x = rr * 2f * MyMathUtil.cosf(i.toFloat()*120f+30f)
                    y = rr * 2f * MyMathUtil.sinf(i.toFloat()*120f+30f)
                }
                // 中心位置(最後)
                val ce = MyPointF().apply {
                    x = rr/2f * MyMathUtil.cosf(i.toFloat()*120f+90f)
                    y = rr/2f * MyMathUtil.sinf(i.toFloat()*120f+90f)
                }
                // 三角形の頂点位置を初期設定
                iniVertex( cX = c, rX = 0f, tX = 30f, ceX = ce, reX = rr/2f )
            }
            polygonOrgLst.add(triangle1)
        }
    }

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

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 三角形を生成
                    createPolygon()
                    // 三角形を移動
                    movePolygon()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 100msごとに描画
                    handler.postDelayed(runnable, 100)
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

    // -------------------------------
    // 三角形を生成
    // -------------------------------
    private fun createPolygon() {
        if ( angle != angleInit ) return

        polygonLst.clear()

        // 三角形リストを初期状態に戻す
        polygonOrgLst.forEach {
            val triangle = it.copy()
            // 中心位置をコピー
            triangle.c = it.c.copy()
            // 中心位置(最初)をコピー
            triangle.cs = it.cs.copy()
            // 中心位置(最後)をコピー
            triangle.ce = it.ce.copy()
            // 頂点位置をコピー
            it.pointLst.forEachIndexed { id, point ->
                triangle.pointLst[id].x = point.x
                triangle.pointLst[id].y = point.y
            }
            polygonLst.add(triangle)
        }
    }

    // -------------------------------
    // 三角形を移動
    // -------------------------------
    private fun movePolygon() {
        angle -= angleDv
        scale -= scaleDv
        trans += transDv

        /*
        polygonLst.forEachIndexed { id, triangle ->
            when (id) {
                0 -> {
                    // スケーリング
                    triangle.r = triangle.r / (scale+scaleDv) * scale
                    // 回転
                    triangle.t -= angleDv
                    // 三角形の頂点を再計算
                    triangle.calVertex()
                }
                else -> {
                    // 三角形の頂点を再計算
                    triangle.calVertex()
                }
            }
        }
        */

        polygonLst.forEachIndexed { id, triangle ->
            when (id) {
                // 大将
                0 -> {
                    // スケーリング
                    triangle.r = triangle.rs * scale + triangle.re * (1f-scale)
                    // 回転
                    triangle.t -= angleDv
                    // 三角形の頂点を再計算
                    triangle.calVertex()
                }
                // 子分
                else -> {
                    // スケーリング
                    triangle.r = triangle.rs * scale + triangle.re * (1f-scale)
                    // 回転(大将より3倍速く回転する)
                    triangle.t -= 3f*angleDv
                    triangle.c.let {
                        it.x = triangle.cs.x * (1f-trans) + triangle.ce.x * trans
                        it.y = triangle.cs.y * (1f-trans) + triangle.ce.y * trans
                    }
                    // 三角形の頂点を再計算
                    triangle.calVertex()
                }
            }
        }


        if ( angle <= angleMin ) {
            angle = angleInit
            scale = scaleInit
            trans = transInit
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

        // 三角形を描く
        //Log.d(javaClass.simpleName,"********************************")
        Log.d(javaClass.simpleName,"polygonOrgLst.size[${polygonOrgLst.size}]")
        Log.d(javaClass.simpleName,"polygonLst.size[${polygonLst.size}]")
        polygonLst.forEachIndexed { _, polygon ->
            val path = Path()
            polygon.pointLst.forEachIndexed { id2, myPointF ->
                when (id2) {
                    0 -> path.moveTo(myPointF.x,myPointF.y)
                    else -> path.lineTo(myPointF.x,myPointF.y)
                }
            }
            path.close()

            canvas.drawPath(path,linePaint)
        }

        // 座標を元に戻す
        canvas.restore()

        // テンポラリ描画を実体用ビットマップに描画する
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

    // 三角形
    private data class Triangle(
        // 三角形を描く元となる円の中心
        var c: MyPointF = MyPointF(),
        // 三角形を描く元となる円の中心(最初)
        var cs: MyPointF = MyPointF(),
        // 三角形を描く元となる円の中心(最後)
        var ce: MyPointF = MyPointF(),
        // 三角形を描く元となる円の半径
        var r: Float = 0f,
        // 三角形を描く元となる円の半径(最初)
        var rs: Float = 0f,
        // 三角形を描く元となる円の半径(最後)
        var re: Float = 0f,
        // 三角形の回転角度
        var t: Float = 0f,
        // 三角形の頂点
        val pointLst: MutableList<MyPointF> = mutableListOf()
    ) {
        // 三角形の頂点位置を初期設定
        fun iniVertex( cX: MyPointF = MyPointF(), rX: Float = 0f, tX: Float = 0f, ceX: MyPointF = MyPointF(), reX: Float = 0f ) {
            c = cX
            cs = cX.copy()
            ce = ceX.copy()
            r = rX
            rs = rX
            re = reX
            t = tX


            (0..2).forEach { _ ->
                // 三角形の頂点リスト
                (0..2).forEach { i ->
                    val x = c.x + r * MyMathUtil.cosf(i.toFloat()*120f+t)
                    val y = c.y + r * MyMathUtil.sinf(i.toFloat()*120f+t)
                    pointLst.add(MyPointF(x,y))
                }
            }
        }

        // 三角形の頂点位置を計算
        fun calVertex() {
            pointLst.forEachIndexed { i, myPointF ->
                myPointF.x = c.x + r * MyMathUtil.cosf(i.toFloat()*120f+t)
                myPointF.y = c.y + r * MyMathUtil.sinf(i.toFloat()*120f+t)
            }
        }
    }
}