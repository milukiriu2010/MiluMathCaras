package milu.kiriu2010.milumathcaras.gui.main

import android.content.res.Resources
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuItem

// ------------------------------------------------------
// 描画データの一覧を生成する
// ------------------------------------------------------
class DrawDataFactory {
    companion object {
        fun createDrawDataLst(menuData: MenuData, resources: Resources): MutableList<DrawData> {
            val drawDataLst = mutableListOf<DrawData>()
            when (menuData.menuItem) {
                // 曲線を選択したときの描画データの一覧
                MenuItem.MENU_CURVE -> {
                    // サイクロイド曲線(cycloid)
                    drawDataLst.add(DrawData(DrawDataID.ID_CYCLOID_01,resources.getString(R.string.draw_curve_cycloid01),floatArrayOf(720f)))
                    // 三芒形/三尖形(deltoid)
                    drawDataLst.add(DrawData(DrawDataID.ID_DELTOID_02,resources.getString(R.string.draw_curve_deltoid02), floatArrayOf(360f,3f),floatArrayOf(0f,3f)))
                    // アステロイド曲線(asteroid)
                    drawDataLst.add(DrawData(DrawDataID.ID_ASTROID_03,resources.getString(R.string.draw_curve_astroid03), floatArrayOf(360f,4f),floatArrayOf(0f,4f)))
                    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_04,resources.getString(R.string.draw_curve_hypocycloid04), floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f)))
                    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_05,resources.getString(R.string.draw_curve_hypocycloid05), floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f)))
                    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_06,resources.getString(R.string.draw_curve_hypocycloid06), floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f)))
                    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_07,resources.getString(R.string.draw_curve_hypocycloid07), floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f)))
                }
            }
            return drawDataLst
        }
    }
}