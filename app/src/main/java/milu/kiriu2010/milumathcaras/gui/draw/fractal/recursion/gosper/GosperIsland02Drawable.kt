package milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.gosper

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.*

// -----------------------------------------------
// ゴスパー島
// -----------------------------------------------
// https://www.mathcurve.com/fractals/gosper/gosper.shtml
// http://ecademy.agnesscott.edu/~lriddle/ifs/ksnow/flowsnake.htm
// -----------------------------------------------
class GosperIsland02Drawable: MyDrawable() {
    // ---------------------------------
    // 描画領域
    // ---------------------------------
    private val side = 1000f
    private val margin = 50f

    // ----------------------------------------
    // 再帰レベル
    // ----------------------------------------
    private var nNow = 0
    // --------------------------------------------------------
    // 再帰レベルの最大値
    //  3回描くと、それ以降は違いがわからないので6回としている
    // --------------------------------------------------------
    private val nMax = 4

    // ----------------------------------------
    // ゴスパー島の回転角度
    // ----------------------------------------
    // arcsin(sqrt(3)/(2*sqrt(7))) = 19.1066
    // ----------------------------------------
    private val angle = asin( sqrt(3f)/(2f*sqrt(7f)) ).toFloat()*180f/ PI.toFloat()

    // ----------------------------------------
    // ゴスパー島の描画点リスト
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
    // ゴスパー島を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
        //strokeWidth = 1f
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

        // ゴスパー島を構築
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
                    // ゴスパー島を構築
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

    // ゴスパー島を構築
    private fun createPath() {
        // ゴスパー島の描画点リストをクリアする
        pointLst.clear()

        // ------------------------------
        // ゴスパー島の頂点
        // 正六角形の時の初期位置
        // ------------------------------
        //  ef
        // d  a
        //  cb
        // ------------------------------
        val r = side*2f/5f
        val a = MyPointF(r,0f)
        val b = MyPointF(r*cos(300f*PI/180f).toFloat(),r*sin(300f*PI/180f).toFloat())
        val c = MyPointF(r*cos(240f*PI/180f).toFloat(),r*sin(240f*PI/180f).toFloat())
        val d = MyPointF(-r,0f)
        val e = MyPointF(r*cos(120f*PI/180f).toFloat(),r*sin(120f*PI/180f).toFloat())
        val f = MyPointF(r*cos(60f*PI/180f).toFloat(),r*sin(60f*PI/180f).toFloat())

        // 次レベルのゴスパー島の描画点を求める
        calNextLevel(a,b,nNow)
        calNextLevel(b,c,nNow)
        calNextLevel(c,d,nNow)
        calNextLevel(d,e,nNow)
        calNextLevel(e,f,nNow)
        calNextLevel(f,a,nNow)

        // 描画中に呼び出すコールバックをキックし、現在の再帰レベルを通知する
        notifyCallback?.receive(nNow.toFloat())
    }

    // -------------------------------------
    // 次レベルのゴスパー島の描画点を求める
    // -------------------------------------
    private fun calNextLevel(a: MyPointF, g: MyPointF, m: Int) {
        // -----------------------------------------------------
        // 再帰呼び出しの際、mを減らしていき0以下になったら終了
        // -----------------------------------------------------
        if ( m <= 0 ) {
            pointLst.add(a)
            return
        }

        // 長さ="A-G"の1/sqrt(7)
        val r = a.distance(g).toFloat()/sqrt(7f).toFloat()
        // "A-G"の角度
        val t = MyMathUtil.getAngle(a,g).toFloat()

        // ---------------------------
        // 描画点B
        // ---------------------------
        // 描画点Aからみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="A-G"の角度 + angle
        // ---------------------------
        val b = MyPointF().apply {
            x = a.x + r * cos((t+angle)*PI/180f).toFloat()
            y = a.y + r * sin((t+angle)*PI/180f).toFloat()
        }

        // --------------------------------
        // 描画点C
        // --------------------------------
        // 描画点Bからみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="A-G"の角度 + angle + 180
        // --------------------------------
        val c = MyPointF().apply {
            x = g.x + r * cos((t+angle+180f)*PI/180f).toFloat()
            y = g.y + r * sin((t+angle+180f)*PI/180f).toFloat()
        }

        // --------------------------------
        // 描画点D
        // --------------------------------
        // 描画点Cからみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="B-C"の角度 -60
        // --------------------------------
        val angleBC = MyMathUtil.getAngle(b,c)
        val d = MyPointF().apply {
            x = c.x + r * cos((angleBC-60f)*PI/180f).toFloat()
            y = c.y + r * sin((angleBC-60f)*PI/180f).toFloat()
        }

        // --------------------------------
        // 描画点E
        // --------------------------------
        // 描画点Dからみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="C-D"の角度 -60
        // --------------------------------
        val angleCD = MyMathUtil.getAngle(c,d)
        val e = MyPointF().apply {
            x = d.x + r * cos((angleCD-60f)*PI/180f).toFloat()
            y = d.y + r * sin((angleCD-60f)*PI/180f).toFloat()
        }

        // --------------------------------
        // 描画点F
        // --------------------------------
        // 描画点Eからみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="D-E"の角度 -60
        // --------------------------------
        val angleDE = MyMathUtil.getAngle(d,e)
        val f = MyPointF().apply {
            x = e.x + r * cos((angleDE-60f)*PI/180f).toFloat()
            y = e.y + r * sin((angleDE-60f)*PI/180f).toFloat()
        }

        // 次レベルのゴスパー島を描画
        calNextLevel(a,b,m-1)
        calNextLevel(b,c,m-1)
        calNextLevel(c,d,m-1)
        calNextLevel(d,e,m-1)
        calNextLevel(e,f,m-1)
        calNextLevel(f,a,m-1)

        // -----------------------------------------------------
        // 再帰呼び出しの際、nを減らしていき0以上の場合、
        // 中央の正六角形を追加描画
        // -----------------------------------------------------
        // と思ったが、うまくいかない
        // -----------------------------------------------------
        //if ( g <= 0 ) return

        // -----------------------------------------------------
        // 以降、重複描画されるが、うまい方法が思いつかない
        // -----------------------------------------------------


        // --------------------------------
        // 描画点A2(E)
        // --------------------------------
        val a2 = e.copy()
        // --------------------------------
        // 描画点B2(D)
        // --------------------------------
        val b2 = d.copy()
        // --------------------------------
        // 描画点C2
        // --------------------------------
        // 描画点B2からみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="A2-B2"の角度 -60
        // --------------------------------
        val angleA2B2 = MyMathUtil.getAngle(a2,b2)
        val c2 = MyPointF().apply {
            x = b2.x + r * cos((angleA2B2-60f)*PI/180f).toFloat()
            y = b2.y + r * sin((angleA2B2-60f)*PI/180f).toFloat()
        }
        // --------------------------------
        // 描画点D2
        // --------------------------------
        // 描画点C2からみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="B2-C2"の角度 -60
        // --------------------------------
        val angleB2C2 = MyMathUtil.getAngle(b2,c2)
        val d2 = MyPointF().apply {
            x = c2.x + r * cos((angleB2C2-60f)*PI/180f).toFloat()
            y = c2.y + r * sin((angleB2C2-60f)*PI/180f).toFloat()
        }
        // --------------------------------
        // 描画点E2
        // --------------------------------
        // 描画点D2からみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="C2-D2"の角度 -60
        // --------------------------------
        val angleC2D2 = MyMathUtil.getAngle(c2,d2)
        val e2 = MyPointF().apply {
            x = d2.x + r * cos((angleC2D2-60f)*PI/180f).toFloat()
            y = d2.y + r * sin((angleC2D2-60f)*PI/180f).toFloat()
        }
        // --------------------------------
        // 描画点F2
        // --------------------------------
        // 描画点E2からみて
        //   距離="A-G"の1/sqrt(7)
        //   角度="D2-E2"の角度 -60
        // --------------------------------
        val angleD2E2 = MyMathUtil.getAngle(d2,e2)
        val f2 = MyPointF().apply {
            x = e2.x + r * cos((angleD2E2-60f)*PI/180f).toFloat()
            y = e2.y + r * sin((angleD2E2-60f)*PI/180f).toFloat()
        }
        // 次レベルのゴスパー島を描画
        calNextLevel(a2,b2,m-1)
        calNextLevel(b2,c2,m-1)
        calNextLevel(c2,d2,m-1)
        calNextLevel(d2,e2,m-1)
        calNextLevel(e2,f2,m-1)
        calNextLevel(f2,a2,m-1)
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
        //  = (左右中央,上下中央)
        // ---------------------------------------------------------------------
        canvas.save()
        Log.d(javaClass.simpleName, "circleLst.size[${pointLst.size}]")
        canvas.translate(intrinsicWidth/2f, intrinsicHeight/2f)

        /*
        //Log.d(javaClass.simpleName,"===============================")
        // ゴスパー島を描画
        val path = Path()
        circleLst.forEachIndexed { index, myPointF ->
            //Log.d(javaClass.simpleName,"index[$index]x[${myPointF.x}]y[${myPointF.y}]")
            if ( index == 0 ) {
                path.moveTo(myPointF.x,myPointF.y)
            }
            else {
                path.lineTo(myPointF.x,myPointF.y)
            }
        }
        path.close()
        canvas.drawPath(path,linePaint)
        */

        // 色インスタンス作成
        val myColor = MyColorFactory.createInstance(ColorType.COLOR_1536)

        // ゴスパー島を描画
        // 1536色のグラデーション
        var n = 0
        var path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            when ( index%6 ) {
                0 -> {
                    path = Path()
                    path.moveTo(myPointF.x,myPointF.y)
                }
                5 -> {
                    path.lineTo(myPointF.x,myPointF.y)
                    path.close()
                    val color = myColor.create(n%7,8)
                    linePaint.color = color.toInt()
                    canvas.drawPath(path,linePaint)
                    n++
                }
                else -> {
                    path.lineTo(myPointF.x,myPointF.y)
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
