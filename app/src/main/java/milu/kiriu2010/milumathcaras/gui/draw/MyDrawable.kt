package milu.kiriu2010.milumathcaras.gui.draw

import android.graphics.drawable.Drawable

abstract class MyDrawable: Drawable()
    , CalculationCallback
    , TouchCallback {
    // ---------------------------------------
    // Drawableの更新を再開・停止に使うフラグ
    // ---------------------------------------
    var isPaused = false

    // -------------------------------------
    // Drawableの更新を再開
    // -------------------------------------
    fun resume() {
        isPaused = false
    }

    // -------------------------------------
    // Drawableの更新を停止
    // -------------------------------------
    fun pause() {
        isPaused = true
    }

    // -------------------------------------
    // TouchCallback
    // タッチしたポイントを受け取る
    // -------------------------------------
    override fun receiveTouchPoint(x: Float, y: Float) {}
}
