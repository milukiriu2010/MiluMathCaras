package milu.kiriu2010.milumathcaras.gui.draw.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 三角形⇔六角形
// -----------------------------------------------------------------
class Triangle2Hexagon01Drawable: MyDrawable() {

    // 描画パターン
    enum class ModePtrn {
        PTRN1,
        PTRN2,
        PTRN3,
        PTRN4,
        PTRN5,
        PTRN6
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 990f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val split = 9f
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
    // 三角形の頂点(DOWN)
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
    // 三角形の頂点(DOWN_RIGHT)
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
    // -------------------------------------
    // 三角形の頂点(DOWN_LEFT)
    // -------------------------------------
    val c0 = MyPointF().also {
        it.x = 0f
        it.y = 0f
    }
    val c1 = MyPointF().also {
        it.x = a
        it.y = 0f
    }
    val c2 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(300f)
        it.y = a*MyMathUtil.sinf(300f)
    }
    val c3 = MyPointF().also {
        it.x = -a
        it.y = 0f
    }
    val c4 = MyPointF().also {
        it.x = a*MyMathUtil.cosf(120f)
        it.y = a*MyMathUtil.sinf(120f)
    }
    // -------------------------------------
    // 三角形の頂点(UP)
    // -------------------------------------
    val d0 = MyPointF().also {
        it.x = 0f
        it.y = 0f
    }
    val d1 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(120f)
        it.y = a* MyMathUtil.sinf(120f)
    }
    val d2 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(60f)
        it.y = a* MyMathUtil.sinf(60f)
    }
    val d3 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(300f)
        it.y = a* MyMathUtil.sinf(300f)
    }
    val d4 = MyPointF().also {
        it.x = a* MyMathUtil.cosf(240f)
        it.y = a* MyMathUtil.sinf(240f)
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
    private var modePtrnNow = ModePtrn.PTRN1

    // 1ターン内の移動比率
    private var ratioNow = 0f
    private val ratioDiv = 0.1f
    private val ratioMax = 1f

    // "描画点の初期位置設定"をしたかどうか
    private var isInitialized = false
    // "描画点の初期位置設定"の実施回数
    private var nCnt = 0

    // 描画点のリスト(偶数行)
    private val vertex0Lst = mutableListOf<Vertex>()
    // 描画点のリスト(奇数行)
    private val vertex1Lst = mutableListOf<Vertex>()

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

        vertex0Lst.clear()
        vertex1Lst.clear()

        // 三角形の移動方向を切り替える
        if ( isInitialized == true ) {
            modePtrnNow = when (modePtrnNow) {
                ModePtrn.PTRN1 -> ModePtrn.PTRN2
                ModePtrn.PTRN2 -> ModePtrn.PTRN3
                ModePtrn.PTRN3 -> ModePtrn.PTRN4
                ModePtrn.PTRN4 -> ModePtrn.PTRN5
                ModePtrn.PTRN5 -> ModePtrn.PTRN6
                ModePtrn.PTRN6 -> ModePtrn.PTRN1
            }
        }

        // "描画点の初期位置設定"をしたかどうか
        if ( isInitialized == false ) {
            isInitialized = true
        }

        // 三角形の移動方向に合わせて、描画点の初期位置・移動方向を決定
        when (modePtrnNow) {
            ModePtrn.PTRN1 -> createPathPtrn1()
            ModePtrn.PTRN2 -> createPathPtrn2()
            ModePtrn.PTRN3 -> createPathPtrn3()
            ModePtrn.PTRN4 -> createPathPtrn4()
            ModePtrn.PTRN5 -> createPathPtrn5()
            ModePtrn.PTRN6 -> createPathPtrn6()
        }

        nCnt++
    }

    // -------------------------------
    // 描画点のパス設定(パターン１)
    // -------------------------------
    private fun createPathPtrn1(color1: Int = Color.RED, color2: Int = Color.GREEN, color3: Int = Color.BLUE ) {
        // R(DOWN)
        val v0 = Vertex().also {
            // 起点位置
            it.slst.add(a0.copy())
            it.slst.add(a0.copy())
            it.slst.add(a1.copy())
            it.slst.add(a2.copy())
            it.slst.add(a0.copy())
            it.slst.add(a0.copy())
            // 終端位置
            it.elst.add(a4.copy())
            it.elst.add(a0.copy())
            it.elst.add(a0.copy())
            it.elst.add(a0.copy())
            it.elst.add(a0.copy())
            it.elst.add(a3.copy())
            // 色
            it.color = color1
            // 横方向シフト
            it.shh = 0f
            // 縦方向シフト
            it.shv = 0f
        }
        // G(UP_RIGHT)
        val v1 = Vertex().also {
            // 起点位置
            it.slst.add(e0.copy())
            it.slst.add(e0.copy())
            it.slst.add(e1.copy())
            it.slst.add(e2.copy())
            it.slst.add(e0.copy())
            it.slst.add(e0.copy())
            // 終端位置
            it.elst.add(e4.copy())
            it.elst.add(e0.copy())
            it.elst.add(e0.copy())
            it.elst.add(e0.copy())
            it.elst.add(e0.copy())
            it.elst.add(e3.copy())
            // 色
            it.color = color2
            // 横方向シフト
            it.shh = a*1.5f
            // 縦方向シフト
            it.shv = -h
        }
        // B(UP_LEFT)
        val v2 = Vertex().also {
            // 起点位置
            it.slst.add(f0.copy())
            it.slst.add(f0.copy())
            it.slst.add(f1.copy())
            it.slst.add(f2.copy())
            it.slst.add(f0.copy())
            it.slst.add(f0.copy())
            // 終端位置
            it.elst.add(f4.copy())
            it.elst.add(f0.copy())
            it.elst.add(f0.copy())
            it.elst.add(f0.copy())
            it.elst.add(f0.copy())
            it.elst.add(f3.copy())
            // 色
            it.color = color3
            // 横方向シフト
            it.shh = a*1.5f
            // 縦方向シフト
            it.shv = -h
        }

        // 偶数行
        vertex0Lst.add(v0.copy())
        vertex0Lst.add(v1.copy())
        vertex0Lst.add(v2.copy())
        // 奇数行
        vertex1Lst.add(v2.copy().also {
            it.shh = 0f
            it.shv = 0f
        })
        vertex1Lst.add(v0.copy().also {
            it.shh = a*1.5f
            it.shv = h
        })
        vertex1Lst.add(v1.copy().also {
            it.shh = a*3f
            it.shv = 0f
        })
    }

    // -------------------------------
    // 描画点のパス設定(パターン２)
    // -------------------------------
    private fun createPathPtrn2(color1: Int = Color.BLUE, color2: Int = Color.GREEN, color3: Int = Color.RED ) {
        // B(DOWN_LEFT)
        val v0 = Vertex().also {
            // 起点位置
            it.slst.add(c0.copy())
            it.slst.add(c0.copy())
            it.slst.add(c1.copy())
            it.slst.add(c2.copy())
            it.slst.add(c0.copy())
            it.slst.add(c0.copy())
            // 終端位置
            it.elst.add(c4.copy())
            it.elst.add(c0.copy())
            it.elst.add(c0.copy())
            it.elst.add(c0.copy())
            it.elst.add(c0.copy())
            it.elst.add(c3.copy())
            // 色
            it.color = color1
            // 横方向シフト
            it.shh = -a
            // 縦方向シフト
            it.shv = 0f
        }
        // G(UP)
        val v1 = Vertex().also {
            // 起点位置
            it.slst.add(d0.copy())
            it.slst.add(d0.copy())
            it.slst.add(d1.copy())
            it.slst.add(d2.copy())
            it.slst.add(d0.copy())
            it.slst.add(d0.copy())
            // 終端位置
            it.elst.add(d4.copy())
            it.elst.add(d0.copy())
            it.elst.add(d0.copy())
            it.elst.add(d0.copy())
            it.elst.add(d0.copy())
            it.elst.add(d3.copy())
            // 色
            it.color = color2
            // 横方向シフト
            it.shh = a*0.5f
            // 縦方向シフト
            it.shv = -h
        }
        // R(DOWN_RIGHT)
        val v2 = Vertex().also {
            // 起点位置
            it.slst.add(b0.copy())
            it.slst.add(b0.copy())
            it.slst.add(b1.copy())
            it.slst.add(b2.copy())
            it.slst.add(b0.copy())
            it.slst.add(b0.copy())
            // 終端位置
            it.elst.add(b4.copy())
            it.elst.add(b0.copy())
            it.elst.add(b0.copy())
            it.elst.add(b0.copy())
            it.elst.add(b0.copy())
            it.elst.add(b3.copy())
            // 色
            it.color = color3
            // 横方向シフト
            it.shh = a*2f
            // 縦方向シフト
            it.shv = 0f
        }

        // 偶数行
        vertex0Lst.add(v0.copy())
        vertex0Lst.add(v1.copy())
        vertex0Lst.add(v2.copy())
        // 奇数行
        vertex1Lst.add(v2.copy().also {
            it.shh = a*0.5f
            it.shv = h
        })
        vertex1Lst.add(v0.copy().also {
            it.shh = a*0.5f
            it.shv = h
        })
        vertex1Lst.add(v1.copy().also {
            it.shh = a*2f
            it.shv = 0f
        })
    }

    // -------------------------------
    // 描画点のパス設定(パターン３)
    // -------------------------------
    private fun createPathPtrn3() {
        createPathPtrn1(Color.BLUE,Color.RED,Color.GREEN)
    }

    // -------------------------------
    // 描画点のパス設定(パターン４)
    // -------------------------------
    private fun createPathPtrn4() {
        createPathPtrn2(Color.GREEN,Color.RED,Color.BLUE)
    }

    // -------------------------------
    // 描画点のパス設定(パターン５)
    // -------------------------------
    private fun createPathPtrn5() {
        createPathPtrn1(Color.GREEN,Color.BLUE,Color.RED)
    }

    // -------------------------------
    // 描画点のパス設定(パターン６)
    // -------------------------------
    private fun createPathPtrn6() {
        createPathPtrn2(Color.RED,Color.BLUE,Color.GREEN)
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
        (0..splitN+4).forEach row@{ row ->
            canvas.save()
            canvas.translate(margin,margin)
            var shiftL = -a*2f
            var shiftU = -h*2f
            canvas.translate(shiftL,shiftU+2f*h*(row/2).toFloat())

            val vertexLst = when (row%2) {
                0 -> vertex0Lst
                1 -> vertex1Lst
                else -> vertex0Lst
            }

            (0..splitN/3+1).forEach col@{ col ->

                vertexLst.forEachIndexed { id1, vertex ->
                    val path = Path()
                    vertex.slst.forEachIndexed { id2, sp ->
                        val ep = vertex.elst[id2]
                        val p = sp.lerp(ep,ratioNow,ratioMax-ratioNow)
                        p.also {
                            it.x += vertex.shh
                            it.y += vertex.shv
                        }
                        when (id2) {
                            0 -> path.moveTo(p.x,p.y)
                            else -> path.lineTo(p.x,p.y)
                        }
                    }
                    path.close()
                    linePaint.color = vertex.color
                    canvas.drawPath(path,linePaint)
                }

                canvas.translate(a*3f,0f)
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
        var elst: MutableList<MyPointF> = mutableListOf(),
        // 色
        var color: Int = Color.RED,
        // 横方向シフト
        var shh: Float = 0f,
        // 縦方向シフト
        var shv: Float = 0f
    )
}