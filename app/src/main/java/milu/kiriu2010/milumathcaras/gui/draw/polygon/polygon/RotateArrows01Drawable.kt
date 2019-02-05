package milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// 回転する矢印
// ---------------------------------------------
// https://forum.drawbot.com/topic/123/rotating-arrows-exercise
// ---------------------------------------------
class RotateArrows01Drawable: MyDrawable() {

    enum class Direction {
        RIGHT,
        DOWN,
        LEFT,
        UP
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 800f
    private val margin = 0f

    // -------------------------------
    // 一辺あたりの矢印の数
    // -------------------------------
    private val n = 4

    // 矢印を新規に生成するかどうか
    //   true  => 生成する
    //   false => 生成しない
    private var isRecreate = true

    // 矢印の向き
    private var direction = Direction.RIGHT

    // 矢印
    private lateinit var arrow: Arrow

    // ---------------------------------
    // 回転の角度
    // ---------------------------------
    private var angle = 0f
    private var angleMax = 360f

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
    // 矢印を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        //strokeWidth = 5f
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
        // 矢印の初期位置設定
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 矢印の初期位置設定
                createPath()
                // 矢印を回転する
                rotatePolygon()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                handler.postDelayed(runnable, 100)
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
    // 矢印の初期位置設定
    // -------------------------------
    private fun createPath() {
        // 矢印を新規に生成しない場合、何もしないで終了
        if ( isRecreate == false ) return

        arrow = Arrow().apply {
            directionA = direction
            sideA  = side/n
        }

        isRecreate = false
    }

    // -------------------------------
    // 矢印を回転する
    // -------------------------------
    private fun rotatePolygon() {
        angle += 10f

        if ( angle.toInt()%90 == 0 ) {
            direction = when (direction) {
                Direction.RIGHT -> Direction.DOWN
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.UP -> Direction.RIGHT
            }

            isRecreate = true
        }

        if ( angle >= angleMax ) {
            angle = 0f
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        // 矢印がインスタンス化されていなければ描画はスキップする
        if ( this::arrow.isInitialized == false ) return

        val len = side/n
        val center = MyPointF().apply {
            x = len/2f
            y = len/2f
        }

        var ns = 0
        var ne = n-1
        var offset = 0f
        when (direction) {
            Direction.RIGHT -> {
                ns = 0
                ne = n-1
                offset = 0f
                backPaint.color = Color.BLACK
                linePaint.color = Color.WHITE
            }
            Direction.LEFT -> {
                ns = 0
                ne = n-1
                offset = 0f
                backPaint.color = Color.BLACK
                linePaint.color = Color.WHITE
            }
            Direction.DOWN -> {
                ns = -1
                ne = n
                offset = -len/2f
                backPaint.color = Color.WHITE
                linePaint.color = Color.BLACK
            }
            Direction.UP -> {
                ns = -1
                ne = n
                offset = -len/2f
                backPaint.color = Color.WHITE
                linePaint.color = Color.BLACK
            }
        }

        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 矢印を描く
        (ns..ne).forEach { i ->
            (ns..ne).forEach { j ->
                // 原点(0,0)の位置
                // = (マージン,マージン)
                canvas.save()
                canvas.translate(margin,margin)

                canvas.translate(i*len,j*len)

                val path = Path()
                arrow.pointLst.forEachIndexed { index, myPointF ->
                    // 中心点と描画点の差分
                    val dv = myPointF.copy().subtract(center)
                    // 回転し、中心点から元の座標に戻す
                    dv.rotate(angle).plus(center)

                    //Log.d(javaClass.simpleName,"i[$i]j[$j]index[$index]angle[$angle]dvX[${dv.x}]dvY[${dv.y}]")


                    if ( index == 0 ) {
                        path.moveTo(dv.x, dv.y)
                    }
                    else {
                        path.lineTo(dv.x, dv.y)
                    }
                }
                path.close()

                canvas.drawPath(path,linePaint)

                // 座標を元に戻す
                canvas.restore()
            }
        }


        /*
        // 矢印を描く
        val len = side/n
        val cx = len/2f
        val cy = len/2f
        val center = MyPointF(cx,cy)
        (0 until n).forEach { i ->
            (0 until n).forEach { j ->
                // 原点(0,0)の位置
                // = (マージン,マージン)
                canvas.save()
                canvas.translate(margin,margin)

                canvas.translate(i*len,j*len)

                val path = Path()
                arrow.pointLst.forEachIndexed { index, myPointF ->
                    // 中心点と描画点の差分
                    val dv = myPointF.copy().subtract(center)
                    // 回転し、中心点から元の座標に戻す
                    dv.rotate(angle).plus(center)

                    //Log.d(javaClass.simpleName,"i[$i]j[$j]index[$index]angle[$angle]dvX[${dv.x}]dvY[${dv.y}]")


                    if ( index == 0 ) {
                        path.moveTo(dv.x, dv.y)
                    }
                    else {
                        path.lineTo(dv.x, dv.y)
                    }
                }
                path.close()

                canvas.drawPath(path,linePaint)

                // 座標を元に戻す
                canvas.restore()
            }
        }
        */

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

    // --------------------------------------
    // 矢印
    // --------------------------------------
    private data class Arrow(
        // 矢印の向き
        var directionA: Direction = Direction.RIGHT,
        // 矢印の頂点リスト
        var pointLst: MutableList<MyPointF> = mutableListOf()
    ) {
        // 矢印を描く領域の大きさ
        var sideA = 200f
            set(data: Float) {
                field = data
                val p1_4 = field/4f
                val p3_4 = field*3f/4f
                val p2 = field/2f

                // -----------------------------
                // 左下から反時計回りに頂点を置く
                // -----------------------------
                when (directionA) {
                    // --------------------------
                    // →
                    // --------------------------
                    Direction.RIGHT -> {
                        pointLst.add(MyPointF(0f,p1_4))
                        pointLst.add(MyPointF(p2,p1_4))
                        pointLst.add(MyPointF(p2,0f))
                        pointLst.add(MyPointF(field,p2))
                        pointLst.add(MyPointF(p2,field))
                        pointLst.add(MyPointF(p2,p3_4))
                        pointLst.add(MyPointF(0f,p3_4))
                    }
                    // --------------------------
                    // ↓
                    // --------------------------
                    Direction.DOWN -> {
                        pointLst.add(MyPointF(p1_4,field))
                        pointLst.add(MyPointF(p1_4,p2))
                        pointLst.add(MyPointF(0f,p2))
                        pointLst.add(MyPointF(p2,0f))
                        pointLst.add(MyPointF(field,p2))
                        pointLst.add(MyPointF(p3_4,p2))
                        pointLst.add(MyPointF(p3_4,field))
                    }
                    // --------------------------
                    // ←
                    // --------------------------
                    Direction.LEFT -> {
                        pointLst.add(MyPointF(field,p3_4))
                        pointLst.add(MyPointF(p2,p3_4))
                        pointLst.add(MyPointF(p2,field))
                        pointLst.add(MyPointF(0f,p2))
                        pointLst.add(MyPointF(p2,0f))
                        pointLst.add(MyPointF(p2,p1_4))
                        pointLst.add(MyPointF(field,p1_4))
                    }
                    // --------------------------
                    // ↑
                    // --------------------------
                    Direction.UP -> {
                        pointLst.add(MyPointF(p3_4,0f))
                        pointLst.add(MyPointF(p3_4,p2))
                        pointLst.add(MyPointF(field,p2))
                        pointLst.add(MyPointF(p2,field))
                        pointLst.add(MyPointF(0f,p2))
                        pointLst.add(MyPointF(p1_4,p2))
                        pointLst.add(MyPointF(p1_4,0f))
                    }
                }
            }


    }
}