package milu.kiriu2010.milumathcaras.gui.draw.d3.cube.hexagon

import android.util.Log
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// ----------------------------------------------
// 立方体(１面だけ６角形を描く)
// ----------------------------------------------
// 2019.09.14
// ----------------------------------------------
class CubeS1Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            1 -> createPathPattern1(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    private fun createPathPattern1( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val scale = 2f

        val va = arrayListOf(-scale,-scale, scale)
        val vb = arrayListOf( scale,-scale, scale)
        val vc = arrayListOf(-scale, scale, scale)
        val vd = arrayListOf( scale, scale, scale)
        val ve = arrayListOf( scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf( scale, scale,-scale)
        val vh = arrayListOf(-scale, scale,-scale)

        val vi = arrayListOf( scale,    0f, scale)
        val vj = arrayListOf( scale,-scale,    0f)
        val vk = arrayListOf( scale,    0f,-scale)
        val vl = arrayListOf( scale, scale,    0f)

        // 頂点データ
        datPos.addAll(ArrayList(va))   // v0
        datPos.addAll(ArrayList(vb))   // v1
        datPos.addAll(ArrayList(vc))   // v2
        datPos.addAll(ArrayList(vd))   // v3
        datPos.addAll(ArrayList(ve))   // v8,v5
        datPos.addAll(ArrayList(vf))   // v9
        datPos.addAll(ArrayList(vg))   // v10,v7
        datPos.addAll(ArrayList(vh))   // v11
        datPos.addAll(ArrayList(vf))   // v12,v9
        datPos.addAll(ArrayList(va))   // v13,v0
        datPos.addAll(ArrayList(vh))   // v14,v11
        datPos.addAll(ArrayList(vc))   // v15,v2
        datPos.addAll(ArrayList(vf))   // v16,v12,v9
        datPos.addAll(ArrayList(ve))   // v17,v8,v5
        datPos.addAll(ArrayList(va))   // v18,v13,v0
        datPos.addAll(ArrayList(vb))   // v19,v4,v1
        datPos.addAll(ArrayList(vc))   // v20,v15,v2
        datPos.addAll(ArrayList(vd))   // v21,v6,v3
        datPos.addAll(ArrayList(vh))   // v22,v14,v11
        datPos.addAll(ArrayList(vg))   // v23,v10,v7

        /*
        datPos.addAll(ArrayList(vb))   // v4,v1
        datPos.addAll(ArrayList(ve))   // v5
        datPos.addAll(ArrayList(vd))   // v6,v3
        datPos.addAll(ArrayList(vg))   // v7
         */
        datPos.addAll(vi)
        datPos.addAll(vb)
        datPos.addAll(vj)

        datPos.addAll(vk)
        datPos.addAll(vg)
        datPos.addAll(vl)

        datPos.addAll(vl)
        datPos.addAll(vd)
        datPos.addAll(vi)

        datPos.addAll(vl)
        datPos.addAll(vi)
        datPos.addAll(vj)

        datPos.addAll(vj)
        datPos.addAll(vk)
        datPos.addAll(vl)

        datPos.addAll(vj)
        datPos.addAll(ve)
        datPos.addAll(vk)

        // -----------------------------
        // 色データ
        // -----------------------------
        // 面ごとに色指定
        // -----------------------------
        // 面
        (0..4).forEach { faceId ->
            // 頂点
            (0..3).forEach {
                datCol.addAll(arrayListOf(0f,0f,0f,1f))
            }
        }

        // 黒
        (0..5).forEach {
            datCol.addAll(arrayListOf(0f,0f,0f,1f))
        }
        // 白
        (0..11).forEach {
            datCol.addAll(arrayListOf(1f,1f,1f,1f))
        }



        // インデックスデータ
        datIdx.addAll(arrayListOf(0,1,2))
        datIdx.addAll(arrayListOf(3,2,1))
        datIdx.addAll(arrayListOf(4,5,6))
        datIdx.addAll(arrayListOf(7,6,5))
        datIdx.addAll(arrayListOf(8,9,10))
        datIdx.addAll(arrayListOf(11,10,9))
        datIdx.addAll(arrayListOf(12,13,14))
        datIdx.addAll(arrayListOf(15,14,13))
        datIdx.addAll(arrayListOf(16,17,18))
        datIdx.addAll(arrayListOf(19,18,17))

        //datIdx.addAll(arrayListOf(20,21,22))
        //datIdx.addAll(arrayListOf(23,22,21))
        (20..37).forEach {
            datIdx.add(it.toShort())
        }

        // バッファ割り当て
        allocateBuffer()
    }

}
