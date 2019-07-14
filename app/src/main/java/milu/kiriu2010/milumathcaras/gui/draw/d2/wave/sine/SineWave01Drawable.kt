package milu.kiriu2010.milumathcaras.gui.draw.d2.wave.sine

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// -------------------------------------------------------------------------------------
// サイン波
// -------------------------------------------------------------------------------------
//   y = k * sin(t)
// -------------------------------------------------------------------------------------
// 加法定理
// sin(a+b) = sin(a)cos(b) + cos(a)sin(b)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Sine_wave
// -------------------------------------------------------------------------------------
class SineWave01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    //private val side = 720f
    private val side = 1080f
    private val margin = 50f

    // ---------------------------------
    // サイン波の係数k
    // ---------------------------------
    //private var k = 180.0f
    private var k = 360f/(side/360f)

    // -------------------------------
    // サイン波の位相
    // -------------------------------
    private var anglePhase = 0f
    private var anglePhaseMax = 360f

    // -------------------------------
    // 描画するサイン波のリスト
    // -------------------------------
    private val waveLst: List<SineWave> = listOf(
        SineWave().apply {
            angleRotate = 0f
            linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0xffff0000.toInt()
                style = Paint.Style.STROKE
                // 円
                pathEffect =
                        PathDashPathEffect(Path().apply {
                            addCircle(10f, 10f, 10f, Path.Direction.CCW)
                        }, 30f, 0f, PathDashPathEffect.Style.ROTATE)
            }
        },
        SineWave().apply {
            angleRotate = 60f
            linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0xff00ff00.toInt()
                style = Paint.Style.STROKE
                // 四角
                pathEffect =
                        PathDashPathEffect(Path().apply {
                            addRect(0f, 0f, 20f, 20f, Path.Direction.CCW)
                        }, 30f, 0f, PathDashPathEffect.Style.ROTATE)
            }
        },
        SineWave().apply {
            angleRotate = 120f
            linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0xff0000ff.toInt()
                style = Paint.Style.STROKE
                // 正三角形
                pathEffect =
                        PathDashPathEffect(Path().apply {
                            moveTo(0f, 0f)
                            lineTo(30f, 0f)
                            lineTo(15f, 15f * sqrt(3f))
                            close()
                        }, 30f, 0f, PathDashPathEffect.Style.ROTATE)
            }
        }
    )

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
    // 使わない
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // サイン波の描画点を生成
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
                    // サイン波の位相を移動
                    movePhase()
                    // サイン波の描画点を生成
                    createPath()
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
    // サイン波の描画点を生成
    // -------------------------------
    private fun createPath() {
        // サイン波を回転させるときの中心点
        val mX = side/2f
        val mY = 0f

        // サイン波の描画点を生成
        waveLst.forEach { wave ->
            // 描画点をクリア
            wave.pointLst.clear()

            (0..side.toInt() step 10).forEach {
                // 回転前の描画点
                val x0 = it.toFloat()
                val y0 = (k * sin((x0+anglePhase)*PI/180f)).toFloat()

                // 中心点と"回転前の描画点"の距離・角度
                val len = sqrt((x0-mX)*(x0-mX)+(y0-mY)*(y0-mY))
                val angle = MyMathUtil.getAngle(MyPointF(mX,mY), MyPointF(x0,y0))

                // 回転後の描画点
                val x1 = len * cos((angle+wave.angleRotate)*PI/180f)+mX
                val y1 = len * sin((angle+wave.angleRotate)*PI/180f)

                wave.pointLst.add(MyPointF(x1.toFloat(),y1.toFloat()))
            }
        }
    }

    // -------------------------------
    // サイン波の位相を移動
    // -------------------------------
    private fun movePhase() {
        anglePhase = anglePhase + 30f
        // ・元の位置に戻す
        if ( anglePhase > anglePhaseMax ) {
            anglePhase = 0f
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
        // = (マージン,上下中央)
        val x0 = margin
        val y0 = (intrinsicHeight/2).toFloat()

        // 原点(x0,y0)を中心に円・サイクロイド曲線を描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // サイン波を描く
        waveLst.forEach { wave ->
            var path = Path()
            wave.circleLst.forEachIndexed { index, myPointF ->
                when (index) {
                    0 -> {
                        path.moveTo(myPointF.x,myPointF.y)
                    }
                    else -> {
                        path.lineTo(myPointF.x,myPointF.y)
                    }
                }
            }
            canvas.drawPath(path,wave.linePaint)
        }
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)
        // サイン波を描く
        // 1536色のグラデーション
        waveLst.forEach { wave ->
            val bunchSize = wave.pointLst.size
            var myPointF2: MyPointF? = null
            wave.pointLst.forEachIndexed { index, myPointF1 ->
                val color = myColor.create(index,bunchSize)
                wave.linePaint.color = color.toInt()
                if ( myPointF2 != null ) {
                    canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,wave.linePaint)
                }
                myPointF2 = myPointF1
            }
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
        framePaint.alpha = alpha
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun setColorFilter(colorFilter: ColorFilter?) {
        framePaint.colorFilter = colorFilter
    }

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicWidth(): Int = (side+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (side+margin*2).toInt()

    private class SineWave() {
        // -------------------------------
        // サイン波の描画点リスト
        // -------------------------------
        val pointLst: MutableList<MyPointF> = mutableListOf()
        // -----------------------------------
        // サイン波の回転角度
        // -----------------------------------
        var angleRotate: Float = 0f
        // -----------------------------------
        // サイン波のペイント
        // -----------------------------------
        lateinit var linePaint: Paint
    }
}
