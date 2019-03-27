package milu.kiriu2010.milumathcaras.gui.draw.circle.tile

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

    // 円の移動距離(最大)
    val dM = 2f*rr+ss
    // 円の移動距離(刻み)
    val dV = dM/5f
    // 円の移動距離(現在)
    var dN = 0f

    // 円の半径比率(最大)
    var rM = 1f
    // 円の半径比率(刻み)
    var rV = rM/5f
    // 円の半径比率(現在)
    var rN = 0f

    // 小さくなる円のインデックス
    val idxSLst = arrayOf(4,3,5,2,6,1,7)
    // 大きくなる円のインデックス
    val idxBLst = arrayOf(3,5,2,6,1,7,0)

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
        circleLst.clear()

        (0 until nL).forEach { row ->
            val idxS = idxSLst[row]
            val idxB = idxBLst[row]
            (0..nL).forEach { col ->
                val circle = Circle().apply {
                    // 円の中心(右シフト)
                    var rshift = 0f
                    when (row%2) {
                        0 -> {
                            color = Color.GREEN
                            // 円の中心(右シフト)
                            rshift = 0f
                            // 半径(初期)
                            r = when  {
                                (col <= idxB) -> 0f
                                (col > idxS)  -> 0f
                                else -> rr
                            }

                        }
                        1 -> {
                            color = Color.RED
                            // 円の中心(右シフト)
                            rshift = (rr * 2f + ss)/2f
                            // 半径(初期)
                            r = when  {
                                (col < idxS)  -> 0f
                                (col >= idxB) -> 0f
                                else -> rr
                            }
                        }
                    }

                    // 円の中心
                    c.x = rr * (2f*(col-1).toFloat()+1f) + ss * col.toFloat() + rshift
                    c.y = rr * (2f*row.toFloat()+1f) + ss * row.toFloat()
                    // 半径比率を変える方向
                    sign = when (col) {
                        idxB -> 1
                        idxS -> -1
                        else -> 0
                    }
                }
                circleLst.add(circle)
            }
        }

        /*
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
        */

    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        dN += dV
        rN += rV
        if ( dN >= dM ) {
            dN = 0f
            rN = 0f
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
        val x0 = margin
        val y0 = margin

        canvas.save()
        canvas.translate(x0,y0)

        // 円を描く
        circleLst.forEachIndexed { id, circle ->
            linePaint.color = circle.color
            val row = id/(nL+1)
            val r = when ( circle.sign ) {
                1 -> rr * rN
                -1 -> circle.r * (rM-rN)
                else -> circle.r
            }

            when (row%2) {
                0 -> canvas.drawCircle(circle.c.x+dN,circle.c.y,r,linePaint)
                1 -> canvas.drawCircle(circle.c.x-dN,circle.c.y,r,linePaint)
            }
        }
        /*
        circleLst.forEachIndexed { id, circle ->
            linePaint.color = circle.color
            val row = id/nL
            when (row%2) {
                0 -> canvas.drawCircle(circle.c.x+dN,circle.c.y,circle.r,linePaint)
                1 -> canvas.drawCircle(circle.c.x-dN,circle.c.y,circle.r,linePaint)
            }
        }
        */

        canvas.restore()

        // これまでの描画は上下逆なので反転する
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
        // 半径比率を変える方向
        //   0: 変化なし
        //   1: 大きくなる
        //  -1: 小さくなる
        // ---------------------------------
        var sign: Int = 0,
        // ---------------------------------
        // 色
        // ---------------------------------
        var color: Int = 0
    )

}