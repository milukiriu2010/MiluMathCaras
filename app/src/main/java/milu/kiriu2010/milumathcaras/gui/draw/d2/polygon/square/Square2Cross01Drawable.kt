package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.square

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -------------------------------------------
// 正方形⇔十字
// -------------------------------------------
// https://66.media.tumblr.com/2222aaac0c1642796c14e59a59ec7486/tumblr_ocmkngaCFl1r2geqjo1_540.gif
// -------------------------------------------
class Square2Cross01Drawable: MyDrawable() {

    private enum class Mode {
        SQUARE,
        SQUARE2CROSS,
        CROSS,
        CROSS2SQUARE
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // 現在のモード
    private var modeNow = Mode.SQUARE

    // -------------------------------
    // 円関連のパラメータ
    // -------------------------------
    // 座標中心から円の中心までの距離
    private val rA = side*0.5f*0.5f
    // 円の半径
    private val rB = side*0.5f/sqrt(10f)
    // 円の中心リスト
    private val centerLst = mutableListOf<MyPointF>()

    // -------------------------------
    // 描画点のリスト
    // -------------------------------
    private val polygonLst = mutableListOf<MyPointF>()

    // -------------------------------
    // 描画点の回転角度
    // -------------------------------
    private val ratio = 0.1f
    private var angleMax1 = 90f
    private var angleMax2 = MyMathUtil.atanf(0.5f)
    private var angleMax = angleMax1
    private var angleMin = 0f
    private var angleNow = angleMin
    private var angleDv = angleMax1*ratio

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
        color = Color.BLACK
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
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 円の中心リスト
        createCircle()
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
                    // 三角形を生成
                    createPolygon()
                    // 三角形を移動
                    movePolygon()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 100msごとに描画
                    handler.postDelayed(runnable, 100)
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
    // 円の中心リストを生成
    // -------------------------------
    private fun createCircle() {
        centerLst.clear()

        (0..3).forEach { i ->
            val ii = i.toFloat()
            val p = MyPointF().also {
                it.x = rA * MyMathUtil.cosf(90f*ii+45f)
                it.y = rA * MyMathUtil.sinf(90f*ii+45f)
            }
            centerLst.add(p)
        }
    }

    // -------------------------------
    // 多角形を生成
    // -------------------------------
    private fun createPolygon() {
        if ( angleNow >= angleMax ) {
            angleNow = angleMin
        }
        if ( angleNow > 0f ) return

        polygonLst.clear()

        modeNow = when (modeNow) {
            Mode.SQUARE -> Mode.SQUARE2CROSS
            Mode.SQUARE2CROSS -> Mode.CROSS
            Mode.CROSS -> Mode.CROSS2SQUARE
            Mode.CROSS2SQUARE -> Mode.SQUARE
        }

        // 多角形を生成する
        when (modeNow) {
            Mode.SQUARE -> {
                angleMax = angleMax1
                angleDv = angleMax*ratio

                val x0 = rA * MyMathUtil.cosf(45f)
                val y0 = rA * MyMathUtil.sinf(45f)
                // 正方形１
                polygonLst.add(MyPointF(x0,y0))
                polygonLst.add(MyPointF(-x0,y0))
                polygonLst.add(MyPointF(-x0,-y0))
                polygonLst.add(MyPointF(x0,-y0))
            }
            Mode.SQUARE2CROSS -> {
                angleMax = angleMax2
                angleDv = angleMax*ratio

                val x0 = rA * MyMathUtil.cosf(45f)
                val y0 = rA * MyMathUtil.sinf(45f)

                // 正方形１
                polygonLst.add(MyPointF(x0,y0))
                polygonLst.add(MyPointF(x0-rB,y0))
                polygonLst.add(MyPointF(x0-rB,y0-rB))
                polygonLst.add(MyPointF(x0,y0-rB))
                // 正方形２
                polygonLst.add(MyPointF(-x0,y0))
                polygonLst.add(MyPointF(-x0,y0-rB))
                polygonLst.add(MyPointF(-x0+rB,y0-rB))
                polygonLst.add(MyPointF(-x0+rB,y0))
                // 正方形３
                polygonLst.add(MyPointF(-x0,-y0))
                polygonLst.add(MyPointF(-x0+rB,-y0))
                polygonLst.add(MyPointF(-x0+rB,-y0+rB))
                polygonLst.add(MyPointF(-x0,-y0+rB))
                // 正方形４
                polygonLst.add(MyPointF(x0,-y0))
                polygonLst.add(MyPointF(x0,-y0+rB))
                polygonLst.add(MyPointF(x0-rB,-y0+rB))
                polygonLst.add(MyPointF(x0-rB,-y0))
            }
            Mode.CROSS -> {
                angleMax = angleMax1
                angleDv = angleMax*ratio

                val x0 = rA * MyMathUtil.cosf(45f)
                val y0 = rA * MyMathUtil.sinf(45f)
                val x1 = rB * MyMathUtil.cosf(angleMax2)
                val y1 = rB * MyMathUtil.sinf(angleMax2)

                // 正方形１
                polygonLst.add(MyPointF(x0,y0))
                polygonLst.add(MyPointF(x0-x1,y0+y1))
                polygonLst.add(MyPointF(-x0,-y0))
                polygonLst.add(MyPointF(-x0+x1,-y0-y1))
                // 正方形２
                polygonLst.add(MyPointF(-x0,y0))
                polygonLst.add(MyPointF(-x0-y1,y0-x1))
                polygonLst.add(MyPointF(x0,-y0))
                polygonLst.add(MyPointF(x0+y1,-y0+x1))
            }
            Mode.CROSS2SQUARE -> {
                angleMax = angleMax2
                angleDv = angleMax*ratio

                val x0 = rA * MyMathUtil.cosf(45f)
                val y0 = rA * MyMathUtil.sinf(45f)
                val x1 = rB * MyMathUtil.cosf(angleMax2)
                val y1 = rB * MyMathUtil.sinf(angleMax2)

                // 正方形１
                polygonLst.add(MyPointF(x0,y0))
                polygonLst.add(MyPointF(x0-x1,y0+y1))
                polygonLst.add(MyPointF(-x0+x1,y0-y1))
                polygonLst.add(MyPointF(x0-y1,y0-x1))
                // 正方形２
                polygonLst.add(MyPointF(-x0,y0))
                polygonLst.add(MyPointF(-x0-y1,y0-x1))
                polygonLst.add(MyPointF(-x0+y1,-y0+x1))
                polygonLst.add(MyPointF(-x0+x1,y0-y1))
                // 正方形３
                polygonLst.add(MyPointF(-x0,-y0))
                polygonLst.add(MyPointF(-x0+x1,-y0-y1))
                polygonLst.add(MyPointF(x0-x1,-y0+y1))
                polygonLst.add(MyPointF(-x0+y1,-y0+x1))
                // 正方形４
                polygonLst.add(MyPointF(x0,-y0))
                polygonLst.add(MyPointF(x0+y1,-y0+x1))
                polygonLst.add(MyPointF(x0-y1,y0-x1))
                polygonLst.add(MyPointF(x0-x1,-y0+y1))
            }
        }
    }

    // -------------------------------
    // 三角形を移動
    // -------------------------------
    private fun movePolygon() {
        angleNow += angleDv

    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        backPaint.color = Color.WHITE
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

        // 円を描く
        centerLst.forEach { center ->
            canvas.drawCircle(center.x,center.y,rB,linePaint)
        }

        // 多角形を描く
        when (modeNow) {
            Mode.SQUARE -> drawModeSquare(canvas)
            Mode.SQUARE2CROSS -> drawModeSquare2Cross(canvas,-1f)
            Mode.CROSS -> drawModeCross(canvas)
            Mode.CROSS2SQUARE -> drawModeSquare2Cross(canvas,1f)
        }

        // 座標を元に戻す
        canvas.restore()

        // テンポラリ描画を実体用ビットマップに描画する
        val matrix = Matrix()
        matrix.postScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // １つの正方形を回転
    private fun drawModeSquare(canvas: Canvas) {
        //backPaint.color = Color.RED
        val path = Path()
        polygonLst.forEachIndexed { id, p ->
            val q = p.copy()
            q.rotate(angleNow)
            if (id == 0) {
                path.moveTo(q.x,q.y)
            }
            else {
                path.lineTo(q.x,q.y)
            }
        }
        path.close()
        canvas.drawPath(path,backPaint)
    }

    // ４つの正方形を描画
    private fun drawModeSquare2Cross(canvas: Canvas,sign: Float) {
        /*
        if ( sign > 0f ) {
            backPaint.color = Color.YELLOW
        }
        else {
            backPaint.color = Color.GREEN
        }
        */
        var path = Path()
        var o = MyPointF()
        polygonLst.forEachIndexed { id, p ->
            val q = p.copy()
            if (id%4 == 0) {
                path.close()
                canvas.drawPath(path,backPaint)
                path.reset()

                o = q
                path.moveTo(q.x,q.y)
            }
            else {
                q.rotate(sign*angleNow,o)
                path.lineTo(q.x,q.y)
            }
        }
        path.close()
        canvas.drawPath(path,backPaint)
    }

    // ２つの正方形を描画
    private fun drawModeCross(canvas: Canvas) {
        //backPaint.color = Color.BLUE
        var path = Path()
        polygonLst.forEachIndexed { id, p ->
            val q = p.copy()
            if (id%4 == 0) {
                path.close()
                canvas.drawPath(path,backPaint)
                path.reset()

                q.rotate(-angleNow)
                path.moveTo(q.x,q.y)
            }
            else {
                q.rotate(-angleNow)
                path.lineTo(q.x,q.y)
            }
        }
        path.close()
        canvas.drawPath(path,backPaint)
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
