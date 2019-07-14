package milu.kiriu2010.milumathcaras.gui.draw.d2

import android.view.MotionEvent

interface TouchCallback {
    // -------------------------------------
    // タッチしたポイントを受け取る
    // -------------------------------------
    fun receiveTouchPoint(event: MotionEvent)
}
