package milu.kiriu2010.milumathcaras.gui.draw

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.color.Color1536Drawable
import milu.kiriu2010.milumathcaras.gui.draw.color.Color768DarkDrawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.julia.Julia01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.mandelbrot.Mandelbrot01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.epicycloid.Epicycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.hypocycloid.Hypocycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.lissajous.Lissajous01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.LogarithmicSpiral01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid.Trochoid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.mandelbrot.Mandelbrot00Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.dragon.DragonCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert.HilbertCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.koch.KochSnowflake01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.sierpinski.SierpinskiCarpet01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.sierpinski.SierpinskiTriangle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.takagi.TakagiCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.tree.TreeCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.Fountain01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.RandomWalk01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.UniformMotion01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon.PolygonInPolygon01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle.TriangleExile01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.sine.SineWave01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.sine.SineWaveCircle01Drawable
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)(k=1.0)
                //DrawDataID.ID_000001_CYCLOID -> Cycloid01Drawable()
                // サイクロイド曲線(cycloid)(k=1.0)
                DrawDataID.ID_000001_CYCLOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=2.0)
                DrawDataID.ID_000002_TROCHOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=-2.0)
                DrawDataID.ID_000003_TROCHOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=0.5)
                DrawDataID.ID_000004_TROCHOID -> Trochoid01Drawable()
                // 対数螺旋(描画点を回転させる方法)
                DrawDataID.ID_000010_LOGARITHMIC_SPIRAL -> LogarithmicSpiral01Drawable()
                // 対数螺旋(画像を回転させる方法)
                //DrawDataID.ID_000010_LOGARITHMIC_SPIRAL -> LogarithmicSpiral02Drawable()
                // カージオイド曲線(cardioid)(k=1.0)
                DrawDataID.ID_000021_CARDIOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=4.0)
                DrawDataID.ID_000022_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=2.1)
                DrawDataID.ID_000023_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=3.8)
                DrawDataID.ID_000024_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=5.5)
                DrawDataID.ID_000025_EPICYCLOID -> Epicycloid01Drawable()
                // エピサイクロイド曲線(k=7.2)
                DrawDataID.ID_000026_EPICYCLOID -> Epicycloid01Drawable()
                // 三芒形/三尖形(deltoid)(k=3.0)
                DrawDataID.ID_000031_DELTOID -> Hypocycloid01Drawable()
                // アステロイド曲線(asteroid)(k=4.0)
                DrawDataID.ID_000032_ASTROID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                DrawDataID.ID_000033_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                DrawDataID.ID_000034_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                DrawDataID.ID_000035_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                DrawDataID.ID_000036_HYPO_CYCLOID -> Hypocycloid01Drawable()
                // サイン波
                DrawDataID.ID_000040_SINE_WAVE -> SineWave01Drawable()
                // 円の周りを回転するサイン波
                DrawDataID.ID_000041_SINE_WAVE_CIRCLE -> SineWaveCircle01Drawable()
                // リサージュ曲線(p:q=1:2)
                DrawDataID.ID_000050_LISSAJOUS_CURVE_1_2 -> Lissajous01Drawable()
                // リサージュ曲線(p:q=3:2)
                DrawDataID.ID_000051_LISSAJOUS_CURVE_3_2 -> Lissajous01Drawable()
                // リサージュ曲線(p:q=3:4)
                DrawDataID.ID_000052_LISSAJOUS_CURVE_3_4 -> Lissajous01Drawable()
                // 高木曲線
                DrawDataID.ID_000101_TAKAGI_CURVE -> TakagiCurve01Drawable()
                // コッホ雪片
                DrawDataID.ID_000102_KOCH_SNOWFLAKE -> KochSnowflake01Drawable()
                // 樹木曲線
                DrawDataID.ID_000103_TREE_CURVE -> TreeCurve01Drawable()
                // ドラゴン曲線
                DrawDataID.ID_000104_DRAGON_CURVE -> DragonCurve01Drawable()
                // シェルピンスキーの三角形
                DrawDataID.ID_000105_SIERPINSKI_TRIANGLE -> SierpinskiTriangle01Drawable()
                // シェルピンスキーのカーペット
                DrawDataID.ID_000106_SIERPINSKI_CARPET -> SierpinskiCarpet01Drawable()
                // ヒルベルト曲線
                DrawDataID.ID_000107_HILBERT_CURVE -> HilbertCurve01Drawable()
                // マンデルブロ―集合(白黒)
                DrawDataID.ID_000200_MANDELBRO_SET -> Mandelbrot00Drawable()
                // マンデルブロ―集合(-1.5-1.0i～+0.5+1.0i)
                DrawDataID.ID_000201_MANDELBRO_SET -> Mandelbrot01Drawable()
                // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
                DrawDataID.ID_000202_MANDELBRO_SET -> Mandelbrot01Drawable()
                // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
                DrawDataID.ID_000203_MANDELBRO_SET -> Mandelbrot01Drawable()
                // ジュリア集合
                DrawDataID.ID_000251_JULIA_SET -> Julia01Drawable()
                // 三角形でEXILE
                DrawDataID.ID_000300_TRIANGLE_EXILE -> TriangleExile01Drawable()
                // 三角形in四角形
                DrawDataID.ID_000301_3_IN_4 -> PolygonInPolygon01Drawable()
                // 三角形in五角形
                DrawDataID.ID_000302_3_IN_5 -> PolygonInPolygon01Drawable()
                // 三角形in六角形
                DrawDataID.ID_000303_3_IN_6 -> PolygonInPolygon01Drawable()
                // 三角形in七角形
                DrawDataID.ID_000304_3_IN_7 -> PolygonInPolygon01Drawable()
                // 三角形in八角形
                DrawDataID.ID_000305_3_IN_8 -> PolygonInPolygon01Drawable()
                // 三角形in九角形
                DrawDataID.ID_000306_3_IN_9 -> PolygonInPolygon01Drawable()
                // 三角形in十角形
                DrawDataID.ID_000307_3_IN_10 -> PolygonInPolygon01Drawable()
                // 四角形in五角形
                DrawDataID.ID_000311_4_IN_5 -> PolygonInPolygon01Drawable()
                // 四角形in六角形
                DrawDataID.ID_000312_4_IN_6 -> PolygonInPolygon01Drawable()
                // 四角形in七角形
                DrawDataID.ID_000313_4_IN_7 -> PolygonInPolygon01Drawable()
                // 四角形in八角形
                DrawDataID.ID_000314_4_IN_8 -> PolygonInPolygon01Drawable()
                // 四角形in九角形
                DrawDataID.ID_000315_4_IN_9 -> PolygonInPolygon01Drawable()
                // 四角形in十角形
                DrawDataID.ID_000316_4_IN_10 -> PolygonInPolygon01Drawable()
                // 五角形in六角形
                DrawDataID.ID_000321_5_IN_6 -> PolygonInPolygon01Drawable()
                // 五角形in七角形
                DrawDataID.ID_000322_5_IN_7 -> PolygonInPolygon01Drawable()
                // 五角形in八角形
                DrawDataID.ID_000323_5_IN_8 -> PolygonInPolygon01Drawable()
                // 五角形in九角形
                DrawDataID.ID_000324_5_IN_9 -> PolygonInPolygon01Drawable()
                // 五角形in十角形
                DrawDataID.ID_000325_5_IN_10 -> PolygonInPolygon01Drawable()
                // 六角形in七角形
                DrawDataID.ID_000331_6_IN_7 -> PolygonInPolygon01Drawable()
                // 六角形in八角形
                DrawDataID.ID_000332_6_IN_8 -> PolygonInPolygon01Drawable()
                // 六角形in九角形
                DrawDataID.ID_000333_6_IN_9 -> PolygonInPolygon01Drawable()
                // 六角形in十角形
                DrawDataID.ID_000334_6_IN_10 -> PolygonInPolygon01Drawable()
                // 七角形in八角形
                DrawDataID.ID_000341_7_IN_8 -> PolygonInPolygon01Drawable()
                // 七角形in九角形
                DrawDataID.ID_000342_7_IN_9 -> PolygonInPolygon01Drawable()
                // 七角形in十角形
                DrawDataID.ID_000343_7_IN_10 -> PolygonInPolygon01Drawable()
                // 八角形in九角形
                DrawDataID.ID_000351_8_IN_9 -> PolygonInPolygon01Drawable()
                // 八角形in十角形
                DrawDataID.ID_000352_8_IN_10 -> PolygonInPolygon01Drawable()
                // 九角形in十角形
                DrawDataID.ID_000361_9_IN_10 -> PolygonInPolygon01Drawable()
                // 等速度運動
                DrawDataID.ID_000401_NATURE_UNFORM_MOTION -> UniformMotion01Drawable()
                // 噴水
                DrawDataID.ID_000402_NATURE_FOUNTAIN -> Fountain01Drawable()
                // ランダムウォーク
                DrawDataID.ID_000403_NATURE_RANDOM_WALK -> RandomWalk01Drawable()
                // 1536色
                DrawDataID.ID_000500_COLOR_1536 -> Color1536Drawable()
                // 768色(暗色)
                DrawDataID.ID_000501_COLOR_768_DARK -> Color768DarkDrawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}