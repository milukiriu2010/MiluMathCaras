package milu.kiriu2010.gui.model.d3

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil
import kotlin.math.sqrt

// ------------------------------------------
// 正八面体
// ------------------------------------------
// 2019.04.27  点・線
// 2019.04.29  色・テクスチャ
// 2019.04.30  頂点・テクスチャ座標修正
// 2019.07.02  パッケージ修正
// 2019.07.10  warning消す
// ------------------------------------------
class Octahedron01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 面
            1 -> createPathPattern1(opt)
            // 点
            10 -> createPathPattern10(opt)
            // 線
            20 -> createPathPattern20(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 面
    private fun createPathPattern1( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val va = arrayListOf(    0f,-sq2*scale,    0f)
        val vb = arrayListOf( scale,        0f, scale)
        val vc = arrayListOf(-scale,        0f, scale)
        val vd = arrayListOf(-scale,        0f,-scale)
        val ve = arrayListOf( scale,        0f,-scale)
        val vf = arrayListOf(    0f, sq2*scale,    0f)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))   // v0
        datPos.addAll(ArrayList<Float>(vb))   // v1
        datPos.addAll(ArrayList<Float>(vc))   // v2

        datPos.addAll(ArrayList<Float>(vf))   // v3
        datPos.addAll(ArrayList<Float>(vc))   // v4
        datPos.addAll(ArrayList<Float>(vb))   // v5

        datPos.addAll(ArrayList<Float>(vf))   // v6
        datPos.addAll(ArrayList<Float>(vb))   // v7
        datPos.addAll(ArrayList<Float>(ve))   // v8

        datPos.addAll(ArrayList<Float>(vf))   // v9
        datPos.addAll(ArrayList<Float>(ve))   // v10
        datPos.addAll(ArrayList<Float>(vd))   // v11

        datPos.addAll(ArrayList<Float>(va))   // v12
        datPos.addAll(ArrayList<Float>(vd))   // v13
        datPos.addAll(ArrayList<Float>(ve))   // v14

        datPos.addAll(ArrayList<Float>(va))   // v15
        datPos.addAll(ArrayList<Float>(vc))   // v16
        datPos.addAll(ArrayList<Float>(vd))   // v17

        datPos.addAll(ArrayList<Float>(vf))   // v18
        datPos.addAll(ArrayList<Float>(vd))   // v19
        datPos.addAll(ArrayList<Float>(vc))   // v20

        datPos.addAll(ArrayList<Float>(va))   // v21
        datPos.addAll(ArrayList<Float>(ve))   // v22
        datPos.addAll(ArrayList<Float>(vb))   // v23

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
        if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
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
            //(0..23).forEach { i ->
            //    // ３頂点で１つの面を構成するため３で割る
            //    val ii = i/3
            //    // 正八面体は６つ頂点があるので６で割る
            //    var tc = MgColor.hsva(360/6*ii,1f,1f,1f)
            //    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            //}
        }
        else {
            (0..23).forEach { _ ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }

        // テクスチャ座標データ
        // 正八面体は８面あるので８回ループ
        (0..7).forEach {
            datTxc.addAll(arrayListOf(0.5f,0f))
            datTxc.addAll(arrayListOf(0f,1f))
            datTxc.addAll(arrayListOf(1f,1f))
        }

        // インデックスデータ
        (0..23).forEach { it ->
            datIdx.addAll(arrayListOf<Short>(it.toShort()))
        }
    }

    // 点
    private fun createPathPattern10( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(    0f,-sq2*scale,    0f)
        val vb = arrayListOf( scale,        0f, scale)
        val vc = arrayListOf(-scale,        0f, scale)
        val vd = arrayListOf(-scale,        0f,-scale)
        val ve = arrayListOf( scale,        0f,-scale)
        val vf = arrayListOf(    0f, sq2*scale,    0f)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))

        // 色データ
        (0..23).forEach { i ->
            // 正八面体は６つ頂点があるので６で割る
            var tc = MgColor.hsva(360/6*i,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }
    }

    // 線
    private fun createPathPattern20( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(    0f,-sq2*scale,    0f)
        val vb = arrayListOf( scale,        0f, scale)
        val vc = arrayListOf(-scale,        0f, scale)
        val vd = arrayListOf(-scale,        0f,-scale)
        val ve = arrayListOf( scale,        0f,-scale)
        val vf = arrayListOf(    0f, sq2*scale,    0f)

        // 頂点データ
        // l0
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        // l1
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vc))
        // l2
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vd))
        // l3
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(ve))
        // l4
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        // l5
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        // l6
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(ve))
        // l7
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vb))
        // l8
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vb))
        // l9
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vc))
        // l10
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vd))
        // l11
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(ve))

        // 色データ
        (0..23).forEach { i ->
            // ２頂点で１つの線を構成するため２で割る
            val ii = i/2
            // 正八面体は１２線があるので１２で割る
            var tc = MgColor.hsva(360/12*ii,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }
    }
}
