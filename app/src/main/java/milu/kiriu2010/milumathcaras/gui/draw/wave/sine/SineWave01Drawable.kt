package milu.kiriu2010.milumathcaras.gui.draw.wave.sine

import android.graphics.*
import android.os.Handler
import android.support.v4.math.MathUtils
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
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
    private var k = 360f/(1080f/360f)

    // -------------------------------
    // サイン波の位相
    // -------------------------------
    private var anglePhase = 0f
    private var anglePhaseMax = 360f

    // -------------------------------
    // サイン波の描画点リスト
    // -------------------------------
    val pointLst = mutableListOf<MyPointF>()

    // -----------------------------------
    // サイン波の回転角度
    // リストに加えた数だけサイン波を描く
    // -----------------------------------
    val angleRotateLst = floatArrayOf(0f,60f,120f)

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

    //
    private val circlePath = Path().apply {
        addCircle(10f,10f,10f,Path.Direction.CCW)
    }

    // -------------------------------
    // サイン波を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
        // 円を30ごとに描く
        pathEffect = PathDashPathEffect(circlePath,30f,0f,PathDashPathEffect.Style.ROTATE)
    }

    // -------------------------------
    // サイン波を描くときの色リスト
    // -------------------------------
    private val colorLst = intArrayOf(
        0xffff0000.toInt(),
        0xff00ff00.toInt(),
        0xff0000ff.toInt()
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
        pointLst.clear()

        // サイン波を回転させるときの中心点
        val mX = side/2f
        val mY = 0f

        angleRotateLst.forEach { angleRotate ->
            (0..side.toInt() step 10).forEach {
                // 回転前の描画点
                val x0 = it.toFloat()
                val y0 = (k * sin((x0+anglePhase)*PI/180f)).toFloat()

                // 中心点と"回転前の描画点"の距離・角度
                val len = sqrt((x0-mX)*(x0-mX)+(y0-mY)*(y0-mY))
                val angle = MyMathUtil.getAngle(MyPointF(mX,mY), MyPointF(x0,y0))

                // 回転後の描画点
                val x1 = len * cos((angle+angleRotate)*PI/180f)+mX
                val y1 = len * sin((angle+angleRotate)*PI/180f)

                pointLst.add(MyPointF(x1.toFloat(),y1.toFloat()))
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

        // 描画点の数
        val cnt = pointLst.size/angleRotateLst.size

        // サイン波を描く時の色
        var n = 0

        // サイン波を描く
        var path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            when (index%cnt) {
                0 -> {
                    path = Path()
                    path.moveTo(myPointF.x,myPointF.y)
                }
                cnt-1 -> {
                    path.lineTo(myPointF.x,myPointF.y)
                    linePaint.color = colorLst[(n++%3)]
                    canvas.drawPath(path,linePaint)
                }
                else -> {
                    path.lineTo(myPointF.x,myPointF.y)
                }
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