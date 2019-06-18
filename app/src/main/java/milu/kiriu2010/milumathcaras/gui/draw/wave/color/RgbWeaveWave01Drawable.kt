package milu.kiriu2010.milumathcaras.gui.draw.wave.color

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.abs
import kotlin.math.sqrt

// -----------------------------------------------------------------
// RGBの棒線で紡ぐ
// -----------------------------------------------------------------
// |赤横|緑縦|青横|
// |青縦|赤横|緑縦|
// |緑横|青縦|赤横|
// |赤縦|緑横|青縦|
// |青横|赤縦|緑横|
// |緑縦|青横|赤縦|
// -----------------------------------------------------------------
// https://66.media.tumblr.com/8bff3f4741cf6436d878c88e3afddb54/tumblr_mjxn87AdVk1r2geqjo1_500.gif
// -----------------------------------------------------------------
class RgbWeaveWave01Drawable: MyDrawable() {

    // 棒線の向き
    enum class LineDir {
        HORIZONTAL,
        VERTICAL
    }

    // 拡大縮小
    enum class Scale {
        EXPAND,
        SHRINK
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // 描画領域の分割数
    private val splitN = 20

    // -------------------------------
    // 棒線を描く領域の一辺の長さ
    // -------------------------------
    private val a = side/splitN.toFloat()

    // 描画点のリスト
    private val squareLst = mutableListOf<Square>()

    // 比率
    private val ratioMax = 1.6f
    private val ratioMin = 0.1f

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
                    // 描画点を移動する
                    movePath()
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
    // 描画点の初期位置設定
    // -------------------------------
    private fun createPath() {
        squareLst.clear()

        (0..splitN).forEach { j ->
            (0..splitN).forEach { i ->
                val square = Square().also {
                    // 棒線の方向
                    it.dir = if ((i+j)%2 == 0) LineDir.HORIZONTAL else LineDir.VERTICAL
                    it.color = when {
                        // 1行目1列目
                        (j%3 == 0) and (i%3 == 0) -> Color.RED
                        // 1行目2列目
                        (j%3 == 0) and (i%3 == 1) -> Color.GREEN
                        // 1行目3列目
                        (j%3 == 0) and (i%3 == 2) -> Color.BLUE
                        // 2行目1列目
                        (j%3 == 1) and (i%3 == 0) -> Color.BLUE
                        // 2行目2列目
                        (j%3 == 1) and (i%3 == 1) -> Color.RED
                        // 2行目3列目
                        (j%3 == 1) and (i%3 == 2) -> Color.GREEN
                        // 3行目1列目
                        (j%3 == 2) and (i%3 == 0) -> Color.GREEN
                        // 3行目2列目
                        (j%3 == 2) and (i%3 == 1) -> Color.BLUE
                        // 3行目3列目
                        (j%3 == 2) and (i%3 == 2) -> Color.RED
                        else -> Color.BLACK
                    }
                }

                squareLst.add(square)
            }
        }

    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {
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


        squareLst.forEachIndexed { id, square ->
            val i = id%(splitN+1)
            val j = id/(splitN+1)
            val ii = i.toFloat()
            val jj = j.toFloat()
            // 座標を保存
            canvas.save()
            // 座標を移動
            canvas.translate(a*ii,a*jj)

            var lt = MyPointF()
            when (square.dir) {
                LineDir.HORIZONTAL -> {
                    lt.x = -a * square.ratio * 0.5f
                    lt.y = -a * 0.1f
                }
                LineDir.VERTICAL -> {
                    lt.x = -a * 0.1f
                    lt.y = -a * square.ratio * 0.5f
                }
            }
            var rb = MyPointF(-lt.x,-lt.y)

            linePaint.color = square.color
            canvas.drawRect(lt.x,lt.y,rb.x,rb.y,linePaint)

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
    private data class Square(
        // 棒線の向き
        var dir: LineDir = LineDir.HORIZONTAL,
        // 棒線の色
        var color: Int = Color.RED,
        // 長さ比率
        var ratio: Float = 1f
    )

}
