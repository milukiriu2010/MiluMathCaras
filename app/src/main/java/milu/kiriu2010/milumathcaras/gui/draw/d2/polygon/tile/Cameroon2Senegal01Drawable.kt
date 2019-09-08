package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.tile

import android.graphics.*
import android.os.Handler
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// ---------------------------------------------
// カメルーンの国旗⇔セネガルの国旗01
// ---------------------------------------------
// 2019.08.30
// ---------------------------------------------
class Cameroon2Senegal01Drawable: MyDrawable() {

    enum class Mode {
        PH1,
        PH2
    }

    // 現在のモード
    private var modeNow = Mode.PH1

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1500f
    private val margin = 0f

    // 領域分割
    private val splitW  = 5f
    private val splitWN = splitW.toInt()
    private val splitH  = 7.5f
    private val splitHN = (splitH.toInt()+1)

    // ---------------------------------
    // 国旗
    // ---------------------------------
    //private val flagW = side/splitW
    private val flagW = 300f
    private val flagWX = flagW*0.5f
    private val flagW1 = 300f
    private val flagW2 = flagW1*0.5f
    private val flagW3 = flagW1/3f
    private val flagW6 = flagW1/6f
    private val flagW10 = flagW1*0.1f
    private val flagW20 = flagW1*0.05f
    //private val flagH = side/splitH
    private val flagH = 200f
    private val flagHX = flagH*0.5f
    private val flagH1 = 200f
    private val flagH2 = flagH1*0.5f
    private val flagH4 = flagH1*0.25f

    // 境界
    private val bw = 36f

    // 国旗の移動比率
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private val ratioDv = 0.1f

    // 四角形を描くときの順番
    private val ids0 = intArrayOf(0,1,2,3)
    private val ids1 = intArrayOf(1,2,3,0)

    // パスの初期化を実施したかどうか
    private var isInitialized = false
    // ratioNow=0にステイするかどうか
    private var isStay = true
    // モードがPH1になった回数
    private var cnt = 0

    // カメルーン国旗の四角形リスト
    private val square0Lst = mutableListOf<Square>()

    // セネガル国旗の四角形リスト
    private val square1Lst = mutableListOf<Square>()

    // ☆リスト(カメルーン・セネガルの順で格納)
    private val starLst = mutableListOf<Star>()

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

    // ---------------------------------
    // ペイント赤
    // ---------------------------------
    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xffcc0000.toInt()
        style = Paint.Style.FILL
    }

    // -------------------------------
    // ペイント黄色
    // -------------------------------
    private val yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // ペイント緑
    // ---------------------------------
    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff008800.toInt()
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // ペイント☆
    // ---------------------------------
    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
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

    // --------------------------------------
    // CalculationCallback
    // 描画に使うデータを計算する
    // --------------------------------------
    // 可変変数 values の引数位置による意味合い
    //
    // 第１引数:
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 初期化
        myinit()
        // パスの初期化
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            // 初期化
            myinit()
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // パスの初期化
                    createPath()
                    // 移動する
                    movePath()
                    // ビットマップに描画
                    drawBitmap()
                    // 描画
                    invalidateSelf()

                    //if (ratioNow >= ratioMax) {
                    if ((ratioNow <= ratioMin) and (isStay == false)) {
                        handler.postDelayed(runnable, 300)
                    }
                    else {
                        handler.postDelayed(runnable, 100)
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
    // 初期化
    // -------------------------------
    private fun myinit() {
        // パスの初期化を実施したかどうか
        isInitialized = false
        // ratioNow=0にステイするかどうか
        isStay = true
        // モードがPH1になった回数
        cnt = 0

    }

    // -------------------------------
    // パスの初期化
    // -------------------------------
    private fun createPath() {
        if ( (ratioNow > ratioMin) and (ratioNow < ratioMax) ) return
        if ( (ratioNow == 0f) and (isStay == false) ) return

        ratioNow = ratioMin

        // モードを設定
        if (isInitialized) {
            modeNow = when (modeNow) {
                Mode.PH1 -> Mode.PH2
                Mode.PH2 -> {
                    cnt++
                    Mode.PH1
                }
            }
        }

        // パス生成
        when (modeNow) {
            Mode.PH1 -> createPathPH1()
            Mode.PH2 -> createPathPH2()
        }


        // パスの初期化を実施したかどうか
        isInitialized = true
        // ratioNow=0にステイするかどうか
        isStay = true
    }

    // パス生成(PH1)
    private fun createPathPH1() {
        square0Lst.clear()
        square1Lst.clear()

        // 中
        val square0 = Square(angle = -90f).also { square ->
            square.ps.add(MyPointF( flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6, flagH2))
            square.ps.add(MyPointF( flagW6, flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = yellowPaint
        })

        // 右
        val square1 = Square(angle = -90f).also { square ->
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(flagW6, flagH2))
            square.ps.add(MyPointF(flagW2, flagH2))
            square.paint = yellowPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // ☆を生成
        createPathStarPH1()
    }

    // ☆を生成(PH1)
    private fun createPathStarPH1() {
        starLst.clear()

        val ang0 = floatArrayOf(18f,90f,162f,234f,306f)
        val ang1 = floatArrayOf(54f,126f,198f,270f,342f)

        val star0 = Star(angle = -90f).also { star ->
            (0..4).forEach { id ->
                star.ps.add(MyPointF().also {
                    it.x = flagW20 * MyMathUtil.cosf(ang0[id])
                    it.y = flagW20 * MyMathUtil.sinf(ang0[id])
                })
                star.ps.add(MyPointF().also {
                    it.x = flagW10 * MyMathUtil.cosf(ang1[id])
                    it.y = flagW10 * MyMathUtil.sinf(ang1[id])
                })
            }
            star.paint = yellowPaint
        }
        starLst.add(star0)
        starLst.add(star0.copy().also{
            it.paint = greenPaint
        })

    }

    // パス生成(PH2)
    private fun createPathPH2() {
        square0Lst.clear()
        square1Lst.clear()

        // 中ー横倒れ
        val square0 = Square(angle = -90f).also { square ->
            square.ps.add(MyPointF(flagW2+flagW3,-flagH2))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(flagW6, 0f))
            square.ps.add(MyPointF( flagW2+flagW3, 0f))
            square.paint = redPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = yellowPaint
        })

        // 右ー横倒れ
        val square1 = Square(angle = -90f).also { square ->
            square.ps.add(MyPointF(flagW6+flagW,-flagH2))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW2, 0f))
            square.ps.add(MyPointF(flagW6+flagW, 0f))
            square.paint = yellowPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // ☆を生成(PH2)
        createPathStarPH2()
    }

    // ☆を生成(PH2)
    private fun createPathStarPH2() {
        starLst.clear()

        val ang0 = floatArrayOf(18f,90f,162f,234f,306f)
        val ang1 = floatArrayOf(54f,126f,198f,270f,342f)

        val star0 = Star(angle = -90f).also { star ->
            (0..4).forEach { id ->
                star.ps.add(MyPointF().also {
                    it.x = flagW20 * MyMathUtil.cosf(ang0[id]) + flagW2
                    it.y = flagW20 * MyMathUtil.sinf(ang0[id]) - flagH4
                })
                star.ps.add(MyPointF().also {
                    it.x = flagW10 * MyMathUtil.cosf(ang1[id]) + flagW2
                    it.y = flagW10 * MyMathUtil.sinf(ang1[id]) - flagH4
                })
            }
            star.paint = yellowPaint
        }
        starLst.add(star0)
        starLst.add(star0.copy().also{
            it.paint = greenPaint
        })
    }

    // -------------------------------
    // 国旗を移動する
    // -------------------------------
    private fun movePath() {
        if ( isStay == false ) {
            ratioNow += ratioDv
        }
        else {
            // ratioNow=0にステイするかどうか
            isStay = false
        }
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val canvas = Canvas(tmpBitmap)

        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),greenPaint)

        (0..splitHN).forEach { h ->
            val hh = h.toFloat()
            canvas.saveLayer(-flagW,hh*flagH,side,(hh+1f)*flagH,null)
            //canvas.save()
            canvas.translate(-2f*flagW-flagW2,hh*flagH+flagH2)
            (-1..splitWN+1).forEach { w ->
                //val ww = w.toFloat()
                canvas.translate(flagW,0f)

                val hw1 = (h+w)%2
                var base: Int = 0
                var sign: Float = -1f
                when (h%2) {
                    0 -> {
                        base = 0
                        sign = 1f
                    }
                    1 -> {
                        base = 1
                        sign = -1f
                    }
                }

                when (cnt%2) {
                    0 -> {
                        when (hw1) {
                            0 -> drawCameroon(canvas,base,sign)
                            1 -> drawSenegal(canvas,base,sign)
                        }
                    }
                    1 -> {
                        when (hw1) {
                            0 -> drawSenegal(canvas,base,sign)
                            1 -> drawCameroon(canvas,base,sign)
                        }
                    }
                }
            }

            canvas.restore()
        }

        // 枠を描画
        canvas.drawRect(RectF(0f,0f,intrinsicWidth.toFloat(),intrinsicHeight.toFloat()),framePaint)

        // これまでの描画はテンポラリ領域に実施していたので、実際の表示に使うビットマップにコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,intrinsicWidth,intrinsicHeight,matrix,true)
    }

    // カメルーン国旗を描画
    private fun drawCameroon(canvas: Canvas,base: Int,sign: Float) {
        val ids = when (base) {
            0 -> ids0
            1 -> ids1
            else -> ids0
        }

        // 長方形描画
        (0..1).forEach { i ->
            val square0 = square0Lst[i]
            val path0 = Path()
            var p0 = square0.ps[base]
            ids.forEachIndexed { id0 , id ->
                val p = square0.ps[id]
                if (id0 == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(ratioNow*square0.angle*sign,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }

        // ☆描画
        var p0 = square0Lst[0].ps[base]
        val star = starLst[0]
        val path = Path()
        star.ps.forEachIndexed { id, p ->
            val p1 = p.copy()
            p1.rotate(ratioNow*star.angle*sign,p0)
            if (id == 0) {
                //path.moveTo(p.x,p.y)
                path.moveTo(p1.x,p1.y)
            }
            else {
                //path.lineTo(p.x,p.y)
                path.lineTo(p1.x,p1.y)
            }
        }
        path.close()
        canvas.drawPath(path,star.paint)
    }

    // セネガル国旗を描画
    private fun drawSenegal(canvas: Canvas,base: Int,sign: Float) {
        val ids = when (base) {
            0 -> ids0
            1 -> ids1
            else -> ids0
        }

        // 長方形描画
        (0..1).forEach { i ->
            val square0 = square1Lst[i]
            val path0 = Path()
            var p0 = square0.ps[base]
            ids.forEachIndexed { id0 , id ->
                val p = square0.ps[id]
                if (id0 == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(ratioNow*square0.angle*sign,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }

        // ☆描画
        var p0 = square1Lst[0].ps[base]
        val star = starLst[1]
        val path = Path()
        star.ps.forEachIndexed { id, p ->
            val p1 = p.copy()
            p1.rotate(ratioNow*star.angle*sign,p0)
            if (id == 0) {
                path.moveTo(p1.x,p1.y)
            }
            else {
                path.lineTo(p1.x,p1.y)
            }
        }
        path.close()
        canvas.drawPath(path,star.paint)
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

    private data class Square(
        // 頂点リスト
        val ps: MutableList<MyPointF> = mutableListOf(),
        // 回転角度
        var angle: Float = 90f,
        // ペイント
        var paint: Paint = Paint()
    )

    private data class Star(
        // 頂点リスト
        val ps: MutableList<MyPointF> = mutableListOf(),
        // 回転角度
        var angle: Float = 90f,
        // ペイント
        var paint: Paint = Paint()
    )
}
