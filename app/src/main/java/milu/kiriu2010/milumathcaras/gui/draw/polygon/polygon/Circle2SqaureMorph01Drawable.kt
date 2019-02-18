package milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyVectorF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// ---------------------------------------------
// 円⇔正方形の変形
// ---------------------------------------------
// https://processing.org/examples/morph.html
// ---------------------------------------------
class Circle2SqaureMorph01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 円ベクトル
    // ---------------------------------
    private val vc = mutableListOf<MyVectorF>()

    // -------------------------------------
    // 正方形ベクトル
    // -------------------------------------
    private val vs = mutableListOf<MyVectorF>()

    // ---------------------------------
    // 変形ベクトル
    // ---------------------------------
    private val vm = mutableListOf<MyVectorF>()
    // 補完比率(0.0f-1.0f)
    private var ratio = 0f

    // ---------------------------------
    // 変形方向を決める状態
    // ---------------------------------
    // 円　　 ⇒ 正方形: true
    // 正方形 ⇒ 円　　: false
    // ---------------------------------
    private var state = false

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
    // 円・正方形を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
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

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 円・正方形の初期ベクトル設定
        createVector()
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
        vm.clear()
        vs.clear()

        // 円ベクトルと変形ベクトルを生成
        val d = 45
        (0 until 360 step 9).forEach {
            // -----------------------------------------------------------
            // 円ベクトル
            // 正方形の頂点とインデックスを合わせるため45度位相を足している
            // -----------------------------------------------------------
            val vx = cos((it+d).toFloat()*PI/180f).toFloat()
            val vy = sin((it+d).toFloat()*PI/180f).toFloat()
            val v = MyVectorF(vx,vy).multiply(side/2f)
            vc.add(v)
        }

        // -----------------------------------------
        // 正方形ベクトルを生成
        // -----------------------------------------
        val s1 = side/4f
        // 正方形の１辺を10分割
        val sdv = side/2f/10f
        //  side/4 => -side/4
        val u2d = FloatArray(10,{ i -> s1-i.toFloat()*sdv })
        // -side/4 =>  side/4
        val d2u = FloatArray(10,{ i -> -s1+i.toFloat()*sdv })

        // 上辺(side/4,side/4) => (-side/4,side/4)
        u2d.forEach {
            vs.add(MyVectorF(it,s1))
        }
        // 左辺(-side/4,side/4) => (-side/4,-side/4)
        u2d.forEach {
            vs.add(MyVectorF(-s1,it))
        }
        // 下辺(-side/4,-side/4) => (side/4,-side/4)
        d2u.forEach {
            vs.add(MyVectorF(it,-s1))
        }
        // 右辺(side/4,-side/4) => (side/4,side/4)
        d2u.forEach {
            vs.add(MyVectorF(s1,it))
        }

        when (state) {
            true -> {
                // 円ベクトルを変形ベクトルにコピー
                vc.forEach {
                    vm.add(it.copy())
                }
            }
            false -> {
                // 正方形ベクトルを変形ベクトルにコピー
                vs.forEach {
                    vm.add(it.copy())
                }
            }
        }

        //Log.d(javaClass.simpleName,"size:vc[${vc.size}]vs[${vs.size}]")
    }

    // -------------------------------
    // 変形する
    // -------------------------------
    private fun morph() {
        // 補完先ベクトル
        val dstV: MutableList<MyVectorF>
        // 補完元ベクトル
        val srcV: MutableList<MyVectorF>

        when (state) {
            true -> {
                // 補完先ベクトル⇒正方形
                dstV = vs
                // 補完元ベクトル⇒円
                srcV = vc
            }
            false -> {
                // 補完先ベクトル⇒円
                dstV = vc
                // 補完元ベクトル⇒正方形
                srcV = vs
            }
        }

        vm.clear()

        srcV.forEachIndexed { index, myVectorF ->
            val v = myVectorF.copy()
            v.lerp(dstV[index],ratio)
            vm.add(v)
        }

        ratio += 0.1f
        if ( ratio > 1f ) {
            ratio = 0f
            state = when (state) {
                true  -> false
                false -> true
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

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // 原点(0,0)の位置
        // = (左右中央,上下中央)
        val x0 = (intrinsicWidth/2).toFloat()
        val y0 = (intrinsicHeight/2).toFloat()

        // 原点(x0,y0)を中心に円・三角形を描く
        canvas.save()
        canvas.translate(x0,y0)

        /*
        // 回転しない多角形を描く
        val pathA = Path()
        polygonA.pointLst.forEachIndexed { index, myPointF ->
            when (index) {
                0 -> pathA.moveTo(myPointF.x,myPointF.y)
                else -> pathA.lineTo(myPointF.x,myPointF.y)
            }

        }
        pathA.close()
        canvas.drawPath(pathA,linePaint)
        */

        // 変形ベクトルを描く
        val path = Path()
        vm.forEachIndexed { index, myVectorF ->
            if ( index == 0 ) {
                path.moveTo(myVectorF.x,myVectorF.y)
            }
            else {
                path.lineTo(myVectorF.x,myVectorF.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
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
    override fun getIntrinsicWidth(): Int = (side+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (side+margin*2).toInt()

    private class Polygon{
        // 多角形の頂点の数
        var vertexCnt = 3
        // 多角形の内角
        var internalAngle = 60f
        // 多角形の一辺の長さ
        var len = 0f
        // 多角形の頂点リスト
        val pointLst: MutableList<MyPointF> = mutableListOf()

        // 多角形の頂点の数から内角を計算する
        fun calInternalAngle() {
            internalAngle = 180f-(360f/vertexCnt.toFloat())
        }

        // 多角形の一辺の長さを頂点リストから計算する
        fun calLen() {
            val x0 = pointLst[0].x
            val y0 = pointLst[0].y
            val x1 = pointLst[1].x
            val y1 = pointLst[1].y
            len = sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))
        }
    }

    private class Track {
        // 描画点リスト
        val pointLst: MutableList<MyPointF> = mutableListOf()
    }
}