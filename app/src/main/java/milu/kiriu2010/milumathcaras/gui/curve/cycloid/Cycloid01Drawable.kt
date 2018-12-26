package milu.kiriu2010.milumathcaras.gui.curve.cycloid

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.main.MyDrawable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// -------------------------------------------------------------------------------------
// サイクロイド曲線
// -------------------------------------------------------------------------------------
//   x = r * ( t - sin(t) )
//   y = r * ( 1 - cos(t) )
// -------------------------------------------------------------------------------------
// https://ja.wikipedia.org/wiki/%E3%82%B5%E3%82%A4%E3%82%AF%E3%83%AD%E3%82%A4%E3%83%89
// -------------------------------------------------------------------------------------
class Cycloid01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------
    // 描画する円の半径
    // -------------------------------
    private val r = 100f

    // -------------------------------
    // サイクロイド曲線の媒介変数(度)
    // -------------------------------
    private var angle = 0f

    // -------------------------------
    // サイクロイド曲線の描画点リスト
    // -------------------------------
    val pointLst = mutableListOf<MyPointF>()

    // -------------------------------
    // 描画領域として使うビットマップ
    // -------------------------------
    private val imageBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)

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
    // サイクロイド曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    // ---------------------------------------
    // サイクロイド曲線に沿う円を描くペイント
    // ---------------------------------------
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    // ---------------------------------------
    // 別スレッドで描画するためのハンドラ
    // ---------------------------------------
    val handler = Handler()

    // ---------------------------------------
    // 描画に使うスレッド
    // ---------------------------------------
    private lateinit var runnable: Runnable

    // -------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -------------------------------
    override fun cal(n: Int) {

        // サイクロイド曲線の描画点を追加
        addPoint()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 100msごとに描画
        val delayMills: Long = 100

        // 描画に使うスレッド
        runnable = Runnable {
            // サイクロイド曲線の描画点を追加
            addPoint()
            // ビットマップに描画
            drawBitmap()
            // 描画
            invalidateSelf()
            // サイクロイド曲線の描画点を移動
            movePoint()

            handler.postDelayed(runnable,delayMills)
        }
        handler.postDelayed(runnable,delayMills)
    }

    // -------------------------------------
    // CalculationCallback
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    override fun postProc() {
        // 描画に使うスレッドを解放する
        handler.removeCallbacks(runnable)
    }

    // -------------------------------
    // サイクロイド曲線の描画点を追加
    // -------------------------------
    private fun addPoint() {
        // サイクロイド曲線の描画点リストに現在の描画点
        val x = r*(angle*PI/180f-sin(angle*PI/180f)).toFloat()
        val y = r*(1-cos(angle*PI/180f)).toFloat()
        val pointNow = MyPointF(x,y)
        // サイクロイド曲線の描画点リストに現在の描画点を加える
        pointLst.add(pointNow)
    }

    // -------------------------------
    // サイクロイド曲線の描画点を移動
    // -------------------------------
    private fun movePoint() {
        // 10度ずつ移動する
        angle = angle + 10f
        Log.d(javaClass.simpleName,"angle[{$angle}]")

        // 2周したら
        // ・元の位置に戻す
        // ・サイクロイド曲線の描画点リストをクリアする
        if ( angle > 720 ) {
            angle = 0f
            pointLst.clear()
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(imageBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 原点(0,0)の位置
        // = (マージン+半径分,中央)
        val x0 = margin+r
        val y0 = (intrinsicHeight/2).toFloat()

        // X軸を描画(中央)
        canvas.save()
        canvas.translate(0f,y0)
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画("マージン＋半径分"右に移動)
        canvas.save()
        canvas.translate(x0,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // サイクロイド曲線に沿う円を描画
        // 初期    の中心座標  (0    ,r)
        // 円一周後の中心座標  (2r*PI,r)
        canvas.save()
        canvas.translate(x0,y0)
        canvas.drawCircle(2f*r*PI.toFloat()*angle/360f,r,r,circlePaint)
        canvas.restore()
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun draw(canvas: Canvas) {
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

    override fun getIntrinsicWidth(): Int = (side+margin*2).toInt()

    override fun getIntrinsicHeight(): Int = (side+margin*2).toInt()
}