package milu.kiriu2010.milumathcaras.gui.menu


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuItem
import milu.kiriu2010.milumathcaras.entity.MenuType
import milu.kiriu2010.milumathcaras.gui.main.DrawDataCallback
import milu.kiriu2010.milumathcaras.id.FragmentID

private const val ARG_PARAM1 = "MenuData"

class MenuFragment : Fragment() {
    // 表示するメニューリスト
    private lateinit var menuDataLst: MutableList<MenuData>
    // 親メニュー
    private var menuDataParent: MenuData? = null

    // メニューを表示するリサイクラービュー
    private lateinit var recyclerViewMenu: RecyclerView

    // メニューを表示するリサイクラービューのアダプタ
    private lateinit var adapter: MenuAdapter

    // 描画データ一覧を表示するコールバック
    private var drawDataCallback: DrawDataCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            menuDataParent = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val ctx = context ?: return view

        // メニューを表示するリサイクラービュー
        recyclerViewMenu = view.findViewById(R.id.recyclerViewMenu)

        // メニューを表示するリサイクラービューのレイアウトマネージャ
        val layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL,false)
        recyclerViewMenu.layoutManager = layoutManager

        // メニュー一覧として表示するデータを取り込む
        menuDataLst = createMenuDataLst(resources,menuDataParent)

        // メニューを表示するリサイクラービューのアダプタ
        adapter = MenuAdapter(ctx, menuDataLst ) {
            // 描画データ一覧を表示する
            drawDataCallback?.showLst(it)
        }
        recyclerViewMenu.adapter = adapter

        // 描画データのリサイクラービューの区切り線
        val itemDecoration = DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL)
        recyclerViewMenu.addItemDecoration(itemDecoration)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // 描画データ一覧を表示するコールバック
        if ( context is DrawDataCallback ) {
            drawDataCallback = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        drawDataCallback = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MenuFragment().apply {
                arguments = Bundle().apply {
                }
            }

        @JvmStatic
        fun newInstance(menuData: MenuData) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,menuData)
                }
            }

        // ------------------------------------------------------
        // ドロワーレイアウトに表示するメニューの一覧
        // ------------------------------------------------------
        public fun createMenuDataLst(resources: Resources, menuDataParent: MenuData? = null): MutableList<MenuData> {
            val menuDataLst = mutableListOf<MenuData>()

            when (menuDataParent?.menuItem){
                 null -> {
                    // [メインメニュー]
                    // 描画リスト
                    menuDataLst.add(MenuData(MenuType.TYPE_MAIN, MenuItem.MENU_DUMMY, FragmentID.ID_DUMMY,resources.getString(R.string.menu_main_drawlst),false))
                    // [サブメニュー]
                    // 曲線一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve),true))
                    // [サブメニュー]
                    // フラクタル一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal),true))
                    // [サブメニュー]
                    // 波一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_WAVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_wave),false))
                     // [サブメニュー]
                     // 円一覧を表示するメニュー
                     menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CIRCLE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_circle),true))
                    // [サブメニュー]
                    // 多角形一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon),true))
                     // [サブメニュー]
                     // 多面体一覧を表示するメニュー
                     menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYHEDRON, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polyhedron),true))
                    // [サブメニュー]
                    // "Nature of Code"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_NATURE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_nature),true))
                     // [サブメニュー]
                     // 錯覚一覧を表示するメニュー
                     menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_OPTICAL_ILLUSION, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_optical_Illusion),false))
                     // [サブメニュー]
                     // 色一覧を表示するメニュー
                     menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_COLOR, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_color),false))
                    // [メインメニュー]
                    // ヘルプ
                    menuDataLst.add(MenuData(MenuType.TYPE_MAIN, MenuItem.MENU_DUMMY,FragmentID.ID_DUMMY,resources.getString(R.string.menu_main_help),false))
                    // [サブメニュー]
                    // このアプリについて
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_DUMMY,FragmentID.ID_ABOUT,resources.getString(R.string.menu_sub_about),false))
                }
                // 曲線一覧を表示するメニュー
                MenuItem.MENU_CURVE -> {
                    // [サブメニュー]
                    // トロコイドを表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_TROCHOID, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_trochoid),false))
                    // [サブメニュー]
                    // エピサイクロイドを表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_EPICYCLOID, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_epicycloid),false))
                    // [サブメニュー]
                    // ハイポサイクロイドを表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_HYPOCYCLOID, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_hypocycloid),false))
                    // [サブメニュー]
                    // 対数螺旋一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_SPIRAL_LOGARITHMIC, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_spiral_logarithmic),false))
                    // [サブメニュー]
                    // インボリュート曲線一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_SPIRAL_INVOLUTE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_spiral_involute),false))
                    // [サブメニュー]
                    // リサージュ曲線を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_LISSAJOUS, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_lissajous),false))
                    // [サブメニュー]
                    // レムニスケート曲線を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_LEMNISCATE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_lemniscate),false))
                    // [サブメニュー]
                    // パスカルの蝸牛形を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_LIMACON, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_limacon),false))
                    // [サブメニュー]
                    // 特別な曲線を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_SPECIAL, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_special),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
                // フラクタル一覧を表示するメニュー
                MenuItem.MENU_FRACTAL -> {
                    // [サブメニュー]
                    // フラクタル(再帰)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_RECURSION, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_recursion),false))
                    // [サブメニュー]
                    // フラクタル(樹木曲線)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_TREE_CURVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_tree_curve),false))
                    // [サブメニュー]
                    // フラクタル(コッホ曲線)を描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_KOCH_CURVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_koch_curve),false))
                    // [サブメニュー]
                    // フラクタル(シェルピンスキー系)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_SIERPINSKI_FAMILY, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_sierpinski_family),false))
                    // [サブメニュー]
                    // フラクタル(ゴスパー曲線)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_GOSPER_CURVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_gosper_curve),false))
                    // [サブメニュー]
                    // フラクタル(複素数)一覧を表示するメニュー
                    //menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_COMPLEX, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_complex),false))
                    // [サブメニュー]
                    // フラクタル(マンデルブロ―集合)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_MANDELBROT, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_mandelbrot),false))
                    // [サブメニュー]
                    // フラクタル(ジュリア集合)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_JULIA, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_julia),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
                // 円一覧を表示するメニュー
                MenuItem.MENU_CIRCLE -> {
                    // [サブメニュー]
                    // "複数円"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CIRCLE_CIRCLES, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_circle_circles),false))
                    // [サブメニュー]
                    // "変形する円"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CIRCLE_MORPH, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_circle_morph),false))
                    // [サブメニュー]
                    // 敷き詰めた円を描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CIRCLE_TILE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_circle_tile),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
                // 多角形一覧を表示するメニュー
                MenuItem.MENU_POLYGON -> {
                    // [サブメニュー]
                    // "多角形のMix"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_MIX, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_mix),false))
                    // [サブメニュー]
                    // 敷き詰めた多角形を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_TILE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_tile),false))
                    // [サブメニュー]
                    // "三角形"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_TRIANGLE_CENTER, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_triangle_center),false))
                    // [サブメニュー]
                    // "多角形in多角形"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_PINP, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_pinp),false))
                    // [サブメニュー]
                    // "多角形out多角形"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_POUTP, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_poutp),false))
                    // [サブメニュー]
                    // 多角形をずらして描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_SLIDE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_slide),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
                // 多面体一覧を表示するメニュー
                MenuItem.MENU_POLYHEDRON -> {
                    // [サブメニュー]
                    // 多面体内にフラクタルを描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYHEDRON_FRACTAL, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polyhedron_fractal),false))
                    // [サブメニュー]
                    // 立方体に座標変換を施したものを描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYHEDRON_CUBE_TRANSFORM, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polyhedron_cube_transform),false))
                    // [サブメニュー]
                    // 球体に座標変換を施したものを描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYHEDRON_SPHERE_TRANSFORM, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polyhedron_sphere_transform),false))
                    // [サブメニュー]
                    // 多面体に様々なエフェクトを施したものを描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYHEDRON_VIEW, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polyhedron_view),false))
                    // [サブメニュー]
                    // 多面体の展開図を描画するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYHEDRON_NET, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polyhedron_net),false))
                }
                // "The Nature of Code"の一覧を表示するメニュー
                MenuItem.MENU_NATURE -> {
                    // [サブメニュー]
                    // "Vectors"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_NATURE_VECTORS, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_nature_vectors),false))
                    // [サブメニュー]
                    // "Forces"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_NATURE_FORCES, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_nature_forces),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
            }

            return menuDataLst
        }
    }
}
