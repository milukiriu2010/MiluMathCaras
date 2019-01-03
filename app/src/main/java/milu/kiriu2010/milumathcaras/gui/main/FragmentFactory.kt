package milu.kiriu2010.milumathcaras.gui.main

import android.support.v4.app.Fragment
import milu.kiriu2010.milucal.gui.misc.AboutFragment
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.id.FragmentID
import java.lang.RuntimeException

class FragmentFactory {
    companion object {
        // ------------------------------------------------
        // メニューデータに対応するフラグメントを生成する
        // ------------------------------------------------
        fun createFragment(menuData: MenuData): Fragment {
            return when (menuData.fragmentID) {
                // 描画データ一覧を表示するフラグメントを生成
                FragmentID.ID_DRAW_LST -> CurveLstFragment.newInstance(menuData)
                // "このアプリについて"を表示するフラグメントを生成
                FragmentID.ID_ABOUT -> AboutFragment.newInstance()
                else -> throw RuntimeException("Not Found fragmentID[${menuData.fragmentID.id}]")
            }
        }
    }
}