package milu.kiriu2010.milumathcaras.gui.draw.nature

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyCircleF
import milu.kiriu2010.gui.basic.MyVectorF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.abs

// ------------------------------------------------------------------
// 摩擦の効果
// ------------------------------------------------------------------
// 摩擦力 = -1 * 摩擦係数 * 垂直抗力 * 速度ベクトル
// ------------------------------------------------------------------
// https://natureofcode.com/book/chapter-2-forces/
// ------------------------------------------------------------------
class FrictionEffect01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val sideW = 1000f
    private val sideH = 1500f
    private val margin = 0f

    // ---------------------------------
    // 円の最大数
    // ---------------------------------
    private var nMax = 10

    // ---------------------------------
    // 円リスト
    // ---------------------------------
    private val circleLst: MutableList<MyCircleF> = mutableListOf()

    // 風の強さ
    val wind = MyVectorF(1f,0f)
    // 重力
    val gravity = MyVectorF(0f,-10f)

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
    // 円を描くペイント
    // -------------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
    }

    // -------------------------------------
    // 円を描く色リスト
    // -------------------------------------
    private val colorLst = arrayOf(
        // 0:red
        0xffff0000.toInt(),
        // 1:pink
        0xffffcccc.toInt(),
        // 2:orange
        0xffff7f00.toInt(),
        // 3:maroon
        0xff800000.toInt(),
        // 4:green(lime)
        0xff00ff00.toInt(),
        // 5:blue
        0xff0000ff.toInt(),
        // 6:cyan
        0xff00ffff.toInt(),
        // 7:indigo
        0xff6f00ff.toInt(),
        // 8:violet
        0xffff00ff.toInt(),
        // 9:green
        0xff008000.toInt()
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

    // -----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:描画する円の最大数
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 可変変数 values を初期値として、このクラスで使う変数に当てはめる
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 描画する円の最大数
                0 -> nMax = fl.toInt()
            }
        }

        // 描画する円を生成
        while (createMover())
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 円を移動する
                moveMover()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                // 10msごとに描画
                handler.postDelayed(runnable, 10)
            }
            handler.postDelayed(runnable, 100)
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
    // TouchCallback
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    override fun calStop() {
        // 描画に使うスレッドを解放する
        handler.removeCallbacks(runnable)
    }

    // -------------------------------
    // 描画する円を生成
    // -------------------------------
    private fun createMover(): Boolean {
        // 既に最大数を超えていたら何もしない
        if ( circleLst.size >= nMax ) return false

        // 円の重さ
        val massA = (circleLst.size.toFloat()+2f)

        // 円の半径
        val rA = massA * 10f

        // ---------------------------------------------------
        // 円の初期位置(左上に配置)
        // y座標を"sideH-rA"でなく"sideH-rA-1"としているのは、
        // 境界チェックのところで、上にはりついてしまうから
        // ---------------------------------------------------
        val x = rA
        val y = sideH-rA-1f


        // 色
        val colorId = circleLst.size%colorLst.size

        // 円
        val circle = MyCircleF().apply{
            // 円の初期位置
            p = MyVectorF(x,y)
            // 円の重さ
            mass = massA
            // 円の半径
            r = rA
            // 円の色
            color = colorLst[colorId]
        }
        // 円に力を加える
        circle.applyForce(wind)
            .applyForce(gravity)

        // 円を生成し、リストに加える
        circleLst.add(circle)

        return true
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveMover() {
        circleLst.forEachIndexed { index, circleF ->
            /*
            if ( index == 0 ) {
                Log.d(javaClass.simpleName,"====================================")
                Log.d(javaClass.simpleName,"px1[${circleF.p.x}]py1[${circleF.p.y}]")
            }
            */
            // 移動前の円の位置
            val p1 = circleF.p.copy()

            // 円を移動する
            circleF.move()
            // 境界を超えていたら、反射する
            circleF.checkBorder(0f,0f,sideW,sideH)

            // 移動後の円の位置
            val p2 = circleF.p.copy()

            // 物体の加速度をクリアする
            circleF.a.multiply(0f)

            // 円に力を加える
            circleF.applyForce(wind).applyForce(gravity)

            /*
            if ( index == 0 ) {
                Log.d(javaClass.simpleName,"px2[${circleF.p.x}]py2[${circleF.p.y}]")
                Log.d(javaClass.simpleName,"amag=${circleF.a.magnitude()}")
                Log.d(javaClass.simpleName,"ax1[${circleF.a.x}]ay1[${circleF.a.y}]")
            }
            */

            // ----------------------------------
            // 摩擦効果を加える
            // ----------------------------------
            // 物体の速度
            val v = circleF.v
            // 摩擦係数
            val c = 5f
            //val c = circleF.a.magnitude()
            // 摩擦力 = -1 * 摩擦係数 * 垂直抗力 * 速度ベクトル
            val fv = v.copy()
                .multiply(-1f)
                .normalized()
                .multiply(c)
            circleF.applyForce(fv)

            // -------------------------------------------------
            // 以下の場合、停止する
            // ・Y座標が半径付近
            // ・位置の変化が一定より小さい
            // -------------------------------------------------
            if ( (abs(circleF.r-circleF.p.y) < 0.2f) and
                (p2.magByDiff(p1) < 2f ) ) {
                circleF.p.y = circleF.r
                circleF.v.multiply(0f)
                circleF.a.multiply(0f)
            }

            /*
            if ( index == 0 ) {
                Log.d(javaClass.simpleName,"c[$c]")
                Log.d(javaClass.simpleName,"ax2[${circleF.a.x}]ay2[${circleF.a.y}]")
            }
            */
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

        // 円を描画
        circleLst.reversed().forEachIndexed { index, myCircleF ->
            linePaint.color = myCircleF.color
            canvas.drawCircle(myCircleF.p.x,myCircleF.p.y,myCircleF.r,linePaint)
        }

        // これまでの描画は上下逆なので、反転する
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
    override fun getIntrinsicWidth(): Int = (sideW+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (sideH+margin*2).toInt()
}