package milu.kiriu2010.milumathcaras.gui.main

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawViewType
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawableFactory
import java.lang.RuntimeException

class DrawDataV2Adapter(
    val context: Context,
    // 描画データ一覧
    val drawDataLst: MutableList<DrawData> = mutableListOf(),
    // "描画データ"クリック時に呼び出されるコールバック
    private val onItemClicked: (DrawData) -> Unit )
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view: View
        val viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder

        when (viewType) {
            // 2D用のセル
            DrawViewType.DVT_DRAWABLE.viewType -> {
                view = inflater.inflate(R.layout.list_row_draw_data,parent,false)
                viewHolder = DrawDataD2ViewHolder(view)

                // -------------------------------------------------
                // "描画データ"クリック時に呼び出されるコールバック
                // -------------------------------------------------
                view.setOnClickListener {
                    val pos = viewHolder.adapterPosition
                    val drawData = drawDataLst[pos]
                    onItemClicked(drawData)
                }
            }
            // OpenGL用のセル
            DrawViewType.DVT_GL.viewType -> {
                view = inflater.inflate(R.layout.list_row_draw_data,parent,false)
                viewHolder = DrawDataGLViewHolder(view)

                // -------------------------------------------------
                // "描画データ"クリック時に呼び出されるコールバック
                // -------------------------------------------------
                view.setOnClickListener {
                    val pos = viewHolder.adapterPosition
                    val drawData = drawDataLst[pos]
                    onItemClicked(drawData)
                }
            }
            // 該当なし
            else -> {
                throw RuntimeException("No match for viewType[$viewType]")
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int = drawDataLst.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        // 描画データ
        val drawData = drawDataLst[pos]
        when (viewHolder) {
            // 2D用のセル
            is DrawDataD2ViewHolder -> {
                // タイトル
                viewHolder.dataTitle.text = drawData.title
                // サムネイル
                val drawable = MyDrawableFactory.createInstance(drawData.id)
                drawable.calStart(false,*drawData.stillImageParam)
                viewHolder.imaveViewThumbNail.setImageDrawable(drawable)
            }
            // OpenGL用のセル
            is DrawDataGLViewHolder -> {
                // タイトル
                viewHolder.dataTitle.text = drawData.title
                // サムネイル
                /*
                val renderer = MgRendererFactory.createInstance(drawData.id,context)
                val bmp = renderer.createBitmapFromGLSurface()
                viewHolder.imaveViewThumbNail.setImageBitmap(bmp)
                */
            }

        }
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

    // -----------------------------------------------------
    // セルを表示するときのXMLリソースを切り替えるために
    // ビュータイプを返す
    // -----------------------------------------------------
    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)
        val drawData = drawDataLst[position]
        return drawData.drawViewType.viewType
    }

    // 2Dセル用
    class DrawDataD2ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        // タイトル
        val dataTitle = view.findViewById<TextView>(R.id.dataTitle)
        // サムネイル
        val imaveViewThumbNail = view.findViewById<ImageView>(R.id.imageViewThumbNail)
    }

    // GLセル用
    class DrawDataGLViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        // タイトル
        val dataTitle = view.findViewById<TextView>(R.id.dataTitle)
        // サムネイル
        val imaveViewThumbNail = view.findViewById<ImageView>(R.id.imageViewThumbNail)
    }
}
