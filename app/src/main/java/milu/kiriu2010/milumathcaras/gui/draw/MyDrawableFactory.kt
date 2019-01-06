package milu.kiriu2010.milumathcaras.gui.draw

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.complex.julia.Julia01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.complex.mandelbrot.Mandelbrot01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.cycloid.Cycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.epicycloid.Epicycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.hypocycloid.Hypocycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.LogarithmicSpiral01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.dragon.DragonCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.hilbert.HilbertCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.koch.KochSnowflake01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.sierpinski.SierpinskiCarpet01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.sierpinski.SierpinskiTriangle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.takagi.TakagiCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.tree.TreeCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)
                DrawDataID.ID_CYCLOID_001 -> Cycloid01Drawable()
                // 対数螺旋
                DrawDataID.ID_LOGARITHMIC_SPIRAL_002 -> LogarithmicSpiral01Drawable()
                // カージオイド曲線(cardioid)
                DrawDataID.ID_CARDIOID_021 -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=4.0)
                DrawDataID.ID_EPICYCLOID_022 -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=2.1)
                DrawDataID.ID_EPICYCLOID_023 -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=3.8)
                DrawDataID.ID_EPICYCLOID_024 -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=5.5)
                DrawDataID.ID_EPICYCLOID_025 -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=7.2)
                DrawDataID.ID_EPICYCLOID_026 -> Epicycloid01Drawable()
                // 三芒形/三尖形(deltoid)
                DrawDataID.ID_DELTOID_031 -> Hypocycloid01Drawable()
                // アステロイド曲線(asteroid)
                DrawDataID.ID_ASTROID_032 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                DrawDataID.ID_HYPO_CYCLOID_033 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                DrawDataID.ID_HYPO_CYCLOID_034 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                DrawDataID.ID_HYPO_CYCLOID_035 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                DrawDataID.ID_HYPO_CYCLOID_036 -> Hypocycloid01Drawable()
                // 高木曲線
                DrawDataID.ID_TAKAGI_CURVE_101 -> TakagiCurve01Drawable()
                // コッホ雪片
                DrawDataID.ID_KOCH_SNOWFLAKE_102 -> KochSnowflake01Drawable()
                // 樹木曲線
                DrawDataID.ID_TREE_CURVE_103 -> TreeCurve01Drawable()
                // ドラゴン曲線
                DrawDataID.ID_DRAGON_CURVE_104 -> DragonCurve01Drawable()
                // シェルピンスキーの三角形
                DrawDataID.ID_SIERPINSKI_TRIANGLE_105 -> SierpinskiTriangle01Drawable()
                // シェルピンスキーのカーペット
                DrawDataID.ID_SIERPINSKI_CARPET_106 -> SierpinskiCarpet01Drawable()
                // ヒルベルト曲線
                DrawDataID.ID_HILBERT_CURVE_107 -> HilbertCurve01Drawable()
                // マンデルブロ―集合
                DrawDataID.ID_MANDELBRO_SET_201 -> Mandelbrot01Drawable()
                // ジュリア集合
                DrawDataID.ID_JULIA_SET_251 -> Julia01Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}