package milu.kiriu2010.milumathcaras.gui.draw

import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.circle.circles.*
import milu.kiriu2010.milumathcaras.gui.draw.circle.morph.Circle2SqaureMorph01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.circle.morph.Circle2SqaureMorph02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.circle.morph.SunMorph01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.circle.tile.Circle2SqaureTile01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.circle.tile.RotateCircleTile01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.circle.tile.SlideCircleTile01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.circle.tile.XmasTreeCircle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.color.Color1536Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.color.Color768DarkDrawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.julia.Julia01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.mandelbrot.Mandelbrot01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.epicycloid.Epicycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.hypocycloid.Hypocycloid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.lemniscate.*
import milu.kiriu2010.milumathcaras.gui.draw.curve.lissajous.Lissajous01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.limacon.Limacon01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.limacon.Limacon02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.limacon.Limacon03Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.mix.CausticCircle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.mix.Cochleoid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.mix.CyclicHarmonic01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.mix.TrefleHabenicht01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.special.*
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.fermat.FermatSpiral01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.involute.InvoluteCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.involute.InvoluteCurve02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.logarithmic.LogarithmicSpiral01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.logarithmic.LogarithmicSpiralCircle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.logarithmic.LogarithmicSpiralCircle02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.spiral.logarithmic.LogarithmicSpiralTriangle01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.curve.trochoid.Trochoid01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.complex.mandelbrot.Mandelbrot00Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.dragon.DragonCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.dragon.LevyCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.gosper.*
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert.HilbertCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert.HilbertCurve02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert.MooreCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.hilbert.MooreCurve02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.koch.*
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.sierpinski.*
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.takagi.TakagiCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.tree.TreeCurve01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.tree.TreeCurve02Drawable
import milu.kiriu2010.milumathcaras.gui.draw.fractal.recursion.tree.TreeCurve03Drawable
import milu.kiriu2010.milumathcaras.gui.draw.illusion.SteppingFeet01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.illusion.StereoKineticEffect01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.forces.FrictionEffect01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.forces.MassEffect01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.vectors.AccelerateTowardsTouchPoint01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.vectors.Fountain01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.vectors.RandomWalk01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.nature.vectors.UniformMotion01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.polygon.*
import milu.kiriu2010.milumathcaras.gui.draw.polygon.square.Square2Cross01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.polygon.tile.*
import milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle.*
import milu.kiriu2010.milumathcaras.gui.draw.polygon.triangle.center.*
import milu.kiriu2010.milumathcaras.gui.draw.wave.color.RgbWeaveWave01Drawable
import milu.kiriu2010.milumathcaras.gui.draw.wave.sine.*
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

class MyDrawableFactory {
    companion object {
        fun createInstance(id: DrawDataID,notifyCallback: NotifyCallback? = null): MyDrawable {
            val myDrawable = when ( id ) {
                // サイクロイド曲線(cycloid)(k=1.0)
                DrawDataID.ID_000001_CYCLOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=2.0)
                DrawDataID.ID_000002_TROCHOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=-2.0)
                DrawDataID.ID_000003_TROCHOID -> Trochoid01Drawable()
                // トロコイド曲線(trochoid)(k=0.5)
                DrawDataID.ID_000004_TROCHOID -> Trochoid01Drawable()
                // レムニスケート曲線
                DrawDataID.ID_000005_LEMNISCATE -> Lemniscate01Drawable()
                // カッシーニの卵形線
                DrawDataID.ID_000006_CASSINIAN_OVAL -> CassinianOval01Drawable()
                // レムニスケート曲線
                DrawDataID.ID_000007_LEMNISCATE -> Lemniscate02Drawable()
                // カッシーニの卵形線
                DrawDataID.ID_000008_CASSINIAN_OVAL -> CassinianOval02Drawable()
                // ハート
                DrawDataID.ID_000009_HEART_01 -> Heart01Drawable()
                // 対数螺旋(描画点を回転させる方法)
                DrawDataID.ID_000010_LOGARITHMIC_SPIRAL -> LogarithmicSpiral01Drawable()
                // 対数螺旋上に円を描画
                DrawDataID.ID_000011_LOGARITHMIC_SPIRAL_CIRCLE_01 -> LogarithmicSpiralCircle01Drawable()
                // 対数螺旋上に円を描画
                DrawDataID.ID_000012_LOGARITHMIC_SPIRAL_CIRCLE_02 -> LogarithmicSpiralCircle02Drawable()
                // インボリュート曲線
                DrawDataID.ID_000013_INVOLUTE_CURVE_01 -> InvoluteCurve01Drawable()
                // 対数螺旋間に円を描画
                DrawDataID.ID_000014_LOGARITHMIC_SPIRAL_TRIANGLE_01 -> LogarithmicSpiralTriangle01Drawable()
                // インボリュート曲線
                DrawDataID.ID_000015_INVOLUTE_CURVE_02 -> InvoluteCurve02Drawable()
                // パスカルの蝸牛形01
                DrawDataID.ID_000016_LIMACON_01 -> Limacon01Drawable()
                // パスカルの蝸牛形02
                DrawDataID.ID_000017_LIMACON_02 -> Limacon02Drawable()
                // パスカルの蝸牛形03
                DrawDataID.ID_000018_LIMACON_03 -> Limacon03Drawable()
                // 蝶々 L.Sautereau 01
                DrawDataID.ID_000019_BUTTERFLY_SAUTEREAU_01 -> ButterflySautereau01Drawable()
                // 蝶々 L.Sautereau 02
                DrawDataID.ID_000020_BUTTERFLY_SAUTEREAU_02 -> ButterflySautereau02Drawable()
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
                // 蝶々 T.Fay 01
                DrawDataID.ID_000027_BUTTERFLY_FAY_01 -> ButterflyFay01Drawable()
                // 陰陽01
                DrawDataID.ID_000028_YING_YANG_01 -> YingYang01Drawable()
                // 陰陽02
                DrawDataID.ID_000029_YING_YANG_02 -> YingYang02Drawable()
                // Trèfle de Habenicht
                DrawDataID.ID_000030_TREFLE_01 -> TrefleHabenicht01Drawable()
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
                // カッシニアン曲線
                DrawDataID.ID_000037_CASSINIAN_CURVE -> CassinianCurve01Drawable()
                // Caustic of a Circle
                DrawDataID.ID_000038_CAUSTIC_CIRCLE_01 -> CausticCircle01Drawable()
                // Cochleoid
                DrawDataID.ID_000039_COCHLEOID_01 -> Cochleoid01Drawable()
                // Cyclic Harmonic Curve
                DrawDataID.ID_000040_CURVE_CYCLIC_HARMONIC_01 -> CyclicHarmonic01Drawable()
                // Fermat Spiral
                DrawDataID.ID_000041_FERMAT_SPIRAL_01 -> FermatSpiral01Drawable()
                // サイン波
                DrawDataID.ID_000200_SINE_WAVE -> SineWave01Drawable()
                // 円の周りを回転するサイン波
                DrawDataID.ID_000201_SINE_WAVE_CIRCLE -> SineWaveCircle01Drawable()
                // 円の周りを回転するサイン波
                DrawDataID.ID_000202_SINE_WAVE_CIRCLE -> SineWaveCircle02Drawable()
                // 隣同士の点がサイン波を描く
                DrawDataID.ID_000203_SINE_WAVE_POINT_01 -> SineWavePoint01Drawable()
                // 隣同士の点がサイン波を描く
                DrawDataID.ID_000204_SINE_WAVE_POINT_02 -> SineWavePoint02Drawable()
                // 1536色
                DrawDataID.ID_000205_COLOR_1536 -> Color1536Drawable()
                // 768色(暗色)
                DrawDataID.ID_000206_COLOR_768_DARK -> Color768DarkDrawable()
                // RGBの棒線で紡ぐ
                DrawDataID.ID_000207_WAVE_RGB_WEAVE_01 -> RgbWeaveWave01Drawable()
                // リサージュ曲線(p:q=1:2)
                DrawDataID.ID_000050_LISSAJOUS_CURVE_1_2 -> Lissajous01Drawable()
                // リサージュ曲線(p:q=3:2)
                DrawDataID.ID_000051_LISSAJOUS_CURVE_3_2 -> Lissajous01Drawable()
                // リサージュ曲線(p:q=3:4)
                DrawDataID.ID_000052_LISSAJOUS_CURVE_3_4 -> Lissajous01Drawable()
                // 高木曲線
                DrawDataID.ID_000101_TAKAGI_CURVE -> TakagiCurve01Drawable()
                // ドラゴン曲線
                DrawDataID.ID_000104_DRAGON_CURVE -> DragonCurve01Drawable()
                // ヒルベルト曲線(タートル)
                DrawDataID.ID_000106_HILBERT_CURVE -> HilbertCurve02Drawable()
                // ヒルベルト曲線
                DrawDataID.ID_000107_HILBERT_CURVE -> HilbertCurve01Drawable()
                // ムーア曲線(タートル)
                DrawDataID.ID_000108_MOORECURVE -> MooreCurve02Drawable()
                // ムーア曲線
                DrawDataID.ID_000109_MOORECURVE -> MooreCurve01Drawable()
                // Lévy C
                DrawDataID.ID_000110_LEVY -> LevyCurve01Drawable()
                // 樹木曲線01
                DrawDataID.ID_000111_TREE_CURVE -> TreeCurve01Drawable()
                // 樹木曲線02
                DrawDataID.ID_000112_TREE_CURVE -> TreeCurve02Drawable()
                // 樹木曲線03
                DrawDataID.ID_000113_TREE_CURVE -> TreeCurve03Drawable()
                // ゴスパー島01
                DrawDataID.ID_000116_GOSPER_ISLAND_01 -> GosperIsland01Drawable()
                // ゴスパー島02
                DrawDataID.ID_000117_GOSPER_ISLAND_02 -> GosperIsland02Drawable()
                // ゴスパー曲線01
                DrawDataID.ID_000118_GOSPER_CURVE_01 -> GosperCurve01Drawable()
                // ゴスパー曲線02
                DrawDataID.ID_000119_GOSPER_CURVE_02 -> GosperCurve02Drawable()
                // ゴスパー曲線03
                DrawDataID.ID_000120_GOSPER_CURVE_03 -> GosperCurve03Drawable()
                // シェルピンスキーの三角形01
                DrawDataID.ID_000121_SIERPINSKI_TRIANGLE_01 -> SierpinskiTriangle01Drawable()
                // シェルピンスキーのカーペット
                DrawDataID.ID_000122_SIERPINSKI_CARPET -> SierpinskiCarpet01Drawable()
                // シェルピンスキーの五角形
                DrawDataID.ID_000123_SIERPINSKI_PENTAGON -> SierpinskiPentagon01Drawable()
                // シェルピンスキーの六角形
                DrawDataID.ID_000124_SIERPINSKI_HEXAGON -> SierpinskiHexagon01Drawable()
                // シェルピンスキーの八角形
                DrawDataID.ID_000125_SIERPINSKI_OCTAGON -> SierpinskiOctagon01Drawable()
                // シェルピンスキーの星
                DrawDataID.ID_000126_SIERPINSKI_STAR -> SierpinskiStar01Drawable()
                // シェルピンスキーの五角形02
                DrawDataID.ID_000127_SIERPINSKI_PENTAGON_02 -> SierpinskiPentagon02Drawable()
                // シェルピンスキーの三角形08
                DrawDataID.ID_000128_SIERPINSKI_TRIANGLE_02 -> SierpinskiTriangle02Drawable()
                // コッホ雪片01
                DrawDataID.ID_000130_KOCH_SNOWFLAKE_01 -> KochSnowflake01Drawable()
                // コッホ雪片02
                DrawDataID.ID_000131_KOCH_SNOWFLAKE_02 -> KochSnowflake02Drawable()
                // コッホ雪片03
                DrawDataID.ID_000132_KOCH_SNOWFLAKE_03 -> KochSnowflake03Drawable()
                // コッホ雪片04
                DrawDataID.ID_000133_KOCH_SNOWFLAKE_04 -> KochSnowflake04Drawable()
                // コッホ雪片05
                DrawDataID.ID_000134_KOCH_SNOWFLAKE_05 -> KochSnowflake05Drawable()
                // マンデルブロ―集合(白黒)
                DrawDataID.ID_000140_MANDELBRO_SET -> Mandelbrot00Drawable()
                // マンデルブロ―集合(-1.5-1.0i～+0.5+1.0i)
                DrawDataID.ID_000141_MANDELBRO_SET -> Mandelbrot01Drawable()
                // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
                DrawDataID.ID_000142_MANDELBRO_SET -> Mandelbrot01Drawable()
                // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
                DrawDataID.ID_000143_MANDELBRO_SET -> Mandelbrot01Drawable()
                // ジュリア集合
                DrawDataID.ID_000144_JULIA_SET -> Julia01Drawable()
                // 三角形in四角形
                DrawDataID.ID_000300_3_IN_4 -> PolygonInPolygon01Drawable()
                // 三角形in五角形
                DrawDataID.ID_000301_3_IN_5 -> PolygonInPolygon01Drawable()
                // 三角形in六角形
                DrawDataID.ID_000302_3_IN_6 -> PolygonInPolygon01Drawable()
                // 三角形in七角形
                DrawDataID.ID_000303_3_IN_7 -> PolygonInPolygon01Drawable()
                // 三角形in八角形
                DrawDataID.ID_000304_3_IN_8 -> PolygonInPolygon01Drawable()
                // 三角形in九角形
                DrawDataID.ID_000305_3_IN_9 -> PolygonInPolygon01Drawable()
                // 三角形in十角形
                DrawDataID.ID_000306_3_IN_10 -> PolygonInPolygon01Drawable()
                // 四角形in五角形
                DrawDataID.ID_000307_4_IN_5 -> PolygonInPolygon01Drawable()
                // 四角形in六角形
                DrawDataID.ID_000308_4_IN_6 -> PolygonInPolygon01Drawable()
                // 四角形in七角形
                DrawDataID.ID_000309_4_IN_7 -> PolygonInPolygon01Drawable()
                // 四角形in八角形
                DrawDataID.ID_000310_4_IN_8 -> PolygonInPolygon01Drawable()
                // 四角形in九角形
                DrawDataID.ID_000311_4_IN_9 -> PolygonInPolygon01Drawable()
                // 四角形in十角形
                DrawDataID.ID_000312_4_IN_10 -> PolygonInPolygon01Drawable()
                // 五角形in六角形
                DrawDataID.ID_000313_5_IN_6 -> PolygonInPolygon01Drawable()
                // 五角形in七角形
                DrawDataID.ID_000314_5_IN_7 -> PolygonInPolygon01Drawable()
                // 五角形in八角形
                DrawDataID.ID_000315_5_IN_8 -> PolygonInPolygon01Drawable()
                // 五角形in九角形
                DrawDataID.ID_000316_5_IN_9 -> PolygonInPolygon01Drawable()
                // 五角形in十角形
                DrawDataID.ID_000317_5_IN_10 -> PolygonInPolygon01Drawable()
                // 六角形in七角形
                DrawDataID.ID_000318_6_IN_7 -> PolygonInPolygon01Drawable()
                // 六角形in八角形
                DrawDataID.ID_000319_6_IN_8 -> PolygonInPolygon01Drawable()
                // 六角形in九角形
                DrawDataID.ID_000320_6_IN_9 -> PolygonInPolygon01Drawable()
                // 六角形in十角形
                DrawDataID.ID_000321_6_IN_10 -> PolygonInPolygon01Drawable()
                // 七角形in八角形
                DrawDataID.ID_000322_7_IN_8 -> PolygonInPolygon01Drawable()
                // 七角形in九角形
                DrawDataID.ID_000323_7_IN_9 -> PolygonInPolygon01Drawable()
                // 七角形in十角形
                DrawDataID.ID_000324_7_IN_10 -> PolygonInPolygon01Drawable()
                // 八角形in九角形
                DrawDataID.ID_000325_8_IN_9 -> PolygonInPolygon01Drawable()
                // 八角形in十角形
                DrawDataID.ID_000326_8_IN_10 -> PolygonInPolygon01Drawable()
                // 九角形in十角形
                DrawDataID.ID_000327_9_IN_10 -> PolygonInPolygon01Drawable()
                // 三角形の重心
                DrawDataID.ID_000370_TRIANGLE_CENTER_OF_GRAVITY -> TriangleCenterOfGravity01Drawable()
                // 三角形の外心
                DrawDataID.ID_000371_TRIANGLE_CIRCUMCENTER -> TriangleCircumCenter01Drawable()
                // 三角形の内心
                DrawDataID.ID_000372_TRIANGLE_INCENTER -> TriangleInCenter01Drawable()
                // 三角形の垂心
                DrawDataID.ID_000373_TRIANGLE_ORTHOCENTER -> TriangleOrthoCenter01Drawable()
                // 三角形の傍心
                DrawDataID.ID_000374_TRIANGLE_EXCENTER -> TriangleExCenter01Drawable()
                // 等速度運動
                DrawDataID.ID_000401_NATURE_UNFORM_MOTION -> UniformMotion01Drawable()
                // 噴水
                DrawDataID.ID_000402_NATURE_FOUNTAIN -> Fountain01Drawable()
                // ランダムウォーク
                DrawDataID.ID_000403_NATURE_RANDOM_WALK -> RandomWalk01Drawable()
                // タッチした方向に向かって加速
                DrawDataID.ID_000404_NATURE_ACCELERATE_TOWARDS_TOUCH_POINT -> AccelerateTowardsTouchPoint01Drawable()
                // 質量の効果
                DrawDataID.ID_000405_NATURE_MASS_EFFECT -> MassEffect01Drawable()
                // 摩擦の効果
                DrawDataID.ID_000406_NATURE_FRICTION_EFFECT -> FrictionEffect01Drawable()
                // 三角形でEXILE
                DrawDataID.ID_000600_TRIANGLE_EXILE -> TriangleExile01Drawable()
                // 多角形のラップ
                DrawDataID.ID_000601_POLYGON_LAP -> PolygonLap01Drawable()
                // 回転する矢印
                DrawDataID.ID_000602_ROTATE_ARROWS -> RotateArrows01Drawable()
                // 回転しながら三角形を合体
                DrawDataID.ID_000603_TRIANGLE_UNITE -> TriangleUnite01Drawable()
                // 正方形⇔ひし形01
                DrawDataID.ID_000604_SQUARE_2_DIAMOND_01 -> Square2Diamond01Drawable()
                // 三角形⇔ひし形
                DrawDataID.ID_000605_TRIANGLE_2_DIAMOND -> Triangle2Diamond01Drawable()
                // 正方形⇔ひし形02
                DrawDataID.ID_000606_SQUARE_2_DIAMOND_02 -> Square2Diamond02Drawable()
                // 三角形⇔六角形
                DrawDataID.ID_000607_TRIANGLE_2_HEXAGON -> Triangle2Hexagon01Drawable()
                // 正方形を×印で並べる
                DrawDataID.ID_000608_SQUARE_CROSS_01 -> SquareCross01Drawable()
                // 六角形が波打つようにスケールを変更01
                DrawDataID.ID_000609_HEXAGON_SCALE_01 -> HexagonScale01Drawable()
                // 六角形が波打つようにスケールを変更02
                DrawDataID.ID_000610_HEXAGON_SCALE_02 -> HexagonScale02Drawable()
                // 正方形⇔十字01
                DrawDataID.ID_000611_SQUARE_2_CROSS_01 -> Square2Cross01Drawable()
                // 三角形out三角形
                DrawDataID.ID_000701_3_OUT_3 -> PolygonOutPolygon01Drawable()
                // 三角形out四角形
                DrawDataID.ID_000702_3_OUT_4 -> PolygonOutPolygon01Drawable()
                // 三角形out五角形
                DrawDataID.ID_000703_3_OUT_5 -> PolygonOutPolygon01Drawable()
                // 三角形out六角形
                DrawDataID.ID_000704_3_OUT_6 -> PolygonOutPolygon01Drawable()
                // 三角形out七角形
                DrawDataID.ID_000705_3_OUT_7 -> PolygonOutPolygon01Drawable()
                // 三角形out八角形
                DrawDataID.ID_000706_3_OUT_8 -> PolygonOutPolygon01Drawable()
                // 三角形out九角形
                DrawDataID.ID_000707_3_OUT_9 -> PolygonOutPolygon01Drawable()
                // 三角形out十角形
                DrawDataID.ID_000708_3_OUT_10 -> PolygonOutPolygon01Drawable()
                // 四角形out三角形
                DrawDataID.ID_000709_4_OUT_3 -> PolygonOutPolygon01Drawable()
                // 四角形out四角形
                DrawDataID.ID_000710_4_OUT_4 -> PolygonOutPolygon01Drawable()
                // 四角形out五角形
                DrawDataID.ID_000711_4_OUT_5 -> PolygonOutPolygon01Drawable()
                // 四角形out六角形
                DrawDataID.ID_000712_4_OUT_6 -> PolygonOutPolygon01Drawable()
                // 四角形out七角形
                DrawDataID.ID_000713_4_OUT_7 -> PolygonOutPolygon01Drawable()
                // 四角形out八角形
                DrawDataID.ID_000714_4_OUT_8 -> PolygonOutPolygon01Drawable()
                // 四角形out九角形
                DrawDataID.ID_000715_4_OUT_9 -> PolygonOutPolygon01Drawable()
                // 四角形out十角形
                DrawDataID.ID_000716_4_OUT_10 -> PolygonOutPolygon01Drawable()
                // 五角形out三角形
                DrawDataID.ID_000717_5_OUT_3 -> PolygonOutPolygon01Drawable()
                // 五角形out四角形
                DrawDataID.ID_000718_5_OUT_4 -> PolygonOutPolygon01Drawable()
                // 五角形out五角形
                DrawDataID.ID_000719_5_OUT_5 -> PolygonOutPolygon01Drawable()
                // 五角形out六角形
                DrawDataID.ID_000720_5_OUT_6 -> PolygonOutPolygon01Drawable()
                // 五角形out七角形
                DrawDataID.ID_000721_5_OUT_7 -> PolygonOutPolygon01Drawable()
                // 五角形out八角形
                DrawDataID.ID_000722_5_OUT_8 -> PolygonOutPolygon01Drawable()
                // 五角形out九角形
                DrawDataID.ID_000723_5_OUT_9 -> PolygonOutPolygon01Drawable()
                // 五角形out十角形
                DrawDataID.ID_000724_5_OUT_10 -> PolygonOutPolygon01Drawable()
                // 六角形out三角形
                DrawDataID.ID_000725_6_OUT_3 -> PolygonOutPolygon01Drawable()
                // 六角形out四角形
                DrawDataID.ID_000726_6_OUT_4 -> PolygonOutPolygon01Drawable()
                // 六角形out五角形
                DrawDataID.ID_000727_6_OUT_5 -> PolygonOutPolygon01Drawable()
                // 六角形out六角形
                DrawDataID.ID_000728_6_OUT_6 -> PolygonOutPolygon01Drawable()
                // 六角形out七角形
                DrawDataID.ID_000729_6_OUT_7 -> PolygonOutPolygon01Drawable()
                // 六角形out八角形
                DrawDataID.ID_000730_6_OUT_8 -> PolygonOutPolygon01Drawable()
                // 六角形out九角形
                DrawDataID.ID_000731_6_OUT_9 -> PolygonOutPolygon01Drawable()
                // 六角形out十角形
                DrawDataID.ID_000732_6_OUT_10 -> PolygonOutPolygon01Drawable()
                // 七角形out三角形
                DrawDataID.ID_000733_7_OUT_3 -> PolygonOutPolygon01Drawable()
                // 七角形out四角形
                DrawDataID.ID_000734_7_OUT_4 -> PolygonOutPolygon01Drawable()
                // 七角形out五角形
                DrawDataID.ID_000735_7_OUT_5 -> PolygonOutPolygon01Drawable()
                // 七角形out六角形
                DrawDataID.ID_000736_7_OUT_6 -> PolygonOutPolygon01Drawable()
                // 七角形out七角形
                DrawDataID.ID_000737_7_OUT_7 -> PolygonOutPolygon01Drawable()
                // 七角形out八角形
                DrawDataID.ID_000738_7_OUT_8 -> PolygonOutPolygon01Drawable()
                // 七角形out九角形
                DrawDataID.ID_000739_7_OUT_9 -> PolygonOutPolygon01Drawable()
                // 七角形out十角形
                DrawDataID.ID_000740_7_OUT_10 -> PolygonOutPolygon01Drawable()
                // 八角形out三角形
                DrawDataID.ID_000741_8_OUT_3 -> PolygonOutPolygon01Drawable()
                // 八角形out四角形
                DrawDataID.ID_000742_8_OUT_4 -> PolygonOutPolygon01Drawable()
                // 八角形out五角形
                DrawDataID.ID_000743_8_OUT_5 -> PolygonOutPolygon01Drawable()
                // 八角形out六角形
                DrawDataID.ID_000744_8_OUT_6 -> PolygonOutPolygon01Drawable()
                // 八角形out七角形
                DrawDataID.ID_000745_8_OUT_7 -> PolygonOutPolygon01Drawable()
                // 八角形out八角形
                DrawDataID.ID_000746_8_OUT_8 -> PolygonOutPolygon01Drawable()
                // 八角形out九角形
                DrawDataID.ID_000747_8_OUT_9 -> PolygonOutPolygon01Drawable()
                // 八角形out十角形
                DrawDataID.ID_000748_8_OUT_10 -> PolygonOutPolygon01Drawable()
                // 九角形out三角形
                DrawDataID.ID_000749_9_OUT_3 -> PolygonOutPolygon01Drawable()
                // 九角形out四角形
                DrawDataID.ID_000750_9_OUT_4 -> PolygonOutPolygon01Drawable()
                // 九角形out五角形
                DrawDataID.ID_000751_9_OUT_5 -> PolygonOutPolygon01Drawable()
                // 九角形out六角形
                DrawDataID.ID_000752_9_OUT_6 -> PolygonOutPolygon01Drawable()
                // 九角形out七角形
                DrawDataID.ID_000753_9_OUT_7 -> PolygonOutPolygon01Drawable()
                // 九角形out八角形
                DrawDataID.ID_000754_9_OUT_8 -> PolygonOutPolygon01Drawable()
                // 九角形out九角形
                DrawDataID.ID_000755_9_OUT_9 -> PolygonOutPolygon01Drawable()
                // 九角形out十角形
                DrawDataID.ID_000756_9_OUT_10 -> PolygonOutPolygon01Drawable()
                // 十角形out三角形
                DrawDataID.ID_000757_10_OUT_3 -> PolygonOutPolygon01Drawable()
                // 十角形out四角形
                DrawDataID.ID_000758_10_OUT_4 -> PolygonOutPolygon01Drawable()
                // 十角形out五角形
                DrawDataID.ID_000759_10_OUT_5 -> PolygonOutPolygon01Drawable()
                // 十角形out六角形
                DrawDataID.ID_000760_10_OUT_6 -> PolygonOutPolygon01Drawable()
                // 十角形out七角形
                DrawDataID.ID_000761_10_OUT_7 -> PolygonOutPolygon01Drawable()
                // 十角形out八角形
                DrawDataID.ID_000762_10_OUT_8 -> PolygonOutPolygon01Drawable()
                // 十角形out九角形
                DrawDataID.ID_000763_10_OUT_9 -> PolygonOutPolygon01Drawable()
                // 十角形out十角形
                DrawDataID.ID_000764_10_OUT_10 -> PolygonOutPolygon01Drawable()
                // 三角形をずらして描く
                DrawDataID.ID_000781_SLIDE_3 -> SlidePolygon01Drawable()
                // 四角形をずらして描く
                DrawDataID.ID_000782_SLIDE_4 -> SlidePolygon01Drawable()
                // 五角形をずらして描く
                DrawDataID.ID_000783_SLIDE_5 -> SlidePolygon01Drawable()
                // 六角形をずらして描く
                DrawDataID.ID_000784_SLIDE_6 -> SlidePolygon01Drawable()
                // 七角形をずらして描く
                DrawDataID.ID_000785_SLIDE_7 -> SlidePolygon01Drawable()
                // 八角形をずらして描く
                DrawDataID.ID_000786_SLIDE_8 -> SlidePolygon01Drawable()
                // 九角形をずらして描く
                DrawDataID.ID_000787_SLIDE_9 -> SlidePolygon01Drawable()
                // 十角形をずらして描く
                DrawDataID.ID_000788_SLIDE_10 -> SlidePolygon01Drawable()
                // だんだん大きくなる円その１
                DrawDataID.ID_000800_CIRCLE_BIGGER_01 -> BiggerCircle01Drawable()
                // だんだん大きくなる円その２
                DrawDataID.ID_000801_CIRCLE_BIGGER_02 -> BiggerCircle02Drawable()
                // 円をずらして描く
                DrawDataID.ID_000802_CIRCLE_SLIDE -> SlideCircle01Drawable()
                // 円の中に円を描き,すべての円を回転させる
                DrawDataID.ID_000803_CIRCLE_ROTATE_01 -> RotateCircle01Drawable()
                // 円の中に円を描き,すべての円を回転させる
                DrawDataID.ID_000804_CIRCLE_ROTATE_02 -> RotateCircle02Drawable()
                // 円の中に円を描き,すべての円を回転させる
                DrawDataID.ID_000805_CIRCLE_TEST_ROTATE_01 -> TestRotateCircle01Drawable()
                // 花火01
                DrawDataID.ID_000806_CIRCLE_FIREWORK_01 -> Fireworks01Drawable()
                // 円⇔正方形の変形
                DrawDataID.ID_000810_CIRCLE_MORPH_CIRCLE2SQUARE -> Circle2SqaureMorph01Drawable()
                // "円⇔正方形の変形"のタイリング
                DrawDataID.ID_000811_CIRCLE_MORPH_CIRCLE2SQUARE -> Circle2SqaureMorph02Drawable()
                // 太陽
                DrawDataID.ID_000812_CIRCLE_MORPH_SUN -> SunMorph01Drawable()
                // "正方形の中で大きくなる円"のタイリング
                DrawDataID.ID_000820_CIRCLE_TILE_CIRCLE2SQUARE -> Circle2SqaureTile01Drawable()
                // クリスマスツリー(円を三角形上に並べる)
                DrawDataID.ID_000821_CIRCLE_XMASTREE -> XmasTreeCircle01Drawable()
                // 青・赤・黄３つの円を回転する
                DrawDataID.ID_000822_CIRCLE_TILE_ROTATE_CIRCLE_01 -> RotateCircleTile01Drawable()
                // 円のタイリング(左右・左下右上・右下左上の順でスライド)
                DrawDataID.ID_000823_CIRCLE_TILE_SLIDE_01 -> SlideCircleTile01Drawable()
                // "Stepping Feet"
                DrawDataID.ID_000900_OPTICAL_ILLUSION_STEPPING_FEET -> SteppingFeet01Drawable()
                // "Stereokinetic Effect(SKE)"
                DrawDataID.ID_000901_OPTICAL_ILLUSION_STEREOKINETIC_EFFECT -> StereoKineticEffect01Drawable()
                else -> throw RuntimeException("Not Found MyDrawable")
            }

            if ( notifyCallback != null )
                myDrawable.setNotifyCallback(notifyCallback)

            return myDrawable
        }
    }
}