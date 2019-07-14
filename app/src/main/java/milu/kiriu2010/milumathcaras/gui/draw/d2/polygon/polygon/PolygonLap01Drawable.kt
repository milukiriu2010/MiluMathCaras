package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.polygon

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI

// ----------------------------------------------------------------------
// 多角形のラップ
// ----------------------------------------------------------------------
// https://twitter.com/beesandbombs/status/872796708803145728
// https://medium.com/androiddevelopers/playing-with-paths-3fbc679a6f77
// ----------------------------------------------------------------------
class PolygonLap01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 800f
    private val margin = 0f

    // ---------------------------------------------------------------------
    // ドットの描画位置(全体を1とする)
    // ---------------------------------------------------------------------
    var dotProgress = 0f
        /*
        set(value) {
            // -----------------------------------
            // 0 <= value <= 1は、その値
            // 0より小さいと0
            // 1より大きいと1
            // を返す
            // -----------------------------------
            field = value.coerceIn(0f, 1f)
            callback.invalidateDrawable(this)
        }
        */
    private val polygons = listOf(
        Polygon(15, 0xffe84c65.toInt(), 362f, 2),
        Polygon(14, 0xffe84c65.toInt(), 338f, 3),
        Polygon(13, 0xffd554d9.toInt(), 314f, 4),
        Polygon(12, 0xffaf6eee.toInt(), 292f, 5),
        Polygon(11, 0xff4a4ae6.toInt(), 268f, 6),
        Polygon(10, 0xff4294e7.toInt(), 244f, 7),
        Polygon(9, 0xff6beeee.toInt(), 220f, 8),
        Polygon(8, 0xff42e794.toInt(), 196f, 9),
        Polygon(7, 0xff5ae75a.toInt(), 172f, 10),
        Polygon(6, 0xffade76b.toInt(), 148f, 11),
        Polygon(5, 0xffefefbb.toInt(), 128f, 12),
        Polygon(4, 0xffe79442.toInt(), 106f, 13),
        Polygon(3, 0xffe84c65.toInt(), 90f, 14)
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

    // -------------------------------
    // 多角形を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = CornerPathEffect(8f)
    }

    // -------------------------------
    // ドットを描くペイント
    // -------------------------------
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff0e0d0e.toInt()
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 黒ペンに小さい円のパス
    // -------------------------------
    private val dotPath = Path().apply {
        // CW:時計回り
        addCircle(0f, 0f, 8f, Path.Direction.CW)
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

        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // ドットを移動する
                    moveDot()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

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
    // ドットを移動する
    // -------------------------------
    private fun moveDot() {
        dotProgress += 0.01f
        if ( dotProgress > 1f ) {
            dotProgress = 0f
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
        canvas.save()
        canvas.translate(intrinsicWidth/2f,intrinsicHeight/2f)

        // 多角形を描く
        polygons.forEach { polygon ->
            linePaint.color = polygon.color
            canvas.drawPath(polygon.path, linePaint)
        }

        // ドットを描く
        polygons.forEach { polygon ->
            // --------------------------------------
            // 下辺ど真ん中を
            // "初期の描画オフセット位置"とするっぽい
            // polygon.length * polygon.lapsは
            // ドットの移動距離
            // --------------------------------------
            // 三角形
            //   0.0 => 389.71+0.0*467.65*14
            //   0.5 => 389.71+0.5*467.65*14
            //   1.0 => 389.71+1.0*467.65*14
            // --------------------------------------
            // 六角形
            //   0.0 => 814.0+0.0*888.0*11
            //   0.5 => 814.0+0.5*888.0*11
            //   1.0 => 814.0+1.0*888.0*11
            val phase = polygon.initialPhase + dotProgress * polygon.length * polygon.laps
            // ---------------------------------------------
            // スタンプを押す
            // ---------------------------------------------
            // pathDot: スタンプに使う黒ペンに小さい円
            // polygon.length: スタンプ間のスペース？
            // phase: 最初のスタンプ位置のオフセット？
            // TRANSLATE: 平行移動？
            // ---------------------------------------------
            dotPaint.pathEffect = PathDashPathEffect(dotPath, polygon.length, phase, PathDashPathEffect.Style.TRANSLATE)
            canvas.drawPath(polygon.path, dotPaint)
        }

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
        // と思ったが、三角形が逆なので
        // このままとする
        val matrix = Matrix()
        //matrix.setScale(1f,-1f)
        matrix.setScale(1f,1f)
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

    private class Polygon(val sides: Int, val color: Int, radius: Float, val laps: Int) {
        private val pathMeasure = PathMeasure()
        val path = createPath(sides, radius)
        // ----------------------------------------------
        // 多角形で使われている全体のパスの長さ
        // ----------------------------------------------
        //   三角形 => 467.65(=90*sqrt(3)*3)
        //   四角形 => 599.62(=106*sqrt(2)*4)
        //   五角形 => 752.36
        //   六角形 => 888.00(=148*6)
        // ----------------------------------------------
        val length by lazy(LazyThreadSafetyMode.NONE) {
            pathMeasure.setPath(path, false)
            //Log.d( javaClass.simpleName, "sides[$sides]length[${pathMeasure.length}]")
            pathMeasure.length
        }

        // ----------------------------------------------
        // 初期位置
        // ----------------------------------------------
        // 三角形 => (1-(1/(2*3)))*467.65 = 389.71
        // 四角形 => (1-(1/(2*4)))*599.62 = 524.67
        // 六角形 => (1-(1/(2*6)))*888    = 814
        // ----------------------------------------------
        val initialPhase by lazy(LazyThreadSafetyMode.NONE) {
            //Log.d( javaClass.simpleName, "sides[$sides]initialPhase[${(1f - (1f / (2 * sides))) * length}]")

            (1f - (1f / (2 * sides))) * length
        }

        private fun createPath(sides: Int, radius: Float): Path {
            val path = Path()

            // ------------------------------
            // 三角形 120
            // 四角形  90
            // 五角形  72
            // 六角形  60
            // ------------------------------
            val angle = 2.0 * PI / sides

            // ------------------------------
            // 三角形 90+60
            // 四角形 90+45
            // 五角形 90+36
            // 六角形 90+30
            // ------------------------------
            val startAngle = PI / 2.0 + Math.toRadians(360.0 / (2 * sides))


            // 下ちょい左を描画スタート地点とするっぽい
            path.moveTo(
                (radius * Math.cos(startAngle)).toFloat(),
                (radius * Math.sin(startAngle)).toFloat())

            // 左回りに描いていく
            for (i in 1 until sides) {
                path.lineTo(
                    (radius * Math.cos(startAngle - angle * i)).toFloat(),
                    (radius * Math.sin(startAngle - angle * i)).toFloat())
            }
            path.close()
            return path
        }
    }
}
