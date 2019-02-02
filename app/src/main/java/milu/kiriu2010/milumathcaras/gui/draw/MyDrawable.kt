package milu.kiriu2010.milumathcaras.gui.draw

import android.graphics.drawable.Drawable

abstract class MyDrawable: Drawable()
    , CalculationCallback
    , TouchCallback {

    // -------------------------------------
    // TouchCallback
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(x: Float, y: Float) {}
}
