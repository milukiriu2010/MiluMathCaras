package milu.kiriu2010.milumathcaras.gui.draw.wave.sine

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// -------------------------------------------------------------------------------------
// 円の周りを回転するサイン波
// -------------------------------------------------------------------------------------
//   y = k * sin(t)
// -------------------------------------------------------------------------------------
// 加法定理
// sin(a+b) = sin(a)cos(b) + cos(a)sin(b)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Sine_wave
// -------------------------------------------------------------------------------------
class SineWaveCircle01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1080f
    private val margin = 50f

    // ---------------------------------
    // サイン波が回転する円の半径
    // ---------------------------------
    private var r = side/3f

    // ---------------------------------
    // サイン波の係数k(波の大きさ)
    // ---------------------------------
    //private var k = side/6f
    private var k = side/24f

    // ---------------------------------
    // サイン波を描画する全体の角度
    // ---------------------------------
    private val d = 120
    //private val d = 180
    //private val d = 360

    // -------------------------------
    // サイン波の位相(係数tに相当)
    // -------------------------------
    private var anglePhase = 0f
    private var anglePhaseMax = 360f

    // 描画点を描く間隔(5度)
    private val interval = 5

    // 描画するサイン波
    private val wave =
        SineWave().apply {
            angleRotate = 0f
            linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0xffff0000.toInt()
                style = Paint.Style.STROKE
                strokeWidth = 5f
            }
        }

    // PathEffectのインデックス
    private var effectId0 = 0
    // PathEffectのインデックス(境界を超えていない場合は、前のPathEffectを使う)
    private var effectId1 = 0

    // 描画に使うPathEffectのリスト
    private val pathEffectLst: List<PathEffect> = listOf(
        // 円
        PathDashPathEffect(Path().apply {
            addCircle(10f, 10f, 10f, Path.Direction.CCW)
        }, 30f, 0f, PathDashPathEffect.Style.ROTATE),
         // 四角
        PathDashPathEffect(Path().apply {
            addRect(0f, 0f, 20f, 20f, Path.Direction.CCW)
        }, 30f, 0f, PathDashPathEffect.Style.ROTATE),
        // 正三角形
        PathDashPathEffect(Path().apply {
            moveTo(0f, 0f)
            lineTo(20f, 0f)
            lineTo(10f, 10f * sqrt(3f))
            close()
        }, 30f, 0f, PathDashPathEffect.Style.ROTATE)
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
        // サイン波を回転させるときの中心点
        val mX = side/2f
        val mY = side/2f

        /*
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
        */

        /* トロコロイド
        // d = 120
        val dv = 1080f/d
        // サイン波の描画点を生成
        // 描画点をクリア
        wave.pointLst.clear()

        (0..d).forEach {
            // "円の中心"に対する"描画線"の角度
            val t0 = it.toFloat() + anglePhase - d.toFloat()

            //val h = k*sin(it.toFloat()*dv*PI/180f).toFloat()
            val h = k*sin((it.toFloat()*dv+anglePhase)*PI/180f).toFloat()

            // 描画点
            val x0 = r*cos(t0*PI/180f)+h*cos(it.toFloat()*dv*PI/180f)
            val y0 = r*sin(t0*PI/180f)+h*sin(it.toFloat()*dv*PI/180f)

            wave.pointLst.add(MyPointF(x0.toFloat(),y0.toFloat()))
        }
        */

        /*
        くちびるみたいな形
        val dv = 1080f/d
        // サイン波の描画点を生成
        waveLst.forEach { wave ->
            // 描画点をクリア
            wave.pointLst.clear()

            (0..d).forEach {
                // "円の中心"に対する"描画線"の角度
                val t0 = it.toFloat() + anglePhase - d.toFloat()

                val h = k*sin(it.toFloat()*dv*PI/180f).toFloat()

                // 描画点
                val x0 = r*cos(t0*PI/180f)+h
                val y0 = r*sin(t0*PI/180f)+h*sin(it.toFloat()*dv*PI/180f)

                wave.pointLst.add(MyPointF(x0.toFloat(),y0.toFloat()))
            }
        }
        */

        /*
        花びら
        d=360
        val dv = 1080f/d
        // サイン波の描画点を生成
        waveLst.forEach { wave ->
            // 描画点をクリア
            wave.pointLst.clear()

            (0..d).forEach {
                // "円の中心"に対する"描画線"の角度
                val t0 = it.toFloat() + anglePhase - d.toFloat()

                val r0 = k*sin(it.toFloat()*dv*PI/180f).toFloat()

                // 描画点
                val x0 = (r+r0)*cos(t0*PI/180f)
                val y0 = (r+r0)*sin(t0*PI/180f)

                wave.pointLst.add(MyPointF(x0.toFloat(),y0.toFloat()))
            }
        }
        */

        /**/
        // 長さdを描く間にサイン波を2周する
        val dv = 720f/d
        //val dv = 1080f/d
        // サイン波の描画点を生成
        // 描画点をクリア
        wave.pointLst.clear()
        (0..d step interval).forEach {
            // "円の中心"に対する"描画線"の角度
            val t0 = it.toFloat() + anglePhase

            //val r0 = k*sin(it.toFloat()*dv*PI/180f).toFloat()
            //val r0 = k*sin((it.toFloat()*dv+anglePhase)*2f*PI/180f).toFloat()
            val r0 = k*sin((it.toFloat()*dv+anglePhase)*PI/180f).toFloat()
            //val r0 = k*sin((it.toFloat()/d)*2f*PI*r/3f).toFloat()
            //val r0 = k*(it%2)
            //val r0 = k*(it%3)

            // 描画点
            val x0 = (r+r0)*cos(t0*PI/180f)
            val y0 = (r+r0)*sin(t0*PI/180f)

            wave.pointLst.add(MyPointF(x0.toFloat(),y0.toFloat()))
        }
        /**/


    }

    // -------------------------------
    // サイン波の位相を移動
    // -------------------------------
    private fun movePhase() {
        anglePhase = anglePhase + 6f
        // ・元の位置に戻す
        if ( anglePhase > anglePhaseMax ) {
            anglePhase = 0f
            effectId0++
            effectId0 = effectId0%pathEffectLst.size
        }
        // 境界を超えたらPathEffectを合わせる
        if ( anglePhase > d ) {
            effectId1 = effectId0%pathEffectLst.size
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
        // = (左右中央,上下中央)
        val x0 = (intrinsicWidth/2).toFloat()
        val y0 = (intrinsicHeight/2).toFloat()

        // 原点(x0,y0)を中心に円・サイクロイド曲線を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)
        // サイン波を描く
        // 1536色のグラデーション
        val bunchSize = wave.pointLst.size
        var myPointF2: MyPointF? = null
        wave.pointLst.forEachIndexed { index, myPointF1 ->
            val color = myColor.create((index+anglePhase.toInt())%bunchSize,bunchSize)
            //val color = myColor.create(index,bunchSize)
            wave.linePaint.color = color.toInt()
            if ( anglePhase+(index*interval) > d ) {
                wave.linePaint.pathEffect = pathEffectLst[effectId0]
            }
            else {
                wave.linePaint.pathEffect = pathEffectLst[effectId1]
            }
            if ( myPointF2 != null ) {
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,wave.linePaint)
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
