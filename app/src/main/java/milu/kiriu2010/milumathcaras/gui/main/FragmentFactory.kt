package milu.kiriu2010.milumathcaras.gui.main

import android.support.v4.app.Fragment
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
                // Drawableを使った描画を実施するフラグメントを生成(製作者名を表示)
                DrawFragmentType.FT_D2_CREDIT_01 -> D2xCredit01Fragment.newInstance(drawData)
                // Drawableを使った描画を実施するフラグメントを生成
                DrawFragmentType.FT_D2_01 -> D2x01Fragment.newInstance(drawData)
                // Drawableを使った描画を実施するフラグメントを生成(01＋媒介変数の値を変更するシークバー１つ)
                DrawFragmentType.FT_D2_02 -> D2x02Fragment.newInstance(drawData)
                // Drawableを使った描画を実施するフラグメントを生成(01＋媒介変数の値を変更するシークバー２つ)
                DrawFragmentType.FT_D2_03 -> D2x03Fragment.newInstance(drawData)
                // Drawableを使った描画を実施するフラグメントを生成(01＋関数式)
                DrawFragmentType.FT_D2_04 -> D2x04Fragment.newInstance(drawData)
                // Drawableを使った描画を実施するフラグメントを生成(02＋関数式)
                DrawFragmentType.FT_D2_05 -> D2x05Fragment.newInstance(drawData)
                // Drawableを使った描画を実施するフラグメントを生成(03＋関数式)
                DrawFragmentType.FT_D2_06 -> D2x06Fragment.newInstance(drawData)
                // OpenGLを使った描画を実施するフラグメントを生成
                //   多面体に様々なエフェクトを施すためのパラメータを付与できる
                DrawFragmentType.FT_D3_ES32_01 -> D3xES32x01Fragment.newInstance(drawData)
                // OpenGLを使った描画を実施するフラグメントを生成
                //   パラメータなし
                DrawFragmentType.FT_D3_ES32_02 -> D3xES32x02Fragment.newInstance(drawData)
                // OpenGLを使った描画を実施するフラグメントを生成(製作者名を表示)
                DrawFragmentType.FT_D3_ES32_CREDIT_01 -> D3xES32xCredit01Fragment.newInstance(drawData)
                // GLSLを使った描画を実施するフラグメントを生成
                DrawFragmentType.FT_GS_ES32_02 -> GSxES32x02Fragment.newInstance(drawData)
                // GLSLを使った描画を実施するフラグメントを生成(製作者名を表示)
                DrawFragmentType.FT_GS_ES32_CREDIT_01 -> GSxES32xCredit01Fragment.newInstance(drawData)
                //else -> throw RuntimeException("Not Found Fragment[${drawData.drawFragmentType}]")
            }
        }

    }
}