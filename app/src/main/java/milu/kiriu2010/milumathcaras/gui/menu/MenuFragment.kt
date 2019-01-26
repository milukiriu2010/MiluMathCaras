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
                    // 多角形一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon),true))
                    // [サブメニュー]
                    // "Nature of Code"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_NATURE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_nature),false))
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
                    // スパイラル一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_SPIRAL, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_spiral),false))
                    // [サブメニュー]
                    // リサージュ曲線を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE_LISSAJOUS, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve_lissajous),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
                // フラクタル一覧を表示するメニュー
                MenuItem.MENU_FRACTAL -> {
                    // [サブメニュー]
                    // フラクタル(再帰)一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL_RECURSION, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal_recursion),false))
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
                // 多角形一覧を表示するメニュー
                MenuItem.MENU_POLYGON -> {
                    // [サブメニュー]
                    // "多角形with円"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_CIRCLE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_circle),false))
                    // [サブメニュー]
                    // "多角形in多角形"一覧を表示するメニュー
                    menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_POLYGON_PINP, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_polygon_pinp),false))
                    // [戻るメニュー]
                    menuDataLst.add(MenuData(MenuType.TYPE_BACK, MenuItem.MENU_BACK, FragmentID.ID_DUMMY,resources.getString(R.string.menu_back),false))
                }
            }

            return menuDataLst
        }
    }
}
