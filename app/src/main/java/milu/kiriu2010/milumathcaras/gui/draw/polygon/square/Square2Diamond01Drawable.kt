package milu.kiriu2010.milumathcaras.gui.draw.polygon.square

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -----------------------------------------------------------------
// 正方形⇔ひし形
// -----------------------------------------------------------------
// https://66.media.tumblr.com/47c47e8bbcb5ee14878548f806a7c086/tumblr_mnor4buGS01r2geqjo1_500.gif
// -----------------------------------------------------------------
class Square2Diamond01Drawable: MyDrawable() {

    // 分裂方向
    enum class ModeSplit {
        HORIZONTAL,
        VERTICAL
    }

    // 点の移動方向
    enum class Direction {
        RIGHT,
        DOWN,
        LEFT,
        UP,
        STILL
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
    // 各描画領域の大きさ
    //   = １ターン内の点の移動距離
    // -------------------------------
    private val sideE = side/split

    // 分裂モード
    private var dirSplit = ModeSplit.VERTICAL

    // 1ターン内の移動比率
    private var ratioNow = 0f
    private val ratioDiv = 0.1f
    private val ratioMax = 1f

    // 描画点のリスト
    //   12点
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
    // 矢印を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
                // "更新"状態
                if ( isPaused == false ) {
                    // 描画点の初期位置設定
                    createPath()
                    // 描画点を移動する
                    movePath()
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
        // 移動比率=0の場合、描画点リストを構築しなおす
        if ( ratioNow != 0f ) return

        vertexLst.clear()

        // 分裂方向を切り替える
        dirSplit = if ( dirSplit == ModeSplit.VERTICAL) ModeSplit.HORIZONTAL else ModeSplit.VERTICAL

        // 分裂方向に合わせて、描画点の初期位置・移動方向を決定
        when (dirSplit) {
            ModeSplit.HORIZONTAL -> {
                val vertex0 = Vertex().also {
                    it.d = Direction.RIGHT
                    it.p = MyPointF(sideE*0.5f,0f)
                }
                val vertex1 = Vertex().also {
                    it.d = Direction.STILL
                    it.p = MyPointF(sideE*0.5f,sideE*0.5f)
                }
                val vertex2 = Vertex().also {
                    it.d = Direction.DOWN
                    it.p = MyPointF(0f,sideE*0.5f)
                }
                val vertex3 = Vertex().also {
                    it.d = Direction.UP
                    it.p = MyPointF(0f,-sideE*0.5f)
                }
                val vertex4 = Vertex().also {
                    it.d = Direction.STILL
                    it.p = MyPointF(sideE*0.5f,-sideE*0.5f)
                }
                val vertex5 = Vertex().also {
                    it.d = Direction.RIGHT
                    it.p = MyPointF(sideE*0.5f,0f)
                }



                vertexLst.add(vertex0)
                vertexLst.add(vertex1)
                vertexLst.add(vertex2)
                vertexLst.add(vertex3)
                vertexLst.add(vertex4)
                vertexLst.add(vertex5)
            }
            ModeSplit.VERTICAL -> {

            }
        }
    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {
        ratioNow + ratioDiv
        if (ratioNow >= ratioMax) {
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
        (-1..splitN).forEach { row ->
            canvas.save()
            canvas.translate(margin,margin)
            canvas.translate(-sideE*1.5f,sideE*(row.toFloat()+0.5f))
            (-1..splitN).forEach { col ->
                canvas.translate(sideE,0f)

                val path = Path()
                vertexLst.forEachIndexed { id, vertex ->
                    when (id) {
                        0 -> path.moveTo(vertex.p.x,vertex.p.y)
                        else -> path.lineTo(vertex.p.x,vertex.p.y)
                    }
                }
                path.close()

                when {
                    // 水平モードの場合、"行＋列=偶数"が初期描画する領域
                    (dirSplit == ModeSplit.HORIZONTAL) and ((row+col)%2==0) -> {
                        canvas.drawPath(path,linePaint)
                    }
                    // 垂直モードの場合、"行＋列=奇数"が初期描画する領域
                    (dirSplit == ModeSplit.VERTICAL) and ((row+col)%2==1) -> {
                        canvas.drawPath(path,linePaint)
                    }
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

    // --------------------------------------
    // 描画点
    // --------------------------------------
    private data class Vertex(
        // 移動方向
        var d: Direction = Direction.RIGHT,
        // 描画点
        var p: MyPointF = MyPointF()
    )
}