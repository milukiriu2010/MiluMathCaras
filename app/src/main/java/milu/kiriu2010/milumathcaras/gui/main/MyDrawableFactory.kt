package milu.kiriu2010.milumathcaras.gui.main

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.curve.cycloid.Cycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.curve.hypocycloid.Hypocycloid01Drawable

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線
                DrawDataID.ID_CYCLOID_01 -> Cycloid01Drawable()
                // ハイポサイクロイド曲線
                DrawDataID.ID_HYPO_CYCLOID_02 -> Hypocycloid01Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}