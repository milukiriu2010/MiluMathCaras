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
                    drawDataLst.add(DrawData(DrawDataID.ID_000001_CYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000001_curve_cycloid),
                        stillImageParam = floatArrayOf(720f,1.0f), motionImageParam = floatArrayOf(0f,1.0f), editParam = floatArrayOf(-5f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_trochoid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 20) ))
                    // トロコイド曲線(trochoid)(k=2.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000002_TROCHOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000002_curve_trochoid),
                        stillImageParam = floatArrayOf(720f,2.0f), motionImageParam = floatArrayOf(0f,2.0f), editParam = floatArrayOf(-5f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_trochoid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 20) ))
                    // トロコイド曲線(trochoid)(k=-2.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000003_TROCHOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000003_curve_trochoid),
                        stillImageParam = floatArrayOf(720f,-2.0f), motionImageParam = floatArrayOf(0f,-2.0f), editParam = floatArrayOf(-5f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_trochoid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 20) ))
                    // トロコイド曲線(trochoid)(k=2.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000004_TROCHOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000004_curve_trochoid),
                        stillImageParam = floatArrayOf(720f,0.5f),  motionImageParam = floatArrayOf(0f,0.5f), editParam = floatArrayOf(-5f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_trochoid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 20) ))
                }
                // エピサイクロイドを選択したときの描画データの一覧
                MenuItem.MENU_CURVE_EPICYCLOID -> {
                    // カージオイド曲線(k=1.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000021_CARDIOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000021_curve_cardioid),
                        stillImageParam = floatArrayOf(360f,1f), motionImageParam = floatArrayOf(0f,1f), editParam = floatArrayOf(0.2f,2.0f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_epicycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 9) ))
                    // エピサイクロイド曲線(k=4.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000022_EPICYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000022_curve_epicycloid),
                        stillImageParam = floatArrayOf(360f,4f), motionImageParam = floatArrayOf(0f,4f), editParam = floatArrayOf(4f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_epicycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // エピサイクロイド曲線(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_000023_EPICYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000023_curve_epicycloid),
                        stillImageParam = floatArrayOf(3600f,2.1f), motionImageParam = floatArrayOf(0f,2.1f), editParam = floatArrayOf(2f,3f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_epicycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // エピサイクロイド曲線(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_000024_EPICYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000024_curve_epicycloid),
                        stillImageParam = floatArrayOf(1800f,3.8f), motionImageParam = floatArrayOf(0f,3.8f), editParam = floatArrayOf(3f,4f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_epicycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // エピサイクロイド曲線(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_000025_EPICYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000025_curve_epicycloid),
                        stillImageParam = floatArrayOf(720f,5.5f), motionImageParam = floatArrayOf(0f,5.5f), editParam = floatArrayOf(5f,6f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_epicycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // エピサイクロイド曲線(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_000026_EPICYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000026_curve_epicycloid),
                        stillImageParam = floatArrayOf(1800f,7.2f), motionImageParam = floatArrayOf(0f,7.2f), editParam = floatArrayOf(7f,8f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_epicycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                }
                // ハイポサイクロイドを選択したときの描画データの一覧
                MenuItem.MENU_CURVE_HYPOCYCLOID -> {
                    // 三芒形/三尖形(deltoid)(k=3.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000031_DELTOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000031_curve_deltoid),
                        stillImageParam = floatArrayOf(360f,3f), motionImageParam = floatArrayOf(0f,3f), editParam = floatArrayOf(0.2f,3f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypocycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 14) ))
                    // アステロイド曲線(asteroid)(k=4.0)
                    drawDataLst.add(DrawData(DrawDataID.ID_000032_ASTROID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000032_curve_astroid),
                        stillImageParam = floatArrayOf(360f,4f), motionImageParam = floatArrayOf(0f,4f), editParam = floatArrayOf(4f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypocycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=2.1)
                    drawDataLst.add(DrawData(DrawDataID.ID_000033_HYPO_CYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000033_curve_hypocycloid),
                        stillImageParam = floatArrayOf(3600f,2.1f), motionImageParam = floatArrayOf(0f,2.1f), editParam = floatArrayOf(2f,3f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypocycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=3.8)
                    drawDataLst.add(DrawData(DrawDataID.ID_000034_HYPO_CYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000034_curve_hypocycloid),
                        stillImageParam = floatArrayOf(1800f,3.8f), motionImageParam = floatArrayOf(0f,3.8f), editParam = floatArrayOf(3f,4f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypocycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=5.5)
                    drawDataLst.add(DrawData(DrawDataID.ID_000035_HYPO_CYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000035_curve_hypocycloid),
                        stillImageParam = floatArrayOf(720f,5.5f), motionImageParam = floatArrayOf(0f,5.5f), editParam = floatArrayOf(5f,6f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypocycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                    // ハイポサイクロイド曲線(hypocycloid)(k=7.2)
                    drawDataLst.add(DrawData(DrawDataID.ID_000036_HYPO_CYCLOID,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000036_curve_hypocycloid),
                        stillImageParam = floatArrayOf(1800f,7.2f), motionImageParam = floatArrayOf(0f,7.2f), editParam = floatArrayOf(7f,8f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypocycloid), "autoParam" to "t", "handParam1" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                }
                // 対数螺旋を選択したときの描画データの一覧
                MenuItem.MENU_CURVE_SPIRAL_LOGARITHMIC -> {
                    // 対数螺旋間に三角形を描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000014_LOGARITHMIC_SPIRAL_TRIANGLE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000014_curve_logarithmic_spiral_triangle01) ))
                    // 対数螺旋上に円を描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000012_LOGARITHMIC_SPIRAL_CIRCLE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000012_curve_logarithmic_spiral_circle02) ))
                    // 対数螺旋上に円を描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000011_LOGARITHMIC_SPIRAL_CIRCLE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000011_curve_logarithmic_spiral_circle01) ))
                    // 対数螺旋
                    drawDataLst.add(DrawData(DrawDataID.ID_000010_LOGARITHMIC_SPIRAL,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000010_curve_logarithmic_spiral),
                        stillImageParam = floatArrayOf(0f,2f,0.14f), motionImageParam = floatArrayOf(0f,2f,0.14f), editParam = floatArrayOf(0f,500f,1f,-1.0f,1.0f,2f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_logarithmic_spiral), "autoParam" to "t", "handParam1" to "a", "handParam2" to "b") ))
                }
                // インボリュート曲線を選択したときの描画データの一覧
                MenuItem.MENU_CURVE_SPIRAL_INVOLUTE -> {
                    // インボリュート曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000015_INVOLUTE_CURVE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000015_curve_involute_curve02) ))
                    // インボリュート曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000013_INVOLUTE_CURVE_01,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000013_curve_involute_curve01),
                        stillImageParam = floatArrayOf(0f,0.3f), motionImageParam = floatArrayOf(0f,0.3f), editParam = floatArrayOf(0.1f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_involute), "autoParam" to "t", "handParam1" to "a") ))
                }
                // リサージュ曲線を選択したときの描画データの一覧
                MenuItem.MENU_CURVE_LISSAJOUS -> {
                    // リサージュ曲線(p:q=1:2)
                    drawDataLst.add(DrawData(DrawDataID.ID_000050_LISSAJOUS_CURVE_1_2,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000050_curve_lissajous_1_2),
                        stillImageParam = floatArrayOf(1f,2f), motionImageParam = floatArrayOf(1f,2f), editParam = floatArrayOf(1f,2f,0f,1f,2f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_lissajous), "autoParam" to "t", "handParam1" to "p", "handParam2" to "q"),
                        fragmentParamMap = mutableMapOf("maxA" to 10, "maxB" to 10) ))
                    // リサージュ曲線(p:q=3:2)
                    drawDataLst.add(DrawData(DrawDataID.ID_000051_LISSAJOUS_CURVE_3_2,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000051_curve_lissajous_3_2),
                        stillImageParam = floatArrayOf(3f,2f), motionImageParam = floatArrayOf(3f,2f), editParam = floatArrayOf(1f,5f,0f,1f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_lissajous), "autoParam" to "t", "handParam1" to "p", "handParam2" to "q"),
                        fragmentParamMap = mutableMapOf("maxA" to 4, "maxB" to 4) ))
                    // リサージュ曲線(p:q=3:4)
                    drawDataLst.add(DrawData(DrawDataID.ID_000052_LISSAJOUS_CURVE_3_4,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000052_curve_lissajous_3_4),
                        stillImageParam = floatArrayOf(3f,4f), motionImageParam = floatArrayOf(3f,4f), editParam = floatArrayOf(1f,5f,0f,1f,5f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_lissajous), "autoParam" to "t", "handParam1" to "p", "handParam2" to "q"),
                        fragmentParamMap = mutableMapOf("maxA" to 4, "maxB" to 4) ))
                }
                // レムニスケート曲線を選択したときの描画データの一覧
                MenuItem.MENU_CURVE_LEMNISCATE -> {
                    // カッシーニの卵型線
                    drawDataLst.add(DrawData(DrawDataID.ID_000008_CASSINIAN_OVAL,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000008_curve_cassinian_oval),
                        stillImageParam = floatArrayOf(360f*10f), motionImageParam = floatArrayOf(360f*0f) ))
                    // レムニスケート曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000007_LEMNISCATE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000007_curve_lemniscate),
                        stillImageParam = floatArrayOf(360f*20f), motionImageParam = floatArrayOf(360f*20f) ))
                    // カッシーニの卵型線
                    drawDataLst.add(DrawData(DrawDataID.ID_000006_CASSINIAN_OVAL,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000006_curve_cassinian_oval),
                        stillImageParam = floatArrayOf(200f,200f), motionImageParam = floatArrayOf(300f,300f), editParam = floatArrayOf(250f,350f,0f,250f,350f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_cassinian_oval), "autoParam" to "t", "handParam1" to "a", "handParam2" to "b"),
                        fragmentParamMap = mutableMapOf("maxA" to 100,"maxB" to 100) ))
                    // レムニスケート曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000005_LEMNISCATE,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000005_curve_lemniscate),
                        stillImageParam = floatArrayOf(500f), motionImageParam = floatArrayOf(500f), editParam = floatArrayOf(100f,500f,0f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_lemniscate), "autoParam" to "t", "handParam1" to "a"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
                }
                // パスカルの蝸牛形を選択したときの描画データの一覧
                MenuItem.MENU_CURVE_LIMACON -> {
                    // パスカルの蝸牛形03
                    drawDataLst.add(DrawData(DrawDataID.ID_000018_LIMACON_03,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000018_curve_limacon03),
                        stillImageParam = floatArrayOf(1.5f,0.5f), motionImageParam = floatArrayOf(1.5f,0.5f), editParam = floatArrayOf(0f,2f,0f,0f,2f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_limacon03), "autoParam" to "t", "handParam1" to "b", "handParam2" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 20, "maxB" to 20) ))
                    // パスカルの蝸牛形02
                    drawDataLst.add(DrawData(DrawDataID.ID_000017_LIMACON_02,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000017_curve_limacon02),
                        stillImageParam = floatArrayOf(1.5f), motionImageParam = floatArrayOf(1.5f), editParam = floatArrayOf(0f,2f,0f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_limacon02), "autoParam" to "t", "handParam1" to "b"),
                        fragmentParamMap = mutableMapOf("maxA" to 20) ))
                    // パスカルの蝸牛形01
                    drawDataLst.add(DrawData(DrawDataID.ID_000016_LIMACON_01,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000016_curve_limacon01),
                        stillImageParam = floatArrayOf(1.5f), motionImageParam = floatArrayOf(1.5f), editParam = floatArrayOf(0f,2f,0f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_limacon01), "autoParam" to "t", "handParam1" to "b"),
                        fragmentParamMap = mutableMapOf("maxA" to 20) ))
                }
                // フラクタル(再帰)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_RECURSION -> {
                    // 高木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000101_TAKAGI_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000101_fractal_recursion_takagi_curve),
                        stillImageParam = floatArrayOf(6f), motionImageParam = floatArrayOf(0f) ))
                    // コッホ雪片
                    drawDataLst.add(DrawData(DrawDataID.ID_000102_KOCH_SNOWFLAKE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000102_fractal_recursion_koch_snowflake),
                        stillImageParam = floatArrayOf(4f), motionImageParam = floatArrayOf(0f) ))
                    // ドラゴン曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000104_DRAGON_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000104_fractal_recursion_dragon_curve),
                        stillImageParam = floatArrayOf(6f), motionImageParam = floatArrayOf(0f) ))
                    // ヒルベルト曲線(タートル)
                    drawDataLst.add(DrawData(DrawDataID.ID_000106_HILBERT_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000106_fractal_recursion_hilbert_curve),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    //// ヒルベルト曲線
                    // drawDataLst.add(DrawData(DrawDataID.ID_000107_HILBERT_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000107_fractal_recursion_hilbert_curve),
                    //     stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ムーア曲線(タートル)
                    drawDataLst.add(DrawData(DrawDataID.ID_000108_MOORECURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000108_fractal_recursion_moore_curve),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ムーア曲線
                    //drawDataLst.add(DrawData(DrawDataID.ID_000109_MOORECURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000109_fractal_recursion_moore_curve),
                    //    stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                }
                // フラクタル(樹木曲線)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_TREE_CURVE -> {
                    // 樹木曲線03
                    drawDataLst.add(DrawData(DrawDataID.ID_000113_TREE_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000113_fractal_recursion_tree_curve_03) ))
                    // 樹木曲線02
                    drawDataLst.add(DrawData(DrawDataID.ID_000112_TREE_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000112_fractal_recursion_tree_curve_02) ))
                    // 樹木曲線01
                    drawDataLst.add(DrawData(DrawDataID.ID_000111_TREE_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000111_fractal_recursion_tree_curve_01),
                        stillImageParam = floatArrayOf(6f), motionImageParam = floatArrayOf(0f) ))
                }
                // フラクタル(シェルピンスキー系)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_SIERPINSKI_FAMILY -> {
                    // シェルピンスキーの星
                    drawDataLst.add(DrawData(DrawDataID.ID_000126_SIERPINSKI_STAR,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000126_fractal_sierpinski_star),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // シェルピンスキーの八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000125_SIERPINSKI_OCTAGON,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000125_fractal_sierpinski_octagon),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // シェルピンスキーの六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000124_SIERPINSKI_HEXAGON,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000124_fractal_sierpinski_hexagon),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // シェルピンスキーの五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000123_SIERPINSKI_PENTAGON,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000123_fractal_sierpinski_pentagon),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // シェルピンスキーのカーペット
                    drawDataLst.add(DrawData(DrawDataID.ID_000122_SIERPINSKI_CARPET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000122_fractal_sierpinski_carpet),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // シェルピンスキーの三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000121_SIERPINSKI_TRIANGLE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000121_fractal_sierpinski_triangle),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                }
                // フラクタル(ごスパー曲線)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_GOSPER_CURVE -> {
                    // ゴスパー曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000118_GOSPER_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000118_fractal_gosper_curve),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ゴスパー島02
                    drawDataLst.add(DrawData(DrawDataID.ID_000117_GOSPER_ISLAND_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000117_fractal_gosper_island_02),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ゴスパー島01
                    drawDataLst.add(DrawData(DrawDataID.ID_000116_GOSPER_ISLAND_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000116_fractal_gosper_island_01),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                }
                // フラクタル(マンデルブロ―集合)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_MANDELBROT -> {
                    // マンデルブロ―集合(白黒)
                    drawDataLst.add(DrawData(DrawDataID.ID_000200_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000200_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.1f), motionImageParam = floatArrayOf(0.001f) ))
                    // マンデルブロ―集合(-1.5-1.0i～+1.5+1.0i)
                    drawDataLst.add(DrawData(DrawDataID.ID_000201_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000201_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.1f), motionImageParam = floatArrayOf(0.001f) ))
                    // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
                    drawDataLst.add(DrawData(DrawDataID.ID_000202_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000202_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.01f,-1.3f,-1.1f,0f,0.2f), motionImageParam = floatArrayOf(0.0001f,-1.3f,-1.1f,0f,0.2f) ))
                    // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
                    drawDataLst.add(DrawData(DrawDataID.ID_000203_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000203_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.001f,-1.28f,-1.26f,0.18f,0.2f), motionImageParam = floatArrayOf(0.00001f,-1.28f,-1.26f,0.18f,0.2f) ))
                }
                // フラクタル(ジュリア集合)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_JULIA -> {
                    // ジュリア集合
                    drawDataLst.add(DrawData(DrawDataID.ID_000251_JULIA_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000251_fractal_complex_julia_set),
                        stillImageParam = floatArrayOf(0.1f), motionImageParam = floatArrayOf(0.001f) ))
                }
                // 波を選択したときの描画データの一覧
                MenuItem.MENU_WAVE -> {
                    // サイン波
                    drawDataLst.add(DrawData(DrawDataID.ID_000040_SINE_WAVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000040_wave_sine) ))
                    // 円の周りを回転するサイン波
                    //drawDataLst.add(DrawData(DrawDataID.ID_000041_SINE_WAVE_CIRCLE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000041_wave_sine_circle) ))
                    // 円の周りを回転するサイン波
                    drawDataLst.add(DrawData(DrawDataID.ID_000042_SINE_WAVE_CIRCLE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000042_wave_sine_circle) ))
                }
                // "複数円"を選択したときの描画データの一覧
                MenuItem.MENU_CIRCLE_CIRCLES -> {
                    // 実験：円の中に円を描き,すべての円を回転させる
                    //drawDataLst.add(DrawData(DrawDataID.ID_000805_CIRCLE_TEST_ROTATE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000805_circle_rotate) ))
                    // 円の中に円を描き,すべての円を回転させる
                    drawDataLst.add(DrawData(DrawDataID.ID_000804_CIRCLE_ROTATE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000804_circle_rotate_02) ))
                    // 円の中に円を描き,すべての円を回転させる
                    drawDataLst.add(DrawData(DrawDataID.ID_000803_CIRCLE_ROTATE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000803_circle_rotate_01) ))
                    // 円をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000802_CIRCLE_SLIDE,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000802_circle_slide),
                        stillImageParam = floatArrayOf(3600f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/8oZNI4en") ))
                    // だんだん大きくなる円その２
                    drawDataLst.add(DrawData(DrawDataID.ID_000801_CIRCLE_BIGGER_02,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000801_circle_bigger_02),
                        stillImageParam = floatArrayOf(3600f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/NsVT04Kn") ))
                    // だんだん大きくなる円その１
                    drawDataLst.add(DrawData(DrawDataID.ID_000800_CIRCLE_BIGGER_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000800_circle_bigger_01),
                        stillImageParam = floatArrayOf(1080f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/K4_NTCiK") ))
                }
                // "変形する円"を選択したときの描画データの一覧
                MenuItem.MENU_CIRCLE_MORPH -> {
                    // "円⇔正方形の変形"のタイリング
                    drawDataLst.add(DrawData(DrawDataID.ID_000811_CIRCLE_MORPH_CIRCLE2SQUARE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000811_circle_morph_circle2square),
                        stillImageParam = floatArrayOf(0.2f), motionImageParam = floatArrayOf(0f) ))
                    // 円⇔正方形の変形
                    drawDataLst.add(DrawData(DrawDataID.ID_000810_CIRCLE_MORPH_CIRCLE2SQUARE,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000810_circle_morph_circle2square) ))
                }
                // 敷き詰めた円を描画するメニュー
                MenuItem.MENU_CIRCLE_TILE -> {
                    // 青・赤・黄３つの円を回転する
                    drawDataLst.add(DrawData(DrawDataID.ID_000822_CIRCLE_TILE_ROTATE_CIRCLE_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000822_circle_tile_rotate_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/a465c018e4c0ff6a7c8228b729821d56/tumblr_moni0oFXm71r2geqjo1_500.gif") ))
                    // クリスマスツリー(円を三角形上に並べる)
                    drawDataLst.add(DrawData(DrawDataID.ID_000821_CIRCLE_XMASTREE,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000821_circle_xmastree),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/8ed06c22c8e4c32a60cf6bcb2b74a2e6/tumblr_my0y8b1jgP1r2geqjo1_500.gif") ))
                    // "正方形の中で大きくなる円"のタイリング
                    drawDataLst.add(DrawData(DrawDataID.ID_000820_CIRCLE_TILE_CIRCLE2SQUARE,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000820_circle_tile_circle2square),
                        stillImageParam = floatArrayOf(0.2f), motionImageParam = floatArrayOf(0f),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://beesandbombs.tumblr.com/image/164719111714") ))
                }
                // "多角形のMix"を選択したときの描画データの一覧
                MenuItem.MENU_POLYGON_MIX -> {
                    // 回転しながら三角形を合体
                    drawDataLst.add(DrawData(DrawDataID.ID_000603_TRIANGLE_UNITE,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000603_polygon_triangle_unite),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/094d3547b2ba27003dff3eaae387a225/tumblr_n13h7v4Ov01r2geqjo1_500.gif") ))
                    // 多角形のラップ
                    drawDataLst.add(DrawData(DrawDataID.ID_000601_POLYGON_LAP,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000601_polygon_lap),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://twitter.com/beesandbombs/status/872796708803145728") ))
                    // 三角形でEXILE
                    drawDataLst.add(DrawData(DrawDataID.ID_000600_TRIANGLE_EXILE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000600_polygon_triangle_exile) ))
                }
                // 敷き詰めた多角形を描画するメニュー
                MenuItem.MENU_POLYGON_TILE -> {
                    // 六角形に沿って三角形が移動
                    drawDataLst.add(DrawData(DrawDataID.ID_000607_TRIANGLE_2_HEXAGON,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000607_polygon_triangle_2_hexagon) ))
                    // 正方形⇔ひし形02
                    drawDataLst.add(DrawData(DrawDataID.ID_000606_SQUARE_2_DIAMOND_02,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000606_polygon_square_2_diamond_02) ))
                    // 三角形⇔ひし形
                    drawDataLst.add(DrawData(DrawDataID.ID_000605_TRIANGLE_2_DIAMOND,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000605_polygon_triangle_2_diamond),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/6fbe92faf0c058002dfee6c5485a0187/tumblr_mo0n7hepeL1ql82o1o1_r3_500.gif") ))
                    // 正方形⇔ひし形01
                    drawDataLst.add(DrawData(DrawDataID.ID_000604_SQUARE_2_DIAMOND_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000604_polygon_square_2_diamond_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/47c47e8bbcb5ee14878548f806a7c086/tumblr_mnor4buGS01r2geqjo1_500.gif") ))
                    // 回転する矢印
                    drawDataLst.add(DrawData(DrawDataID.ID_000602_ROTATE_ARROWS,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000602_polygon_rotate_arrows),
                        creditMap = mutableMapOf<String,String>("name" to "Just van Rossum", "url" to "https://twitter.com/justvanrossum/status/1091237538583511041") ))
                }
                // "三角形"を選択したときの描画データの一覧
                MenuItem.MENU_POLYGON_TRIANGLE_CENTER -> {
                    // 三角形の傍心
                    drawDataLst.add(DrawData(DrawDataID.ID_000374_TRIANGLE_EXCENTER,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000374_triangle_excenter),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_triangle_excenter)) ))
                    // 三角形の垂心
                    drawDataLst.add(DrawData(DrawDataID.ID_000373_TRIANGLE_ORTHOCENTER,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000373_triangle_orthocenter),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_triangle_orthocenter)) ))
                    // 三角形の内心
                    drawDataLst.add(DrawData(DrawDataID.ID_000372_TRIANGLE_INCENTER,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000372_triangle_incenter),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_triangle_incenter)) ))
                    // 三角形の外心
                    drawDataLst.add(DrawData(DrawDataID.ID_000371_TRIANGLE_CIRCUMCENTER,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000371_triangle_circumcenter),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_triangle_circumcenter)) ))
                    // 三角形の重心
                    drawDataLst.add(DrawData(DrawDataID.ID_000370_TRIANGLE_CENTER_OF_GRAVITY,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000370_triangle_center_of_gravity),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_triangle_center_of_gravity)) ))
                }
                // "多角形in多角形"を選択したときの描画データの一覧
                MenuItem.MENU_POLYGON_PINP -> {
                    // 三角形in四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000300_3_IN_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000300_polygon_3_in_4),
                        stillImageParam = floatArrayOf(4f,3f), motionImageParam = floatArrayOf(4f,3f) ))
                    // 三角形in五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000301_3_IN_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000301_polygon_3_in_5),
                        stillImageParam = floatArrayOf(5f,3f), motionImageParam = floatArrayOf(5f,3f) ))
                    // 三角形in六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000302_3_IN_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000302_polygon_3_in_6),
                        stillImageParam = floatArrayOf(6f,3f), motionImageParam = floatArrayOf(6f,3f) ))
                    // 三角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000303_3_IN_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000303_polygon_3_in_7),
                        stillImageParam = floatArrayOf(7f,3f), motionImageParam = floatArrayOf(7f,3f) ))
                    // 三角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000304_3_IN_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000304_polygon_3_in_8),
                        stillImageParam = floatArrayOf(8f,3f), motionImageParam = floatArrayOf(8f,3f) ))
                    // 三角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000305_3_IN_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000305_polygon_3_in_9),
                        stillImageParam = floatArrayOf(9f,3f), motionImageParam = floatArrayOf(9f,3f) ))
                    // 三角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000306_3_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000306_polygon_3_in_10),
                        stillImageParam = floatArrayOf(10f,3f), motionImageParam = floatArrayOf(10f,3f) ))
                    // 四角形in五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000307_4_IN_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000307_polygon_4_in_5),
                        stillImageParam = floatArrayOf(5f,4f), motionImageParam = floatArrayOf(5f,4f) ))
                    // 四角形in六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000308_4_IN_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000308_polygon_4_in_6),
                        stillImageParam = floatArrayOf(6f,4f), motionImageParam = floatArrayOf(6f,4f) ))
                    // 四角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000309_4_IN_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000309_polygon_4_in_7),
                        stillImageParam = floatArrayOf(7f,4f), motionImageParam = floatArrayOf(7f,4f) ))
                    // 四角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000310_4_IN_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000310_polygon_4_in_8),
                        stillImageParam = floatArrayOf(8f,4f), motionImageParam = floatArrayOf(8f,4f) ))
                    // 四角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000311_4_IN_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000311_polygon_4_in_9),
                        stillImageParam = floatArrayOf(9f,4f), motionImageParam = floatArrayOf(9f,4f) ))
                    // 四角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000312_4_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000312_polygon_4_in_10),
                        stillImageParam = floatArrayOf(10f,4f), motionImageParam = floatArrayOf(10f,4f) ))
                    // 五角形in六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000313_5_IN_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000313_polygon_5_in_6),
                        stillImageParam = floatArrayOf(6f,5f), motionImageParam = floatArrayOf(6f,5f) ))
                    // 五角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000314_5_IN_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000314_polygon_5_in_7),
                        stillImageParam = floatArrayOf(7f,5f), motionImageParam = floatArrayOf(7f,5f) ))
                    // 五角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000315_5_IN_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000315_polygon_5_in_8),
                        stillImageParam = floatArrayOf(8f,5f), motionImageParam = floatArrayOf(8f,5f) ))
                    // 五角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000316_5_IN_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000316_polygon_5_in_9),
                        stillImageParam = floatArrayOf(9f,5f), motionImageParam = floatArrayOf(9f,5f) ))
                    // 五角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000317_5_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000317_polygon_5_in_10),
                        stillImageParam = floatArrayOf(10f,5f), motionImageParam = floatArrayOf(10f,5f) ))
                    // 六角形in七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000318_6_IN_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000318_polygon_6_in_7),
                        stillImageParam = floatArrayOf(7f,6f), motionImageParam = floatArrayOf(7f,6f) ))
                    // 六角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000319_6_IN_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000319_polygon_6_in_8),
                        stillImageParam = floatArrayOf(8f,6f), motionImageParam = floatArrayOf(8f,6f) ))
                    // 六角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000320_6_IN_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000320_polygon_6_in_9),
                        stillImageParam = floatArrayOf(9f,6f), motionImageParam = floatArrayOf(9f,6f) ))
                    // 六角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000321_6_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000321_polygon_6_in_10),
                        stillImageParam = floatArrayOf(10f,6f), motionImageParam = floatArrayOf(10f,6f) ))
                    // 七角形in八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000322_7_IN_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000322_polygon_7_in_8),
                        stillImageParam = floatArrayOf(8f,7f), motionImageParam = floatArrayOf(8f,7f) ))
                    // 七角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000323_7_IN_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000323_polygon_7_in_9),
                        stillImageParam = floatArrayOf(9f,7f), motionImageParam = floatArrayOf(9f,7f) ))
                    // 七角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000324_7_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000324_polygon_7_in_10),
                        stillImageParam = floatArrayOf(10f,7f), motionImageParam = floatArrayOf(10f,7f) ))
                    // 八角形in九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000325_8_IN_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000325_polygon_8_in_9),
                        stillImageParam = floatArrayOf(9f,8f), motionImageParam = floatArrayOf(9f,8f) ))
                    // 八角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000326_8_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000326_polygon_8_in_10),
                        stillImageParam = floatArrayOf(10f,8f), motionImageParam = floatArrayOf(10f,8f) ))
                    // 九角形in十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000327_9_IN_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000327_polygon_9_in_10),
                        stillImageParam = floatArrayOf(10f,9f), motionImageParam = floatArrayOf(10f,9f) ))
                }
                // "多角形out多角形"を選択したときの描画データの一覧
                MenuItem.MENU_POLYGON_POUTP -> {
                    // 三角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000701_3_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000701_polygon_3_out_3),
                            stillImageParam = floatArrayOf(3f,3f), motionImageParam = floatArrayOf(3f,3f) ))
                    // 三角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000702_3_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000702_polygon_3_out_4),
                        stillImageParam = floatArrayOf(3f,4f), motionImageParam = floatArrayOf(3f,4f) ))
                    // 三角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000703_3_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000703_polygon_3_out_5),
                        stillImageParam = floatArrayOf(3f,5f), motionImageParam = floatArrayOf(3f,5f) ))
                    // 三角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000704_3_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000704_polygon_3_out_6),
                        stillImageParam = floatArrayOf(3f,6f), motionImageParam = floatArrayOf(3f,6f) ))
                    // 三角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000705_3_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000705_polygon_3_out_7),
                        stillImageParam = floatArrayOf(3f,7f), motionImageParam = floatArrayOf(3f,7f) ))
                    // 三角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000706_3_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000706_polygon_3_out_8),
                        stillImageParam = floatArrayOf(3f,8f), motionImageParam = floatArrayOf(3f,8f) ))
                    // 三角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000707_3_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000707_polygon_3_out_9),
                        stillImageParam = floatArrayOf(3f,9f), motionImageParam = floatArrayOf(3f,9f) ))
                    // 三角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000708_3_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000708_polygon_3_out_10),
                        stillImageParam = floatArrayOf(3f,10f), motionImageParam = floatArrayOf(3f,10f) ))
                    // 四角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000711_4_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000711_polygon_4_out_3),
                        stillImageParam = floatArrayOf(4f,3f), motionImageParam = floatArrayOf(4f,3f) ))
                    // 四角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000712_4_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000712_polygon_4_out_4),
                        stillImageParam = floatArrayOf(4f,4f), motionImageParam = floatArrayOf(4f,4f) ))
                    // 四角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000713_4_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000713_polygon_4_out_5),
                        stillImageParam = floatArrayOf(4f,5f), motionImageParam = floatArrayOf(4f,5f) ))
                    // 四角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000714_4_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000714_polygon_4_out_6),
                        stillImageParam = floatArrayOf(4f,6f), motionImageParam = floatArrayOf(4f,6f) ))
                    // 四角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000715_4_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000715_polygon_4_out_7),
                        stillImageParam = floatArrayOf(4f,7f), motionImageParam = floatArrayOf(4f,7f) ))
                    // 四角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000716_4_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000716_polygon_4_out_8),
                        stillImageParam = floatArrayOf(4f,8f), motionImageParam = floatArrayOf(4f,8f) ))
                    // 四角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000717_4_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000717_polygon_4_out_9),
                        stillImageParam = floatArrayOf(4f,9f), motionImageParam = floatArrayOf(4f,9f) ))
                    // 四角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000718_4_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000718_polygon_4_out_10),
                        stillImageParam = floatArrayOf(4f,10f), motionImageParam = floatArrayOf(4f,10f) ))
                    // 五角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000721_5_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000721_polygon_5_out_3),
                        stillImageParam = floatArrayOf(5f,3f), motionImageParam = floatArrayOf(5f,3f) ))
                    // 五角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000722_5_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000722_polygon_5_out_4),
                        stillImageParam = floatArrayOf(5f,4f), motionImageParam = floatArrayOf(5f,4f) ))
                    // 五角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000723_5_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000723_polygon_5_out_5),
                        stillImageParam = floatArrayOf(5f,5f), motionImageParam = floatArrayOf(5f,5f) ))
                    // 五角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000724_5_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000724_polygon_5_out_6),
                        stillImageParam = floatArrayOf(5f,6f), motionImageParam = floatArrayOf(5f,6f) ))
                    // 五角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000725_5_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000725_polygon_5_out_7),
                        stillImageParam = floatArrayOf(5f,7f), motionImageParam = floatArrayOf(5f,7f) ))
                    // 五角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000726_5_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000726_polygon_5_out_8),
                        stillImageParam = floatArrayOf(5f,8f), motionImageParam = floatArrayOf(5f,8f) ))
                    // 五角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000727_5_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000727_polygon_5_out_9),
                        stillImageParam = floatArrayOf(5f,9f), motionImageParam = floatArrayOf(5f,9f) ))
                    // 五角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000728_5_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000728_polygon_5_out_10),
                        stillImageParam = floatArrayOf(5f,10f), motionImageParam = floatArrayOf(5f,10f) ))
                    // 六角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000731_6_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000731_polygon_6_out_3),
                        stillImageParam = floatArrayOf(6f,3f), motionImageParam = floatArrayOf(6f,3f) ))
                    // 六角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000732_6_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000732_polygon_6_out_4),
                        stillImageParam = floatArrayOf(6f,4f), motionImageParam = floatArrayOf(6f,4f) ))
                    // 六角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000733_6_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000733_polygon_6_out_5),
                        stillImageParam = floatArrayOf(6f,5f), motionImageParam = floatArrayOf(6f,5f) ))
                    // 六角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000734_6_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000734_polygon_6_out_6),
                        stillImageParam = floatArrayOf(6f,6f), motionImageParam = floatArrayOf(6f,6f) ))
                    // 六角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000735_6_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000735_polygon_6_out_7),
                        stillImageParam = floatArrayOf(6f,7f), motionImageParam = floatArrayOf(6f,7f) ))
                    // 六角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000736_6_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000736_polygon_6_out_8),
                        stillImageParam = floatArrayOf(6f,8f), motionImageParam = floatArrayOf(6f,8f) ))
                    // 六角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000737_6_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000737_polygon_6_out_9),
                        stillImageParam = floatArrayOf(6f,9f), motionImageParam = floatArrayOf(6f,9f) ))
                    // 六角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000738_6_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000738_polygon_6_out_10),
                        stillImageParam = floatArrayOf(6f,10f), motionImageParam = floatArrayOf(6f,10f) ))
                    // 七角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000741_7_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000741_polygon_7_out_3),
                        stillImageParam = floatArrayOf(7f,3f), motionImageParam = floatArrayOf(7f,3f) ))
                    // 七角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000742_7_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000742_polygon_7_out_4),
                        stillImageParam = floatArrayOf(7f,4f), motionImageParam = floatArrayOf(7f,4f) ))
                    // 七角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000743_7_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000743_polygon_7_out_5),
                        stillImageParam = floatArrayOf(7f,5f), motionImageParam = floatArrayOf(7f,5f) ))
                    // 七角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000744_7_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000744_polygon_7_out_6),
                        stillImageParam = floatArrayOf(7f,6f), motionImageParam = floatArrayOf(7f,6f) ))
                    // 七角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000745_7_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000745_polygon_7_out_7),
                        stillImageParam = floatArrayOf(7f,7f), motionImageParam = floatArrayOf(7f,7f) ))
                    // 七角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000746_7_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000746_polygon_7_out_8),
                        stillImageParam = floatArrayOf(7f,8f), motionImageParam = floatArrayOf(7f,8f) ))
                    // 七角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000747_7_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000747_polygon_7_out_9),
                        stillImageParam = floatArrayOf(7f,9f), motionImageParam = floatArrayOf(7f,9f) ))
                    // 七角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000748_7_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000748_polygon_7_out_10),
                        stillImageParam = floatArrayOf(7f,10f), motionImageParam = floatArrayOf(7f,10f) ))
                    // 八角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000751_8_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000751_polygon_8_out_3),
                        stillImageParam = floatArrayOf(8f,3f), motionImageParam = floatArrayOf(8f,3f) ))
                    // 八角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000752_8_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000752_polygon_8_out_4),
                        stillImageParam = floatArrayOf(8f,4f), motionImageParam = floatArrayOf(8f,4f) ))
                    // 八角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000753_8_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000753_polygon_8_out_5),
                        stillImageParam = floatArrayOf(8f,5f), motionImageParam = floatArrayOf(8f,5f) ))
                    // 八角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000754_8_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000754_polygon_8_out_6),
                        stillImageParam = floatArrayOf(8f,6f), motionImageParam = floatArrayOf(8f,6f) ))
                    // 八角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000755_8_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000755_polygon_8_out_7),
                        stillImageParam = floatArrayOf(8f,7f), motionImageParam = floatArrayOf(8f,7f) ))
                    // 八角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000756_8_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000756_polygon_8_out_8),
                        stillImageParam = floatArrayOf(8f,8f), motionImageParam = floatArrayOf(8f,8f) ))
                    // 八角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000757_8_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000757_polygon_8_out_9),
                        stillImageParam = floatArrayOf(8f,9f), motionImageParam = floatArrayOf(8f,9f) ))
                    // 八角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000758_8_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000758_polygon_8_out_10),
                        stillImageParam = floatArrayOf(8f,10f), motionImageParam = floatArrayOf(8f,10f) ))
                    // 九角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000761_9_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000761_polygon_9_out_3),
                        stillImageParam = floatArrayOf(9f,3f), motionImageParam = floatArrayOf(9f,3f) ))
                    // 九角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000762_9_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000762_polygon_9_out_4),
                        stillImageParam = floatArrayOf(9f,4f), motionImageParam = floatArrayOf(9f,4f) ))
                    // 九角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000763_9_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000763_polygon_9_out_5),
                        stillImageParam = floatArrayOf(9f,5f), motionImageParam = floatArrayOf(9f,5f) ))
                    // 九角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000764_9_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000764_polygon_9_out_6),
                        stillImageParam = floatArrayOf(9f,6f), motionImageParam = floatArrayOf(9f,6f) ))
                    // 九角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000765_9_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000765_polygon_9_out_7),
                        stillImageParam = floatArrayOf(9f,7f), motionImageParam = floatArrayOf(9f,7f) ))
                    // 九角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000766_9_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000766_polygon_9_out_8),
                        stillImageParam = floatArrayOf(9f,8f), motionImageParam = floatArrayOf(9f,8f) ))
                    // 九角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000767_9_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000767_polygon_9_out_9),
                        stillImageParam = floatArrayOf(9f,9f), motionImageParam = floatArrayOf(9f,9f) ))
                    // 九角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000768_9_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000768_polygon_9_out_10),
                        stillImageParam = floatArrayOf(9f,10f), motionImageParam = floatArrayOf(9f,10f) ))
                    // 十角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000771_10_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000771_polygon_10_out_3),
                        stillImageParam = floatArrayOf(10f,3f), motionImageParam = floatArrayOf(10f,3f) ))
                    // 十角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000772_10_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000772_polygon_10_out_4),
                        stillImageParam = floatArrayOf(10f,4f), motionImageParam = floatArrayOf(10f,4f) ))
                    // 十角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000773_10_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000773_polygon_10_out_5),
                        stillImageParam = floatArrayOf(10f,5f), motionImageParam = floatArrayOf(10f,5f) ))
                    // 十角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000774_10_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000774_polygon_10_out_6),
                        stillImageParam = floatArrayOf(10f,6f), motionImageParam = floatArrayOf(10f,6f) ))
                    // 十角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000775_10_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000775_polygon_10_out_7),
                        stillImageParam = floatArrayOf(10f,7f), motionImageParam = floatArrayOf(10f,7f) ))
                    // 十角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000776_10_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000776_polygon_10_out_8),
                        stillImageParam = floatArrayOf(10f,8f), motionImageParam = floatArrayOf(10f,8f) ))
                    // 十角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000777_10_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000777_polygon_10_out_9),
                        stillImageParam = floatArrayOf(10f,9f), motionImageParam = floatArrayOf(10f,9f) ))
                    // 十角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000778_10_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000778_polygon_10_out_10),
                        stillImageParam = floatArrayOf(10f,10f), motionImageParam = floatArrayOf(10f,10f) ))
                }
                // 多角形をずらして描画するデータの一覧
                MenuItem.MENU_POLYGON_SLIDE -> {
                    /*
                    // 六角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000604_SLIDE_HEXAGON,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000604_polygon_slide_hexagon),
                        stillImageParam = floatArrayOf(12f), motionImageParam = floatArrayOf(0f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 三角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000781_SLIDE_3,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000781_polygon_slide_3),
                        stillImageParam = floatArrayOf(3f), motionImageParam = floatArrayOf(3f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 四角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000782_SLIDE_4,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000782_polygon_slide_4),
                        stillImageParam = floatArrayOf(4f), motionImageParam = floatArrayOf(4f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                        */
                    // 五角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000783_SLIDE_5,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000783_polygon_slide_5),
                        stillImageParam = floatArrayOf(5f), motionImageParam = floatArrayOf(5f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 六角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000784_SLIDE_6,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000784_polygon_slide_6),
                        stillImageParam = floatArrayOf(6f), motionImageParam = floatArrayOf(6f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 七角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000785_SLIDE_7,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000785_polygon_slide_7),
                        stillImageParam = floatArrayOf(7f), motionImageParam = floatArrayOf(7f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 八角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000786_SLIDE_8,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000786_polygon_slide_8),
                        stillImageParam = floatArrayOf(8f), motionImageParam = floatArrayOf(8f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 九角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000787_SLIDE_9,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000787_polygon_slide_9),
                        stillImageParam = floatArrayOf(9f), motionImageParam = floatArrayOf(9f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                    // 十角形をずらして描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000788_SLIDE_10,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000788_polygon_slide_10),
                        stillImageParam = floatArrayOf(10f), motionImageParam = floatArrayOf(10f),
                        creditMap = mutableMapOf<String,String>("name" to "papert", "url" to "http://logo.twentygototen.org/dMgxWRrj") ))
                }
                // 多面体に様々なエフェクトを施したものを描画するメニュー
                MenuItem.MENU_POLYHEDRON_VIEW -> {
                    // トーラス
                    drawDataLst.add(DrawData(DrawDataID.ID_001006_GL_TORUS_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001006_gl_torus_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 6f, "scale" to 0.25f) ))
                    // 球
                    drawDataLst.add(DrawData(DrawDataID.ID_001005_GL_SPHERE_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001005_gl_sphere_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 5f, "scale" to 1f) ))
                    // 正二十面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001004_GL_ICOSAHEDRON_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001004_gl_icosahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 4f, "scale" to 0.5f) ))
                    // 正十二面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001003_GL_DODECAHEDRON_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001003_gl_dodecahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 3f, "scale" to 0.5f) ))
                    // 正八面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001002_GL_OCTAHEDRON_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001002_gl_octahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 2f, "scale" to 0.8f) ))
                    // 立方体
                    drawDataLst.add(DrawData(DrawDataID.ID_001001_GL_CUBE_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001001_gl_cube_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 1f, "scale" to 0.6f) ))
                    // 正四面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001000_GL_TETRAHEDRON_01,DrawFragmentType.FT_D3_01,resources.getString(R.string.draw_001000_gl_tetrahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 0f) ))
                }
                // 立方体に座標変換を施したものを描画するメニュー
                MenuItem.MENU_POLYHEDRON_CUBE_TRANSFORM -> {
                    // 立方体座標変換04
                    drawDataLst.add(DrawData(DrawDataID.ID_001103_GL_CUBE_TRANSFORM_04,DrawFragmentType.FT_D3_02,resources.getString(R.string.draw_001103_gl_cube_transform_04),
                        drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換03
                    drawDataLst.add(DrawData(DrawDataID.ID_001102_GL_CUBE_TRANSFORM_03,DrawFragmentType.FT_D3_02,resources.getString(R.string.draw_001102_gl_cube_transform_03),
                        drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換02
                    drawDataLst.add(DrawData(DrawDataID.ID_001101_GL_CUBE_TRANSFORM_02,DrawFragmentType.FT_D3_02,resources.getString(R.string.draw_001101_gl_cube_transform_02),
                        drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換01
                    drawDataLst.add(DrawData(DrawDataID.ID_001100_GL_CUBE_TRANSFORM_01,DrawFragmentType.FT_D3_02,resources.getString(R.string.draw_001100_gl_cube_transform_01),
                        drawViewType = DrawViewType.DVT_GL ))
                }
                // "Nature of Code"-"Vectors"を選択したときの描画データの一覧
                MenuItem.MENU_NATURE_VECTORS -> {
                    // 等速度運動
                    drawDataLst.add(DrawData(DrawDataID.ID_000401_NATURE_UNFORM_MOTION,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000401_nature_uniform_motion),
                        stillImageParam = floatArrayOf(5f),
                        creditMap = mutableMapOf<String,String>("name" to "The Nature of Code", "url" to "https://natureofcode.com/book/chapter-1-vectors/") ))
                    // 噴水
                    drawDataLst.add(DrawData(DrawDataID.ID_000402_NATURE_FOUNTAIN,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000402_nature_fountain),
                        stillImageParam = floatArrayOf(5f), motionImageParam = floatArrayOf(1f),
                        creditMap = mutableMapOf<String,String>("name" to "The Nature of Code", "url" to "https://natureofcode.com/book/chapter-1-vectors/") ))
                    // ランダムウォーク
                    drawDataLst.add(DrawData(DrawDataID.ID_000403_NATURE_RANDOM_WALK,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000403_nature_random_walk),
                        stillImageParam = floatArrayOf(5f),
                        creditMap = mutableMapOf<String,String>("name" to "The Nature of Code", "url" to "https://natureofcode.com/book/chapter-1-vectors/") ))
                    // タッチした方向に向かって加速
                    drawDataLst.add(DrawData(DrawDataID.ID_000404_NATURE_ACCELERATE_TOWARDS_TOUCH_POINT,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000404_nature_accelerate_towards_touch_point),
                        stillImageParam = floatArrayOf(5f),
                        creditMap = mutableMapOf<String,String>("name" to "The Nature of Code", "url" to "https://natureofcode.com/book/chapter-1-vectors/") ))
                }
                // "Nature of Code"-"Forces"を選択したときの描画データの一覧
                MenuItem.MENU_NATURE_FORCES -> {
                    // 質量の効果
                    drawDataLst.add(DrawData(DrawDataID.ID_000405_NATURE_MASS_EFFECT,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000405_nature_mass_effect),
                        stillImageParam = floatArrayOf(10f), motionImageParam = floatArrayOf(10f),
                        creditMap = mutableMapOf<String,String>("name" to "The Nature of Code", "url" to "https://natureofcode.com/book/chapter-2-forces/") ))
                    // 摩擦の効果
                    drawDataLst.add(DrawData(DrawDataID.ID_000406_NATURE_FRICTION_EFFECT,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000406_nature_friction_effect),
                        stillImageParam = floatArrayOf(10f), motionImageParam = floatArrayOf(10f),
                        creditMap = mutableMapOf<String,String>("name" to "The Nature of Code", "url" to "https://natureofcode.com/book/chapter-2-forces/") ))
                }
                // 色を選択したときの描画データの一覧
                MenuItem.MENU_COLOR -> {
                    // 1536色
                    drawDataLst.add(DrawData(DrawDataID.ID_000500_COLOR_1536,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000500_color_1536)))
                    // 768色(暗色)
                    drawDataLst.add(DrawData(DrawDataID.ID_000501_COLOR_768_DARK,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000501_color_768_dark)))
                }
                // 錯覚を選択したときの描画データの一覧
                MenuItem.MENU_OPTICAL_ILLUSION -> {
                    // "Stereokinetic Effect"
                    drawDataLst.add(DrawData(DrawDataID.ID_000901_OPTICAL_ILLUSION_STEREOKINETIC_EFFECT,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000901_optical_illusion_stereokinetic_effect),
                        creditMap = mutableMapOf<String,String>("name" to "Michael Bach", "url" to "https://michaelbach.de/ot/mot-ske/index.html") ))
                    // "Stepping Feet"
                    drawDataLst.add(DrawData(DrawDataID.ID_000900_OPTICAL_ILLUSION_STEPPING_FEET,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000900_optical_illusion_stepping_feet),
                        creditMap = mutableMapOf<String,String>("name" to "Michael Bach", "url" to "https://michaelbach.de/ot/mot-feetLin/index.html") ))
                }
                else -> {
                    throw RuntimeException("No Draw List for ${menuData.menuItem.title}")
                }
            }
            return drawDataLst
        }
    }
}