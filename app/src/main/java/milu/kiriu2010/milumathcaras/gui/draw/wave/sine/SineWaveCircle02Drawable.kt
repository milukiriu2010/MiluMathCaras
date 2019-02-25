package milu.kiriu2010.milumathcaras.gui.draw.wave.sine

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -------------------------------------------------------------------------------------
// 円の周りを回転するサイン波
// -------------------------------------------------------------------------------------
//   x = (r + kr*r*sin(kc*t))*cos(t)
//   y = (r + kr*r*sin(kc*t))*sin(t)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Sine_wave
// -------------------------------------------------------------------------------------
class SineWaveCircle02Drawable: MyDrawable() {

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
    // サイン波の係数k
    // 円に対する波の大きさの比率
    // ---------------------------------
    private var kr = 0.2f

    // ---------------------------------
    // サイン波の山の個数
    // ---------------------------------
    private var kc = 6f

    // -------------------------------
    // 円の位相(係数tに相当)
    // -------------------------------
    private var angle = 0f
    private var angleMax = 720f
    // PathEffectと色を切り替える角度
    private var angleThreshHold1 = 360f

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

    // PathEffectインデックス
    private var pathEffectId = 1
    // 次のPathEffectを反映するサイン波上の位置
    private var nextPathEffectPos = 0

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
            lineTo(30f, 0f)
            lineTo(15f, 15f * sqrt(3f))
            close()
        }, 30f, 0f, PathDashPathEffect.Style.ROTATE)
    )

    // 色インスタンス作成
    //val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)
    val myColor = MyColorFactory.createInstance(ColorType.COLOR_768_DARK)
    // 色数
    val colorSize = myColor.getColorSize()


    // 色インデックス
    private var colorId = 1
    // 色を反映するサイン波上の位置
    private var nextColorPos = 0

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
                    //createPath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 10)
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
        // 描画点をクリア
        wave.pointLst.clear()
        (0..360 step 5).forEach {
            val d = it.toFloat()
            val x = (1f+kr*MyMathUtil.sinf(kc*d-90f))*r*MyMathUtil.cosf(d)
            val y = (1f+kr*MyMathUtil.sinf(kc*d-90f))*r*MyMathUtil.sinf(d)
            wave.pointLst.add(MyPointF(x,y))
        }

        // 次のPathEffectを反映するサイン波上の位置
        nextPathEffectPos = wave.pointLst.size
    }

    // -------------------------------
    // サイン波の位相を移動
    // -------------------------------
    private fun movePhase() {
        // 10度ずつが、きれいに回転して見える
        angle += 10f
        if ( angle >= angleMax ) {
            angle = 0f

            // PathEffectインデックス
            pathEffectId = (pathEffectId+1)%pathEffectLst.size
            // 次のPathEffectを反映するサイン波上の位置
            nextPathEffectPos = wave.pointLst.size
            // 色インデックス
            colorId = colorId+colorSize/6
            colorId = colorId%colorSize
            // 色を反映するサイン波上の位置
            nextColorPos = wave.pointLst.size
        }
        else if ( angle >= angleThreshHold1 ) {
            // 次のPathEffectを反映するサイン波上の位置
            nextPathEffectPos = nextPathEffectPos-1
            // 色を反映するサイン波上の位置
            nextColorPos = nextColorPos-1
        }

        //Log.d(javaClass.simpleName,"angle[$angle]nextPathEffectPos[$nextPathEffectPos]angleThreshHold1[$angleThreshHold1]")
        /**/
        // 波を回転する
        wave.pointLst.forEachIndexed { index, myPointF ->
            //if ( index == 0 ) {
            //    Log.d(javaClass.simpleName,"x[${myPointF.x}]y[${myPointF.y}]")
            //}
            // 3倍が、きれいに回転してみえる
            myPointF.rotate(angle*3f)
            //
            //if ( index == 0 ) {
            //    Log.d(javaClass.simpleName,"x[${myPointF.x}]y[${myPointF.y}]")
            //}
            //
        }
        /**/
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

        /*
        // サイン波を描く
        var myPointF2: MyPointF? = null
        wave.pointLst.forEachIndexed { index, myPointF1 ->
            // サイン波が境界を越えた場合
            if ( anglePhaseC+(index*interval) > d ) {
                wave.linePaint.pathEffect = pathEffectLst[pathEffectId0]
                val color = myColor.create(colorId0,colorSize)
                wave.linePaint.color = color.toInt()
            }
            // サイン波が境界を越えていない場合
            else {
                wave.linePaint.pathEffect = pathEffectLst[pathEffectId1]
                val color = myColor.create(colorId1,colorSize)
                wave.linePaint.color = color.toInt()
            }
            if ( myPointF2 != null ) {
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,wave.linePaint)
            }
            myPointF2 = myPointF1
        }
        */

        //Log.d(javaClass.simpleName,"nextPathEffectPos[$nextPathEffectPos]")

        // サイン波を描く
        var myPointF2: MyPointF? = null
        val bunchSize = wave.pointLst.size
        wave.pointLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                //wave.linePaint.color = myColor.create(index,bunchSize).toInt()
                wave.linePaint.pathEffect = if ( index < nextPathEffectPos ) {
                    pathEffectLst[pathEffectId]
                }
                else {
                    pathEffectLst[(pathEffectId+1)%pathEffectLst.size]
                }
                wave.linePaint.color = if ( index < nextColorPos ) {
                    myColor.create(colorId,colorSize).toInt()
                }
                else {
                    myColor.create((colorId+colorSize/6)%colorSize,colorSize).toInt()
                }

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
