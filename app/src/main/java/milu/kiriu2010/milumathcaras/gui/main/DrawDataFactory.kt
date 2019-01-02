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
                    drawDataLst.add(DrawData(DrawDataID.ID_CYCLOID_001,resources.getString(R.string.draw_curve_cycloid_001), floatArrayOf(720f) ))
                    // 三芒形/三尖形(deltoid)
                    drawDataLst.add(DrawData(DrawDataID.ID_DELTOID_002,resources.getString(R.string.draw_curve_deltoid_002), floatArrayOf(360f,3f),floatArrayOf(0f,3f) ))
                    // アステロイド曲線(asteroid)
                    drawDataLst.add(DrawData(DrawDataID.ID_ASTROID_003,resources.getString(R.string.draw_curve_astroid_003), floatArrayOf(360f,4f),floatArrayOf(0f,4f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_004,resources.getString(R.string.draw_curve_hypocycloid_004), floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_005,resources.getString(R.string.draw_curve_hypocycloid_005), floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_006,resources.getString(R.string.draw_curve_hypocycloid_006), floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_007,resources.getString(R.string.draw_curve_hypocycloid_007), floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f) ))
                }
                // フラクタルを選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL -> {
                    // 高木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_TAKAGI_CURVE_101,resources.getString(R.string.draw_fractal_takagi_curve_101), floatArrayOf(6f), floatArrayOf(0f) ))
                    // コッホ雪片
                    drawDataLst.add(DrawData(DrawDataID.ID_KOCH_SNOWFLAKE_102,resources.getString(R.string.draw_fractal_koch_snowflake_102), floatArrayOf(4f), floatArrayOf(0f) ))
                    // 樹木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_TREE_CURVE_103,resources.getString(R.string.draw_fractal_tree_103), floatArrayOf(4f), floatArrayOf(0f) ))
                }
                // フラクタル(複素数)を選択したときの描画データの一覧
                MenuItem.MENU_COMPLEX -> {
                    // マンデルブロ―集合
                    drawDataLst.add(DrawData(DrawDataID.ID_MANDELBRO_SET_201,resources.getString(R.string.draw_complex_mandelbrot_set_201), floatArrayOf(0.1f), floatArrayOf(0.001f) ))
                }
            }
            return drawDataLst
        }
    }
}