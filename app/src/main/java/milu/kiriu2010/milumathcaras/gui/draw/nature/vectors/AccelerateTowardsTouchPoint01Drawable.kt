package milu.kiriu2010.milumathcaras.gui.draw.nature.vectors

import android.graphics.*
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyCircleF
import milu.kiriu2010.gui.basic.MyVectorF
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ------------------------------------------------------------------
// リリース⇒ランダムウォーク
// タッチ  ⇒タッチ点に向かって加速
// ------------------------------------------------------------------
// https://natureofcode.com/book/chapter-1-vectors/
// ------------------------------------------------------------------
class AccelerateTowardsTouchPoint01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val sideW = 1000f
    private val sideH = 1500f
    private val margin = 0f

    // ---------------------------------
    // ランダムウォークする円の最大数
    // ---------------------------------
    private var nMax = 10

    // ---------------------------------
    // ランダムウォークする円の半径
    // ---------------------------------
    private val r = 50f

    // ---------------------------------
    // ランダムウォークする円リスト
    // ---------------------------------
    private val circleLst: MutableList<MyCircleF> = mutableListOf()

    // ---------------------------------
    // タッチ点
    // ---------------------------------
    private val touchPoint = MyVectorF(-1f,-1f)

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
    // ランダムウォークする円を描くペイント
    // -------------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
    }

    // -------------------------------------
    // ランダムウォークする円を描く色リスト
    // -------------------------------------
    private val colorLst = arrayOf(
        // 0:red
        0xffff0000.toInt(),
        // 1:pink
        0xffffcccc.toInt(),
        // 2:orange
        0xffff7f00.toInt(),
        // 3:maroon
        0xff800000.toInt(),
        // 4:green(lime)
        0xff00ff00.toInt(),
        // 5:blue
        0xff0000ff.toInt(),
        // 6:cyan
        0xff00ffff.toInt(),
        // 7:indigo
        0xff6f00ff.toInt(),
        // 8:violet
        0xffff00ff.toInt(),
        // 9:green
        0xff008000.toInt()
    )

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
    // 第１引数:ランダムウォークする円の最大数
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 可変変数 values を初期値として、このクラスで使う変数に当てはめる
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // ランダムウォークする円の最大数
                0 -> nMax = fl.toInt()
            }
        }

        // ランダムウォークする円を生成
        while (createMover())
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // ランダムウォークする円を生成
                    createMover()
                    // 円を移動する
                    moveMover()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    // 10msごとに描画
                    handler.postDelayed(runnable, 10)
                }
                // "停止"状態のときは、更新されないよう処理をスキップする
                else {
                    handler.postDelayed(runnable, 100)
                }
            }
            handler.postDelayed(runnable, 100)
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
    // TouchCallback
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    override fun calStop() {
        // 描画に使うスレッドを解放する
        handler.removeCallbacks(runnable)
    }

    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(event: MotionEvent) {
        //Log.d(javaClass.simpleName,"Touch:x[${x}]y[${y}]" )
        when (event.action ) {
            MotionEvent.ACTION_UP -> {
                touchPoint.x = -1f
                touchPoint.y = -1f
            }
            else -> {
                touchPoint.x = event.x
                touchPoint.y = event.y
            }
        }
    }

    // -------------------------------
    // ランダムウォークする円を生成
    // -------------------------------
    private fun createMover(): Boolean {
        // 既に最大数を超えていたら何もしない
        if ( circleLst.size >= nMax ) return false

        // 円の初期位置を描画領域内でランダムに設定
        val x = (r.toInt()..(sideW-r).toInt()).shuffled()[0].toFloat()
        val y = (r.toInt()..(sideH-r).toInt()).shuffled()[0].toFloat()

        // 初期速度の候補リスト
        // -45,-35,-25,-15,-5,5,15,25,35,45
        //val v = IntArray(10,{ i -> i*10-45} )
        // -95,-75,-55,-35,-15,15,35,55,75,95
        //val v = IntArray(10,{ i -> i*20-95} )
        // -15,-10,-5,0,5,10,15
        val v = IntArray(7,{ i -> i*5-15} )
        // 円の初期速度をランダムに設定
        val vx = v.random().toFloat()
        val vy = v.random().toFloat()

        // 加速度を変更する
        // -45,-35,-25,-15,-5,5,15,25,35,45
        //val a = IntArray(10, { i -> i*10-45})
        // -5,-4,-3,-2,-1,0,1,2,3,4,5
        val a = IntArray(11, { i -> i-5})
        // 円の初期加速度をランダムに設定
        val ax = a.random().toFloat()
        val ay = a.random().toFloat()

        // 色
        val colorId = (0 until colorLst.size).random()

        // 円を生成し、リストに加える
        circleLst.add(MyCircleF(MyVectorF(x,y),r,MyVectorF(vx,vy), MyVectorF(ax,ay), color = colorLst[colorId]))

        return true
    }

    // -------------------------------
    // 円を移動する
    // -------------------------------
    private fun moveMover() {
        val iterator = circleLst.iterator()

        while (iterator.hasNext()) {
            val myCircleF1 = iterator.next()

            if ( touchPoint != MyVectorF(-1f,-1f)) {

                // 円の位置からみたタッチ点のベクトル
                val dir = touchPoint.copy().subtract(myCircleF1.p)
                // 円の加速度を"円の位置からみたタッチ点のベクトル"を元に生成
                val a = dir.multiply(0.1f)
                myCircleF1.a = a

                //Log.d(javaClass.simpleName,"ax[${a.x}]ay[${a.y}]")
            }

            // 円を移動する(速度制限付き)
            myCircleF1.move(20f)
            // 境界を超えていたら、円のリストから削除する
            if ( myCircleF1.overBorder(0f,0f,sideW,sideH) < 0 ) {
                iterator.remove()
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

        // 等速度運動をする円を描画
        val colorCnt = colorLst.size
        circleLst.forEachIndexed { index, myCircleF ->
            //linePaint.color = colorLst[index%colorCnt]
            linePaint.color = myCircleF.color
            canvas.drawCircle(myCircleF.p.x,myCircleF.p.y,myCircleF.r,linePaint)
        }

        // これまでの描画はテンポラリなので、実際の描画に使うビットマップにコピーする
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
    override fun getIntrinsicWidth(): Int = (sideW+margin*2).toInt()

    // -------------------------------
    // Drawable
    // -------------------------------
    override fun getIntrinsicHeight(): Int = (sideH+margin*2).toInt()
}