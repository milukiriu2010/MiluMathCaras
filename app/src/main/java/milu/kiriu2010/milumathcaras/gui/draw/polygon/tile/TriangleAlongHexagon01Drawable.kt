package milu.kiriu2010.milumathcaras.gui.draw.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 六角形に沿って三角形が移動
// -----------------------------------------------------------------
class TriangleAlongHexagon01Drawable: MyDrawable() {

    // 三角形の向き(とがっている方向)
    enum class ModeDir {
        DOWN,
        UP
    }

    // 行先頭の三角形の色
    enum class ModeColor {
        RED,
        GREEN,
        BLUE
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val split = 10f
    private val splitN = split.toInt()
    // -------------------------------
    // 三角形一辺の長さ
    // -------------------------------
    private val a = side/split
    // -------------------------------
    // 三角形の高さ
    // -------------------------------
    private val h = a*sqrt(3f)/2f

    // -------------------------------------
    // 三角形の頂点(下向き)
    // -------------------------------------
    val a0 = MyPointF().also {
        it.x = 0f
        it.y = 0f
    }
    val a1 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(300f)
        it.y = a* MyMathUtil.sinf(300f)
    }
    val a2 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(240f)
        it.y = a* MyMathUtil.sinf(240f)
    }
    val a3 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(120f)
        it.y = a* MyMathUtil.sinf(120f)
    }
    val a4 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(60f)
        it.y = a* MyMathUtil.sinf(60f)
    }
    // -------------------------------------
    // 三角形の頂点(上向き)
    // -------------------------------------
    val b0 = MyPointF().also {
        it.x = 0f
        it.y = 0f
    }
    val b1 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(240f)
        it.y = a*MyMathUtil.sinf(240f)
    }
    val b2 = MyPointF().also {
        it.x = -a
        it.y = 0f
    }
    val b3 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(60f)
        it.y = a*MyMathUtil.sinf(60f)
    }
    val b4 = MyPointF().also {
        it.x = a
        it.y = 0f
    }

    // 現在の三角形の向き
    private var modeDirNow = ModeDir.DOWN
    // 先頭行先頭列の三角形の色
    private var modeColorNow = ModeColor.RED

    // 1ターン内の移動比率
    private var ratioNow = 0f
    private val ratioDiv = 0.1f
    private val ratioMax = 1f

    // "描画点の初期位置設定"の実施回数
    private var nCnt = 0

    // 描画点のリスト
    private val vertexLst = mutableListOf<Vertex>()

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
        // 描画点の初期位置設定
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                /*
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

                    if ( (ratioNow == 0f) or (ratioNow == 0.5f) ) {
                        handler.postDelayed(runnable, 300)
                    }
                    else {
                        handler.postDelayed(runnable, 100)
                    }
                }
                // "停止"状態のときは、更新されないよう処理をスキップする
                else {
                    handler.postDelayed(runnable, 100)
                }
                */
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
        // 移動比率=0の場合、描画点リストを構築しなおす
        if ( ratioNow != 0f ) return


        vertexLst.clear()
        // 三角形の移動方向を切り替える
        if (nCnt > 0) {
            modeDirNow = when (modeDirNow) {
                ModeDir.DOWN -> ModeDir.UP
                ModeDir.UP -> ModeDir.DOWN
            }
        }

        // 三角形の移動方向に合わせて、描画点の初期位置・移動方向を決定
        when (modeDirNow) {
            ModeDir.DOWN        -> createPathDOWN()
            ModeDir.UP          -> createPathUP()
        }

        nCnt++
    }

    // -------------------------------
    // 描画点のパス設定(下向き)
    // -------------------------------
    private fun createPathDOWN() {
        // DOWN
        val v0 = Vertex().also {
            // 起点位置
            it.slst.add(a0.copy())
            it.slst.add(a1.copy())
            it.slst.add(a2.copy())
            it.slst.add(a0.copy())
            // 終端位置
            it.elst.add(a4.copy())
            it.elst.add(a0.copy())
            it.elst.add(a0.copy())
            it.elst.add(a3.copy())
        }








        val v1 = Vertex().also {
            // 起点位置
            it.slst.add(MyPointF(0f,a))
            it.slst.add(MyPointF(a,a))
            it.slst.add(MyPointF(0f,0f))
            it.slst.add(MyPointF(0f,a))
            // 終端位置
            it.elst.add(MyPointF(0f,a))
            it.elst.add(MyPointF(0f,0f))
            it.elst.add(MyPointF(0f,0f))
            it.elst.add(MyPointF(-a,0f))
        }

        vertexLst.add(v0)
        vertexLst.add(v1)

    }

    // -------------------------------
    // 描画点のパス設定(上向き)
    // -------------------------------
    private fun createPathUP() {
        val v0 = Vertex().also {
            // 起点位置
            it.slst.add(MyPointF(a,a))
            it.slst.add(MyPointF(a,0f))
            it.slst.add(MyPointF(0f,a))
            it.slst.add(MyPointF(a,a))
            // 終端位置
            it.elst.add(MyPointF(a,a))
            it.elst.add(MyPointF(0f,a))
            it.elst.add(MyPointF(0f,a))
            it.elst.add(MyPointF(0f,2f*a))
        }

        val v1 = Vertex().also {
            // 起点位置
            it.slst.add(MyPointF(0f,0f))
            it.slst.add(MyPointF(0f,a))
            it.slst.add(MyPointF(a,0f))
            it.slst.add(MyPointF(0f,0f))
            // 終端位置
            it.elst.add(MyPointF(0f,0f))
            it.elst.add(MyPointF(a,0f))
            it.elst.add(MyPointF(a,0f))
            it.elst.add(MyPointF(a,-a))
        }

        vertexLst.add(v0)
        vertexLst.add(v1)
    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {

        ratioNow += ratioDiv
        // 描画点を移動する
        if (ratioNow > ratioMax) {
            ratioNow = 0f
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
        // = (マージン,マージン)

        // 描画点を描く
        (0..splitN+2).forEach row@{ row ->
            canvas.save()
            canvas.translate(margin,margin)
            canvas.translate(-a*2f,h*(row-2).toFloat())
            (0..splitN+2).forEach col@{ col ->
                canvas.translate(a,0f)

                vertexLst.forEach { vertex ->
                    val path = Path()
                    vertex.slst.forEachIndexed { id, sp ->
                        val ep = vertex.elst[id]
                        val p = sp.lerp(ep,ratioNow,ratioMax-ratioNow)
                        when (id) {
                            0 -> path.moveTo(p.x,p.y)
                            else -> path.lineTo(p.x,p.y)
                        }

                    }
                    path.close()
                    canvas.drawPath(path,linePaint)
                }
            }
            // 座標を元に戻す
            canvas.restore()
        }

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
        var elst: MutableList<MyPointF> = mutableListOf()
    )
}