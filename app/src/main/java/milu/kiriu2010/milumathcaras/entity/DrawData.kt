package milu.kiriu2010.milumathcaras.entity

import android.os.Parcel
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
    ID_CYCLOID_01(1),
    // 三芒形/三尖形(deltoid)
    ID_DELTOID_02(2),
    // アステロイド曲線(astroid)
    ID_ASTROID_03(3),
    // ハイポサイクロイド曲線
    ID_HYPO_CYCLOID_04(4);
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
