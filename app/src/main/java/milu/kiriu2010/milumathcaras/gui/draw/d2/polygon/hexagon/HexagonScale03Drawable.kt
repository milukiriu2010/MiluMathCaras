package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.hexagon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// 六角形の頂点から六角形が現れる01
// -----------------------------------------------------------------
// 2019.09.26
// -----------------------------------------------------------------
class HexagonScale03Drawable: MyDrawable() {

    private enum class ModePtn {
        PTN1,
        PTN2
    }

    // 現在のモード
    private var modeNow = ModePtn.PTN1

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 720f
    private val margin = 0f

    // -------------------------------
    // 描画領域の分割数
    // -------------------------------
    private val splitN = 12
    // -------------------------------
    // 六角形を形成する半径の長さ
    // -------------------------------
    private val a = side/splitN.toFloat()
    // 六角形の半径の半分の長さ
    private val b = a * 0.5f
    // 六角形の中心と辺との距離
    private val c = b*sqrt(3f)

    // 基準となる六角形
    private val hexagon = mutableListOf<MyPointF>()

    // 1ターン内の移動比率
    private var ratioDv = 0.1f
    //private val ratio1Lst = floatArrayOf(0f,-1f,-2f,-3f,-4f,-5f,-6f)
    //private val ratio2Lst = floatArrayOf(1f,2f,3f,4f,5f,6f,7f)
    private val ratio1Lst = floatArrayOf(0f,-0.5f,-1.5f,-2f,-2.5f,-3.5f,-4f)
    private val ratio2Lst = floatArrayOf(1f,1.5f,2f,2.5f,3f,3.5f,4f)
    private var ratioLst = ratio1Lst.copyOf()

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
    // 頂点を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
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
        // 描画点の初期位置設定
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
                    // スケールを変更する
                    shiftScale()
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
    // 描画点の初期位置設定
    // -------------------------------
    private fun createPath() {
        hexagon.clear()

        (0..5).forEach { i ->
            val t = 30f + 60f * i.toFloat()
            val p = MyPointF().also {
                it.x = a * MyMathUtil.cosf(t)
                it.y = a * MyMathUtil.sinf(t)
            }
            hexagon.add(p)
        }

        ratioLst = when (modeNow) {
            ModePtn.PTN1 -> ratio1Lst.copyOf()
            ModePtn.PTN2 -> ratio1Lst.copyOf()
        }
    }

    // -------------------------------
    // スケールを変更する
    // -------------------------------
    private fun shiftScale() {
        when (modeNow) {
            ModePtn.PTN1 -> shiftScale1()
            ModePtn.PTN2 -> shiftScale2()
        }
    }

    // スケールを変更する(パターン１)
    private fun shiftScale1() {
        ratioLst.forEachIndexed { id, ratio ->
            ratioLst[id] = ratio + ratioDv
        }

        if ( ratioLst.last() > 1f ) {
            modeNow = ModePtn.PTN2
            ratioLst = ratio2Lst.copyOf()
        }
    }

    // スケールを変更する(パターン２)
    private fun shiftScale2() {
        ratioLst.forEachIndexed { id, ratio ->
            ratioLst[id] = ratio - ratioDv
        }

        if ( ratioLst.last() < 1f ) {
            modeNow = ModePtn.PTN1
            ratioLst = ratio1Lst.copyOf()
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // ベース描画
        when (modeNow) {
            ModePtn.PTN1 -> drawBasePtn1(canvas)
            ModePtn.PTN2 -> drawBasePtn1(canvas)
        }

        // アニメ描画
        when (modeNow) {
            ModePtn.PTN1 -> drawAnimPtn1(canvas)
            ModePtn.PTN2 -> drawAnimPtn1(canvas)
        }

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // これまでの描画はテンポラリなので、実体にコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // ベース描画(パターン１)
    private fun drawBasePtn1(canvas: Canvas) {
        (0..splitN).forEach { i ->
            val ii = i.toFloat()

            val shx = when (i%2) {
                0 -> 0f
                1 -> -c
                else -> 0f
            }

            // 座標を保存
            canvas.save()
            canvas.translate(shx,ii*3f*b-a+b)

            (0..splitN).forEach { _ ->
                drawHexagon(canvas,1f)
                canvas.translate(2f*c,0f)
            }
            // 座標を元に戻す
            canvas.restore()
        }
    }

    // アニメ描画(パターン１)
    private fun drawAnimPtn1(canvas: Canvas) {
        // 座標を保存
        canvas.save()

        canvas.translate(intrinsicWidth.toFloat()*0.5f,intrinsicHeight.toFloat()*0.5f)

        (0..(splitN/2)).forEach { i ->
            val cc = i.toFloat() * 2f *c
            val ratio = ratioLst[i]

            (0..5).forEach { j ->
                val jj1 = j.toFloat()*60f
                val cos1 = cc*MyMathUtil.cosf(jj1)
                val sin1 = cc*MyMathUtil.sinf(jj1)
                val jj2 = (j+1).toFloat()*60f
                val cos2 = cc*MyMathUtil.cosf(jj2)
                val sin2 = cc*MyMathUtil.sinf(jj2)

                // 六角形を描く(六角形の頂点上)
                canvas.save()
                canvas.translate(cos1,sin1)
                drawHexagon(canvas,ratio,true)
                canvas.restore()

                // 六角形を描く(六角形の頂点間)
                val n = i-1
                val nn = n.toFloat()
                val m = n+1
                val mm = m.toFloat()
                (1..n).forEach { k ->
                    val kk = k.toFloat()
                    canvas.save()
                    val x = ((mm-kk)*cos1+kk*cos2)/mm
                    val y = ((mm-kk)*sin1+kk*sin2)/mm
                    canvas.translate(x,y)
                    drawHexagon(canvas,ratio,true)
                    canvas.restore()
                }
            }
        }

        // 座標を元に戻す
        canvas.restore()
    }

    // アニメ描画(パターン２)
    /*
    private fun drawAnimPtn2(canvas: Canvas) {
        // 座標を保存
        canvas.save()

        canvas.translate(intrinsicWidth.toFloat()*0.5f,intrinsicHeight.toFloat()*0.5f)

        (0..(splitN/2)).forEach { i ->
            val cc = i.toFloat() * 2f *c
            val ratio = ratioLst[i]

            (0..5).forEach { j ->
                val jj1 = j.toFloat()*60f
                val cos1 = cc*MyMathUtil.cosf(jj1)
                val sin1 = cc*MyMathUtil.sinf(jj1)
                val jj2 = (j+1).toFloat()*60f
                val cos2 = cc*MyMathUtil.cosf(jj2)
                val sin2 = cc*MyMathUtil.sinf(jj2)

                // 六角形を描く(六角形の頂点上)
                canvas.save()
                canvas.translate(cos1,sin1)
                drawHexagon(canvas,ratio,true)
                canvas.restore()

                // 六角形を描く(六角形の頂点間)
                val n = i-1
                val nn = n.toFloat()
                val m = n+1
                val mm = m.toFloat()
                (1..n).forEach { k ->
                    val kk = k.toFloat()
                    canvas.save()
                    val x = ((mm-kk)*cos1+kk*cos2)/mm
                    val y = ((mm-kk)*sin1+kk*sin2)/mm
                    canvas.translate(x,y)
                    drawHexagon(canvas,ratio,true)
                    canvas.restore()
                }
            }
        }

        // 座標を元に戻す
        canvas.restore()
    }

     */

    // ベース描画(パターン２)
    // 使ってない
    /*
    private fun drawBasePtn2(canvas: Canvas) {
        (0..splitN).forEach { i ->
            val ii = i.toFloat()

            val shx = when (i%2) {
                0 -> 0f
                1 -> -c
                else -> 0f
            }

            // 座標を保存
            canvas.save()
            canvas.translate(shx,ii*3f*b-a-b)

            (0..splitN).forEach { _ ->
                drawHexagon(canvas,1f)
                canvas.translate(2f*c,0f)
            }
            // 座標を元に戻す
            canvas.restore()
        }
    }

     */

    // 六角形を描画
    private fun drawHexagon(canvas: Canvas, ratio: Float, backFlg: Boolean = false ) {
        val ratioA = when {
            ratio < 0f -> 0f
            ratio > 1f -> 1f
            else -> ratio
        }

        val path = Path()
        hexagon.forEachIndexed { id, myPointF ->
            if ( id == 0 ) {
                path.moveTo(myPointF.x*ratioA,myPointF.y*ratioA)
            }
            else {
                path.lineTo(myPointF.x*ratioA,myPointF.y*ratioA)
            }
        }
        path.close()
        if (backFlg) canvas.drawPath(path,backPaint)
        canvas.drawPath(path,linePaint)
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
