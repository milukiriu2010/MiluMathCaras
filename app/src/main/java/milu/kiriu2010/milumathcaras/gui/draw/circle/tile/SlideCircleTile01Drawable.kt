package milu.kiriu2010.milumathcaras.gui.draw.circle.tile

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// --------------------------------------------------------
// 円のタイリング(左右・左下右上・右下左上の順でスライド)
// --------------------------------------------------------
// 移動距離は半径の４倍
// --------------------------------------------------------
// https://66.media.tumblr.com/4724d7f3ebfb87faf6c1c89532a5b122/tumblr_n39wd4r7tp1r2geqjo1_500.gif
// --------------------------------------------
class SlideCircleTile01Drawable: MyDrawable() {

    private enum class Mode {
        L_R,
        UR_DL,
        UL_DR,
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1050f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val split = 15f
    private val splitN = split.toInt()
    // -------------------------------
    // 円の半径
    // -------------------------------
    private val r = side/split*0.5f

    // -------------------------------
    // 移動比率
    // -------------------------------
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private val ratioDv = 0.1f

    // モード
    private var modeNow = Mode.UL_DR

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
    // 円を生成
    // -------------------------------
    private fun createCircle() {
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        ratioNow += ratioDv
        if ( ratioNow >= ratioMax ) {
            ratioNow = ratioMin
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


        (0..splitN+1).forEach { i ->
            val ii = i.toFloat()
            val ix = if ((i%2) == 0) -4f*r else -6f*r
            canvas.save()
            canvas.translate(ix,2f*sqrt(3f)*r*ii)
            (0..splitN+1).forEach { j ->
                val jj = j.toFloat()
                canvas.translate(4f*r,0f)

                when (modeNow) {
                    // 偶数行 ⇒ 左
                    // 奇数行 ⇒ 右
                    Mode.L_R -> {
                        val jx = if ((i%2) == 0) -4f*r*ratioNow else 4f*r*ratioNow
                        linePaint.color = Color.BLACK
                        canvas.drawCircle(jx,0f,r,linePaint)
                    }
                    // 行/2=偶数・偶数列 ⇒ 左下
                    // 行/2=偶数・偶数列 ⇒ 左下
                    // 行/2=奇数・奇数列 ⇒ 右上
                    // 行/2=奇数・奇数列 ⇒ 右上
                    Mode.UL_DR -> {
                        val dx = 4f*r*MyMathUtil.cosf(120f)*ratioNow
                        val dy = 4f*r*MyMathUtil.sinf(120f)*ratioNow
                        /*
                        val ddx = if ((j%2) == 0) dx else -dx
                        val ddy = if ((j%2) == 0) dy else -dy
                        linePaint.color = if ((j%2) == 0 ) Color.BLACK else Color.RED
                        canvas.drawCircle(ddx,ddy,r,linePaint)
                        */
                        var ddx = 0f
                        var ddy = 0f
                        when ((i/2)%2){
                            0 -> {
                                when (j%2) {
                                    0 -> {
                                        ddx = dx
                                        ddy = dy
                                    }
                                    1 -> {
                                        ddx = -dx
                                        ddy = -dy
                                    }
                                }
                            }
                            1 -> {
                                when (j%2) {
                                    0 -> {
                                        ddx = -dx
                                        ddy = -dy
                                    }
                                    1 -> {
                                        ddx = dx
                                        ddy = dy
                                    }
                                }
                            }
                        }
                        canvas.drawCircle(ddx,ddy,r,linePaint)
                    }
                }
            }
            canvas.restore()
        }


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
