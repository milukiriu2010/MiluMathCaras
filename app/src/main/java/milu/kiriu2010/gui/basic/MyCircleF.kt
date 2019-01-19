package milu.kiriu2010.gui.basic

import kotlin.math.PI
import kotlin.math.acos

// 円
data class MyCircleF(
    // 位置
    var p: MyPointF,
    // 半径
    var r: Float,
    // 速度
    var v: MyVectorF
) {
    // 次の地点へ移動
    fun move(): MyCircleF {
        p.x += v.x
        p.y += v.y

        return this
    }

    // ---------------------------------------------------------------
    // 境界に達していたら、反射する
    // ---------------------------------------------------------------
    // x0: 境界の左端
    // y0: 境界の下端
    // x1: 境界の右端
    // y1: 境界の上端
    // ---------------------------------------------------------------
    fun checkBorder(x0: Float, y0: Float, x1: Float, y1: Float): MyCircleF {
        // "円の左端"が"境界の左端"に達していた場合
        if ( (p.x-r) < x0 ) {
            p.x = r
            v.x = -1 * v.x
        }
        // "円の右端"が"境界の右端"に達していた場合
        else if ( (p.x+r) >= x1 ) {
            p.x = x1-r
            v.x = -1 * v.x
        }

        // "円の下端"が"境界の下端"に達していた場合
        if ( (p.y-r) < y0 ) {
            p.y = r
            v.y = -1 * v.y
        }
        // "円の上端"が"境界の上端"に達していた場合
        else if ( (p.y+r) >= y1 ) {
            p.y = y1-r
            v.y = -1 * v.y
        }

        return this
    }

    // ---------------------------------------------------------------
    // 衝突していたら、進行方向を変える
    // ---------------------------------------------------------------
    fun checkCollision( obj: MyCircleF ): MyCircleF {
        // "円の中心同士の距離"の２乗
        val lenC = (p.x-obj.p.x)*(p.x-obj.p.x)+(p.y-obj.p.y)*(p.y-obj.p.y)
        // "円の半径を足し合わせた距離"の２乗
        val lenR = (r+obj.r)*(r+obj.r)

        // 衝突していない場合、何もしない
        if ( lenC > lenR ) return this

        // objからみた自身の位置ベクトルの単位ベクトル(反射の法線ベクトル)
        val vn = MyVectorF(p.x - obj.p.x,p.y - obj.p.y).normalize()

        // 法線ベクトルの長さ
        val lenVN = vn.mag()
        // 自身の速度
        val lenV = v.mag()
        // "法線ベクトル"と"自身の速度ベクトル"の内積
        val dot = v.dot(vn)
        // "法線ベクトル"と"自身の速度ベクトル"のなす角度
        //   ラジアン(0～π)が返ってくる
        val angle = acos(dot/(lenVN*lenV))

        // "法線ベクトル"と"自身の速度ベクトル"のなす角度
        //   90度以上の場合 → 反射する
        //   90度以内の場合 → 反射しない(同じ速度ベクトルのまま進む)
        if ( 2f*angle < PI ) return this

        // 進行方向を変える
        // -----------------------------------------
        // 反射ベクトル
        // R = F + 2(-F*N)*N
        // -----------------------------------------
        v.sum(vn.multiply(-2f*dot))

        return this
    }
}