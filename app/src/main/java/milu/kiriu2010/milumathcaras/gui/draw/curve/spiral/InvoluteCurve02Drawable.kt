package milu.kiriu2010.milumathcaras.gui.draw.curve.spiral

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
// インボリュート曲線のタイリング
// -------------------------------------------------------------------------------------
//   x = a * (cos(t) + t * sin(t))
//   y = a * (sin(t) - t * cos(t))
// -------------------------------------------------------------------------------------
// https://www.mathcurve.com/courbes2d.gb/developpantedecercle/developpantedecercle.shtml
// -------------------------------------------------------------------------------------
class InvoluteCurve02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // ---------------------------------
    // インボリュート曲線の変数a
    // ---------------------------------
    private var a = 50f

    // -------------------------------
    // インボリュート曲線の回転角度(変数tに相当)
    // -------------------------------
    private var angle = 0f
    private var angleMax1 = 360f
    private var angleMax2 = 260f
    private var angleDv = 5f
    private var angleN = (angleMax2/angleDv).toInt()+1

    // -------------------------------
    // インボリュート曲線を描く色
    // -------------------------------
    private var colorId = 0


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

    // -------------------------------
    // 三角形を描くペイント
    // -------------------------------
    private val drawPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xffff8800.toInt()
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
    // values
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
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

                    handler.postDelayed(runnable, 50)
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

        val d = 120f
        (0..5).forEach { i ->
            val rev = if ( i%2 == 0 ) 1f else -1f
            val df = i.toFloat()*d
            (0..angleMax2.toInt() step angleDv.toInt()).forEach { t ->
                val tf = t.toFloat()
                val cos = MyMathUtil.cosf(tf+df)
                val sin = MyMathUtil.sinf(tf+df)
                val x = a*(cos+2f*PI.toFloat()*tf*sin/360f)
                val y = a*(sin-2f*PI.toFloat()*tf*cos/360f)*rev
                pointLst.add(MyPointF(x,y))
            }
        }
    }

    // インボリュート曲線を回転する
    private fun rotatePath() {
        angle += angleDv
        colorId++

        if ( angle >= angleMax1 ) {
            angle = 0f
            colorId = 0
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

        // 回転後のインボリュート曲線の描画点
        val rotateLst = mutableListOf<MyPointF>()
        // 三角形用描画点
        val pointTriangle = mutableListOf<MyPointF>()
        pointLst.forEachIndexed { id, p1 ->
            // 描画点の原点からの距離
            val lenXY = sqrt(p1.x*p1.x+p1.y*p1.y)
            // 描画点のX軸に対する角度
            val angleXY = MyMathUtil.getAngle(MyPointF(0f,0f),p1)

            // ------------------------------------------------------------------------
            // インボリュート曲線が回転しているようにみえるするために、描画点をangle度回転させる
            // ------------------------------------------------------------------------
            // angleに+1をかけると、内に収束するようにみえる
            // angleに-1をかけると、外に広がるようにみえる
            // ------------------------------------------------------------------------
            val x1 = lenXY * cos((sign*angle+angleXY)*PI/180f)
            val y1 = lenXY * sin((sign*angle+angleXY)*PI/180f)

            rotateLst.add(MyPointF(x1.toFloat(),y1.toFloat()))
            if ( ( id%angleN == 0 ) and ( (id/angleN)%2 == 0 ) ) {
                pointTriangle.add(MyPointF(x1.toFloat(),y1.toFloat()))
            }
        }

        val path = Path()
        pointTriangle.forEachIndexed { id, p ->
            when (id) {
                0 -> path.moveTo(p.x,p.y)
                else -> path.lineTo(p.x,p.y)
            }
        }
        path.close()

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // インボリュート曲線を描く
        // 1536色のグラデーション
        val bunchSize = angleN
        var p2: MyPointF? = null
        //val d = 6f
        val d = (1+2f*PI.toFloat()*260f/360f)
        (0..4).forEach { row ->
            val rowf = row.toFloat()
            canvas.save()
            canvas.translate(x0,y0)
            canvas.translate(-a*d,rowf*a*d)
            (0..4).forEach { col ->
                val colf = col.toFloat()
                canvas.translate(a*d,0f)

                // インボリュート曲線を描く
                rotateLst.forEachIndexed { id, p1 ->
                    if ( p2 != null ) {
                        val color = myColor.create((id+colorId)%angleN,bunchSize)
                        linePaint.color = color.toInt()
                        canvas.drawLine(p1.x.toFloat(),p1.y.toFloat(),p2?.x!!,p2?.y!!,linePaint)
                    }

                    p2 = when ( id%angleN ) {
                        angleN-1 -> null
                        else -> p1
                    }
                }

                // 三角形を描く
                drawPaint.color = myColor.create((colorId)%angleN,bunchSize).toInt()
                canvas.drawPath(path,drawPaint)
            }
            // 座標を元に戻す
            canvas.restore()
        }

        // これまでの描画はテンポラリのため実像に描画する
        val matrix = Matrix()
        matrix.postScale(1f,1f)
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