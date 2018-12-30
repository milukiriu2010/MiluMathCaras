package milu.kiriu2010.milumathcaras.gui.main

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.curve.cycloid.Cycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.curve.hypocycloid.Hypocycloid01Drawable

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)
                DrawDataID.ID_CYCLOID_01 -> Cycloid01Drawable()
                // 三芒形/三尖形(deltoid)
                DrawDataID.ID_DELTOID_02 -> Hypocycloid01Drawable()
                // アステロイド曲線(asteroid)
                DrawDataID.ID_ASTROID_03 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                DrawDataID.ID_HYPO_CYCLOID_04 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                DrawDataID.ID_HYPO_CYCLOID_05 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                DrawDataID.ID_HYPO_CYCLOID_06 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                DrawDataID.ID_HYPO_CYCLOID_07 -> Hypocycloid01Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}