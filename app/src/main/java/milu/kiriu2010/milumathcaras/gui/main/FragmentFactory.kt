package milu.kiriu2010.milumathcaras.gui.main

import android.support.v4.app.Fragment
import android.util.Log
import milu.kiriu2010.milucal.gui.misc.AboutFragment
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawFragmentType
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.gui.drawfragment.Rectangle01Fragment
import milu.kiriu2010.milumathcaras.gui.drawfragment.Square01Fragment
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
                FragmentID.ID_DRAW_LST -> DrawLst01Fragment.newInstance(menuData)
                // "このアプリについて"を表示するフラグメントを生成
                FragmentID.ID_ABOUT -> AboutFragment.newInstance()
                else -> throw RuntimeException("Not Found fragmentID[${menuData.fragmentID.id}]")
            }
        }

        // ------------------------------------------------
        // 描画データに対応するフラグメントを生成する
        // ------------------------------------------------
        fun createFragment(drawData: DrawData): Fragment {
            return when (drawData.drawFragmentType) {
                // 正方形領域に描画データを表示するフラグメントを生成
                DrawFragmentType.FT_SQUARE_01 -> Square01Fragment.newInstance(drawData)
                // 長方形領域に描画データを表示するフラグメントを生成
                DrawFragmentType.FT_RECTANGLE_01 -> Rectangle01Fragment.newInstance(drawData)
                else -> throw RuntimeException("Not Found Fragment[${drawData.drawFragmentType}]")
            }
        }

    }
}