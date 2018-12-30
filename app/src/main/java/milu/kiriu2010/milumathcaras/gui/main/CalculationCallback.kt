package milu.kiriu2010.milumathcaras.gui.main

interface CalculationCallback {
    // -------------------------------------
    // 描画に使うデータを計算開始
    // -------------------------------------
    // 第１引数
    //   true :描画スレッドを起動する
    //   false:描画スレッドを起動しない
    // 第２引数:可変長
    //   呼び出し側で自由に使うための変数
    // -------------------------------------
    fun calStart(isKickThread: Boolean = true, vararg values: Float)

    // -------------------------------------
    // 描画ビューを閉じる際,呼び出す後処理
    // -------------------------------------
    fun calStop()

    // -------------------------------------
    // 描画中に呼び出すコールバックを設定
    // -------------------------------------
    fun setNotifyCallback(notifyCallback: NotifyCallback)
}
