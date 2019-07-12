package milu.kiriu2010.milumathcaras.gui.menu

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuType
import java.lang.RuntimeException

class MenuAdapter(
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
                //Log.d(javaClass.simpleName,"onCreateViewHolder:MenuMainViewHolder")
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_menu_main,parent,false)
                viewHolder = MenuMainViewHolder(view)
            }
            // サブメニュー
            MenuType.TYPE_SUB.viewType -> {
                //Log.d(javaClass.simpleName,"onCreateViewHolder:MenuSubViewHolder")
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
            // サブメニュー
            MenuType.TYPE_BACK.viewType -> {
                //Log.d(javaClass.simpleName,"onCreateViewHolder:MenuBackViewHolder")
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_menu_back,parent,false)
                viewHolder = MenuBackViewHolder(view)
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
                //Log.d(javaClass.simpleName,"onBindViewHolder:MenuMainViewHolder[${menuData.title}]")
                // タイトルを設定
                viewHolder.textViewMenuMain.text = menuData.title
            }
            // サブメニュー
            is MenuSubViewHolder -> {
                //Log.d(javaClass.simpleName,"onBindViewHolder:MenuSubViewHolder[${menuData.title}]")
                // タイトルを設定
                viewHolder.textViewMenuSub.text = menuData.title
            }
            // 戻るメニュー
            is MenuBackViewHolder -> {
                // タイトルを設定
                viewHolder.textViewMenuBack.text = menuData.title
            }
        }
    }

    // -----------------------------------------------------
    // セルを表示するときのXMLリソースを切り替えるために
    // ビュータイプを返す
    // -----------------------------------------------------
    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)
        val menuData = menuDataLst[position]
        return menuData.menuType.viewType
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

    // 戻るメニュー
    class MenuBackViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // タイトル
        val textViewMenuBack = view.findViewById<TextView>(R.id.textViewMenuBack)
    }
}