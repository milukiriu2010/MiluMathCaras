package milu.kiriu2010.milumathcaras.gui.draw.d2.fractal.recursion.sierpinski

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// -----------------------------------------------------------------
// シェルピンスキーの五角形
// -----------------------------------------------------------------
// http://ecademy.agnesscott.edu/~lriddle/ifs/pentagon/pentagon.htm
// -----------------------------------------------------------------
class SierpinskiPentagon02Drawable: MyDrawable() {
    // -------------------------------------
    // 描画領域
    // -------------------------------------
    private val side = 1000f
    private val margin = 50f

    // -------------------------------------
    // 変形モード
    // -------------------------------------
    // ・縮小
    // ・分裂
    // ・元に戻る
    // -------------------------------------
    enum class ModeMorph {
        SHRINK,
        SPLIT,
        BACK
    }

    // 現在の変形モード
    private var modeMorphNow = ModeMorph.SHRINK

    // 1ターン内の移動比率
    private var ratioNow = 0f
    private var ratioDiv = 0.1f
    private var ratioMax = 1f

    // 縮小比率(3-sqrt(5))/2
    private val ratioShrink = (3f-sqrt(5f))/2f

    // "描画点の初期位置設定"をしたかどうか
    private var isInitialized = false

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    // --------------------------------------------------------
    private val nMax = 2

    // ------------------------------
    // シェルピンスキーの五角形の頂点
    // ------------------------------
    private val pentagon = Polygon().apply {
        center = MyPointF()
        r = side/2f
    }

    // ----------------------------------------
    // 変形リスト
    // ----------------------------------------
    // shrink用
    private val morph0Lst = mutableListOf<Morph>()

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
    // シェルピンスキーの五角形を描くペイント
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

        // シェルピンスキーの五角形を構築
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
                    // シェルピンスキーの五角形を構築
                    createPath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

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

    // シェルピンスキーの五角形を構築
    private fun createPath() {
        // 移動比率=0の場合、描画点リストを構築しなおす
        if ( ratioNow != 0f ) return

        // 変形モードを切り替える
        if ( isInitialized == true ) {
            when ( modeMorphNow ) {
                ModeMorph.SHRINK -> {
                    modeMorphNow = ModeMorph.SPLIT
                }
                ModeMorph.SPLIT -> {
                    when (nNow) {
                        nMax -> {
                            nNow = 0
                            modeMorphNow = ModeMorph.BACK
                        }
                        else -> {
                            nNow++
                            modeMorphNow = ModeMorph.SHRINK
                        }
                    }
                }
                ModeMorph.BACK -> {
                    modeMorphNow = ModeMorph.SHRINK
                }
            }
        }

        // "描画点の初期位置設定"をしたかどうか
        if ( isInitialized == false ) {
            isInitialized = true
        }

        // 次レベルのシェルピンスキーの五角形の描画点を求める
        when (modeMorphNow) {
            ModeMorph.SHRINK -> {
                // 変形リストをクリアする(shrink用)
                morph0Lst.clear()
                calNextLevelShrink(pentagon,nNow)
            }
            ModeMorph.SPLIT -> {}
            ModeMorph.BACK -> {}
        }

    }

    // 次レベルのシェルピンスキーの五角形の描画点を求める
    //   Shrink
    private fun calNextLevelShrink(src:Polygon, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            val dst = Polygon().also {
                it.center = src.center.copy()
                // 半径が(3-sqrt(5))/2倍と小さくなる
                it.r = src.r*ratioShrink
                it.pointLst.clear()
                // 108度回転する
                // もともと18度傾いているので126度している
                (0..4).forEach { i ->
                    val x = it.r*MyMathUtil.cosf(i.toFloat()*72f+126f)
                    val y = it.r*MyMathUtil.sinf(i.toFloat()*72f+126f)
                    it.pointLst.add(MyPointF(x,y))
                }
            }
            val morph = Morph().also {
                it.src = src
                it.dst = dst
            }
            morph0Lst.add(morph)
            return
        }

        // 次の五角形の半径 = 前の五角形の半径 * (3-sqrt(5))/2
        val rN = src.r * ratioShrink

        // "前の五角形の半径-次の五角形の半径"
        val rC = src.r - rN

        // -------------------------------------------------------------------------
        // 次の五角形を描く
        // -------------------------------------------------------------------------
        // 次の五角形の中心 =
        //    "前の五角形の中心" +
        //    "前の五角形の半径-次の五角形の半径"を72度ずつ回転させた位置
        // -------------------------------------------------------------------------
        (0..4).forEach {
            val x = rC * MyMathUtil.cosf(it.toFloat()*72f+18f)
            val y = rC * MyMathUtil.sinf(it.toFloat()*72f+18f)
            val polygon = Polygon().apply {
                center = src.center.copy().plusSelf(MyPointF(x,y))
                r = rN
            }
            calNextLevelShrink(polygon,n-1)
        }
    }

    // -------------------------------------
    // 再帰レベルを１つ増やす
    // -------------------------------------
    private fun incrementLevel() {
        ratioNow += ratioDiv
        // 描画点を移動する
        if (ratioNow > ratioMax) {
            ratioNow = 0f
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
        canvas.save()
        canvas.translate(intrinsicWidth/2f, intrinsicHeight/2f)

        when (modeMorphNow) {
            ModeMorph.SHRINK -> {
                morph0Lst.forEach { morph ->
                    val path = Path()
                    val n = morph.dst.pointLst.size
                    (0 until n).forEach { i ->
                        val x = morph.src.pointLst[i].x*(1f-ratioNow) + morph.dst.pointLst[i].x*ratioNow + morph.dst.center.x
                        val y = morph.src.pointLst[i].y*(1f-ratioNow) + morph.dst.pointLst[i].y*ratioNow + morph.dst.center.y

                        when (i) {
                            0 -> path.moveTo(x,y)
                            else -> path.lineTo(x,y)
                        }
                    }
                    path.close()
                    canvas.drawPath(path,linePaint)
                }
            }
            ModeMorph.SPLIT -> {
                morph0Lst.forEach { morph ->
                    (0..4).forEach { i ->
                        val path = Path()
                        // 回転の中心となる頂点
                        val c = morph.dst.pointLst[i]
                        morph.dst.pointLst.forEachIndexed labelB@{ id, myPointF ->
                            //if (id == i) return@labelB

                            val p = myPointF.copy().rotate(108f*ratioNow,c)

                            when (id) {
                                0     -> path.moveTo(p.x+morph.dst.center.x,p.y+morph.dst.center.y)
                                else -> path.lineTo(p.x+morph.dst.center.x,p.y+morph.dst.center.y)
                            }
                        }
                        path.close()
                        canvas.drawPath(path,linePaint)
                    }
                }
            }
            ModeMorph.BACK -> {
                morph0Lst.forEach { morph ->
                    (0..4).forEach { i ->
                        val path = Path()
                        // 回転の中心となる頂点
                        val c = morph.dst.pointLst[i]
                        morph.dst.pointLst.forEachIndexed labelB@{ id, myPointF ->
                            //if (id == i) return@labelB

                            val p = myPointF.copy().rotate(108f,c)

                            when (id) {
                                0     -> path.moveTo(p.x+morph.dst.center.x,p.y+morph.dst.center.y)
                                else -> path.lineTo(p.x+morph.dst.center.x,p.y+morph.dst.center.y)
                            }
                        }
                        path.close()
                        canvas.drawPath(path,linePaint)
                    }
                }

                val path = Path()
                pentagon.pointLst.forEachIndexed { id, p ->
                    when (id) {
                        0    -> path.moveTo(p.x,p.y)
                        else -> path.lineTo(p.x,p.y)
                    }
                }
                path.close()
                linePaint.alpha = (255f * ratioNow).toInt()
                canvas.drawPath(path,linePaint)
            }
        }
        // 座標を元に戻す
        canvas.restore()


        /*
        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // シェルピンスキーの五角形を描画
        // 1536色のグラデーション
        val bunchSize = polygonLst.size
        polygonLst.forEachIndexed { id1, polygon ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            val path: Path = Path()
            polygon.pointLst.forEachIndexed { id2, myPointF ->
                // 18度右に傾いてるので、左に18度回転する
                var p = MyPointF(polygon.center.x+myPointF.x,polygon.center.y+myPointF.y )

                when ( id2%5 ) {
                    0 -> {
                        path.moveTo(p.x,p.y)
                    }
                    else -> {
                        path.lineTo(p.x,p.y)
                    }
                }
            }
            path.close()
            val color = myColor.create(id1,bunchSize)
            linePaint.color = color.toInt()
            canvas.drawPath(path,linePaint)
        }

        // 座標を元に戻す
        canvas.restore()
        */


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

    // -------------------------------
    // 五角形
    // -------------------------------
    private class Polygon {
        // 中心
        var center: MyPointF = MyPointF()
        // 半径
        var r: Float = 0f
            set(data: Float) {
                field = data
                pointLst.clear()
                (0..4).forEach {
                    val x = data*MyMathUtil.cosf(it.toFloat()*72f+18f)
                    val y = data*MyMathUtil.sinf(it.toFloat()*72f+18f)
                    pointLst.add(MyPointF(x,y))
                }
            }
        // 頂点(中心から見た座標)
        var pointLst: MutableList<MyPointF> = mutableListOf()

        // クローン
        fun copy(): Polygon {
            val dst = Polygon().also {
                it.center = center
                it.r = r
                it.pointLst = pointLst.toMutableList()
            }
            return dst
        }
    }

    // -------------------------------
    // 変形(元⇒先)
    // -------------------------------
    private class Morph {
        // 元
        lateinit var src: Polygon
        // 先
        lateinit var dst: Polygon
    }

}
