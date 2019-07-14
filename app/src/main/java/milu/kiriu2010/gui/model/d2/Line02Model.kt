package milu.kiriu2010.gui.model.d2

import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// ----------------------------------------------
// 曲線(床用)
// ----------------------------------------------
// 2019.07.13
// ----------------------------------------------
class Line02Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            1 -> createPathPattern1()
            else -> createPathPattern1()
        }

        // バッファ割り当て
        allocateBuffer()
    }

    private fun createPathPattern1() {
        (-360..360 step 5).forEach { i ->
            val ii = i.toFloat()

            // 位置
            datPos.add(ii/36f)
            datPos.add(MyMathUtil.sinf(ii))
            datPos.add(0f)

            // 色
            datCol.addAll(arrayListOf(1f,0f,0f,1f))

            // インデックス
            //datIdx.addAll(arrayListOf((3*i).toShort(),(3*i+1).toShort(),(3*i+2).toShort()))
        }
    }
}
