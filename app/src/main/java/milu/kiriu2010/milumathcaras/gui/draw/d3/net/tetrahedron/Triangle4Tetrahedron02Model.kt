package milu.kiriu2010.milumathcaras.gui.draw.d3.net.tetrahedron

import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------------
// 正四面体展開図用の三角形
// --------------------------------------
// 2019.05.21  初回
// --------------------------------------
class Triangle4Tetrahedron02Model: MgModelAbs() {

    val sqrt3   = 1.73205f

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 正四面体展開図(中央)
            1 -> createPathPattern1()
            // 正四面体展開図(下)
            2 -> createPathPattern2()
            // 正四面体展開図(右上)
            3 -> createPathPattern3()
            // 正四面体展開図(左上)
            4 -> createPathPattern4()
            else -> createPathPattern1()
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 正四面体展開図(0-1-2)
    private fun createPathPattern1() {
        // 頂点データ
        datPos.addAll(arrayListOf( 0f,sqrt3,0f))
        datPos.addAll(arrayListOf(-1f,   0f,0f))
        datPos.addAll(arrayListOf( 1f,   0f,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(1f,0f,0f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }

    // 正四面体展開図(下)
    private fun createPathPattern2() {
        // 頂点データ
        datPos.addAll(arrayListOf(-1f,    0f,0f))
        datPos.addAll(arrayListOf( 0f,-sqrt3,0f))
        datPos.addAll(arrayListOf( 1f,    0f,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(0f,1f,0f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }

    // 正四面体展開図(右上)
    private fun createPathPattern3() {
        // 頂点データ
        datPos.addAll(arrayListOf( 1f,    0f,0f))
        datPos.addAll(arrayListOf( 2f,sqrt3,0f))
        datPos.addAll(arrayListOf( 0f,sqrt3,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(0f,0f,1f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }

    // 正四面体展開図(左上)
    private fun createPathPattern4() {
        // 頂点データ
        datPos.addAll(arrayListOf( 0f,sqrt3,0f))
        datPos.addAll(arrayListOf(-2f,sqrt3,0f))
        datPos.addAll(arrayListOf(-1f,   0f,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(1f,1f,0f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }
}
