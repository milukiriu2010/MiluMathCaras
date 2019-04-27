package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.sqrt

// -------------------------------------------
// 正十二面体
// -------------------------------------------
// 2019.04.27  クリア
// -------------------------------------------
class Dodecahedron01Model: MgModelAbs() {

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

        val goldR = MyMathUtil.GOLDEN_RATIO
        val cos36f = MyMathUtil.COS36F
        val sin36f = MyMathUtil.SIN36F
        val cos72f = MyMathUtil.COS72F
        val sin72f = MyMathUtil.SIN72F

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(              scale,                 0f,              0f)
        val vb = arrayListOf(       cos72f*scale,       sin72f*scale,              0f)
        val vc = arrayListOf(      -cos36f*scale,       sin36f*scale,              0f)
        val vd = arrayListOf(      -cos36f*scale,      -sin36f*scale,              0f)
        val ve = arrayListOf(       cos72f*scale,      -sin72f*scale,              0f)
        val vf = arrayListOf(        goldR*scale,                 0f,           scale)
        val vg = arrayListOf( goldR*cos36f*scale, goldR*sin36f*scale,     goldR*scale)
        val vh = arrayListOf( goldR*cos72f*scale, goldR*sin72f*scale,           scale)
        val vi = arrayListOf(-goldR*cos72f*scale, goldR*sin72f*scale,     goldR*scale)
        val vj = arrayListOf(-goldR*cos36f*scale, goldR*sin36f*scale,           scale)
        val vk = arrayListOf(       -goldR*scale,                 0f,     goldR*scale)
        val vl = arrayListOf(-goldR*cos36f*scale,-goldR*sin36f*scale,           scale)
        val vm = arrayListOf(-goldR*cos72f*scale,-goldR*sin72f*scale,     goldR*scale)
        val vn = arrayListOf( goldR*cos72f*scale,-goldR*sin72f*scale,           scale)
        val vo = arrayListOf( goldR*cos36f*scale,-goldR*sin36f*scale,     goldR*scale)
        val vp = arrayListOf(           -1*scale,                 0f,(goldR+1f)*scale)
        val vq = arrayListOf(      -cos72f*scale,      -sin72f*scale,(goldR+1f)*scale)
        val vr = arrayListOf(       cos36f*scale,      -sin36f*scale,(goldR+1f)*scale)
        val vs = arrayListOf(       cos36f*scale,       sin36f*scale,(goldR+1f)*scale)
        val vt = arrayListOf(      -cos72f*scale,       sin72f*scale,(goldR+1f)*scale)

        // 頂点データ
        // ABC
        datPos.addAll(ArrayList<Float>(va))    // A:0
        datPos.addAll(ArrayList<Float>(vb))    // B:1
        datPos.addAll(ArrayList<Float>(vc))    // C:2
        // ACD
        datPos.addAll(ArrayList<Float>(va))    // A:3
        datPos.addAll(ArrayList<Float>(vc))    // C:4
        datPos.addAll(ArrayList<Float>(vd))    // D:5
        // ADE
        datPos.addAll(ArrayList<Float>(va))    // A:6
        datPos.addAll(ArrayList<Float>(vd))    // D:7
        datPos.addAll(ArrayList<Float>(ve))    // E:8
        // BHI
        datPos.addAll(ArrayList<Float>(vb))    // B:9
        datPos.addAll(ArrayList<Float>(vh))    // H:10
        datPos.addAll(ArrayList<Float>(vi))    // I:11
        // BIJ
        datPos.addAll(ArrayList<Float>(vb))    // B:12
        datPos.addAll(ArrayList<Float>(vi))    // I:13
        datPos.addAll(ArrayList<Float>(vj))    // J:14
        // BJC
        datPos.addAll(ArrayList<Float>(vb))    // B:15
        datPos.addAll(ArrayList<Float>(vj))    // J:16
        datPos.addAll(ArrayList<Float>(vc))    // C:17
        // CJK
        datPos.addAll(ArrayList<Float>(vc))    // C:18
        datPos.addAll(ArrayList<Float>(vj))    // J:19
        datPos.addAll(ArrayList<Float>(vk))    // K:20
        // CKL
        datPos.addAll(ArrayList<Float>(vc))    // C:21
        datPos.addAll(ArrayList<Float>(vk))    // K:22
        datPos.addAll(ArrayList<Float>(vl))    // L:23
        // CLD
        datPos.addAll(ArrayList<Float>(vc))    // C:24
        datPos.addAll(ArrayList<Float>(vl))    // L:25
        datPos.addAll(ArrayList<Float>(vd))    // D:26
        // DLM
        datPos.addAll(ArrayList<Float>(vd))    // D:27
        datPos.addAll(ArrayList<Float>(vl))    // L:28
        datPos.addAll(ArrayList<Float>(vm))    // M:29
        // DMN
        datPos.addAll(ArrayList<Float>(vd))    // D:30
        datPos.addAll(ArrayList<Float>(vm))    // M:31
        datPos.addAll(ArrayList<Float>(vn))    // N:32
        // DNE
        datPos.addAll(ArrayList<Float>(vd))    // D:33
        datPos.addAll(ArrayList<Float>(vn))    // N:34
        datPos.addAll(ArrayList<Float>(ve))    // E:35
        // ENO
        datPos.addAll(ArrayList<Float>(ve))    // E:36
        datPos.addAll(ArrayList<Float>(vn))    // N:37
        datPos.addAll(ArrayList<Float>(vo))    // O:38
        // EOF
        datPos.addAll(ArrayList<Float>(ve))    // E:39
        datPos.addAll(ArrayList<Float>(vo))    // O:40
        datPos.addAll(ArrayList<Float>(vf))    // F:41
        // EFA
        datPos.addAll(ArrayList<Float>(ve))    // E:42
        datPos.addAll(ArrayList<Float>(vf))    // F:43
        datPos.addAll(ArrayList<Float>(va))    // A:44
        // AFG
        datPos.addAll(ArrayList<Float>(va))    // A:45
        datPos.addAll(ArrayList<Float>(vf))    // F:46
        datPos.addAll(ArrayList<Float>(vg))    // G:47
        // AGH
        datPos.addAll(ArrayList<Float>(va))    // A:48
        datPos.addAll(ArrayList<Float>(vg))    // G:49
        datPos.addAll(ArrayList<Float>(vh))    // H:50
        // AHB
        datPos.addAll(ArrayList<Float>(va))    // A:51
        datPos.addAll(ArrayList<Float>(vh))    // H:52
        datPos.addAll(ArrayList<Float>(vb))    // B:53
        // RQP
        datPos.addAll(ArrayList<Float>(vr))    // R:54
        datPos.addAll(ArrayList<Float>(vq))    // Q:55
        datPos.addAll(ArrayList<Float>(vp))    // P:56
        // RPT
        datPos.addAll(ArrayList<Float>(vr))    // R:57
        datPos.addAll(ArrayList<Float>(vp))    // P:58
        datPos.addAll(ArrayList<Float>(vt))    // T:59
        // RTS
        datPos.addAll(ArrayList<Float>(vr))    // R:60
        datPos.addAll(ArrayList<Float>(vt))    // T:61
        datPos.addAll(ArrayList<Float>(vs))    // S:62
        // QML
        datPos.addAll(ArrayList<Float>(vq))    // Q:63
        datPos.addAll(ArrayList<Float>(vm))    // M:64
        datPos.addAll(ArrayList<Float>(vl))    // L:65
        // QLK
        datPos.addAll(ArrayList<Float>(vq))    // Q:66
        datPos.addAll(ArrayList<Float>(vl))    // L:67
        datPos.addAll(ArrayList<Float>(vk))    // K:68
        // QKP
        datPos.addAll(ArrayList<Float>(vq))    // Q:69
        datPos.addAll(ArrayList<Float>(vk))    // K:70
        datPos.addAll(ArrayList<Float>(vp))    // P:71
        // PKJ
        datPos.addAll(ArrayList<Float>(vp))    // P:72
        datPos.addAll(ArrayList<Float>(vk))    // K:73
        datPos.addAll(ArrayList<Float>(vj))    // J:74
        // PJI
        datPos.addAll(ArrayList<Float>(vp))    // P:75
        datPos.addAll(ArrayList<Float>(vj))    // J:76
        datPos.addAll(ArrayList<Float>(vi))    // I:77
        // PIT
        datPos.addAll(ArrayList<Float>(vp))    // P:78
        datPos.addAll(ArrayList<Float>(vi))    // I:79
        datPos.addAll(ArrayList<Float>(vt))    // T:80
        // TIH
        datPos.addAll(ArrayList<Float>(vt))    // T:81
        datPos.addAll(ArrayList<Float>(vi))    // I:82
        datPos.addAll(ArrayList<Float>(vh))    // H:83
        // THG
        datPos.addAll(ArrayList<Float>(vt))    // T:84
        datPos.addAll(ArrayList<Float>(vh))    // H:85
        datPos.addAll(ArrayList<Float>(vg))    // G:86
        // TGS
        datPos.addAll(ArrayList<Float>(vt))    // T:87
        datPos.addAll(ArrayList<Float>(vg))    // G:88
        datPos.addAll(ArrayList<Float>(vs))    // S:89
        // SGF
        datPos.addAll(ArrayList<Float>(vs))    // S:90
        datPos.addAll(ArrayList<Float>(vg))    // G:91
        datPos.addAll(ArrayList<Float>(vf))    // F:92
        // SFO
        datPos.addAll(ArrayList<Float>(vs))    // S:93
        datPos.addAll(ArrayList<Float>(vf))    // F:94
        datPos.addAll(ArrayList<Float>(vo))    // O:95
        // SOR
        datPos.addAll(ArrayList<Float>(vs))    // S:96
        datPos.addAll(ArrayList<Float>(vo))    // O:97
        datPos.addAll(ArrayList<Float>(vr))    // R:98
        // RON
        datPos.addAll(ArrayList<Float>(vr))    // R:99
        datPos.addAll(ArrayList<Float>(vo))    // O:100
        datPos.addAll(ArrayList<Float>(vn))    // N:101
        // RNM
        datPos.addAll(ArrayList<Float>(vr))    // R:102
        datPos.addAll(ArrayList<Float>(vn))    // N:103
        datPos.addAll(ArrayList<Float>(vm))    // M:104
        // RMQ
        datPos.addAll(ArrayList<Float>(vr))    // R:105
        datPos.addAll(ArrayList<Float>(vm))    // M:106
        datPos.addAll(ArrayList<Float>(vq))    // Q:107


        // 法線データ
        // ABCDE
        (0..8).forEach { i ->
            // (B-A) x (C-A)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*2, 3*1, 3*0 ) )
        }
        // BHIJC
        (9..17).forEach { i ->
            // (H-B) x (I-B)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*11, 3*10, 3*9 ) )
        }
        // CJKLD
        (18..26).forEach { i ->
            // (J-C) x (K-C)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*20, 3*19, 3*18 ) )
        }
        // DLMNE
        (27..35).forEach { i ->
            // (L-D) x (M-D)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*29, 3*28, 3*27 ) )
        }
        // ENOFA
        (36..44).forEach { i ->
            // (N-E) x (O-E)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*38, 3*37, 3*36 ) )
        }
        // AFGHB
        (45..53).forEach { i ->
            // (F-A) x (G-A)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*47, 3*46, 3*45 ) )
        }
        // RQPTS
        (54..62).forEach { i ->
            // (Q-R) x (P-R)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*56, 3*55, 3*54 ) )
        }
        // QMLKP
        (63..71).forEach { i ->
            // (M-Q) x (L-Q)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*65, 3*64, 3*63 ) )
        }
        // PKJIT
        (72..80).forEach { i ->
            // (K-P) x (J-P)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*74, 3*73, 3*72 ) )
        }
        // TIHGS
        (81..89).forEach { i ->
            // (I-T) x (H-T)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*83, 3*82, 3*81 ) )
        }
        // SGFOR
        (90..98).forEach { i ->
            // (G-S) x (F-S)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*92, 3*91, 3*90 ) )
        }
        // RONMQ
        (99..107).forEach { i ->
            // (O-R) x (N-R)
            datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 3*101, 3*100, 3*99 ) )
        }

        // 色データ
        // ABCDE(赤)
        (0..8).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // BHIJC(オレンジ)
        (9..17).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0.5f,0f,1f))
        }
        // CJKLD(黄色)
        (18..26).forEach {
            datCol.addAll(arrayListOf<Float>(1f,1f,0f,1f))
        }
        // DLMNE(黄緑)
        (27..35).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,1f,0f,1f))
        }
        // ENOFA(緑)
        (36..44).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // AFGHB
        (45..53).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0.5f,1f))
        }
        // RQPTS
        (54..62).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,1f,1f))
        }
        // QMLKP
        (63..71).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0.5f,1f,1f))
        }
        // PKJIT
        (72..80).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }
        // TIHGS
        (81..89).forEach {
            datCol.addAll(arrayListOf<Float>(0.5f,0f,1f,1f))
        }
        // SGFOR
        (90..98).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,1f,1f))
        }
        // RONMQ
        (99..107).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0.5f,1f))
        }
        /*
        (0..107).forEach { i ->
            // ９頂点で１つの面を構成するため９で割る
            val ii = i/9
            // 正十二面体は２０頂点あるので２０で割る
            var tc = MgColor.hsva(360/20*ii,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }
        */

        // インデックスデータ
        (0..107).forEach {
            when (it%3) {
                0 -> datIdx.add(it.toShort())
                1 -> datIdx.add((it+1).toShort())
                2 -> datIdx.add((it-1).toShort())
            }
        }
    }

    // 点
    private fun createPathPattern10( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        val goldR = MyMathUtil.GOLDEN_RATIO
        val cos36f = MyMathUtil.COS36F
        val sin36f = MyMathUtil.SIN36F
        val cos72f = MyMathUtil.COS72F
        val sin72f = MyMathUtil.SIN72F

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(              scale,                 0f,              0f)
        val vb = arrayListOf(       cos72f*scale,       sin72f*scale,              0f)
        val vc = arrayListOf(      -cos36f*scale,       sin36f*scale,              0f)
        val vd = arrayListOf(      -cos36f*scale,      -sin36f*scale,              0f)
        val ve = arrayListOf(       cos72f*scale,      -sin72f*scale,              0f)
        val vf = arrayListOf(        goldR*scale,                 0f,           scale)
        val vg = arrayListOf( goldR*cos36f*scale, goldR*sin36f*scale,     goldR*scale)
        val vh = arrayListOf( goldR*cos72f*scale, goldR*sin72f*scale,           scale)
        val vi = arrayListOf(-goldR*cos72f*scale, goldR*sin72f*scale,     goldR*scale)
        val vj = arrayListOf(-goldR*cos36f*scale, goldR*sin36f*scale,           scale)
        val vk = arrayListOf(       -goldR*scale,                 0f,     goldR*scale)
        val vl = arrayListOf(-goldR*cos36f*scale,-goldR*sin36f*scale,           scale)
        val vm = arrayListOf(-goldR*cos72f*scale,-goldR*sin72f*scale,     goldR*scale)
        val vn = arrayListOf( goldR*cos72f*scale,-goldR*sin72f*scale,           scale)
        val vo = arrayListOf( goldR*cos36f*scale,-goldR*sin36f*scale,     goldR*scale)
        val vp = arrayListOf(           -1*scale,                 0f,(goldR+1f)*scale)
        val vq = arrayListOf(      -cos72f*scale,      -sin72f*scale,(goldR+1f)*scale)
        val vr = arrayListOf(       cos36f*scale,      -sin36f*scale,(goldR+1f)*scale)
        val vs = arrayListOf(       cos36f*scale,       sin36f*scale,(goldR+1f)*scale)
        val vt = arrayListOf(      -cos72f*scale,       sin72f*scale,(goldR+1f)*scale)

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
        datPos.addAll(ArrayList<Float>(vm))
        datPos.addAll(ArrayList<Float>(vn))
        datPos.addAll(ArrayList<Float>(vo))
        datPos.addAll(ArrayList<Float>(vp))
        datPos.addAll(ArrayList<Float>(vq))
        datPos.addAll(ArrayList<Float>(vr))
        datPos.addAll(ArrayList<Float>(vs))
        datPos.addAll(ArrayList<Float>(vt))

        // 色データ
        (0..107).forEach { i ->
            // 正十二面体は２０頂点あるので２０で割る
            var tc = MgColor.hsva(360/20*i,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }
    }

    // 線
    private fun createPathPattern20( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        val goldR = MyMathUtil.GOLDEN_RATIO
        val cos36f = MyMathUtil.COS36F
        val sin36f = MyMathUtil.SIN36F
        val cos72f = MyMathUtil.COS72F
        val sin72f = MyMathUtil.SIN72F

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(              scale,                 0f,              0f)
        val vb = arrayListOf(       cos72f*scale,       sin72f*scale,              0f)
        val vc = arrayListOf(      -cos36f*scale,       sin36f*scale,              0f)
        val vd = arrayListOf(      -cos36f*scale,      -sin36f*scale,              0f)
        val ve = arrayListOf(       cos72f*scale,      -sin72f*scale,              0f)
        val vf = arrayListOf(        goldR*scale,                 0f,           scale)
        val vg = arrayListOf( goldR*cos36f*scale, goldR*sin36f*scale,     goldR*scale)
        val vh = arrayListOf( goldR*cos72f*scale, goldR*sin72f*scale,           scale)
        val vi = arrayListOf(-goldR*cos72f*scale, goldR*sin72f*scale,     goldR*scale)
        val vj = arrayListOf(-goldR*cos36f*scale, goldR*sin36f*scale,           scale)
        val vk = arrayListOf(       -goldR*scale,                 0f,     goldR*scale)
        val vl = arrayListOf(-goldR*cos36f*scale,-goldR*sin36f*scale,           scale)
        val vm = arrayListOf(-goldR*cos72f*scale,-goldR*sin72f*scale,     goldR*scale)
        val vn = arrayListOf( goldR*cos72f*scale,-goldR*sin72f*scale,           scale)
        val vo = arrayListOf( goldR*cos36f*scale,-goldR*sin36f*scale,     goldR*scale)
        val vp = arrayListOf(           -1*scale,                 0f,(goldR+1f)*scale)
        val vq = arrayListOf(      -cos72f*scale,      -sin72f*scale,(goldR+1f)*scale)
        val vr = arrayListOf(       cos36f*scale,      -sin36f*scale,(goldR+1f)*scale)
        val vs = arrayListOf(       cos36f*scale,       sin36f*scale,(goldR+1f)*scale)
        val vt = arrayListOf(      -cos72f*scale,       sin72f*scale,(goldR+1f)*scale)

        // 頂点データ
        // l0
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vb))
        // l1
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vc))
        // l2
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vd))
        // l3
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(ve))
        // l4
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(va))
        // l5
        datPos.addAll(ArrayList<Float>(va))
        datPos.addAll(ArrayList<Float>(vf))
        // l6
        datPos.addAll(ArrayList<Float>(vb))
        datPos.addAll(ArrayList<Float>(vh))
        // l7
        datPos.addAll(ArrayList<Float>(vc))
        datPos.addAll(ArrayList<Float>(vj))
        // l8
        datPos.addAll(ArrayList<Float>(vd))
        datPos.addAll(ArrayList<Float>(vl))
        // l9
        datPos.addAll(ArrayList<Float>(ve))
        datPos.addAll(ArrayList<Float>(vn))
        // l10
        datPos.addAll(ArrayList<Float>(vf))
        datPos.addAll(ArrayList<Float>(vg))
        // l11
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vh))
        // l12
        datPos.addAll(ArrayList<Float>(vh))
        datPos.addAll(ArrayList<Float>(vi))
        // l13
        datPos.addAll(ArrayList<Float>(vi))
        datPos.addAll(ArrayList<Float>(vj))
        // l14
        datPos.addAll(ArrayList<Float>(vj))
        datPos.addAll(ArrayList<Float>(vk))
        // l15
        datPos.addAll(ArrayList<Float>(vk))
        datPos.addAll(ArrayList<Float>(vl))
        // l16
        datPos.addAll(ArrayList<Float>(vl))
        datPos.addAll(ArrayList<Float>(vm))
        // l17
        datPos.addAll(ArrayList<Float>(vm))
        datPos.addAll(ArrayList<Float>(vn))
        // l18
        datPos.addAll(ArrayList<Float>(vn))
        datPos.addAll(ArrayList<Float>(vo))
        // l19
        datPos.addAll(ArrayList<Float>(vo))
        datPos.addAll(ArrayList<Float>(vf))
        // l20
        datPos.addAll(ArrayList<Float>(vg))
        datPos.addAll(ArrayList<Float>(vs))
        // l21
        datPos.addAll(ArrayList<Float>(vi))
        datPos.addAll(ArrayList<Float>(vt))
        // l22
        datPos.addAll(ArrayList<Float>(vk))
        datPos.addAll(ArrayList<Float>(vp))
        // l23
        datPos.addAll(ArrayList<Float>(vm))
        datPos.addAll(ArrayList<Float>(vq))
        // l24
        datPos.addAll(ArrayList<Float>(vo))
        datPos.addAll(ArrayList<Float>(vr))
        // l25
        datPos.addAll(ArrayList<Float>(vp))
        datPos.addAll(ArrayList<Float>(vq))
        // l26
        datPos.addAll(ArrayList<Float>(vq))
        datPos.addAll(ArrayList<Float>(vr))
        // l27
        datPos.addAll(ArrayList<Float>(vr))
        datPos.addAll(ArrayList<Float>(vs))
        // l28
        datPos.addAll(ArrayList<Float>(vs))
        datPos.addAll(ArrayList<Float>(vt))
        // l29
        datPos.addAll(ArrayList<Float>(vt))
        datPos.addAll(ArrayList<Float>(vp))

        // 色データ
        (0 until datPos.size).forEach { i ->
            // ２頂点で１つの線を構成するため２で割る
            val ii = i/2
            // 正十二面体は３０あるので３０で割る
            var tc = MgColor.hsva(360/30*ii,1f,1f,1f)
            datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
        }

    }

}
