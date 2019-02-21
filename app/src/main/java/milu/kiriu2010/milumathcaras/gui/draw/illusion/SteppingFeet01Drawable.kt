package milu.kiriu2010.milumathcaras.gui.draw.illusion

import android.graphics.*
import android.os.Handler
import android.util.Log
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

    // ---------------------------------
    // バーの高さ
    // ---------------------------------
    val barH = sideH/nBar.toFloat()

    // ---------------------------------
    // 足の幅
    // ---------------------------------
    var feetW = sideW/10f

    // ---------------------------------
    // 足の高さ
    // ---------------------------------
    var feetH = barH*4f

    // ---------------------------------
    // 足の位置(X軸からの距離)
    // ---------------------------------
    var dY = 0f

    // ---------------------------------
    // 足の単位時間あたりの移動距離
    // ---------------------------------
    var v = feetH/32f

    // -------------------------------------
    // 足リスト
    // -------------------------------------
    private val feetLst = mutableListOf<Feet>()

    // -------------------------------------
    // コントラストON/OFF
    // -------------------------------------
    private var contrastFlg = false

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
    // コントラストを描くペイント
    // -------------------------------
    private val contrastPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x88aaaaaa.toInt()
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 足を描くペイント
    // -------------------------------
    private val feetPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // バックグラウンドの描画に使う色リスト
    private val colorBackLst = intArrayOf(
        0xff000000.toInt(),
        0xffffffff.toInt()
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

        // 足を生成
        createFeet()

        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 足を移動する
                    moveFeet()
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

    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(x: Float, y: Float) {
        Log.d(javaClass.simpleName,"Touch:x[${x}]y[${y}]" )
        // タッチする⇒コントラストON
        // タッチ離す⇒コントラストOFF
        contrastFlg = if ( ( x == -1f ) and ( y == -1f ) ) {
            false
        }
        else {
            true
        }
    }

    // -------------------------------
    // 足を生成
    // -------------------------------
    private fun createFeet() {
        // 黄色の足
        val feetA = Feet().apply {
            shape = RectF(3f*feetW,0f,4f*feetW,feetH)
            color = 0xffffff00.toInt()
        }

        // 青色の足
        val feetB = Feet().apply {
            shape = RectF(6f*feetW,0f,7f*feetW,feetH)
            color = 0xff000080.toInt()
        }

        feetLst.add(feetA)
        feetLst.add(feetB)
    }

    // -------------------------------
    // 足を移動する
    // -------------------------------
    private fun moveFeet() {
        dY = dY + v
        if ( dY + feetH >= sideH ) {
            dY = sideH - feetH
            v = -v
        }
        else if ( dY <= 0f ) {
            dY = 0f
            v = -v
        }
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
        (0 until nBar).forEach { i ->
            // 色(バックグラウンド)
            var colorBack = colorBackLst[i%2]

            canvas.save()
            canvas.translate(x0,y0+i.toFloat()*barH)

            // バックグラウンド描画
            backPaint.color = colorBack
            canvas.drawRect(RectF(0f,0f,sideW,barH),backPaint)

            // 座標を元に戻す
            canvas.restore()
        }

        // コントラストを描画
        if ( contrastFlg ) {
            canvas.drawRect(RectF(0f,0f,sideW,sideH),contrastPaint)
        }

        // 足を描画
        canvas.save()
        canvas.translate(x0,y0+dY)
        // 足を描画
        feetLst.forEach {
            feetPaint.color = it.color
            canvas.drawRect(it.shape,feetPaint)
        }
        // 座標を元に戻す
        canvas.restore()

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
    // 足
    // ---------------------------------
    data class Feet(
        // ---------------------------------
        // 形
        // ---------------------------------
        var shape: RectF = RectF(),
        // ---------------------------------
        // 色
        // ---------------------------------
        var color: Int = 0
    )
}