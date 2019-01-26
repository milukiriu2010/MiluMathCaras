package milu.kiriu2010.milumathcaras.gui.draw

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.color.Color1536Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.julia.Julia01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.mandelbrot.Mandelbrot01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid.Cycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.epicycloid.Epicycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.hypocycloid.Hypocycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.lissajous.Lissajous01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.LogarithmicSpiral01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.LogarithmicSpiral02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid.Trochoid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.mandelbrot.Mandelbrot00Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.dragon.DragonCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert.HilbertCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.koch.KochSnowflake01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.sierpinski.SierpinskiCarpet01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.sierpinski.SierpinskiTriangle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.takagi.TakagiCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.tree.TreeCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.UniformMotion01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon.PolygonInPolygon01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle.TriangleExile01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.sine.SineWave01Drawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)(k=1.0)
                //DrawDataID.ID_001_CYCLOID -> Cycloid01Drawable()
                // サイクロイド曲線(cycloid)(k=1.0)
                DrawDataID.ID_001_CYCLOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=2.0)
                DrawDataID.ID_002_TROCHOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=-2.0)
                DrawDataID.ID_003_TROCHOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=0.5)
                DrawDataID.ID_004_TROCHOID -> Trochoid01Drawable()
                // 対数螺旋(描画点を回転させる方法)
                DrawDataID.ID_010_LOGARITHMIC_SPIRAL -> LogarithmicSpiral01Drawable()
                // 対数螺旋(画像を回転させる方法)
                //DrawDataID.ID_010_LOGARITHMIC_SPIRAL -> LogarithmicSpiral02Drawable()
                // カージオイド曲線(cardioid)(k=1.0)
                DrawDataID.ID_021_CARDIOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=4.0)
                DrawDataID.ID_022_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=2.1)
                DrawDataID.ID_023_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=3.8)
                DrawDataID.ID_024_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=5.5)
                DrawDataID.ID_025_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=7.2)
                DrawDataID.ID_026_EPICYCLOID -> Epicycloid01Drawable()
                // 三芒形/三尖形(deltoid)(k=3.0)
                DrawDataID.ID_031_DELTOID -> Hypocycloid01Drawable()
                // アステロイド曲線(asteroid)(k=4.0)
                DrawDataID.ID_032_ASTROID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                DrawDataID.ID_033_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                DrawDataID.ID_034_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                DrawDataID.ID_035_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                DrawDataID.ID_036_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // サイン波
                DrawDataID.ID_040_SINE_WAVE -> SineWave01Drawable()
                // リサージュ曲線(p:q=1:2)
                DrawDataID.ID_050_LISSAJOUS_CURVE_1_2 -> Lissajous01Drawable()
                // リサージュ曲線(p:q=3:2)
                DrawDataID.ID_051_LISSAJOUS_CURVE_3_2 -> Lissajous01Drawable()
                // リサージュ曲線(p:q=3:4)
                DrawDataID.ID_052_LISSAJOUS_CURVE_3_4 -> Lissajous01Drawable()
                // 高木曲線
                DrawDataID.ID_101_TAKAGI_CURVE -> TakagiCurve01Drawable()
                // コッホ雪片
                DrawDataID.ID_102_KOCH_SNOWFLAKE -> KochSnowflake01Drawable()
                // 樹木曲線
                DrawDataID.ID_103_TREE_CURVE -> TreeCurve01Drawable()
                // ドラゴン曲線
                DrawDataID.ID_104_DRAGON_CURVE -> DragonCurve01Drawable()
                // シェルピンスキーの三角形
                DrawDataID.ID_105_SIERPINSKI_TRIANGLE -> SierpinskiTriangle01Drawable()
                // シェルピンスキーのカーペット
                DrawDataID.ID_106_SIERPINSKI_CARPET -> SierpinskiCarpet01Drawable()
                // ヒルベルト曲線
                DrawDataID.ID_107_HILBERT_CURVE -> HilbertCurve01Drawable()
                // マンデルブロ―集合(白黒)
                DrawDataID.ID_200_MANDELBRO_SET -> Mandelbrot00Drawable()
                // マンデルブロ―集合(-1.5-1.0i～+0.5+1.0i)
                DrawDataID.ID_201_MANDELBRO_SET -> Mandelbrot01Drawable()
                // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
                DrawDataID.ID_202_MANDELBRO_SET -> Mandelbrot01Drawable()
                // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
                DrawDataID.ID_203_MANDELBRO_SET -> Mandelbrot01Drawable()
                // ジュリア集合
                DrawDataID.ID_251_JULIA_SET -> Julia01Drawable()
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
                // 等速度運動
                DrawDataID.ID_401_NATURE_UNFORM_MOTION -> UniformMotion01Drawable()
                // 1536色
                DrawDataID.ID_500_COLOR_1536 -> Color1536Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}