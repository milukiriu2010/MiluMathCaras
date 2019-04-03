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
    // サイン波
    ID_000040_SINE_WAVE(40),
    // 円の周りを回転するサイン波
    ID_000041_SINE_WAVE_CIRCLE(41),
    // 円の周りを回転するサイン波
    ID_000042_SINE_WAVE_CIRCLE(42),
    // リサージュ曲線(p:q=1:2)
    ID_000050_LISSAJOUS_CURVE_1_2(50),
    // リサージュ曲線(p:q=3:2)
    ID_000051_LISSAJOUS_CURVE_3_2(51),
    // リサージュ曲線(p:q=3:4)
    ID_000052_LISSAJOUS_CURVE_3_4(52),
    // 高木曲線
    ID_000101_TAKAGI_CURVE(101),
    // コッホ雪片
    ID_000102_KOCH_SNOWFLAKE(102),
    // ドラゴン曲線
    ID_000104_DRAGON_CURVE(104),
    // ヒルベルト曲線
    ID_000107_HILBERT_CURVE(107),
    // ムーア曲線
    ID_000108_MOORECURVE(108),
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
    // ゴスパー曲線
    ID_000118_GOSPER_CURVE(118),
    // シェルピンスキーの三角形
    ID_000121_SIERPINSKI_TRIANGLE(121),
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
    // マンデルブロ―集合(白黒)
    ID_000200_MANDELBRO_SET(200),
    // マンデルブロ―集合(-1.5-1.0i～+0.5+1.0i)
    ID_000201_MANDELBRO_SET(201),
    // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
    ID_000202_MANDELBRO_SET(202),
    // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
    ID_000203_MANDELBRO_SET(203),
    // ジュリア集合
    ID_000251_JULIA_SET(251),
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
    // 1536色
    ID_000500_COLOR_1536(500),
    // 768色(暗色)
    ID_000501_COLOR_768_DARK(501),
    // 三角形でEXILE
    ID_000600_TRIANGLE_EXILE(600),
    // 多角形のラップ
    ID_000601_POLYGON_LAP(601),
    // 回転する矢印
    ID_000602_ROTATE_ARROWS(602),
    // 回転しながら三角形を合体
    ID_000603_TRIANGLE_UNITE(603),
    // 正方形⇔ひし形
    ID_000604_SQUARE_2_DIAMOND(604),
    // 三角形⇔ひし型
    ID_000605_TRIANGLE_2_DIAMOND(605),
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
    ID_000711_4_OUT_3(711),
    // 四角形out四角形
    ID_000712_4_OUT_4(712),
    // 四角形out五角形
    ID_000713_4_OUT_5(713),
    // 四角形out六角形
    ID_000714_4_OUT_6(714),
    // 四角形out七角形
    ID_000715_4_OUT_7(715),
    // 四角形out八角形
    ID_000716_4_OUT_8(716),
    // 四角形out九角形
    ID_000717_4_OUT_9(717),
    // 四角形out十角形
    ID_000718_4_OUT_10(718),
    // 五角形out三角形
    ID_000721_5_OUT_3(721),
    // 五角形out四角形
    ID_000722_5_OUT_4(722),
    // 五角形out五角形
    ID_000723_5_OUT_5(723),
    // 五角形out六角形
    ID_000724_5_OUT_6(724),
    // 五角形out七角形
    ID_000725_5_OUT_7(725),
    // 五角形out八角形
    ID_000726_5_OUT_8(726),
    // 五角形out九角形
    ID_000727_5_OUT_9(727),
    // 五角形out十角形
    ID_000728_5_OUT_10(728),
    // 六角形out三角形
    ID_000731_6_OUT_3(731),
    // 六角形out四角形
    ID_000732_6_OUT_4(732),
    // 六角形out五角形
    ID_000733_6_OUT_5(733),
    // 六角形out六角形
    ID_000734_6_OUT_6(734),
    // 六角形out七角形
    ID_000735_6_OUT_7(735),
    // 六角形out八角形
    ID_000736_6_OUT_8(736),
    // 六角形out九角形
    ID_000737_6_OUT_9(737),
    // 六角形out十角形
    ID_000738_6_OUT_10(738),
    // 七角形out三角形
    ID_000741_7_OUT_3(741),
    // 七角形out四角形
    ID_000742_7_OUT_4(742),
    // 七角形out五角形
    ID_000743_7_OUT_5(743),
    // 七角形out六角形
    ID_000744_7_OUT_6(744),
    // 七角形out七角形
    ID_000745_7_OUT_7(745),
    // 七角形out八角形
    ID_000746_7_OUT_8(746),
    // 七角形out九角形
    ID_000747_7_OUT_9(747),
    // 七角形out十角形
    ID_000748_7_OUT_10(748),
    // 八角形out三角形
    ID_000751_8_OUT_3(751),
    // 八角形out四角形
    ID_000752_8_OUT_4(752),
    // 八角形out五角形
    ID_000753_8_OUT_5(753),
    // 八角形out六角形
    ID_000754_8_OUT_6(754),
    // 八角形out七角形
    ID_000755_8_OUT_7(755),
    // 八角形out八角形
    ID_000756_8_OUT_8(756),
    // 八角形out九角形
    ID_000757_8_OUT_9(757),
    // 八角形out十角形
    ID_000758_8_OUT_10(758),
    // 九角形out三角形
    ID_000761_9_OUT_3(761),
    // 九角形out四角形
    ID_000762_9_OUT_4(762),
    // 九角形out五角形
    ID_000763_9_OUT_5(763),
    // 九角形out六角形
    ID_000764_9_OUT_6(764),
    // 九角形out七角形
    ID_000765_9_OUT_7(765),
    // 九角形out八角形
    ID_000766_9_OUT_8(766),
    // 九角形out九角形
    ID_000767_9_OUT_9(767),
    // 九角形out十角形
    ID_000768_9_OUT_10(768),
    // 十角形out三角形
    ID_000771_10_OUT_3(771),
    // 十角形out四角形
    ID_000772_10_OUT_4(772),
    // 十角形out五角形
    ID_000773_10_OUT_5(773),
    // 十角形out六角形
    ID_000774_10_OUT_6(774),
    // 十角形out七角形
    ID_000775_10_OUT_7(775),
    // 十角形out八角形
    ID_000776_10_OUT_8(776),
    // 十角形out九角形
    ID_000777_10_OUT_9(777),
    // 十角形out十角形
    ID_000778_10_OUT_10(778),
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
    // 円⇔正方形の変形
    ID_000810_CIRCLE_MORPH_CIRCLE2SQUARE(810),
    // "円⇔正方形の変形"のタイリング
    ID_000811_CIRCLE_MORPH_CIRCLE2SQUARE(811),
    // "正方形の中で大きくなる円"のタイリング
    ID_000820_CIRCLE_TILE_CIRCLE2SQUARE(820),
    // クリスマスツリー(円を三角形上に並べる)
    ID_000821_CIRCLE_XMASTREE(821),
    // 青・赤・黄３つの円を回転する
    ID_000822_CIRCLE_TILE_ROTATE_CIRCLE_01(822),
    // "Stepping Feet"
    ID_000900_OPTICAL_ILLUSION_STEPPING_FEET(900),
    // "Stereokinetic Effect(SKE)"
    ID_000901_OPTICAL_ILLUSION_STEREOKINETIC_EFFECT(901),
}

// ------------------------------
// 描画に使うフラグメントの種類
// ------------------------------
@Parcelize
enum class DrawFragmentType: Parcelable {
    // 製作者名を表示する
    FT_CREDIT_01,
    // 正方形領域に描画する
    FT_SQUARE_01,
    // 正方形領域に描画する(01＋媒介変数の値を変更するSeekBar１つ)
    FT_SQUARE_02,
    // 正方形領域に描画する(01＋媒介変数の値を変更するSeekBar２つ)
    FT_SQUARE_03,
    // 正方形領域に描画する(01＋関数式)
    FT_SQUARE_04,
    // 正方形領域に描画する(02＋関数式)
    FT_SQUARE_05,
    // 正方形領域に描画する(03＋関数式)
    FT_SQUARE_06,
    // タッチイベントを受け付ける
    FT_TOUCH_01
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
    val fragmentParamMap: MutableMap<String,Int> = mutableMapOf()
):  Parcelable
