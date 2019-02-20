package milu.kiriu2010.milumathcaras.gui.draw.illusion

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyVectorF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -------------------------------------------------
// "円⇔正方形の変形"のタイリング
// -------------------------------------------------
// https://michaelbach.de/ot/mot-feetLin/index.html
// -------------------------------------------------
// フォアグラウンド
//   黄黄黄黄
//   青青青青
// バックグラウンド
//   黒白黒白黒白黒白
// ---------------------------------------------
class SteppingFeet01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val sideW = 1000f
    private val sideH = 1500f
    private val margin = 0f

    // ---------------------------------
    // バー(20:黒10白10)
    // ---------------------------------
    private val nBar = 20

    // -------------------------------------
    // 変形する多角形リスト
    // -------------------------------------
    private val polygonLst = mutableListOf<Polygon>()

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
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 円・正方形を描くペイント
    // -------------------------------
    private val forePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // 描画に使う色リスト
    private val colorLst = intArrayOf(
        0xff000000.toInt(),
        0xffffffff.toInt()
    )
    /*
    private val colorLst = intArrayOf(
        Color.BLACK,
        Color.WHITE
    )
    */

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
    // 第１引数:初期状態の変形比率
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        /*
        // 初期状態の変形比率
        var ratioInit = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 初期状態の変形比率
                0 -> ratioInit = fl
            }
        }
        */

        /*
        // 円・正方形の初期ベクトル設定
        createVector()
        // 初期状態の変形比率まで変形する
        while (polygonLst[0].ratio < ratioInit) morph()
        */
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    /*
                    // 変形する
                    morph()
                    */
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
    // 円・正方形の初期ベクトル設定
    // -------------------------------
    private fun createVector() {
        polygonLst.clear()
    }

    // -------------------------------
    // 変形する
    // -------------------------------
    private fun morph() {
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin

        //Log.d(javaClass.simpleName,"========================================")
        // バーの高さ
        val barH = sideH/nBar.toFloat()
        (0 until nBar).forEach { i ->
            // 色(バックグラウンド)
            var colorBack = colorLst[i%2]

            canvas.save()
            canvas.translate(x0,y0+i.toFloat()*barH)

            // バックグラウンド描画
            backPaint.color = colorBack
            canvas.drawRect(RectF(0f,0f,sideW,barH),backPaint)

            // 座標を元に戻す
            canvas.restore()
        }

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

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
    override fun getIntrinsicWidth(): Int = (sideW+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (sideH+margin*2).toInt()

    // ---------------------------------
    // 多角形
    // ---------------------------------
    private data class Polygon(
        // 変形ベクトルリスト
        val vlst: MutableList<MyVectorF> = mutableListOf(),
        // 変形比率(0.0-1.0)
        var ratio: Float = 0f,
        // 変形の方向
        //  1 => 0.0～1.0
        // -1 => 1.0～0.0
        var direction: Int = 1
    )
}