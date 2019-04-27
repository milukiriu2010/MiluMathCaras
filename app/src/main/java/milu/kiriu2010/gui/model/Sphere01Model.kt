package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


// -------------------------------------------
// 球
// -------------------------------------------
// 2019.04.27-02
// -------------------------------------------
class Sphere01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        // 緯度
        var row    = opt["row"]?.toInt() ?: 16
        // 経度
        var column = opt["column"]?.toInt() ?: 16
        var radius = opt["radius"]?.toFloat() ?: 1f
        var scale  = opt["scale"] ?: 1f
        var color  = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f


        var rad = radius * scale

        (0..row).forEach { i ->
            var r = PI.toFloat() / row.toFloat() * i.toFloat()
            var ry = cos(r)
            var rr = sin(r)
            (0..column).forEach {  ii ->
                var tr = PI.toFloat() * 2f/column.toFloat() * ii.toFloat()
                var tx = rr * rad * cos(tr)
                var ty = ry * rad;
                var tz = rr * rad * sin(tr)
                var rx = rr * cos(tr)
                var rz = rr * sin(tr)
                if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                    datCol.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                }
                else {
                    var tc = MgColor.hsva(360/row*i,1f,1f,1f)
                    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
                datPos.addAll(arrayListOf(tx,ty,tz))
                datNor.addAll(arrayListOf(rx,ry,rz))
                datTxc.add(1f-1f/column.toFloat()*ii.toFloat())
                datTxc.add(1f/row.toFloat()*i.toFloat())
            }

            (0 until row).forEach { i ->
                (0 until column).forEach { ii ->
                    val r = (column+1)*i+ii
                    datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+1).toShort(),(r+column+2).toShort()))
                    datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+column+2).toShort(),(r+column+1).toShort()))
                }
            }
        }

        allocateBuffer()
    }
}
