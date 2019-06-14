package milu.kiriu2010.gui.renderer

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.gui.model.MgModelAbs
import kotlin.math.sqrt

// ------------------------------------------
// 正四面体
// ------------------------------------------
// 2019.04.27  点・線
// 2019.04.29  色・テクスチャ
// 2019.04.30  テクスチャ座標修正
// 2019.06.14  頂点座標修正
// ------------------------------------------
class Tetrahedron01Model : MgModelAbs() {

    private val ta = sqrt(3f)/2f
    private val tb = 0.5f
    private val tc = sqrt(2f)

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 面：底面が三角形
            1 -> createPathPattern1(opt)
            // 面：正四面体の辺が立方体の対角線に合うよう配置
            2 -> createPathPattern2(opt)
            // 点：底面が三角形
            10 -> createPathPattern10(opt)
            // 線：底面が三角形
            20 -> createPathPattern20(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 面：底面が三角形
    private fun createPathPattern1( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // v0,v3,v11
        val va = arrayListOf(-ta*scale, 0f,-tb*scale)
        // v1,v5,v6
        val vb = arrayListOf(       0f, 0f, 1f*scale)
        // v2,v8,v9
        val vc = arrayListOf( ta*scale, 0f,-tb*scale)
        // v4,v7,v10
        val vd = arrayListOf(       0f, tc*scale,       0f)

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
            // (v2-v0) x (v1-v0)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*2, 3*1, 3*0 ) )
        }
        (3..5).forEach {
            // (v5-v3) x (v4-v3)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*5, 3*4, 3*3 ) )
        }
        (6..8).forEach {
            // (v8-v6) x (v7-v6)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*8, 3*7, 3*6 ) )
        }
        (9..11).forEach {
            // (v11-v9) x (v10-v9)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*11, 3*10, 3*9 ) )
        }

        // 色データ
        (0..11).forEach { i ->
            if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
                // ３頂点で１つの面を構成するため３で割る
                val ii = i/3
                // 正四面体は４つ頂点があるので４で割る
                var tc = MgColor.hsva(360/4*ii,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
            else {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }

        // テクスチャ座標データ
        // 正四面体は４面あるので４回ループ
        (0..3).forEach {
            datTxc.addAll(arrayListOf(  0f,1f))
            datTxc.addAll(arrayListOf(0.5f,0f))
            datTxc.addAll(arrayListOf(  1f,1f))
        }

        // インデックスデータ
        // 裏からみているので、右回りにインデックスを張る必要がある
        datIdx.addAll(arrayListOf<Short>(0,2,1))
        datIdx.addAll(arrayListOf<Short>(3,5,4))
        datIdx.addAll(arrayListOf<Short>(6,8,7))
        datIdx.addAll(arrayListOf<Short>(9,11,10))
    }

    // 面：正四面体の辺が立方体の対角線に合うよう配置
    private fun createPathPattern2( opt: Map<String,Float> ){
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // v0,v5,v9
        val va = arrayListOf(-scale, scale,-scale)
        // v1,v4,v6
        val vb = arrayListOf(-scale,-scale, scale)
        // v2,v8,v10
        val vc = arrayListOf( scale, scale, scale)
        // v3,v7,v11
        val vd = arrayListOf( scale,-scale,-scale)


        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))  // v0,v5,v9
        datPos.addAll(ArrayList<Float>(vb))  // v1,v4,v6
        datPos.addAll(ArrayList<Float>(vc))  // v2,v8,v10
        datPos.addAll(ArrayList<Float>(vd))  // v3,v7,v11
        datPos.addAll(ArrayList<Float>(vb))  // v1,v4,v6
        datPos.addAll(ArrayList<Float>(va))  // v0,v5,v9
        datPos.addAll(ArrayList<Float>(vb))  // v1,v4,v6
        datPos.addAll(ArrayList<Float>(vd))  // v3,v7,v11
        datPos.addAll(ArrayList<Float>(vc))  // v2,v8,v10
        datPos.addAll(ArrayList<Float>(va))  // v0,v5,v9
        datPos.addAll(ArrayList<Float>(vc))  // v2,v8,v10
        datPos.addAll(ArrayList<Float>(vd))  // v3,v7,v11

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
        (0..11).forEach { i ->
            if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
                // ３頂点で１つの面を構成するため３で割る
                val ii = i/3
                // 正四面体は４つ頂点があるので４で割る
                var tc = MgColor.hsva(360/4*ii,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
            else {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }

        // テクスチャデータ
        // 正四面体は４面あるので４回ループ
        (0..3).forEach {
            datTxc.addAll(arrayListOf(  0f,1f))
            datTxc.addAll(arrayListOf(0.5f,0f))
            datTxc.addAll(arrayListOf(  1f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf<Short>(0,1,2))
        datIdx.addAll(arrayListOf<Short>(3,4,5))
        datIdx.addAll(arrayListOf<Short>(6,7,8))
        datIdx.addAll(arrayListOf<Short>(9,10,11))
    }

    // 点：底面が三角形
    private fun createPathPattern10( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f

        // v0,v3,v11
        val va = arrayListOf(-ta*scale, 0f,-tb*scale)
        // v1,v5,v6
        val vb = arrayListOf(       0f, 0f, 1f*scale)
        // v2,v8,v9
        val vc = arrayListOf( ta*scale, 0f,-tb*scale)
        // v4,v7,v10
        val vd = arrayListOf(       0f, tc*scale,       0f)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))

        // 色データ
        (0..3).forEach { i ->
            // 正四面体は４つ頂点があるので４で割る
            var tc = MgColor.hsva(360/4*i,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }

    }

    // 線：底面が三角形
    private fun createPathPattern20( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f

        // v0,v3,v11
        val va = arrayListOf(-ta*scale, 0f,-tb*scale)
        // v1,v5,v6
        val vb = arrayListOf(       0f, 0f, 1f*scale)
        // v2,v8,v9
        val vc = arrayListOf( ta*scale, 0f,-tb*scale)
        // v4,v7,v10
        val vd = arrayListOf(       0f, tc*scale,       0f)

        // 頂点データ
        // l0
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        // l1
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        // l2
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(va))
        // l3
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(va))
        // l4
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vb))
        // l5
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vc))

        // 色データ
        (0..11).forEach { i ->
            // ２頂点で１つの線を構成するため２で割る
            val ii = i/2
            // 正四面体は６つ線があるので６で割る
            var tc = MgColor.hsva(360/6*ii,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }

    }


}