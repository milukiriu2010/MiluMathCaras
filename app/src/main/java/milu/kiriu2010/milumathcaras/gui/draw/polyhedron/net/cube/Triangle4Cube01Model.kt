package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.cube

import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------------
// 立方体展開図用の三角形
// --------------------------------------
// 2019.05.26  初回
// --------------------------------------
class Triangle4Cube01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 正四面体展開図
            1 -> createPathPattern1(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 正四面体展開図
    private fun createPathPattern1(opt: Map<String, Float>) {
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 頂点データ
        datPos.addAll(arrayListOf(-1f, 1f,0f))
        datPos.addAll(arrayListOf(-1f,-1f,0f))
        datPos.addAll(arrayListOf( 1f,-1f,0f))
        datPos.addAll(arrayListOf( 1f,-1f,0f))
        datPos.addAll(arrayListOf( 1f, 1f,0f))
        datPos.addAll(arrayListOf(-1f, 1f,0f))

        // 法線データ
        (0..5).forEach {
            datNor.addAll(arrayListOf(0f,0f,1f))
        }

        // 色データ
        (0..5).forEach {
            datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2,3,4,5))
    }
}
