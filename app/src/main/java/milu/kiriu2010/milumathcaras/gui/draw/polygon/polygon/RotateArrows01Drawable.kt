package milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// 回転する矢印
// ---------------------------------------------
// https://forum.drawbot.com/topic/123/rotating-arrows-exercise
// ---------------------------------------------
class RotateArrows01Drawable: MyDrawable() {

    enum class Direction {
        RIGHT,
        DOWN,
        LEFT,
        UP
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 800f
    private val margin = 0f

    // -------------------------------
    // 一辺あたりの矢印の数
    // -------------------------------
    private val n = 4

    // 矢印
    private var arrow = Arrow().apply {
        this.sidea  = side/n
    }


    // ---------------------------------
    // 矢印リスト
    // ---------------------------------
    //private val arrowLst: MutableList<Arrow> = mutableListOf()

    // ---------------------------------
    // 回転の角度
    // ---------------------------------
    private var angle = 0f
    private var angleMax = 0f

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
    // 矢印を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
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
        // 矢印の初期位置設定
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 矢印を回転する
                rotatePolygon()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                handler.postDelayed(runnable, 100)
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
    // 矢印の初期位置設定
    // -------------------------------
    private fun createPath() {
        // 矢印を描く領域の一辺の長さ
        val len = side/n



        /*
        val arrow = Arrow().apply {
            this.sidea = len
        }

        (1..n).forEach { i ->
            (1..n).forEach { j ->
                arrowLst.add(arrow.copy())
            }
        }
        */
    }

    // -------------------------------
    // 矢印を回転する
    // -------------------------------
    private fun rotatePolygon() {
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
        canvas.save()

        // 矢印を描く
        (1..n).forEach { i ->
            (1..n).forEach { j ->
                canvas.translate(margin,margin)
            }
        }

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.setScale(1f,-1f)
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
        linePaint.alpha = alpha
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun setColorFilter(colorFilter: ColorFilter?) {
        linePaint.colorFilter = colorFilter
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
    // 矢印
    // --------------------------------------
    private data class Arrow(
        // 矢印の向き
        var direction: Direction = Direction.RIGHT,
        // 矢印の頂点リスト
        var pointLst: MutableList<MyPointF> = mutableListOf()
    ) {
        // 矢印を描く領域の大きさ
        var sidea = 200f
            set(data: Float) {
                field = data
                val p1_4 = field/4f
                val p3_4 = field*3f/4f
                val p2 = field/2f

                // -----------------------------
                // 左下から反時計回りに頂点を置く
                // -----------------------------
                when (direction) {
                    // --------------------------
                    // →
                    // --------------------------
                    Direction.RIGHT -> {
                        pointLst.add(MyPointF(0f,p1_4))
                        pointLst.add(MyPointF(p2,p1_4))
                        pointLst.add(MyPointF(p2,0f))
                        pointLst.add(MyPointF(field,p2))
                        pointLst.add(MyPointF(p2,field))
                        pointLst.add(MyPointF(p2,p3_4))
                        pointLst.add(MyPointF(0f,p3_4))
                    }
                }
            }


    }
}