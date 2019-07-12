package milu.kiriu2010.milumathcaras.gui.main


import android.content.Context
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
import milu.kiriu2010.milumathcaras.id.FragmentID

private const val ARG_PARAM1 = "menudata"

// ----------------------------------------
// 描画データの一覧を表示するフラグメント
// ----------------------------------------
class DrawLst01Fragment : Fragment() {

    // メニューデータ
    private lateinit var menuData: MenuData

    // 描画データのリサイクラービュー
    private lateinit var recyclerViewDrawData: RecyclerView

    // 描画データのリサイクラービューのアダプタ
    private lateinit var adapter: DrawDataV2Adapter

    // 描画データを描画するコールバック
    private var drawDataCallback: DrawDataCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            menuData = it.getParcelable(ARG_PARAM1) ?: MenuData(MenuType.TYPE_BACK,MenuItem.MENU_BACK,FragmentID.ID_DUMMY,"",false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw_lst_01, container, false)

        val ctx = context ?: return view

        // 描画データのリサイクラービュー
        recyclerViewDrawData = view.findViewById(R.id.recyclerViewDrawData)

        // 描画データのリサイクラービューのレイアウトマネージャ
        val layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false)
        recyclerViewDrawData.layoutManager = layoutManager

        /*
        // 描画データのリサイクラービューのアダプタ
        adapter = DrawDataAdapter(ctx, DrawDataFactory.createDrawDataLst(menuData,resources)) { drawData ->
            //Log.d(javaClass.simpleName,"motionImageParam.size[${drawData.motionImageParam.size}]")

            // 描画データをクリックすると、描画するようコールバックを呼び出す
            // ここでは、MainActivityが呼び出されている
            drawDataCallback?.draw(drawData)
        }
        */

        // 描画データのリサイクラービューのアダプタ
        adapter = DrawDataV2Adapter(ctx, DrawDataFactory.createDrawDataLst(menuData,resources)) { drawData ->
            //Log.d(javaClass.simpleName,"motionImageParam.size[${drawData.motionImageParam.size}]")

            // 描画データをクリックすると、描画するようコールバックを呼び出す
            // ここでは、MainActivityが呼び出されている
            drawDataCallback?.draw(drawData)
        }
        recyclerViewDrawData.adapter = adapter

        // 描画データのリサイクラービューの区切り線
        val itemDecoration = DividerItemDecoration(ctx,DividerItemDecoration.VERTICAL)
        recyclerViewDrawData.addItemDecoration(itemDecoration)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // 描画データを描画するコールバック
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
        fun newInstance(menuData: MenuData) =
            DrawLst01Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,menuData)
                }
            }
    }
}
