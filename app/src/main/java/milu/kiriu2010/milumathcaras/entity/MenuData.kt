package milu.kiriu2010.milumathcaras.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import milu.kiriu2010.milumathcaras.id.FragmentID

// --------------------------------------------------------
// ドロワーレイアウトで表示を切り替えに使うメニュータイプ
// --------------------------------------------------------
@Parcelize
enum class MenuType(val viewType: Int): Parcelable {
    TYPE_MAIN(0),
    TYPE_SUB(1)
}

// ------------------------------------
// 描画データ一覧を生成に使う項目
// ------------------------------------
@Parcelize
enum class MenuItem(val title: String): Parcelable {
    // ダミーメニュー
    MENU_DUMMY("Dummy"),
    // 曲線を描画するメニュー
    MENU_CURVE("Curve"),
    // トロコイドを描画するメニュー
    MENU_TROCHOID("Trochoid"),
    // エピサイクロイドを描画するメニュー
    MENU_EPICYCLOID("Epicycloid"),
    // ハイポサイクロイドを描画するメニュー
    MENU_HYPOCYCLOID("Hypocycloid"),
    // フラクタルを描画するメニュー
    MENU_FRACTAL("Fractal"),
    // フラクタル(複素数)を描画するメニュー
    MENU_COMPLEX("Complex"),
    // 波を描画するメニュー
    MENU_WAVE("Wave"),
    // 多角形を描画するメニュー
    MENU_POLYGON("Polygon")
}

// ------------------------------------
// メニューデータ
// ------------------------------------
@Parcelize
data class MenuData(
    // ドロワーレイアウトで表示を切り替えに使うメニュータイプ
    val menuType: MenuType,
    // 描画データ一覧を生成に使う項目
    val menuItem: MenuItem,
    // クリック時に呼び出されるフラグメント
    val fragmentID: FragmentID,
    // メニューのタイトル
    var title: String
): Parcelable

