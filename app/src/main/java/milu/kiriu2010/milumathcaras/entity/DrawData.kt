package milu.kiriu2010.milumathcaras.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// ---------------------------------------------------
// 描画データの識別子
// ---------------------------------------------------
// EnumのParcelable
// https://qiita.com/hkusu/items/bf0029283c1032054119
// ---------------------------------------------------
@Parcelize
enum class DrawDataID(val id: Int): Parcelable {
    // サイクロイド曲線(k=1.0)
    ID_000001_CYCLOID(1),
    // トロコイド曲線(k=2.0)
    ID_000002_TROCHOID(2),
    // トロコイド曲線(k=3.0)
    ID_000003_TROCHOID(3),
    // トロコイド曲線(k=4.0)
    ID_000004_TROCHOID(4),
    // レムニスケート曲線
    ID_000005_LEMNISCATE(5),
    // カッシーニの卵形線
    ID_000006_CASSINIAN_OVAL(6),
    // レムニスケート曲線
    ID_000007_LEMNISCATE(7),
    // カッシーニの卵形線
    ID_000008_CASSINIAN_OVAL(8),
    // ハート
    ID_000009_HEART_01(9),
    // 対数螺旋
    ID_000010_LOGARITHMIC_SPIRAL(10),
    // 対数螺旋上に円を描く
    ID_000011_LOGARITHMIC_SPIRAL_CIRCLE_01(11),
    // 対数螺旋上に円を描く
    ID_000012_LOGARITHMIC_SPIRAL_CIRCLE_02(12),
    // インボリュート曲線
    ID_000013_INVOLUTE_CURVE_01(13),
    // 対数螺旋間に三角形を描く
    ID_000014_LOGARITHMIC_SPIRAL_TRIANGLE_01(14),
    // インボリュート曲線
    ID_000015_INVOLUTE_CURVE_02(15),
    // パスカルの蝸牛形01
    ID_000016_LIMACON_01(16),
    // パスカルの蝸牛形02
    ID_000017_LIMACON_02(17),
    // パスカルの蝸牛形03
    ID_000018_LIMACON_03(18),
    // 蝶々 L.Sautereau 01
    ID_000019_BUTTERFLY_SAUTEREAU_01(19),
    // 蝶々 L.Sautereau 02
    ID_000020_BUTTERFLY_SAUTEREAU_02(20),
    // カージオイド曲線(k=1.0)
    ID_000021_CARDIOID(21),
    // エピサイクロイド曲線(k=4.0)
    ID_000022_EPICYCLOID(22),
    // エピサイクロイド曲線(k=2.1)
    ID_000023_EPICYCLOID(23),
    // エピサイクロイド曲線(k=3.8)
    ID_000024_EPICYCLOID(24),
    // エピサイクロイド曲線(k=5.5)
    ID_000025_EPICYCLOID(25),
    // エピサイクロイド曲線(k=7.2)
    ID_000026_EPICYCLOID(26),
    // 蝶々 T.Fay 01
    ID_000027_BUTTERFLY_FAY_01(27),
    // 陰陽01
    ID_000028_YING_YANG_01(28),
    // 陰陽02
    ID_000029_YING_YANG_02(29),
    // Trèfle de Habenicht
    ID_000030_TREFLE_01(30),
    // 三芒形/三尖形(deltoid)(k=3.0)
    ID_000031_DELTOID(31),
    // アステロイド曲線(astroid)(k=4.0)
    ID_000032_ASTROID(32),
    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
    ID_000033_HYPO_CYCLOID(33),
    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
    ID_000034_HYPO_CYCLOID(34),
    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
    ID_000035_HYPO_CYCLOID(35),
    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
    ID_000036_HYPO_CYCLOID(36),
    // カッシニアン曲線
    ID_000037_CASSINIAN_CURVE(37),
    // Caustic of a circle
    ID_000038_CAUSTIC_CIRCLE_01(38),
    // Cochleoid
    ID_000039_COCHLEOID_01(39),
    // Cyclic-Harmonic Curve
    ID_000040_CURVE_CYCLIC_HARMONIC_01(40),
    // Fermat's Spiral
    ID_000041_FERMAT_SPIRAL_01(41),
    // Lituus
    ID_000042_LITUUS_01(42),
    // Hypotrochoid
    ID_000043_HYPOTROCHOID_01(43),
    // Rose
    ID_000044_ROSE_01(44),
    // Torus Knot
    ID_000045_TORUS_KNOT_01(45),
    // 対数螺旋間に四角形を描く
    ID_000046_LOGARITHMIC_SPIRAL_SQUARE_01(46),
    // リサージュ曲線(p:q=1:2)
    ID_000050_LISSAJOUS_CURVE_1_2(50),
    // リサージュ曲線(p:q=3:2)
    ID_000051_LISSAJOUS_CURVE_3_2(51),
    // リサージュ曲線(p:q=3:4)
    ID_000052_LISSAJOUS_CURVE_3_4(52),
    // 高木曲線
    ID_000101_TAKAGI_CURVE(101),
    // ドラゴン曲線
    ID_000104_DRAGON_CURVE(104),
    // ヒルベルト曲線(タートル)
    ID_000106_HILBERT_CURVE(106),
    // ヒルベルト曲線
    ID_000107_HILBERT_CURVE(107),
    // ムーア曲線(タートル)
    ID_000108_MOORECURVE(108),
    // ムーア曲線
    ID_000109_MOORECURVE(109),
    // Lévy C
    ID_000110_LEVY(110),
    // 樹木曲線01
    ID_000111_TREE_CURVE(111),
    // 樹木曲線02
    ID_000112_TREE_CURVE(112),
    // 樹木曲線03
    ID_000113_TREE_CURVE(113),
    // ゴスパー島01
    ID_000116_GOSPER_ISLAND_01(116),
    // ゴスパー島02
    ID_000117_GOSPER_ISLAND_02(117),
    // ゴスパー曲線01
    ID_000118_GOSPER_CURVE_01(118),
    // ゴスパー曲線02
    ID_000119_GOSPER_CURVE_02(119),
    // ゴスパー曲線03
    ID_000120_GOSPER_CURVE_03(120),
    // シェルピンスキーの三角形01
    ID_000121_SIERPINSKI_TRIANGLE_01(121),
    // シェルピンスキーのカーペット
    ID_000122_SIERPINSKI_CARPET(122),
    // シェルピンスキーの五角形
    ID_000123_SIERPINSKI_PENTAGON(123),
    // シェルピンスキーの六角形
    ID_000124_SIERPINSKI_HEXAGON(124),
    // シェルピンスキーの八角形
    ID_000125_SIERPINSKI_OCTAGON(125),
    // シェルピンスキーの星
    ID_000126_SIERPINSKI_STAR(126),
    // シェルピンスキーの五角形02
    ID_000127_SIERPINSKI_PENTAGON_02(127),
    // シェルピンスキーの三角形02
    ID_000128_SIERPINSKI_TRIANGLE_02(128),
    // コッホ雪片01
    ID_000130_KOCH_SNOWFLAKE_01(130),
    // コッホ雪片02
    ID_000131_KOCH_SNOWFLAKE_02(131),
    // コッホ雪片03
    ID_000132_KOCH_SNOWFLAKE_03(132),
    // コッホ雪片04
    ID_000133_KOCH_SNOWFLAKE_04(133),
    // コッホ雪片05
    ID_000134_KOCH_SNOWFLAKE_05(134),
    // マンデルブロ―集合(白黒)
    ID_000140_MANDELBRO_SET(140),
    // マンデルブロ―集合(-1.5-1.0i～+0.5+1.0i)
    ID_000141_MANDELBRO_SET(141),
    // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
    ID_000142_MANDELBRO_SET(142),
    // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
    ID_000143_MANDELBRO_SET(143),
    // ジュリア集合
    ID_000144_JULIA_SET(144),
    // サイン波
    ID_000200_SINE_WAVE(200),
    // 円の周りを回転するサイン波
    ID_000201_SINE_WAVE_CIRCLE(201),
    // 円の周りを回転するサイン波
    ID_000202_SINE_WAVE_CIRCLE(202),
    // 隣同士の点がサイン波を描く
    ID_000203_SINE_WAVE_POINT_01(203),
    // 隣同士の点がサイン波を描く
    ID_000204_SINE_WAVE_POINT_02(204),
    // 1536色
    ID_000205_COLOR_1536(205),
    // 768色(暗色)
    ID_000206_COLOR_768_DARK(206),
    // RGBの棒線で紡ぐ
    ID_000207_WAVE_RGB_WEAVE_01(207),
    // 三角形in四角形
    ID_000300_3_IN_4(300),
    // 三角形in五角形
    ID_000301_3_IN_5(301),
    // 三角形in六角形
    ID_000302_3_IN_6(302),
    // 三角形in七角形
    ID_000303_3_IN_7(303),
    // 三角形in八角形
    ID_000304_3_IN_8(304),
    // 三角形in九角形
    ID_000305_3_IN_9(305),
    // 三角形in十角形
    ID_000306_3_IN_10(306),
    // 四角形in五角形
    ID_000307_4_IN_5(307),
    // 四角形in六角形
    ID_000308_4_IN_6(308),
    // 四角形in七角形
    ID_000309_4_IN_7(309),
    // 四角形in八角形
    ID_000310_4_IN_8(310),
    // 四角形in九角形
    ID_000311_4_IN_9(311),
    // 四角形in十角形
    ID_000312_4_IN_10(312),
    // 五角形in六角形
    ID_000313_5_IN_6(313),
    // 五角形in七角形
    ID_000314_5_IN_7(314),
    // 五角形in八角形
    ID_000315_5_IN_8(315),
    // 五角形in九角形
    ID_000316_5_IN_9(316),
    // 五角形in十角形
    ID_000317_5_IN_10(317),
    // 六角形in七角形
    ID_000318_6_IN_7(318),
    // 六角形in八角形
    ID_000319_6_IN_8(319),
    // 六角形in九角形
    ID_000320_6_IN_9(320),
    // 六角形in十角形
    ID_000321_6_IN_10(321),
    // 七角形in八角形
    ID_000322_7_IN_8(322),
    // 七角形in九角形
    ID_000323_7_IN_9(323),
    // 七角形in十角形
    ID_000324_7_IN_10(324),
    // 八角形in九角形
    ID_000325_8_IN_9(325),
    // 八角形in十角形
    ID_000326_8_IN_10(326),
    // 九角形in十角形
    ID_000327_9_IN_10(327),
    // 三角形の重心
    ID_000370_TRIANGLE_CENTER_OF_GRAVITY(370),
    // 三角形の外心
    ID_000371_TRIANGLE_CIRCUMCENTER(371),
    // 三角形の内心
    ID_000372_TRIANGLE_INCENTER(372),
    // 三角形の垂心
    ID_000373_TRIANGLE_ORTHOCENTER(373),
    // 三角形の傍心
    ID_000374_TRIANGLE_EXCENTER(374),
    // 等加速度運動
    ID_000401_NATURE_UNFORM_MOTION(401),
    // 噴水
    ID_000402_NATURE_FOUNTAIN(402),
    // ランダムウォーク
    ID_000403_NATURE_RANDOM_WALK(403),
    // タッチポイントに向かって加速
    ID_000404_NATURE_ACCELERATE_TOWARDS_TOUCH_POINT(404),
    // 質量の効果
    ID_000405_NATURE_MASS_EFFECT(405),
    // 摩擦の効果
    ID_000406_NATURE_FRICTION_EFFECT(406),
    // 三角形でEXILE
    ID_000600_TRIANGLE_EXILE(600),
    // 多角形のラップ
    ID_000601_POLYGON_LAP(601),
    // 回転する矢印
    ID_000602_ROTATE_ARROWS(602),
    // 回転しながら三角形を合体
    ID_000603_TRIANGLE_UNITE(603),
    // 正方形⇔ひし形01
    ID_000604_SQUARE_2_DIAMOND_01(604),
    // 三角形⇔ひし型
    ID_000605_TRIANGLE_2_DIAMOND(605),
    // 正方形⇔ひし形02
    ID_000606_SQUARE_2_DIAMOND_02(606),
    // 三角形⇔六角形01
    ID_000607_TRIANGLE_2_HEXAGON_01(607),
    // 正方形を×印で並べる
    ID_000608_SQUARE_CROSS_01(608),
    // 六角形が波打つようにスケールを変更01
    ID_000609_HEXAGON_SCALE_01(609),
    // 六角形が波打つようにスケールを変更02
    ID_000610_HEXAGON_SCALE_02(610),
    // 正方形⇔十字
    ID_000611_SQUARE_2_CROSS_01(611),
    // 三角形内を線が移動
    ID_000612_LINES_IN_TRIANGLE_01(612),
    // 正方形内を正方形が移動
    ID_000613_SQUARE_IN_SQUARE_01(613),
    // 三角形⇔六角形02
    ID_000614_TRIANGLE_2_HEXAGON_02(614),
    // 正方形⇔正方形
    ID_000615_SQUARE_2_SQUARE_01(615),
    // ギニアの国旗⇔マリの国旗01
    ID_000616_GUINEA_2_MALI_01(616),
    // カメルーンの国旗⇔セネガルの国旗01
    ID_000617_CAMEROON_2_SENEGAL_01(617),
    // 三角形out三角形
    ID_000701_3_OUT_3(701),
    // 三角形out四角形
    ID_000702_3_OUT_4(702),
    // 三角形out五角形
    ID_000703_3_OUT_5(703),
    // 三角形out六角形
    ID_000704_3_OUT_6(704),
    // 三角形out七角形
    ID_000705_3_OUT_7(705),
    // 三角形out八角形
    ID_000706_3_OUT_8(706),
    // 三角形out九角形
    ID_000707_3_OUT_9(707),
    // 三角形out十角形
    ID_000708_3_OUT_10(708),
    // 四角形out三角形
    ID_000709_4_OUT_3(711),
    // 四角形out四角形
    ID_000710_4_OUT_4(712),
    // 四角形out五角形
    ID_000711_4_OUT_5(713),
    // 四角形out六角形
    ID_000712_4_OUT_6(714),
    // 四角形out七角形
    ID_000713_4_OUT_7(715),
    // 四角形out八角形
    ID_000714_4_OUT_8(716),
    // 四角形out九角形
    ID_000715_4_OUT_9(717),
    // 四角形out十角形
    ID_000716_4_OUT_10(718),
    // 五角形out三角形
    ID_000717_5_OUT_3(721),
    // 五角形out四角形
    ID_000718_5_OUT_4(722),
    // 五角形out五角形
    ID_000719_5_OUT_5(723),
    // 五角形out六角形
    ID_000720_5_OUT_6(724),
    // 五角形out七角形
    ID_000721_5_OUT_7(725),
    // 五角形out八角形
    ID_000722_5_OUT_8(726),
    // 五角形out九角形
    ID_000723_5_OUT_9(727),
    // 五角形out十角形
    ID_000724_5_OUT_10(728),
    // 六角形out三角形
    ID_000725_6_OUT_3(731),
    // 六角形out四角形
    ID_000726_6_OUT_4(732),
    // 六角形out五角形
    ID_000727_6_OUT_5(733),
    // 六角形out六角形
    ID_000728_6_OUT_6(734),
    // 六角形out七角形
    ID_000729_6_OUT_7(735),
    // 六角形out八角形
    ID_000730_6_OUT_8(736),
    // 六角形out九角形
    ID_000731_6_OUT_9(737),
    // 六角形out十角形
    ID_000732_6_OUT_10(738),
    // 七角形out三角形
    ID_000733_7_OUT_3(741),
    // 七角形out四角形
    ID_000734_7_OUT_4(742),
    // 七角形out五角形
    ID_000735_7_OUT_5(743),
    // 七角形out六角形
    ID_000736_7_OUT_6(744),
    // 七角形out七角形
    ID_000737_7_OUT_7(745),
    // 七角形out八角形
    ID_000738_7_OUT_8(746),
    // 七角形out九角形
    ID_000739_7_OUT_9(747),
    // 七角形out十角形
    ID_000740_7_OUT_10(748),
    // 八角形out三角形
    ID_000741_8_OUT_3(751),
    // 八角形out四角形
    ID_000742_8_OUT_4(752),
    // 八角形out五角形
    ID_000743_8_OUT_5(753),
    // 八角形out六角形
    ID_000744_8_OUT_6(754),
    // 八角形out七角形
    ID_000745_8_OUT_7(755),
    // 八角形out八角形
    ID_000746_8_OUT_8(756),
    // 八角形out九角形
    ID_000747_8_OUT_9(757),
    // 八角形out十角形
    ID_000748_8_OUT_10(758),
    // 九角形out三角形
    ID_000749_9_OUT_3(761),
    // 九角形out四角形
    ID_000750_9_OUT_4(762),
    // 九角形out五角形
    ID_000751_9_OUT_5(763),
    // 九角形out六角形
    ID_000752_9_OUT_6(764),
    // 九角形out七角形
    ID_000753_9_OUT_7(765),
    // 九角形out八角形
    ID_000754_9_OUT_8(766),
    // 九角形out九角形
    ID_000755_9_OUT_9(767),
    // 九角形out十角形
    ID_000756_9_OUT_10(768),
    // 十角形out三角形
    ID_000757_10_OUT_3(771),
    // 十角形out四角形
    ID_000758_10_OUT_4(772),
    // 十角形out五角形
    ID_000759_10_OUT_5(773),
    // 十角形out六角形
    ID_000760_10_OUT_6(774),
    // 十角形out七角形
    ID_000761_10_OUT_7(775),
    // 十角形out八角形
    ID_000762_10_OUT_8(776),
    // 十角形out九角形
    ID_000763_10_OUT_9(777),
    // 十角形out十角形
    ID_000764_10_OUT_10(778),
    // 三角形をずらして描く
    ID_000781_SLIDE_3(781),
    // 四角形をずらして描く
    ID_000782_SLIDE_4(782),
    // 五角形をずらして描く
    ID_000783_SLIDE_5(783),
    // 六角形をずらして描く
    ID_000784_SLIDE_6(784),
    // 七角形をずらして描く
    ID_000785_SLIDE_7(785),
    // 八角形をずらして描く
    ID_000786_SLIDE_8(786),
    // 九角形をずらして描く
    ID_000787_SLIDE_9(787),
    // 十角形をずらして描く
    ID_000788_SLIDE_10(788),
    // だんだん大きくなる円その１
    ID_000800_CIRCLE_BIGGER_01(800),
    // だんだん大きくなる円その２
    ID_000801_CIRCLE_BIGGER_02(801),
    // 円をずらして描く
    ID_000802_CIRCLE_SLIDE(802),
    // 円の中に円を描き,すべての円を回転させる
    // 回っている最中に広がって、再び戻ってくる
    ID_000803_CIRCLE_ROTATE_01(803),
    // 円の中に円を描き,すべての円を回転させる
    ID_000804_CIRCLE_ROTATE_02(804),
    // 円の中に円を描き,すべての円を回転させる(テスト)
    ID_000805_CIRCLE_TEST_ROTATE_01(805),
    // 花火01
    ID_000806_CIRCLE_FIREWORK_01(806),
    // 日本の国旗⇔パラオの国旗01
    ID_000807_CIRCLE_JAPAN_2_PALAU_01(807),
    // 日本の国旗⇔パラオの国旗02
    ID_000808_CIRCLE_JAPAN_2_PALAU_02(808),
    // PorterDuff"水色と紫色"の"DARKENとLIGHTEN"
    ID_000809_CIRCLE_PORTERDUFF_DL_01(809),
    // 円⇔正方形の変形
    ID_000810_CIRCLE_MORPH_CIRCLE2SQUARE(810),
    // "円⇔正方形の変形"のタイリング
    ID_000811_CIRCLE_MORPH_CIRCLE2SQUARE(811),
    // 太陽
    ID_000812_CIRCLE_MORPH_SUN(812),
    // "正方形の中で大きくなる円"のタイリング
    ID_000820_CIRCLE_TILE_CIRCLE2SQUARE(820),
    // クリスマスツリー(円を三角形上に並べる)
    ID_000821_CIRCLE_XMASTREE(821),
    // 青・赤・黄３つの円を回転する
    ID_000822_CIRCLE_TILE_ROTATE_CIRCLE_01(822),
    // 円のタイリング(左右・左下右上・右下左上の順でスライド)
    ID_000823_CIRCLE_TILE_SLIDE_01(823),
    // 円弧
    ID_000824_CIRCLE_TILE_ARC_01(824),
    // "Stepping Feet"
    ID_000900_OPTICAL_ILLUSION_STEPPING_FEET(900),
    // "Stereokinetic Effect(SKE)"
    ID_000901_OPTICAL_ILLUSION_STEREOKINETIC_EFFECT(901),
    // 正四面体
    ID_001000_GL_TETRAHEDRON_01(1000),
    // 立方体
    ID_001001_GL_CUBE_01(1001),
    // 正八面体
    ID_001002_GL_OCTAHEDRON_01(1002),
    // 正十二面体
    ID_001003_GL_DODECAHEDRON_01(1003),
    // 正二十面体
    ID_001004_GL_ICOSAHEDRON_01(1004),
    // 球
    ID_001005_GL_SPHERE_01(1005),
    // トーラス
    ID_001006_GL_TORUS_01(1006),
    // 円柱01
    ID_001007_GL_CYLINDER_01(1007),
    // 円錐
    ID_001008_GL_CONE_01(1008),
    // 円錐台
    ID_001009_GL_CONE_TRUNCATED_01(1009),
    // 円柱02
    ID_001010_GL_CYLINDER_02(1010),
    // トーラスの結び目
    ID_001011_GL_TORUS_KNOT_01(1011),
    // 立方体座標変換01
    ID_001100_GL_CUBE_TRANSFORM_01(1100),
    // 立方体座標変換02
    ID_001101_GL_CUBE_TRANSFORM_02(1101),
    // 立方体座標変換03
    ID_001102_GL_CUBE_TRANSFORM_03(1102),
    // 立方体座標変換04
    ID_001103_GL_CUBE_TRANSFORM_04(1103),
    // 立方体座標変換05
    ID_001104_GL_CUBE_TRANSFORM_05(1104),
    // 立方体座標変換06
    ID_001105_GL_CUBE_TRANSFORM_06(1105),
    // 立方体座標変換07
    ID_001106_GL_CUBE_TRANSFORM_07(1106),
    // 立方体座標変換08
    ID_001107_GL_CUBE_TRANSFORM_08(1107),
    // 立方体座標変換09
    ID_001108_GL_CUBE_TRANSFORM_09(1108),
    // 立方体座標変換10
    ID_001109_GL_CUBE_TRANSFORM_10(1109),
    // 立方体座標変換11
    ID_001110_GL_CUBE_TRANSFORM_11(1110),
    // 立方体座標変換12
    ID_001111_GL_CUBE_TRANSFORM_12(1111),
    // 六角形にみえる立方体01
    ID_001112_GL_CUBE_LIKE_HEXAGON_01(1112),
    // 六角形にみえる立方体02
    ID_001113_GL_CUBE_LIKE_HEXAGON_02(1113),
    // 立方体の展開図02
    ID_001200_GL_NET_CUBE_02(1200),
    // 正四面体の展開図02
    ID_001201_GL_NET_TETRAHEDRON_02(1201),
    // 立方体の展開図01
    ID_001202_GL_NET_CUBE_01(1202),
    // 正八面体の展開図
    ID_001203_GL_NET_OCTAHEDRON_01(1203),
    // 正十二面体の展開図
    ID_001204_GL_NET_DODECAHEDRON_01(1204),
    // 正二十面体の展開図
    ID_001205_GL_NET_ICOSAHEDRON_01(1205),
    // 立方体の展開図03
    ID_001206_GL_NET_CUBE_03(1207),
    // 球体座標変換01
    ID_001300_GL_SPHERE_TRANSFORM_01(1300),
    // 球体座標変換02
    ID_001301_GL_SPHERE_TRANSFORM_02(1301),
    // 球体色シフト01
    ID_001302_GL_SPHERE_COLOR_SHIFT_01(1302),
    // 球体色シフト02
    ID_001303_GL_SPHERE_COLOR_SHIFT_02(1303),
    // 球体:キューブ環境マップ01
    ID_001304_GL_SPHERE_CUBEMAP_01(1304),
    // 日本⇔バングラディッシュ01
    ID_001305_GL_JAPAN_2_BANGLADESH_01(1305),
    // 円柱のエピサイクロイド01
    ID_001306_GL_CYLINDER_EPICYCLOID_01(1306),
    // 立方体の中をコッホ雪片が回転
    ID_001400_GL_KOCH_SNOWFLAKE_01(1400),
    // コッホ雪片inキューブ環境マッピング
    ID_001401_GL_KOCH_SNOWFLAKE_CUBEMAP_01(1402),
    // 線でHelixを描画
    ID_001500_GL_HELIX_01(1500),
    // 円の位相をずらして描画
    ID_001501_GL_CIRCLE_PHASE_SHIFT_01(1501),
    // 波うつ床01
    ID_001502_GL_WAVE_FLOOR_01(1502),
    // 波うつ床02
    ID_001503_GL_WAVE_FLOOR_02(1503),
    // 円が床を境に円運動
    ID_001504_GL_WAVE_CIRCLE_01(1504),
    // サイン波
    ID_001505_GL_WAVE_SIN_01(1505),
    // 同心円を描画
    ID_001600_GLSL_CONCENTRIC_CIRCLE(1600),
    // オーブを描画
    ID_001601_GLSL_ORB(1601),
    // 放射状に広がる線を描画
    ID_001602_GLSL_ZOOM_LINE(1602),
    // 10個の円が競争する
    ID_001603_GLSL_CIRCLE10_RUN(1603),
}

// ------------------------------
// 描画に使うフラグメントの種類
// ------------------------------
@Parcelize
enum class DrawFragmentType: Parcelable {
    // Drawableを使った描画を実施するフラグメントを生成(製作者名を表示)
    FT_D2_CREDIT_01,
    // Drawableを使った描画を実施するフラグメントを生成
    FT_D2_01,
    // Drawableを使った描画を実施するフラグメントを生成(01＋媒介変数の値を変更するSeekBar１つ)
    FT_D2_02,
    // Drawableを使った描画を実施するフラグメントを生成(01＋媒介変数の値を変更するSeekBar２つ)
    FT_D2_03,
    // Drawableを使った描画を実施するフラグメントを生成(01＋関数式)
    FT_D2_04,
    // Drawableを使った描画を実施するフラグメントを生成(02＋関数式)
    FT_D2_05,
    // Drawableを使った描画を実施するフラグメントを生成(03＋関数式)
    FT_D2_06,
    // OpenGLを使った描画を実施するフラグメントを生成
    //   多面体に様々なエフェクトを施すためのパラメータを付与できる
    FT_D3_ES32_01,
    // OpenGLを使った描画を実施するフラグメントを生成
    //   パラメータなし
    FT_D3_ES32_02,
    // OpenGLを使った描画を実施するフラグメントを生成(製作者名を表示)
    FT_D3_ES32_CREDIT_01,
    // GLSLを使った描画を実施するフラグメントを生成
    FT_GS_ES32_02,
    // GLSLを使った描画を実施するフラグメントを生成(製作者名を表示)
    FT_GS_ES32_CREDIT_01,
}

// ------------------
// 描画に使うビュータイプ
// ------------------
enum class DrawViewType(val viewType: Int) {
    // Drawableを継承
    DVT_DRAWABLE(1),
    // GLSurfaceViewを継承
    DVT_GL(2)
}

// ------------------
// 描画データ
// ------------------
@Parcelize
data class DrawData(
    // 描画データの識別子
    val id: DrawDataID,
    // 描画に使うフラグメントの種類
    val drawFragmentType: DrawFragmentType,
    // タイトル
    val title: String,
    // 描画に使うビュータイプ
    var drawViewType: DrawViewType = DrawViewType.DVT_DRAWABLE,
    // クレジット
    val creditMap: MutableMap<String,String> = mutableMapOf(),
    // 静止画の初期パラメータ
    val stillImageParam: FloatArray = floatArrayOf(),
    // 動画用の初期パラメータ
    val motionImageParam: FloatArray = floatArrayOf(),
    // ---------------------------------------------
    // 編集用パラメータ
    // ---------------------------------------------
    // Square02Fragmentで利用している引数の意味
    // １番目：最小値
    // ２番目：最大値
    // ３番目：動画用の初期パラメータから採用する位置
    // ---------------------------------------------
    val editParam: FloatArray = floatArrayOf(),
    // ---------------------------------------------
    // 関数式
    // ---------------------------------------------
    // キー
    //   funcDesc   => 関数式
    //   autoParam  => 自動で更新されるパラメータ
    //   handParam1 => 手動で更新されるパラメータ１
    //   handParam2 => 手動で更新されるパラメータ２
    // ---------------------------------------------
    val funcDescMap: MutableMap<String, String> = mutableMapOf(),
    // ---------------------------------------------
    // フラグメント用パラメータ
    // ---------------------------------------------
    val fragmentParamMap: MutableMap<String,Int> = mutableMapOf(),
    // 動画用の初期パラメータVer2
    val motionImageV2Param: MutableMap<String,Float> = mutableMapOf()
):  Parcelable
