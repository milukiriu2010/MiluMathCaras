package milu.kiriu2010.milumathcaras.gui.draw.polygon.tile

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 三角形⇔ひし形
// -----------------------------------------------------------------
// https://66.media.tumblr.com/6fbe92faf0c058002dfee6c5485a0187/tumblr_mo0n7hepeL1ql82o1o1_r3_500.gif
// -----------------------------------------------------------------
class Triangle2Diamond01Drawable: MyDrawable() {

    // 三角形の向き
    enum class ModeDir {
        UP1,
        UP2,
        DOWN1,
        DOWN2
    }

    // 三角形の分裂方向
    enum class ModeSplit {
        LEFT,
        RIGHT,
        UP,
        DOWN
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
    // 三角形重心⇔頂点の距離
    // -------------------------------
    private val r1 = a*sqrt(3f)/3f
    // -------------------------------
    // 三角形重心⇔各辺の最短距離
    // -------------------------------
    private val r2 = a*sqrt(3f)/6f

    // 頂点
    val a0 = MyPointF().also {
        it.x = r1*MyMathUtil.cosf(330f)
        it.y = r1*MyMathUtil.sinf(330f)
    }
    val a1 = MyPointF().also {
        it.x = r1*MyMathUtil.cosf(210f)
        it.y = r1*MyMathUtil.sinf(210f)
    }
    val a2 = MyPointF().also {
        it.x = r1*MyMathUtil.cosf(210f)
        it.y = r1*MyMathUtil.sinf(210f)
    }
    val b0 = MyPointF().also {
        it.x = r2*MyMathUtil.cosf(270f)
        it.y = r2*MyMathUtil.sinf(270f)
    }
    val b1 = MyPointF().also {
        it.x = r2*MyMathUtil.cosf(150f)
        it.y = r2*MyMathUtil.sinf(150f)
    }
    val b2 = MyPointF().also {
        it.x = r2*MyMathUtil.cosf(30f)
        it.y = r2*MyMathUtil.sinf(30f)
    }
    val c0 = MyPointF().also {
        it.x = r1*MyMathUtil.cosf(270f)
        it.y = r1*MyMathUtil.sinf(270f)
    }
    val c1 = MyPointF().also {
        it.x = r1*MyMathUtil.cosf(150f)
        it.y = r1*MyMathUtil.sinf(150f)
    }
    val c2 = MyPointF().also {
        it.x = r1*MyMathUtil.cosf(30f)
        it.y = r1*MyMathUtil.sinf(30f)
    }



    // 現在の三角形の向き
    private var modeDirNow = ModeDir.DOWN1

    // 1ターン内の移動比率
    private var ratioNow = 0f
    private val ratioDiv = 0.1f
    private val ratioMax = 1f

    // "描画点の初期位置設定"の実施回数
    private var nCnt = 0

    //   DOWN1:5点
    //   UP1  :5点
    //   DOWN2:7点
    //   UP2  :7点
    // 描画点のリスト(パターン０)
    private val vertex0Lst = mutableListOf<Vertex>()

    // 描画点のリスト(パターン２)
    private val vertex2Lst = mutableListOf<Vertex>()

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
        //color = 0xffff7f00.toInt()
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val dmyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
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

    // -------------------------------
    // 描画点のパス設定
    // -------------------------------
    private fun createPath() {
        // 移動比率=0の場合、描画点リストを構築しなおす
        if ( ratioNow != 0f ) return

        vertex0Lst.clear()


        // 三角形の向きを切り替える
        /*
        if (nCnt > 1) {
            modeDirNow = when (modeDirNow) {
                ModeDir.DOWN1 -> ModeDir.DOWN2
                ModeDir.DOWN2 -> ModeDir.UP1
                ModeDir.UP1 -> ModeDir.UP2
                ModeDir.UP2 -> ModeDir.DOWN1
                else -> ModeDir.DOWN1
            }
        }
        */


        // -------------------------------
        // 描画点のパス設定
        // -------------------------------
        when (modeDirNow) {
            ModeDir.DOWN1 -> createPathDOWN1()
        }

        nCnt++
    }

    // -------------------------------
    // 描画点のパス設定(DOWN1)
    // -------------------------------
    private fun createPathDOWN1() {
        // １行目１つ目
        val v1 = Vertex().also {
            it.ms = ModeSplit.UP
            // 起点位置
            it.slst.add(b0.copy())  // 0
            it.slst.add(a1.copy())  // 1
            it.slst.add(a2.copy())  // 2
            it.slst.add(a0.copy())  // 3
            it.slst.add(b0.copy())  // 4
            // 終端位置
            it.elst.add(c0.copy())  // 0
            it.elst.add(a1.copy())  // 1
            it.elst.add(MyPointF()) // 2
            it.elst.add(a0.copy())  // 3
            it.elst.add(c0.copy())  // 4
        }
        // １行目２つ目
        val v2 = Vertex().also {
            it.ms = ModeSplit.RIGHT
            // 起点位置
            it.slst.add(b2.copy())  // 0
            it.slst.add(a0.copy())  // 1
            it.slst.add(a1.copy())  // 2
            it.slst.add(a2.copy())  // 3
            it.slst.add(b2.copy())  // 4
            // 終端位置
            it.elst.add(c2.copy())  // 0
            it.elst.add(a0.copy())  // 1
            it.elst.add(MyPointF()) // 2
            it.elst.add(a2.copy())  // 3
            it.elst.add(c2.copy())  // 4
        }
        // １行目３つ目
        val v3 = Vertex().also {
            it.ms = ModeSplit.UP
            // 起点位置
            it.slst.add(b0.copy())  // 0
            it.slst.add(a1.copy())  // 1
            it.slst.add(a2.copy())  // 2
            it.slst.add(a0.copy())  // 3
            it.slst.add(b0.copy())  // 4
            // 終端位置
            it.elst.add(c0.copy())  // 0
            it.elst.add(a1.copy())  // 1
            it.elst.add(MyPointF()) // 2
            it.elst.add(a0.copy())  // 3
            it.elst.add(c0.copy())  // 0
        }
        // １行目４つ目
        val v4 = Vertex().also {
            it.ms = ModeSplit.RIGHT
            // 起点位置
            it.slst.add(b2.copy())  // 0
            it.slst.add(a0.copy())  // 1
            it.slst.add(a1.copy())  // 2
            it.slst.add(a2.copy())  // 3
            it.slst.add(b2.copy())  // 4
            // 終端位置
            it.elst.add(c2.copy())  // 0
            it.elst.add(a0.copy())  // 1
            it.elst.add(MyPointF()) // 2
            it.elst.add(a2.copy())  // 3
            it.elst.add(c2.copy())  // 4
        }
        // １行目５つ目
        val v5 = Vertex().also {
            it.ms = ModeSplit.UP
            // 起点位置
            it.slst.add(b0.copy())  // 0
            it.slst.add(a1.copy())  // 1
            it.slst.add(a2.copy())  // 2
            it.slst.add(a0.copy())  // 3
            it.slst.add(b0.copy())  // 4
            // 終端位置
            it.elst.add(c0.copy())  // 0
            it.elst.add(a1.copy())  // 1
            it.elst.add(MyPointF()) // 2
            it.elst.add(a0.copy())  // 3
            it.elst.add(c0.copy())  // 4
        }
        // １行目６つ目
        val v6 = Vertex().also {
            it.ms = ModeSplit.LEFT
            // 起点位置
            it.slst.add(b1.copy())  // 0
            it.slst.add(a2.copy())  // 1
            it.slst.add(a0.copy())  // 2
            it.slst.add(a1.copy())  // 3
            it.slst.add(b1.copy())  // 4
            // 終端位置
            it.elst.add(c1.copy())  // 0
            it.elst.add(a2.copy())  // 1
            it.elst.add(MyPointF()) // 2
            it.elst.add(a1.copy())  // 3
            it.elst.add(c1.copy())  // 4
        }

        vertex0Lst.add(v1)
        vertex0Lst.add(v2)
        vertex0Lst.add(v3)
        vertex0Lst.add(v4)
        vertex0Lst.add(v5)
        vertex0Lst.add(v6)
    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {

        //ratioNow += ratioDiv
        ratioNow = 0.1f
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


        // 三角形/ひし形を描画
        (0..splitN+2).forEach { row ->
            canvas.save()
            canvas.translate(margin,margin)
            canvas.translate(-a,a*(row-1).toFloat())

            val vertexLst = when (row%3) {
                0 -> vertex0Lst
                else -> vertex0Lst
            }

            //Log.d(javaClass.simpleName,"vertexLst.size[${vertexLst.size}]")

            (0..splitN+2).forEach { col ->
                canvas.translate(a,0f)

                val vLst = vertexLst.filterIndexed { id, vertex -> ( id == (col%3)*2 ) or ( id == (col%3)*2+1 ) }
                Log.d(javaClass.simpleName,"vLst.size[${vLst.size}]")

                vLst.forEach { vertex ->
                    val path = Path()
                    vertex.slst.forEachIndexed { id, sp ->
                        val ep = vertex.elst[id]
                        //val p = sp.lerp(ep,ratioNow,ratioMax-ratioNow)
                        val p = sp
                        when (id) {
                            0 -> path.moveTo(p.x,p.y)
                            else -> path.lineTo(p.x,p.y)
                        }

                        if ( ( row == 0 ) and ( col == 0 ) ) {
                            Log.d(javaClass.simpleName,"id[${id}]x[${p.x}]y[${p.y}]")
                        }
                        //canvas.drawCircle(sp.x,sp.y,10f,dmyPaint)
                    }
                    path.close()
                    canvas.drawPath(path,linePaint)
                }

            }

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
        // 分裂方向
        var ms: ModeSplit = ModeSplit.DOWN,

        // 頂点の起点位置
        var slst: MutableList<MyPointF> = mutableListOf(),
        // 頂点の終端位置
        var elst: MutableList<MyPointF> = mutableListOf()
    )
}