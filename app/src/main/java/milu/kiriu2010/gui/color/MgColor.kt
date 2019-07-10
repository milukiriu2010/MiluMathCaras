package milu.kiriu2010.gui.color

import kotlin.math.floor

// -------------------------------------
// 色生成
// -------------------------------------
// 2019.06.11 範囲外の場合、白を返却
// 2019.07.10 warning消す
// -------------------------------------
class MgColor {

    companion object {
        // ---------------------------------------------------------
        // HSVカラー取得用関数
        // ---------------------------------------------------------
        // HSV では、色相は 0 ～ 360 の範囲に収まっている必要がありますが、
        // それ以上に大きな数値を指定しても計算が破綻しないように関数内で処理しています。
        // また、彩度や明度に不正な値が指定されている場合には正しい値を返しませんので注意しましょう。
        // 彩度・明度・透明度はいずれも 0 ～ 1 の範囲で指定してください
        // ---------------------------------------------------------
        // h: 色相(0-360)
        // s: 彩度(0.0-1.0)
        // v: 明度(0.0-1.0)
        // a: 透明度(0.0-1.0)
        // ---------------------------------------------------------
        // https://wgld.org/sitemap.html
        // ---------------------------------------------------------
        fun hsva(h: Int, s: Float, v: Float, a: Float): ArrayList<Float> {
            if ( (s > 1f) or (v > 1f) or (a > 1f) ) return arrayListOf<Float>(1f,1f,1f,1f)

            val color = arrayListOf<Float>()
            val th = h%360
            val i = floor(th.toFloat()/60f)
            val f = th.toFloat()/60f - i
            val m = v*(1f-s)
            val n = v*(1f-s*f)
            val k = v*(1-s*(1-f))
            if ( ((s>0f) == false) and ((s<0f) == false) ) {
                color.addAll(arrayListOf<Float>(v,v,v,a))
            }
            else {
                var r = arrayListOf(v,n,m,m,k,v)
                var g = arrayListOf(k,v,v,n,m,m)
                var b = arrayListOf(m,m,k,v,v,n)
                color.addAll(arrayListOf(r[i.toInt()],g[i.toInt()],b[i.toInt()],a))
            }
            return color
        }
    }
}
