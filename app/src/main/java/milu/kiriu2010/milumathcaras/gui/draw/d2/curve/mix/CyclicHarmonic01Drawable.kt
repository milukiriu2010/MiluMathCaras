package milu.kiriu2010.milumathcaras.gui.draw.d2.curve.mix

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.pow

// -------------------------------------------------------------------------------------
// Cyclic Harmonic Curve
// -------------------------------------------------------------------------------------
//   ρ = a*(1+e*cos(n*t))
// -------------------------------------------------------------------------------------
// https://www.mathcurve.com/courbes2d.gb/conchoidderosace/conchoidderosace.shtml
// -------------------------------------------------------------------------------------
// 2019.06.29
// -------------------------------------------------------------------------------------
class CyclicHarmonic01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // Cyclic Harmonicの変数a
    // ---------------------------------
    private var a = 200f

    // ---------------------------------
    // Cyclic Harmonicの変数n
    // ---------------------------------
    private var n = 3f

    // ---------------------------------
    // Cyclic Harmonicの変数e
    // ---------------------------------
    private var e = 1f

    // ----------------------------------
    // Cyclic Harmonicの位相(変数tに相当)
    // ----------------------------------
    private var angle = 0f
    private var angleMax = 360f

    // -------------------------------
    // Cyclic Harmonicの描画点リスト
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
    // Cyclic Harmonicを描くペイント
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

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // values
    // 第１引数:変数n
    // 第２引数:変数e
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 変数n
                0 -> n = fl
                // 変数e
                1 -> e = fl
            }
        }

        // 媒介変数の最大角度を求める
        calAngleMax()

        // Cyclic Harmonicの描画点リストを生成
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
                    // Cyclic Harmonicを回転する
                    rotatePath()
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

    // -----------------------------------------------------
    // ハイポサイクロイド曲線の媒介変数(度)の最大値を求める
    // -----------------------------------------------------
    private fun calAngleMax() {
        // ハイポサイクロイド曲線描画に使う内円の係数の小数点以下の桁数
        val numOfDecimals = MyMathUtil.getNumberOfDecimals(n)
        //Log.d(javaClass.simpleName,"numOfDecimals[$numOfDecimals]")

        // "ハイポサイクロイド曲線描画に使う内円の係数"が整数となるよう補正する値(分母)
        // kが整数になるように10の倍数を掛けた値
        val kD = 10f.pow(numOfDecimals).toInt()
        // kDを"kDとkNの最大公約数"で割った値
        var kd = kD

        // "ハイポサイクロイド曲線描画に使う内円の係数"が整数となるよう補正された値(分子)
        // kが整数になるように掛けた値(10の倍数)
        val kN = (n * kD).toInt()
        // kNを"kDとkNの最大公約数"で割った値
        var kn = kN

        // ----------------------------------------------------------------------------
        // k=3.0 => kD= 1,kN= 3 => kd= 1,kn= 3
        // k=4.0 => kD= 1,kN= 4 => kd= 1,kn= 4
        // k=2.1 => kD=10,kN=21 => kd=10,kn=21
        // k=3.8 => kD=10,kN=38 => kd= 5,kn=19
        // k=5.5 => kD=10,kN=55 => kd= 2,kn=11
        // k=7.2 => kD=10,kN=72 => kd= 5,kn=36
        //   kd:ハイポサイクロイド曲線を描く点が元の位置に戻るために外円を周回する回数
        //   kn:ハイポサイクロイド曲線を描く点が外円と接する回数(内円が回転する回数)
        // ----------------------------------------------------------------------------
        // 少しわかりづらいので、書き直すと、
        // k=3.0の場合、内円が外円内を1周する間に、内円自身は 3周自転している
        // k=5.5の場合、内円が外円内を2周する間に、内円自身は11周自転している
        // ----------------------------------------------------------------------------
        // kdとknは、kDとkNそれぞれに10の倍数を掛けた値なので、
        // 2 or 5で割り切れる可能性がある
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

    // -----------------------------------
    // Cyclic Harmonicの描画点リストを生成
    // -----------------------------------
    private fun createPath() {
        // 描画点リストをクリア
        pointLst.clear()


        (0..angleMax.toInt() step 1).forEach {
            val t = it.toFloat()
            val cos1 = MyMathUtil.cosf(t)
            val cosn = MyMathUtil.cosf(n.toFloat()*t)
            val sin1 = MyMathUtil.sinf(t)
            val x = a*(1f+e*cosn)*cos1
            val y = a*(1f+e*cosn)*sin1
            pointLst.add(MyPointF(x,y))
        }

        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
    }

    // Cyclic Harmonicを回転する
    private fun rotatePath() {
        angle += 5f

        // ・元の角度に戻す
        if ( angle > angleMax ) {
            angle = 0f
        }
        // 描画中に呼び出すコールバックをキックし、現在の媒介変数の値を通知する
        notifyCallback?.receive(angle)
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
        val x0 = intrinsicWidth/2f
        val y0 = intrinsicHeight/2f

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

        // 原点(x0,y0)を中心にCyclic Harmonicを描く
        canvas.save()
        canvas.translate(x0,y0)

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // Cyclic Harmonicを描く
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                val id = (index+angle.toInt())%bunchSize
                val color = myColor.create(id,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
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