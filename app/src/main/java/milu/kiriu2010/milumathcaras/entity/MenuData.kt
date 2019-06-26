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
    // パスカルの蝸牛形を描画するメニュー
    MENU_CURVE_LIMACON("Limacon"),
    // SIN/COSのミックスを描画するメニュー
    MENU_CURVE_MIX_SINCOS("Mix sin/cos"),
    // 特別な形の曲線を描画するメニュー
    MENU_CURVE_SPECIAL_SHAPE("Special Shape"),
    // フラクタルを描画するメニュー
    MENU_FRACTAL("Fractal"),
    // フラクタル(再帰)を描画するメニュー
    MENU_FRACTAL_RECURSION("Recursion"),
    // フラクタル(樹木曲線)を描画するメニュー
    MENU_FRACTAL_TREE_CURVE("Tree Curve"),
    // フラクタル(コッホ曲線)を描画するメニュー
    MENU_FRACTAL_KOCH_CURVE("Koch Curve"),
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
    // SIN/COS波を描画するメニュー
    MENU_WAVE_SINCOS("Sin/Cos"),
    // 色を描画するメニュー
    MENU_WAVE_COLOR("Color"),
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
    // 錯覚を描画するメニュー
    MENU_OPTICAL_ILLUSION("Optical Illusion"),
    // 多面体を描画するメニュー
    MENU_POLYHEDRON("Polyhedron"),
    // 多面体に様々なエフェクトを施したものを描画するメニュー
    MENU_POLYHEDRON_VIEW("Polyhedron View"),
    // 立方体に座標変換を施したものを描画するメニュー
    MENU_POLYHEDRON_CUBE_TRANSFORM("Cube Transform"),
    // 球体に座標変換を施したものを描画するメニュー
    MENU_POLYHEDRON_SPHERE_TRANSFORM("Sphere Transform"),
    // 多面体の展開図を描画するメニュー
    MENU_POLYHEDRON_NET("Net of Polyhedron"),
    // 多面体内にフラクタルを描画するメニュー
    MENU_POLYHEDRON_FRACTAL("Fractal in Polyhedron"),
    // ３次元波を描画するメニュー
    MENU_POLYHEDRON_WAVE("3D Wave"),
    // GLSL
    MENU_POLYHEDRON_GLSL("GLSL")
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

