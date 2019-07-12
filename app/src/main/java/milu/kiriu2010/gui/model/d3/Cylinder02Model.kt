package milu.kiriu2010.gui.model.d3

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.math.MyMathUtil

// -------------------------------------------
// 円柱(弧でグラデーション)
// -------------------------------------------
// 2019.07.12
// -------------------------------------------
class Cylinder02Model: MgModelAbs() {

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

    fun createPathPattern1( opt: Map<String,Float> ) {
        // 底面の分割数
        var column = opt["column"]?.toInt() ?: 32
        // 円の半径
        var radius = opt["radius"] ?: 1f
        // 円柱の高さ
        var height = opt["height"] ?: 1f
        var scale  = opt["scale"] ?: 1f
        var color  = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 円の半径(スケーリング後)
        val r = radius * scale
        // 円柱の高さ(スケーリング後)
        val hH = height*scale

        // 側面を弧で区切って描く
        (0 until column).forEach { i ->
            // 角度
            var t1 = 360f/column.toFloat()*i.toFloat()
            var t2 = 360f/column.toFloat()*(i+1).toFloat()
            var cos1 = MyMathUtil.cosf(t1)
            var sin1 = MyMathUtil.sinf(t1)
            var cos2 = MyMathUtil.cosf(t2)
            var sin2 = MyMathUtil.sinf(t2)
            var x1 = r * cos1
            var z1 = r * sin1
            var x2 = r * cos2
            var z2 = r * sin2

            datPos.addAll(arrayListOf(x1,hH,z1))
            datPos.addAll(arrayListOf(x2,hH,z2))
            datPos.addAll(arrayListOf(x1,0f,z1))
            datPos.addAll(arrayListOf(x2,0f,z2))
            datPos.addAll(arrayListOf(x1,0f,z1))
            datPos.addAll(arrayListOf(x2,hH,z2))

            (0..2).forEach {
                datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 18*i+3, 18*i+6, 18*i ) )
            }
            (3..5).forEach {
                datNor.addAll( MyMathUtil.crossProduct3Dv2( datPos, 18*i+12, 18*i+15, 18*i+9 ) )
            }

            if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            else {
                var tc = MgColor.hsva(360/column*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
            // テクスチャ座標
            datTxc.addAll(arrayListOf((cos1+1f)/2f,0f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,0f))
            datTxc.addAll(arrayListOf((cos1+1f)/2f,1f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,1f))
            datTxc.addAll(arrayListOf((cos1+1f)/2f,1f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,0f))
            /*
            datTxc.addAll(arrayListOf((cos1+1f)/2f,(sin1+1f)/2f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,(sin2+1f)/2f))
            datTxc.addAll(arrayListOf((cos1+1f)/2f,0f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,0f))
            datTxc.addAll(arrayListOf((cos1+1f)/2f,0f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,(sin2+1f)/2f))
            */

            // 頂点インデックス
            (0..17).forEach {
                datIdx.add((18*i+it).toShort())
            }
        }

        // 頂点インデックスのシフトに使う
        val size1 = 6*column

        // 上面を描く
        (0 until column).forEach { i ->
            // 角度
            var t1 = 360f/column.toFloat()*i.toFloat()
            var t2 = 360f/column.toFloat()*(i+1).toFloat()
            var cos1 = MyMathUtil.cosf(t1)
            var sin1 = MyMathUtil.sinf(t1)
            var cos2 = MyMathUtil.cosf(t2)
            var sin2 = MyMathUtil.sinf(t2)
            var x1 = r * cos1
            var z1 = r * sin1
            var x2 = r * cos2
            var z2 = r * sin2

            datPos.addAll(arrayListOf(x1,hH,z1))
            datPos.addAll(arrayListOf(0f,hH,0f))
            datPos.addAll(arrayListOf(x2,hH,z2))

            datNor.addAll(arrayListOf(0f,1f,0f))
            datNor.addAll(arrayListOf(0f,1f,0f))
            datNor.addAll(arrayListOf(0f,1f,0f))

            if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            else {
                var tc = MgColor.hsva(360/column*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
            // テクスチャ座標
            datTxc.addAll(arrayListOf((cos1+1f)/2f,(sin1+1f)/2f))
            datTxc.addAll(arrayListOf(0.5f,0.5f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,(sin2+1f)/2f))

            // 頂点インデックス
            datIdx.addAll(arrayListOf((size1+i*3).toShort(),(size1+i*3+1).toShort(),(size1+i*3+2).toShort()))
        }

        // 頂点インデックスのシフトに使う
        val size2 = size1+3*column

        // 底面を描く
        (0 until column).forEach { i ->
            // 角度
            var t1 = 360f/column.toFloat()*i.toFloat()
            var t2 = 360f/column.toFloat()*(i+1).toFloat()
            var cos1 = MyMathUtil.cosf(t1)
            var sin1 = MyMathUtil.sinf(t1)
            var cos2 = MyMathUtil.cosf(t2)
            var sin2 = MyMathUtil.sinf(t2)
            var x1 = r * cos1
            var z1 = r * sin1
            var x2 = r * cos2
            var z2 = r * sin2

            datPos.addAll(arrayListOf(x2,0f,z2))
            datPos.addAll(arrayListOf(0f,0f,0f))
            datPos.addAll(arrayListOf(x1,0f,z1))

            datNor.addAll(arrayListOf(0f,-1f,0f))
            datNor.addAll(arrayListOf(0f,-1f,0f))
            datNor.addAll(arrayListOf(0f,-1f,0f))

            if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
            }
            else {
                var tc = MgColor.hsva(360/column*i,1f,1f,1f)
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
            }
            // テクスチャ座標
            datTxc.addAll(arrayListOf((cos2+1f)/2f,(sin2+1f)/2f))
            datTxc.addAll(arrayListOf(0.5f,0.5f))
            datTxc.addAll(arrayListOf((cos1+1f)/2f,(sin1+1f)/2f))

            // 頂点インデックス
            datIdx.addAll(arrayListOf((size2+i*3).toShort(),(size2+i*3+1).toShort(),(size2+i*3+2).toShort()))
        }
    }

}
