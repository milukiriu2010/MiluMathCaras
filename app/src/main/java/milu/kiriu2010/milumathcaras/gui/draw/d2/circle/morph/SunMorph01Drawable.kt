package milu.kiriu2010.milumathcaras.gui.draw.d2.circle.morph

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ------------------------------------------------
// 太陽の変形
// ------------------------------------------------
// (1) 光線６つ(だんだん細長くなり、やがて消える)
// (2) 光線なし
// (3) 光線１２(だんだん細長くなり、やがて消える)
// (4) 光線なし
// の繰り返し
// ------------------------------------------------
// https://66.media.tumblr.com/e915741f2047ea59304cd90240b92baa/tumblr_mji84mNDUm1r2geqjo1_500.gif
// ------------------------------------------------
class SunMorph01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // -------------------------------------------
    // 状態
    // -------------------------------------------
    // 光線６分割
    // 光線１２分割
    // 光線なし
    // -------------------------------------------
    private enum class State {
        RAY6,
        RAY12,
        RAY0A,
        RAY0B
    }
    private var stateNow = State.RAY6

    // 太陽の半径
    private val rr = side*0.25f

    // 比率
    private var ratioMax = 1f
    private var ratioMin = 0f
    private var ratioNow = ratioMin
    private var ratioDv  = 0.1f

    // 光線リスト(開始)
    private val raySLst = mutableListOf<Ray>()
    // 光線リスト(終了)
    private val rayELst = mutableListOf<Ray>()

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
    // 円を描くペイント
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
        // パス生成
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
                    // パス生成
                    createPath()
                    // 変形する
                    morph()
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
    // パスを生成
    // -------------------------------
    private fun createPath() {
        if ( ratioNow > ratioMin ) return

        // 光線リストをクリア
        raySLst.clear()
        rayELst.clear()

        // 状態を更新
        stateNow = when (stateNow) {
            State.RAY6 -> State.RAY0A
            State.RAY12 -> State.RAY0B
            State.RAY0A -> State.RAY12
            State.RAY0B -> State.RAY6
        }

        // 光線リストを生成
        when (stateNow) {
            // ----------------------------
            // 光線６つ
            // ----------------------------
            // 角度
            //  30 -  90 ⇒  60 -  60
            //  90 - 150 ⇒ 120 - 120
            // 150 - 210 ⇒ 180 - 180
            // 210 - 270 ⇒ 240 - 240
            // 270 - 330 ⇒ 300 - 300
            // 330 - 390 ⇒ 360 - 360
            // 半径
            // 0.25 - 0.35 ⇒ 0.35 - 0.50
            // ----------------------------
            State.RAY6 -> {
                (0..5).forEach { i ->
                    val ii = i.toFloat()
                    // 光線(開始)
                    val rayS = Ray().also {
                        it.ra = side*0.25f
                        it.rb = side*0.35f
                        it.ta = 30f + 60f * ii
                        it.tb = 90f + 60f * ii
                    }
                    // 光線(終了)
                    val rayE = Ray().also {
                        it.ra = side*0.35f
                        it.rb = side*0.5f
                        it.ta = 60f * (ii+1f)
                        it.tb = 60f * (ii+1f)
                    }
                    raySLst.add(rayS)
                    rayELst.add(rayE)
                }
                ratioDv = 0.1f
            }
            // ----------------------------
            // 光線１２つ
            // ----------------------------
            // 角度
            //   0 -  30 ⇒  15 -  15
            //  30 -  60 ⇒  45 -  45
            //  60 -  90 ⇒  75 -  75
            //  ～
            // 300 - 330 ⇒ 315 - 315
            // 330 - 360 ⇒ 345 - 345
            // 半径
            // 0.25 - 0.40 ⇒ 0.30 - 0.50
            // ----------------------------
            State.RAY12 -> {
                (0..11).forEach { i ->
                    val ii = i.toFloat()
                    // 光線(開始)
                    val rayS = Ray().also {
                        it.ra = side*0.25f
                        it.rb = side*0.40f
                        it.ta = 30f * ii
                        it.tb = 30f * (ii+1f)
                    }
                    // 光線(終了)
                    val rayE = Ray().also {
                        it.ra = side*0.3f
                        it.rb = side*0.5f
                        it.ta = 15f + 30f * ii
                        it.tb = 15f + 30f * ii
                    }
                    raySLst.add(rayS)
                    rayELst.add(rayE)
                }
                ratioDv = 0.1f
            }
            // 光線なし
            State.RAY0A -> {
                ratioDv = 0.5f
            }
            State.RAY0B -> {
                ratioDv = 0.5f
            }
        }

    }

    // -------------------------------
    // 変形する
    // -------------------------------
    private fun morph() {
        ratioNow += ratioDv
        if ( ratioNow >= ratioMax ) {
            ratioNow = ratioMin
            // 光線リストをクリア
            raySLst.clear()
            rayELst.clear()
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
        // = (左右中央,上下中央)
        val x0 = (intrinsicWidth/2).toFloat()
        val y0 = (intrinsicHeight/2).toFloat()

        // 原点(x0,y0)を中心に円・三角形を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 光線を描画
        //Log.d(javaClass.simpleName,"==================================")
        raySLst.forEachIndexed { id, rayS ->
            val rayE = rayELst[id]

            // 内側の半径
            val ra = rayS.ra * (ratioMax-ratioNow) + rayE.ra * ratioNow
            // 外側の半径
            val rb = rayS.rb * (ratioMax-ratioNow) + rayE.rb * ratioNow
            // 開始角度
            val ta = rayS.ta * (ratioMax-ratioNow) + rayE.ta * ratioNow
            // 終了角度
            val tb = rayS.tb * (ratioMax-ratioNow) + rayE.tb * ratioNow

            // 外側の弧
            val rectb = RectF(-rb,-rb,rb,rb)
            canvas.drawArc(rectb,ta,tb-ta,true,linePaint)

            // 内側の弧
            val recta = RectF(-ra,-ra,ra,ra)
            canvas.drawArc(recta,ta,tb-ta,true,backPaint)

            //Log.d(javaClass.simpleName,"id[$id]ra[$ra]rb[$rb]ta[$ta]tb[$tb]ratio[$ratioNow]")
        }

        // 静止している円を描画
        canvas.drawCircle(0f,0f,rr,linePaint)

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画はテンポラリにつき実体に描画
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

    // 光線
    private data class Ray (
        // 内側の半径
        var ra: Float = 0f,
        // 外側の半径
        var rb: Float = 0f,
        // 開始角度
        var ta: Float = 0f,
        // 終了角度
        var tb: Float = 0f
    )
}