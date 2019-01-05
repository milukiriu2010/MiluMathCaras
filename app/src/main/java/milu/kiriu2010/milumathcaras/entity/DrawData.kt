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
    // サイクロイド曲線
    ID_CYCLOID_001(1),
    // カージオイド曲線
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
    // 三芒形/三尖形(deltoid)
    ID_DELTOID_031(31),
    // アステロイド曲線(astroid)
    ID_ASTROID_032(32),
    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
    ID_HYPO_CYCLOID_033(33),
    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
    ID_HYPO_CYCLOID_034(34),
    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
    ID_HYPO_CYCLOID_035(35),
    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
    ID_HYPO_CYCLOID_036(36),
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
    // マンデルブロ―集合
    ID_MANDELBRO_SET_201(201)
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
