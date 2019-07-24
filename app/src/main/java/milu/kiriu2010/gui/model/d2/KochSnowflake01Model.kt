package milu.kiriu2010.gui.model.d2

import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.basic.MyTurtle
import milu.kiriu2010.gui.model.MgModelAbs
import kotlin.math.pow
import kotlin.math.sqrt

// ----------------------------------------------
// コッホ雪片
// ----------------------------------------------
// 2019.06.14
// 2019.07.02  パッケージ修正
// ----------------------------------------------
class KochSnowflake01Model: MgModelAbs() {

    private val myTurtle = MyTurtle()

    private val sqrt3_1 = sqrt(3f)/3f

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        myTurtle.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            1 -> createPathPattern1(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 面
    private fun createPathPattern1( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f
        // 再帰レベル
        val nNow = opt["nNow"] ?: 4f

        // 移動距離
        val dv = scale * 2f/3f.pow(nNow)
        Log.d(javaClass.simpleName,"dv[$dv]")

        // コッホ雪片の初期位置(左下)
        val a = MyPointF().apply {
            x = -scale
            y = -scale*sqrt3_1
        }

        // ---------------------------------
        // コッホ雪片を描く亀に以下を設定
        // ・初期位置
        // ・移動距離
        // ・初期角度
        // ---------------------------------
        myTurtle.addPoint(a).apply {
            d = dv
            t = 0f
        }

        // コッホ雪片をパターンAで描画
        createCurveA(nNow.toInt())
        // タートル右120度
        myTurtle.turn(120f)
        // コッホ雪片をパターンAで描画
        createCurveA(nNow.toInt())
        // タートル右120度
        myTurtle.turn(120f)
        // コッホ雪片をパターンAで描画
        createCurveA(nNow.toInt())

        myTurtle.pLst.forEachIndexed { id, myPointF ->
            // 位置
            datPos.addAll(arrayListOf(myPointF.x,myPointF.y,0f))
            // 法線
            datNor.addAll(arrayListOf(0f,0f,1f))
            // 色
            if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
                datCol.addAll(arrayListOf(1f,0f,0f,1f))
            }
            else {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            // インデックス
            datIdx.add(id.toShort())
        }
    }

    // -------------------------------------
    // コッホ雪片をパターンAで描画
    // -------------------------------------
    private fun createCurveA( n: Int ) {
        if ( n > 0 ) {
            // パターンA
            createCurveA(n-1)
            // 左60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1)
            // 右120度
            myTurtle.turn(120f)
            // パターンA
            createCurveA(n-1)
            // 左60度
            myTurtle.turn(-60f)
            // パターンA
            createCurveA(n-1)
        }
        else if ( n == 1 ){
            // --------------------------
            // 亀の軌跡
            // --------------------------
            // ・移動
            // ・左60度
            // ・移動
            // ・右120度
            // ・移動
            // ・左60度
            // ・移動
            // --------------------------
            myTurtle.move()
                .turn(-60f)
                .move()
                .turn(120f)
                .move()
                .turn(-60f)
                .move()
        }
        else if ( n == 0 ) {
            myTurtle.move()
        }
    }
}
