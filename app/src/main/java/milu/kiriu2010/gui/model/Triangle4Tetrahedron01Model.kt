package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

// --------------------------------------
// 正四面体展開図用の三角形
// --------------------------------------
// 2019.05.21  初回
// --------------------------------------
class Triangle4Tetrahedron01Model: MgModelAbs() {

    val ta = MyMathUtil.SQLT3/3f
    val tb = MyMathUtil.SQLT3/6f
    val tc = MyMathUtil.SQLT3*2f/3f
    val td = 0.5f
    val te = 1f

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 正四面体展開図(0-1-2)
            1 -> createPathPattern1(opt)
            // 正四面体展開図(3-4-5)
            2 -> createPathPattern2(opt)
            // 正四面体展開図(6-7-8)
            3 -> createPathPattern3(opt)
            // 正四面体展開図(9-10-11)
            4 -> createPathPattern4(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 正四面体展開図(0-1-2)
    private fun createPathPattern1(opt: Map<String, Float>) {

        // 頂点データ
        datPos.addAll(arrayListOf(-td, tb,0f))
        datPos.addAll(arrayListOf( 0f,-ta,0f))
        datPos.addAll(arrayListOf( td, tb,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(1f,0f,0f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }

    // 正四面体展開図(3-4-5)
    private fun createPathPattern2(opt: Map<String, Float>) {

        // 頂点データ
        datPos.addAll(arrayListOf(-td, tb,0f))
        datPos.addAll(arrayListOf(-te,-ta,0f))
        datPos.addAll(arrayListOf( 0f,-ta,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(0f,1f,0f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }

    // 正四面体展開図(6-7-8)
    private fun createPathPattern3(opt: Map<String, Float>) {

        // 頂点データ
        datPos.addAll(arrayListOf( 0f,-ta,0f))
        datPos.addAll(arrayListOf( te,-ta,0f))
        datPos.addAll(arrayListOf( td, tb,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(0f,0f,1f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }

    // 正四面体展開図(9-10-11)
    private fun createPathPattern4(opt: Map<String, Float>) {

        // 頂点データ
        datPos.addAll(arrayListOf( td, tb,0f))
        datPos.addAll(arrayListOf( 0f, tc,0f))
        datPos.addAll(arrayListOf(-td, tb,0f))

        // 法線データ
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))
        datNor.addAll(arrayListOf(0f,0f,1f))

        // 色データ
        (0..2).forEach {
            datCol.addAll(arrayListOf(1f,1f,0f,1f))
        }

        // 頂点インデックス
        datIdx.addAll(arrayListOf(0,1,2))
    }
}
