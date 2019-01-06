package milu.kiriu2010.milumathcaras.gui.main

import android.content.res.Resources
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.entity.MenuData
import milu.kiriu2010.milumathcaras.entity.MenuItem
import java.lang.RuntimeException

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
                }
                // エピサイクロイドを選択したときの描画データの一覧
                MenuItem.MENU_EPICYCLOID -> {
                    // カージオイド曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_CARDIOID_021,resources.getString(R.string.draw_curve_cardioid_021), floatArrayOf(360f,1f),floatArrayOf(0f,1f) ))
                    // エピサイクロイド曲線(k=4.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_EPICYCLOID_022,resources.getString(R.string.draw_curve_epicycloid_022), floatArrayOf(360f,4f),floatArrayOf(0f,4f) ))
                    // エピサイクロイド曲線(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_EPICYCLOID_023,resources.getString(R.string.draw_curve_epicycloid_023), floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f) ))
                    // エピサイクロイド曲線(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_EPICYCLOID_024,resources.getString(R.string.draw_curve_epicycloid_024), floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f) ))
                    // エピサイクロイド曲線(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_EPICYCLOID_025,resources.getString(R.string.draw_curve_epicycloid_025), floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f) ))
                    // エピサイクロイド曲線(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_EPICYCLOID_026,resources.getString(R.string.draw_curve_epicycloid_026), floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f) ))
                }
                // ハイポサイクロイドを選択したときの描画データの一覧
                MenuItem.MENU_HYPOCYCLOID -> {
                    // 三芒形/三尖形(deltoid)
                    drawDataLst.add(DrawData(DrawDataID.ID_DELTOID_031,resources.getString(R.string.draw_curve_deltoid_031), floatArrayOf(360f,3f),floatArrayOf(0f,3f) ))
                    // アステロイド曲線(asteroid)
                    drawDataLst.add(DrawData(DrawDataID.ID_ASTROID_032,resources.getString(R.string.draw_curve_astroid_032), floatArrayOf(360f,4f),floatArrayOf(0f,4f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_033,resources.getString(R.string.draw_curve_hypocycloid_033), floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_034,resources.getString(R.string.draw_curve_hypocycloid_034), floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_035,resources.getString(R.string.draw_curve_hypocycloid_035), floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_HYPO_CYCLOID_036,resources.getString(R.string.draw_curve_hypocycloid_036), floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f) ))
                }
                // フラクタルを選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL -> {
                    // 高木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_TAKAGI_CURVE_101,resources.getString(R.string.draw_fractal_takagi_curve_101), floatArrayOf(6f), floatArrayOf(0f) ))
                    // コッホ雪片
                    drawDataLst.add(DrawData(DrawDataID.ID_KOCH_SNOWFLAKE_102,resources.getString(R.string.draw_fractal_koch_snowflake_102), floatArrayOf(4f), floatArrayOf(0f) ))
                    // 樹木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_TREE_CURVE_103,resources.getString(R.string.draw_fractal_tree_curve_103), floatArrayOf(6f), floatArrayOf(0f) ))
                    // ドラゴン曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_DRAGON_CURVE_104,resources.getString(R.string.draw_fractal_dragon_curve_104), floatArrayOf(6f), floatArrayOf(0f) ))
                    // シェルピンスキーの三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_SIERPINSKI_TRIANGLE_105,resources.getString(R.string.draw_fractal_sierpinski_triangle_105), floatArrayOf(2f), floatArrayOf(0f) ))
                    // シェルピンスキーのカーペット
                    drawDataLst.add(DrawData(DrawDataID.ID_SIERPINSKI_CARPET_106,resources.getString(R.string.draw_fractal_sierpinski_carpet_106), floatArrayOf(2f), floatArrayOf(0f) ))
                    // ヒルベルト曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_HILBERT_CURVE_107,resources.getString(R.string.draw_fractal_hilbert_curve_107), floatArrayOf(2f), floatArrayOf(0f) ))
                }
                // フラクタル(複素数)を選択したときの描画データの一覧
                MenuItem.MENU_COMPLEX -> {
                    // マンデルブロ―集合
                    drawDataLst.add(DrawData(DrawDataID.ID_MANDELBRO_SET_201,resources.getString(R.string.draw_complex_mandelbrot_set_201), floatArrayOf(0.1f), floatArrayOf(0.001f) ))
                    // ジュリア集合
                    drawDataLst.add(DrawData(DrawDataID.ID_JULIA_SET_251,resources.getString(R.string.draw_complex_julia_set_251), floatArrayOf(0.1f), floatArrayOf(0.001f) ))
                }
                else -> {
                    throw RuntimeException("No Draw List for ${menuData.menuItem.title}")
                }
            }
            return drawDataLst
        }
    }
}