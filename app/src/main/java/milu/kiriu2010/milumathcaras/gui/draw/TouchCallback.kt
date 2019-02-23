package milu.kiriu2010.milumathcaras.gui.draw

import android.view.MotionEvent

interface TouchCallback {
    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    fun receiveTouchPoint(x: Float, y: Float, event: MotionEvent)
}
