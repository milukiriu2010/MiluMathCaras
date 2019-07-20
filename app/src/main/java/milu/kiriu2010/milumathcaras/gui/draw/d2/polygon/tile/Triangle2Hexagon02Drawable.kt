package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 三角形⇔六角形02
// -----------------------------------------------------------------
// 開いた状態(三角形)⇔閉じた状態(六角形)を交互に繰り返す
// -----------------------------------------------------------------
// https://66.media.tumblr.com/9d2270db688ea684dd5e86b6c718e334/tumblr_oqhhibwGeV1r2geqjo1_540.gif
// -----------------------------------------------------------------
// 2019.07.17
// -----------------------------------------------------------------
class Triangle2Hexagon02Drawable: MyDrawable() {

    // 描画パターン
    enum class Mode {
        TRI2HEX,
        HEX2TRI
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 960f
    private val margin = 0f

    // レベル(最大)
    private val lvMax = 6

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val split = 8f
    private val splitN = split.toInt()
    // -------------------------------
    // 三角形一辺の長さ
    // -------------------------------
    private val a = side/split/3

    // "描画点の初期位置設定"をしたかどうか
    private var isInitialized = false

    // 描画する三角形のリスト
    private val triangleLst = mutableListOf<Triangle>()

    // 移動比率
    private val ratios = floatArrayOf(
        0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,
        0.0f,0.2f,0.4f,0.6f,0.8f,1.0f)

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
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 頂点を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff19b5fe.toInt()
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
    // 可変変数 values の引数位置による意味合い
    //
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // "描画点の初期位置設定"をしたかどうか
        isInitialized = false
        // 描画点の初期位置設定
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
                    // 描画点を移動する
                    movePath()
                    // 描画点の初期位置設定
                    createPath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

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
    // 描画点の初期位置設定
    // -------------------------------
    private fun createPath() {
        // "描画点の初期位置設定"をしたかどうか
        // 設定していない⇒初期化を実施
        // 設定している  ⇒移動比率の位置により、モードを変更
        if ( isInitialized == false ) {
            triangleLst.clear()
            isInitialized = true

            // -------------------------------
            // 三角形の初期化
            // -------------------------------
            (0..lvMax).forEach {
                initPath(it)
            }
        }
        else {
        }
    }

    // -------------------------------
    // 三角形の初期化
    // -------------------------------
    private fun initPath(lv: Int) {
        // ６つの三角形
        (0..5).forEach { i ->
            val ii = i.toFloat()

            val triangle = Triangle()
            // 三角形の頂点
            (0..2).forEach { j ->
                val jj = j.toFloat()

                // 開始点
                val s0 = if (j%2 == 0) a else sqrt(3f)*a
                val ps = MyPointF().also {
                    it.x = s0 * MyMathUtil.cosf(60f*ii+30f*jj)
                    it.y = s0 * MyMathUtil.sinf(60f*ii+30f*jj)
                }
                triangle.slst.add(ps)

                // 終了点
                val e0 = if (j == 0) 0f else a
                val pe = MyPointF().also {
                    it.x = e0 * MyMathUtil.cosf(60f*ii+60f*jj)
                    it.y = e0 * MyMathUtil.sinf(60f*ii+60f*jj)
                }
                triangle.elst.add(pe)
            }

            // 描画モード
            triangle.mode = Mode.TRI2HEX
            // 色
            triangle.color = if (i%2==0) 0xff19b5fe.toInt() else Color.WHITE
            // 移動比率の位置
            triangle.ratioId = lvMax-lv

            triangleLst.add(triangle)
        }
    }


    // -------------------------------
    // 三角形を移動する
    // -------------------------------
    private fun movePath() {
        triangleLst.forEachIndexed { id, triangle ->
            if (triangle.ratioId >= (ratios.size-1)) {
                val ii = (id%6).toFloat()

                // モードを変更する＆三角形の始点・終点を変更
                when (triangle.mode) {
                    Mode.TRI2HEX -> createHex2Tri(triangle,ii)
                    Mode.HEX2TRI -> createTri2Hex(triangle,ii)
                }

            }
            else {
                // 移動比率の位置をインクリメントする
                triangle.ratioId++
            }
        }
    }

    // ---------------------------------------
    // 三角形再生成
    // 三角形⇒六角形
    // ---------------------------------------
    private fun createTri2Hex(triangle: Triangle, ii: Float) {
        triangle.slst.clear()
        triangle.elst.clear()

        // 三角形の頂点
        (0..2).forEach { j ->
            val jj = j.toFloat()

            // 開始点
            val s0 = if (j%2 == 0) a else sqrt(3f)*a
            val ps = MyPointF().also {
                it.x = s0 * MyMathUtil.cosf(60f*ii+30f*jj)
                it.y = s0 * MyMathUtil.sinf(60f*ii+30f*jj)
            }
            triangle.slst.add(ps)

            // 終了点
            val e0 = if (j == 0) 0f else a
            val pe = MyPointF().also {
                it.x = e0 * MyMathUtil.cosf(60f*ii+60f*jj)
                it.y = e0 * MyMathUtil.sinf(60f*ii+60f*jj)
            }
            triangle.elst.add(pe)
        }

        // 描画モード
        triangle.mode = Mode.TRI2HEX
        // 移動比率の位置を初期化
        triangle.ratioId = 0
    }

    // ---------------------------------------
    // 三角形再生成
    // 六角形⇒三角形
    // ---------------------------------------
    private fun createHex2Tri(triangle: Triangle,ii: Float) {
        triangle.slst.clear()
        triangle.elst.clear()

        // 三角形の頂点
        (0..2).forEach { j ->
            val jj = j.toFloat()

            // 開始点
            val s0 = if (j == 0) 0f else a
            val ps = MyPointF().also {
                it.x = s0 * MyMathUtil.cosf(60f*ii+60f*jj)
                it.y = s0 * MyMathUtil.sinf(60f*ii+60f*jj)
            }
            triangle.slst.add(ps)

            // 終了点
            val e0 = if (j%2 == 0) a else sqrt(3f)*a
            val pe = MyPointF().also {
                it.x = e0 * MyMathUtil.cosf(60f*ii+30f*jj)
                it.y = e0 * MyMathUtil.sinf(60f*ii+30f*jj)
            }
            triangle.elst.add(pe)
        }

        // 描画モード
        triangle.mode = Mode.HEX2TRI
        // 移動比率の位置を初期化
        triangle.ratioId = 0
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
        // = (中央,中央)
        val x0 = intrinsicWidth.toFloat()*0.5f
        val y0 = intrinsicHeight.toFloat()*0.5f

        canvas.save()
        canvas.translate(x0,y0)

        // レベル(最大)
        //val lvMax = triangleLst.size/6-1

        // レベルごとに描画
        (0..lvMax).forEach { lv0 ->
            canvas.save()

            // レベル１以上
            if (lv0 > 0) {
                // 正六角形の座標シフト
                (0..5).forEach { si0 ->
                    // -----------------------------
                    // 正六角形の座標シフト
                    // 頂点
                    // -----------------------------
                    val sit0 = si0.toFloat()
                    val sx0 = lv0*3f*a*MyMathUtil.cosf(60f*sit0)
                    val sy0 = lv0*3f*a*MyMathUtil.sinf(60f*sit0)

                    canvas.save()
                    canvas.translate(sx0,sy0)

                    // ６つずつ三角形を描画する
                    drawTriangle(canvas,lv0)

                    canvas.restore()

                    // -----------------------------
                    // レベル２以上
                    // -----------------------------
                    // 正六角形の座標シフト
                    // 頂点間の補間
                    // -----------------------------
                    val sit1 = (si0+1).toFloat()
                    val sx1 = lv0*3f*a*MyMathUtil.cosf(60f*sit1)
                    val sy1 = lv0*3f*a*MyMathUtil.sinf(60f*sit1)
                    val nN = lv0
                    val nNt = nN.toFloat()
                    val p0 = MyPointF(sx0,sy0)
                    val p1 = MyPointF(sx1,sy1)
                    (1..(lv0-1)).forEach { nn ->
                        val nnt = nn.toFloat()
                        val p2 = p0.lerp(p1,nnt,nNt-nnt)
                        canvas.save()
                        canvas.translate(p2.x,p2.y)

                        // ６つずつ三角形を描画する
                        drawTriangle(canvas,lv0)

                        canvas.restore()
                    }
                }
            }
            // レベル０
            else {
                // ６つずつ三角形を描画する
                drawTriangle(canvas,lv0)
            }

            canvas.restore()
        }

        canvas.restore()

        // これまでの描画はテンポラリなので、実体にコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // 三角形を描画
    private fun drawTriangle(canvas: Canvas,lv0: Int) {
        (0..5).forEach { lv1 ->
            val triangle = triangleLst[lv0*6+lv1]

            // 移動比率
            val ratio = ratios[triangle.ratioId]
            // 三角形の色
            linePaint.color = triangle.color

            val path = Path()
            (0..2).forEach { i ->
                // 始点
                val ps = triangle.slst[i]
                // 終点
                val pe = triangle.elst[i]
                // 移動後の点の位置
                val p = ps.lerp(pe,ratio,1f-ratio)

                if (i==0) {
                    path.moveTo(p.x,p.y)
                }
                else {
                    path.lineTo(p.x,p.y)
                }
            }
            path.close()
            canvas.drawPath(path,linePaint)
        }
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

    // --------------------------------------
    // 三角形
    // --------------------------------------
    private data class Triangle(
        // 頂点の起点位置
        var slst: MutableList<MyPointF> = mutableListOf(),
        // 頂点の終端位置
        var elst: MutableList<MyPointF> = mutableListOf(),
        // 描画モード
        var mode: Mode = Mode.TRI2HEX,
        // 色
        var color: Int = Color.RED,
        // 移動比率の位置
        var ratioId: Int = 0
    )
}
