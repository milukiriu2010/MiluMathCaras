package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.dragon

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -----------------------------------------------
// ドラゴン曲線
// -----------------------------------------------
// https://en.wikipedia.org/wiki/Dragon_curve
// -----------------------------------------------
class DragonCurve01Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    private val side = 1024f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  13回描くと、それ以降は違いがわからないので15回としている
    // --------------------------------------------------------
    private val nMax = 15

    // ----------------------------------------
    // ドラゴン曲線の描画点リスト
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
    // ドラゴン曲線を描くペイント
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

    // ---------------------------------------
    // ドラゴン曲線を描く方向
    // ---------------------------------------
    private enum class Direction {
        PLUS,
        MINUS
    }

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

        // ドラゴン曲線を構築
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

    // ドラゴン曲線を構築
    private fun createPath() {
        // ドラゴン曲線の描画点リストをクリアする
        pointLst.clear()

        // ------------------------------
        // ドラゴン曲線の頂点
        // ------------------------------
        // a:左,b:右
        // ------------------------------
        val a = MyPointF(side/4f,side/2f)
        val b = MyPointF(side*3f/4f,side/2f)

        // 次レベルのドラゴン曲線の描画点を求める
        calNextLevel(a,b,Direction.MINUS,nNow)
        pointLst.add(b)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // -------------------------------------
    // 次レベルのドラゴン曲線の描画点を求める
    // -------------------------------------
    // direction
    //   PLUS :"A-B"の向きに対し、+90度側にCを描画
    //   MINUS:"A-B"の向きに対し、-90度側にCを描画
    // -------------------------------------
    private fun calNextLevel(a: MyPointF, b: MyPointF, direction: Direction, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            pointLst.add(a)
            return
        }

        // -------------------------------------------------
        // 描画点C
        // -------------------------------------------------
        //   "A-C"の長さ: "A-B"の長さ×sqrt(2)/2
        //   "A-C"の角度:
        //      direction=+1 => "A-B"の角度+45度
        //      direction=-1 => "A-B"の角度-45度
        // -------------------------------------------------
        // "A-B"の長さ
        val lenAB = sqrt((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y))*sqrt(2f)/2f
        // "A-B"の角度
        val angleAB = MyMathUtil.getAngle(a,b)
        // "A-C"の角度: "A-B"の角度-30度
        val angleAC = if ( direction == Direction.PLUS ) angleAB + 45.0 else angleAB-45.0

        // 描画点CのX座標
        val cx = a.x + (lenAB * cos(angleAC*PI/180f)).toFloat()
        // 描画点EのY座標
        val cy = a.y + (lenAB * sin(angleAC*PI/180f)).toFloat()
        // 描画点E
        val c = MyPointF(cx,cy)

        // 次レベルのドラゴン曲線を描画
        calNextLevel(a,c,Direction.MINUS,n-1)
        calNextLevel(c,b,Direction.PLUS ,n-1)
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
        //Log.d(javaClass.simpleName, "ymargin[$ymargin]kochH[$kochH]intrinsicHeight[$intrinsicHeight]")
        canvas.translate(margin, margin)

        //Log.d(javaClass.simpleName,"===============================")
        // ドラゴン曲線を描画
        val path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            if ( index == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        canvas.drawPath(path,linePaint)

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
