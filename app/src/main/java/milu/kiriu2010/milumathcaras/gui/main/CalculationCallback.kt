package milu.kiriu2010.milumathcaras.gui.main

interface CalculationCallback {
    // -------------------------------------
    // 描画に使うデータを計算する
    // -------------------------------------
    fun cal(n: Int = 0)

    // -------------------------------------
    // 描画中に呼び出すコールバックを設定
    // -------------------------------------
    fun setNotifyCallback(notifyCallback: NotifyCallback)

    // -------------------------------------
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    fun postProc()
}
