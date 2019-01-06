package milu.kiriu2010.milumathcaras.gui.draw.fractal.sierpinski

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// --------------------------------------------------
// シェルピンスキーのカーペット
// --------------------------------------------------
// https://en.wikipedia.org/wiki/Sierpinski_triangle
// --------------------------------------------------
class SierpinskiCarpet01Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    // シェルピンスキーのカーペットは
    // 正方形を３分割するので３の階乗を選ぶ
    //   = 729 = 3^6
    // ---------------------------------
    private val side = 729f
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
    // シェルピンスキーのカーペットの描画点リスト
    // ----------------------------------------
    private val pointLst = mutableListOf<MyPointF>()

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
    // シェルピンスキーのカーペットを描くペイント
    // -------------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 1f
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

        // シェルピンスキーのカーペットを構築
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()


        // 描画に使うスレッド
        if (isKickThread) {
            runnable = Runnable {
                // 再帰レベルを１つ増やす
                incrementLevel()
                // 三角波を構築
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

    // シェルピンスキーのカーペットを構築
    private fun createPath() {
        // シェルピンスキーのカーペットの描画点リストをクリアする
        pointLst.clear()

        // ------------------------------
        // シェルピンスキーのカーペットの頂点
        // ------------------------------
        // a:左下,b:右下,c:右上,d:左上
        // ------------------------------
        val a = MyPointF(0f,0f)
        val b = MyPointF(side,0f)
        val c = MyPointF(side,side)
        val d = MyPointF(0f,side)

        // 次レベルのシェルピンスキーのカーペットの描画点を求める
        calNextLevel(a,b,c,d,nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // -----------------------------------------------
    // 次レベルのシェルピンスキーのカーペットの描画点を求める
    // -----------------------------------------------
    private fun calNextLevel(a: MyPointF, b: MyPointF, c: MyPointF, d: MyPointF, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            pointLst.add(a)
            pointLst.add(b)
            pointLst.add(c)
            pointLst.add(d)
            return
        }

        // 次のレベルのカーペットの頂点を求める
        // -----------------------------------
        // d3,d2,c3,c2
        // d0,d1,c0,c1
        // a3,a2,b3,b2
        // a0,a1,b0,b1
        // -----------------------------------
        // "A-B"のX座標の"左:右=1:3"
        val l1r3 = (2f*a.x+b.x)/3f
        // "A-B"のX座標の"左:右=3:1"
        val l3r1 = (a.x+2f*b.x)/3f
        // "D-A"のY座標の"上:下=1:3"
        val u1d3 = (2f*d.y+a.y)/3f
        // "D-A"のY座標の"上:下=3:1"
        val u3d1 = (d.y+2f*a.y)/3f

        // a0:A
        val a0 = a
        // a1:("A-B"の1/3,A)
        val a1 = MyPointF(l1r3,a.y)
        // a2:("A-B"の1/3,"D-A"の2/3)
        val a2 = MyPointF(l1r3,u3d1)
        // a3:(A,"D-A"の2/3)
        val a3 = MyPointF(a.x,u3d1)
        // b0:("A-B"の2/3,B)
        val b0 = MyPointF(l3r1,b.y)
        // b1:B
        val b1 = b
        // b2:(B,"D-A"の2/3)
        val b2 = MyPointF(b.x,u3d1)
        // b3:("A-B"の2/3,"D-A"の2/3)
        val b3 = MyPointF(l3r1,u3d1)
        // c0:("A-B"の2/3,"D-A"の1/3)
        val c0 = MyPointF(l3r1,u1d3)
        // c1:(C,"D-A"の1/3)
        val c1 = MyPointF(c.x,u1d3)
        // c2:C
        val c2 = c
        // c3:("A-B"の2/3,C)
        val c3 = MyPointF(l3r1,c.y)
        // d0:(D,"D-A"の1/3)
        val d0 = MyPointF(d.x,u1d3)
        // d1:("A-B"の1/3,"D-A"の1/3)
        val d1 = MyPointF(l1r3,u1d3)
        // d2:("A-B"の1/3,D)
        val d2 = MyPointF(l1r3,d.y)
        // d3:D
        val d3 = d

        // 次レベルのシェルピンスキーのカーペットを描画
        // a0,a1,a2,a3を頂点とするカーペット
        calNextLevel(a0,a1,a2,a3,n-1)
        // a1,b0,b3,a2を頂点とするカーペット
        calNextLevel(a1,b0,b3,a2,n-1)
        // b0,b1,b2,b3を頂点とするカーペット
        calNextLevel(b0,b1,b2,b3,n-1)
        // a3,a2,d1,d0を頂点とするカーペット
        calNextLevel(a3,a2,d1,d0,n-1)
        // b3,b2,c1,c0を頂点とするカーペット
        calNextLevel(b3,b2,c1,c0,n-1)
        // d0,d1,d2,d3を頂点とするカーペット
        calNextLevel(d0,d1,d2,d3,n-1)
        // d1,c0,c3,d2を頂点とするカーペット
        calNextLevel(d1,c0,c3,d2,n-1)
        // c0,c1,c2,c3を頂点とするカーペット
        calNextLevel(c0,c1,c2,c3,n-1)
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
        //  = (マージン,マージン)
        // ---------------------------------------------------------------------
        canvas.save()
        canvas.translate(margin, margin)

        //Log.d(javaClass.simpleName,"===============================")
        // シェルピンスキーのカーペットを描画
        var path: Path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            when ( index%4 ) {
                0 -> {
                    path = Path()
                    path.moveTo(myPointF.x,myPointF.y)
                }
                1 -> {
                    path.lineTo(myPointF.x,myPointF.y)
                }
                2 -> {
                    path.lineTo(myPointF.x,myPointF.y)
                }
                else -> {
                    path.lineTo(myPointF.x,myPointF.y)
                    path.close()
                    canvas.drawPath(path,linePaint)
                }
            }
        }

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画は上下逆なので反転する
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
        canvas.drawBitmap(tmpBitmap,0f,0f,backPaint)
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
