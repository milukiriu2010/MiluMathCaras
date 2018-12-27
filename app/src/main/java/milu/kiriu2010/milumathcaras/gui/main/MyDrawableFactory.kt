package milu.kiriu2010.milumathcaras.gui.main

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.curve.cycloid.Cycloid01Drawable

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド
                DrawDataID.ID_CYCLOID_01 -> Cycloid01Drawable()
                else -> throw RuntimeException("No MyDrawable")
            }

            myDrawable.setNotifyCallback(notifyCallback!!)

            return myDrawable
        }
    }
}