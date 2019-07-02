package milu.kiriu2010.gui.model.d3

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// ----------------------------------------------
// 立方体
// ----------------------------------------------
// 2019.04.27  点・線
// 2019.04.28  createPathPattern2 by wgld.org
// 2019.04.29  色・テクスチャ
// 2019.04.30  テクスチャ座標・インデックス修正
// 2019.05.01  点・線の色修正
// 2019.06.12  コメント追加
// 2019.07.02  パッケージ修正
// ----------------------------------------------
class Cube01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 面:自作
            1 -> createPathPattern1(opt)
            // 面:wgld.org:テクスチャマップ
            2 -> createPathPattern2(opt)
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
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val va = arrayListOf(-scale,-scale, scale)
        val vb = arrayListOf( scale,-scale, scale)
        val vc = arrayListOf(-scale, scale, scale)
        val vd = arrayListOf( scale, scale, scale)
        val ve = arrayListOf( scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf( scale, scale,-scale)
        val vh = arrayListOf(-scale, scale,-scale)

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

        // 色データ
        if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
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
        }
        else {
            (0..23).forEach { i ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }

        // テクスチャ座標データ
        // 立方体は６面あるので６回ループする
        (0..5).forEach {
            datTxc.addAll(arrayListOf(0f,1f)) // v0,v4, v8,v12,v16,v20
            datTxc.addAll(arrayListOf(1f,1f)) // v1,v5, v9,v13,v17,v21
            datTxc.addAll(arrayListOf(0f,0f)) // v2,v6,v10,v14,v18,v22
            datTxc.addAll(arrayListOf(1f,0f)) // v3,v7,v11,v15,v19,v23
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
        datIdx.addAll(arrayListOf(20,21,22))
        datIdx.addAll(arrayListOf(23,22,21))
    }

    // 面:wgld.org
    private fun createPathPattern2( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        var color  = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val hs = scale * 0.5f

        val va = arrayListOf(-hs,-hs, hs)
        val vb = arrayListOf( hs,-hs, hs)
        val vc = arrayListOf( hs, hs, hs)
        val vd = arrayListOf(-hs, hs, hs)
        val ve = arrayListOf(-hs,-hs,-hs)
        val vf = arrayListOf(-hs, hs,-hs)
        val vg = arrayListOf( hs, hs,-hs)
        val vh = arrayListOf( hs,-hs,-hs)

        // 頂点データ
        // 0-3(前)
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        // 4-7(後)
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vh))
        // 8-11(上)
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vg))
        // 12-15(底)
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(va))
        // 16-19(右)
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vb))
        // 20-23(左)
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vf))

        // 法線データ
        // 0-3(前)
        datNor.addAll(arrayListOf(-1f,-1f, 1f))
        datNor.addAll(arrayListOf( 1f,-1f, 1f))
        datNor.addAll(arrayListOf( 1f, 1f, 1f))
        datNor.addAll(arrayListOf(-1f, 1f, 1f))
        // 4-7(後)
        datNor.addAll(arrayListOf(-1f,-1f,-1f))
        datNor.addAll(arrayListOf(-1f, 1f,-1f))
        datNor.addAll(arrayListOf( 1f, 1f,-1f))
        datNor.addAll(arrayListOf( 1f,-1f,-1f))
        // 8-11(上)
        datNor.addAll(arrayListOf(-1f, 1f,-1f))
        datNor.addAll(arrayListOf(-1f, 1f, 1f))
        datNor.addAll(arrayListOf( 1f, 1f, 1f))
        datNor.addAll(arrayListOf( 1f, 1f,-1f))
        // 12-15(底)
        datNor.addAll(arrayListOf(-1f,-1f,-1f))
        datNor.addAll(arrayListOf( 1f,-1f,-1f))
        datNor.addAll(arrayListOf( 1f,-1f, 1f))
        datNor.addAll(arrayListOf(-1f,-1f, 1f))
        // 16-19(右)
        datNor.addAll(arrayListOf( 1f,-1f,-1f))
        datNor.addAll(arrayListOf( 1f, 1f,-1f))
        datNor.addAll(arrayListOf( 1f, 1f, 1f))
        datNor.addAll(arrayListOf( 1f,-1f, 1f))
        // 20-23(下)
        datNor.addAll(arrayListOf(-1f,-1f,-1f))
        datNor.addAll(arrayListOf(-1f,-1f, 1f))
        datNor.addAll(arrayListOf(-1f, 1f, 1f))
        datNor.addAll(arrayListOf(-1f, 1f,-1f))

        // 色データ
        (0..datPos.size/3).forEach { i ->
            if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            else {
                val tc = MgColor.hsva(360/datPos.size/3*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
        }

        // テクスチャ座標データ
        (0..5).forEach {
            datTxc.addAll(arrayListOf(0f,0f))
            datTxc.addAll(arrayListOf(1f,0f))
            datTxc.addAll(arrayListOf(1f,1f))
            datTxc.addAll(arrayListOf(0f,1f))
        }

        // インデックスデータ
        datIdx.addAll(arrayListOf(0,1,2))
        datIdx.addAll(arrayListOf(0,2,3))
        datIdx.addAll(arrayListOf(4,5,6))
        datIdx.addAll(arrayListOf(4,6,7))
        datIdx.addAll(arrayListOf(8,9,10))
        datIdx.addAll(arrayListOf(8,10,11))
        datIdx.addAll(arrayListOf(12,13,14))
        datIdx.addAll(arrayListOf(12,14,15))
        datIdx.addAll(arrayListOf(16,17,18))
        datIdx.addAll(arrayListOf(16,18,19))
        datIdx.addAll(arrayListOf(20,21,22))
        datIdx.addAll(arrayListOf(20,22,23))
    }

    // 点
    private fun createPathPattern10( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val va = arrayListOf(-scale,-scale, scale)
        val vb = arrayListOf( scale,-scale, scale)
        val vc = arrayListOf(-scale, scale, scale)
        val vd = arrayListOf( scale, scale, scale)
        val ve = arrayListOf( scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf( scale, scale,-scale)
        val vh = arrayListOf(-scale, scale,-scale)

        // 頂点データ
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vh))

        // 色データ
        if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
            (0..7).forEach { i ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }
        else {
            (0..7).forEach { i ->
                // 立方体は８つ頂点があるので８で割る
                var tc = MgColor.hsva(360/8*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
        }
    }


    // 線
    private fun createPathPattern20( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val va = arrayListOf(-scale,-scale, scale)
        val vb = arrayListOf( scale,-scale, scale)
        val vc = arrayListOf(-scale, scale, scale)
        val vd = arrayListOf( scale, scale, scale)
        val ve = arrayListOf( scale,-scale,-scale)
        val vf = arrayListOf(-scale,-scale,-scale)
        val vg = arrayListOf( scale, scale,-scale)
        val vh = arrayListOf(-scale, scale,-scale)

        // 頂点データ
        // l0
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        // l1
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vd))
        // l2
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vc))
        // l3
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(va))
        // l4
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vf))
        // l5
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(ve))
        // l6
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vh))
        // l7
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vg))
        // l8
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))
        // l9
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vh))
        // l10
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vg))
        // l11
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(ve))

        // 色データ
        if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
            (0..23).forEach { i ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }
        else {
            (0..23).forEach { i ->
                // ２頂点で１つの線を構成するため２で割る
                val ii = i/2
                // 立方体は１２線があるので１２で割る
                var tc = MgColor.hsva(360/8*ii,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
        }
    }

}
