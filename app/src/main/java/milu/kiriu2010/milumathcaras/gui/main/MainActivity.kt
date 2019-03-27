package milu.kiriu2010.milumathcaras.gui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.gui.common.ExceptionFragment
import milu.kiriu2010.gui.drawer.DrawerActivity
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuItem
import milu.kiriu2010.milumathcaras.entity.MenuType
import milu.kiriu2010.milumathcaras.gui.drawfragment.Square01Fragment
import milu.kiriu2010.milumathcaras.gui.menu.MenuFragment
import milu.kiriu2010.milumathcaras.id.FragmentID
import java.util.*
import kotlin.Exception

class MainActivity : DrawerActivity()
    , DrawDataCallback {

    // ドロワーレイアウトを表示するフラグメント
    private lateinit var fragmentDrawer: Fragment

    // 選択されたメニューデータ
    private lateinit var selectedMenuData: MenuData

    // メニューとして表示するフラグメントのスタック
    private val menuFragmentStack: Stack<Fragment> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 画面が消えないようにする
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // ドロワーレイアウトを表示する
        setupDrawLayout()

        try {
            if ( savedInstanceState == null ) {
                // メニューフラグメントに表示するメニューデータ一覧を取得
                var menuDataLst = MenuFragment.createMenuDataLst(resources).filter { it.menuType == MenuType.TYPE_SUB }
                // -----------------------------------------------------------------
                // ここの処理は、
                // 描画データ一覧を表示するフラグメントを取得するため実施してる
                // あとで、入れ替える予定
                // -----------------------------------------------------------------
                // 子メニューがあったら、子メニュー一覧を取得する
                while (menuDataLst[0].hasChildMenu) {
                    menuDataLst = MenuFragment.createMenuDataLst(resources,menuDataLst[0]).filter { it.menuType == MenuType.TYPE_SUB }
                }
                // 描画データ一覧を表示するフラグメントを追加
                if ( menuDataLst.size > 0 ) {
                    // メニューの先頭を選択
                    selectedMenuData = menuDataLst[0]

                    // 描画データ一覧を表示するフラグメントを追加
                    addDrawFragment(selectedMenuData)
                }
                // ドロワーレイアウトを表示するフラグメントを追加
                if ( supportFragmentManager.findFragmentByTag(FragmentID.ID_DRAWER_LAYOUT.id) == null ) {
                    fragmentDrawer = MenuFragment.newInstance()

                    supportFragmentManager.beginTransaction()
                        .add(R.id.frameMenu,fragmentDrawer, FragmentID.ID_DRAWER_LAYOUT.id)
                        .commit()
                }
            }
        } catch (ex: Exception) {
            // Exceptionの内容を表示
            showException(ex)
        }
    }

    // -----------------------------------
    // DrawDataCallback
    // 描画データを描画する
    // -----------------------------------
    override fun draw(drawData: DrawData) {
        try {
            // 描画データを描画するフラグメントを追加
            if (supportFragmentManager.findFragmentByTag(FragmentID.ID_DRAW_DATA.id) == null) {
                val fragment = FragmentFactory.createFragment(drawData)

                supportFragmentManager.beginTransaction()
                    .add(R.id.frameMain, fragment, FragmentID.ID_DRAW_DATA.id)
                    .commit()
            }
        } catch (ex: Exception) {
            // Exceptionの内容を表示
            showException(ex)
        }
    }

    // -------------------------------------
    // 描画データ一覧を表示する
    // -------------------------------------
    override fun showLst(menuData: MenuData) {
        try {
            // "戻る"メニュー
            when (menuData.menuItem) {
                MenuItem.MENU_BACK -> {
                    val menuFragment = menuFragmentStack.pop()
                    supportFragmentManager.beginTransaction()
                        .remove(menuFragment)
                        .commit()
                    return
                }
            }

            when (menuData.hasChildMenu) {
                // 子メニューがない場合
                false -> {
                    // タップ時にドロワーを閉じる
                    if ( drawerLayout != null ) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }

                    //  現在選択されているメニューと同じメニューが選択された場合、何もしない
                    if ( menuData == selectedMenuData ) return

                    // 全てのフラグメントを削除
                    //   ・ドロワーレイアウトを表示するフラグメントは除く
                    supportFragmentManager.fragments.forEach {
                        /*
                        if ( it != fragmentDrawer ) {
                            supportFragmentManager.beginTransaction()
                                .remove(it)
                                .commit()
                        }
                        */
                        if ( it !is MenuFragment ) {
                            supportFragmentManager.beginTransaction()
                                .remove(it)
                                .commit()
                        }
                    }

                    selectedMenuData = menuData
                    // 選択したメニューに対応する描画データ一覧を表示するフラグメントを追加
                    addDrawFragment(selectedMenuData)
                }
                // 子メニューがある場合
                true -> {
                    selectedMenuData = menuData

                    // ---------------------------------------------------------
                    // 子メニューに対応するメニューフラグメントを生成＆追加する
                    // ---------------------------------------------------------
                    addMenuFragment(selectedMenuData)                }
            }
        } catch (ex: Exception) {
            // Exceptionの内容を表示
            showException(ex)
        }
    }

    // ---------------------------------------------------
    // メニューに対応する描画フラグメントを生成＆追加する
    // ---------------------------------------------------
    private fun addDrawFragment(menuData: MenuData) {
        try {
            val fragment = FragmentFactory.createFragment(menuData)

            supportFragmentManager.beginTransaction()
                .add(R.id.frameMain, fragment, menuData.fragmentID.id)
                .commit()
        } catch (ex: Exception) {
            // Exceptionの内容を表示
            showException(ex)
        }
    }

    // ---------------------------------------------------------
    // 子メニューに対応するメニューフラグメントを生成＆追加する
    // ---------------------------------------------------------
    private fun addMenuFragment(menuData: MenuData) {
        try {
            val fragment = MenuFragment.newInstance(menuData)

            menuFragmentStack.push(fragment)

            supportFragmentManager.beginTransaction()
                .add(R.id.frameMenu, fragment, menuData.fragmentID.id)
                .commit()
        } catch (ex: Exception) {
            // Exceptionの内容を表示
            showException(ex)
        }
    }

    // ----------------------------------------
    // Exceptionの内容を表示
    // ----------------------------------------
    private fun showException(ex: Exception) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameMain, ExceptionFragment.newInstance(ex.message!!,ex), FragmentID.ID_EXCEPTION.id)
            .commit()
    }

    // -----------------------------------
    // "戻る"ボタン押下
    // -----------------------------------
    override fun onBackPressed() {
        // 描画データを描画するフラグメントが表示されていたらスタックから削除する
        val fragment = supportFragmentManager.findFragmentByTag(FragmentID.ID_DRAW_DATA.id)
        if ( fragment != null ) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        //Log.d(javaClass.simpleName,"MainActivity:id[${item.itemId}]home[${android.R.id.home}]")
        return when (item.itemId) {
            // アップバーのハンバーガーアイコンをクリック
            android.R.id.home -> {
                // --------------------------------------
                // ナビゲーションドロワーを開いていて、
                // 下の階層のメニューを開いていたら、
                // 上の階層のメニューに戻る
                // --------------------------------------
                if ( menuFragmentStack.empty() == false ) {
                    val menuFragment = menuFragmentStack.pop()
                    if ( menuFragment != null ) {
                        supportFragmentManager.beginTransaction()
                            .remove(menuFragment)
                            .commit()
                    }
                    true
                }
                // -----------------------------------------
                // ナビゲーションドロワーを開いていていない
                //  or
                // 一番上の階層のメニューを開いている
                // ⇒
                // デフォルトの動作を実行
                // -----------------------------------------
                else {
                    super.onOptionsItemSelected(item)
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
