package milu.kiriu2010.gui.model.d3

import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.MgModelAbs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


// ----------------------------------------------------
// 球
// ----------------------------------------------------
// 2019.04.27  パターン１
// 2019.05.12  パターン２
// 2019.05.21  頂点インデックス修正
// 2019.06.11  A成分を指定ありで色を自動生成可能とする
// 2019.07.02  パッケージ修正
// 2019.07.10  warning消す
// 2019.07.16  色を緯度・経度で変更できるように修正
// 2019.10.02  半球オプション追加
// ----------------------------------------------------
class Sphere01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ) {
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        val pattern = opt["pattern"]?.toInt() ?: 1

        when ( pattern ) {
            // 球体を塗りつぶす
            1 -> createPathPattern1(opt)
            // 球体を線で描画
            2 -> createPathPattern2(opt)
            else -> createPathPattern1(opt)
        }

        // バッファ割り当て
        allocateBuffer()
    }

    // 球体を塗りつぶす
    //   latlng
    //     1:東西/2:南北で色が変化
    //   hemi
    //     0:全球同一色/2:半球で色分け
    private fun createPathPattern1( opt: Map<String,Float> ) {
        // 緯度
        var row    = opt["row"]?.toInt() ?: 32
        // 経度
        var column = opt["column"]?.toInt() ?: 32
        var radius = opt["radius"] ?: 1f
        var scale  = opt["scale"] ?: 1f
        // 全球の色/球体前半の色
        var color1  = FloatArray(4)
        color1[0] = opt["colorR"] ?: -1f
        color1[1] = opt["colorG"] ?: -1f
        color1[2] = opt["colorB"] ?: -1f
        color1[3] = opt["colorA"] ?: -1f
        // 経度:1/緯度:2
        var latlng = opt["latlng"] ?: 1f
        // 全球:0/半球:1
        var hemi = opt["hemi"] ?: 0f
        // 球体後半の色
        var color2  = FloatArray(4)
        color2[0] = opt["colorR2"] ?: color1[0]
        color2[1] = opt["colorG2"] ?: color1[1]
        color2[2] = opt["colorB2"] ?: color1[2]
        color2[3] = opt["colorA2"] ?: color1[3]

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
                // RGBA全成分の指定あり
                if ( ( color1[0] != -1f ) and ( color1[1] != -1f ) and ( color1[2] != -1f ) and ( color1[3] != -1f ) ) {
                    // 全球
                    if (hemi == 0f) {
                        datCol.addAll(arrayListOf(color1[0],color1[1],color1[2],color1[3]))
                    }
                    // 半球
                    else {
                        // 前半:上半球
                        if (i < (row/2)) {
                            datCol.addAll(arrayListOf(color1[0],color1[1],color1[2],color1[3]))
                        }
                        // 後半:下半球
                        else {
                            datCol.addAll(arrayListOf(color2[0],color2[1],color2[2],color2[3]))
                        }
                    }
                }
                // A成分のみ指定あり
                else if ( ( color1[0] == -1f ) and ( color1[1] == -1f ) and ( color1[2] == -1f ) and ( color1[3] != -1f ) ) {
                    val tc = if ( latlng == 1f ) {
                        // 経度で色を変更
                        MgColor.hsva(360/column*ii,1f,1f,color1[3])
                    }
                    else {
                        // 緯度で色を変更
                        MgColor.hsva(360/row*i,1f,1f,color1[3])
                    }
                    datCol.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
                // RGBA全成分の指定なし
                else {
                    val tc = if ( latlng == 1f ) {
                        // 経度で色を変更
                        MgColor.hsva(360 / column * ii, 1f, 1f, 1f)
                    }
                    else {
                        // 緯度で色を変更
                        MgColor.hsva(360 / row * i, 1f, 1f, 1f)
                    }
                    datCol.addAll(arrayListOf<Float>(tc[0],tc[1],tc[2],tc[3]))
                }
                datPos.addAll(arrayListOf(tx,ty,tz))
                datNor.addAll(arrayListOf(rx,ry,rz))
                datTxc.add(1f-1f/column.toFloat()*ii.toFloat())
                datTxc.add(1f/row.toFloat()*i.toFloat())
            }
        }

        (0 until row).forEach { i ->
            (0 until column).forEach { ii ->
                val r = (column+1)*i+ii
                datIdx.addAll(arrayListOf(r.toShort(),(r+1).toShort(),(r+column+2).toShort()))
                datIdx.addAll(arrayListOf(r.toShort(),(r+column+2).toShort(),(r+column+1).toShort()))
            }
        }
    }

    // 線で描画
    private fun createPathPattern2( opt: Map<String,Float> ) {
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
                datPos.addAll(arrayListOf(tx,ty,tz))
                datNor.addAll(arrayListOf(rx,ry,rz))
                datTxc.add(1f-1f/column.toFloat()*ii.toFloat())
                datTxc.add(1f/row.toFloat()*i.toFloat())
            }

            (0 until datPos.size/3).forEach { ii ->
                datIdx.add(ii.toShort())
            }

        }
    }

}
