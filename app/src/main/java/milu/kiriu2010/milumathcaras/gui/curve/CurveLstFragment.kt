package milu.kiriu2010.milumathcaras.gui.curve


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
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.main.DrawDataAdapter
import milu.kiriu2010.milumathcaras.gui.main.DrawDataCallback

class CurveLstFragment : Fragment() {

    // 描画データのリサイクラービュー
    private lateinit var recyclerViewDrawData: RecyclerView

    // 描画データのリサイクラービューのアダプタ
    private lateinit var adapter: DrawDataAdapter

    // 描画データを描画するコールバック
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
        val view = inflater.inflate(R.layout.fragment_curve_lst, container, false)

        val ctx = context ?: return view

        // 描画データのリサイクラービュー
        recyclerViewDrawData = view.findViewById(R.id.recyclerViewDrawData)

        // 描画データのリサイクラービューのレイアウトマネージャ
        val layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false)
        recyclerViewDrawData.layoutManager = layoutManager

        // 描画データのリサイクラービューのアダプタ
        adapter = DrawDataAdapter(ctx, createDrawDataLst()) { drawData ->
            // 描画データをクリックすると、描画するようコールバックを呼び出す
            drawDataCallback?.draw(drawData)
        }
        recyclerViewDrawData.adapter = adapter

        // 描画データのリサイクラービューの区切り線
        val itemDecoration = DividerItemDecoration(ctx,DividerItemDecoration.VERTICAL)
        recyclerViewDrawData.addItemDecoration(itemDecoration)

        return view
    }

    // ------------------------------------------------------
    // 描画データの一覧
    // ------------------------------------------------------
    private fun createDrawDataLst(): MutableList<DrawData> {
        val drawDataLst = mutableListOf<DrawData>()

        // サイクロイド曲線
        drawDataLst.add(DrawData(DrawDataID.ID_CYCLOID_01,resources.getString(R.string.draw_curve_cycloid01)))

        return drawDataLst
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
        fun newInstance() =
            CurveLstFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
