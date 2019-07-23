package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 正方形⇔正方形
// -----------------------------------------------------------------
// https://twitter.com/i/status/1044920001646473216
// -----------------------------------------------------------------
class Square2Square01Drawable: MyDrawable() {

    // 移動方向
    enum class ModePtrn {
        // 黒(偶数:上/奇数:下)
        BUD
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val split = 10f
    private val splitN = split.toInt()

    // -------------------------------
    // 正方形の半径
    // -------------------------------
    // 内側
    private val a = side/split*0.5f
    // 外側
    private val b = a*sqrt(2f)

    // 現在の移動方向
    private var modeNow = ModePtrn.BUD

    // 1ターン内の移動比率
    private var ratioNow = 0f
    private val ratioDiv = 0.1f
    private val ratioMax = 1f

    // 描画点の初期化回数
    private var nCnt = 0

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
    // 白
    // -------------------------------
    private val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 黒
    // -------------------------------
    private val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
                    // 描画点の初期位置設定
                    createPath()
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
        // 移動比率=最大or0の場合、描画点リストを構築しなおす
        if ( ( ratioNow > 0f ) and ( ratioNow < ratioMax ) ) return
        ratioNow = 0f

        // 描画点をクリア
        squareLst.clear()

        // 描画点の初期化回数が２以上であれば、
        // 移動モードを変更
        if ( nCnt > 1 ) {
            modeNow = when (modeNow) {
                ModePtrn.BUD -> ModePtrn.BUD
            }
        }

        // 描画点を設定
        when (modeNow) {
            ModePtrn.BUD -> createPathBUD()
        }

        nCnt++
    }

    // -------------------------------
    // 描画点を設定
    // 黒(偶数:上/奇数:下)
    // -------------------------------
    private fun createPathBUD() {

        // 偶数:上
        val su = Square()
        (0..3).forEach { i ->
            val ii = i.toFloat()
            val cos1 = a*MyMathUtil.cosf(ii*90f+45f)
            val sin1 = a*MyMathUtil.sinf(ii*90f+45f)
            val cos2 = a*MyMathUtil.cosf(ii*90f)
            val sin2 = a*MyMathUtil.sinf(ii*90f)

            // 開始点
            val ps = MyPointF().also {
                it.x = cos1
                it.y = sin1
            }
            su.slst.add(ps)

            // 終了点
            val pe = MyPointF().also {
                it.x = cos2
                it.y = sin2 - a
            }
            su.elst.add(pe)
        }
        squareLst.add(su)

        // 奇数:下方向
        val sd = Square()
        (0..3).forEach { i ->
            val ii = i.toFloat()
            val cos1 = a*MyMathUtil.cosf(ii*90f+45f)
            val sin1 = a*MyMathUtil.sinf(ii*90f+45f)
            val cos2 = a*MyMathUtil.cosf(ii*90f+90f)
            val sin2 = a*MyMathUtil.sinf(ii*90f+90f)

            // 開始点
            val ps = MyPointF().also {
                it.x = cos1
                it.y = sin1
            }
            sd.slst.add(ps)

            // 終了点
            val pe = MyPointF().also {
                it.x = cos2
                it.y = sin2 + a
            }
            sd.elst.add(pe)
        }
        squareLst.add(sd)
    }

    // -------------------------------
    // 描画点を移動する
    // -------------------------------
    private fun movePath() {
        ratioNow += ratioDiv
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),whitePaint)

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 原点(0,0)の位置
        // = (マージン,マージン)
        canvas.save()
        canvas.translate(margin,margin)
        canvas.translate(-2f*a,-2f*a)

        // 描画点を描く
        (0..splitN+2).forEach { row ->
            val rowf = row.toFloat()
            canvas.save()
            canvas.translate(0f,2f*a*rowf)
            (0..splitN+2).forEach { col ->
                val colf = col.toFloat()
                canvas.save()
                canvas.translate(2f*a*colf,0f)

                when (modeNow) {
                    ModePtrn.BUD -> drawBUD(canvas,col)
                }

                canvas.restore()
            }
            canvas.restore()
        }
        // 座標を元に戻す
        canvas.restore()

        // これまでの描画はテンポラリなので、実体にコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // ---------------------------------
    // 描画
    // 黒(奇数:上/偶数:下)
    // ---------------------------------
    private fun drawBUD(canvas: Canvas,col: Int) {
        val square = when (col%2) {
            0    -> squareLst[0]
            else -> squareLst[1]
        }
        val path = Path()
        (0..3).forEach { i ->
            val ps = square.slst[i]
            val pe = square.elst[i]
            val p = ps.lerp(pe,ratioNow,ratioMax-ratioNow)
            if (i == 0) {
                path.moveTo(p.x,p.y)
            }
            else {
                path.lineTo(p.x,p.y)
            }
        }
        path.close()
        canvas.drawPath(path,blackPaint)
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
    // 正方形
    // --------------------------------------
    private data class Square(
        // 移動開始点
        val slst: MutableList<MyPointF> = mutableListOf(),
        // 移動終了点
        val elst: MutableList<MyPointF> = mutableListOf()
    )
}