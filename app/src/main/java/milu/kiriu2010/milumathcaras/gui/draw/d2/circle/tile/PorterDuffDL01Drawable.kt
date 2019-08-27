package milu.kiriu2010.milumathcaras.gui.draw.d2.circle.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// PorterDuff"水色と紫色"の"DARKENとLIGHTEN"
// ---------------------------------------------
// 2019.08.26
// ---------------------------------------------
class PorterDuffDL01Drawable: MyDrawable() {

    enum class Mode {
        LR,
        UD
    }

    // 現在のモード
    private var modeNow = Mode.UD

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
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
    private val flagH = side/splitH
    private val flagR = flagH*0.4f

    // 国旗の円を描く位置
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

    // -------------------------------
    // ダミーペイント
    // -------------------------------
    private val dummyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // 日本の国旗の円を描くペイント
    // ---------------------------------
    private val frontJPN = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xffff00ff.toInt()
        style = Paint.Style.FILL
        //alpha = 128
    }

    // -------------------------------
    // 日本の国旗の下地を描くペイント
    // -------------------------------
    private val backJPN = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // パラウの国旗の円を描くペイント
    // ---------------------------------
    private val frontPLU = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff00ffff.toInt()
        style = Paint.Style.FILL
        //alpha = 128
    }

    // -------------------------------
    // パラウの国旗の下地を描くペイント
    // -------------------------------
    private val backPLU = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
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
    // 第１引数:初期状態の変形比率
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
                        handler.postDelayed(runnable, 50)
                    }
                    else {
                        handler.postDelayed(runnable, 10)
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

        canvas.drawColor(0xffffffff.toInt())

        // 国旗の円を描画
        when (modeNow) {
            Mode.LR -> {
                (0..splitHN+3).forEach { h ->
                    val hh = h.toFloat()
                    canvas.save()
                    canvas.translate(-2f*flagW,flagH*(hh-2f))
                    val w0 = (h%2).toFloat()
                    canvas.translate(w0*flagW,0f)
                    (0..splitWN+5 step 2).forEach { w ->
                        val ww = w.toFloat()
                        // API level 21
                        canvas.saveLayer(-2f*flagW,-2f*flagH,side+2f*flagW,side+2f*flagH,dummyPaint)
                        canvas.translate(flagW*ww,0f)

                        canvas.drawBitmap(bmpFlagLst[0],0f,0f,dummyPaint)
                        val pdMode = when (h%2){
                            0 -> PorterDuff.Mode.DARKEN
                            1 -> PorterDuff.Mode.LIGHTEN
                            else -> PorterDuff.Mode.SCREEN
                        }
                        dummyPaint.xfermode = PorterDuffXfermode(pdMode)

                        canvas.drawBitmap(bmpFlagLst[1],flagW,0f,dummyPaint)
                        dummyPaint.xfermode = null

                        canvas.restore()
                    }

                    canvas.restore()
                }
            }
            Mode.UD -> {
                (0..splitWN+3).forEach { w ->
                    val ww = w.toFloat()
                    canvas.save()
                    canvas.translate((ww-2f)*flagW,-2f*flagH)
                    val h0 = (w%2).toFloat()
                    canvas.translate(0f,h0*flagH)
                    (0..splitHN+5 step 2).forEach { h ->
                        val hh = h.toFloat()
                        canvas.saveLayer(-2f*flagW,-2f*flagH,side+2f*flagW,side+2f*flagH,dummyPaint)
                        canvas.translate(0f,flagH*hh)

                        canvas.drawBitmap(bmpFlagLst[0],0f,-flagH,dummyPaint)
                        val pdMode = when (w%2){
                            0 -> PorterDuff.Mode.DARKEN
                            1 -> PorterDuff.Mode.LIGHTEN
                            else -> PorterDuff.Mode.SCREEN
                        }
                        dummyPaint.xfermode = PorterDuffXfermode(pdMode)

                        canvas.drawBitmap(bmpFlagLst[1],0f,0f,dummyPaint)
                        dummyPaint.xfermode = null

                        canvas.restore()
                    }

                    canvas.restore()
                }
            }
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
        val bmpJPN1 = Bitmap.createBitmap(3*flagW.toInt(),3*flagH.toInt(),Bitmap.Config.ARGB_8888)
        val cvsJPN1 = Canvas(bmpJPN1)
        cvsJPN1.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
        // 円
        when (modeNow) {
            Mode.LR -> {
                cvsJPN1.save()
                cvsJPN1.translate(1.5f*flagW,1.5f*flagH)
                cvsJPN1.drawCircle(flagW*ratioNow,0f,flagR,frontJPN)
                cvsJPN1.restore()
            }
            Mode.UD -> {
                cvsJPN1.save()
                cvsJPN1.translate(1.5f*flagW,1.5f*flagH)
                cvsJPN1.drawCircle(0f,flagH*ratioNow,flagR,frontJPN)
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
        val bmpPLU1 = Bitmap.createBitmap(3*flagW.toInt(),3*flagH.toInt(),Bitmap.Config.ARGB_8888)
        val cvsPLU1 = Canvas(bmpPLU1)
        cvsPLU1.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
        // 円
        when (modeNow) {
            Mode.LR -> {
                cvsPLU1.save()
                cvsPLU1.translate(1.5f*flagW,1.5f*flagH)
                cvsPLU1.drawCircle(-flagW*ratioNow,0f,flagR,frontPLU)
                cvsPLU1.restore()
            }
            Mode.UD -> {
                cvsPLU1.save()
                cvsPLU1.translate(1.5f*flagW,1.5f*flagH)
                cvsPLU1.drawCircle(0f,-flagH*ratioNow,flagR,frontPLU)
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
