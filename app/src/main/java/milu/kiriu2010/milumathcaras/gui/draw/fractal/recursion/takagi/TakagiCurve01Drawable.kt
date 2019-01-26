package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.takagi

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------------------------------
// 高木曲線
// https://ja.wikipedia.org/wiki/%E9%AB%98%E6%9C%A8%E6%9B%B2%E7%B7%9A
// ---------------------------------------------------------------------
class TakagiCurve01Drawable: MyDrawable() {
    // -------------------------------
    // 描画領域
    // -------------------------------
    // レベルが１つ増えるごとに
    // 三角波の数が2倍になるので
    // 1024 = 2^10 としている
    // -------------------------------
    private val side = 1024f
    private val margin = 50f

    // ----------------------------------------
    // 合計する三角波のレベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 合計する三角波のレベルの最大値
    //  6回描くと、それ以降は違いがわからないので8回としている
    // --------------------------------------------------------
    private val nMax = 8

    // -------------------------------
    // 高木曲線の描画点リスト
    // -------------------------------
    private val pointMap = mutableMapOf<Float,Float>()

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
    // 高木曲線を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
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

    // ----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // ----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:合計する三角波のレベル(整数)
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 合計する三角波のレベル
        nNow = 0
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 合計する三角波のレベル
                0 -> nNow = fl.toInt()
            }
        }

        // 三角波を構築
        createWave()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if (isKickThread) {
            runnable = Runnable {
                // 合計する三角波のレベルを１つ増やす
                incrementLevel()
                // 三角波を構築
                createWave()
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
            handler.postDelayed(runnable, 1000)
        }
    }

    // -----------------------------------
    // 三角波を構築
    // -----------------------------------
    private fun createWave() {
        // 高木曲線の描画点リストをクリアする
        pointMap.clear()

        // -----------------------------------
        // 三角波レベル0(n=0)の位置
        // -----------------------------------
        //  a:左,b:右,c:真ん中
        // -----------------------------------
        // 三角波の高さは、おそらく関係ないが
        // 長さの1/2とした
        // -----------------------------------
        val a = MyPointF(0f, 0f)
        val b = MyPointF(side, 0f)
        val c = MyPointF(side / 2, side / 2)

        // ---------------------------------------------------------
        // 三角波レベル0(n=0)の位置を高木曲線の描画点リストに加える
        // ---------------------------------------------------------
        pointMap.put(a.x, a.y)
        pointMap.put(b.x, b.y)
        pointMap.put(c.x, c.y)

        // -----------------------------------
        // 次のレベルの三角波を加える
        // -----------------------------------
        addWave(a, c, nNow)
        addWave(c, b, nNow)

        // 描画中に呼び出すコールバックをキックし、現在の三角波のレベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // -----------------------------------------
    // 次のレベルの三角波を加える(再帰呼び出し)
    // -----------------------------------------
    private fun addWave(d: MyPointF, e: MyPointF, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            return
        }

        // ---------------------------------------------
        // dとeの中央点が足し算する次のレベルの三角波
        // ---------------------------------------------
        val x0 = (d.x + e.x)/2f
        val y0 = (d.y + e.y)/2f

        // ---------------------------------------------
        // 座標x0における高木曲線の高さを求める
        // ---------------------------------------------
        val y3 = getCorrectY(x0)

        // ---------------------------------------------------------
        // 座標x0における現在の"高木曲線の高さ"に次の三角波を加える
        // ---------------------------------------------------------
        pointMap.put(x0,y0+y3)

        // -----------------------------------
        // 次のレベルの三角波を加える
        // -----------------------------------
        //  a:左,b:右,c:真ん中
        // -----------------------------------
        val a = MyPointF(d.x,0f)
        val b = MyPointF(e.x,0f)
        val c = MyPointF(x0,y0)

        // --------------------------------------------
        // 本当は次のレベルはn+1だが
        // 引き算の方が考えやすかったのでn-1としている
        // --------------------------------------------
        addWave(a,c,n-1)
        addWave(c,b,n-1)
    }

    // -------------------------------------
    // 座標x0における高木曲線の高さを求める
    // -------------------------------------
    //   = x0を挟む座標x1,x2の高さの1/2
    // -------------------------------------
    private fun getCorrectY(x0: Float): Float {
        // x1: x0の1つ左側の描画点
        val x1 = pointMap.keys.filter { it < x0 }.sorted().last()
        // x2: x0の1つ右側の描画点
        val x2 = pointMap.keys.filter { it > x0 }.sorted().first()

        val y1 = pointMap.get(x1) ?: 0f
        val y2 = pointMap.get(x2) ?: 0f

        return (y1+y2)/2f
    }

    // -------------------------------------
    // 合計する三角波のレベルを１つ増やす
    // -------------------------------------
    private fun incrementLevel() {
        nNow++
        // 最大値を超えたら０に戻す
        if ( nNow > nMax ) {
            nNow = 0
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

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)
        // バックグランドを描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), backPaint)

        // 枠を描画
        canvas.drawRect(RectF(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat()), framePaint)

        // 原点(0,0)の位置
        //  = (マージン,マージン)
        canvas.save()
        canvas.translate(margin,margin)

        /*
        // 高木曲線を描く
        val path = Path()
        pointMap.keys.sorted().forEachIndexed { index, x ->
            when (index) {
                0 -> path.moveTo(x,pointMap.get(x) ?: 0f)
                else -> path.lineTo(x,pointMap.get(x) ?: 0f)
            }
        }
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)
        // サイン波を描く
        // 1536色のグラデーション
        val bunchSize = pointMap.size
        val xLst = pointMap.keys.sorted()
        xLst.forEachIndexed { index, x1 ->
            val color = myColor.create(index,bunchSize)
            linePaint.color = color.toInt()
            val x2 = when (index) {
                bunchSize-1 -> x1
                else -> xLst[index+1]
            }
            canvas.drawLine(x1,pointMap.get(x1) ?: 0f,x2,pointMap.get(x2) ?: 0f,linePaint)
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