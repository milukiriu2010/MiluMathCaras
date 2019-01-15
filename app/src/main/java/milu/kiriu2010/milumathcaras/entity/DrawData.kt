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
    ID_001_CYCLOID(1),
    // トロコイド曲線(k=2.0)
    ID_002_TROCHOID(2),
    // トロコイド曲線(k=3.0)
    ID_003_TROCHOID(3),
    // トロコイド曲線(k=4.0)
    ID_004_TROCHOID(4),
    // 対数螺旋
    ID_010_LOGARITHMIC_SPIRAL(10),
    // カージオイド曲線(k=1.0)
    ID_021_CARDIOID(21),
    // エピサイクロイド曲線(k=4.0)
    ID_022_EPICYCLOID(22),
    // エピサイクロイド曲線(k=2.1)
    ID_023_EPICYCLOID(23),
    // エピサイクロイド曲線(k=3.8)
    ID_024_EPICYCLOID(24),
    // エピサイクロイド曲線(k=5.5)
    ID_025_EPICYCLOID(25),
    // エピサイクロイド曲線(k=7.2)
    ID_026_EPICYCLOID(26),
    // 三芒形/三尖形(deltoid)(k=3.0)
    ID_031_DELTOID(31),
    // アステロイド曲線(astroid)(k=4.0)
    ID_032_ASTROID(32),
    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
    ID_033_HYPO_CYCLOID(33),
    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
    ID_034_HYPO_CYCLOID(34),
    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
    ID_035_HYPO_CYCLOID(35),
    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
    ID_036_HYPO_CYCLOID(36),
    // サイン波
    ID_040_SINE_WAVE(40),
    // 高木曲線
    ID_101_TAKAGI_CURVE(101),
    // コッホ雪片
    ID_102_KOCH_SNOWFLAKE(102),
    // 樹木曲線
    ID_103_TREE_CURVE(103),
    // ドラゴン曲線
    ID_104_DRAGON_CURVE(104),
    // シェルピンスキーの三角形
    ID_105_SIERPINSKI_TRIANGLE(105),
    // シェルピンスキーのカーペット
    ID_106_SIERPINSKI_CARPET(106),
    // ヒルベルト曲線
    ID_107_HILBERT_CURVE(107),
    // マンデルブロ―集合
    ID_201_MANDELBRO_SET(201),
    // ジュリア集合
    ID_251_JULIA_SET(251),
    // 三角形でEXILE
    ID_300_TRIANGLE_EXILE(300),
    // 三角形in四角形
    ID_301_3_IN_4(301),
    // 三角形in五角形
    ID_302_3_IN_5(302),
    // 三角形in六角形
    ID_303_3_IN_6(303),
    // 三角形in七角形
    ID_304_3_IN_7(304),
    // 三角形in八角形
    ID_305_3_IN_8(305),
    // 三角形in九角形
    ID_306_3_IN_9(306),
    // 三角形in十角形
    ID_307_3_IN_10(307),
    // 四角形in五角形
    ID_311_4_IN_5(311),
    // 四角形in六角形
    ID_312_4_IN_6(312),
    // 四角形in七角形
    ID_313_4_IN_7(313),
    // 四角形in八角形
    ID_314_4_IN_8(314),
    // 四角形in九角形
    ID_315_4_IN_9(315),
    // 四角形in十角形
    ID_316_4_IN_10(316),
    // 五角形in六角形
    ID_321_5_IN_6(321),
    // 五角形in七角形
    ID_322_5_IN_7(322),
    // 五角形in八角形
    ID_323_5_IN_8(323),
    // 五角形in九角形
    ID_324_5_IN_9(324),
    // 五角形in十角形
    ID_325_5_IN_10(325),
    // 六角形in七角形
    ID_331_6_IN_7(331),
    // 六角形in八角形
    ID_332_6_IN_8(332),
    // 六角形in九角形
    ID_333_6_IN_9(333),
    // 六角形in十角形
    ID_334_6_IN_10(334),
    // 七角形in八角形
    ID_341_7_IN_8(341),
    // 七角形in九角形
    ID_342_7_IN_9(342),
    // 七角形in十角形
    ID_343_7_IN_10(343),
    // 八角形in九角形
    ID_351_8_IN_9(351),
    // 八角形in十角形
    ID_352_8_IN_10(352),
    // 九角形in十角形
    ID_361_9_IN_10(361),
}

// ------------------
// 描画データ
// ------------------
@Parcelize
data class DrawData(
    // 描画データの識別子
    val id: DrawDataID,
    // タイトル
    val title: String,
    // 静止画の初期パラメータ
    val stillImageParam: FloatArray = floatArrayOf(),
    // 動画用の初期パラメータ
    val motionImageParam: FloatArray = floatArrayOf()
):  Parcelable
