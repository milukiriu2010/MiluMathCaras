package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil


// -------------------------------------------
// 円錐台
// -------------------------------------------
// 2019.06.10  初回
// -------------------------------------------
class ConeTruncated01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        // 底面の分割数
        var column = opt["column"]?.toInt() ?: 32
        // 円の半径(上面)
        var tradius = opt["tradius"] ?: 0.5f
        // 円の半径(底面)
        var bradius = opt["bradius"] ?: 1f
        // 円錐の高さ
        var height = opt["height"] ?: 1f
        var scale  = opt["scale"] ?: 1f
        var color  = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        // 円の半径(上面)(スケーリング後)
        val tr = tradius * scale;
        // 円の半径(底面)(スケーリング後)
        val br = bradius * scale
        // 円錐の高さ(スケーリング後)
        val hH = height*scale

        // 隣同士の頂点の角度
        var tt = 360f/column.toFloat()

        // 上面を描く
        (0 until column).forEach { i ->
            // 角度
            var t1 = 360f/column.toFloat()*i.toFloat()
            var t2 = t1+tt
            var cos1 = MyMathUtil.cosf(t1)
            var sin1 = MyMathUtil.sinf(t1)
            var cos2 = MyMathUtil.cosf(t2)
            var sin2 = MyMathUtil.sinf(t2)
            var z1 = tr * cos1
            var x1 = tr * sin1
            var z2 = tr * cos2
            var x2 = tr * sin2

            datPos.addAll(arrayListOf(x2,hH,z2))
            datPos.addAll(arrayListOf(0f,hH,0f))
            datPos.addAll(arrayListOf(x1,hH,z1))

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
            datTxc.addAll(arrayListOf((cos2+1f)*0.5f,(sin2+1f)*0.5f))
            datTxc.addAll(arrayListOf(0.5f,0.5f))
            datTxc.addAll(arrayListOf((cos1+1f)*0.5f,(sin1+1f)*0.5f))

            // 頂点インデックス
            datIdx.addAll(arrayListOf((i*3).toShort(),(i*3+1).toShort(),(i*3+2).toShort()))
        }

        // 頂点インデックスのシフトに使う
        val size1 = 3*column

        // 上面⇒底面に向かって線を描いていく
        (0 until column).forEach { j ->
            // 角度
            var t0 = 360f/column.toFloat()*j.toFloat()
            var t1 = t0+tt
            var cos0 = MyMathUtil.cosf(t0)
            var sin0 = MyMathUtil.sinf(t0)
            var cos1 = MyMathUtil.cosf(t1)
            var sin1 = MyMathUtil.sinf(t1)
            var z0 = tr * cos0
            var x0 = tr * sin0
            var z1 = tr * cos1
            var x1 = tr * sin1
            var z2 = br * cos0
            var x2 = br * sin0
            var z3 = br * cos1
            var x3 = br * sin1

            datPos.addAll(arrayListOf(x0,hH,z0))    // 左上
            datPos.addAll(arrayListOf(x2,0f,z2))    // 左下
            datPos.addAll(arrayListOf(x3,0f,z3))    // 右下
            datPos.addAll(arrayListOf(x1,hH,z1))    // 右上
            datPos.addAll(arrayListOf(x0,hH,z0))    // 左上
            datPos.addAll(arrayListOf(x3,0f,z3))    // 右下

            (0..5).forEach {
                if ( it <= 2 ) {
                    datNor.addAll(MyMathUtil.crossProduct3Dv2(datPos,3*size1+6*3*j+3,3*size1+6*3*j+6,3*size1+6*3*j))
                }
                else {
                    datNor.addAll(MyMathUtil.crossProduct3Dv2(datPos,3*size1+6*3*j+12,3*size1+6*3*j+15,3*size1+6*3*j+9))
                }

                if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                    datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                }
                else {
                    var tc = MgColor.hsva(360/column*j,1f,1f,1f)
                    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
            }
            // テクスチャ座標
            datTxc.addAll(arrayListOf((cos0+1f)*0.5f,0f))
            datTxc.addAll(arrayListOf((cos0+1f)*0.5f,1f))
            datTxc.addAll(arrayListOf((cos1+1f)*0.5f,1f))
            datTxc.addAll(arrayListOf((cos1+1f)*0.5f,0f))
            datTxc.addAll(arrayListOf((cos0+1f)*0.5f,0f))
            datTxc.addAll(arrayListOf((cos1+1f)*0.5f,1f))

            // インデックス
            (0..5).forEach {
                datIdx.add((size1+6*j+it).toShort())
            }
        }

        // 頂点インデックスのシフトに使う
        val size2 = size1 + 6*column

        // 底面を描く
        (0 until column).forEach { i ->
            // 角度
            var t1 = 360f/column.toFloat()*i.toFloat()
            var t2 = t1+tt
            var cos1 = MyMathUtil.cosf(t1)
            var sin1 = MyMathUtil.sinf(t1)
            var cos2 = MyMathUtil.cosf(t2)
            var sin2 = MyMathUtil.sinf(t2)
            var z1 = br * cos1
            var x1 = br * sin1
            var z2 = br * cos2
            var x2 = br * sin2

            datPos.addAll(arrayListOf(x1,0f,z1))
            datPos.addAll(arrayListOf(0f,0f,0f))
            datPos.addAll(arrayListOf(x2,0f,z2))

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
            datTxc.addAll(arrayListOf((cos1+1f)/2f,(sin1+1f)/2f))
            datTxc.addAll(arrayListOf(0.5f,0.5f))
            datTxc.addAll(arrayListOf((cos2+1f)/2f,(sin2+1f)/2f))

            // 頂点インデックス
            datIdx.addAll(arrayListOf((size2+i*3).toShort(),(size2+i*3+1).toShort(),(size2+i*3+2).toShort()))
        }

        // バッファ割り当て
        allocateBuffer()
    }
}
