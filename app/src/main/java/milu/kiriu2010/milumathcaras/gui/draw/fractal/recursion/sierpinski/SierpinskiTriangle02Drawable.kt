package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.sierpinski

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// --------------------------------------------------
// シェルピンスキーの三角形02
// --------------------------------------------------
// https://en.wikipedia.org/wiki/Sierpinski_triangle
// --------------------------------------------------
// 2019.06.08
// --------------------------------------------------
class SierpinskiTriangle02Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    // レベルが１つ増えるごとに
    // 三角形の辺を２分割するので
    // 1024 = 2^10 としている
    // ---------------------------------
    private val side = 1024f
    private val margin = 0f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 8

    // スケール
    private var scaleNow = 2f
    private var scalelDv = 0.2f
    private val scaleMin = 2f
    private val scaleMax = 4f

    // ----------------------------------------
    // シェルピンスキーの三角形の描画点リスト
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

    // ----------------------------------
    // シェルピンスキーの三角形を描くペイント
    // ----------------------------------
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
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // シェルピンスキーの三角形を構築
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
                    // スケールを変える
                    incrementScale()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    handler.postDelayed(runnable, 10)
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

    // シェルピンスキーの三角形を構築
    private fun createPath() {
        // シェルピンスキーの三角形の描画点リストをクリアする
        pointLst.clear()

        // ------------------------------
        // シェルピンスキーの三角形の頂点
        // ------------------------------
        // a:左,b:右,c:中央
        // ------------------------------
        val a = MyPointF(0f,0f)
        val b = MyPointF(side,0f)
        val c = MyPointF(side/2f,side*sqrt(3f)/2f)

        // 次レベルのシェルピンスキーの三角形の描画点を求める
        calNextLevel(a,b,c,nNow)
    }

    // -------------------------------------
    // 次レベルのシェルピンスキーの三角形の描画点を求める
    // -------------------------------------
    private fun calNextLevel(a: MyPointF, b: MyPointF, c: MyPointF, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            pointLst.add(a)
            pointLst.add(b)
            pointLst.add(c)
            return
        }

        // 次のレベルの正三角形の頂点を求める
        // "A-B"の中点AB
        val ab = MyPointF( (a.x+b.x)/2f, (a.y+b.y)/2f)
        // "B-C"の中点BC
        val bc = MyPointF( (b.x+c.x)/2f, (b.y+c.y)/2f)
        // "C-A"の中点CA
        val ca = MyPointF( (c.x+a.x)/2f, (c.y+a.y)/2f)

        // 次レベルのシェルピンスキーの三角形を描画
        // A,AB,CAを頂点とする三角形
        calNextLevel(a,ab,ca,n-1)
        // AB,B,BCを頂点とする三角形
        calNextLevel(ab,b,bc ,n-1)
        // CA,BC,Cを頂点とする三角形
        calNextLevel(ca,bc,c ,n-1)
    }

    // -------------------------------------
    // スケールを変える
    // -------------------------------------
    private fun incrementScale() {
        scaleNow += scalelDv
        if ( scaleNow > scaleMax ) {
            scaleNow = scaleMin
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
        // 原点(0,0)の位置 => 三角形を中央に描きたい
        //  = (マージン,"描画領域の高さー三角形の高さ"/2)
        // ---------------------------------------------------------------------
        val ymargin = (intrinsicHeight.toFloat() - side*sqrt(3f)/2f)/2f

        canvas.save()
        //Log.d(javaClass.simpleName, "ymargin[$ymargin]intrinsicHeight[$intrinsicHeight]")
        canvas.translate(margin, ymargin)

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // シェルピンスキーの三角形を描画
        var path: Path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            when ( index%3 ) {
                0 -> {
                    path = Path()
                    path.moveTo(myPointF.x*scaleNow,myPointF.y*scaleNow)
                }
                1 -> {
                    path.lineTo(myPointF.x*scaleNow,myPointF.y*scaleNow)
                }
                else -> {
                    path.lineTo(myPointF.x*scaleNow,myPointF.y*scaleNow)
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
