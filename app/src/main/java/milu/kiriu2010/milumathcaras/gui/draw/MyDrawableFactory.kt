package milu.kiriu2010.milumathcaras.gui.draw

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.complex.julia.Julia01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.complex.mandelbrot.Mandelbrot01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid.Cycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.epicycloid.Epicycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.hypocycloid.Hypocycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.LogarithmicSpiral02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid.Trochoid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.dragon.DragonCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.hilbert.HilbertCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.koch.KochSnowflake01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.sierpinski.SierpinskiCarpet01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.sierpinski.SierpinskiTriangle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.takagi.TakagiCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.tree.TreeCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon.PolygonInPolygon01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle.TriangleExile01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.sine.SineWave01Drawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)(k=1.0)
                DrawDataID.ID_CYCLOID_001 -> Cycloid01Drawable()
                // トロコイド曲線(trochoid)(k=2.0)
                DrawDataID.ID_TROCHOID_002 -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=-2.0)
                DrawDataID.ID_TROCHOID_003 -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=0.5)
                DrawDataID.ID_TROCHOID_004 -> Trochoid01Drawable()
                // 対数螺旋(描画点を回転させる方法)
                //DrawDataID.ID_LOGARITHMIC_SPIRAL_010 -> LogarithmicSpiral01Drawable()
                // 対数螺旋(画像を回転させる方法)
                DrawDataID.ID_LOGARITHMIC_SPIRAL_010 -> LogarithmicSpiral02Drawable()
                // カージオイド曲線(cardioid)(k=1.0)
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
                // 三芒形/三尖形(deltoid)(k=3.0)
                DrawDataID.ID_DELTOID_031 -> Hypocycloid01Drawable()
                // アステロイド曲線(asteroid)(k=4.0)
                DrawDataID.ID_ASTROID_032 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                DrawDataID.ID_HYPO_CYCLOID_033 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                DrawDataID.ID_HYPO_CYCLOID_034 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                DrawDataID.ID_HYPO_CYCLOID_035 -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                DrawDataID.ID_HYPO_CYCLOID_036 -> Hypocycloid01Drawable()
                // サイン波
                DrawDataID.ID_SINE_WAVE_040 -> SineWave01Drawable()
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
                // 三角形でEXILE
                DrawDataID.ID_300_TRIANGLE_EXILE -> TriangleExile01Drawable()
                // 三角形in四角形
                DrawDataID.ID_301_3_IN_4 -> PolygonInPolygon01Drawable()
                // 三角形in五角形
                DrawDataID.ID_302_3_IN_5 -> PolygonInPolygon01Drawable()
                // 三角形in六角形
                DrawDataID.ID_303_3_IN_6 -> PolygonInPolygon01Drawable()
                // 三角形in七角形
                DrawDataID.ID_304_3_IN_7 -> PolygonInPolygon01Drawable()
                // 三角形in八角形
                DrawDataID.ID_305_3_IN_8 -> PolygonInPolygon01Drawable()
                // 三角形in九角形
                DrawDataID.ID_306_3_IN_9 -> PolygonInPolygon01Drawable()
                // 三角形in十角形
                DrawDataID.ID_307_3_IN_10 -> PolygonInPolygon01Drawable()
                // 四角形in五角形
                DrawDataID.ID_311_4_IN_5 -> PolygonInPolygon01Drawable()
                // 四角形in六角形
                DrawDataID.ID_312_4_IN_6 -> PolygonInPolygon01Drawable()
                // 四角形in七角形
                DrawDataID.ID_313_4_IN_7 -> PolygonInPolygon01Drawable()
                // 四角形in八角形
                DrawDataID.ID_314_4_IN_8 -> PolygonInPolygon01Drawable()
                // 四角形in九角形
                DrawDataID.ID_315_4_IN_9 -> PolygonInPolygon01Drawable()
                // 四角形in十角形
                DrawDataID.ID_316_4_IN_10 -> PolygonInPolygon01Drawable()
                // 五角形in六角形
                DrawDataID.ID_321_5_IN_6 -> PolygonInPolygon01Drawable()
                // 五角形in七角形
                DrawDataID.ID_322_5_IN_7 -> PolygonInPolygon01Drawable()
                // 五角形in八角形
                DrawDataID.ID_323_5_IN_8 -> PolygonInPolygon01Drawable()
                // 五角形in九角形
                DrawDataID.ID_324_5_IN_9 -> PolygonInPolygon01Drawable()
                // 五角形in十角形
                DrawDataID.ID_325_5_IN_10 -> PolygonInPolygon01Drawable()
                // 六角形in七角形
                DrawDataID.ID_331_6_IN_7 -> PolygonInPolygon01Drawable()
                // 六角形in八角形
                DrawDataID.ID_332_6_IN_8 -> PolygonInPolygon01Drawable()
                // 六角形in九角形
                DrawDataID.ID_333_6_IN_9 -> PolygonInPolygon01Drawable()
                // 六角形in十角形
                DrawDataID.ID_334_6_IN_10 -> PolygonInPolygon01Drawable()
                // 七角形in八角形
                DrawDataID.ID_341_7_IN_8 -> PolygonInPolygon01Drawable()
                // 七角形in九角形
                DrawDataID.ID_342_7_IN_9 -> PolygonInPolygon01Drawable()
                // 七角形in十角形
                DrawDataID.ID_343_7_IN_10 -> PolygonInPolygon01Drawable()
                // 八角形in九角形
                DrawDataID.ID_351_8_IN_9 -> PolygonInPolygon01Drawable()
                // 八角形in十角形
                DrawDataID.ID_352_8_IN_10 -> PolygonInPolygon01Drawable()
                // 九角形in十角形
                DrawDataID.ID_361_9_IN_10 -> PolygonInPolygon01Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}