package milu.kiriu2010.gui.model

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.math.MyMathUtil
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// ------------------------------------------------------
// トーラス
// ------------------------------------------------------
// 2019.04.29  allocateBuffer
// 2019.05.02  テクスチャ座標修正
// 2019.05.21  頂点インデックス修正
// 2019.06.11  A成分を指定ありで色を自動生成可能とする
// ------------------------------------------------------
class Torus01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        // パイプを形成する円を、いくつの頂点で表現するのかを指定する
        var row = opt["row"]?.toInt() ?: 32
        // パイプをどれくらい分割するのかを指定する
        var column = opt["column"]?.toInt() ?: 32
        // パイプそのものの半径
        var iradius = opt["iradius"]?.toFloat() ?: 1f
        // 原点からパイプの中心までの距離
        var oradius = opt["oradius"]?.toFloat() ?: 2f
        var scale = opt["scale"] ?: 1f
        var color = FloatArray(4)
        color[0] = opt["colorR"] ?: -1f
        color[1] = opt["colorG"] ?: -1f
        color[2] = opt["colorB"] ?: -1f
        color[3] = opt["colorA"] ?: -1f

        var irad = iradius * scale
        var orad = oradius * scale

        (0..row).forEach { i ->
            var r = PI.toFloat() *2f/row.toFloat()*i.toFloat()
            var rr = cos(r)
            var ry = sin(r)
            (0..column).forEach { ii ->
                val tr = PI.toFloat() *2f/column.toFloat()*ii.toFloat()
                val tx = (rr*irad+orad)*cos(tr)
                val ty = ry*irad
                val tz = (rr*irad+orad)*sin(tr)
                val rx = rr * cos(tr)
                val rz = rr * sin(tr)
                datPos.addAll(arrayListOf<Float>(tx,ty,tz))
                datNor.addAll(arrayListOf<Float>(rx,ry,rz))
                // RGBA全成分の指定あり
                if ( ( color[0] != -1f ) and ( color[1] != -1f ) and ( color[2] != -1f ) and ( color[3] != -1f ) ) {
                    datCol.addAll(arrayListOf<Float>(color[0],color[1],color[2],color[3]))
                }
                // A成分のみ指定あり
                else if ( ( color[0] == -1f ) and ( color[1] == -1f ) and ( color[2] == -1f ) and ( color[3] != -1f ) ) {
                    val tc = MgColor.hsva(360/column*ii,1f,1f,color[3])
                    datCol.addAll(arrayListOf<Float>(tc[0],tc[1],tc[2],tc[3]))
                }
                // RGBA全成分の指定なし
                else {
                    val tc = MgColor.hsva(360/column*ii,1f,1f,1f)
                    datCol.addAll(arrayListOf<Float>(tc[0],tc[1],tc[2],tc[3]))
                }
                val rs = 1f/column.toFloat()*ii.toFloat()
                var rt = 1f/row.toFloat()*i.toFloat()+0.5f
                if (rt>1f) {
                    rt -= 1f
                }
                rt = 1f-rt
                datTxc.addAll(arrayListOf(rs,rt))
            }
        }

        (0 until row).forEach { i ->
            (0 until column).forEach { ii ->
                val r = (column+1)*i+ii
                datIdx.addAll(arrayListOf<Short>(r.toShort(),(r+column+1).toShort(),(r+1).toShort()))
                datIdx.addAll(arrayListOf<Short>((r+column+1).toShort(),(r+column+2).toShort(),(r+1).toShort()))
            }
        }

        allocateBuffer()
    }
}
