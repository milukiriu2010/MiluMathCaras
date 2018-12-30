package milu.kiriu2010.milumathcaras.id

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class FragmentID(val id: String): Parcelable {
    // ドロワーレイアウトのメニューを表示するフラグメント
    ID_DRAWER_LAYOUT("fragmentDrawerLayout"),
    // 曲線一覧を表示するフラグメント
    ID_CURVE_LST("fragmentCurveLst"),
    // フラクタル一覧を表示するフラグメント
    //ID_FRACTAL_LST("fragmentFractalLst"),
    // 描画データを描画するフラグメント
    ID_DRAW_DATA("fragmentDrawData")
}
