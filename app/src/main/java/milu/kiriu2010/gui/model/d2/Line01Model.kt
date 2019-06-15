package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.basic.MyPointF
import kotlin.math.pow

// ----------------------------------------------
// 線
// ----------------------------------------------
// 2019.06.15
// ----------------------------------------------
class Line01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            1 -> createPathPattern1(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    private fun createPathPattern1( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 位置
        datPos.addAll(arrayListOf(-scale, 0f,0f))
        datPos.addAll(arrayListOf( scale, 0f,0f))

        (0..1).forEach {
            // 法線
            datNor.addAll(arrayListOf(0f,0f,1f))
            // 色
            if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
                datCol.addAll(arrayListOf<Float>(1f,1f,1f,1f))
            }
            else {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
        }

        // インデックス
        datIdx.addAll(arrayListOf(0,1))
    }
}
