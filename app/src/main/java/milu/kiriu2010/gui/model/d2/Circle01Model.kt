package milu.kiriu2010.gui.model.d2

import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// ----------------------------------------------
// 円
// ----------------------------------------------
// 2019.06.15
// 2019.07.02  パッケージ修正
// 2019.07.14  XY平面(面)
// ----------------------------------------------
class Circle01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // XY平面(周囲のみ)
            1 -> createPathPattern1(opt)
            // XY平面(面)
            2 -> createPathPattern2(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // XY平面(周囲のみ)
    private fun createPathPattern1( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        // 円の分割数
        var row = opt["row"]?.toInt() ?: 32
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        (0..row).forEach { i ->
            val ii = 360f*i.toFloat()/row.toFloat()
            val cos = MyMathUtil.cosf(ii) * scale
            val sin = MyMathUtil.sinf(ii) * scale
            // 位置
            datPos.addAll(arrayListOf(cos,sin,0f))
            // 法線
            datNor.addAll(arrayListOf(0f,0f,1f))
            // 色
            if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] == -1f ) ) {
                datCol.addAll(arrayListOf(1f,1f,1f,1f))
            }
            else {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            // インデックス
            datIdx.add(i.toShort())
        }
    }

    // XY平面(面)
    private fun createPathPattern2( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        // 円の分割数
        var row = opt["row"]?.toInt() ?: 32
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        (0 until row).forEach { i ->
            val ii1 = 360f*i.toFloat()/row.toFloat()
            val ii2 = 360f*(i+1).toFloat()/row.toFloat()
            val cos1 = MyMathUtil.cosf(ii1) * scale
            val sin1 = MyMathUtil.sinf(ii1) * scale
            val cos2 = MyMathUtil.cosf(ii2) * scale
            val sin2 = MyMathUtil.sinf(ii2) * scale
            // 位置
            datPos.addAll(arrayListOf(cos1,sin1,0f))
            datPos.addAll(arrayListOf(0f,0f,0f))
            datPos.addAll(arrayListOf(cos2,sin2,0f))
            // 法線
            (0..2).forEach {
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
            datIdx.add((3*i).toShort())
            datIdx.add((3*i+1).toShort())
            datIdx.add((3*i+2).toShort())
        }
    }
}
