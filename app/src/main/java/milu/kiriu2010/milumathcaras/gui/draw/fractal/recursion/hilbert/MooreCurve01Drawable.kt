package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// --------------------------------------------------
// ムーア曲線
// --------------------------------------------------
// https://en.wikipedia.org/wiki/Moore_curve
// --------------------------------------------------
class MooreCurve01Drawable: MyDrawable() {
    // -------------------------------------
    // 描画領域
    // -------------------------------------
    // ムーア曲線は
    // 正方形を２分割するので２の階乗を選ぶ
    //   = 1024 = 2^10
    // -------------------------------------
    private val side = 1024f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  7回以上描くと、塗りつぶされていしまうので6回としている
    // --------------------------------------------------------
    private val nMax = 6

    // ----------------------------------------
    // ムーア曲線の描画点リスト
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
    // ムーア曲線を描くペイント
    // -------------------------------------
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
    // ムーア曲線"コ"の開いている向き
    // ---------------------------------------
    private enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
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

        // ムーア曲線を構築
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

    // ムーア曲線を構築
    private fun createPath() {
        // ムーア曲線の描画点リストをクリアする
        pointLst.clear()

        // ------------------------------
        // ムーア曲線の頂点を描く
        // 正方形の頂点のリスト
        // ------------------------------
        // a:左上,b:左下,c:右下,d:右上,
        // ------------------------------
        val a = MyPointF(0f,side)
        val b = MyPointF(0f,0f)
        val c = MyPointF(side,0f)
        val d = MyPointF(side,side)

        // 次レベルのムーア曲線の描画点を求める
        // 初期は上が開いている"コ"を描く
        calNextLevel(a,b,c,d,Direction.UP,nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // -----------------------------------------------
    // 次レベルのムーア曲線の描画点を求める
    // -----------------------------------------------
    private fun calNextLevel(a: MyPointF, b: MyPointF, c: MyPointF, d: MyPointF, direction: Direction, n: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( n <= 0 ) {
            // ---------------------------------------------------
            // AD
            // BC
            // ---------------------------------------------------
            // a3a2d3d2
            // a0a1d0d2
            // b3b2c3c2
            // b0b1c0c1
            // ---------------------------------------------------
            // ムーア曲線は、a1,b2,c3,d0を頂点とした線を描く
            // "a2-a1"の長さ="A-B"の1/4
            // "a1-b2"の長さ="A-B"の1/2
            // "b2-b1"の長さ="A-B"の1/4
            // ---------------------------------------------------
            // "A-D"間の"左:右=1:3"の位置
            val l1r3 =(3f*a.x+d.x)/4f
            // "A-D"間の"左:右=3:1"の位置
            val l3r1 =(a.x+3f*d.x)/4f
            // "A-B"間の"上:下=1:3"の位置
            val u1d3 =(3f*a.y+b.y)/4f
            // "A-B"間の"上:下=3:1"の位置
            val u3d1 =(a.y+3f*b.y)/4f

            // a1の位置
            val a1 = MyPointF(l1r3,u1d3)
            // b2の位置
            val b2 = MyPointF(l1r3,u3d1)
            // c3の位置
            val c3 = MyPointF(l3r1,u3d1)
            // d0の位置
            val d0 = MyPointF(l3r1,u1d3)

            when (direction) {
                // 上が開いている"コ"を描く
                Direction.UP -> {
                    pointLst.add(a1)
                    pointLst.add(b2)
                    pointLst.add(c3)
                    pointLst.add(d0)
                }
                // 下が開いている"コ"を描く
                Direction.DOWN -> {
                    pointLst.add(c3)
                    pointLst.add(d0)
                    pointLst.add(a1)
                    pointLst.add(b2)
                }
                // 左が開いている"コ"を描く
                Direction.LEFT -> {
                    pointLst.add(a1)
                    pointLst.add(d0)
                    pointLst.add(c3)
                    pointLst.add(b2)
                }
                // 右が開いている"コ"を描く
                Direction.RIGHT -> {
                    pointLst.add(c3)
                    pointLst.add(b2)
                    pointLst.add(a1)
                    pointLst.add(d0)
                }
            }
            return
        }

        // 次のレベルのムーア曲線を描くための
        // 正方形の頂点を求める
        // ---------------------------------------
        // AD
        // BC
        // ---------------------------------------
        // AED
        // FGH
        // BIC
        // ---------------------------------------
        // Gは、正方形ABCDの中央
        // ---------------------------------------
        // "A-D"のX座標の中央値
        val a1d1 = (a.x+d.x)/2f
        // "A-B"のY座標の中央値
        val a1b1 = (a.y+b.y)/2f
        // E
        val e = MyPointF(a1d1,a.y)
        // F
        val f = MyPointF(a.x,a1b1)
        // G
        val g = MyPointF(a1d1,a1b1)
        // H
        val h = MyPointF(d.x,a1b1)
        // I
        val i = MyPointF(a1d1,b.y)

        // 次レベルのムーア曲線を描画
        when (direction) {
            // -------------------------------
            // 前レベルが上向き
            // -------------------------------
            // 右上:左向き
            // 右下:左向き
            // 左下:右向き
            // 左上:右向き
            // -------------------------------
            Direction.UP -> {
                if ( n == nNow ) {
                    // 右上:左向き
                    calNextLevel(e,g,h,d,Direction.LEFT,n-1)
                    // 右下:左向き
                    calNextLevel(g,i,c,h,Direction.LEFT,n-1)
                    // 左下:右向き
                    calNextLevel(f,b,i,g,Direction.RIGHT,n-1)
                    // 左上:右向き
                    calNextLevel(a,f,g,e,Direction.RIGHT,n-1)
                }
                else {
                    // 左上:左向き
                    calNextLevel(a,f,g,e,Direction.LEFT,n-1)
                    // 左下:上向き
                    calNextLevel(f,b,i,g,Direction.UP,n-1)
                    // 右下:上向き
                    calNextLevel(g,i,c,h,Direction.UP,n-1)
                    // 右上:右向き
                    calNextLevel(e,g,h,d,Direction.RIGHT,n-1)
                }
        }
            // -------------------------------
            // 前レベルが下向き
            // -------------------------------
            // 右下:右向き
            // 右上:下向き
            // 左上:下向き
            // 左下:左向き
            // -------------------------------
            Direction.DOWN -> {
                // 右下:右向き
                calNextLevel(g,i,c,h,Direction.RIGHT,n-1)
                // 右上:下向き
                calNextLevel(e,g,h,d,Direction.DOWN,n-1)
                // 左上:下向き
                calNextLevel(a,f,g,e,Direction.DOWN,n-1)
                // 左下:左向き
                calNextLevel(f,b,i,g,Direction.LEFT,n-1)
            }
            // -------------------------------
            // 前レベルが左向き
            // -------------------------------
            // 左上:上向き
            // 右上:左向き
            // 右下:左向き
            // 左下:下向き
            // -------------------------------
            Direction.LEFT -> {
                // 左上:上向き
                calNextLevel(a,f,g,e,Direction.UP,n-1)
                // 右上:左向き
                calNextLevel(e,g,h,d,Direction.LEFT,n-1)
                // 右下:左向き
                calNextLevel(g,i,c,h,Direction.LEFT,n-1)
                // 左下:下向き
                calNextLevel(f,b,i,g,Direction.DOWN,n-1)
            }
            // -------------------------------
            // 前レベルが右向き
            // -------------------------------
            // 右下:下向き
            // 左下:右向き
            // 左上:右向き
            // 右上:上向き
            // -------------------------------
            else -> {
                // 右下:下向き
                calNextLevel(g,i,c,h,Direction.DOWN,n-1)
                // 左下:右向き
                calNextLevel(f,b,i,g,Direction.RIGHT,n-1)
                // 左上:右向き
                calNextLevel(a,f,g,e,Direction.RIGHT,n-1)
                // 右上:上向き
                calNextLevel(e,g,h,d,Direction.UP,n-1)
            }
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
        //  = (マージン,マージン)
        // ---------------------------------------------------------------------
        canvas.save()
        canvas.translate(margin, margin)

        /*
        //Log.d(javaClass.simpleName,"===============================")
        // ムーア曲線を描画
        val path: Path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            if (index == 0) {
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

        // ムーア曲線を描画
        // 1536色のグラデーション
        val bunchSize = pointLst.size
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            if ( myPointF2 != null ) {
                val color = myColor.create(index,bunchSize)
                linePaint.color = color.toInt()
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
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
