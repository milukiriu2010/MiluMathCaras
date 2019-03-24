package milu.kiriu2010.milumathcaras.gui.draw.circle.circles

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// --------------------------------------------
// 青・赤・黄３つの円を回転する
// --------------------------------------------
// それぞれの円の大きさは同じだが、
// 回転する半径が徐々に大きくなる
// --------------------------------------------
// https://66.media.tumblr.com/a465c018e4c0ff6a7c8228b729821d56/tumblr_moni0oFXm71r2geqjo1_500.gif
// --------------------------------------------
class RotateCircle03Drawable: MyDrawable() {

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
    // 各描画領域の大きさ
    // -------------------------------
    private val sideE = side/split


    // -------------------------------
    // 円の回転角度
    // -------------------------------
    private var angle = 0f
    private var angleMax = 360f

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
        circleLst.clear()

        // 各描画領域の大きさに対する半径の比率
        val ratio = 0.3f

        // 一番小さい円(青)
        val circle0 = Circle().apply {
            // 回転の中心からの距離
            // 各描画領域の大きさに対し２割
            l = sideE*0.2f
            // 回転の中心からの角度
            t = 0f
            // 半径
            r = sideE * ratio
            // 色
            color = Color.BLUE
        }

        // 中間の円(赤)
        val circle1 = Circle().apply {
            // 回転の中心からの距離
            // 各描画領域の大きさに対し３割
            l = sideE*0.3f
            // 回転の中心からの角度
            t = 120f
            // 半径
            r = sideE * ratio
            // 色
            color = Color.RED
        }

        // 一番大きい円(黄)
        val circle2 = Circle().apply {
            // 回転の中心からの距離
            // 各描画領域の大きさに対し４割
            l = sideE*0.4f
            // 回転の中心からの角度
            t = 270f
            // 半径
            r = sideE * ratio
            // 色
            color = Color.YELLOW
        }

        circleLst.add(circle0)
        circleLst.add(circle1)
        circleLst.add(circle2)
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        angle += 10f
        if ( angle >= angleMax ) {
            angle = 0f
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


        Log.d(javaClass.simpleName,"==================================")
        // 円を描く
        circleLst.forEach { circle ->
            linePaint.color = circle.color
            // Y軸方向
            (0 until splitN).forEach { row ->
                canvas.save()
                canvas.translate(x0,y0)
                canvas.translate(-sideE/2f,sideE*(row.toFloat()+0.5f))
                // X軸方向
                (0 until splitN).forEach { col ->
                    canvas.translate(sideE, 0f)
                    val x = circle.l * MyMathUtil.cosf(circle.t+angle)
                    val y = circle.l * MyMathUtil.sinf(circle.t+angle)
                    canvas.drawCircle(x,y,circle.r,linePaint)
                    canvas.drawCircle(x,y,circle.r,framePaint)
                }
                canvas.restore()
            }
        }

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
        // 回転の中心からの距離
        // ---------------------------------
        var l: Float = 0f,
        // ---------------------------------
        // 回転の中心からの角度
        // ---------------------------------
        var t: Float = 0f,
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