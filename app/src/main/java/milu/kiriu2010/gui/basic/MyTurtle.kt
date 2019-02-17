package milu.kiriu2010.gui.basic

import milu.kiriu2010.math.MyMathUtil


data class MyTurtle(
    // 現在位置
    var p: MyPointF = MyPointF(),
    // 移動距離
    var d: Float = 0f,
    // 現在角度
    var t: Float = 0f,
    // 位置リスト
    var pLst: MutableList<MyPointF> = mutableListOf()
) {

    // ---------------------------
    // 初期化
    // ---------------------------
    fun clear() {
        pLst.clear()
        d = 0f
        t = 0f
    }

    // ---------------------------
    // 位置を追加
    // ---------------------------
    fun addPoint(p0: MyPointF): MyTurtle {
        pLst.add(p0)
        p = p0
        return this;
    }

    // ---------------------------
    // 向きを変更
    // ---------------------------
    fun turn(angle: Float): MyTurtle {
        t = MyMathUtil.correctAngle(t+angle)
        return this
    }

    // ---------------------------
    // 移動
    // ---------------------------
    fun move(): MyTurtle {
        // 移動後の位置
        p = p.copy().apply {
            x = x + d*MyMathUtil.cosf(t)
            y = y + d*MyMathUtil.sinf(t)
        }
        addPoint(p)
        return this
    }
}
