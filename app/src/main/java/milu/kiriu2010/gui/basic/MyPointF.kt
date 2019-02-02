package milu.kiriu2010.gui.basic

import kotlin.math.sqrt

// X,Y座標
data class MyPointF(
        var x: Float,
        var y: Float
) {
    // 距離
    fun distance(p: MyPointF): Float {
        val dx = x - p.x
        val dy = y - p.y
        return sqrt(dx*dx+dy*dy)
    }
}
