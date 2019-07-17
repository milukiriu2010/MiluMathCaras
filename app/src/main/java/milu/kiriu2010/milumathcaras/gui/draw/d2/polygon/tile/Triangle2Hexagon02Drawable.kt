package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

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
    private val side = 900f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val split = 9f
    private val splitN = split.toInt()
    // -------------------------------
    // 三角形一辺の長さ
    // -------------------------------
    private val a = side/split/3

    // -------------------------------------
    // 三角形の頂点(開いた状態:A)
    // -------------------------------------
    val a00 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(0f)
        it.y = a * MyMathUtil.sinf(0f)
    }
    val a01 = MyPointF().also {
        it.x = 2f*a * MyMathUtil.cosf(30f)
        it.y = 2f*a * MyMathUtil.sinf(30f)
    }
    val a02 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(60f)
        it.y = a * MyMathUtil.sinf(60f)
    }
    // -------------------------------------
    // 三角形の頂点(開いた状態:B)
    // -------------------------------------
    val b00 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(60f)
        it.y = a * MyMathUtil.sinf(60f)
    }
    val b01 = MyPointF().also {
        it.x = 2f*a * MyMathUtil.cosf(90f)
        it.y = 2f*a * MyMathUtil.sinf(90f)
    }
    val b02 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(120f)
        it.y = a * MyMathUtil.sinf(120f)
    }
    // -------------------------------------
    // 三角形の頂点(開いた状態:C)
    // -------------------------------------
    val c00 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(120f)
        it.y = a * MyMathUtil.sinf(120f)
    }
    val c01 = MyPointF().also {
        it.x = 2f*a * MyMathUtil.cosf(150f)
        it.y = 2f*a * MyMathUtil.sinf(150f)
    }
    val c02 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(180f)
        it.y = a * MyMathUtil.sinf(180f)
    }
    // -------------------------------------
    // 三角形の頂点(閉じた状態:D)
    // -------------------------------------
    val d00 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(180f)
        it.y = a * MyMathUtil.sinf(180f)
    }
    val d01 = MyPointF().also {
        it.x = 2f*a * MyMathUtil.cosf(210f)
        it.y = 2f*a * MyMathUtil.sinf(210f)
    }
    val d02 = MyPointF().also {
        it.x = a * MyMathUtil.cosf(240f)
        it.y = a * MyMathUtil.sinf(240f)
    }
    // -------------------------------------
    // 三角形の頂点(UP_RIGHT)
    // -------------------------------------
    val e0 = MyPointF().also {
        it.x = 0f
        it.y = 0f
    }
    val e1 = MyPointF().also {
        it.x = -a
        it.y = 0f
    }
    val e2 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(120f)
        it.y = a*MyMathUtil.sinf(120f)
    }
    val e3 = MyPointF().also {
        it.x = a
        it.y = 0f
    }
    val e4 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(300f)
        it.y = a*MyMathUtil.sinf(300f)
    }
    // -------------------------------------
    // 三角形の頂点(UP_LEFT)
    // -------------------------------------
    val f0 = MyPointF().also {
        it.x = 0f
        it.y = 0f
    }
    val f1 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(60f)
        it.y = a*MyMathUtil.sinf(60f)
    }
    val f2 = MyPointF().also {
        it.x = a
        it.y = 0f
    }
    val f3 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(240f)
        it.y = a*MyMathUtil.sinf(240f)
    }
    val f4 = MyPointF().also {
        it.x = -a
        it.y = 0f
    }

    // 現在の描画パターン
    private var modeNow = Mode.TRI2HEX

    // "描画点の初期位置設定"をしたかどうか
    private var isInitialized = false

    // 描画点のリスト
    private val vertexLst = mutableListOf<Vertex>()

    // 移動比率
    private val ratios = floatArrayOf(
        0.0f,0.0f,0.0f,0.0f,0.0f,
        0.2f,0.4f,0.6f,0.8f,1.0f)

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
            vertexLst.clear()


        }
        else {

        }
    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {
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

        // これまでの描画はテンポラリなので、実体にコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
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

    // --------------------------------------
    // 描画点
    // --------------------------------------
    private data class Vertex(
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
