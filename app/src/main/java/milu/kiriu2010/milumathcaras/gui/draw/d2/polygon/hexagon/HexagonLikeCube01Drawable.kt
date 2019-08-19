package milu.kiriu2010.milumathcaras.gui.draw.d2.polygon.hexagon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

// -----------------------------------------------------------------
// 立方体のような正六角形01
// -----------------------------------------------------------------
// ピンク(上:Y軸)・緑(左:Z軸)・黒(右:X軸)の領域に分かれ４回回転
// -----------------------------------------------------------------
// https://twitter.com/InfinityLoopGIF/status/1149451600248868864
// -----------------------------------------------------------------
// 2019.07.25
// -----------------------------------------------------------------
class HexagonLikeCube01Drawable: MyDrawable() {

    // 描画モード
    enum class ModePtr {
        // Y軸(右⇒左)
        YRL
    }

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 960f
    private val margin = 0f

    // -------------------------------
    // 基準となる長さ
    // -------------------------------
    private val a1 = 50f
    private val a2 = 2f*a1
    private val a4 = 4f*a1
    private val a8 = 8f*a1

    // 描画する平行四辺形リスト(４つ)
    private val parallels = mutableListOf<Parallelogram>()

    // 平行四辺形の移動比率
    private var ratioPNow = 0f
    private val ratioPDv = 0.1f

    // 描画モード
    private var modeNow = ModePtr.YRL

    // "描画点の初期位置設定"回数
    private var nCnt = 0


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
    // 頂点を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
    // --------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 描画点の初期位置設定
        createPath()
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // "更新"状態
                if ( isPaused == false ) {
                    // 描画点の初期位置設定
                    createPath()
                    // 描画オブジェクトを移動する
                    movePath()
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
    // 描画点の初期位置設定
    // -------------------------------
    private fun createPath() {
        if ( (ratioPNow > 0f) and (ratioPNow < 1f) ) return
        ratioPNow = 0f
        parallels.clear()

        // モード変更
        if ( nCnt > 1 ) {
            modeNow = when (modeNow) {
                ModePtr.YRL -> ModePtr.YRL
            }
        }

        // 描画パス初期化
        when (modeNow) {
            ModePtr.YRL -> {
                createParallelYRL()
            }
        }

        nCnt++
    }

    // -------------------------------
    // 描画パス初期化(平行四辺形)
    // 共通
    // -------------------------------
    private fun createParalle(prl: Parallelogram, agls:FloatArray, agle: FloatArray) {
        (0..3).forEach { i ->
            val ps = MyPointF().also {
                it.x = agls[2*i+1] * MyMathUtil.cosf(agls[2*i])
                it.y = agls[2*i+1] * MyMathUtil.sinf(agls[2*i])
            }
            //Log.d(javaClass.simpleName,"ps[$i][${ps.x}][${ps.y}]")
            prl.slst.add(ps)

            val pe = MyPointF().also {
                it.x = agle[2*i+1] * MyMathUtil.cosf(agle[2*i])
                it.y = agle[2*i+1] * MyMathUtil.sinf(agle[2*i])
            }
            prl.elst.add(pe)
        }
    }

    // -------------------------------
    // 描画パス初期化(平行四辺形)
    // Y軸(右⇒左)
    // -------------------------------
    private fun createParallelYRL() {
        // 緑:裏右⇒表右
        // 角度,長さのペア
        val agl0s = floatArrayOf( 0f,0f,30f,a4,330f,a4,270f,a4)
        val agl0e = floatArrayOf(30f,a4,90f,a4,  0f,0f,330f,a4)
        Parallelogram().also { prl ->
            createParalle(prl,agl0s,agl0e)
            prl.color = Color.GREEN
            parallels.add(prl)
        }

        // 緑:表左⇒裏左
        // 角度,長さのペア
        val agl1s = floatArrayOf( 90f,a4,150f,a4,210f,a4,  0f,0f)
        val agl1e = floatArrayOf(150f,a4,  0f,0f,270f,a4,210f,a4)
        Parallelogram().also { prl ->
            createParalle(prl,agl1s,agl1e)
            prl.color = Color.GREEN
            parallels.add(prl)
        }

        // ピンク:表右⇒表左
        // 角度,長さのペア
        val agl2s = floatArrayOf(30f,a4, 90f,a4,  0f,0f,330f,a4)
        val agl2e = floatArrayOf(90f,a4,150f,a4,210f,a4,  0f,0f)
        Parallelogram().also { prl ->
            createParalle(prl,agl2s,agl2e)
            prl.color = 0xffffc0cb.toInt()
            parallels.add(prl)
        }

        // 黒:表上⇒表右
        // 角度,長さのペア
        val agl3s = floatArrayOf(330f,a4,  0f,0f,210f,a4,270f,a4)
        val agl3e = floatArrayOf(  0f,0f,210f,a4,270f,a4,330f,a4)
        Parallelogram().also { prl ->
            createParalle(prl,agl3s,agl3e)
            prl.color = Color.BLACK
            parallels.add(prl)
        }



    }

    // -------------------------------
    // 描画オブジェクトを移動する
    // -------------------------------
    private fun movePath() {
        ratioPNow += ratioPDv
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

        // 座標を保存
        canvas.save()
        // 原点を移動
        canvas.translate(intrinsicWidth.toFloat()*0.5f,intrinsicHeight.toFloat()*0.5f)

        // 平行四辺形を描画
        //Log.d(javaClass.simpleName,"paralles[${parallels.size}]")
        //Log.d(javaClass.simpleName,"=============================")
        parallels.forEachIndexed { _, prl ->
            val path = Path()
            (0..3).forEach { i ->
                val ps = prl.slst[i]
                val pe = prl.elst[i]
                val p = ps.lerp(pe,ratioPNow,1f-ratioPNow)
                //Log.d(javaClass.simpleName,"p[$i][${p.x}][${p.y}]")
                if (i == 0) {
                    path.moveTo(p.x,p.y)
                }
                else {
                    path.lineTo(p.x,p.y)
                }
            }
            path.close()
            linePaint.color = prl.color
            canvas.drawPath(path,linePaint)
        }

        // 座標を元に戻す
        canvas.restore()

        // これまでの描画はテンポラリなので、実体にコピーする
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

    // 平行四辺形
    private data class Parallelogram(
        // 開始点
        val slst: MutableList<MyPointF> = mutableListOf(),
        // 終了点
        val elst: MutableList<MyPointF> = mutableListOf(),
        // 色
        var color: Int = Color.BLACK
    )
}
