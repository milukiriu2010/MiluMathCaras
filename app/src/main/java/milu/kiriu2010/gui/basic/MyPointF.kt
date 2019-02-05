package milu.kiriu2010.gui.basic

import milu.kiriu2010.math.MyMathUtil
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// X,Y座標
data class MyPointF(
        var x: Float = 0f,
        var y: Float = 0f
) {
    // 距離
    fun distance(p: MyPointF): Float {
        val dx = x - p.x
        val dy = y - p.y
        return sqrt(dx*dx+dy*dy)
    }

    // 大きさ
    fun magnitude(): Float {
        return sqrt(x*x+y*y)
    }

    fun plus(p: MyPointF): MyPointF {
        x = x + p.x
        y = y + p.y
        return this
    }

    fun subtract(p: MyPointF): MyPointF {
        x = x - p.x
        y = y - p.y
        return this
    }

    fun rotate(angleDv: Float): MyPointF {
        val mag = magnitude()
        val angle = MyMathUtil.getAngle(MyPointF(0f,0f),this)
        x = mag * cos((angle+angleDv)*PI/180f).toFloat()
        y = mag * sin((angle+angleDv)*PI/180f).toFloat()
        return this
    }
}
