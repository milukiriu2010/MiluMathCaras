package milu.kiriu2010.milumathcaras.gui.curve.hypocycloid

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.main.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

// -------------------------------------------------------------------------------------
// ハイポサイクロイド曲線
// -------------------------------------------------------------------------------------
//   x = r * (k-1) * cos(t) + r * cos((k-1)*t)
//   y = r * (k-1) * sin(t) - r * sin((k-1)*t)
// -------------------------------------------------------------------------------------
//   k = 3.0 => 三芒形/三尖形(deltoid)
//   k = 4.0 => アステロイド(asteroid)
// -------------------------------------------------------------------------------------
// https://en.wikipedia.org/wiki/Hypocycloid
// -------------------------------------------------------------------------------------
class Hypocycloid01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------------------
    // ハイポサイクロイド曲線描画に使う外円の半径
    // -------------------------------------------
    private val rR = 500f

    // -------------------------------------------
    // ハイポサイクロイド曲線描画に使う内円の係数
    // -------------------------------------------
    private var k = 4.0f

    // -------------------------------
    // 現在地として描画する円の半径
    // -------------------------------
    private val nr = 10f

    // ------------------------------------
    // ハイポサイクロイド曲線の媒介変数(度)
    // ------------------------------------
    private var angle = 0f
    private var angleMax = 360f

    // ------------------------------------
    // ハイポサイクロイド曲線の描画点リスト
    // ------------------------------------
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
    // サイクロイド曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    // ---------------------------------------
    // サイクロイド曲線の現在値を描くペイント
    // ---------------------------------------
    private val lineNowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    // ---------------------------------------
    // サイクロイド曲線に沿う円を描くペイント
    // ---------------------------------------
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    // -----------------------------------------------
    // サイクロイド曲線に沿う円の現在値を描くペイント
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

    // -------------------------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -------------------------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:媒介変数の初期位置(通常は0度)
    // 第２引数:ハイポサイクロイド曲線描画に使う内円の係数
    // -------------------------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 媒介変数の初期位置
        var angleInit = 0f
        // 可変変数 values を初期値として、このクラスで使う変数に当てはめる
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 媒介変数の初期位置
                0 -> angleInit = fl
                // ハイポサイクロイド曲線描画に使う内円の係数
                1 -> k = fl
            }
        }

        // ハイポサイクロイド曲線の媒介変数(度)の最大値を求める
        calAngleMax()

        // ハイポサイクロイド曲線の描画点を追加
        addPoint()
        // 初期位置に車で描画点を追加する
        while ( angle < angleInit )  {
            // ハイポサイクロイド曲線の描画点を移動
            movePoint()
            // ハイポサイクロイド曲線の描画点を追加
            addPoint()
        }
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // ハイポサイクロイド曲線の描画点を移動
                movePoint()
                // ハイポサイクロイド曲線の描画点を追加
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

    // -----------------------------------------------------
    // ハイポサイクロイド曲線の媒介変数(度)の最大値を求める
    // -----------------------------------------------------
    private fun calAngleMax() {
        // ハイポサイクロイド曲線描画に使う内円の係数の小数点以下の桁数
        val numOfDecimals = MyMathUtil.getNumberOfDecimals(k)
        //Log.d(javaClass.simpleName,"numOfDecimals[$numOfDecimals]")

        // "ハイポサイクロイド曲線描画に使う内円の係数"が整数となるよう補正する値(分母)
        val kD = 10f.pow(numOfDecimals).toInt()
        var kd = kD

        // "ハイポサイクロイド曲線描画に使う内円の係数"が整数となるよう補正された値(分子)
        val kN = (k * kD).toInt()
        var kn = kN

        // ----------------------------------------------------------------------------
        // k=2.1 => kD=10,kN=21
        // k=3.8 => kD=10,kN=38 => kd=5,kn=19
        // k=5.5 => kD=10,kN=55 => kd=2,kn=11
        // k=7.2 => kD=10,kN=72 => kd=5,kn=36
        //   kd:ハイポサイクロイド曲線を描く点が元の位置に戻るために外円を周回する回数
        //   kn:ハイポサイクロイド曲線を描く点が外円と接する回数
        // ----------------------------------------------------------------------------
        (1..numOfDecimals).forEach {
            // 分母・分子ともに2で割り切れれば、2で割る
            if ( (kd%2 == 0) and (kn%2 == 0) ) {
                kd=kd/2
                kn=kn/2
            }
            // 分母・分子ともに5で割り切れれば、5で割る
            if ( (kd%5 == 0) and (kn%5 == 0) ) {
                kd=kd/5
                kn=kn/5
            }
        }

        angleMax = 360f * kd.toFloat()
        //Log.d(javaClass.simpleName,"angleMax[$angleMax]")
    }

    // ------------------------------------
    // ハイポサイクロイド曲線の描画点を追加
    // ------------------------------------
    private fun addPoint() {
        // ハイポサイクロイド曲線を描く内円の半径
        val rR1 = rR/k

        // ハイポサイクロイド曲線の描画点リストに現在の描画点
        val x = (rR - rR1) * cos(angle*PI/180f).toFloat() + rR1 * cos((k-1)*angle*PI/180f).toFloat()
        val y = (rR - rR1) * sin(angle*PI/180f).toFloat() - rR1 * sin((k-1)*angle*PI/180f).toFloat()
        // サイクロイド曲線の描画点リストに現在の描画点を加える
        val pointNow = MyPointF(x,y)
        pointLst.add(pointNow)

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // -------------------------------
    // サイクロイド曲線の描画点を移動
    // -------------------------------
    private fun movePoint() {
        // 5度ずつ移動する
        angle = angle + 5f
        //Log.d(javaClass.simpleName,"angle[{$angle}]")

        // 最大値を超えたら
        // ・元の位置に戻す
        // ・サイクロイド曲線の描画点リストをクリアする
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
        // = (左右中央,上下中央)
        val x0 = (intrinsicWidth/2).toFloat()
        val y0 = (intrinsicHeight/2).toFloat()

        // X軸を描画(上下中央)
        canvas.save()
        canvas.translate(0f,y0)
        canvas.drawLine(0f,0f,intrinsicWidth.toFloat(),0f, framePaint)
        canvas.restore()

        // Y軸を描画(左右中央)
        canvas.save()
        canvas.translate(x0,0f)
        canvas.drawLine(0f,0f,0f,intrinsicHeight.toFloat(), framePaint)
        canvas.restore()

        // 原点(x0,y0)を中心に円・ハイポサイクロイド曲線を描く
        canvas.save()
        canvas.translate(x0,y0)

        // ハイポサイクロイド曲線を描く外円を描画
        canvas.drawCircle(0f,0f,rR,framePaint)

        // ハイポサイクロイド曲線を描く内円の半径
        val rR1 = rR/k
        // ハイポサイクロイド曲線を描く内円の中心が軌道を描く円の半径
        val rR2 = rR - rR1
        // ハイポサイクロイド曲線を描く内円の中心が軌道を描く円の媒介変数
        val t2 = angle*PI.toFloat()/180f
        // ハイポサイクロイド曲線を描く内円を描画
        // 初期    の中心座標  (rR - rR/k,0)
        canvas.drawCircle(rR2*cos(t2),rR2*sin(t2),rR1,backPaint)
        canvas.drawCircle(rR2*cos(t2),rR2*sin(t2),rR1,circlePaint)

        // ハイポサイクロイド曲線の現在値を取得
        val nowPointF = when ( pointLst.size ) {
            0 -> MyPointF(0f,0f)
            else -> pointLst[pointLst.size-1]
        }

        // "円の中心"と"ハイポサイクロイド曲線の現在値"を結ぶ
        canvas.drawLine(rR2*cos(t2),rR2*sin(t2),nowPointF.x,nowPointF.y,circlePaint)

        // ハイポサイクロイド曲線を描く
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

        // ハイポサイクロイド曲線を描く内円の中心の現在値をドットで描く
        canvas.drawCircle(rR2*cos(t2),rR2*sin(t2),nr,circleNowPaint)

        // ハイポサイクロイド曲線の現在値をドットで描く
        canvas.drawCircle(nowPointF.x,nowPointF.y,nr,lineNowPaint)

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
        canvas.drawBitmap(tmpBitmap,0f,0f,backPaint)
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