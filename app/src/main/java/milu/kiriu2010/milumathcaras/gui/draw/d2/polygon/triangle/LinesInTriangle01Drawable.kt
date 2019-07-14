package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.triangle

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -------------------------------------------
// 三角形内を線が移動
// -------------------------------------------
// https://twitter.com/InfinityLoopGIF/status/1120846923924234240
// -------------------------------------------
// 2019.07.14
// -------------------------------------------
class LinesInTriangle01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------
    // 三角形の頂点リスト
    // -------------------------------
    private val triangle = mutableListOf<MyPointF>()
    // 三角形の半径
    private val r = side*0.5f

    // -------------------------------
    // 線分の描画点リスト
    // -------------------------------
    private val pointLst = mutableListOf<MyPointF>()

    // 描画点の数
    private val pointN = 60
    private val pointNN = pointN/3
    // 描画点の現在位置
    private var pointNow = 0

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
    // 三角形を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
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

    init {
        (0..2).forEach { i ->
            val ii = i.toFloat()*120f+30f
            val p = MyPointF().also {
                it.x = r * MyMathUtil.cosf(ii)
                it.y = r * MyMathUtil.sinf(ii)
            }
            triangle.add(p)
        }

        (0..2).forEach { i ->
            val pa: MyPointF
            val pb: MyPointF
            when (i) {
                0 -> {
                    pa = triangle[0]
                    pb = triangle[1]
                }
                1 -> {
                    pa = triangle[1]
                    pb = triangle[2]
                }
                2 -> {
                    pa = triangle[2]
                    pb = triangle[0]
                }
                else -> {
                    pa = triangle[0]
                    pb = triangle[1]
                }
            }

            (0  until pointNN).forEach { j ->
                val j0 = j.toFloat()
                val j1 = pointNN.toFloat()-j0
                val p = pa.lerp(pb,j0,j1)
                pointLst.add(p)
                /*
                Log.d(javaClass.simpleName,"j0[$j0]j1[$j1]")
                Log.d(javaClass.simpleName,"p [$p.x][$p.y]")
                Log.d(javaClass.simpleName,"pa[$pa.x][$pa.y]")
                */
            }
        }

    }

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // values
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 三角形を生成
        createPolygon()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 三角形を移動
                    movePolygon()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 100msごとに描画
                    handler.postDelayed(runnable, 50)
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
    // 三角形を生成
    // -------------------------------
    private fun createPolygon() {
    }

    // -------------------------------
    // 線分を移動
    // -------------------------------
    private fun movePolygon() {
        pointNow = (pointNow+1)%pointN
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

        // 原点(x0,y0)を中心に円・三角形を描く
        canvas.save()
        canvas.translate(x0,y0)

        // 三角形を描く
        val path = Path()
        triangle.forEachIndexed { id, p ->
            if ( id == 0 ) {
                path.moveTo(p.x,p.y)
            }
            else {
                path.lineTo(p.x,p.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)

        // 線分を描く
        (0 until pointNN).forEach { i ->
            val j0 = (pointNow+i)%pointN
            val j1 = (pointNow+i+pointNN)%pointN
            val pa = pointLst[j0]
            val pb = pointLst[j1]
            /*
            Log.d(javaClass.simpleName,"j0[$j0]j1[$j1]")
            Log.d(javaClass.simpleName,"pa[$pa.x][$pa.y]")
            Log.d(javaClass.simpleName,"pb[$pb.x][$pb.y]")
            */
            canvas.drawLine(pa.x,pa.y,pb.x,pb.y,linePaint)
        }

        // 座標を元に戻す
        canvas.restore()

        // テンポラリ描画を実体用ビットマップに描画する
        val matrix = Matrix()
        matrix.postScale(1f,1f)
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
