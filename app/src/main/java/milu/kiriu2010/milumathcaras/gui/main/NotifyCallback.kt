package milu.kiriu2010.milumathcaras.gui.main

interface NotifyCallback {
    // ---------------------------------
    // 通知データを受信する
    // ---------------------------------
    fun receive(data: Float)
}
