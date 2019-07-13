package milu.kiriu2010.milumathcaras.gui.draw.d2.illusion

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -------------------------------------------------
// Stereokinetic Effect(SKE)
// -------------------------------------------------
// https://michaelbach.de/ot/mot-ske/index.html
// -------------------------------------------------
// 円を１２個描く
//   黄青黄青黄青黄青
//   青黄青黄
// ---------------------------------------------
class StereoKineticEffect01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------------
    // 描画する円の全体の数
    // -------------------------------------
    private var nT = 12

    // -------------------------------------
    // 中心よりに描画する円の数
    // -------------------------------------
    private var nC = 4

    // -------------------------------------
    // 円リスト
    // -------------------------------------
    private val circleLst = mutableListOf<Circle>()

    // -------------------------------------
    // 円の回転角度
    // -------------------------------------
    var angle = 0f
    var angleMax = 360f

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
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // 円の描画に使う色リスト
    private val colorLst = intArrayOf(
        0xff0000ff.toInt(),
        0xffffff00.toInt()
    )

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

                    handler.postDelayed(runnable, 10)
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
        // 前回計算した半径
        var prevR = 0f
        (0 until nT).forEach { i ->
            val circle = Circle().apply {
                // 半径
                r = side*(nT-i).toFloat()/nT.toFloat()/2f
                // 中心位置のX座標
                val x = when {
                    i < (nT-nC) -> {
                        // 前回計算した半径
                        prevR = r
                        side / 2f - r
                    }
                    else -> side/2f-prevR*2f+r
                }
                // 中心
                c = MyPointF(x,0f)
                // 色
                color = colorLst[i%2]
            }
            // 円リストに追加
            circleLst.add(circle)
        }
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        // 回転角度
        val dv = 5f
        angle += dv
        if ( angle >= angleMax ) {
            angle = 0f
        }

        /*
        ここで計算すると、誤差がたまっていって、
        中心に集まってしまうので、
        画像を描画するところで、計算する

        // 円の中心位置を移動する
        circleLst.forEach { circle ->
            // 中心位置
            val c = circle.c
            circle.c.x = c.x * MyMathUtil.cosf(dv) - c.y * MyMathUtil.sinf(dv)
            circle.c.y = c.x * MyMathUtil.sinf(dv) + c.y * MyMathUtil.cosf(dv)
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
        // = (左右中央,上下中央)
        val x0 = intrinsicWidth.toFloat()/2f
        val y0 = intrinsicHeight.toFloat()/2f

        //Log.d(javaClass.simpleName,"========================================")
        // 円描画
        circleLst.forEach { circle ->
            canvas.save()
            canvas.translate(x0,y0)
            // 中心位置
            val c = circle.c.copy()
            val x = c.x * MyMathUtil.cosf(angle) - c.y * MyMathUtil.sinf(angle)
            val y = c.x * MyMathUtil.sinf(angle) + c.y * MyMathUtil.cosf(angle)

            circlePaint.color = circle.color
            canvas.drawCircle(x,y,circle.r,circlePaint)

            // 座標を元に戻す
            canvas.restore()
        }

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
    override fun getIntrinsicWidth(): Int = (side+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (side+margin*2).toInt()

    // ---------------------------------
    // 円
    // ---------------------------------
    data class Circle(
        // ---------------------------------
        // 中心
        // ---------------------------------
        var c: MyPointF = MyPointF(),
        // ---------------------------------
        // 半径
        // ---------------------------------
        var r: Float = 0f,
        // ---------------------------------
        // 色
        // ---------------------------------
        var color: Int = 0
    )
}