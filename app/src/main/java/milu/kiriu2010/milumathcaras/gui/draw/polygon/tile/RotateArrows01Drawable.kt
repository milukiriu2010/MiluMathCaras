package milu.kiriu2010.milumathcaras.gui.draw.polygon.tile

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -----------------------------------------------------------------
// 回転する矢印
// -----------------------------------------------------------------
// https://twitter.com/justvanrossum/status/1091237538583511041
// https://forum.drawbot.com/topic/123/rotating-arrows-exercise
// -----------------------------------------------------------------
// # triggered by Mike Duggan:
// # https://twitter.com/mickduggan/status/1090577885067386880
//
// def arrow(center, size, rotation, barThickness, arrowhead):
//   with savedState():
//     translate(*center)
//     scale(size)
//     rotate(rotation)
//     points = [
//       (-0.5, barThickness/2),
//       (0.5 - arrowhead, barThickness/2),
//       (0.5 - arrowhead, 0.5),
//       (0.5, 0),
//       (0.5 - arrowhead, -0.5),
//       (0.5 - arrowhead, -barThickness/2),
//       (-0.5, -barThickness/2),
//     ]
//     polygon(*points)
//
// def arrowGrid(numArrows, arrowSize, barThickness, arrowhead, rotation):
//   for i in range(numArrows):
//     x = i * arrowSize
//     for j in range(numArrows):
//       y = j * arrowSize
//       arrow((x, y), arrowSize, rotation, barThickness, arrowhead)
//
// def easeInOut(t):
//   assert 0 <= t <= 1
//   return (1 - cos(t * pi)) / 2
//
// canvasSize = 400
// numArrows = 4
// arrowSize = canvasSize / numArrows
// barThickness = 0.5
// arrowhead = 0.5
// rotation = 180
//
// duration = 4
// framesPerSecond = 15
// numFrames = duration * framesPerSecond
//
// for frame in range(numFrames):
//   t = 4 * frame / numFrames
//   quadrant = floor(t)
//   t = easeInOut(t % 1)
//   angle = ((quadrant + t) * 90) % 380
//   flip = quadrant % 2
//   angle = -angle
//
//   newPage(canvasSize, canvasSize)
//   frameDuration(1/framesPerSecond)
//
//   if flip:
//     fill(1)
//     rect(0, 0, canvasSize, canvasSize)
//     fill(0)
//     arrowGrid(numArrows + 1, arrowSize, barThickness, arrowhead, angle)
//   else:
//     fill(0)
//     rect(0, 0, canvasSize, canvasSize)
//     fill(1)
//     translate(-arrowSize/2, -arrowSize/2)
//     arrowGrid(numArrows + 2, arrowSize, 1 - barThickness, arrowhead, angle + 180)
//
// saveImage("Arrows.gif")
// -----------------------------------------------------------------
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
                // "更新"状態
                if ( isPaused == false ) {
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

        // 90度ごとに描画する矢印の初期向きを変える
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

        // 矢印を描画する領域の一辺の長さ
        val len = side/n
        // 矢印を描画する領域の中心点
        val center = MyPointF().apply {
            x = len/2f
            y = len/2f
        }

        // ------------------------------------------
        // 矢印を縦横に描画する際のパラメータ
        // ------------------------------------------
        // 右/左向き ⇒
        //    4 x 4
        //    描画領域にぴったりあう
        // 上/下向き ⇒
        //    5 x 5
        //    端は切れた感じで描画
        // ------------------------------------------
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
                ns = 0
                ne = n
                offset = -len/2f
                backPaint.color = Color.WHITE
                linePaint.color = Color.BLACK
            }
            Direction.UP -> {
                ns = 0
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

        Log.d(javaClass.simpleName,"===[${arrow.directionA}][${System.identityHashCode(arrow)}]===============================================")
        // 矢印を描く
        (ns..ne).forEach { i ->
            (ns..ne).forEach { j ->
                // 原点(0,0)の位置
                // = (マージン,マージン)
                canvas.save()
                canvas.translate(margin+offset,margin+offset)

                canvas.translate(i*len,j*len)

                val path = Path()
                arrow.pointLst.forEachIndexed { index, myPointF ->
                    // 中心点と描画点の差分
                    val dv = myPointF.copy().subtractSelf(center)
                    // 回転し、中心点から元の座標に戻す
                    // 元の形を既に回転した形にしているのでangleは90度くくりとする
                    dv.rotate((angle.toInt()%90).toFloat()).plusSelf(center)

                    /*
                    if ( ( i == 0 ) and ( j== 0 ) ) {
                        Log.d(javaClass.simpleName,"i[$i]j[$j]idx[$index]angle[$angle]dvX[${dv.x}]dvY[${dv.y}]")
                    }
                    */

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

                pointLst.clear()

                // -----------------------------
                // 左下から反時計回りに頂点を置く
                // -----------------------------
                when (directionA) {
                    // --------------------------
                    // →
                    // --------------------------
                    Direction.RIGHT -> {
                        //Log.d(javaClass.simpleName, "*** right ***")
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
                        //Log.d(javaClass.simpleName, "*** down ***")
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
                        //Log.d(javaClass.simpleName, "*** left ***")
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
                        //Log.d(javaClass.simpleName, "*** up ***")
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