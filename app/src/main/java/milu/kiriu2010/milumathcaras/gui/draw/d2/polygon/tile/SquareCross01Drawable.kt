package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.abs

// -----------------------------------------------------------------
// 正方形を×印で並べる
// -----------------------------------------------------------------
class SquareCross01Drawable: MyDrawable() {

    // 正方形の移動方向
    enum class MoveDir {
        RIGHT,
        LEFT,
        DOWN,
        UP,
        STILL
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 900f
    private val margin = 0f

    // -------------------------------
    // 正方形の一辺の長さ
    // -------------------------------
    private val a = 100f

    // 描画点のリスト
    private val squareLst = mutableListOf<Square>()

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
        //color = 0xff19b5fe.toInt()
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

        // 初期状態は"×印"で並べ、右回りで動いていく
        (-4..4).forEach { y ->
            (-4..4).forEach { x ->
                if ( abs(x) == abs(y) ) {
                    val square = Square().also {
                        it.s = x
                        it.t = y
                        it.l = abs(x)
                        it.d = when {
                            ((x < 0) and (y < 0)) -> MoveDir.RIGHT
                            ((x > 0) and (y < 0)) -> MoveDir.DOWN
                            ((x < 0) and (y > 0)) -> MoveDir.UP
                            ((x > 0) and (y > 0)) -> MoveDir.LEFT
                            else -> MoveDir.STILL
                        }
                    }
                    squareLst.add(square)
                }
            }
        }


    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {
        squareLst.forEach { square ->
            when (square.d) {
                MoveDir.RIGHT -> {
                    // 右へ移動
                    if (square.s < square.l) {
                        square.s++
                    }
                    // 下へ向きを変える
                    else {
                        square.d = MoveDir.DOWN
                        square.t++
                    }
                }
                MoveDir.LEFT -> {
                    // 左へ移動
                    if (square.s > -square.l) {
                        square.s--
                    }
                    // 上へ向きを変える
                    else {
                        square.d = MoveDir.UP
                        square.t--
                    }
                }
                MoveDir.DOWN -> {
                    // 下へ移動
                    if (square.t < square.l) {
                        square.t++
                    }
                    // 左へ向きを変える
                    else {
                        square.d = MoveDir.LEFT
                        square.s--
                    }
                }
                MoveDir.UP -> {
                    // 上へ移動
                    if (square.t > -square.l) {
                        square.t--
                    }
                    // 右へ向きを変える
                    else {
                        square.d = MoveDir.RIGHT
                        square.s++
                    }
                }
                else -> {}
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

        // 原点(0,0)の位置
        // = (中央,中央)
        canvas.save()
        canvas.translate(intrinsicWidth/2f,intrinsicHeight/2f)

        squareLst.forEach { square ->
            val x = a * (square.s.toFloat()-0.5f)
            val y = a * (square.t.toFloat()-0.5f)
            /*
            linePaint.color = when(square.d) {
                MoveDir.RIGHT -> Color.RED
                MoveDir.LEFT -> Color.GREEN
                MoveDir.DOWN -> Color.BLUE
                MoveDir.UP -> Color.YELLOW
                MoveDir.STILL -> Color.BLACK
            }
            */
            canvas.drawRect(x,y,x+a,y+a,linePaint)
        }

        // 座標を元に戻す
        canvas.restore()

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
    /*
    private data class Vertex(
        // 頂点の起点位置
        var slst: MutableList<MyPointF> = mutableListOf(),
        // 頂点の終端位置
        var elst: MutableList<MyPointF> = mutableListOf()
    )
    */

    private data class Square(
        // 現在位置(横)
        var s: Int = 0,
        // 現在位置(縦)
        var t: Int = 0,
        // レベル
        var l: Int = 0,
        // 現在移動方向
        var d: MoveDir = MoveDir.STILL
    )

}