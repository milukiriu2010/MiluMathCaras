package milu.kiriu2010.milumathcaras.gui.draw.circle.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// ---------------------------------------------
// "正方形の中で大きくなる円"のタイリング
// ---------------------------------------------
// https://beesandbombs.tumblr.com/image/164719111714
// ---------------------------------------------
// 赤青赤青赤青赤青赤
// 青赤青赤青赤青赤青
// 赤青赤青赤青赤青赤
// 青赤青赤青赤青赤青
// 赤青赤青赤青赤青赤
// 青赤青赤青赤青赤青
// 赤青赤青赤青赤青赤
// 青赤青赤青赤青赤青
// 赤青赤青赤青赤青赤
// ---------------------------------------------
class Circle2SqaureTile01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 20f

    // ---------------------------------
    // タイル個数(9x9)
    // ---------------------------------
    // 9x9としているが
    // 中央を基準とするため
    // 値を変えたい場合は、奇数がよい
    // ---------------------------------
    private val nTile = 9
    // タイルの大きさ
    private val sizeTile = (side-margin*(nTile-1).toFloat())/nTile.toFloat()

    // -------------------------------------
    // 変形する多角形リスト
    // -------------------------------------
    private val polygonLst = mutableListOf<Square>()

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
    // バックグランドを描くペイント
    // -------------------------------
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 円・正方形を描くペイント
    // -------------------------------
    private val objPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // -------------------------------------
    // 円の枠を描くペイント
    // -------------------------------------
    private val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    // -------------------------------------
    // 正方形間の隙間を描くペイント
    // -------------------------------------
    private val gapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:初期状態の変形比率
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 正方形のリストを生成
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
                    // 変形する
                    morph()
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
    // 初期状態の変形比率まで変形する
    // -------------------------------
    private fun createPolygon() {
        polygonLst.clear()

        // タイルの中央インデックス
        val ci = (nTile-1)/2
        val ratioMax = 1f

        (0 until nTile).forEach { i ->
            (0 until nTile).forEach { j ->
                val square = Square()
                square.len = sizeTile
                //square.ratio = 0f
                val distance = sqrt((i-ci).toFloat()*(i-ci).toFloat()+(j-ci).toFloat()*(j-ci).toFloat())
                val distanceMax = sqrt(ci.toFloat()*ci.toFloat())
                square.ratio = ratioMax * (distanceMax-distance)/distanceMax

                val colA = when ((i+j)%2) {
                    // apricot
                    0 -> 0xffef820d.toInt()
                    // baby blue
                    else -> 0xff89cfef.toInt()
                }

                val colB = when ((i+j)%2) {
                    // chili
                    //0 -> 0xffc21807.toInt()
                    // punch
                    0 -> 0xffec5578.toInt()
                    // yale
                    else -> 0xff0e4c92.toInt()
                }
                square.colorA = colA
                square.colorB = colB

                polygonLst.add(square)
            }
        }
    }

    // -------------------------------
    // 変形する
    // -------------------------------
    private fun morph() {
        polygonLst.forEach {
            it.ratio += 0.05f
            if ( it.ratio >= 1.0f ) {
                it.ratio = 0f
                val colA = it.colorA
                val colB = it.colorB
                it.colorA = colB
                it.colorB = colA
            }
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)

        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),backPaint)

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin

        //Log.d(javaClass.simpleName,"========================================")
        // 正方形と円を描画
        (0 until nTile).forEach { i ->
            (0 until nTile).forEach { j ->
                // 描画する正方形
                val polygon = polygonLst[i*nTile+j]

                canvas.save()
                canvas.translate(x0+i.toFloat()*(sizeTile+margin),y0+j.toFloat()*(sizeTile+margin))

                // 正方形描画
                objPaint.color = polygon.colorA
                canvas.drawRect(RectF(0f,0f,sizeTile,sizeTile),objPaint)

                // 円描画
                canvas.translate(polygon.len/2f,polygon.len/2f)
                objPaint.color = polygon.colorB
                canvas.drawCircle(0f,0f,polygon.r,objPaint)
                canvas.drawCircle(0f,0f,polygon.r,rimPaint)

                // 座標を元に戻す
                canvas.restore()
            }
        }

        // 枠描画(縦方向)
        (0..nTile).forEach { i ->
            canvas.save()
            canvas.translate(i.toFloat()*(sizeTile+margin),0f)

            canvas.drawRect(0f,0f,margin,intrinsicHeight.toFloat(),gapPaint)

            // 座標を元に戻す
            canvas.restore()
        }

        // 枠描画(横方向)
        (0..nTile).forEach { i ->
            canvas.save()
            canvas.translate(0f, i.toFloat()*(sizeTile+margin))

            canvas.drawRect(0f,0f,intrinsicWidth.toFloat(),margin,gapPaint)

            // 座標を元に戻す
            canvas.restore()
        }

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // これまでの描画はテンポラリ領域に実施していたので、実際の表示に使うビットマップにコピーする
        val matrix = Matrix()
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

    // ---------------------------------
    // 正方形
    // ---------------------------------
    private class Square {
        // 一辺の長さ
        var len: Float = 0f
            set(data: Float) {
                field = data
                r = field * sqrt(2f)/2f * ratio
            }
        // 円の半径の比率(0.0～1.0)
        //   0.0=>円の半径=0
        //   1.0=>円の半径="正方形の対角線/2"
        var ratio: Float = 0f
            set(data: Float) {
                field = data
                r = len * sqrt(2f)/2f * field
            }
        // 円の半径
        var r: Float = 0f
        // 正方形の色(初期値)
        var colorA = 0xff000000.toInt()
        // 円の色(初期値)
        var colorB = 0xffffffff.toInt()
    }
}