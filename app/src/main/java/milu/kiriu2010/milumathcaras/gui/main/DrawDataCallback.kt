package milu.kiriu2010.milumathcaras.gui.main

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.MenuData

interface DrawDataCallback {
    // -------------------------------------
    // 描画データを描画する
    // -------------------------------------
    fun draw( drawData: DrawData )

    // -------------------------------------
    // 描画データ一覧を表示する
    // -------------------------------------
    fun showLst( menuData: MenuData )
}
