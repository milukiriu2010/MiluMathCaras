package milu.kiriu2010.gui.model

import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.sqrt


// 正八面体
class Octahedron01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(0f,-sq2*scale,0f)
        val vb = arrayListOf(scale,0f,scale)
        val vc = arrayListOf(-scale,0f,scale)
        val vd = arrayListOf(-scale,0f,-scale)
        val ve = arrayListOf(scale,0f,-scale)
        val vf = arrayListOf(0f,sq2*scale,0f)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))   // v0
        datPos.addAll(ArrayList<Float>(vb))   // v1
        datPos.addAll(ArrayList<Float>(vc))   // v2
        datPos.addAll(ArrayList<Float>(vb))   // v3,v1
        datPos.addAll(ArrayList<Float>(vf))   // v4
        datPos.addAll(ArrayList<Float>(vc))   // v5,v2
        datPos.addAll(ArrayList<Float>(vb))   // v6,v3,v1
        datPos.addAll(ArrayList<Float>(ve))   // v7
        datPos.addAll(ArrayList<Float>(vf))   // v8,v4
        datPos.addAll(ArrayList<Float>(ve))   // v9,v7
        datPos.addAll(ArrayList<Float>(vd))   // v10
        datPos.addAll(ArrayList<Float>(vf))   // v11,v8,v4
        datPos.addAll(ArrayList<Float>(ve))   // v12,v9,v7
        datPos.addAll(ArrayList<Float>(va))   // v13,v0
        datPos.addAll(ArrayList<Float>(vd))   // v14,v10
        datPos.addAll(ArrayList<Float>(va))   // v15,v13,v0
        datPos.addAll(ArrayList<Float>(vc))   // v16,v5,v2
        datPos.addAll(ArrayList<Float>(vd))   // v17,v14,v10
        datPos.addAll(ArrayList<Float>(vf))   // v18,v11,v8,v4
        datPos.addAll(ArrayList<Float>(vd))   // v19,v17,v14,v10
        datPos.addAll(ArrayList<Float>(vc))   // v20,v16,v5,v2
        datPos.addAll(ArrayList<Float>(va))   // v21,v15,v13,v0
        datPos.addAll(ArrayList<Float>(ve))   // v22,v12,v9,v7
        datPos.addAll(ArrayList<Float>(vb))   // v23,v6,v3,v1

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
        (12..14).forEach {
            // (v13-v12) x (v14-v12)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*13, 3*14, 3*12 ) )
        }
        (15..17).forEach {
            // (v16-v15) x (v17-v15)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*16, 3*17, 3*15 ) )
        }
        (18..20).forEach {
            // (v19-v18) x (v20-v18)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*19, 3*20, 3*18 ) )
        }
        (21..23).forEach {
            // (v22-v21) x (v23-v21)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*22, 3*23, 3*21 ) )
        }

        // 色データ
        // ABC(赤)
        (0..2).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // BFC(だいだい)
        (3..5).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.5f,0f,1f))
        }
        // BEF(黄色)
        (6..8).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }
        // EDF(緑)
        (9..11).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // EAD(水色)
        (12..14).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
        }
        // ACD
        (15..17).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.5f,1f,1f))
        }
        // FDC(青)
        (18..20).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        // AEB(紫)
        (21..23).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,1f,1f))
        }

        // インデックスデータ
        (0..23).forEach { it ->
            datIdx.addAll(arrayListOf<Short>(it.toShort()))
        }

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
