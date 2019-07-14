package milu.kiriu2010.milumathcaras.gui.draw.d2.fractal.recursion.sierpinski

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -----------------------------------------------------------------
// シェルピンスキーの六角形
// -----------------------------------------------------------------
// http://ecademy.agnesscott.edu/~lriddle/ifs/pentagon/pentagon.htm
// -----------------------------------------------------------------
class SierpinskiHexagon01Drawable: MyDrawable() {
    // -------------------------------------
    // 描画領域
    // -------------------------------------
    private val side = 1000f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  5回描くと遅いので、5回までとしている
    // --------------------------------------------------------
    private val nMax = 5

    // ----------------------------------------
    // シェルピンスキーの六角形リスト
    // ----------------------------------------
    private val polygonLst = mutableListOf<Polygon>()

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
    // シェルピンスキーの六角形を描くペイント
    // -------------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
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

    // ----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // ----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:再帰レベル(整数)
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 再帰レベル
        nNow = 0
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 再帰レベル
                0 -> nNow = fl.toInt()
            }
        }

        // シェルピンスキーの六角形を構築
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()


        // 描画に使うスレッド
        if (isKickThread) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 再帰レベルを１つ増やす
                    incrementLevel()
                    // シェルピンスキーの六角形を構築
                    createPath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 最初と最後は1秒後に描画
                    if ( nNow == nMax || nNow == 0 ) {
                        handler.postDelayed(runnable, 1000)
                    }
                    // 500msごとに描画
                    else {
                        handler.postDelayed(runnable, 500)
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
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    override fun calStop() {
        // 描画に使うスレッドを解放する
        handler.removeCallbacks(runnable)
    }

    // -------------------------------------
    // CalculationCallback
    // 描画中に呼び出すコールバックを設定
    // -------------------------------------
    override fun setNotifyCallback(notifyCallback: NotifyCallback) {
        this.notifyCallback = notifyCallback
    }

    // シェルピンスキーの六角形を構築
    private fun createPath() {
        // シェルピンスキーの六角形リストをクリアする
        polygonLst.clear()

        // ------------------------------
        // シェルピンスキーの六角形の頂点
        // ------------------------------
        val polygon = Polygon().apply {
            center = MyPointF()
            r = side/2f
        }

        // 次レベルのシェルピンスキーの六角形の描画点を求める
        calNextLevel(polygon,nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // -----------------------------------------------
    // 次レベルのシェルピンスキーの六角形の描画点を求める
    // -----------------------------------------------
    private fun calNextLevel(polygon0:Polygon, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            polygonLst.add(polygon0)
            return
        }

        // 次の六角形の半径 = 前の六角形の半径 * 1/3
        val rN = polygon0.r /3f

        // "前の六角形の半径-次の六角形の半径"
        val rC = polygon0.r - rN

        // -------------------------------------------------------------------------
        // 次の六角形を描く
        // -------------------------------------------------------------------------
        // 次の六角形の中心 =
        //    "前の六角形の中心" +
        //    "前の六角形の半径-次の六角形の半径"を60度ずつ回転させた位置
        // -------------------------------------------------------------------------
        (0..5).forEach {
            val x = rC * MyMathUtil.cosf(it.toFloat()*60f)
            val y = rC * MyMathUtil.sinf(it.toFloat()*60f)
            val polygon = Polygon().apply {
                center = polygon0.center.copy().plusSelf(MyPointF(x,y))
                r = rN
            }
            calNextLevel(polygon,n-1)
        }
    }

    // -------------------------------------
    // 再帰レベルを１つ増やす
    // -------------------------------------
    private fun incrementLevel() {
        nNow++
        // 最大値を超えたら０に戻す
        if ( nNow > nMax ) {
            nNow = 0
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), framePaint)

        // ---------------------------------------------------------------------
        // 原点(0,0)の位置
        //  = (左右中央,上下中央)
        // ---------------------------------------------------------------------

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // シェルピンスキーの六角形を描画
        // 1536色のグラデーション
        val bunchSize = polygonLst.size
        polygonLst.forEachIndexed { id1, polygon ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            canvas.save()
            canvas.translate(intrinsicWidth/2f, intrinsicHeight/2f)
            canvas.translate(polygon.center.x,polygon.center.y)

            val path: Path = Path()
            polygon.pointLst.forEachIndexed { id2, myPointF ->
                when ( id2%6 ) {
                    0 -> {
                        path.moveTo(myPointF.x,myPointF.y)
                    }
                    else -> {
                        path.lineTo(myPointF.x,myPointF.y)
                    }
                }
            }
            path.close()
            val color = myColor.create(id1,bunchSize)
            linePaint.color = color.toInt()
            canvas.drawPath(path,linePaint)

            // 座標を元に戻す
            canvas.restore()
        }


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

    private class Polygon {
        // 中心
        var center: MyPointF = MyPointF()
        // 半径
        var r: Float = 0f
            set(data: Float) {
                field = data
                (0..5).forEach {
                    val x = data*MyMathUtil.cosf(it.toFloat()*60f)
                    val y = data*MyMathUtil.sinf(it.toFloat()*60f)
                    pointLst.add(MyPointF(x,y))
                }
            }
        // 頂点(中心から見た座標)
        val pointLst: MutableList<MyPointF> = mutableListOf()
    }
}
