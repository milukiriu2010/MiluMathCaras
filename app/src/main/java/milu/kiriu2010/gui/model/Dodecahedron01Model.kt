package milu.kiriu2010.gui.model

import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.sqrt


// 正十二面体
class Dodecahedron01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        val sq2 = sqrt(2f)

        val goldR = MyMathUtil.GOLDEN_RATIO
        val cos36f = MyMathUtil.COS36F
        val sin36f = MyMathUtil.SIN36F
        val cos72f = MyMathUtil.COS72F
        val sin72f = MyMathUtil.SIN72F

        var scale = opt["scale"] ?: 1f

        val va = arrayListOf(scale,0f,0f)
        val vb = arrayListOf(cos72f*scale,sin72f*scale,0f)

        // 頂点データ
        // ABC
        datPos.addAll(ArrayList<Float>(va))      // A:0
        datPos.addAll(ArrayList<Float>(vb))      // B:1
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))     // C:2
        // ACD
        datPos.addAll(ArrayList<Float>(va))              // A:3
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))     // C:4
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))    // D:5
        // ADE
        datPos.addAll(ArrayList<Float>(va))              // A:6
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))    // D:7
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))     // E:8
        // BHI
        datPos.addAll(ArrayList<Float>(vb))                  // B:9
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))      // H:10
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))  // I:11
        // BIJ
        datPos.addAll(ArrayList<Float>(vb))                  // B:12
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))  // I:13
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))     // J:14
        // BJC
        datPos.addAll(ArrayList<Float>(vb))                  // B:15
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))     // J:16
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:17
        // CJK
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:18
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))     // J:19
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                   // K:20
        // CKL
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:21
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                   // K:22
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))    // L:23
        // CLD
        datPos.addAll(arrayListOf(-cos36f,sin36f,0f))                 // C:24
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))    // L:25
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:26
        // DLM
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:27
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))    // L:28
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR)) // M:29
        // DMN
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:30
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR)) // M:31
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))     // N:32
        // DNE
        datPos.addAll(arrayListOf(-cos36f,-sin36f,0f))                // D:33
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))     // N:34
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:35
        // ENO
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:36
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))     // N:37
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))  // O:38
        // EOF
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:39
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))  // O:40
        datPos.addAll(arrayListOf(goldR,0f,1f))                       // F:41
        // EFA
        datPos.addAll(arrayListOf(cos72f,-sin72f,0f))                 // E:42
        datPos.addAll(arrayListOf(goldR,0f,1f))                       // F:43
        datPos.addAll(ArrayList<Float>(va))                          // A:44
        // AFG
        datPos.addAll(ArrayList<Float>(va))                          // A:45
        datPos.addAll(arrayListOf(goldR,0f,1f))                       // F:46
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))   // G:47
        // AGH
        datPos.addAll(ArrayList<Float>(va))                          // A:48
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))   // G:49
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))      // H:50
        // AHB
        datPos.addAll(ArrayList<Float>(va))                          // A:51
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))      // H:52
        datPos.addAll(ArrayList<Float>(vb))                  // B:53
        // RQP
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:54
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:55
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:56
        // RPT
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:57
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:58
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:59
        // RTS
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:60
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:61
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:62
        // QML
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:63
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR))       // M:64
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))          // L:65
        // QLK
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:66
        datPos.addAll(arrayListOf(-goldR*cos36f,-goldR*sin36f,1f))          // L:67
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                         // K:68
        // QKP
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:69
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                         // K:70
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:71
        // PKJ
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:72
        datPos.addAll(arrayListOf(-goldR,0f,goldR))                         // K:73
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))           // J:74
        // PJI
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:75
        datPos.addAll(arrayListOf(-goldR*cos36f,goldR*sin36f,1f))           // J:76
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))        // I:77
        // PIT
        datPos.addAll(arrayListOf(-1f,0f,goldR+1f))                         // P:78
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))        // I:79
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:80
        // TIH
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:81
        datPos.addAll(arrayListOf(-goldR*cos72f,goldR*sin72f,goldR))        // I:82
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))            // H:83
        // THG
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:84
        datPos.addAll(arrayListOf(goldR*cos72f,goldR*sin72f,1f))            // H:85
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))         // G:86
        // TGS
        datPos.addAll(arrayListOf(-cos72f,sin72f,goldR+1f))                 // T:87
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))         // G:88
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:89
        // SGF
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:90
        datPos.addAll(arrayListOf(goldR*cos36f,goldR*sin36f,goldR))         // G:91
        datPos.addAll(arrayListOf(goldR,0f,1f))                             // F:92
        // SFO
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:93
        datPos.addAll(arrayListOf(goldR,0f,1f))                             // F:94
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))        // O:95
        // SOR
        datPos.addAll(arrayListOf(cos36f,sin36f,goldR+1f))                  // S:96
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))        // O:97
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:98
        // RON
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:99
        datPos.addAll(arrayListOf(goldR*cos36f,-goldR*sin36f,goldR))        // O:100
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))           // N:101
        // RNM
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:102
        datPos.addAll(arrayListOf(goldR*cos72f,-goldR*sin72f,1f))           // N:103
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR))       // M:104
        // RMQ
        datPos.addAll(arrayListOf(cos36f,-sin36f,goldR+1f))                 // R:105
        datPos.addAll(arrayListOf(-goldR*cos72f,-goldR*sin72f,goldR))       // M:106
        datPos.addAll(arrayListOf(-cos72f,-sin72f,goldR+1f))                // Q:107


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

        // インデックスデータ
        (0..107).forEach {
            when (it%3) {
                0 -> datIdx.add(it.toShort())
                1 -> datIdx.add((it+1).toShort())
                2 -> datIdx.add((it-1).toShort())
            }
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
