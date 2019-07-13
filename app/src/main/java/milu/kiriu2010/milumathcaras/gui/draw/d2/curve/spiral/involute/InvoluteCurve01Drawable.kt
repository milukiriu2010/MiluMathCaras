package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.spiral.involute

import android.graphics.*
import android.os.Handler
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -------------------------------------------------------------------------------------
// インボリュート曲線
// -------------------------------------------------------------------------------------
//   x = a * (cos(t) + t * sin(t))
//   y = a * (sin(t) - t * cos(t))
// -------------------------------------------------------------------------------------
// https://www.mathcurve.com/courbes2d.gb/developpantedecercle/developpantedecercle.shtml
// -------------------------------------------------------------------------------------
// 描画点を回転することで螺旋を回転
// -------------------------------------------------------------------------------------
class InvoluteCurve01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // インボリュート曲線の変数a
    // ---------------------------------
    private var a = 10f

    // -------------------------------
    // インボリュート曲線の回転角度(変数tに相当)
    // -------------------------------
    private var angle = 0f
    private var angleMax = 1080f

    // -------------------------------
    // インボリュート曲線の回転方向
    // -------------------------------
    // +1:内に収束するようにみえる
    // -1:外に広がるようにみえる
    // -------------------------------
    private var sign = +1f

    // -------------------------------
    // インボリュート曲線の描画点リスト
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
    // インボリュート曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
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
    // values
    // 第１引数:インボリュート曲線の回転角度
    // 第２引数:インボリュート曲線の変数a
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // インボリュート曲線の回転角度
        angle = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // インボリュート曲線の回転角度
                0 -> angle = fl
                // インボリュート曲線の変数a
                1 -> a = fl
            }
        }

        // インボリュート曲線の描画点リストを生成
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
                    // インボリュート曲線を回転する
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

    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(event: MotionEvent) {
        //Log.d(javaClass.simpleName,"Touch:x[${x}]y[${y}]" )
        // タッチすると、回転方向を変更する
        if ( event.action == MotionEvent.ACTION_DOWN ) {
            sign = -sign
        }
    }

    // -------------------------------
    // インボリュート曲線の描画点リストを生成
    // -------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()

        // 448(=2240/5)点描画する
        (0..2240 step 5).forEach {
            val t = it.toFloat()
            val cos = MyMathUtil.cosf(t)
            val sin = MyMathUtil.sinf(t)
            val x = a*(cos+t*sin)
            val y = a*(sin-t*cos)
            pointLst.add(MyPointF(x,y))
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // インボリュート曲線を回転する
    private fun rotatePath() {
        angle += 20f

        // ３周したら、
        // ・元の角度に戻す
        // ・回転方向を変更する
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

        // 原点(x0,y0)を中心にインボリュート曲線を描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // インボリュート曲線を描く
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            // 描画点の原点からの距離
            val lenXY = sqrt(myPointF.x*myPointF.x+myPointF.y*myPointF.y)
            // 描画点のX軸に対する角度
            val angleXY = MyMathUtil.getAngle(MyPointF(0f,0f),myPointF)

            // ------------------------------------------------------------------------
            // インボリュート曲線が回転しているようにみえるするために、描画点をangle度回転させる
            // ------------------------------------------------------------------------
            // angleに+1をかけると、内に収束するようにみえる
            // angleに-1をかけると、外に広がるようにみえる
            // ------------------------------------------------------------------------
            val x1 = lenXY * cos((sign*angle+angleXY)*PI/180f)
            val y1 = lenXY * sin((sign*angle+angleXY)*PI/180f)
            if ( index == 0 ) {
                path.moveTo(x1.toFloat(),y1.toFloat())
            }
            else {
                path.lineTo(x1.toFloat(),y1.toFloat())
            }
        }
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // インボリュート曲線を描く
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            // 描画点の原点からの距離
            val lenXY = sqrt(myPointF1.x*myPointF1.x+myPointF1.y*myPointF1.y)
            // 描画点のX軸に対する角度
            val angleXY = MyMathUtil.getAngle(MyPointF(0f,0f),myPointF1)

            // ------------------------------------------------------------------------
            // インボリュート曲線が回転しているようにみえるするために、描画点をangle度回転させる
            // ------------------------------------------------------------------------
            // angleに+1をかけると、内に収束するようにみえる
            // angleに-1をかけると、外に広がるようにみえる
            // ------------------------------------------------------------------------
            val x1 = lenXY * cos((sign*angle+angleXY)*PI/180f)
            val y1 = lenXY * sin((sign*angle+angleXY)*PI/180f)
            if ( myPointF2 != null ) {
                val color = myColor.create(index,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(x1.toFloat(),y1.toFloat(),myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = MyPointF(x1.toFloat(),y1.toFloat())
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