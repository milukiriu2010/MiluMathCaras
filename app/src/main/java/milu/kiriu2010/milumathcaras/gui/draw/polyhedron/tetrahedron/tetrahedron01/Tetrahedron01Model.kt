package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.tetrahedron.tetrahedron01

import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.MgModelAbs
import kotlin.math.sqrt

class Tetrahedron01Model : MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {



        // v0,v3,v11
        val va = arrayListOf(-sqrt(3f)/2f,0f,-0.5f)
        // v1,v5,v6
        val vb = arrayListOf(0f,0f,1f)
        // v2,v8,v9
        val vc = arrayListOf(sqrt(3f)/2f,0f,-0.5f)
        // v4,v7,v10
        val vd = arrayListOf(0f,sqrt(2f),0f)

        // 頂点データ
        // v0
        datPos.addAll(ArrayList<Float>(va))
        // v1
        datPos.addAll(ArrayList<Float>(vb))
        // v2
        datPos.addAll(ArrayList<Float>(vc))
        // v3
        datPos.addAll(ArrayList<Float>(va))
        // v4
        datPos.addAll(ArrayList<Float>(vd))
        // v5
        datPos.addAll(ArrayList<Float>(vb))
        // v6
        datPos.addAll(ArrayList<Float>(vb))
        // v7
        datPos.addAll(ArrayList<Float>(vd))
        // v8
        datPos.addAll(ArrayList<Float>(vc))
        // v9
        datPos.addAll(ArrayList<Float>(vc))
        // v10
        datPos.addAll(ArrayList<Float>(vd))
        // v11
        datPos.addAll(ArrayList<Float>(va))

        // 法線データ
        (0..2).forEach {
            // (v1-v0) x (v2-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*1, 3*2, 3*0 ) )
        }
        (3..5).forEach {
            // (v4-v3) x (v5-v3)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*4, 3*5, 3*3 ) )
        }
        (6..8).forEach {
            // (v7-v6) x (v8-v6)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*7, 3*8, 3*6 ) )
        }
        (9..11).forEach {
            // (v10-v9) x (v11-v9)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*10, 3*11, 3*9 ) )
        }

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        (3..5).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        (6..8).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        (9..11).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,4,5))
        datIdx.addAll(arrayListOf<Short>(6,7,8))
        datIdx.addAll(arrayListOf<Short>(9,10,11))

        // バッファ割り当て
        allocateBuffer()
    }
}