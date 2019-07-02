package milu.kiriu2010.gui.model.d2

import milu.kiriu2010.gui.model.MgModelAbs

// ----------------------------------------------
// 板ポリゴン
// ----------------------------------------------
// 2019.06.15
// 2019.06.30 ZX平面
// 2019.07.02  パッケージ修正
// ----------------------------------------------
class Board01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // XY平面
            1 -> createPathPattern1(opt)
            // ZX平面
            2 -> createPathPattern2(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 面(XY平面)
    private fun createPathPattern1( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 位置(Zを描くような順序)
        datPos.addAll(arrayListOf(-scale, scale,0f))
        datPos.addAll(arrayListOf( scale, scale,0f))
        datPos.addAll(arrayListOf(-scale,-scale,0f))
        datPos.addAll(arrayListOf( scale,-scale,0f))

        (0..3).forEach {
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
        datIdx.addAll(arrayListOf(0,1,2,3,2,1))
    }

    // 面(ZX平面)
    private fun createPathPattern2( opt: Map<String,Float> ) {
        var scale = opt["scale"] ?: 1f
        val color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 位置(Zを描くような順序)
        datPos.addAll(arrayListOf(-scale,0f, scale))
        datPos.addAll(arrayListOf( scale,0f, scale))
        datPos.addAll(arrayListOf(-scale,0f,-scale))
        datPos.addAll(arrayListOf( scale,0f,-scale))

        (0..3).forEach {
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
        datIdx.addAll(arrayListOf(0,1,2,3,2,1))
    }
}
