package milu.kiriu2010.milumathcaras.gui.main

import milu.kiriu2010.milumathcaras.entity.DrawData

interface DrawDataCallback {
    // -------------------------------------
    // 描画データを描画する
    // -------------------------------------
    fun draw( drawData: DrawData )
}