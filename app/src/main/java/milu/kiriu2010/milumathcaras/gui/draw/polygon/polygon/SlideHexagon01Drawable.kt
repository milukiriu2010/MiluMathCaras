package milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon

import android.graphics.*
import android.os.Handler
import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.ColorType
import milu.kiriu2010.gui.color.MyColorFactory
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// --------------------------------------------
// 六角形をずらして描く
// --------------------------------------------
// http://logo.twentygototen.org/dMgxWRrj
// --------------------------------------------
class SlideHexagon01Drawable: MyDrawable() {

    // -------------------------------
    // 描画領域
    // -------------------------------
    private val side = 1000f
    private val margin = 50f

    // ---------------------------------
    // 六角形の半径
    // ---------------------------------
    private var r = side/4f

    // -------------------------------
    // 六角形の描画角度
    // -------------------------------
    private var angleMax = 360f*12f
    // １つの六角形を描画しているときの頂点数
    private var nVertex = 0
    private var nVertexMax = 6
    private var angleVertex = -60f
    // 最初の描画角度
    private var angleInit = 60f
    // 現在の描画角度
    private var angle = angleInit
    // 一辺上に描画する点(3分割)
    private var nVertexDv = 0
    private var nVertexDvMax = 3
    // 描画する六角形の数
    private var nCnt = 0
    private var nCntMax = 12
    //private var angleDv = 30f

    // -------------------------------
    // 六角形の描画点リスト
    // -------------------------------
    val pointLst = mutableListOf<MyPointF>()

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
    // 六角形を描くペイント
    // -------------------------------
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
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
    // 第１引数:描画する六角形の数
    // -----------------------------------------
    override fun calStart(isKickThread: Boolean, vararg values: Float) {
        // 描画する六角形の数
        nCntMax = 12
        values.forEachIndexed { index, fl ->
            //Log.d(javaClass.simpleName,"index[$index]fl[$fl]")
            when (index) {
                // 描画する六角形の数
                0 -> nCntMax = fl.toInt()
            }
        }

        /*
        // 初期位置まで描画点を追加する
        while ( nCnt < nCntMax )  {
            // 六角形の描画点を追加
            addPoint()
            // 六角形の描画点を移動
            //movePoint()
        }
        */
        // ビットマップに描画
        drawBitmap()
        // 描画
        invalidateSelf()

        // 描画に使うスレッド
        if ( isKickThread ) {
            runnable = Runnable {
                // 六角形の描画点を追加
                addPoint()
                // 六角形の描画点を移動
                //movePoint()
                // ビットマップに描画
                drawBitmap()
                // 描画
                invalidateSelf()

                // 最後は1秒後に描画
                if (nCnt == nCntMax) {
                    handler.postDelayed(runnable, 1000)
                }
                // 100msごとに描画
                else {
                    handler.postDelayed(runnable, 50)
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
    // 六角形の描画点を追加
    // -------------------------------
    private fun addPoint() {
        // すべての六角形を描いたらクリアする
        if ( nCnt == nCntMax ) {
            nCnt = 0
            pointLst.clear()
        }

        // 六角形を描く座標
        var x = 0f
        var y = 0f
        when ( nVertexDv ) {
            // 頂点までたどり着く
            nVertexDvMax-1 -> {
                x = r*cos(angle/PI/180f).toFloat()
                y = r*sin(angle/PI/180f).toFloat()
                nVertex++
                nVertex = nVertex%nVertexMax
                nVertexDv = 0
                // 頂点までたどりついたら描画角度を変える
                angle = angle - angleVertex
            }
            // 頂点まで描いている途中
            else -> {
                x = r*(nVertexDv+1).toFloat()/nVertexDvMax.toFloat()*cos(angle/PI/180f).toFloat()
                y = r*(nVertexDv+1).toFloat()/nVertexDvMax.toFloat()*sin(angle/PI/180f).toFloat()
                nVertexDv++
            }
        }
        // 描画点をリストに加える
        pointLst.add(MyPointF(x,y))

        // １つの六角形を描き終わったら、描画角度を変える
        if ( ( nVertexDv == 0 ) and ( nVertex == 0 ) ) {
            angle = angle-angleVertex/2f
            nCnt++
        }

        Log.d(javaClass.simpleName,"======================================")
        Log.d(javaClass.simpleName,"nCnt[$nCnt]nCntMax[$nCntMax]nVertexMax[$nVertexMax]")
        Log.d(javaClass.simpleName,"nVertexDv[$nVertexDv]nVertex[$nVertex]angle[$angle]")
    }

    /*
    // -------------------------------
    // 円の描画点を移動
    // -------------------------------
    private fun movePoint() {
        // 10度ずつ移動する
        angleA = angleA + angleDv
        //Log.d(javaClass.simpleName,"angleA[{$angleA}]")

        // １周するたびに円の中心を移動する
        if ( angleA > 0f && angleA.toInt()%360 == 0 ) {
            angleB += angleDv
        }

        // 最大角度を超えたら
        // ・元の位置に戻す
        // ・描画点リストをクリアする
        if ( angleA > angleMax ) {
            angleA = 0f
            angleB = 0f
            pointLst.clear()
        }
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

        /*
        // 円を描く
        val path = Path()
        pointLst.forEachIndexed { index, myPointF ->
            if ( index == 0 ) {
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
        // 円を描く
        val path = Path()
        val bunchSize = nVertexDvMax*nVertexMax
        var myPointF2: MyPointF? = null
        pointLst.forEachIndexed { index, myPointF1 ->
            val color = myColor.create(index%bunchSize,bunchSize)
            linePaint.color = color.toInt()
            if ( myPointF2 != null ) {
                canvas.drawLine(myPointF1.x,myPointF1.y,myPointF2?.x!!,myPointF2?.y!!,linePaint)
            }
            myPointF2 = myPointF1
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