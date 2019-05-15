package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


// -------------------------------------------
// 円柱
// -------------------------------------------
// 2019.05.15  初回
// -------------------------------------------
class Cylinder01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        // 円柱を輪切りする数(高さの分割数)
        var row    = opt["row"]?.toInt() ?: 16
        // 底面の分割数
        var column = opt["column"]?.toInt() ?: 16
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
        // 各輪切りの高さ(スケーリング後)
        val h = height*scale/row.toFloat()



        // 上面⇒底面を輪切りで描いていく
        (0..row).forEach { i ->
            val y = h * (row-i).toFloat()
            (0..column).forEach { j ->
                // 角度
                var t = 360f/column.toFloat()*(column-j).toFloat()
                var cos = MyMathUtil.cosf(t)
                var sin = MyMathUtil.sinf(t)
                var x = r * cos
                var z = r * sin
                datPos.addAll(arrayListOf(x,y,z))
                datNor.addAll(arrayListOf(cos,0f,sin))
                if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                    datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                }
                else {
                    var tc = MgColor.hsva(360/row*i,1f,1f,1f)
                    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
                // テクスチャ座標(X座標)
                datTxc.add(1f/column.toFloat()*j.toFloat())
                // テクスチャ座標(Y座標)
                datTxc.add(1f-1f/row.toFloat()*i.toFloat())
            }
        }

        // 頂点インデックス
        (0 until row).forEach { i ->
            (0 until column).forEach { j ->
                val k = (column+1)*i+j
                datIdx.addAll(arrayListOf(k.toShort(),(k+column+2).toShort(),(k+1).toShort()))
                datIdx.addAll(arrayListOf(k.toShort(),(k+column+1).toShort(),(k+column+2).toShort()))
            }
        }

        // 上面を描く
        // 同心円で分割
        (0..row).forEach { i ->
            // 同心円の半径
            val rR = r*i.toFloat()/row.toFloat()
            (0..column).forEach { j ->
                // 角度
                var t = 360f/column.toFloat()*(column-j).toFloat()
                var cos = MyMathUtil.cosf(t)
                var sin = MyMathUtil.sinf(t)
                var x = rR * cos
                var z = rR * sin
                datPos.addAll(arrayListOf(x,height*scale,z))
                datNor.addAll(arrayListOf(0f,1f,0f))
                if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                    datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                }
                else {
                    var tc = MgColor.hsva(360/row*i,1f,1f,1f)
                    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
                // テクスチャ座標(X座標)
                datTxc.add(1f/column.toFloat()*j.toFloat())
                // テクスチャ座標(Y座標)
                datTxc.add(1f-1f/row.toFloat()*i.toFloat())
            }
        }

        // 頂点インデックス
        (0 until row).forEach { i ->
            (0 until column).forEach { j ->
                val k = (column+1)*i+j
                datIdx.addAll(arrayListOf(k.toShort(),(k+column+2).toShort(),(k+1).toShort()))
                datIdx.addAll(arrayListOf(k.toShort(),(k+column+1).toShort(),(k+column+2).toShort()))
            }
        }


        allocateBuffer()
    }
}
