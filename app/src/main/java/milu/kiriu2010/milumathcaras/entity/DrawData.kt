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
    // 対数螺旋
    ID_000010_LOGARITHMIC_SPIRAL(10),
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
    // シェルピンスキーの三角形
    ID_000105_SIERPINSKI_TRIANGLE(105),
    // シェルピンスキーのカーペット
    ID_000106_SIERPINSKI_CARPET(106),
    // ヒルベルト曲線
    ID_000107_HILBERT_CURVE(107),
    // ムーア曲線
    ID_000108_MOORECURVE(108),
    // 樹木曲線
    ID_000111_TREE_CURVE(111),
    // 樹木曲線
    ID_000112_TREE_CURVE(112),
    // 樹木曲線
    ID_000113_TREE_CURVE(113),
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
    ID_000301_3_IN_4(301),
    // 三角形in五角形
    ID_000302_3_IN_5(302),
    // 三角形in六角形
    ID_000303_3_IN_6(303),
    // 三角形in七角形
    ID_000304_3_IN_7(304),
    // 三角形in八角形
    ID_000305_3_IN_8(305),
    // 三角形in九角形
    ID_000306_3_IN_9(306),
    // 三角形in十角形
    ID_000307_3_IN_10(307),
    // 四角形in五角形
    ID_000311_4_IN_5(311),
    // 四角形in六角形
    ID_000312_4_IN_6(312),
    // 四角形in七角形
    ID_000313_4_IN_7(313),
    // 四角形in八角形
    ID_000314_4_IN_8(314),
    // 四角形in九角形
    ID_000315_4_IN_9(315),
    // 四角形in十角形
    ID_000316_4_IN_10(316),
    // 五角形in六角形
    ID_000321_5_IN_6(321),
    // 五角形in七角形
    ID_000322_5_IN_7(322),
    // 五角形in八角形
    ID_000323_5_IN_8(323),
    // 五角形in九角形
    ID_000324_5_IN_9(324),
    // 五角形in十角形
    ID_000325_5_IN_10(325),
    // 六角形in七角形
    ID_000331_6_IN_7(331),
    // 六角形in八角形
    ID_000332_6_IN_8(332),
    // 六角形in九角形
    ID_000333_6_IN_9(333),
    // 六角形in十角形
    ID_000334_6_IN_10(334),
    // 七角形in八角形
    ID_000341_7_IN_8(341),
    // 七角形in九角形
    ID_000342_7_IN_9(342),
    // 七角形in十角形
    ID_000343_7_IN_10(343),
    // 八角形in九角形
    ID_000351_8_IN_9(351),
    // 八角形in十角形
    ID_000352_8_IN_10(352),
    // 九角形in十角形
    ID_000361_9_IN_10(361),
    // 等加速度運動
    ID_000401_NATURE_UNFORM_MOTION(401),
    // 噴水
    ID_000402_NATURE_FOUNTAIN(402),
    // ランダムウォーク
    ID_000403_NATURE_RANDOM_WALK(403),
    // 加速度
    ID_000404_NATURE_ACCELERATE_TOWARDS_TOUCH_POINT(404),
    // 1536色
    ID_000500_COLOR_1536(500),
    // 768色(暗色)
    ID_000501_COLOR_768_DARK(501),
    // 三角形でEXILE
    ID_000600_TRIANGLE_EXILE(600),
    // 多角形のラップ
    ID_000601_POLYGON_LAP(601),
    // 円⇔正方形の変形
    ID_000602_CIRCLE2SQUARE_MORPH(602),
    // 回転する矢印
    ID_000603_ROTATE_ARROWS(603),
    // 三角形out三角形
    ID_000701_3_OUT_3(701),
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
    // 正方形領域に描画する(媒介変数の値を変更するSeekBar１つ)
    FT_SQUARE_02,
    // 正方形領域に描画する(媒介変数の値を変更するSeekBar２つ)
    FT_SQUARE_03,
    // 長方形領域に描画する
    FT_RECTANGLE_01,
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
    //val credit: Array<String> = arrayOf(),
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
    val editParam: FloatArray = floatArrayOf()
):  Parcelable
