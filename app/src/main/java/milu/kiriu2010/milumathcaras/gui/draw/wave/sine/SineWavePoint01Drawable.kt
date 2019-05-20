package milu.kiriu2010.milumathcaras.gui.draw.wave.sine

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -------------------------------------------------------------------------------------
// 隣同士の点がサイン波を描く
// -------------------------------------------------------------------------------------
// https://66.media.tumblr.com/352c06f02c2203e085213921a1579c3e/tumblr_mjrmkzlAPI1r2geqjo1_500.gif
// -------------------------------------------------------------------------------------
class SineWavePoint01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // ---------------------------------
    // 描画領域の分割数
    // ---------------------------------
    private val split = 10f
    private val splitN = split.toInt()

    // ---------------------------------
    // 円の半径
    // ---------------------------------
    private var r = side/(split*2f)

    // -------------------------------
    // 円の位相(係数tに相当)
    // -------------------------------
    private var angleNow = 0f
    private var angleDiv = 10f
    private var angleMax = 360f

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
    // 線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    // -------------------------------
    // 頂点を描くペイント
    // -------------------------------
    private val vertexPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
    // values
    // 使わない
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // サイン波の描画点を生成
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
                    // サイン波の位相を移動
                    movePhase()
                    // サイン波の描画点を生成
                    //createPath()
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
    // サイン波の描画点を生成
    // -------------------------------
    private fun createPath() {
    }

    // -------------------------------
    // サイン波の位相を移動
    // -------------------------------
    private fun movePhase() {
        // 10度ずつが、きれいに回転して見える
        angleNow += angleDiv
        if ( angleNow >= angleMax ) {
            angleNow = 0f
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
        (0..splitN*2).forEach { row ->
            canvas.save()
            canvas.translate(margin,margin)
            var shiftL = -r*1.2f
            var shiftU = -r*1.2f
            canvas.translate(shiftL,shiftU+r*1.2f*row.toFloat())

            val d1 = angleDiv*row.toFloat()

            (0..splitN*2).forEach { col ->
                canvas.translate(r*1.2f,0f)
                canvas.drawCircle(0f,0f,r,linePaint)

                val d2 = angleDiv*col.toFloat()
                val x = r*MyMathUtil.cosf(angleNow+d1+d2)
                val y = r*MyMathUtil.sinf(angleNow+d1+d2)
                canvas.drawCircle(x,y,10f,vertexPaint)
            }
            // 座標を元に戻す
            canvas.restore()
        }

        // これまでの描画はテンポラリにつき、実体にコピーする
        val matrix = Matrix()
        matrix.postScale(1f,1f)
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
