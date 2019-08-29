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
        PH3
    }

    // 現在のモード
    private var modeNow = Mode.PH1

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1350f
    private val margin = 0f

    // 領域分割
    private val splitW  = 5f
    private val splitWN = splitW.toInt()
    private val splitH  = 10f
    private val splitHN = splitH.toInt()

    // ---------------------------------
    // 国旗
    // ---------------------------------
    private val flagW = side/splitW
    private val flagW2 = flagW*0.5f
    private val flagW3 = flagW/3f
    private val flagW6 = flagW/6f
    private val flagH = side/splitH
    private val flagH2 = flagH*0.5f

    // 国旗の移動比率
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private val ratioDv = 0.1f

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
        color = Color.GREEN
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
                Mode.PH1 -> Mode.PH1
                Mode.PH2 -> Mode.PH3
                Mode.PH3 -> Mode.PH1
            }
        }

        // パス生成
        when (modeNow) {
            Mode.PH1 -> createPathPH1()
        }


        // パスの初期化を実施したかどうか
        isInitialized = true
    }

    // パス生成(PH1)
    private fun createPathPH1() {
        // 右上
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

        // 右下
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

        // 左下
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

        // 左上
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

    // -------------------------------
    // 国旗の〇部分を移動する
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

        canvas.saveLayer(0f,0f,flagW,flagH,null)

        canvas.translate(flagW2,flagH2)

        (0..3).forEach { i ->
            val square0 = square0Lst[i]
            val path0 = Path()
            square0.ps.forEachIndexed { id, p ->
                if (id == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    path0.lineTo(p.x,p.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }




        /*
        // 国旗を描画
        (0..splitHN+1).forEach { h ->
            val hh = h.toFloat()
            canvas.save()
            canvas.translate(-flagW,flagH*(hh-1f))
            (0..splitWN+1).forEach { w ->
                val ww = w.toFloat()
                canvas.save()
                canvas.translate(flagW*ww,0f)

                when ((h+w)%2) {
                    0 -> {
                        canvas.drawBitmap(bmpFlagLst[0],0f,0f,framePaint)
                    }
                    1 -> {
                        canvas.drawBitmap(bmpFlagLst[1],0f,0f,framePaint)
                    }
                }

                canvas.restore()
            }

            canvas.restore()
        }
         */

        canvas.restore()

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // これまでの描画はテンポラリ領域に実施していたので、実際の表示に使うビットマップにコピーする
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

    private data class Square(
        // 頂点リスト
        val ps: MutableList<MyPointF> = mutableListOf(),
        // ペイント
        var paint: Paint = Paint()
    )
}
