package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.lissajous

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -------------------------------------------------------------------------------------
// リサージュ曲線
// -------------------------------------------------------------------------------------
//   x = a * sin(p*t+d)
//   y = b * sin(q*t)
// -------------------------------------------------------------------------------------
// (1) a:b=1:1, d=90度 ⇒ 円
// (2) a:b=1:2, d=45度 ⇒ 放物線
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Lissajous_curve
// https://www.mathcurve.com/courbes2d.gb/lissajous/lissajous.shtml
// -------------------------------------------------------------------------------------
class Lissajous01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // リサージュ曲線の変数a
    // ---------------------------------
    private var a = side/2f
    // ---------------------------------
    // リサージュ曲線の変数b
    // ---------------------------------
    private var b = side/2f
    // ---------------------------------
    // リサージュ曲線の変数p
    // ---------------------------------
    private var p = 3f
    // ---------------------------------
    // リサージュ曲線の変数q
    // ---------------------------------
    private var q = 4f

    // ----------------------------------
    // リサージュ曲線の位相(変数dに相当)
    // ----------------------------------
    private var angle = 0f
    private var angleMax = 720f

    // -------------------------------
    // リサージュ曲線の描画点リスト
    // -------------------------------
    val pointLst = mutableListOf<MyPointF>()

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
    // リサージュ曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // -------------------------------
    // リサージュ曲線を描く色リスト
    // -------------------------------
    private val colorLst = arrayOf(
        // 0:red
        0xffff0000.toInt(),
        // 1:pink
        0xffffcccc.toInt(),
        // 2:orange
        0xffff7f00.toInt(),
        // 3:maroon
        0xff800000.toInt(),
        // 4:yellow
        0xffffff00.toInt(),
        // 5:green(lime)
        0xff00ff00.toInt(),
        // 6:green
        0xff008000.toInt(),
        // 7:naby
        0xff000080.toInt(),
        // 8:blue
        0xff0000ff.toInt(),
        // 9:cyan
        0xff00ffff.toInt(),
        // 10:indigo
        0xff6f00ff.toInt(),
        // 11:violet
        0xffff00ff.toInt()
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
    // values
    // 第１引数:リサージュ曲線の変数p
    // 第２引数:リサージュ曲線の変数q
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // リサージュ曲線の変数p
                0 -> p = fl
                // リサージュ曲線の変数q
                1 -> q = fl
            }
        }

        // リサージュ曲線の描画点リストを生成
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
                    // リサージュ曲線の描画点リストを生成
                    createPath()
                    // リサージュ曲線を回転する
                    rotatePath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 最初と最後は1秒後に描画
                    if (angle == angleMax || angle == 0f) {
                        handler.postDelayed(runnable, 1000)
                    }
                    // 100msごとに描画
                    else {
                        handler.postDelayed(runnable, 100)
                    }
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

    // -----------------------------------
    // リサージュ曲線の描画点リストを生成
    // -----------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        (0..360 step 1).forEach {
            val x = a*sin((p*it.toFloat()+angle)*PI/180f)
            val y = b*sin(q*it.toFloat()*PI/180f)
            pointLst.add(MyPointF(x.toFloat(),y.toFloat()))
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // リサージュ曲線を回転する
    private fun rotatePath() {
        angle += 5f

        // ２回転したら
        // ・元の角度に戻す
        if ( angle > angleMax ) {
            angle = 0f
        }
        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
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
        val x0 = intrinsicWidth/2f
        val y0 = intrinsicHeight/2f

        // X軸を描画(上下中央)
        canvas.save()
        canvas.translate(0f,y0)
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画(左右中央)
        canvas.save()
        canvas.translate(x0,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // 原点(x0,y0)を中心に描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // リサージュ曲線を描く
        // 赤色で線を描く
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            when (index) {
                0 -> path.moveTo(myPointF.x,myPointF.y)
                else -> path.lineTo(myPointF.x,myPointF.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)
        */

        /*
        // リサージュ曲線を描く
        // 30度ずつ色を変える
        val bunchSize = circleLst.size/colorLst.size
        var colorPos = -1
        var path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            when (index%bunchSize) {
                0 -> {
                    if ( colorPos >= 0) {
                        linePaint.color = colorLst[colorPos%colorLst.size]
                        path.lineTo(myPointF.x,myPointF.y)
                        canvas.drawPath(path,linePaint)
                    }
                    colorPos++
                    path = Path()
                    path.moveTo(myPointF.x,myPointF.y)
                }
                else -> path.lineTo(myPointF.x,myPointF.y)
            }
        }
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // リサージュ曲線を描く
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                val color = myColor.create(index,bunchSize)
                //Log.d(javaClass.simpleName,"index[$index]bunchSize[$bunchSize]color[${"0x%08x".format(color)}]")
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
        }

        // 座標を元に戻す
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

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicWidth(): Int = (side+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (side+margin*2).toInt()
}