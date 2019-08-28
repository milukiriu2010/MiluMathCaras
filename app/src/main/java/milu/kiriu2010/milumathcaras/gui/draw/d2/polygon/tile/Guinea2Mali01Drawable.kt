package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// ギニアの国旗⇔マリの国旗01
// ---------------------------------------------
// 2019.08.28
// ---------------------------------------------
class Guinea2Mali01Drawable: MyDrawable() {

    enum class Mode {
        LR,
        UD
    }

    // 現在のモード
    private var modeNow = Mode.LR

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
    private val flagW0 = flagW/3f
    private val flagH = side/splitH

    // 国旗の移動比率
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private val ratioDv = 0.1f

    // パスの初期化を実施したかどうか
    private var isInitialized = false

    // ---------------------------------------------------------------------
    // 描画領域として使うビットマップ
    // ---------------------------------------------------------------------
    // 画面にタッチするとdrawが呼び出されるようなのでビットマップに描画する
    // ---------------------------------------------------------------------
    private lateinit var imageBitmap: Bitmap
    private val tmpBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)

    // 描画する国旗のビットマップリスト
    private val bmpFlagLst = mutableListOf<Bitmap>()

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
                Mode.LR -> Mode.UD
                Mode.UD -> Mode.LR
            }
        }

        // パスの初期化を実施したかどうか
        isInitialized = true
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

        bmpFlagLst.clear()
        // ----------------------------
        // 日本の国旗のビットマップ
        // ----------------------------
        // L⇒R
        // U⇒D
        // ----------------------------
        createBmpJPN1()
        // ----------------------------
        // パラオの国旗のビットマップ
        // ----------------------------
        // L⇒R
        // U⇒D
        // ----------------------------
        createBmpPLU1()

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin

        canvas.save()
        canvas.translate(x0,y0)

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

        canvas.restore()

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // これまでの描画はテンポラリ領域に実施していたので、実際の表示に使うビットマップにコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // ----------------------------
    // 日本の国旗のビットマップ
    // ----------------------------
    // L⇒R
    // U⇒D
    // ----------------------------
    private fun createBmpJPN1() {
        val bmpJPN1 = Bitmap.createBitmap(flagW.toInt(),flagH.toInt(),Bitmap.Config.ARGB_8888)
        val cvsJPN1 = Canvas(bmpJPN1)
        cvsJPN1.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
        // 下地
        cvsJPN1.drawRect(0f,0f,flagW,flagH,yellowPaint)
        // 円
        when (modeNow) {
            Mode.LR -> {
                cvsJPN1.save()
                cvsJPN1.translate(-0.5f*flagW,0.5f*flagH)
                (0..2).forEach { _ ->
                    //cvsJPN1.drawCircle(flagW*ratioNow,0f,flagR,redPaint)
                    cvsJPN1.translate(flagW,0f)
                }
                cvsJPN1.restore()
            }
            Mode.UD -> {
                cvsJPN1.save()
                cvsJPN1.translate(0.5f*flagW,-0.5f*flagH)
                (0..2).forEach { _ ->
                    //cvsJPN1.drawCircle(0f,flagH*ratioNow,flagR,redPaint)
                    cvsJPN1.translate(0f,flagH)
                }
                cvsJPN1.restore()
            }
        }
        bmpFlagLst.add(bmpJPN1)
    }

    // ----------------------------
    // パラオの国旗のビットマップ
    // ----------------------------
    // L⇒R
    // U⇒D
    // ----------------------------
    private fun createBmpPLU1() {
        val bmpPLU1 = Bitmap.createBitmap(flagW.toInt(),flagH.toInt(),Bitmap.Config.ARGB_8888)
        val cvsPLU1 = Canvas(bmpPLU1)
        cvsPLU1.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
        // 下地
        //cvsPLU1.drawRect(0f,0f,flagW,flagH,backPLU)
        // 円
        when (modeNow) {
            Mode.LR -> {
                cvsPLU1.save()
                cvsPLU1.translate(-0.5f*flagW,0.5f*flagH)
                (0..2).forEach { _ ->
                    //cvsPLU1.drawCircle(flagW*ratioNow,0f,flagR,greenPaint)
                    cvsPLU1.translate(flagW,0f)
                }
                cvsPLU1.restore()
            }
            Mode.UD -> {
                cvsPLU1.save()
                cvsPLU1.translate(0.5f*flagW,-0.5f*flagH)
                (0..2).forEach { _ ->
                    //cvsPLU1.drawCircle(0f,flagH*ratioNow,flagR,greenPaint)
                    cvsPLU1.translate(0f,flagH)
                }
                cvsPLU1.restore()
            }
        }
        bmpFlagLst.add(bmpPLU1)
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
}
