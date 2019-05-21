package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

// --------------------------------------
// 三角形
// --------------------------------------
// 2019.05.21  初回
// --------------------------------------
class Triangle01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 展開図
            2 -> createPathPattern1(opt)
            // WebGL
            2 -> createPathPattern2(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 展開図
    private fun createPathPattern1(opt: Map<String, Float>) {

        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        val ta = sqrt(3f)/2f*scale
        val tb = 0.5f*scale

        // 頂点データ
        datPos.addAll(arrayListOf( 0f,tb,0f))
        datPos.addAll(arrayListOf(-ta,0f,0f))
        datPos.addAll(arrayListOf( ta,0f,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
            datCol.addAll(arrayListOf(1f,0f,0f,1f))
            datCol.addAll(arrayListOf(0f,1f,0f,1f))
            datCol.addAll(arrayListOf(0f,0f,1f,1f))
        }
        else {
            datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
        }
    }

    // WebGL
    private fun createPathPattern2(opt: Map<String, Float>) {
        // 頂点データ
        datPos.addAll(arrayListOf( 0f,1f,0f))
        datPos.addAll(arrayListOf( 1f,0f,0f))
        datPos.addAll(arrayListOf(-1f,0f,0f))

        // 色データ
        datCol.addAll(arrayListOf(1f,0f,0f,1f))
        datCol.addAll(arrayListOf(0f,1f,0f,1f))
        datCol.addAll(arrayListOf(0f,0f,1f,1f))
    }
}
