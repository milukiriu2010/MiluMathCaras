package milu.kiriu2010.milumathcaras.gui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import milu.kiriu2010.gui.drawer.DrawerActivity
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuItem
import milu.kiriu2010.milumathcaras.entity.MenuType
import milu.kiriu2010.milumathcaras.gui.curve.CurveDrawFragment
import milu.kiriu2010.milumathcaras.gui.curve.CurveLstFragment
import milu.kiriu2010.milumathcaras.gui.menu.MenuFragment
import milu.kiriu2010.milumathcaras.id.FragmentID

class MainActivity : DrawerActivity()
    , DrawDataCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 画面が消えないようにする
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // ドロワーレイアウトを表示する
        setupDrawLayout()

        if ( savedInstanceState == null ) {
            // 曲線一覧を表示するフラグメントを追加
            if ( supportFragmentManager.findFragmentByTag(FragmentID.ID_CURVE_LST.id) == null ) {
                // 曲線一覧を表示するメニュー
                val menuData = MenuData(MenuType.TYPE_SUB,MenuItem.MENU_CURVE,FragmentID.ID_CURVE_LST,resources.getString(R.string.menu_curve))

                supportFragmentManager.beginTransaction()
                    .add(R.id.frameMain, CurveLstFragment.newInstance(menuData), FragmentID.ID_CURVE_LST.id)
                    .commit()
            }
            // ドロワーレイアウトを表示するフラグメントを追加
            if ( supportFragmentManager.findFragmentByTag(FragmentID.ID_DRAWER_LAYOUT.id) == null ) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.frameMenu,MenuFragment.newInstance(), FragmentID.ID_DRAWER_LAYOUT.id)
                    .commit()
            }
        }
    }

    // -----------------------------------
    // DrawDataCallback
    // 描画データを描画する
    // -----------------------------------
    override fun draw(drawData: DrawData) {
        // 描画データを描画するフラグメントを追加
        if ( supportFragmentManager.findFragmentByTag(FragmentID.ID_DRAW_DATA.id) == null ) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frameMain, CurveDrawFragment.newInstance(drawData), FragmentID.ID_DRAW_DATA.id)
                .commit()
        }
    }

    // -------------------------------------
    // 描画データ一覧を表示する
    // -------------------------------------
    override fun showLst(menuData: MenuData) {
        // 全てのフラグメントを削除
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
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
