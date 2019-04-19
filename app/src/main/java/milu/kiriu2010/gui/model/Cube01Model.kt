package milu.kiriu2010.gui.model

import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.sqrt


// 立方体
class Cube01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(-scale,-scale,scale)
        val vb = arrayListOf(scale,-scale,scale)
        val vc = arrayListOf(-scale,scale,scale)
        val vd = arrayListOf(scale,scale,scale)
        val ve = arrayListOf(scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf(scale,scale,-scale)
        val vh = arrayListOf(-scale,scale,-scale)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))   // v0
        datPos.addAll(ArrayList<Float>(vb))   // v1
        datPos.addAll(ArrayList<Float>(vc))   // v2
        datPos.addAll(ArrayList<Float>(vd))   // v3
        datPos.addAll(ArrayList<Float>(vb))   // v4,v1
        datPos.addAll(ArrayList<Float>(ve))   // v5
        datPos.addAll(ArrayList<Float>(vd))   // v6,v3
        datPos.addAll(ArrayList<Float>(vg))   // v7
        datPos.addAll(ArrayList<Float>(ve))   // v8,v5
        datPos.addAll(ArrayList<Float>(vf))   // v9
        datPos.addAll(ArrayList<Float>(vg))   // v10,v7
        datPos.addAll(ArrayList<Float>(vh))   // v11
        datPos.addAll(ArrayList<Float>(vf))   // v12,v9
        datPos.addAll(ArrayList<Float>(va))   // v13,v0
        datPos.addAll(ArrayList<Float>(vh))   // v14,v11
        datPos.addAll(ArrayList<Float>(vc))   // v15,v2
        datPos.addAll(ArrayList<Float>(vf))   // v16,v12,v9
        datPos.addAll(ArrayList<Float>(ve))   // v17,v8,v5
        datPos.addAll(ArrayList<Float>(va))   // v18,v13,v0
        datPos.addAll(ArrayList<Float>(vb))   // v19,v4,v1
        datPos.addAll(ArrayList<Float>(vc))   // v20,v15,v2
        datPos.addAll(ArrayList<Float>(vd))   // v21,v6,v3
        datPos.addAll(ArrayList<Float>(vh))   // v22,v14,v11
        datPos.addAll(ArrayList<Float>(vg))   // v23,v10,v7


        // 法線データ
        (0..23).forEach {
            val m = it/4
            // (v1-v0) x (v2-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*(4*m+1), 3*(4*m+2), 3*(4*m) ) )
        }
        /*
        // ABCD
        (0..3).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*1, 3*2, 3*0 ) )
        }
        // BEGD
        (4..7).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*5, 3*6, 3*4 ) )
        }
        // EFGH
        (8..11).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*9, 3*10, 3*8 ) )
        }
        // FACH
        (12..15).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*13, 3*14, 3*12 ) )
        }
        // ABEF
        (16..19).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*17, 3*18, 3*16 ) )
        }
        // CDGH
        (20..23).forEach {
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*21, 3*22, 3*20 ) )
        }
        */


        // 色データ
        // ABDC(赤)
        (0..3).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // BEGD(黄色)
        (4..7).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }
        // EFGH(緑)
        (8..11).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // FACH(水色)
        (12..15).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
        }
        // ABEF(青)
        (16..19).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        // CDGH(紫)
        (20..23).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,1f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf(0,1,2))
        datIdx.addAll(arrayListOf(1,3,2))
        datIdx.addAll(arrayListOf(4,5,6))
        datIdx.addAll(arrayListOf(5,7,6))
        datIdx.addAll(arrayListOf(8,9,10))
        datIdx.addAll(arrayListOf(9,11,10))
        datIdx.addAll(arrayListOf(12,13,14))
        datIdx.addAll(arrayListOf(13,15,14))
        datIdx.addAll(arrayListOf(16,17,18))
        datIdx.addAll(arrayListOf(17,19,18))
        datIdx.addAll(arrayListOf(20,21,22))
        datIdx.addAll(arrayListOf(21,23,22))

        // 頂点バッファ
        bufPos = ByteBuffer.allocateDirect(datPos.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datPos.toFloatArray())
                position(0)
            }
        }

        // 法線バッファ
        bufNor = ByteBuffer.allocateDirect(datNor.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datNor.toFloatArray())
                position(0)
            }
        }

        // 色バッファ
        bufCol = ByteBuffer.allocateDirect(datCol.toArray().size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datCol.toFloatArray())
                position(0)
            }
        }

        // インデックスバッファ
        bufIdx = ByteBuffer.allocateDirect(datIdx.toArray().size * 2).run {
            order(ByteOrder.nativeOrder())

            asShortBuffer().apply {
                put(datIdx.toShortArray())
                position(0)
            }
        }
    }
}
