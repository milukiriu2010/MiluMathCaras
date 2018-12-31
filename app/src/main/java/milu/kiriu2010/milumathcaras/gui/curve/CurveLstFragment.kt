package milu.kiriu2010.milumathcaras.gui.curve


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.gui.main.DrawDataAdapter
import milu.kiriu2010.milumathcaras.gui.main.DrawDataCallback
import milu.kiriu2010.milumathcaras.gui.main.DrawDataFactory

private const val ARG_PARAM1 = "menudata"

// ----------------------------------------
// 描画データの一覧を表示するフラグメント
// ----------------------------------------
class CurveLstFragment : Fragment() {

    // メニューデータ
    private lateinit var menuData: MenuData

    // 描画データのリサイクラービュー
    private lateinit var recyclerViewDrawData: RecyclerView

    // 描画データのリサイクラービューのアダプタ
    private lateinit var adapter: DrawDataAdapter

    // 描画データを描画するコールバック
    private var drawDataCallback: DrawDataCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            menuData = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_curve_lst, container, false)

        val ctx = context ?: return view

        // 描画データのリサイクラービュー
        recyclerViewDrawData = view.findViewById(R.id.recyclerViewDrawData)

        // 描画データのリサイクラービューのレイアウトマネージャ
        val layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false)
        recyclerViewDrawData.layoutManager = layoutManager

        // 描画データのリサイクラービューのアダプタ
        adapter = DrawDataAdapter(ctx, DrawDataFactory.createDrawDataLst(menuData,resources)) { drawData ->
            //Log.d(javaClass.simpleName,"motionImageParam.size[${drawData.motionImageParam.size}]")

            // 描画データをクリックすると、描画するようコールバックを呼び出す
            drawDataCallback?.draw(drawData)
        }
        recyclerViewDrawData.adapter = adapter

        // 描画データのリサイクラービューの区切り線
        val itemDecoration = DividerItemDecoration(ctx,DividerItemDecoration.VERTICAL)
        recyclerViewDrawData.addItemDecoration(itemDecoration)

        return view
    }

    /*
    // ------------------------------------------------------
    // 描画データの一覧
    // ------------------------------------------------------
    private fun createDrawDataLst(): MutableList<DrawData> {
        val drawDataLst = mutableListOf<DrawData>()

        // サイクロイド曲線(cycloid)
        drawDataLst.add(DrawData(DrawDataID.ID_CYCLOID_001,resources.getString(R.string.draw_curve_cycloid01),floatArrayOf(720f)))
        // 三芒形/三尖形(deltoid)
        drawDataLst.add(DrawData(DrawDataID.ID_DELTOID_002,"Deltoid", floatArrayOf(360f,3f),floatArrayOf(0f,3f)))
        // アステロイド曲線(asteroid)
        drawDataLst.add(DrawData(DrawDataID.ID_ASTROID_003,"Astroid", floatArrayOf(360f,4f),floatArrayOf(0f,4f)))
        // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
        drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_004,"Hypocycloid(k=2.1)", floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f)))
        // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
        drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_005,"Hypocycloid(k=3.8)", floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f)))
        // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
        drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_006,"Hypocycloid(k=5.5)", floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f)))
        // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
        drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_007,"Hypocycloid(k=7.2)", floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f)))

        return drawDataLst
    }
    */

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
            CurveLstFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,menuData)
                }
            }
    }
}
