package milu.kiriu2010.milumathcaras.gui.draw.d2.circle.morph

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyVectorF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// "円⇔正方形の変形"のタイリング
// ---------------------------------------------
// https://processing.org/examples/morph.html
// ---------------------------------------------
// 黒白黒白
// 白黒白黒
// 黒白黒白
// 白黒白黒
// ---------------------------------------------
// 点⇒円(0.0～0.8)/円⇒正方形(0.8～1.0)
// 正方形⇒円(1.0～0.8)/円⇒点(0.8～0.0)
// ---------------------------------------------
class Circle2SqaureMorph02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 0f

    // ---------------------------------
    // タイル個数(4x4)
    // ---------------------------------
    private val nTile = 4
    // タイルの大きさ
    private val sizeTile = side/nTile.toFloat()
    // 一辺に対する円の大きさ
    private val ratioCircle = 0.5f

    // ---------------------------------
    // タイル(0,0)の描画状態
    // ---------------------------------
    // 0 ⇒
    //   バックグラウンド：黒
    //   フォアグラウンド：白
    // ---------------------------------
    // 1 ⇒
    //   バックグラウンド：白
    //   フォアグラウンド：黒
    // ---------------------------------
    private var state = 0

    // ---------------------------------
    // 円ベクトル(0.8の位置)
    // ---------------------------------
    private val vc = mutableListOf<MyVectorF>()

    // -------------------------------------
    // 正方形ベクトル(1.0の位置)
    // -------------------------------------
    private val vs = mutableListOf<MyVectorF>()

    // -------------------------------------
    // 変形する多角形リスト
    // -------------------------------------
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
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // -------------------------------
    // 円・正方形を描くペイント
    // -------------------------------
    private val forePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    // 描画に使う色リスト
    private val colorLst = intArrayOf(
        0xff000000.toInt(),
        0xffffffff.toInt()
    )
    /*
    private val colorLst = intArrayOf(
        Color.BLACK,
        Color.WHITE
    )
    */

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
        // 初期状態の変形比率
        var ratioInit = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 初期状態の変形比率
                0 -> ratioInit = fl
            }
        }


        // 円・正方形の初期ベクトル設定
        createVector()
        // 初期状態の変形比率まで変形する
        while (polygonLst[0].ratio < ratioInit) morph()
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
    // 円・正方形の初期ベクトル設定
    // -------------------------------
    private fun createVector() {
        vc.clear()
        vs.clear()
        polygonLst.clear()

        // -----------------------------------------
        // 円ベクトル(0.8の位置)を生成
        // -----------------------------------------
        val d = 45f
        (0 until 360 step 9).forEach {
            // -----------------------------------------------------------
            // 円ベクトル
            // 正方形の頂点とインデックスを合わせるため45度位相を足している
            // -----------------------------------------------------------
            val vx = MyMathUtil.cosf(it.toFloat()+d)
            val vy = MyMathUtil.sinf(it.toFloat()+d)
            val v = MyVectorF(vx,vy).multiply(side/nTile.toFloat()/2f*ratioCircle)
            vc.add(v)
        }

        // -----------------------------------------
        // 正方形ベクトル(1.0の位置)を生成
        // -----------------------------------------
        // 正方形の１辺を10分割
        val sdv = sizeTile/10f
        //  side/4 => -side/4
        val u2d = FloatArray(10,{ i -> sizeTile/2f-i.toFloat()*sdv })
        // -side/4 =>  side/4
        val d2u = FloatArray(10,{ i -> -sizeTile/2f+i.toFloat()*sdv })

        // 上辺(sizeTile/2,sizeTile/2) => (-sizeTile/2,sizeTile/2)
        u2d.forEach {
            vs.add(MyVectorF(it,sizeTile/2f))
        }
        // 左辺(-sizeTile/2,sizeTile/2) => (-sizeTile/2,-sizeTile/2)
        u2d.forEach {
            vs.add(MyVectorF(-sizeTile/2f,it))
        }
        // 下辺(-sizeTile/2,-sizeTile/2) => (sizeTile/2,-sizeTile/2)
        d2u.forEach {
            vs.add(MyVectorF(it,-sizeTile/2f))
        }
        // 右辺(sizeTile/2,-sizeTile/2) => (sizeTile/2,sizeTile/2)
        d2u.forEach {
            vs.add(MyVectorF(sizeTile/2f,it))
        }

        /*
        vc.forEachIndexed { index, myVectorF1 ->
            val myVectorF2 = vs[index]
            Log.d(javaClass.simpleName,"id[$index]cx[${myVectorF1.x}]cy[${myVectorF1.y}]sx[${myVectorF2.x}]sy[${myVectorF2.y}]")
        }
        */

        // -------------------------------------
        // 変形する多角形その１
        // -------------------------------------
        // 点⇒円(0.0～0.8)/円⇒正方形(0.8～1.0)
        // -------------------------------------
        val polygon1 = Polygon().apply {
            vc.forEach {
                vlst.add(it.copy().multiply(0f))
            }
            ratio = 0f
            direction = 1
        }
        polygonLst.add(polygon1)

        // -------------------------------------
        // 変形する多角形その２
        // -------------------------------------
        // 正方形⇒円(1.0～0.8)/円⇒点(0.8～0.0)
        // -------------------------------------
        val polygon2 = Polygon().apply {
            vs.forEach {
                vlst.add(it.copy())
            }
            ratio = 1f
            direction = -1
        }
        polygonLst.add(polygon2)
    }

    // -------------------------------
    // 変形する
    // -------------------------------
    private fun morph() {

        polygonLst.forEachIndexed{ _, polygon ->
            polygon.vlst.clear()
            when {
                (polygon.ratio >= 0f) and (polygon.ratio < ratioCircle ) -> {
                    vc.forEach {
                        val ratio = polygon.ratio/ratioCircle
                        polygon.vlst.add(it.copy().multiply(ratio))
                    }
                }
                (polygon.ratio >= ratioCircle) and (polygon.ratio <= 1f ) -> {
                    val ratio = (polygon.ratio-ratioCircle)/(1f-ratioCircle)
                    //Log.d(javaClass.simpleName,"id1[$id1]ratio[$ratio]")
                    vc.forEachIndexed { index, myVectorF ->
                        val v = myVectorF.copy()
                        v.lerp(vs[index],ratio)
                        polygon.vlst.add(v)
                    }
                }
            }

            when ( polygon.direction ) {
                1 -> {
                    polygon.ratio += 0.05f
                    if ( polygon.ratio > 1f ) {
                        polygon.ratio = 0f
                    }
                }
                -1 -> {
                    polygon.ratio -= 0.05f
                    if ( polygon.ratio < 0f ) {
                        polygon.ratio = 1f
                        state = (state+1)%2
                    }
                }
            }
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = margin
        val y0 = margin


        //Log.d(javaClass.simpleName,"========================================")
        (0 until nTile).forEach { i ->
            (0 until nTile).forEach { j ->
                // 色(バックグラウンド)
                var colorBack = colorLst[(0+i+j+state)%2]
                // 色(フォアグラウンド)
                var colorFore = colorLst[(1+i+j+state)%2]
                // 描画する多角形
                //val polygon = polygonLst[(i+j+state)%2]
                val polygon = polygonLst[(i+j)%2]
                //Log.d(javaClass.simpleName,"i[$i]j[$j]state[$state]back[$colorBack]fore[$colorFore]")

                canvas.save()
                canvas.translate(x0+i.toFloat()*sizeTile,y0+j.toFloat()*sizeTile)

                // バックグラウンド描画
                backPaint.color = colorBack
                canvas.drawRect(RectF(0f,0f,sizeTile,sizeTile),backPaint)

                // フォアグラウンド描画
                val path = Path()
                polygon.vlst.forEachIndexed { index, myVectorF ->
                    if ( index == 0 ) {
                        path.moveTo(myVectorF.x,myVectorF.y)
                    }
                    else {
                        path.lineTo(myVectorF.x,myVectorF.y)
                    }
                }
                forePaint.color = colorFore
                canvas.translate(sizeTile/2f,sizeTile/2f)
                canvas.drawPath(path,forePaint)


                // 座標を元に戻す
                canvas.restore()
            }
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
    // 多角形
    // ---------------------------------
    private data class Polygon(
        // 変形ベクトルリスト
        val vlst: MutableList<MyVectorF> = mutableListOf(),
        // 変形比率(0.0-1.0)
        var ratio: Float = 0f,
        // 変形の方向
        //  1 => 0.0～1.0
        // -1 => 1.0～0.0
        var direction: Int = 1
    )
}