package milu.kiriu2010.gui.basic

import java.lang.RuntimeException
import kotlin.math.sqrt

// ベクトル
// ・速度
data class MyVectorF(
    // X方向
    var x: Float = 0f,
    // Y方向
    var y: Float = 0f
) {
    // ベクトルの大きさ
    fun magnitude(): Float {
        return sqrt(x*x+y*y)
    }

    // ベクトルの大きさの２乗
    fun magnitude2(): Float {
        return x*x+y*y
    }

    // ベクトル差分の大きさ
    fun magByDiff(v: MyVectorF): Float {
        val dx = x - v.x
        val dy = y - v.y
        return sqrt(dx*dx+dy*dy)
    }

    // --------------------------------------
    // 線形補完
    // --------------------------------------
    //  this : 補完元ベクトル
    //  v    : 補完先ベクトル
    //  ratio: 補完の割合(0.0-1.0)
    //         0.0: 補完元ベクトルと一致
    //         1.0: 補完先ベクトルと一致
    // --------------------------------------
    fun lerp(v: MyVectorF, ratio: Float ) {
        val dx = v.x - x
        x = x + dx*ratio

        val dy = v.y - y
        y = y + dy*ratio
    }

    // 単位ベクトルに変換
    fun normalized(): MyVectorF {
        val mag = magnitude()
        if (mag != 0f) {
            x /= mag
            y /= mag
        }
        return this
    }

    // 内積を求める
    fun dot(v: MyVectorF): Float {
        return x*v.x+y*v.y
    }

    // ------------------------------------------------
    // 反射ベクトルを求める
    // ------------------------------------------------
    // R = F + 2(-F*N)*N
    //   F  : 自身のベクトル
    //   N  : 法線ベクトル
    //   F*N: "自身のベクトル"と"法線ベクトル"の内積
    // ------------------------------------------------
    // vn: 法線ベクトル
    // ------------------------------------------------
    fun reflect(vn: MyVectorF): MyVectorF {
        // "法線ベクトル"と"自身の速度ベクトル"の内積
        val dot = dot(vn)

        // 反射ベクトルに変換
        plus(vn.multiply(-2f*dot))

        return this
    }

    // ベクトル同士を足す
    fun plus(v: MyVectorF): MyVectorF {
        x += v.x
        y += v.y
        return this
    }

    // ベクトル同士を引く
    fun subtract(v: MyVectorF): MyVectorF {
        x -= v.x
        y -= v.y
        return this
    }

    // ベクトルの大きさを変える
    fun multiply(d: Float): MyVectorF {
        x *= d
        y *= d
        return this
    }

    // ベクトルの大きさを変える
    fun divide(d: Float): MyVectorF {
        if ( d == 0f ) {
            throw RuntimeException("Not allowed to divide by 0.")
        }
        x /= d
        y /= d
        return this
    }

    // リミット
    fun limit(mag: Float): MyVectorF {
        val magOrg = magnitude()

        if (magOrg <= mag) {
            return this
        }
        else if ( magOrg == 0f ) {
            return this
        }
        else {
            multiply(mag/magOrg)
            return this
        }
    }
}
