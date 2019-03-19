package milu.kiriu2010.milumathcaras.gui.draw.circle.circles

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// --------------------------------------------
// クリスマスツリー(円を三角形上に並べる)
// --------------------------------------------
// https://66.media.tumblr.com/8ed06c22c8e4c32a60cf6bcb2b74a2e6/tumblr_my0y8b1jgP1r2geqjo1_500.gif
// --------------------------------------------
class XmasTreeCircle01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    //private val side = 1000f
    private val side = 290f
    private val margin = 50f

    // 円の半径
    val rr = 15f
    // 円と円の間隔
    val ss = 10f

    // -------------------------------------
    // クリスマスツリーの階層(必ず奇数)
    // -------------------------------------
    //      緑
    //     赤赤
    //    緑緑緑
    // -------------------------------------
    private val nL = 7

    // -------------------------------------
    // 円リスト
    // -------------------------------------
    private val circleLst = mutableListOf<Circle>()

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

    // -----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
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
        circleLst.clear()

        (1..nL).forEach { row ->
            (1..nL).forEach { col ->
                val circle = Circle().apply {
                    var rshift = 0f
                    when (row%2) {
                        0 -> {
                            color = Color.RED
                            rshift = (rr * 2f + ss)/2f
                        }
                        1 -> {
                            color = Color.GREEN
                            rshift = 0f
                        }
                    }

                    c.x = rr * (2f*(col-1).toFloat()+1f) + ss * col.toFloat() + rshift
                    c.y = rr * (2f*(row-1).toFloat()+1f) + ss * row.toFloat()
                    r = rr
                }
                circleLst.add(circle)
            }
        }
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        /*
        angle += 5f
        if ( angle >= angleMax ) {
            angle = 0f
        }
        */
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
        val x0 = margin
        val y0 = margin

        canvas.save()
        canvas.translate(x0,y0)

        // 円を描く
        circleLst.forEachIndexed { id, circle ->
            linePaint.color = circle.color
            canvas.drawCircle(circle.c.x,circle.c.y,circle.r,linePaint)
        }

        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
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

    // ---------------------------------
    // 円
    // ---------------------------------
    data class Circle (
        // ---------------------------------
        // 中心
        // ---------------------------------
        var c: MyPointF = MyPointF(),
        // ---------------------------------
        // 半径
        // ---------------------------------
        var r: Float = 0f,
        // ---------------------------------
        // 半径比率
        // ---------------------------------
        var rratio: Float = 1f,
        // ---------------------------------
        // 半径比率を変える方向
        //   0: 変化なし
        //   1: 大きくなる
        //  -1: 小さくなる
        // ---------------------------------
        var rs: Float = 0f,
        // ---------------------------------
        // 色
        // ---------------------------------
        var color: Int = 0
    )

}