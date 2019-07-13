package milu.kiriu2010.milumathcaras.gui.draw.d2.circle.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// --------------------------------------------------------
// 円弧のタイリング
// --------------------------------------------------------
// 2019.07.13
// --------------------------------------------
class SlideArcTile01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // 分割数
    private val splitN = 14

    // 回転角度
    private val angles = arrayListOf<Float>(0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f)
    //private val angleDVs = arrayListOf<Float>(28f,26f,24f,22f,20f,18f,16f,14f,12f,10f,8f,6f,4f,2f)
    private val angleDVs = arrayListOf<Float>(14f,12f,10f,8f,6f,4f,2f,14f,12f,10f,8f,6f,4f,2f)
    //private val angleDVs = arrayListOf<Float>(128f,64f,32f,16f,8f,4f,2f,128f,64f,32f,16f,8f,4f,2f)

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

    // -----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -----------------------------------------
    // 可変変数 values の引数位置による意味合い
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 円を生成
        createCircle()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 円を移動する
                    moveCircle()
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
    // 円を生成
    // -------------------------------
    private fun createCircle() {
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        (0 until splitN).forEach { i ->
            angles[i] += angleDVs[i]
            if (angles[i] >= 360f) {
                angles[i] = 0f
            }
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

        canvas.save()

        val x0 = intrinsicWidth.toFloat()*0.5f
        val y0 = intrinsicHeight.toFloat()*0.5f
        canvas.translate(x0,y0)

        (splitN-1 downTo 0).forEach { i ->
            val ii = (i+1).toFloat()
            val rectf = RectF(-50f*ii,-50f*ii,50f*ii,50f*ii)
            (0..11).forEach { j ->
                val s = 30f*j + angles[i]
                linePaint.color = if ((j%2) == 0) Color.BLACK else Color.WHITE
                canvas.drawArc(rectf,s,30f,true,linePaint)
            }
        }

        canvas.restore()

        // これまでの描画はテンポラリのため、実体に反映する
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
}
