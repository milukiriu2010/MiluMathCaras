package milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// -------------------------------------------------------------------------------------
// トロコイド曲線
// -------------------------------------------------------------------------------------
//   x = r * (t - k * sin(t))
//   y = r * (1 - k * cos(t))
// -------------------------------------------------------------------------------------
// "|k|>1" ⇒ 螺旋を描く
// "|k|=1" ⇒ サイクロイド曲線
// "|k|<1" ⇒ 緩やかな波
// kの符号の違いは、位相が90度？異なるだけで、絶対値が同じであれば同じ形を描く
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Trochoid
// http://aau-square.hatenablog.jp/entry/2016/07/21/%E3%83%88%E3%83%AD%E3%82%B3%E3%82%A4%E3%83%89%E3%81%AE%E8%A9%B1
// -------------------------------------------------------------------------------------
class Trochoid01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // トロコイド曲線に接する円の半径
    // ---------------------------------
    // 円が２回転するのに必要な長さ
    //   = 2*(2PI * r) + 2r
    //   = 2r(2*PI+1)
    // ---------------------------------
    //private val r = 70f
    private val r = (side/2f/(2*PI+1f)).toFloat()

    // ---------------------------------
    // トロコイド曲線の係数k
    // ---------------------------------
    // "|k|>1" ⇒ 螺旋を描く
    // "|k|=1" ⇒ サイクロイド曲線
    // "|k|<1" ⇒ 緩やかな波
    // ---------------------------------
    private var k = 2.0f
    //private var k = -1.0f
    //private var k = -2.0f

    // -------------------------------
    // 現在地として描画する円の半径
    // -------------------------------
    private val nr = 10f

    // -------------------------------
    // トロコイド曲線の媒介変数(度)
    // -------------------------------
    private var angle = 0f
    private var angleMax = 720f

    // -------------------------------
    // トロコイド曲線の描画点リスト
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
    // トロコイド曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    // ---------------------------------------
    // トロコイド曲線の現在値を描くペイント
    // ---------------------------------------
    private val lineNowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    // ---------------------------------------
    // トロコイド曲線に沿う円を描くペイント
    // ---------------------------------------
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    // -----------------------------------------------
    // トロコイド曲線に沿う円の現在値を描くペイント
    // -----------------------------------------------
    private val circleNowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
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
    // 第１引数:媒介変数の初期位置(通常は0度)
    // 第２引数:トロコイド曲線の係数k
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 媒介変数の初期位置
        var angleInit = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 媒介変数の初期位置
                0 -> angleInit = fl
                // トロコイド曲線の係数k
                1 -> k = fl
            }
        }

        // トロコイド曲線の描画点を追加
        addPoint()
        // 初期位置に車で描画点を追加する
        while ( angle < angleInit )  {
            // トロコイド曲線の描画点を移動
            movePoint()
            // トロコイド曲線の描画点を追加
            addPoint()
        }
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // トロコイド曲線の描画点を移動
                movePoint()
                // トロコイド曲線の描画点を追加
                addPoint()
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
    // トロコイド曲線の描画点を追加
    // -------------------------------
    private fun addPoint() {
        // トロコイド曲線の描画点リストに現在の描画点
        val x = r*(angle*PI/180f-k*sin(angle*PI/180f)).toFloat()
        val y = r*(1-k*cos(angle*PI/180f)).toFloat()
        // トロコイド曲線の描画点リストに現在の描画点を加える
        val pointNow = MyPointF(x,y)
        pointLst.add(pointNow)

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // -------------------------------
    // トロコイド曲線の描画点を移動
    // -------------------------------
    private fun movePoint() {
        // 10度ずつ移動する
        angle = angle + 10f
        //Log.d(javaClass.simpleName,"angle[{$angle}]")

        // 2周したら
        // ・元の位置に戻す
        // ・トロコイド曲線の描画点リストをクリアする
        if ( angle > angleMax ) {
            angle = 0f
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
        // = (マージン+半径分,上下中央)
        val x0 = margin+r
        val y0 = (intrinsicHeight/2).toFloat()

        // X軸を描画(上下中央)
        canvas.save()
        canvas.translate(0f,y0)
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画("マージン＋半径分"右に移動)
        canvas.save()
        canvas.translate(x0,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // 原点(x0,y0)を中心に円・トロコイド曲線を描く
        canvas.save()
        canvas.translate(x0,y0)
        // トロコイド曲線に沿う円を描画
        // 初期    の中心座標  (0    ,r)
        // 円一周後の中心座標  (2r*PI,r)
        canvas.drawCircle(2f*r*PI.toFloat()*angle/360f,r,r,backPaint)
        canvas.drawCircle(2f*r*PI.toFloat()*angle/360f,r,r,circlePaint)

        // トロコイド曲線の現在値を取得
        val nowPointF = when ( pointLst.size ) {
            0 -> MyPointF(0f,0f)
            else -> pointLst[pointLst.size-1]
        }

        // "円の中心"と"トロコイド曲線の現在値"を結ぶ
        canvas.drawLine(2f*r*PI.toFloat()*angle/360f,r,nowPointF.x,nowPointF.y,circlePaint)

        // トロコイド曲線を描く
        val path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            if ( index == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        canvas.drawPath(path,linePaint)

        // トロコイド曲線に沿う円の中心の現在値をドットで描く
        canvas.drawCircle(2f*r*PI.toFloat()*angle/360f,r,nr,circleNowPaint)

        // トロコイド曲線の現在値をドットで描く
        canvas.drawCircle(nowPointF.x,nowPointF.y,nr,lineNowPaint)

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