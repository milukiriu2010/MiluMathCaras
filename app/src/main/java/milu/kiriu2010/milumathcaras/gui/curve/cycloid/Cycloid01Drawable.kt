package milu.kiriu2010.milumathcaras.gui.curve.cycloid

import android.graphics.*
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

    // -------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -------------------------------
    override fun cal(n: Int) {
        val canvas = Canvas(imageBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // X軸を描画
        canvas.save()
        canvas.translate(0f,(intrinsicHeight/2).toFloat())
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画
        canvas.save()
        canvas.translate(margin+r,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // サイクロイド曲線の描画点リストに現在の描画点
        val x = r*(n.toFloat()*PI/180f-sin(n.toFloat()*PI/180f)).toFloat()
        val y = r*(1-cos(n.toFloat()*PI/180f)).toFloat()
        val pointNow = MyPointF(x,y)
        // サイクロイド曲線の描画点リストに現在の描画点を加える
        pointLst.add(pointNow)


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