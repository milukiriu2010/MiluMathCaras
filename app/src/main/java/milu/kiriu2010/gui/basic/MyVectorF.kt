package milu.kiriu2010.gui.basic

import kotlin.math.sqrt

// ベクトル
// ・速度
data class MyVectorF(
    // X方向
    var x: Float,
    // Y方向
    var y: Float
) {
    // ベクトルの大きさ
    fun mag(): Float {
        return sqrt(x*x+y*y)
    }

    // 単位ベクトルに変換
    fun normalize(): MyVectorF {
        val mag = mag()
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

    // ベクトル同士を足す
    fun sum(v: MyVectorF): MyVectorF {
        x += v.x
        y += v.y
        return this
    }

    // ベクトルの大きさを変える
    fun multiply(d: Float): MyVectorF {
        x *= d
        y *= d
        return this
    }
}
