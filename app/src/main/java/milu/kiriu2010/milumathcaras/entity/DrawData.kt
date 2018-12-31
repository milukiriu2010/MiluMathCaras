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
    // 三芒形/三尖形(deltoid)
    ID_DELTOID_002(2),
    // アステロイド曲線(astroid)
    ID_ASTROID_003(3),
    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
    ID_HYPO_CYCLOID_004(4),
    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
    ID_HYPO_CYCLOID_005(5),
    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
    ID_HYPO_CYCLOID_006(6),
    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
    ID_HYPO_CYCLOID_007(7),
    // 高木曲線
    ID_TAKAGI_CURVE_101(101)
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
