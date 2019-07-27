package milu.kiriu2010.milumathcaras.gui.draw.d3.cube.hexagon

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// ----------------------------------------------
// 台形01
// ----------------------------------------------
// 2019.07.27
// ----------------------------------------------
class Trapezoid01Model: MgModelAbs() {

    private val a = 2f

    // 頂点リスト
    private lateinit var va: ArrayList<Float>
    private lateinit var vb: ArrayList<Float>
    private lateinit var vc: ArrayList<Float>
    private lateinit var vd: ArrayList<Float>
    // 色
    private lateinit var color: ArrayList<Float>

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 黒:右
            1 -> createPathPattern1()
            // 黒:右下
            2 -> createPathPattern2()
            else -> createPathPattern1()
        }


        // 頂点データ
        datPos.addAll(ArrayList(va))   // v0
        datPos.addAll(ArrayList(vd))   // v1
        datPos.addAll(ArrayList(vb))   // v2
        datPos.addAll(ArrayList(vc))   // v3
        datPos.addAll(ArrayList(vb))   // v4
        datPos.addAll(ArrayList(vd))   // v5

        // 法線データ
        (0..1).forEach {
            // (v1-v0) x (v2-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*(3*it+1), 3*(3*it+2), 3*(3*it) ) )
        }

        // 色データ
        (0..5).forEach {
            datCol.addAll(color)
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf(0,1,2,3,4,5))

        // バッファ割り当て
        allocateBuffer()
    }

    // 黒:右
    private fun createPathPattern1() {
        va = arrayListOf( a,  a,-a)
        vb = arrayListOf( a, -a,-a)
        vc = arrayListOf(1f,-1f,-1f)
        vd = arrayListOf(1f, 1f,-1f)

        color = arrayListOf(0f,0f,0f,1f)
    }

    // 黒:右下
    private fun createPathPattern2() {
        va = arrayListOf( a, -a,-a)
        vb = arrayListOf( a, -a, a)
        vc = arrayListOf(1f,-1f, 1f)
        vd = arrayListOf(1f,-1f,-1f)

        color = arrayListOf(0f,0f,0f,1f)
    }

    // 点
    private fun createPathPattern10( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val va = arrayListOf(-scale,-scale, scale)
        val vb = arrayListOf( scale,-scale, scale)
        val vc = arrayListOf(-scale, scale, scale)
        val vd = arrayListOf( scale, scale, scale)
        val ve = arrayListOf( scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf( scale, scale,-scale)
        val vh = arrayListOf(-scale, scale,-scale)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vh))

        // 色データ
        if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
            (0..7).forEach { _ ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }
        else {
            (0..7).forEach { i ->
                // 立方体は８つ頂点があるので８で割る
                var tc = MgColor.hsva(360/8*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
        }
    }


    // 線
    private fun createPathPattern20( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val va = arrayListOf(-scale,-scale, scale)
        val vb = arrayListOf( scale,-scale, scale)
        val vc = arrayListOf(-scale, scale, scale)
        val vd = arrayListOf( scale, scale, scale)
        val ve = arrayListOf( scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf( scale, scale,-scale)
        val vh = arrayListOf(-scale, scale,-scale)

        // 頂点データ
        // l0
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        // l1
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vd))
        // l2
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vc))
        // l3
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(va))
        // l4
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vf))
        // l5
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(ve))
        // l6
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vh))
        // l7
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vg))
        // l8
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))
        // l9
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vh))
        // l10
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vg))
        // l11
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(ve))

        // 色データ
        if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
            (0..23).forEach { _ ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }
        else {
            (0..23).forEach { i ->
                // ２頂点で１つの線を構成するため２で割る
                val ii = i/2
                // 立方体は１２線があるので１２で割る
                var tc = MgColor.hsva(360/8*ii,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
        }
    }

}
