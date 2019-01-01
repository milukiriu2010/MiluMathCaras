package milu.kiriu2010.milumathcaras.gui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.gui.common.ExceptionFragment
import milu.kiriu2010.gui.drawer.DrawerActivity
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuType
import milu.kiriu2010.milumathcaras.gui.curve.CurveDrawFragment
import milu.kiriu2010.milumathcaras.gui.curve.CurveLstFragment
import milu.kiriu2010.milumathcaras.gui.menu.MenuFragment
import milu.kiriu2010.milumathcaras.id.FragmentID
import kotlin.Exception

class MainActivity : DrawerActivity()
    , DrawDataCallback {

    // ドロワーレイアウトを表示するフラグメント
    private lateinit var fragmentDrawer: Fragment

    // 選択されたメニューデータ
    private lateinit var selectedMenuData: MenuData

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
                val menuDataLst = MenuFragment.createMenuDataLst(resources).filter { it.menuType == MenuType.TYPE_SUB }
                if ( menuDataLst.size > 0 ) {
                    // メニューの先頭を選択
                    selectedMenuData = menuDataLst[0]

                    // 描画データ一覧を表示するフラグメントを追加
                    addFragment(selectedMenuData)
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
                supportFragmentManager.beginTransaction()
                    .add(R.id.frameMain, CurveDrawFragment.newInstance(drawData), FragmentID.ID_DRAW_DATA.id)
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
            // タップ時にドロワーを閉じる
            if ( drawerLayout != null ) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            //  現在選択されているメニューと同じメニューが選択された場合、何もしない
            if ( menuData == selectedMenuData ) return

            // 全てのフラグメントを削除
            //   ・ドロワーレイアウトを表示するフラグメントは除く
            supportFragmentManager.fragments.forEach {
                if ( it != fragmentDrawer ) {
                    supportFragmentManager.beginTransaction()
                        .remove(it)
                        .commit()
                }
            }

            selectedMenuData = menuData
            // 選択したメニューに対応する描画データ一覧を表示するフラグメントを追加
            addFragment(selectedMenuData)
        } catch (ex: Exception) {
            // Exceptionの内容を表示
            showException(ex)
        }
    }

    // ------------------------------------------------
    // メニューに対応するフラグメントを生成＆追加する
    // ------------------------------------------------
    private fun addFragment(menuData: MenuData) {
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
}
