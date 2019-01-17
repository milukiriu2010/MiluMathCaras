package milu.kiriu2010.gui.basic

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
    fun move() {
        p.x += v.x
        p.y += v.y
    }
}