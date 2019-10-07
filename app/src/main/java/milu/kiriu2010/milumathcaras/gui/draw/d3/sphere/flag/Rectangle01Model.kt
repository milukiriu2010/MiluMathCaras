package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.flag

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// ----------------------------------------------
// 長方形01
// ----------------------------------------------
// 縦：横=１：２
// (1) 日本
// (2) バングラディッシュ
// (3) 上半分(白)
// (4) 上半分(赤)
// ----------------------------------------------
// 2019.09.15
// ----------------------------------------------
class Rectangle01Model: MgModelAbs() {

    private val a = 1f

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
            // 日本
            1 -> createPathPattern1()
            // バングラディッシュ
            2 -> createPathPattern2()
            // 上半分(白)
            3 -> createPathPattern3()
            // 上半分(赤)
            4 -> createPathPattern4()
            else -> createPathPattern1()
        }

        // 頂点データ
        datPos.addAll(ArrayList(vb))
        datPos.addAll(ArrayList(vc))
        datPos.addAll(ArrayList(va))

        datPos.addAll(ArrayList(vd))
        datPos.addAll(ArrayList(va))
        datPos.addAll(ArrayList(vc))

        // 色データ
        (0..5).forEach {
            datCol.addAll(color)
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf(0,1,2,3,4,5))

        // バッファ割り当て
        allocateBuffer()
    }

    // 日本
    private fun createPathPattern1() {
        va = arrayListOf(-a, a*0.5f,0f)
        vb = arrayListOf(-a,-a*0.5f,0f)
        vc = arrayListOf( a,-a*0.5f,0f)
        vd = arrayListOf( a, a*0.5f,0f)

        color = arrayListOf(1f,1f,1f,1f)
    }

    // バングラディッシュ
    private fun createPathPattern2() {
        va = arrayListOf(-a, a*0.5f,0f)
        vb = arrayListOf(-a,-a*0.5f,0f)
        vc = arrayListOf( a,-a*0.5f,0f)
        vd = arrayListOf( a, a*0.5f,0f)

        color = arrayListOf(0f,0.6f,0f,1f)
    }

    // 上半分(白)
    private fun createPathPattern3() {
        va = arrayListOf(-a, a*0.5f,0f)
        vb = arrayListOf(-a,     0f,0f)
        vc = arrayListOf( a,     0f,0f)
        vd = arrayListOf( a, a*0.5f,0f)

        color = arrayListOf(1f,1f,1f,1f)
    }

    // 上半分(赤)
    private fun createPathPattern4() {
        va = arrayListOf(-a, a*0.5f,0f)
        vb = arrayListOf(-a,     0f,0f)
        vc = arrayListOf( a,     0f,0f)
        vd = arrayListOf( a, a*0.5f,0f)

        color = arrayListOf(1f,0f,0f,1f)
    }
}
