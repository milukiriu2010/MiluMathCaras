package milu.kiriu2010.milumathcaras.gui.draw.polygon.tile

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 六角形が波打つようにスケールを変更01
// -----------------------------------------------------------------
// https://66.media.tumblr.com/b7ed4a9b13d54c3469d9372dd662df70/tumblr_mviec26tSc1r2geqjo1_500.gif
// -----------------------------------------------------------------
class HexagonScale01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 960f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val splitN = 12
    // -------------------------------
    // 六角形を形成する半径の長さ
    // -------------------------------
    private val r = side/splitN.toFloat()*0.5f
    private val r3 = r*sqrt(3f)

    // 基準となる六角形
    private val hexagon = mutableListOf<MyPointF>()

    // 1ターン内の移動比率
    private var ratioN = 0
    private val ratioLst = floatArrayOf(1f,0.9f,0.8f,0.7f,0.6f,0.5f,0.6f,0.7f,0.8f)

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
                    // スケールを変更する
                    shiftScale()
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
        hexagon.clear()

        (0..5).forEach { i ->
            val t = 30f + 60f * i.toFloat()
            val p = MyPointF().also {
                it.x = r * MyMathUtil.cosf(t)
                it.y = r * MyMathUtil.sinf(t)
            }
            hexagon.add(p)
        }
    }

    // -------------------------------
    // スケールを変更する
    // -------------------------------
    private fun shiftScale() {
        ratioN = (ratioN+1)%ratioLst.size
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

        // 座標を保存
        canvas.save()

        // 原点を移動
        canvas.translate(intrinsicWidth.toFloat()*0.5f,intrinsicHeight.toFloat()*0.5f)

        (0..splitN).forEach { i ->
            val ii = i.toFloat()
            val ratioA = (i+ratioN)%ratioLst.size
            val ratio = ratioLst[ratioA]
            Log.d(javaClass.simpleName,"ratio[$ratio]hexagon[${hexagon.size}]")
            (0..5).forEach { j ->
                val jj = j.toFloat()
                val rr = r3*ii
                val cos = rr*MyMathUtil.cosf(60f * j)
                val sin = rr*MyMathUtil.sinf(60f * j)

                canvas.save()
                canvas.translate(cos,sin)
                drawHexagon(canvas,ratio)

                // 途中のパスに六角形を描く
                val kmax = if ( i <= 1 ) 0 else i-1
                val cos1 = r3*MyMathUtil.cosf(120f+60f*jj)
                val sin1 = r3*MyMathUtil.sinf(120f+60f*jj)
                (1..kmax).forEach { _ ->
                    canvas.translate(cos1,sin1)
                    drawHexagon(canvas,ratio)
                }

                canvas.restore()
            }
        }



        // 座標を元に戻す
        canvas.restore()

        // これまでの描画はテンポラリなので、実体にコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    private fun drawHexagon(canvas: Canvas, ratio: Float) {
        val path = Path()
        hexagon.forEachIndexed { id, myPointF ->
            val x = myPointF.x*ratio
            val y = myPointF.y*ratio
            Log.d(javaClass.simpleName,"id[$id]x[$x]y[$y]")
            if ( id == 0 ) {
                path.moveTo(myPointF.x*ratio,myPointF.y*ratio)
            }
            else {
                path.lineTo(myPointF.x*ratio,myPointF.y*ratio)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)
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
