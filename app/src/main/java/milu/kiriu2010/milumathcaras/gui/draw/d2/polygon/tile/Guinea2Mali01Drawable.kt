package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// ギニアの国旗⇔マリの国旗01
// ---------------------------------------------
// 2019.08.28
// ---------------------------------------------
class Guinea2Mali01Drawable: MyDrawable() {

    enum class Mode {
        PH1,
        PH2,
        PH3,
        PH4,
        PH5,
        PH6
    }

    // 現在のモード
    private var modeNow = Mode.PH1

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1500f
    private val margin = 0f

    // 領域分割
    private val splitW  = 5f
    private val splitWN = splitW.toInt()
    private val splitH  = 7.5f
    private val splitHN = (splitH.toInt()+1)

    // ---------------------------------
    // 国旗
    // ---------------------------------
    //private val flagW = side/splitW
    private val flagW = 300f
    private val flagWX = flagW*0.5f
    private val flagW1 = 270f
    private val flagW2 = flagW1*0.5f
    private val flagW3 = flagW1/3f
    private val flagW6 = flagW1/6f
    //private val flagH = side/splitH
    private val flagH = 210f
    private val flagHX = flagH*0.5f
    private val flagH1 = 180f
    private val flagH2 = flagH1*0.5f

    // 境界
    private val bw = 36f

    // 国旗の移動比率
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private val ratioDv = 0.1f
    private val angle = 90f

    // パスの初期化を実施したかどうか
    private var isInitialized = false

    // ギニア国旗の四角形リスト
    private val square0Lst = mutableListOf<Square>()

    // マリ国旗の四角形リスト
    private val square1Lst = mutableListOf<Square>()


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

    // ---------------------------------
    // ペイント赤
    // ---------------------------------
    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    // -------------------------------
    // ペイント黄色
    // -------------------------------
    private val yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // ペイント緑
    // ---------------------------------
    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff008800.toInt()
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // ペイント黒
    // ---------------------------------
    private val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // パスの初期化
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            // パスの初期化を実施したかどうか
            isInitialized = false
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // パスの初期化
                    createPath()
                    // 移動する
                    movePath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    if (ratioNow >= ratioMax) {
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
    // パスの初期化
    // -------------------------------
    private fun createPath() {
        if ( (ratioNow > ratioMin) and (ratioNow < ratioMax) ) return

        ratioNow = ratioMin

        // モードを設定
        if (isInitialized) {
            modeNow = when (modeNow) {
                Mode.PH1 -> Mode.PH2
                Mode.PH2 -> Mode.PH3
                Mode.PH3 -> Mode.PH4
                Mode.PH4 -> Mode.PH5
                Mode.PH5 -> Mode.PH6
                Mode.PH6 -> Mode.PH1
            }
        }

        // パス生成
        when (modeNow) {
            Mode.PH1 -> createPathPH1()
            Mode.PH2 -> createPathPH2()
            Mode.PH3 -> createPathPH3()
            Mode.PH4 -> createPathPH4()
            Mode.PH5 -> createPathPH5()
            Mode.PH6 -> createPathPH6()
        }


        // パスの初期化を実施したかどうか
        isInitialized = true
    }

    // パス生成(PH1)
    private fun createPathPH1() {
        square0Lst.clear()
        square1Lst.clear()

        // 右下
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW2,0f))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 右上
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 左上
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 左下
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })
    }

    // パス生成(PH2)
    private fun createPathPH2() {
        square0Lst.clear()
        square1Lst.clear()

        // 右上
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 上
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 左下
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })


        // 下
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })
    }

    // パス生成(PH3)
    private fun createPathPH3() {
        square0Lst.clear()
        square1Lst.clear()

        // 上
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 左上
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 下
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 右下
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })

    }

    // パス生成(PH4)
    private fun createPathPH4() {
        square0Lst.clear()
        square1Lst.clear()

        // 左上
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 左下
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW2,0f))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 右下
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 右上
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW2,0f))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })

    }

    // パス生成(PH5)
    private fun createPathPH5() {
        square0Lst.clear()
        square1Lst.clear()

        // 左下
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW2,0f))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 下
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 右上
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW2,0f))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 上
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })

    }

    // パス生成(PH6)
    private fun createPathPH6() {
        square0Lst.clear()
        square1Lst.clear()

        // 下
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 右下
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 上
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 左上
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })


    }

    // -------------------------------
    // 国旗を移動する
    // -------------------------------
    private fun movePath() {
        ratioNow += ratioDv
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)

        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),yellowPaint)

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin

        canvas.save()
        canvas.translate(x0,y0)

        // 国旗を描く
        (0..splitHN-1).forEach { h ->
            val hh = h.toFloat()
            canvas.save()
            canvas.translate(-flagWX,flagHX+hh*flagH)
            (0..splitWN-1).forEach { w ->
                val ww = w.toFloat()
                //canvas.saveLayer(ww*flagW,hh*flagH,(ww+1f)*flagW,(hh+1f)*flagH,null)
                //canvas.saveLayer(0f,0f,flagW,flagH,null)
                //canvas.saveLayer(0f,0f,side,side,null)
                canvas.translate(flagW,0f)

                val hw1 = (h+w)%2

                when (hw1) {
                    // ギニア国旗を描画
                    0 -> drawGuinea(canvas)
                    // マリ国旗を描画
                    1 -> drawMali(canvas)
                }
            }
            canvas.restore()
        }

        // 国旗の周りの枠を描く(横)
        (0..splitHN-1).forEach { h ->
            val hh = h.toFloat()
            canvas.save()
            canvas.translate(-bw*0.5f,-bw*0.5f+hh*flagH)
            canvas.drawRect(0f,0f,side,bw,blackPaint)
            canvas.restore()
        }

        // 国旗の周りの枠を描く(縦)
        (0..splitWN).forEach { w ->
            val ww = w.toFloat()
            canvas.save()
            canvas.translate(-bw*0.5f+ww*flagW,0f)
            canvas.drawRect(0f,0f,bw,side,blackPaint)
            canvas.restore()
        }


        canvas.restore()


        /*
        canvas.saveLayer(0f,0f,flagW,flagH,null)

        canvas.translate(flagW2,flagH2)

        (0..3).forEach { i ->
            val square0 = square0Lst[i]
            val path0 = Path()
            var p0 = square0.ps[0]
            square0.ps.forEachIndexed { id, p ->
                if (id == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(-ratioNow*angle,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }
         */



        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // これまでの描画はテンポラリ領域に実施していたので、実際の表示に使うビットマップにコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // ギニア国旗を描画
    private fun drawGuinea(canvas: Canvas) {
        (0..3).forEach { i ->
            val square0 = square0Lst[i]
            val path0 = Path()
            var p0 = square0.ps[0]
            square0.ps.forEachIndexed { id, p ->
                if (id == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(-ratioNow*angle,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }
    }

    // マリ国旗を描画
    private fun drawMali(canvas: Canvas) {
        (0..3).forEach { i ->
            val square0 = square1Lst[i]
            val path0 = Path()
            var p0 = square0.ps[0]
            square0.ps.forEachIndexed { id, p ->
                if (id == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(-ratioNow*angle,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
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

    private data class Square(
        // 頂点リスト
        val ps: MutableList<MyPointF> = mutableListOf(),
        // ペイント
        var paint: Paint = Paint()
    )
}
