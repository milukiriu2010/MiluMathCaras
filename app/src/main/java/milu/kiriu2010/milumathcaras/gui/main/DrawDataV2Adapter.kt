package milu.kiriu2010.milumathcaras.gui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawableFactory

class DrawDataV2Adapter(
    val context: Context,
    // 描画データ一覧
    val drawDataLst: MutableList<DrawData> = mutableListOf(),
    // "描画データ"クリック時に呼び出されるコールバック
    private val onItemClicked: (DrawData) -> Unit )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        when (viewType) {
            else -> {
                view = inflater.inflate(R.layout.list_row_draw_data,parent,false)
                viewHolder = DrawDataViewHolder(view)

                // -------------------------------------------------
                // "描画データ"クリック時に呼び出されるコールバック
                // -------------------------------------------------
                view.setOnClickListener {
                    val pos = viewHolder.adapterPosition
                    val drawData = drawDataLst[pos]
                    onItemClicked(drawData)
                }
            }
        }

        return viewHolder
        /*
        val view = inflater.inflate(R.layout.list_row_draw_data,parent,false)
        val viewHolder = DrawDataViewHolder(view)

        // -------------------------------------------------
        // "描画データ"クリック時に呼び出されるコールバック
        // -------------------------------------------------
        view.setOnClickListener {
            val pos = viewHolder.adapterPosition
            val drawData = drawDataLst[pos]
            onItemClicked(drawData)
        }

        return viewHolder
        */
    }

    override fun getItemCount(): Int = drawDataLst.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        /*
        // 描画データ
        val drawData = drawDataLst[pos]
        // タイトル
        viewHolder.dataTitle.text = drawData.title
        // サムネイル
        val drawable = MyDrawableFactory.createInstance(drawData.id)
        drawable.calStart(false,*drawData.stillImageParam)
        viewHolder.imaveViewThumbNail.setImageDrawable(drawable)
        */
    }

    class DrawDataViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // タイトル
        val dataTitle = view.findViewById<TextView>(R.id.dataTitle)
        // サムネイル
        val imaveViewThumbNail = view.findViewById<ImageView>(R.id.imageViewThumbNail)
    }
}
