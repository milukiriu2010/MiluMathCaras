package milu.kiriu2010.milumathcaras.gui.main

import android.support.v4.app.Fragment
import android.util.Log
import milu.kiriu2010.milucal.gui.misc.AboutFragment
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawFragmentType
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.gui.drawfragment.*
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
                // 製作者名を表示するフラグメントを生成
                DrawFragmentType.FT_CREDIT_01 -> Credit01Fragment.newInstance(drawData)
                // 正方形領域に描画データを表示するフラグメントを生成
                DrawFragmentType.FT_SQUARE_01 -> Square01Fragment.newInstance(drawData)
                // 正方形領域に描画データを表示するフラグメントを生成(01＋媒介変数の値を変更するシークバー１つ)
                DrawFragmentType.FT_SQUARE_02 -> Square02Fragment.newInstance(drawData)
                // 正方形領域に描画データを表示するフラグメントを生成(01＋媒介変数の値を変更するシークバー２つ)
                DrawFragmentType.FT_SQUARE_03 -> Square03Fragment.newInstance(drawData)
                // 正方形領域に描画データを表示するフラグメントを生成(01＋関数式)
                DrawFragmentType.FT_SQUARE_04 -> Square04Fragment.newInstance(drawData)
                // 正方形領域に描画データを表示するフラグメントを生成(02＋関数式)
                DrawFragmentType.FT_SQUARE_05 -> Square05Fragment.newInstance(drawData)
                // 正方形領域に描画データを表示するフラグメントを生成(03＋関数式)
                DrawFragmentType.FT_SQUARE_06 -> Square06Fragment.newInstance(drawData)
                // タッチイベントを受け付けるフラグメントを生成
                DrawFragmentType.FT_TOUCH_01 -> Touch01Fragment.newInstance(drawData)
                // OpenGLを使った描画を実施するフラグメントを生成
                DrawFragmentType.FT_D3_01 -> D3x01Fragment.newInstance(drawData)
                else -> throw RuntimeException("Not Found Fragment[${drawData.drawFragmentType}]")
            }
        }

    }
}