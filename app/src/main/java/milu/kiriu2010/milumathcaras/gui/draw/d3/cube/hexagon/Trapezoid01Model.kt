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
            // 濃い緑:左下
            3 -> createPathPattern3()
            // 濃い緑:左
            4 -> createPathPattern4()
            // ピンク:左上
            5 -> createPathPattern5()
            // ピンク:右上
            6 -> createPathPattern6()
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

    // 濃い緑:左下
    private fun createPathPattern3() {
        va = arrayListOf( a, -a, a)
        vb = arrayListOf(-a, -a, a)
        vc = arrayListOf(-1f,-1f, 1f)
        vd = arrayListOf( 1f,-1f, 1f)

        color = arrayListOf(0f,0.392157f,0f,1f)
    }

    // 濃い緑:左
    private fun createPathPattern4() {
        va = arrayListOf(-a, -a, a)
        vb = arrayListOf(-a,  a, a)
        vc = arrayListOf(-1f, 1f, 1f)
        vd = arrayListOf(-1f,-1f, 1f)

        color = arrayListOf(0f,0.392157f,0f,1f)
    }

    // ピンク:左上
    private fun createPathPattern5() {
        va = arrayListOf(-a,  a, a)
        vb = arrayListOf(-a,  a,-a)
        vc = arrayListOf(-1f, 1f,-1f)
        vd = arrayListOf(-1f, 1f, 1f)

        color = arrayListOf(1f,0.752941f,0.796078f,1f)
    }

    // ピンク:右上
    private fun createPathPattern6() {
        va = arrayListOf(-a,  a,-a)
        vb = arrayListOf( a,  a,-a)
        vc = arrayListOf( 1f, 1f,-1f)
        vd = arrayListOf(-1f, 1f,-1f)

        color = arrayListOf(1f,0.752941f,0.796078f,1f)
    }
}
