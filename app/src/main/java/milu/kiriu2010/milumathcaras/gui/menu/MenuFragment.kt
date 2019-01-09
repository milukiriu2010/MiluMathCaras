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

class MenuFragment : Fragment() {

    // メニューを表示するリサイクラービュー
    private lateinit var recyclerViewMenu: RecyclerView

    // メニューを表示するリサイクラービューのアダプタ
    private lateinit var adapter: MenuAdapter

    // 描画データ一覧を表示するコールバック
    private var drawDataCallback: DrawDataCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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

        // メニューを表示するリサイクラービューのアダプタ
        adapter = MenuAdapter(ctx, createMenuDataLst(resources) ) {
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

        // ------------------------------------------------------
        // ドロワーレイアウトに表示するメニューの一覧
        // ------------------------------------------------------
        public fun createMenuDataLst(resources: Resources): MutableList<MenuData> {
            val menuDataLst = mutableListOf<MenuData>()

            // [メインメニュー]
            // 描画リスト
            menuDataLst.add(MenuData(MenuType.TYPE_MAIN, MenuItem.MENU_DUMMY, FragmentID.ID_DUMMY,resources.getString(R.string.menu_main_drawlst)))
            // [サブメニュー]
            // 曲線一覧を表示するメニュー
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_CURVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_curve)))
            // [サブメニュー]
            // エピサイクロイドを表示するメニュー
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_EPICYCLOID, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_epicycloid)))
            // [サブメニュー]
            // ハイポサイクロイドを表示するメニュー
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_HYPOCYCLOID, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_hypocycloid)))
            // [サブメニュー]
            // フラクタル一覧を表示するメニュー
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_FRACTAL, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_fractal)))
            // [サブメニュー]
            // フラクタル(複素数)一覧を表示するメニュー
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_COMPLEX, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_complex)))
            // [サブメニュー]
            // 波一覧を表示するメニュー
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_WAVE, FragmentID.ID_DRAW_LST,resources.getString(R.string.menu_sub_wave)))
            // [メインメニュー]
            // ヘルプ
            menuDataLst.add(MenuData(MenuType.TYPE_MAIN, MenuItem.MENU_DUMMY,FragmentID.ID_DUMMY,resources.getString(R.string.menu_main_help)))
            // [サブメニュー]
            // このアプリについて
            menuDataLst.add(MenuData(MenuType.TYPE_SUB, MenuItem.MENU_DUMMY,FragmentID.ID_ABOUT,resources.getString(R.string.menu_sub_about)))

            return menuDataLst
        }
    }
}
