package milu.kiriu2010.milumathcaras.gui.draw

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.complex.mandelbrot.Mandelbrot01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.cycloid.Cycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.hypocycloid.Hypocycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.koch.KochSnowflake01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.takagi.TakagiCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.tree.TreeCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)
                DrawDataID.ID_CYCLOID_001 -> Cycloid01Drawable()
                // 三芒形/三尖形(deltoid)
                DrawDataID.ID_DELTOID_032 -> Hypocycloid01Drawable()
                // アステロイド曲線(asteroid)
                DrawDataID.ID_ASTROID_033 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                DrawDataID.ID_HYPO_CYCLOID_034 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                DrawDataID.ID_HYPO_CYCLOID_035 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                DrawDataID.ID_HYPO_CYCLOID_036 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                DrawDataID.ID_HYPO_CYCLOID_037 -> Hypocycloid01Drawable()
                // 高木曲線
                DrawDataID.ID_TAKAGI_CURVE_101 -> TakagiCurve01Drawable()
                // コッホ雪片
                DrawDataID.ID_KOCH_SNOWFLAKE_102 -> KochSnowflake01Drawable()
                // 樹木曲線
                DrawDataID.ID_TREE_CURVE_103 -> TreeCurve01Drawable()
                // マンデルブロ―集合
                DrawDataID.ID_MANDELBRO_SET_201 -> Mandelbrot01Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}