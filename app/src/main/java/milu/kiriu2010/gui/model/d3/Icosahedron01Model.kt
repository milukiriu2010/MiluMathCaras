package milu.kiriu2010.gui.model.d3

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// --------------------------------------------
// 正二十面体
// --------------------------------------------
// 2019.04.29  色・テクスチャ
// 2019.04.30  頂点データ修正 for テクスチャ
// 2019.07.02  パッケージ修正
// --------------------------------------------
// https://github.com/8q/Android-OpenGL-Icosahedron/blob/master/GL1/src/com/example/gl1/MyIcosa.java
// --------------------------------------------
class Icosahedron01Model: MgModelAbs() {

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
        val goldR = MyMathUtil.GOLDEN_RATIO

        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 頂点
        val va = arrayListOf(       scale,          0f,-goldR*scale)
        val vb = arrayListOf(      -scale,          0f,-goldR*scale)
        val vc = arrayListOf(          0f,-goldR*scale,      -scale)
        val vd = arrayListOf(-goldR*scale,      -scale,          0f)
        val ve = arrayListOf( goldR*scale,      -scale,          0f)
        val vf = arrayListOf(          0f, goldR*scale,      -scale)
        val vg = arrayListOf( goldR*scale,       scale,          0f)
        val vh = arrayListOf(-goldR*scale,       scale,          0f)
        val vi = arrayListOf(          0f,-goldR*scale,       scale)
        val vj = arrayListOf(      -scale,          0f, goldR*scale)
        val vk = arrayListOf(       scale,          0f, goldR*scale)
        val vl = arrayListOf(          0f, goldR*scale,       scale)

        // 頂点データ
        // ABC
        datPos.addAll(ArrayList<Float>(va))      // A:0
        datPos.addAll(ArrayList<Float>(vb))      // B:1
        datPos.addAll(ArrayList<Float>(vc))      // C:2
        // ACE
        datPos.addAll(ArrayList<Float>(vc))      // C:4
        datPos.addAll(ArrayList<Float>(ve))      // E:5
        datPos.addAll(ArrayList<Float>(va))      // A:3
        // AEG
        datPos.addAll(ArrayList<Float>(vg))      // G:8
        datPos.addAll(ArrayList<Float>(va))      // A:6
        datPos.addAll(ArrayList<Float>(ve))      // E:7
        // AGF
        datPos.addAll(ArrayList<Float>(va))      // A:9
        datPos.addAll(ArrayList<Float>(vg))      // G:10
        datPos.addAll(ArrayList<Float>(vf))      // F:11
        // AFB
        datPos.addAll(ArrayList<Float>(vb))      // B:14
        datPos.addAll(ArrayList<Float>(va))      // A:12
        datPos.addAll(ArrayList<Float>(vf))      // F:13
        // BFH
        datPos.addAll(ArrayList<Float>(vh))      // H:17
        datPos.addAll(ArrayList<Float>(vb))      // B:15
        datPos.addAll(ArrayList<Float>(vf))      // F:16
        // BHD
        datPos.addAll(ArrayList<Float>(vb))      // B:18
        datPos.addAll(ArrayList<Float>(vh))      // H:19
        datPos.addAll(ArrayList<Float>(vd))      // D:20
        // BDC
        datPos.addAll(ArrayList<Float>(vd))      // D:22
        datPos.addAll(ArrayList<Float>(vc))      // C:23
        datPos.addAll(ArrayList<Float>(vb))      // B:21
        // CDI
        datPos.addAll(ArrayList<Float>(vc))      // C:24
        datPos.addAll(ArrayList<Float>(vd))      // D:25
        datPos.addAll(ArrayList<Float>(vi))      // I:26
        // CIE
        datPos.addAll(ArrayList<Float>(ve))      // E:29
        datPos.addAll(ArrayList<Float>(vc))      // C:27
        datPos.addAll(ArrayList<Float>(vi))      // I:28
        // EIK
        datPos.addAll(ArrayList<Float>(vk))      // K:32
        datPos.addAll(ArrayList<Float>(ve))      // E:30
        datPos.addAll(ArrayList<Float>(vi))      // I:31
        // EKG
        datPos.addAll(ArrayList<Float>(ve))      // E:33
        datPos.addAll(ArrayList<Float>(vk))      // K:34
        datPos.addAll(ArrayList<Float>(vg))      // G:35
        // GKL
        datPos.addAll(ArrayList<Float>(vl))      // L:38
        datPos.addAll(ArrayList<Float>(vg))      // G:36
        datPos.addAll(ArrayList<Float>(vk))      // K:37
        // GLF
        datPos.addAll(ArrayList<Float>(vg))      // G:39
        datPos.addAll(ArrayList<Float>(vl))      // L:40
        datPos.addAll(ArrayList<Float>(vf))      // F:41
        // KIJ
        datPos.addAll(ArrayList<Float>(vj))      // J:44
        datPos.addAll(ArrayList<Float>(vk))      // K:42
        datPos.addAll(ArrayList<Float>(vi))      // I:43
        // KJL
        datPos.addAll(ArrayList<Float>(vk))      // K:45
        datPos.addAll(ArrayList<Float>(vj))      // J:46
        datPos.addAll(ArrayList<Float>(vl))      // L:47
        // LJH
        datPos.addAll(ArrayList<Float>(vh))      // H:50
        datPos.addAll(ArrayList<Float>(vl))      // L:48
        datPos.addAll(ArrayList<Float>(vj))      // J:49
        // LHF
        datPos.addAll(ArrayList<Float>(vl))      // L:51
        datPos.addAll(ArrayList<Float>(vh))      // H:52
        datPos.addAll(ArrayList<Float>(vf))      // F:53
        // JID
        datPos.addAll(ArrayList<Float>(vd))      // D:56
        datPos.addAll(ArrayList<Float>(vj))      // J:54
        datPos.addAll(ArrayList<Float>(vi))      // I:55
        // JDH
        datPos.addAll(ArrayList<Float>(vj))      // J:57
        datPos.addAll(ArrayList<Float>(vd))      // D:58
        datPos.addAll(ArrayList<Float>(vh))      // H:59

        // 法線データ
        (0..59).forEach { i ->
            // (B-A) x (C-A)
            val m=i/3
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*(3*m+2), 3*(3*m+1), 3*(3*m+0) ) )
        }

        // 色データ
        if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
            // ABC(赤)
            (0..2).forEach {
                datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
            }
            // ACE
            (3..5).forEach {
                datCol.addAll(arrayListOf<Float>(1f,0.25f,0f,1f))
            }
            // AEG(だいだい)
            (6..8).forEach {
                datCol.addAll(arrayListOf<Float>(1f,0.5f,0f,1f))
            }
            // AGF
            (9..11).forEach {
                datCol.addAll(arrayListOf<Float>(1f,0.75f,0f,1f))
            }
            // AFB(黄色)
            (12..14).forEach {
                datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
            }
            // BFH
            (15..17).forEach {
                datCol.addAll(arrayListOf<Float>(0.75f,1f,0f,1f))
            }
            // BHD
            (18..20).forEach {
                datCol.addAll(arrayListOf<Float>(0.5f,1f,0f,1f))
            }
            // BDC
            (21..23).forEach {
                datCol.addAll(arrayListOf<Float>(0.25f,1f,0f,1f))
            }
            // CDI
            (24..26).forEach {
                datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
            }
            // CIE
            (27..29).forEach {
                datCol.addAll(arrayListOf<Float>(0f,1f,0.25f,1f))
            }
            // EIK
            (30..32).forEach {
                datCol.addAll(arrayListOf<Float>(0f,1f,0.5f,1f))
            }
            // EKG
            (33..35).forEach {
                datCol.addAll(arrayListOf<Float>(0f,1f,0.75f,1f))
            }
            // GKL(水色)
            (36..38).forEach {
                datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
            }
            // GLF
            (39..41).forEach {
                datCol.addAll(arrayListOf<Float>(0f,0.75f,1f,1f))
            }
            // KIJ
            (42..44).forEach {
                datCol.addAll(arrayListOf<Float>(0f,0.5f,1f,1f))
            }
            // KJL
            (45..47).forEach {
                datCol.addAll(arrayListOf<Float>(0f,0.25f,1f,1f))
            }
            // LJH(青)
            (48..50).forEach {
                datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
            }
            // LHF
            (51..53).forEach {
                datCol.addAll(arrayListOf<Float>(0.25f,0f,1f,1f))
            }
            // JID
            (54..56).forEach {
                datCol.addAll(arrayListOf<Float>(0.5f,0f,1f,1f))
            }
            // JDH
            (57..59).forEach {
                datCol.addAll(arrayListOf<Float>(0.75f,0f,1f,1f))
            }
        }
        else {
            (0..59).forEach { i ->
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }

        // テクスチャ座標データ
        // 正二十面体は２０面あるので２０回ループ
        (0..19).forEach {
            datTxc.addAll(arrayListOf(  0f,1f))
            datTxc.addAll(arrayListOf(  1f,1f))
            datTxc.addAll(arrayListOf(0.5f,0f))
        }

        // インデックスデータ
        (0..59).forEach {
            when (it%3) {
                0 -> datIdx.add(it.toShort())
                1 -> datIdx.add((it+1).toShort())
                2 -> datIdx.add((it-1).toShort())
            }
        }

    }

    // 点
    private fun createPathPattern10( opt: Map<String,Float> ) {
        val goldR = MyMathUtil.GOLDEN_RATIO

        var scale = opt["scale"] ?: 1f

        // 頂点
        val va = arrayListOf(       scale,          0f,-goldR*scale)
        val vb = arrayListOf(      -scale,          0f,-goldR*scale)
        val vc = arrayListOf(          0f,-goldR*scale,      -scale)
        val vd = arrayListOf(-goldR*scale,      -scale,          0f)
        val ve = arrayListOf( goldR*scale,      -scale,          0f)
        val vf = arrayListOf(          0f, goldR*scale,      -scale)
        val vg = arrayListOf( goldR*scale,       scale,          0f)
        val vh = arrayListOf(-goldR*scale,       scale,          0f)
        val vi = arrayListOf(          0f,-goldR*scale,       scale)
        val vj = arrayListOf(      -scale,          0f, goldR*scale)
        val vk = arrayListOf(       scale,          0f, goldR*scale)
        val vl = arrayListOf(          0f, goldR*scale,       scale)

        // 頂点データ
        // ABC
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vi))
        datPos.addAll(ArrayList<Float>(vj))
        datPos.addAll(ArrayList<Float>(vk))
        datPos.addAll(ArrayList<Float>(vl))


        // 色データ
        (0..11).forEach { i ->
            // 正二十面体は１２頂点があるので１２で割る
            var tc = MgColor.hsva(360/12*i,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }

    }


    // 線
    private fun createPathPattern20( opt: Map<String,Float> ) {
        val goldR = MyMathUtil.GOLDEN_RATIO

        var scale = opt["scale"] ?: 1f

        // 頂点
        val va = arrayListOf(       scale,          0f,-goldR*scale)
        val vb = arrayListOf(      -scale,          0f,-goldR*scale)
        val vc = arrayListOf(          0f,-goldR*scale,      -scale)
        val vd = arrayListOf(-goldR*scale,      -scale,          0f)
        val ve = arrayListOf( goldR*scale,      -scale,          0f)
        val vf = arrayListOf(          0f, goldR*scale,      -scale)
        val vg = arrayListOf( goldR*scale,       scale,          0f)
        val vh = arrayListOf(-goldR*scale,       scale,          0f)
        val vi = arrayListOf(          0f,-goldR*scale,       scale)
        val vj = arrayListOf(      -scale,          0f, goldR*scale)
        val vk = arrayListOf(       scale,          0f, goldR*scale)
        val vl = arrayListOf(          0f, goldR*scale,       scale)

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
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(ve))
        // l4
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vg))
        // l5
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vf))
        // l6
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vf))
        // l7
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vh))
        // l8
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vd))
        // l9
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        // l10
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vi))
        // l11
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(ve))
        // l12
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vg))
        // l13
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vf))
        // l14
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vh))
        // l15
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vd))
        // l16
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vi))
        // l17
        datPos.addAll(ArrayList<Float>(vi))
        datPos.addAll(ArrayList<Float>(ve))
        // l18
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vk))
        // l19
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vk))
        // l20
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vl))
        // l21
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vl))
        // l22
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vl))
        // l23
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vj))
        // l24
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vj))
        // l25
        datPos.addAll(ArrayList<Float>(vi))
        datPos.addAll(ArrayList<Float>(vj))
        // l26
        datPos.addAll(ArrayList<Float>(vi))
        datPos.addAll(ArrayList<Float>(vk))
        // l27
        datPos.addAll(ArrayList<Float>(vj))
        datPos.addAll(ArrayList<Float>(vk))
        // l28
        datPos.addAll(ArrayList<Float>(vk))
        datPos.addAll(ArrayList<Float>(vl))
        // l29
        datPos.addAll(ArrayList<Float>(vl))
        datPos.addAll(ArrayList<Float>(vj))

        // 色データ
        (0..59).forEach { i ->
            // ２頂点で１つの線を構成するため２で割る
            val ii = i/2
            // 正二十面体は３０線があるので３０で割る
            var tc = MgColor.hsva(360/30*ii,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }
    }
}
