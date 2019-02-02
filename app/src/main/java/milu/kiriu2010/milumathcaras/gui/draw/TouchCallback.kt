package milu.kiriu2010.milumathcaras.gui.draw

interface TouchCallback {
    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    fun receiveTouchPoint(x: Float, y: Float)
}
