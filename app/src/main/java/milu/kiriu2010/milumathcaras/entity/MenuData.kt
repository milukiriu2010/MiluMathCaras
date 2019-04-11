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
    TYPE_SUB(1),
    TYPE_BACK(2)
}

// ------------------------------------
// 描画データ一覧を生成に使う項目
// ------------------------------------
@Parcelize
enum class MenuItem(val title: String): Parcelable {
    // ダミーメニュー
    MENU_DUMMY("Dummy"),
    // 戻る
    MENU_BACK("Back"),
    // 曲線を描画するメニュー
    MENU_CURVE("Curve"),
    // トロコイドを描画するメニュー
    MENU_CURVE_TROCHOID("Trochoid"),
    // エピサイクロイドを描画するメニュー
    MENU_CURVE_EPICYCLOID("Epicycloid"),
    // ハイポサイクロイドを描画するメニュー
    MENU_CURVE_HYPOCYCLOID("Hypocycloid"),
    // 対数螺旋を描画するメニュー
    MENU_CURVE_SPIRAL_LOGARITHMIC("Logarithmic Spiral"),
    // インボリュート曲線を描画するメニュー
    MENU_CURVE_SPIRAL_INVOLUTE("Involute"),
    // リサージュ曲線を描画するメニュー
    MENU_CURVE_LISSAJOUS("Lissajous"),
    // レムニスケート曲線を描画するメニュー
    MENU_CURVE_LEMNISCATE("Lemniscate"),
    // フラクタルを描画するメニュー
    MENU_FRACTAL("Fractal"),
    // フラクタル(再帰)を描画するメニュー
    MENU_FRACTAL_RECURSION("Recursion"),
    // フラクタル(樹木曲線)を描画するメニュー
    MENU_FRACTAL_TREE_CURVE("Tree Curve"),
    // フラクタル(シェルピンスキー系)を描画するメニュー
    MENU_FRACTAL_SIERPINSKI_FAMILY("Sierpinski Family"),
    // フラクタル(ごスパー曲線)を描画するメニュー
    MENU_FRACTAL_GOSPER_CURVE("Gosper Curve"),
    // フラクタル(複素数)を描画するメニュー
    MENU_FRACTAL_COMPLEX("Complex"),
    // フラクタル(マンデルブロ―集合)を描画するメニュー
    MENU_FRACTAL_MANDELBROT("MandelBrot"),
    // フラクタル(ジュリア集合)を描画するメニュー
    MENU_FRACTAL_JULIA("Julia"),
    // 波を描画するメニュー
    MENU_WAVE("Wave"),
    // 円を描画するメニュー
    MENU_CIRCLE("Circle"),
    // 複数円を描画するメニュー
    MENU_CIRCLE_CIRCLES("Circles"),
    // 変形した円を描画するメニュー
    MENU_CIRCLE_MORPH("Morphing Circle"),
    // 敷き詰めた円を描画するメニュー
    MENU_CIRCLE_TILE("Tiling Circles"),
    // 多角形を描画するメニュー
    MENU_POLYGON("Polygon"),
    // "多角形のMIX"を描画するメニュー
    MENU_POLYGON_MIX("Polygon Mix"),
    // 敷き詰めた多角形を描画するメニュー
    MENU_POLYGON_TILE("Tiling Polygons"),
    // 三角形の中心を描画するメニュー
    MENU_POLYGON_TRIANGLE_CENTER("Triangle Center"),
    // "多角形in多角形"を描画するメニュー
    MENU_POLYGON_PINP("P in P"),
    // "多角形out多角形"を描画するメニュー
    MENU_POLYGON_POUTP("P out P"),
    // 多角形をずらして描画するメニュー
    MENU_POLYGON_SLIDE("Polygon Slide"),
    // The Nature of Code
    // https://natureofcode.com/book/chapter-1-vectors/
    MENU_NATURE( "Nature"),
    // "The Nature of Code" - "Vectors"
    MENU_NATURE_VECTORS( "Vectors"),
    // "The Nature of Code" - "Forces"
    MENU_NATURE_FORCES( "Forces"),
    // 色を描画するメニュー
    MENU_COLOR("Color"),
    // 錯覚を描画するメニュー
    MENU_OPTICAL_ILLUSION("Optical Illusion")
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
    var title: String,
    // 子メニューがあるかどうか
    val hasChildMenu: Boolean
): Parcelable

