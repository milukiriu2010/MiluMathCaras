package milu.kiriu2010.milumathcaras.gui.draw.d3.net.dodecahedron

import milu.kiriu2010.gui.model.MgModelAbs
import kotlin.math.sqrt

// --------------------------------------
// 正十二面体展開図用の五角形
// --------------------------------------
// 一辺の長さ=2
// --------------------------------------
// 2019.05.27  初回
// --------------------------------------
class Pentagon4Dodecahedron01Model: MgModelAbs() {

    val hH = sqrt(5f+2f*sqrt(5f))
    val hh = sqrt(10f+2f*sqrt(5f))/2f
    val ww = (sqrt(5f)+1f)/2f


    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 正十二面体展開図(上向き)
            1 -> createPathPattern1(opt)
            // 正十二面体展開図(下向き)
            2 -> createPathPattern2(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 正十二面体展開図(上向き)
    private fun createPathPattern1(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 頂点データ
        datPos.addAll(arrayListOf( 0f,hH,0f))   // A:上
        datPos.addAll(arrayListOf(-ww,hh,0f))   // B:左上
        datPos.addAll(arrayListOf(-1f,0f,0f))   // C:左下
        datPos.addAll(arrayListOf( 1f,0f,0f))   // D:左上
        datPos.addAll(arrayListOf( ww,hh,0f))   // E:右上
        /*
        datPos.addAll(arrayListOf( 0f,hH,0f))   // A:上
        datPos.addAll(arrayListOf(-ww,hh,0f))   // B:左中
        datPos.addAll(arrayListOf(-1f,0f,0f))   // C:左下
        datPos.addAll(arrayListOf( 0f,hH,0f))   // A:上
        datPos.addAll(arrayListOf(-1f,0f,0f))   // C:左下
        datPos.addAll(arrayListOf( 1f,0f,0f))   // D:左上
        datPos.addAll(arrayListOf( 0f,hH,0f))   // A:上
        datPos.addAll(arrayListOf( 1f,0f,0f))   // D:左上
        datPos.addAll(arrayListOf( ww,hh,0f))   // E:右中
        */

        // 法線データ
        (0..4).forEach {
            datNor.addAll(arrayListOf(0f,0f,1f))
        }

        // 色データ
        (0..4).forEach {
            datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
        }

        // 頂点インデックス
        // ABC
        datIdx.addAll(arrayListOf(0,1,2))
        // ACD
        datIdx.addAll(arrayListOf(0,2,3))
        // ADE
        datIdx.addAll(arrayListOf(0,3,4))
    }

    // 正十二面体展開図(下向き)
    private fun createPathPattern2(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 頂点データ
        datPos.addAll(arrayListOf( 0f,-hH,0f))   // A:下
        datPos.addAll(arrayListOf( ww,-hh,0f))   // B:右中
        datPos.addAll(arrayListOf( 1f,0f,0f))    // C:右上
        datPos.addAll(arrayListOf(-1f,0f,0f))    // D:左上
        datPos.addAll(arrayListOf(-ww,-hh,0f))   // E:左中

        // 法線データ
        (0..4).forEach {
            datNor.addAll(arrayListOf(0f,0f,1f))
        }

        // 色データ
        (0..4).forEach {
            datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
        }

        // 頂点インデックス
        // ABC
        datIdx.addAll(arrayListOf(0,1,2))
        // ACD
        datIdx.addAll(arrayListOf(0,2,3))
        // ADE
        datIdx.addAll(arrayListOf(0,3,4))
    }
}
