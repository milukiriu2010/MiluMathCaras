package milu.kiriu2010.milumathcaras.gui.main

import android.content.res.Resources
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.*
import java.lang.RuntimeException

// ------------------------------------------------------
// 描画データの一覧を生成する
// ------------------------------------------------------
class DrawDataFactory {
    companion object {
        fun createDrawDataLst(menuData: MenuData, resources: Resources): MutableList<DrawData> {
            val drawDataLst = mutableListOf<DrawData>()
            when (menuData.menuItem) {
                // トロコイドを選択したときの描画データの一覧
                MenuItem.MENU_CURVE_TROCHOID -> {
                    // サイクロイド曲線(cycloid)(k=1.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_001_CYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_001_curve_cycloid), floatArrayOf(720f) ))
                    // トロコイド曲線(trochoid)(k=2.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_002_TROCHOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_002_curve_trochoid), floatArrayOf(720f,2.0f), floatArrayOf(0f,2.0f) ))
                    // トロコイド曲線(trochoid)(k=-2.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_003_TROCHOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_003_curve_trochoid), floatArrayOf(720f,-2.0f), floatArrayOf(0f,-2.0f) ))
                    // トロコイド曲線(trochoid)(k=2.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_004_TROCHOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_004_curve_trochoid), floatArrayOf(720f,0.5f), floatArrayOf(0f,0.5f) ))
                }
                // エピサイクロイドを選択したときの描画データの一覧
                MenuItem.MENU_CURVE_EPICYCLOID -> {
                    // カージオイド曲線(k=1.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_021_CARDIOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_021_curve_cardioid), floatArrayOf(360f,1f),floatArrayOf(0f,1f) ))
                    // エピサイクロイド曲線(k=4.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_022_EPICYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_022_curve_epicycloid), floatArrayOf(360f,4f),floatArrayOf(0f,4f) ))
                    // エピサイクロイド曲線(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_023_EPICYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_023_curve_epicycloid), floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f) ))
                    // エピサイクロイド曲線(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_024_EPICYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_024_curve_epicycloid), floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f) ))
                    // エピサイクロイド曲線(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_025_EPICYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_025_curve_epicycloid), floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f) ))
                    // エピサイクロイド曲線(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_026_EPICYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_026_curve_epicycloid), floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f) ))
                }
                // ハイポサイクロイドを選択したときの描画データの一覧
                MenuItem.MENU_CURVE_HYPOCYCLOID -> {
                    // 三芒形/三尖形(deltoid)(k=3.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_031_DELTOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_031_curve_deltoid), floatArrayOf(360f,3f),floatArrayOf(0f,3f) ))
                    // アステロイド曲線(asteroid)(k=4.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_032_ASTROID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_032_curve_astroid), floatArrayOf(360f,4f),floatArrayOf(0f,4f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_033_HYPO_CYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_033_curve_hypocycloid), floatArrayOf(3600f,2.1f),floatArrayOf(0f,2.1f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_034_HYPO_CYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_034_curve_hypocycloid), floatArrayOf(1800f,3.8f),floatArrayOf(0f,3.8f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_035_HYPO_CYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_035_curve_hypocycloid), floatArrayOf(720f,5.5f),floatArrayOf(0f,5.5f) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_036_HYPO_CYCLOID,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_036_curve_hypocycloid), floatArrayOf(1800f,7.2f),floatArrayOf(0f,7.2f) ))
                }
                // スパイラルを選択したときの描画データの一覧
                MenuItem.MENU_CURVE_SPIRAL -> {
                    // 対数螺旋
                    drawDataLst.add(DrawData(DrawDataID.ID_010_LOGARITHMIC_SPIRAL,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_010_curve_logarithmic_spiral), floatArrayOf(0f) ))
                }
                // フラクタル(再帰)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_RECURSION -> {
                    // 高木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_101_TAKAGI_CURVE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_101_fractal_recursion_takagi_curve), floatArrayOf(6f), floatArrayOf(0f) ))
                    // コッホ雪片
                    drawDataLst.add(DrawData(DrawDataID.ID_102_KOCH_SNOWFLAKE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_102_fractal_recursion_koch_snowflake), floatArrayOf(4f), floatArrayOf(0f) ))
                    // 樹木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_103_TREE_CURVE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_103_fractal_recursion_tree_curve), floatArrayOf(6f), floatArrayOf(0f) ))
                    // ドラゴン曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_104_DRAGON_CURVE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_104_fractal_recursion_dragon_curve), floatArrayOf(6f), floatArrayOf(0f) ))
                    // シェルピンスキーの三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_105_SIERPINSKI_TRIANGLE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_105_fractal_recursion_sierpinski_triangle), floatArrayOf(2f), floatArrayOf(0f) ))
                    // シェルピンスキーのカーペット
                    drawDataLst.add(DrawData(DrawDataID.ID_106_SIERPINSKI_CARPET,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_106_fractal_recursion_sierpinski_carpet), floatArrayOf(2f), floatArrayOf(0f) ))
                    // ヒルベルト曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_107_HILBERT_CURVE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_107_fractal_recursion_hilbert_curve), floatArrayOf(2f), floatArrayOf(0f) ))
                }
                // フラクタル(複素数)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_COMPLEX -> {
                    // マンデルブロ―集合
                    drawDataLst.add(DrawData(DrawDataID.ID_201_MANDELBRO_SET,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_201_fractal_complex_mandelbrot_set), floatArrayOf(0.1f), floatArrayOf(0.001f) ))
                    // ジュリア集合
                    drawDataLst.add(DrawData(DrawDataID.ID_251_JULIA_SET,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_251_fractal_complex_julia_set), floatArrayOf(0.1f), floatArrayOf(0.001f) ))
                }
                // 波を選択したときの描画データの一覧
                MenuItem.MENU_WAVE -> {
                    // サイン波
                    drawDataLst.add(DrawData(DrawDataID.ID_040_SINE_WAVE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_040_wave_sine) ))
                }
                // "多角形with円"を選択したときの描画データの一覧
                MenuItem.MENU_POLYGON_CIRCLE -> {
                    // 三角形でEXILE
                    drawDataLst.add(DrawData(DrawDataID.ID_300_TRIANGLE_EXILE,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_300_polygon_triangle_exile) ))
                }
                // "多角形in多角形"を選択したときの描画データの一覧
                MenuItem.MENU_POLYGON_PINP -> {
                    // 三角形in四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_301_3_IN_4,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_301_polygon_3_in_4), floatArrayOf(4f,3f), floatArrayOf(4f,3f) ))
                    // 三角形in五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_302_3_IN_5,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_302_polygon_3_in_5), floatArrayOf(5f,3f), floatArrayOf(5f,3f) ))
                    // 三角形in六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_303_3_IN_6,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_303_polygon_3_in_6), floatArrayOf(6f,3f), floatArrayOf(6f,3f) ))
                    // 三角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_304_3_IN_7,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_304_polygon_3_in_7), floatArrayOf(7f,3f), floatArrayOf(7f,3f) ))
                    // 三角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_305_3_IN_8,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_305_polygon_3_in_8), floatArrayOf(8f,3f), floatArrayOf(8f,3f) ))
                    // 三角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_306_3_IN_9,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_306_polygon_3_in_9), floatArrayOf(9f,3f), floatArrayOf(9f,3f) ))
                    // 三角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_307_3_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_307_polygon_3_in_10), floatArrayOf(10f,3f), floatArrayOf(10f,3f) ))
                    // 四角形in五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_311_4_IN_5,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_311_polygon_4_in_5), floatArrayOf(5f,4f), floatArrayOf(5f,4f) ))
                    // 四角形in六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_312_4_IN_6,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_312_polygon_4_in_6), floatArrayOf(6f,4f), floatArrayOf(6f,4f) ))
                    // 四角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_313_4_IN_7,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_313_polygon_4_in_7), floatArrayOf(7f,4f), floatArrayOf(7f,4f) ))
                    // 四角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_314_4_IN_8,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_314_polygon_4_in_8), floatArrayOf(8f,4f), floatArrayOf(8f,4f) ))
                    // 四角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_315_4_IN_9,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_315_polygon_4_in_9), floatArrayOf(9f,4f), floatArrayOf(9f,4f) ))
                    // 四角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_316_4_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_316_polygon_4_in_10), floatArrayOf(10f,4f), floatArrayOf(10f,4f) ))
                    // 五角形in六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_321_5_IN_6,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_321_polygon_5_in_6), floatArrayOf(6f,5f), floatArrayOf(6f,5f) ))
                    // 五角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_322_5_IN_7,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_322_polygon_5_in_7), floatArrayOf(7f,5f), floatArrayOf(7f,5f) ))
                    // 五角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_323_5_IN_8,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_323_polygon_5_in_8), floatArrayOf(8f,5f), floatArrayOf(8f,5f) ))
                    // 五角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_324_5_IN_9,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_324_polygon_5_in_9), floatArrayOf(9f,5f), floatArrayOf(9f,5f) ))
                    // 五角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_325_5_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_325_polygon_5_in_10), floatArrayOf(10f,5f), floatArrayOf(10f,5f) ))
                    // 六角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_331_6_IN_7,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_331_polygon_6_in_7), floatArrayOf(7f,6f), floatArrayOf(7f,6f) ))
                    // 六角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_332_6_IN_8,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_332_polygon_6_in_8), floatArrayOf(8f,6f), floatArrayOf(8f,6f) ))
                    // 六角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_333_6_IN_9,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_333_polygon_6_in_9), floatArrayOf(9f,6f), floatArrayOf(9f,6f) ))
                    // 六角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_334_6_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_334_polygon_6_in_10), floatArrayOf(10f,6f), floatArrayOf(10f,6f) ))
                    // 七角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_341_7_IN_8,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_341_polygon_7_in_8), floatArrayOf(8f,7f), floatArrayOf(8f,7f) ))
                    // 七角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_342_7_IN_9,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_342_polygon_7_in_9), floatArrayOf(9f,7f), floatArrayOf(9f,7f) ))
                    // 七角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_343_7_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_343_polygon_7_in_10), floatArrayOf(10f,7f), floatArrayOf(10f,7f) ))
                    // 八角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_351_8_IN_9,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_351_polygon_8_in_9), floatArrayOf(9f,8f), floatArrayOf(9f,8f) ))
                    // 八角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_352_8_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_352_polygon_8_in_10), floatArrayOf(10f,8f), floatArrayOf(10f,8f) ))
                    // 九角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_361_9_IN_10,DrawFragmentType.FT_SQUARE_01,resources.getString(R.string.draw_361_polygon_9_in_10), floatArrayOf(10f,9f), floatArrayOf(10f,9f) ))
                }
                // "Nature of Code"を選択したときの描画データの一覧
                MenuItem.MENU_NATURE_OF_CODE -> {
                    // 等速度運動
                    drawDataLst.add(DrawData(DrawDataID.ID_401_NATURE_UNFORM_MOTION,DrawFragmentType.FT_RECTANGLE_01,resources.getString(R.string.draw_401_uniform_motion) ))
                }
                else -> {
                    throw RuntimeException("No Draw List for ${menuData.menuItem.title}")
                }
            }
            return drawDataLst
        }
    }
}