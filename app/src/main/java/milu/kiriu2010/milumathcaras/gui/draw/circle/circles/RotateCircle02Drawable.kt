package milu.kiriu2010.milumathcaras.gui.draw.circle.circles

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.sqrt

// --------------------------------------------
// 円の中に円を描き,すべての円を回転させる
// --------------------------------------------
class RotateCircle02Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 再帰レベル
    // ---------------------------------
    private var nNow = 0
    private val nMax = 3

    // -------------------------------
    // 円の回転角度
    // -------------------------------
    private var angle = 0f
    private var angleMax = 360f

    // -------------------------------------
    // 円リスト
    // -------------------------------------
    private val circleLst = mutableListOf<Circle>()

    private val circleTest1 = Circle().apply {
        c = MyPointF(200f,200f)
        r = 100f
    }
    private val circleTest2 = Circle().apply {
        c = MyPointF(100f,100f)
        r = 100f
    }

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

    // -----------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // -----------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:描画角度の初期位置
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        /*
        // 描画角度の初期位置
        angleInit = 0f
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 描画角度の初期位置
                0 -> angleInit = fl
            }
        }
        */

        // 円を生成
        createCircle()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 円を生成
                    createCircle()
                    // 円を移動する
                    moveCircle()
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
    // 円を生成
    // -------------------------------
    private fun createCircle() {
        circleLst.clear()
        val circle = Circle().apply {
            c = MyPointF()
            r = side/2f
            color = Color.WHITE
            g = 0
        }

        nNow = 4
        addCircle(circle,nNow-1)
    }

    // -------------------------------
    // 子円を追加する
    // -------------------------------
    private fun addCircle(cp: Circle, n: Int ) {
        circleLst.add(cp)
        if ( n <= 0 ) {
            return
        }

        // 正三角形
        //　次世代の円の半径 = 前世代の円の半径 * (2*sqrt(3)-3)
        //val rN = cp.r * (2f*sqrt(3f)-3f)
        // 正五角形
        // 次の星の半径 = 前の星の半径 * (3-sqrt(5))/2
        val rN = cp.r * (3f-sqrt(5f))/2f

        // "次世代の円の中心 - 前世代の円の中心"の距離 =
        //   前世代の円の半径 - 次世代の円の半径
        val lN = cp.r - rN

        // --------------------------------------
        // 次世代の円
        // --------------------------------------
        /* 正三角形
        (0..2).forEach { id ->
            val cC = Circle().apply {
                // 中心
                c = cp.c.copy()
                    .plusSelf(
                        MyPointF().apply {
                            x = lN*MyMathUtil.cosf(id.toFloat()*120f+90f+angle*(nNow-n).toFloat())
                            y = lN*MyMathUtil.sinf(id.toFloat()*120f+90f+angle*(nNow-n).toFloat())
                        }
                    )
                // 半径
                r = rN
                // 色
                color = when {
                    (cp.color == Color.WHITE) and ( id == 0 ) -> Color.RED
                    (cp.color == Color.WHITE) and ( id == 1 ) -> Color.GREEN
                    (cp.color == Color.WHITE) and ( id == 2 ) -> Color.BLUE
                    else -> Color.WHITE
                }
                // 世代
                g = nNow - n
                // 親円
                p = cp
            }

            addCircle(cC,n-1)
        }
        */
        // 正五角形
        (0..4).forEach { id ->
            val cC = Circle().apply {
                // 中心
                c = cp.c.copy()
                    .plusSelf(
                        MyPointF().apply {
                            x = lN*MyMathUtil.cosf(id.toFloat()*72f+18f+angle*(nNow-n).toFloat())
                            y = lN*MyMathUtil.sinf(id.toFloat()*72f+18f+angle*(nNow-n).toFloat())
                        }
                    )
                // 半径
                r = rN
                // 色
                //color = when {
                //    (cp.color == Color.WHITE) and ( id == 0 ) -> Color.RED
                //    (cp.color == Color.WHITE) and ( id == 1 ) -> Color.YELLOW
                //    (cp.color == Color.WHITE) and ( id == 2 ) -> Color.GREEN
                //    (cp.color == Color.WHITE) and ( id == 3 ) -> Color.MAGENTA
                //    (cp.color == Color.WHITE) and ( id == 4 ) -> Color.BLUE
                //    else -> Color.WHITE
                //}
                color = when {
                    ( id == 0 ) -> Color.RED
                    ( id == 1 ) -> Color.YELLOW
                    ( id == 2 ) -> Color.GREEN
                    ( id == 3 ) -> Color.MAGENTA
                    ( id == 4 ) -> Color.BLUE
                    else -> Color.WHITE
                }
                // 世代
                g = nNow - n
                // 親円
                p = cp
            }

            addCircle(cC,n-1)
        }

    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveCircle() {
        angle += 5f
        if ( angle >= angleMax ) {
            angle = 0f
        }
    }
    /*
    private fun moveCircle() {
        angle = 5f
        angle = 30f
    }
    */

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

        // 原点(x0,y0)を中心に円を描く
        canvas.save()
        canvas.translate(x0,y0)

        Log.d(javaClass.simpleName,"==================================")
        // 円を描く
        (0..nNow).forEach { i ->
            circleLst.filter{ it.g == i }.forEach { circle ->
                Log.d(javaClass.simpleName,"===[$i]===========================")
                linePaint.color = circle.color
                canvas.drawCircle(circle.c.x,circle.c.y,circle.r,linePaint)
                canvas.drawCircle(circle.c.x,circle.c.y,circle.r,framePaint)
            }
        }
        /*
        (0..nNow).forEach { i ->
            circleLst.filter{ it.g == i }.forEach { circle ->
                Log.d(javaClass.simpleName,"===[$i]===========================")
                linePaint.color = circle.color
                if ( circle.p == null ) {
                    canvas.drawCircle(circle.c.x,circle.c.y,circle.r,linePaint)
                    canvas.drawCircle(circle.c.x,circle.c.y,circle.r,framePaint)
                }
                // 親世代がある場合、
                // 前世代の中心位置に対して現在の中心位置を回転して
                // 円を描画
                else {
                    //val cc = rotate(angle,circle,false)
                    val cc = rotate(angle,circle,true)
                    canvas.drawCircle(cc.x,cc.y,circle.r,linePaint)
                    canvas.drawCircle(cc.x,cc.y,circle.r,framePaint)

                    Log.d(javaClass.simpleName,"x[${cc.x}y[{$cc.y}]")
                }
            }
        }
        */

        /*
        linePaint.color = Color.WHITE
        canvas.drawCircle(circleTest2.c.x,circleTest2.c.y,circleTest2.r,linePaint)
        canvas.drawCircle(circleTest2.c.x,circleTest2.c.y,circleTest2.r,framePaint)

        canvas.drawCircle(circleTest2.c.x,circleTest2.c.y,5f,linePaint)
        canvas.drawCircle(circleTest2.c.x,circleTest2.c.y,5f,framePaint)

        circleTest1.c.rotate(angle, MyPointF(100f,100f))
        linePaint.color = Color.RED
        canvas.drawCircle(circleTest1.c.x,circleTest1.c.y,circleTest1.r,linePaint)
        canvas.drawCircle(circleTest1.c.x,circleTest1.c.y,circleTest1.r,framePaint)
        */

        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    private fun rotate( angleDv: Float, circle: Circle, isCopy: Boolean ): MyPointF {
        if ( circle.p == null) {
            return MyPointF()
        }
        else {
            val p = rotate( angleDv, circle.p!!, true )
            val cc = when (isCopy) {
                true -> circle.c.copy().rotate(angleDv,p)
                false -> circle.c.rotate(angleDv,p)
            }

            return cc
        }
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
    // 円
    // ---------------------------------
    data class Circle (
        // ---------------------------------
        // 中心
        // ---------------------------------
        var c: MyPointF = MyPointF(),
        // ---------------------------------
        // 半径
        // ---------------------------------
        var r: Float = 0f,
        // ---------------------------------
        // 色
        // ---------------------------------
        var color: Int = 0,
        // ---------------------------------
        // 世代
        // ---------------------------------
        var g: Int = 0,
        // ---------------------------------
        // 親円
        // ---------------------------------
        var p: Circle? = null
    )

}