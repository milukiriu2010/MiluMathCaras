package milu.kiriu2010.milumathcaras.gui.menu

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_curve_draw.view.*
import kotlinx.android.synthetic.main.list_row_menu_main.view.*
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuType
import java.lang.RuntimeException

class MenuAdapter(
    context: Context,
    // メニューを表示するリサイクラービューのアダプタに表示するメニュー一覧
    private val menuDataLst: MutableList<MenuData> = mutableListOf(),
    // メニューをクリックしたとき呼び出されるコールバック
    private val onItemClick: (MenuData) -> Unit )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        when (viewType) {
            // メインメニュー
            MenuType.TYPE_MAIN.viewType -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_menu_main,parent,false)
                viewHolder = MenuMainViewHolder(view)
            }
            // サブメニュー
            MenuType.TYPE_SUB.viewType -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_menu_sub,parent,false)
                viewHolder = MenuSubViewHolder(view)
                // アイテムをクリックしたら
                // 当該のMenuDataをコールバックに渡す
                view.setOnClickListener {
                    val pos = viewHolder.adapterPosition
                    val menuData = menuDataLst[pos]
                    onItemClick(menuData)
                }
            }
            // 該当なし
            else -> {
                throw RuntimeException("No match for viewType[$viewType]")
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int = menuDataLst.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        val menuData = menuDataLst[pos]
        when (viewHolder) {
            // メインメニュー
            is MenuMainViewHolder -> {
                // タイトルを設定
                viewHolder.textViewMenuMain.text = menuData.title
            }
            // サブメニュー
            is MenuSubViewHolder -> {
                // タイトルを設定
                viewHolder.textViewMenuSub.text = menuData.title
            }
        }
    }

    // メインメニュー
    class MenuMainViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // タイトル
        val textViewMenuMain = view.findViewById<TextView>(R.id.textViewMenuMain)
    }

    // サブメニュー
    class MenuSubViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // タイトル
        val textViewMenuSub = view.findViewById<TextView>(R.id.textViewMenuSub)
    }
}