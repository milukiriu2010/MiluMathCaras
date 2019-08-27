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
                    // カッシニアン曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000037_CASSINIAN_CURVE,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000037_curve_cassinian),
                        stillImageParam = floatArrayOf(3f), motionImageParam = floatArrayOf(3f), editParam = floatArrayOf(0f,10f,0f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_cassinian), "autoParam" to "t", "handParam1" to "n"),
                        fragmentParamMap = mutableMapOf("maxA" to 10) ))
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
                // SIN/COSのミックスを描画するメニュー
                MenuItem.MENU_CURVE_MIX_SINCOS -> {
                    // Torus Knot
                    drawDataLst.add(DrawData(DrawDataID.ID_000045_TORUS_KNOT_01,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000045_curve_torus_knot_01),
                        stillImageParam = floatArrayOf(2f,3f), motionImageParam = floatArrayOf(2f,3f), editParam = floatArrayOf(1f,8f,0f,1f,8f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_torus_knot_01), "autoParam" to "t", "handParam1" to "p", "handParam2" to "q"),
                        fragmentParamMap = mutableMapOf("maxA" to 7, "maxB" to 7) ))
                    // Rose
                    drawDataLst.add(DrawData(DrawDataID.ID_000044_ROSE_01,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000044_curve_rose_01),
                        stillImageParam = floatArrayOf(3f), motionImageParam = floatArrayOf(3f), editParam = floatArrayOf(0f,8f,0f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_rose_01), "autoParam" to "t", "handParam1" to "n"),
                        fragmentParamMap = mutableMapOf("maxA" to 8) ))
                    // Hypotrochoid
                    drawDataLst.add(DrawData(DrawDataID.ID_000043_HYPOTROCHOID_01,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000043_curve_hypotrochoid_01),
                        stillImageParam = floatArrayOf(5f,1.6f), motionImageParam = floatArrayOf(5f,1.6f), editParam = floatArrayOf(2f,8f,0f,0f,3f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_hypotrochoid_01), "autoParam" to "t", "handParam1" to "q", "handParam2" to "k"),
                        fragmentParamMap = mutableMapOf("maxA" to 30, "maxB" to 30) ))
                    // Lituus
                    drawDataLst.add(DrawData(DrawDataID.ID_000042_LITUUS_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000042_curve_lituus_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_littus_01)) ))
                    // Fermat Spiral
                    drawDataLst.add(DrawData(DrawDataID.ID_000041_FERMAT_SPIRAL_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000041_curve_fermat_spiral_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_fermat_spiral_01)) ))
                    // Cyclic Harmony Curve
                    drawDataLst.add(DrawData(DrawDataID.ID_000040_CURVE_CYCLIC_HARMONIC_01,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000040_curve_cyclic_harmonic_01),
                        stillImageParam = floatArrayOf(3f,1f), motionImageParam = floatArrayOf(3f,1f), editParam = floatArrayOf(0f,5f,0f,0f,2f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_cyclic_harmonic_01), "autoParam" to "t", "handParam1" to "n", "handParam2" to "e"),
                        fragmentParamMap = mutableMapOf("maxA" to 20, "maxB" to 20) ))
                    // Cochleoid
                    drawDataLst.add(DrawData(DrawDataID.ID_000039_COCHLEOID_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000039_curve_cochleoid_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_cochleoid_01)) ))
                    // Caustic of a Circle
                    drawDataLst.add(DrawData(DrawDataID.ID_000038_CAUSTIC_CIRCLE_01,DrawFragmentType.FT_D2_06,resources.getString(R.string.draw_000038_curve_caustic_circle_01),
                        stillImageParam = floatArrayOf(50f,50f), motionImageParam = floatArrayOf(50f,50f), editParam = floatArrayOf(-100f,100f,0f,-100f,100f,1f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_caustic_circle_01), "autoParam" to "t", "handParam1" to "a", "handParam2" to "b"),
                        fragmentParamMap = mutableMapOf("maxA" to 20, "maxB" to 20) ))
                    // Trèfle de Habenicht
                    drawDataLst.add(DrawData(DrawDataID.ID_000030_TREFLE_01,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000030_curve_trefle_habenicht_01),
                        stillImageParam = floatArrayOf(3f), motionImageParam = floatArrayOf(3f), editParam = floatArrayOf(0f,8f,0f),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_trefle_habenicht_01), "autoParam" to "t", "handParam1" to "n"),
                        fragmentParamMap = mutableMapOf("maxA" to 8) ))
                }
                // 特別な形の曲線の描画一覧
                MenuItem.MENU_CURVE_SPECIAL_SHAPE -> {
                    // 陰陽 02
                    drawDataLst.add(DrawData(DrawDataID.ID_000029_YING_YANG_02,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000029_curve_ying_yang_02),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_ying_yang_02)) ))
                    // 陰陽 01
                    drawDataLst.add(DrawData(DrawDataID.ID_000028_YING_YANG_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000028_curve_ying_yang_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_ying_yang_01)) ))
                    // 蝶々 T.Fay 01
                    drawDataLst.add(DrawData(DrawDataID.ID_000027_BUTTERFLY_FAY_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000027_curve_butterfly_fay_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_butterfly_fay_01)) ))
                    // 蝶々 L.Sautereau 02
                    drawDataLst.add(DrawData(DrawDataID.ID_000020_BUTTERFLY_SAUTEREAU_02,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000020_curve_butterfly_sautereau_02),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_butterfly_sautereau_02)) ))
                    // 蝶々 L.Sautereau 01
                    drawDataLst.add(DrawData(DrawDataID.ID_000019_BUTTERFLY_SAUTEREAU_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000019_curve_butterfly_sautereau_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_butterfly_sautereau_01)) ))
                    // ハート
                    drawDataLst.add(DrawData(DrawDataID.ID_000009_HEART_01,DrawFragmentType.FT_D2_04,resources.getString(R.string.draw_000009_curve_heart_01),
                        funcDescMap = mutableMapOf("funcDesc" to resources.getString(R.string.func_curve_heart_01)) ))
                }
                // フラクタル(再帰)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_RECURSION -> {
                    // Lévy C
                    drawDataLst.add(DrawData(DrawDataID.ID_000110_LEVY,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000110_fractal_recursion_levyc_curve),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ムーア曲線(タートル)
                    drawDataLst.add(DrawData(DrawDataID.ID_000108_MOORECURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000108_fractal_recursion_moore_curve),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ムーア曲線
                    //drawDataLst.add(DrawData(DrawDataID.ID_000109_MOORECURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000109_fractal_recursion_moore_curve),
                    //    stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ヒルベルト曲線(タートル)
                    drawDataLst.add(DrawData(DrawDataID.ID_000106_HILBERT_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000106_fractal_recursion_hilbert_curve),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    //// ヒルベルト曲線
                    // drawDataLst.add(DrawData(DrawDataID.ID_000107_HILBERT_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000107_fractal_recursion_hilbert_curve),
                    //     stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ドラゴン曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000104_DRAGON_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000104_fractal_recursion_dragon_curve),
                        stillImageParam = floatArrayOf(6f), motionImageParam = floatArrayOf(0f) ))
                    //    stillImageParam = floatArrayOf(4f), motionImageParam = floatArrayOf(0f) ))
                    // 高木曲線
                    drawDataLst.add(DrawData(DrawDataID.ID_000101_TAKAGI_CURVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000101_fractal_recursion_takagi_curve),
                        stillImageParam = floatArrayOf(6f), motionImageParam = floatArrayOf(0f) ))
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
                // フラクタル(コッホ曲線)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_KOCH_CURVE -> {
                    // コッホ雪片04
                    drawDataLst.add(DrawData(DrawDataID.ID_000133_KOCH_SNOWFLAKE_04,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000133_fractal_recursion_koch_snowflake_04) ))
                    // コッホ雪片03
                    drawDataLst.add(DrawData(DrawDataID.ID_000132_KOCH_SNOWFLAKE_03,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000132_fractal_recursion_koch_snowflake_03) ))
                    // コッホ雪片05
                    drawDataLst.add(DrawData(DrawDataID.ID_000134_KOCH_SNOWFLAKE_05,DrawFragmentType.FT_D2_05,resources.getString(R.string.draw_000134_fractal_recursion_koch_snowflake_05),
                        stillImageParam = floatArrayOf(3f,85f), motionImageParam = floatArrayOf(0f,85f), editParam = floatArrayOf(45f,90f,1f),
                        funcDescMap = mutableMapOf( "autoParam" to "n", "handParam1" to "angle"),
                        fragmentParamMap = mutableMapOf("maxA" to 9) ))
                    // コッホ雪片02
                    drawDataLst.add(DrawData(DrawDataID.ID_000131_KOCH_SNOWFLAKE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000131_fractal_recursion_koch_snowflake_02),
                        stillImageParam = floatArrayOf(4f), motionImageParam = floatArrayOf(0f) ))
                    // コッホ雪片01
                    //drawDataLst.add(DrawData(DrawDataID.ID_000130_KOCH_SNOWFLAKE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000102_fractal_recursion_koch_snowflake_01),
                }
                // フラクタル(シェルピンスキー系)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_SIERPINSKI_FAMILY -> {
                    // シェルピンスキーの三角形02
                    drawDataLst.add(DrawData(DrawDataID.ID_000128_SIERPINSKI_TRIANGLE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000128_fractal_sierpinski_triangle_02) ))
                    // シェルピンスキーの五角形02
                    drawDataLst.add(DrawData(DrawDataID.ID_000127_SIERPINSKI_PENTAGON_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000127_fractal_sierpinski_pentagon_02) ))
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
                    // シェルピンスキーの三角形01
                    drawDataLst.add(DrawData(DrawDataID.ID_000121_SIERPINSKI_TRIANGLE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000121_fractal_sierpinski_triangle_01),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                }
                // フラクタル(ごスパー曲線)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_GOSPER_CURVE -> {
                    // ゴスパー曲線03
                    drawDataLst.add(DrawData(DrawDataID.ID_000120_GOSPER_CURVE_03,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000120_fractal_gosper_curve_03) ))
                    // ゴスパー曲線02
                    drawDataLst.add(DrawData(DrawDataID.ID_000119_GOSPER_CURVE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000119_fractal_gosper_curve_02),
                        stillImageParam = floatArrayOf(2f), motionImageParam = floatArrayOf(0f) ))
                    // ゴスパー曲線01
                    drawDataLst.add(DrawData(DrawDataID.ID_000118_GOSPER_CURVE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000118_fractal_gosper_curve_01),
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
                    drawDataLst.add(DrawData(DrawDataID.ID_000140_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000140_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.1f), motionImageParam = floatArrayOf(0.001f) ))
                    // マンデルブロ―集合(-1.5-1.0i～+1.5+1.0i)
                    drawDataLst.add(DrawData(DrawDataID.ID_000141_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000141_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.1f), motionImageParam = floatArrayOf(0.001f) ))
                    // マンデルブロ―集合(-1.3+0.0i～-1.1+0.2i)
                    drawDataLst.add(DrawData(DrawDataID.ID_000142_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000142_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.01f,-1.3f,-1.1f,0f,0.2f), motionImageParam = floatArrayOf(0.0001f,-1.3f,-1.1f,0f,0.2f) ))
                    // マンデルブロ―集合(-1.28+0.18i～-1.26+0.20i)
                    drawDataLst.add(DrawData(DrawDataID.ID_000143_MANDELBRO_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000143_fractal_complex_mandelbrot_set),
                        stillImageParam = floatArrayOf(0.001f,-1.28f,-1.26f,0.18f,0.2f), motionImageParam = floatArrayOf(0.00001f,-1.28f,-1.26f,0.18f,0.2f) ))
                }
                // フラクタル(ジュリア集合)を選択したときの描画データの一覧
                MenuItem.MENU_FRACTAL_JULIA -> {
                    // ジュリア集合
                    drawDataLst.add(DrawData(DrawDataID.ID_000144_JULIA_SET,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000144_fractal_complex_julia_set),
                        stillImageParam = floatArrayOf(0.1f), motionImageParam = floatArrayOf(0.001f) ))
                }
                // 波を選択したときの描画データの一覧
                MenuItem.MENU_WAVE_SINCOS -> {
                    // 隣同士の点がサイン波を描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000204_SINE_WAVE_POINT_02,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000204_wave_sine_point_02),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/7b67b31dc375ebad6303869f997b3718/tumblr_mjrn2diIUo1r2geqjo1_500.gif") ))
                    // 隣同士の点がサイン波を描く
                    drawDataLst.add(DrawData(DrawDataID.ID_000203_SINE_WAVE_POINT_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000203_wave_sine_point_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/352c06f02c2203e085213921a1579c3e/tumblr_mjrmkzlAPI1r2geqjo1_500.gif") ))
                    // 円の周りを回転するサイン波
                    drawDataLst.add(DrawData(DrawDataID.ID_000202_SINE_WAVE_CIRCLE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000202_wave_sine_circle) ))
                    // 円の周りを回転するサイン波
                    //drawDataLst.add(DrawData(DrawDataID.ID_000201_SINE_WAVE_CIRCLE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000041_wave_sine_circle) ))
                    // サイン波
                    drawDataLst.add(DrawData(DrawDataID.ID_000200_SINE_WAVE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000200_wave_sine) ))
                }
                // 波を選択したときの描画データの一覧
                MenuItem.MENU_WAVE_COLOR -> {
                    // RGBの棒線で紡ぐ
                    drawDataLst.add(DrawData(DrawDataID.ID_000207_WAVE_RGB_WEAVE_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000207_wave_rgb_weave_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/8bff3f4741cf6436d878c88e3afddb54/tumblr_mjxn87AdVk1r2geqjo1_500.gif") ))
                    // 1536色
                    drawDataLst.add(DrawData(DrawDataID.ID_000205_COLOR_1536,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000205_wave_color_1536)))
                    // 768色(暗色)
                    drawDataLst.add(DrawData(DrawDataID.ID_000206_COLOR_768_DARK,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000206_wave_color_768_dark)))
                }
                // "複数円"を選択したときの描画データの一覧
                MenuItem.MENU_CIRCLE_CIRCLES -> {
                    // 花火01
                    drawDataLst.add(DrawData(DrawDataID.ID_000806_CIRCLE_FIREWORK_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000806_circle_firework_01) ))
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
                    // 太陽
                    drawDataLst.add(DrawData(DrawDataID.ID_000812_CIRCLE_MORPH_SUN,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000812_circle_morph_sun),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/e915741f2047ea59304cd90240b92baa/tumblr_mji84mNDUm1r2geqjo1_500.gif") ))
                    // "円⇔正方形の変形"のタイリング
                    drawDataLst.add(DrawData(DrawDataID.ID_000811_CIRCLE_MORPH_CIRCLE2SQUARE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000811_circle_morph_circle2square),
                        stillImageParam = floatArrayOf(0.2f), motionImageParam = floatArrayOf(0f) ))
                    // 円⇔正方形の変形
                    drawDataLst.add(DrawData(DrawDataID.ID_000810_CIRCLE_MORPH_CIRCLE2SQUARE,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000810_circle_morph_circle2square) ))
                }
                // 敷き詰めた円を描画するメニュー
                MenuItem.MENU_CIRCLE_TILE -> {
                    // PorterDuff"水色と紫色"の"DARKENとLIGHTEN"
                    drawDataLst.add(DrawData(DrawDataID.ID_000809_CIRCLE_PORTERDUFF_DL_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000809_circle_porterduff_dl_01) ))
                    // 日本の国旗⇔パラオの国旗02
                    drawDataLst.add(DrawData(DrawDataID.ID_000808_CIRCLE_JAPAN_2_PALAU_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000808_circle_japan_2_palau_02) ))
                    // 日本の国旗⇔パラオの国旗01
                    drawDataLst.add(DrawData(DrawDataID.ID_000807_CIRCLE_JAPAN_2_PALAU_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000807_circle_japan_2_palau_01) ))
                    // 円弧のタイリング
                    drawDataLst.add(DrawData(DrawDataID.ID_000824_CIRCLE_TILE_ARC_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000824_circle_tile_arc_01) ))
                    // 円のタイリング(左右・左下右上・右下左上の順でスライド)
                    drawDataLst.add(DrawData(DrawDataID.ID_000823_CIRCLE_TILE_SLIDE_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000823_circle_tile_slide_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/4724d7f3ebfb87faf6c1c89532a5b122/tumblr_n39wd4r7tp1r2geqjo1_500.gif") ))
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
                    // 正方形内を正方形が移動
                    drawDataLst.add(DrawData(DrawDataID.ID_000613_SQUARE_IN_SQUARE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000613_polygon_square_in_square_01) ))
                    // 三角形内を線が移動
                    drawDataLst.add(DrawData(DrawDataID.ID_000612_LINES_IN_TRIANGLE_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000612_polygon_lines_in_triangle_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://twitter.com/InfinityLoopGIF/status/1120846923924234240") ))
                    // 正方形⇔十字01
                    drawDataLst.add(DrawData(DrawDataID.ID_000611_SQUARE_2_CROSS_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000611_polygon_square_2_cross_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/2222aaac0c1642796c14e59a59ec7486/tumblr_ocmkngaCFl1r2geqjo1_540.gif") ))
                    // 六角形が波打つようにスケールを変更02
                    drawDataLst.add(DrawData(DrawDataID.ID_000610_HEXAGON_SCALE_02,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000610_polygon_hexagon_scale_02) ))
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
                    // 正方形⇔正方形
                    drawDataLst.add(DrawData(DrawDataID.ID_000615_SQUARE_2_SQUARE_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000615_polygon_square_2_square_01),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://twitter.com/beesandbombs/status/1044920001646473216") ))
                    // 三角形⇔ひし形02
                    drawDataLst.add(DrawData(DrawDataID.ID_000614_TRIANGLE_2_HEXAGON_02,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000614_polygon_triangle_2_hexagon_02),
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/9d2270db688ea684dd5e86b6c718e334/tumblr_oqhhibwGeV1r2geqjo1_540.gif") ))
                    // 六角形が波打つようにスケールを変更01
                    drawDataLst.add(DrawData(DrawDataID.ID_000609_HEXAGON_SCALE_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000609_polygon_hexagon_scale_01) ))
                    // 正方形を×印で並べる
                    drawDataLst.add(DrawData(DrawDataID.ID_000608_SQUARE_CROSS_01,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000608_polygon_square_cross_01) ))
                    // 六角形に沿って三角形が移動
                    drawDataLst.add(DrawData(DrawDataID.ID_000607_TRIANGLE_2_HEXAGON_01,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000607_polygon_triangle_2_hexagon_01) ))
                    // 正方形⇔ひし形02
                    drawDataLst.add(DrawData(DrawDataID.ID_000606_SQUARE_2_DIAMOND_02,DrawFragmentType.FT_D2_CREDIT_01,resources.getString(R.string.draw_000606_polygon_square_2_diamond_02) ))
                    // 三角形⇔ひし形01
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
                    drawDataLst.add(DrawData(DrawDataID.ID_000709_4_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000709_polygon_4_out_3),
                        stillImageParam = floatArrayOf(4f,3f), motionImageParam = floatArrayOf(4f,3f) ))
                    // 四角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000710_4_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000710_polygon_4_out_4),
                        stillImageParam = floatArrayOf(4f,4f), motionImageParam = floatArrayOf(4f,4f) ))
                    // 四角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000711_4_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000711_polygon_4_out_5),
                        stillImageParam = floatArrayOf(4f,5f), motionImageParam = floatArrayOf(4f,5f) ))
                    // 四角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000712_4_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000712_polygon_4_out_6),
                        stillImageParam = floatArrayOf(4f,6f), motionImageParam = floatArrayOf(4f,6f) ))
                    // 四角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000713_4_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000713_polygon_4_out_7),
                        stillImageParam = floatArrayOf(4f,7f), motionImageParam = floatArrayOf(4f,7f) ))
                    // 四角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000714_4_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000714_polygon_4_out_8),
                        stillImageParam = floatArrayOf(4f,8f), motionImageParam = floatArrayOf(4f,8f) ))
                    // 四角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000715_4_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000715_polygon_4_out_9),
                        stillImageParam = floatArrayOf(4f,9f), motionImageParam = floatArrayOf(4f,9f) ))
                    // 四角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000716_4_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000716_polygon_4_out_10),
                        stillImageParam = floatArrayOf(4f,10f), motionImageParam = floatArrayOf(4f,10f) ))
                    // 五角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000717_5_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000717_polygon_5_out_3),
                        stillImageParam = floatArrayOf(5f,3f), motionImageParam = floatArrayOf(5f,3f) ))
                    // 五角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000718_5_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000718_polygon_5_out_4),
                        stillImageParam = floatArrayOf(5f,4f), motionImageParam = floatArrayOf(5f,4f) ))
                    // 五角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000719_5_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000719_polygon_5_out_5),
                        stillImageParam = floatArrayOf(5f,5f), motionImageParam = floatArrayOf(5f,5f) ))
                    // 五角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000720_5_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000720_polygon_5_out_6),
                        stillImageParam = floatArrayOf(5f,6f), motionImageParam = floatArrayOf(5f,6f) ))
                    // 五角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000721_5_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000721_polygon_5_out_7),
                        stillImageParam = floatArrayOf(5f,7f), motionImageParam = floatArrayOf(5f,7f) ))
                    // 五角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000722_5_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000722_polygon_5_out_8),
                        stillImageParam = floatArrayOf(5f,8f), motionImageParam = floatArrayOf(5f,8f) ))
                    // 五角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000723_5_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000723_polygon_5_out_9),
                        stillImageParam = floatArrayOf(5f,9f), motionImageParam = floatArrayOf(5f,9f) ))
                    // 五角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000724_5_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000724_polygon_5_out_10),
                        stillImageParam = floatArrayOf(5f,10f), motionImageParam = floatArrayOf(5f,10f) ))
                    // 六角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000725_6_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000725_polygon_6_out_3),
                        stillImageParam = floatArrayOf(6f,3f), motionImageParam = floatArrayOf(6f,3f) ))
                    // 六角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000726_6_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000726_polygon_6_out_4),
                        stillImageParam = floatArrayOf(6f,4f), motionImageParam = floatArrayOf(6f,4f) ))
                    // 六角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000727_6_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000727_polygon_6_out_5),
                        stillImageParam = floatArrayOf(6f,5f), motionImageParam = floatArrayOf(6f,5f) ))
                    // 六角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000728_6_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000728_polygon_6_out_6),
                        stillImageParam = floatArrayOf(6f,6f), motionImageParam = floatArrayOf(6f,6f) ))
                    // 六角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000729_6_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000729_polygon_6_out_7),
                        stillImageParam = floatArrayOf(6f,7f), motionImageParam = floatArrayOf(6f,7f) ))
                    // 六角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000730_6_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000730_polygon_6_out_8),
                        stillImageParam = floatArrayOf(6f,8f), motionImageParam = floatArrayOf(6f,8f) ))
                    // 六角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000731_6_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000731_polygon_6_out_9),
                        stillImageParam = floatArrayOf(6f,9f), motionImageParam = floatArrayOf(6f,9f) ))
                    // 六角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000732_6_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000732_polygon_6_out_10),
                        stillImageParam = floatArrayOf(6f,10f), motionImageParam = floatArrayOf(6f,10f) ))
                    // 七角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000733_7_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000733_polygon_7_out_3),
                        stillImageParam = floatArrayOf(7f,3f), motionImageParam = floatArrayOf(7f,3f) ))
                    // 七角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000734_7_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000734_polygon_7_out_4),
                        stillImageParam = floatArrayOf(7f,4f), motionImageParam = floatArrayOf(7f,4f) ))
                    // 七角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000735_7_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000735_polygon_7_out_5),
                        stillImageParam = floatArrayOf(7f,5f), motionImageParam = floatArrayOf(7f,5f) ))
                    // 七角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000736_7_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000736_polygon_7_out_6),
                        stillImageParam = floatArrayOf(7f,6f), motionImageParam = floatArrayOf(7f,6f) ))
                    // 七角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000737_7_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000737_polygon_7_out_7),
                        stillImageParam = floatArrayOf(7f,7f), motionImageParam = floatArrayOf(7f,7f) ))
                    // 七角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000738_7_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000738_polygon_7_out_8),
                        stillImageParam = floatArrayOf(7f,8f), motionImageParam = floatArrayOf(7f,8f) ))
                    // 七角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000739_7_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000739_polygon_7_out_9),
                        stillImageParam = floatArrayOf(7f,9f), motionImageParam = floatArrayOf(7f,9f) ))
                    // 七角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000740_7_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000740_polygon_7_out_10),
                        stillImageParam = floatArrayOf(7f,10f), motionImageParam = floatArrayOf(7f,10f) ))
                    // 八角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000741_8_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000741_polygon_8_out_3),
                        stillImageParam = floatArrayOf(8f,3f), motionImageParam = floatArrayOf(8f,3f) ))
                    // 八角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000742_8_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000742_polygon_8_out_4),
                        stillImageParam = floatArrayOf(8f,4f), motionImageParam = floatArrayOf(8f,4f) ))
                    // 八角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000743_8_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000743_polygon_8_out_5),
                        stillImageParam = floatArrayOf(8f,5f), motionImageParam = floatArrayOf(8f,5f) ))
                    // 八角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000744_8_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000744_polygon_8_out_6),
                        stillImageParam = floatArrayOf(8f,6f), motionImageParam = floatArrayOf(8f,6f) ))
                    // 八角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000745_8_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000745_polygon_8_out_7),
                        stillImageParam = floatArrayOf(8f,7f), motionImageParam = floatArrayOf(8f,7f) ))
                    // 八角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000746_8_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000746_polygon_8_out_8),
                        stillImageParam = floatArrayOf(8f,8f), motionImageParam = floatArrayOf(8f,8f) ))
                    // 八角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000747_8_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000747_polygon_8_out_9),
                        stillImageParam = floatArrayOf(8f,9f), motionImageParam = floatArrayOf(8f,9f) ))
                    // 八角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000748_8_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000748_polygon_8_out_10),
                        stillImageParam = floatArrayOf(8f,10f), motionImageParam = floatArrayOf(8f,10f) ))
                    // 九角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000749_9_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000749_polygon_9_out_3),
                        stillImageParam = floatArrayOf(9f,3f), motionImageParam = floatArrayOf(9f,3f) ))
                    // 九角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000750_9_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000750_polygon_9_out_4),
                        stillImageParam = floatArrayOf(9f,4f), motionImageParam = floatArrayOf(9f,4f) ))
                    // 九角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000751_9_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000751_polygon_9_out_5),
                        stillImageParam = floatArrayOf(9f,5f), motionImageParam = floatArrayOf(9f,5f) ))
                    // 九角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000752_9_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000752_polygon_9_out_6),
                        stillImageParam = floatArrayOf(9f,6f), motionImageParam = floatArrayOf(9f,6f) ))
                    // 九角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000753_9_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000753_polygon_9_out_7),
                        stillImageParam = floatArrayOf(9f,7f), motionImageParam = floatArrayOf(9f,7f) ))
                    // 九角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000754_9_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000754_polygon_9_out_8),
                        stillImageParam = floatArrayOf(9f,8f), motionImageParam = floatArrayOf(9f,8f) ))
                    // 九角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000755_9_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000755_polygon_9_out_9),
                        stillImageParam = floatArrayOf(9f,9f), motionImageParam = floatArrayOf(9f,9f) ))
                    // 九角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000756_9_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000756_polygon_9_out_10),
                        stillImageParam = floatArrayOf(9f,10f), motionImageParam = floatArrayOf(9f,10f) ))
                    // 十角形out三角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000757_10_OUT_3,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000757_polygon_10_out_3),
                        stillImageParam = floatArrayOf(10f,3f), motionImageParam = floatArrayOf(10f,3f) ))
                    // 十角形out四角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000758_10_OUT_4,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000758_polygon_10_out_4),
                        stillImageParam = floatArrayOf(10f,4f), motionImageParam = floatArrayOf(10f,4f) ))
                    // 十角形out五角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000759_10_OUT_5,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000759_polygon_10_out_5),
                        stillImageParam = floatArrayOf(10f,5f), motionImageParam = floatArrayOf(10f,5f) ))
                    // 十角形out六角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000760_10_OUT_6,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000760_polygon_10_out_6),
                        stillImageParam = floatArrayOf(10f,6f), motionImageParam = floatArrayOf(10f,6f) ))
                    // 十角形out七角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000761_10_OUT_7,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000761_polygon_10_out_7),
                        stillImageParam = floatArrayOf(10f,7f), motionImageParam = floatArrayOf(10f,7f) ))
                    // 十角形out八角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000762_10_OUT_8,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000762_polygon_10_out_8),
                        stillImageParam = floatArrayOf(10f,8f), motionImageParam = floatArrayOf(10f,8f) ))
                    // 十角形out九角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000763_10_OUT_9,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000763_polygon_10_out_9),
                        stillImageParam = floatArrayOf(10f,9f), motionImageParam = floatArrayOf(10f,9f) ))
                    // 十角形out十角形
                    drawDataLst.add(DrawData(DrawDataID.ID_000764_10_OUT_10,DrawFragmentType.FT_D2_01,resources.getString(R.string.draw_000764_polygon_10_out_10),
                        stillImageParam = floatArrayOf(10f,10f), motionImageParam = floatArrayOf(10f,10f) ))
                }
                // 多角形をずらして描画するデータの一覧
                MenuItem.MENU_POLYGON_SLIDE -> {
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
                    // トーラスの結び目
                    //drawDataLst.add(DrawData(DrawDataID.ID_001011_GL_TORUS_KNOT_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001011_gl_torus_knot_01),
                    //    drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 11f, "scale" to 1f) ))
                    // 円柱02
                    drawDataLst.add(DrawData(DrawDataID.ID_001010_GL_CYLINDER_02,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001010_gl_cylinder_02),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 10f, "scale" to 1f) ))
                    // 円錐台
                    drawDataLst.add(DrawData(DrawDataID.ID_001009_GL_CONE_TRUNCATED_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001009_gl_cone_truncated_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 9f, "scale" to 1f) ))
                    // 円錐
                    drawDataLst.add(DrawData(DrawDataID.ID_001008_GL_CONE_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001008_gl_cone_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 8f, "scale" to 1f) ))
                    // 円柱01
                    drawDataLst.add(DrawData(DrawDataID.ID_001007_GL_CYLINDER_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001007_gl_cylinder_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 7f, "scale" to 1f) ))
                    // トーラス
                    drawDataLst.add(DrawData(DrawDataID.ID_001006_GL_TORUS_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001006_gl_torus_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 6f, "scale" to 0.25f) ))
                    // 球
                    drawDataLst.add(DrawData(DrawDataID.ID_001005_GL_SPHERE_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001005_gl_sphere_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 5f, "scale" to 1f) ))
                    // 正二十面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001004_GL_ICOSAHEDRON_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001004_gl_icosahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 4f, "scale" to 0.5f) ))
                    // 正十二面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001003_GL_DODECAHEDRON_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001003_gl_dodecahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 3f, "scale" to 0.5f) ))
                    // 正八面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001002_GL_OCTAHEDRON_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001002_gl_octahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 2f, "scale" to 0.8f) ))
                    // 立方体
                    drawDataLst.add(DrawData(DrawDataID.ID_001001_GL_CUBE_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001001_gl_cube_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 1f, "scale" to 0.6f) ))
                    // 正四面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001000_GL_TETRAHEDRON_01,DrawFragmentType.FT_D3_ES32_01,resources.getString(R.string.draw_001000_gl_tetrahedron_01),
                        drawViewType = DrawViewType.DVT_GL, motionImageV2Param = mutableMapOf("modelType" to 0f) ))
                }
                // 立方体に座標変換を施したものを描画するメニュー
                MenuItem.MENU_POLYHEDRON_CUBE_TRANSFORM -> {
                    // 正四面体
                    drawDataLst.add(DrawData(DrawDataID.ID_001112_GL_CUBE_LIKE_HEXAGON_01,DrawFragmentType.FT_D3_ES32_CREDIT_01,resources.getString(R.string.draw_001112_gl_cube_like_hexagon_01), drawViewType = DrawViewType.DVT_GL,
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://twitter.com/InfinityLoopGIF/status/1149451600248868864") ))
                    // 立方体座標変換12
                    drawDataLst.add(DrawData(DrawDataID.ID_001111_GL_CUBE_TRANSFORM_12,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001111_gl_cube_transform_12), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換11
                    drawDataLst.add(DrawData(DrawDataID.ID_001110_GL_CUBE_TRANSFORM_11,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001110_gl_cube_transform_11), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換10
                    drawDataLst.add(DrawData(DrawDataID.ID_001109_GL_CUBE_TRANSFORM_10,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001109_gl_cube_transform_10), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換09
                    drawDataLst.add(DrawData(DrawDataID.ID_001108_GL_CUBE_TRANSFORM_09,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001108_gl_cube_transform_09), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換08
                    drawDataLst.add(DrawData(DrawDataID.ID_001107_GL_CUBE_TRANSFORM_08,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001107_gl_cube_transform_08), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換07
                    drawDataLst.add(DrawData(DrawDataID.ID_001106_GL_CUBE_TRANSFORM_07,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001106_gl_cube_transform_07), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換06
                    drawDataLst.add(DrawData(DrawDataID.ID_001105_GL_CUBE_TRANSFORM_06,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001105_gl_cube_transform_06), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換05
                    drawDataLst.add(DrawData(DrawDataID.ID_001104_GL_CUBE_TRANSFORM_05,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001104_gl_cube_transform_05), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換04
                    drawDataLst.add(DrawData(DrawDataID.ID_001103_GL_CUBE_TRANSFORM_04,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001103_gl_cube_transform_04), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換03
                    drawDataLst.add(DrawData(DrawDataID.ID_001102_GL_CUBE_TRANSFORM_03,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001102_gl_cube_transform_03), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換02
                    drawDataLst.add(DrawData(DrawDataID.ID_001101_GL_CUBE_TRANSFORM_02,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001101_gl_cube_transform_02), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体座標変換01
                    drawDataLst.add(DrawData(DrawDataID.ID_001100_GL_CUBE_TRANSFORM_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001100_gl_cube_transform_01), drawViewType = DrawViewType.DVT_GL ))
                }
                // 球体に座標変換を施したものを描画するメニュー
                MenuItem.MENU_POLYHEDRON_SPHERE_TRANSFORM -> {
                    // 球体:キューブ環境マップ01
                    drawDataLst.add(DrawData(DrawDataID.ID_001304_GL_SPHERE_CUBEMAP_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001304_gl_sphere_cubemap_01), drawViewType = DrawViewType.DVT_GL ))
                    // 球体色シフト02
                    drawDataLst.add(DrawData(DrawDataID.ID_001303_GL_SPHERE_COLOR_SHIFT_02,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001303_gl_sphere_color_shift_02), drawViewType = DrawViewType.DVT_GL ))
                    // 球体色シフト01
                    drawDataLst.add(DrawData(DrawDataID.ID_001302_GL_SPHERE_COLOR_SHIFT_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001302_gl_sphere_color_shift_01), drawViewType = DrawViewType.DVT_GL ))
                    // 球体座標変換02
                    drawDataLst.add(DrawData(DrawDataID.ID_001301_GL_SPHERE_TRANSFORM_02,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001301_gl_sphere_transform_02), drawViewType = DrawViewType.DVT_GL ))
                    // 球体座標変換01
                    drawDataLst.add(DrawData(DrawDataID.ID_001300_GL_SPHERE_TRANSFORM_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001300_gl_sphere_transform_01), drawViewType = DrawViewType.DVT_GL ))
                }
                // 多面体の展開図を描画するメニュー
                MenuItem.MENU_POLYHEDRON_NET -> {
                    // 立方体の展開図02
                    drawDataLst.add(DrawData(DrawDataID.ID_001200_GL_NET_CUBE_02,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001200_gl_net_cube_02), drawViewType = DrawViewType.DVT_GL ))
                    // 正二十面体の展開図01
                    drawDataLst.add(DrawData(DrawDataID.ID_001205_GL_NET_ICOSAHEDRON_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001205_gl_net_icosahedron_01), drawViewType = DrawViewType.DVT_GL ))
                    // 正十二面体の展開図01
                    drawDataLst.add(DrawData(DrawDataID.ID_001204_GL_NET_DODECAHEDRON_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001204_gl_net_dodecahedron_01), drawViewType = DrawViewType.DVT_GL ))
                    // 正八面体の展開図01
                    drawDataLst.add(DrawData(DrawDataID.ID_001203_GL_NET_OCTAHEDRON_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001203_gl_net_octahedron_01), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体の展開図01
                    drawDataLst.add(DrawData(DrawDataID.ID_001202_GL_NET_CUBE_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001202_gl_net_cube_01), drawViewType = DrawViewType.DVT_GL ))
                    // 正四面体の展開図02
                    drawDataLst.add(DrawData(DrawDataID.ID_001201_GL_NET_TETRAHEDRON_02,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001201_gl_net_tetrahedron_02), drawViewType = DrawViewType.DVT_GL ))
                }
                // 多面体内にフラクタルを描画するメニュー
                MenuItem.MENU_POLYHEDRON_FRACTAL -> {
                    // コッホ雪片inキューブ環境マップ01
                    drawDataLst.add(DrawData(DrawDataID.ID_001401_GL_KOCH_SNOWFLAKE_CUBEMAP_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001401_gl_koch_snowflake_cubemap_01), drawViewType = DrawViewType.DVT_GL ))
                    // 立方体の中をコッホ雪片が回転
                    drawDataLst.add(DrawData(DrawDataID.ID_001400_GL_KOCH_SNOWFLAKE_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001400_gl_koch_snowflake_01), drawViewType = DrawViewType.DVT_GL ))
                }
                // ３次元波を描画するメニュー
                MenuItem.MENU_POLYHEDRON_WAVE -> {
                    // 円が床を境に円運動
                    drawDataLst.add(DrawData(DrawDataID.ID_001504_GL_WAVE_CIRCLE_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001504_gl_wave_circle_01), drawViewType = DrawViewType.DVT_GL ))
                    // 波うつ床02
                    drawDataLst.add(DrawData(DrawDataID.ID_001503_GL_WAVE_FLOOR_02,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001503_gl_wave_floor_02), drawViewType = DrawViewType.DVT_GL ))
                    // 波うつ床01
                    drawDataLst.add(DrawData(DrawDataID.ID_001502_GL_WAVE_FLOOR_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001502_gl_wave_floor_01), drawViewType = DrawViewType.DVT_GL ))
                    // 円の位相をずらして描画
                    drawDataLst.add(DrawData(DrawDataID.ID_001501_GL_CIRCLE_PHASE_SHIFT_01,DrawFragmentType.FT_D3_ES32_02,resources.getString(R.string.draw_001501_gl_circle_phase_shift_01), drawViewType = DrawViewType.DVT_GL ))
                    // 線でHelixを描画
                    drawDataLst.add(DrawData(DrawDataID.ID_001500_GL_HELIX_01,DrawFragmentType.FT_D3_ES32_CREDIT_01,resources.getString(R.string.draw_001500_gl_helix_01), drawViewType = DrawViewType.DVT_GL,
                        creditMap = mutableMapOf<String,String>("name" to "beesandbombs", "url" to "https://66.media.tumblr.com/4c437d88df4fa9bac581e50310a02a51/tumblr_milgzyY8vO1r2geqjo1_500.gif") ))
                }
                // GLSLを用いて描画するメニュー
                MenuItem.MENU_POLYHEDRON_GLSL -> {
                    // 10個の円が競争する
                    drawDataLst.add(DrawData(DrawDataID.ID_001603_GLSL_CIRCLE10_RUN,DrawFragmentType.FT_GS_ES32_02,resources.getString(R.string.draw_001603_glsl_circle10_run), drawViewType = DrawViewType.DVT_GL,
                        motionImageV2Param = mutableMapOf<String,Float>( "shader" to 1603f  )  ))
                    // 放射状に広がるを描画
                    drawDataLst.add(DrawData(DrawDataID.ID_001602_GLSL_ZOOM_LINE,DrawFragmentType.FT_GS_ES32_CREDIT_01,resources.getString(R.string.draw_001602_glsl_zoom_line), drawViewType = DrawViewType.DVT_GL,
                        creditMap = mutableMapOf<String,String>("name" to "wgld.org", "url" to "https://wgld.org/d/glsl/g004.html"),
                        motionImageV2Param = mutableMapOf<String,Float>( "shader" to 1602f  )  ))
                    // オーブを描画
                    drawDataLst.add(DrawData(DrawDataID.ID_001601_GLSL_ORB,DrawFragmentType.FT_GS_ES32_CREDIT_01,resources.getString(R.string.draw_001601_glsl_orb), drawViewType = DrawViewType.DVT_GL,
                        creditMap = mutableMapOf<String,String>("name" to "wgld.org", "url" to "https://wgld.org/d/glsl/g003.html"),
                        motionImageV2Param = mutableMapOf<String,Float>( "shader" to 1601f  )  ))
                    // 同心円を描画
                    drawDataLst.add(DrawData(DrawDataID.ID_001600_GLSL_CONCENTRIC_CIRCLE,DrawFragmentType.FT_GS_ES32_CREDIT_01,resources.getString(R.string.draw_001600_glsl_concentric_circle), drawViewType = DrawViewType.DVT_GL,
                        creditMap = mutableMapOf<String,String>("name" to "wgld.org", "url" to "https://wgld.org/d/glsl/g002.html"),
                        motionImageV2Param = mutableMapOf<String,Float>( "shader" to 1600f  )  ))
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