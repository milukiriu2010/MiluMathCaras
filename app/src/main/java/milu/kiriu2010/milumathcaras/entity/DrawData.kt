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
    ID_CYCLOID_001(1),
    // トロコイド曲線(k=2.0)
    ID_TROCHOID_002(2),
    // トロコイド曲線(k=3.0)
    ID_TROCHOID_003(3),
    // トロコイド曲線(k=4.0)
    ID_TROCHOID_004(4),
    // 対数螺旋
    ID_LOGARITHMIC_SPIRAL_010(10),
    // カージオイド曲線(k=1.0)
    ID_CARDIOID_021(21),
    // エピサイクロイド曲線(k=4.0)
    ID_EPICYCLOID_022(22),
    // エピサイクロイド曲線(k=2.1)
    ID_EPICYCLOID_023(23),
    // エピサイクロイド曲線(k=3.8)
    ID_EPICYCLOID_024(24),
    // エピサイクロイド曲線(k=5.5)
    ID_EPICYCLOID_025(25),
    // エピサイクロイド曲線(k=7.2)
    ID_EPICYCLOID_026(26),
    // 三芒形/三尖形(deltoid)(k=3.0)
    ID_DELTOID_031(31),
    // アステロイド曲線(astroid)(k=4.0)
    ID_ASTROID_032(32),
    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
    ID_HYPO_CYCLOID_033(33),
    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
    ID_HYPO_CYCLOID_034(34),
    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
    ID_HYPO_CYCLOID_035(35),
    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
    ID_HYPO_CYCLOID_036(36),
    // サイン波
    ID_SINE_WAVE_040(40),
    // 高木曲線
    ID_TAKAGI_CURVE_101(101),
    // コッホ雪片
    ID_KOCH_SNOWFLAKE_102(102),
    // 樹木曲線
    ID_TREE_CURVE_103(103),
    // ドラゴン曲線
    ID_DRAGON_CURVE_104(104),
    // シェルピンスキーの三角形
    ID_SIERPINSKI_TRIANGLE_105(105),
    // シェルピンスキーのカーペット
    ID_SIERPINSKI_CARPET_106(106),
    // ヒルベルト曲線
    ID_HILBERT_CURVE_107(107),
    // マンデルブロ―集合
    ID_MANDELBRO_SET_201(201),
    // ジュリア集合
    ID_JULIA_SET_251(251),
    // 三角形でEXILE
    ID_TRIANGLE_EXILE_301(301),
    // 三角形in四角形
    ID_3_IN_4_302(302),
    // 三角形in五角形
    ID_3_IN_5_303(303),
    // 三角形in六角形
    ID_3_IN_6_304(304),
    // 三角形in七角形
    ID_3_IN_7_305(305),
    // 三角形in八角形
    ID_3_IN_8_306(306),
    // 三角形in九角形
    ID_3_IN_9_307(307),
    // 三角形in
    ID_3_IN_10_308(308),
    // 四角形in五角形
    ID_4_IN_5_309(309),
    // 四角形in六角形
    ID_4_IN_6_310(310),
    // 四角形in七角形
    ID_4_IN_7_311(311)
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
