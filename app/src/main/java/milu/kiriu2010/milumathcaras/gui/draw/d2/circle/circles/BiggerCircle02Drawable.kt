package milu.kiriu2010.milumathcaras.gui.draw.d2.circle.circles

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// --------------------------------------------
// だんだん大きくなる円
// --------------------------------------------
// 7回転する
// --------------------------------------------
// http://logo.twentygototen.org/NsVT04Kn
// --------------------------------------------
class BiggerCircle02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 回転数
    // ---------------------------------
    private val nMax = 7

    // ---------------------------------
    // 中心移動
    // ---------------------------------
    private var m = 0
    private val mMax = 2

    // ---------------------------------
    // 円の半径
    // ---------------------------------
    private var r = (side/2f/nMax.toFloat()).toFloat()

    // -------------------------------
    // 円弧の描画角度
    // -------------------------------
    private var angleInit = 180f
    private var angle = 0f
    private var angleDv = 5f
    private var angleMax = 360f*nMax.toFloat()*mMax.toFloat()

    // -------------------------------
    // 円の描画点リスト
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
    // 円を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
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

    // -----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:描画角度の初期位置
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 描画角度の初期位置
        angleInit = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 描画角度の初期位置
                0 -> angleInit = fl
            }
        }

        // 初期位置まで描画点を追加する
        while ( angle < angleInit )  {
            // 円の描画点を追加
            addPoint()
            // 円の描画点を移動
            movePoint()
        }
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 円の描画点を追加
                    addPoint()
                    // 円の描画点を移動
                    movePoint()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 最初と最後は1秒後に描画
                    if (angle == angleMax || angle == 0f) {
                        handler.postDelayed(runnable, 1000)
                    }
                    // 10msごとに描画
                    else {
                        handler.postDelayed(runnable, 10)
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

    // -------------------------------
    // 円の描画点を追加
    // -------------------------------
    private fun addPoint() {
        val x = when (m%mMax) {
            // 左寄せ
            0 -> r+r*cos((angle+180f)*PI/180f).toFloat()
            // 右寄せ
            1 -> side-r+r*cos((angle)*PI/180f).toFloat()
            else -> 0f
        }
        val y = r*sin(angle*PI/180f).toFloat()
        pointLst.add(MyPointF(x,y))
    }

    // -------------------------------
    // 円の描画点を移動
    // -------------------------------
    private fun movePoint() {
        // １周するたびに円の半径を大きくする
        // ただし、中心移動が発生すると、最初の大きさに戻る
        if ( angle.toInt()%360 == 0 ) {
            m = (angle.toInt()/360)/nMax
            r = (angle.toInt()/360%nMax+1).toFloat()*(side/2f/nMax.toFloat())
        }

        // 10度ずつ移動する
        angle = angle + angleDv
        //Log.d(javaClass.simpleName,"angle[{$angle}]")


        // 最大角度を超えたら
        // ・元の位置に戻す
        // ・円の半径を戻す
        // ・中心移動をもとの位置に戻す
        // ・描画点リストをクリアする
        if ( angle > angleMax ) {
            angle = 0f
            r = side/2f/7f
            m = 0
            pointLst.clear()
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

        // 原点(x0,y0)を中心に円を描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // 円を描く
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            if ( index == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)
        // 円を描く
        val path = Path()
        val bunchSize = 360/angleDv.toInt()
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            val color = myColor.create(index%bunchSize,bunchSize)
            linePaint.color = color.toInt()
            if ( myPointF2 != null) {
                // 中心位置が移動した場合、点同士を繋げないようにするために
                // 離れすぎている場合は、描画しないようにしている
                val myPointFDiff = myPointF1.copy().subtractSelf(myPointF2!!)
                if ( myPointFDiff.magnitude() < r ) {
                    canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
                }
            }
            myPointF2 = myPointF1
        }
        canvas.drawPath(path,linePaint)

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