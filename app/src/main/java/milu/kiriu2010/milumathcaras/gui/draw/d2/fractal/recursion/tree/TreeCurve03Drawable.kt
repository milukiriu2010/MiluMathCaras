package milu.kiriu2010.milumathcaras.gui.draw.d2.fractal.recursion.tree

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// ----------------------------------------------------
// 樹木曲線(すぼむ)
// ----------------------------------------------------
// https://qiita.com/kmtoki/items/b94892771132e30aa1a2
// ----------------------------------------------------
class TreeCurve03Drawable: MyDrawable() {
    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1024f
    private val margin = 50f

    // --------------------------------------------------------
    // 樹木の次の枝を描く際の角度の係数
    // --------------------------------------------------------
    private var nNow = 1f
    private val nMax = 50f
    private var sign = 1

    // ----------------------------------------
    // 現在の枝の長さ：次の枝の長さの比率
    // ----------------------------------------
    private var lenRatio = 0.7f

    // ----------------------------------------
    // 現在の枝に対し、次の枝の角度
    // ----------------------------------------
    private var angleN = 5f
    private var angleDv =1.2f

    // ----------------------------------------
    // 樹木曲線の描画点リスト
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

    // -------------------------------
    // 樹木曲線を描くペイント
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
    // ----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {

        // 樹木曲線を構築
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
                    // 樹木曲線を構築
                    createPath()
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

    // 樹木曲線を構築
    private fun createPath() {
        // 樹木曲線の描画点リストをクリアする
        pointLst.clear()

        // -------------------------
        // 樹木曲線の頂点
        // -------------------------
        val a = MyPointF(side/2f,0f)
        val b = MyPointF(side/2f,side/4f)

        // 次レベルの樹木曲線の描画点を求める
        calNextLevel(a,b,angleN,angleDv*nNow,10)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }


    // -------------------------------------
    // 次レベルの樹木曲線の描画点を求める
    // -------------------------------------
    private fun calNextLevel(a: MyPointF, b: MyPointF, angle: Float, angleDiff: Float, n: Int) {
        pointLst.add(a)
        pointLst.add(b)

        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if (n <= 0) {
            return
        }

        // -----------------------------------------------------
        // 次の枝の長さ"B-C" = 現在の枝"A-B"の長さ×lenRatio
        // 次の枝の長さ"B-D" = 現在の枝"A-B"の長さ×lenRatio
        // -----------------------------------------------------
        val lenNext = b.distance(a)*lenRatio

        // -----------------------------------------------------
        // 現在の枝"A-B"の角度
        // -----------------------------------------------------
        val angleAB = MyMathUtil.getAngle(a,b)

        // -----------------------------------------------------
        // 次の枝の角度"B-C" = 現在の枝"A-B"の角度 - angle
        // -----------------------------------------------------
        val angleBC = angleAB - angle

        // -----------------------------------------------------
        // 次の枝の角度"B-D" = 現在の枝"A-B"の角度 + angle
        // -----------------------------------------------------
        val angleBD = angleAB + angle

        // 描画点C
        val cx = b.x + lenNext*cos(angleBC*PI/180f).toFloat()
        val cy = b.y + lenNext*sin(angleBC*PI/180f).toFloat()
        val c = MyPointF(cx,cy)

        // 描画点D
        val dx = b.x + lenNext*cos(angleBD*PI/180f).toFloat()
        val dy = b.y + lenNext*sin(angleBD*PI/180f).toFloat()
        val d = MyPointF(dx,dy)

        // 次レベルの樹木曲線を描画
        calNextLevel(b,c,angle*angleDiff,angleDiff,n-1)
        calNextLevel(b,d,angle*angleDiff,angleDiff,n-1)
    }

    // -------------------------------------
    // 再帰レベルを１つ増やす
    // -------------------------------------
    private fun incrementLevel() {
        when ( sign ) {
            1 -> {
                nNow++
                // 最大値を超えたら減らしていく
                if ( nNow > nMax ) {
                    sign = -1
                    nNow = nMax - 1
                }
            }
            -1 -> {
                nNow--
                // 1を下回ったら増やしていく
                if ( nNow < 1 ) {
                    sign = 1
                    nNow = 1f
                }
            }
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

        canvas.save()
        // ---------------------------------------------------------------------
        // 原点(0,0)の位置
        //  = (マージン,マージン)
        // ---------------------------------------------------------------------
        canvas.translate(margin,margin)

        /*
        // 樹木曲線を描画
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            if ( index%2 == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // 樹木曲線を描画
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            if ( index%2 == 0 ) {
                path.reset()
                path = Path()
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
                val color = myColor.create(index,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawPath(path,linePaint)
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