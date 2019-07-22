package milu.kiriu2010.milumathcaras.gui.draw.d3

import android.content.Context
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.d3.cube.*
import milu.kiriu2010.milumathcaras.gui.draw.d3.cubemap.SphereCubemap01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.fractal.KochSnowflake01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.glsl.GLSL01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.net.cube.NetCube01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.net.dodecahedron.NetDodecahedron01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.net.icosahedron.NetIcosahedron01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.net.octahedron.NetOctahedron01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.net.tetrahedron.NetTetrahedron02Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.SphereColorShift01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.SphereColorShift02Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.SphereTransform01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.SphereTransform02Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.view.Polyhedron02Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.wave.CirclePhaseShift01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.wave.Helix01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.wave.WaveCircle01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.wave.floor.WaveFloor01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.d3.wave.floor.WaveFloor02Renderer
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import java.lang.RuntimeException

class MgRendererFactory {
    companion object {
        fun createInstance(id: DrawDataID, context: Context, notifyCallback: NotifyCallback? = null): MgRenderer {
            val renderer = when (id) {
                // 正四面体
                DrawDataID.ID_001000_GL_TETRAHEDRON_01 -> Polyhedron02Renderer(context)
                // 立方体
                DrawDataID.ID_001001_GL_CUBE_01 -> Polyhedron02Renderer(context)
                // 正八面体
                DrawDataID.ID_001002_GL_OCTAHEDRON_01 -> Polyhedron02Renderer(context)
                // 正十二面体
                DrawDataID.ID_001003_GL_DODECAHEDRON_01 -> Polyhedron02Renderer(context)
                // 正二十面体
                DrawDataID.ID_001004_GL_ICOSAHEDRON_01 -> Polyhedron02Renderer(context)
                // 球
                DrawDataID.ID_001005_GL_SPHERE_01 -> Polyhedron02Renderer(context)
                // トーラス
                DrawDataID.ID_001006_GL_TORUS_01 -> Polyhedron02Renderer(context)
                // 円柱01
                DrawDataID.ID_001007_GL_CYLINDER_01 -> Polyhedron02Renderer(context)
                // 円錐
                DrawDataID.ID_001008_GL_CONE_01 -> Polyhedron02Renderer(context)
                // 円錐台
                DrawDataID.ID_001009_GL_CONE_TRUNCATED_01 -> Polyhedron02Renderer(context)
                // 円柱02
                DrawDataID.ID_001010_GL_CYLINDER_02 -> Polyhedron02Renderer(context)
                // 立方体座標変換01
                DrawDataID.ID_001100_GL_CUBE_TRANSFORM_01 -> CubeTransform01Renderer(context)
                // 立方体座標変換02
                DrawDataID.ID_001101_GL_CUBE_TRANSFORM_02 -> CubeTransform02Renderer(context)
                // 立方体座標変換03
                DrawDataID.ID_001102_GL_CUBE_TRANSFORM_03 -> CubeTransform03Renderer(context)
                // 立方体座標変換04
                DrawDataID.ID_001103_GL_CUBE_TRANSFORM_04 -> CubeTransform04Renderer(context)
                // 立方体座標変換05
                DrawDataID.ID_001104_GL_CUBE_TRANSFORM_05 -> CubeTransform05Renderer(context)
                // 立方体座標変換06
                DrawDataID.ID_001105_GL_CUBE_TRANSFORM_06 -> CubeTransform06Renderer(context)
                // 立方体座標変換07
                DrawDataID.ID_001106_GL_CUBE_TRANSFORM_07 -> CubeTransform07Renderer(context)
                // 立方体座標変換08
                DrawDataID.ID_001107_GL_CUBE_TRANSFORM_08 -> CubeTransform08Renderer(context)
                // 立方体座標変換09
                DrawDataID.ID_001108_GL_CUBE_TRANSFORM_09 -> CubeTransform09Renderer(context)
                // 立方体座標変換10
                DrawDataID.ID_001109_GL_CUBE_TRANSFORM_10 -> CubeTransform10Renderer(context)
                // 立方体座標変換11
                DrawDataID.ID_001110_GL_CUBE_TRANSFORM_11 -> CubeTransform11Renderer(context)
                // 立方体座標変換13
                DrawDataID.ID_001111_GL_CUBE_TRANSFORM_12 -> CubeTransform12Renderer(context)
                // 正四面体の展開図02
                DrawDataID.ID_001201_GL_NET_TETRAHEDRON_02 -> NetTetrahedron02Renderer(context)
                // 立方体の展開図01
                DrawDataID.ID_001202_GL_NET_CUBE_01 -> NetCube01Renderer(context)
                // 正八面体の展開図01
                DrawDataID.ID_001203_GL_NET_OCTAHEDRON_01 -> NetOctahedron01Renderer(context)
                // 正十二面体の展開図01
                DrawDataID.ID_001204_GL_NET_DODECAHEDRON_01 -> NetDodecahedron01Renderer(context)
                // 正二十面体の展開図01
                DrawDataID.ID_001205_GL_NET_ICOSAHEDRON_01 -> NetIcosahedron01Renderer(context)
                // 球体座標変換01
                DrawDataID.ID_001300_GL_SPHERE_TRANSFORM_01 -> SphereTransform01Renderer(context)
                // 球体座標変換02
                DrawDataID.ID_001301_GL_SPHERE_TRANSFORM_02 -> SphereTransform02Renderer(context)
                // 球体色シフト01
                DrawDataID.ID_001302_GL_SPHERE_COLOR_SHIFT_01 -> SphereColorShift01Renderer(context)
                // 球体色シフト02
                DrawDataID.ID_001303_GL_SPHERE_COLOR_SHIFT_02 -> SphereColorShift02Renderer(context)
                // 球体:キューブ環境マップ01
                DrawDataID.ID_001304_GL_SPHERE_CUBEMAP_01 -> SphereCubemap01Renderer(context)
                // 立方体の中をコッホ雪片が回転
                DrawDataID.ID_001400_GL_KOCH_SNOWFLAKE_01 -> KochSnowflake01Renderer(context)
                // 線でHelixを描画
                DrawDataID.ID_001500_GL_HELIX_01 -> Helix01Renderer(context)
                // 円の位相をずらして描画
                DrawDataID.ID_001501_GL_CIRCLE_PHASE_SHIFT_01 -> CirclePhaseShift01Renderer(context)
                // 波うつ床01
                DrawDataID.ID_001502_GL_WAVE_FLOOR_01 -> WaveFloor01Renderer(context)
                // 波うつ床02
                DrawDataID.ID_001503_GL_WAVE_FLOOR_02 -> WaveFloor02Renderer(context)
                // 円が床を境に円運動
                DrawDataID.ID_001504_GL_WAVE_CIRCLE_01 -> WaveCircle01Renderer(context)
                // 同心円を描画
                DrawDataID.ID_001600_GLSL_CONCENTRIC_CIRCLE -> GLSL01Renderer(context)
                // オーブを描画
                DrawDataID.ID_001601_GLSL_ORB -> GLSL01Renderer(context)
                // 放射状に広がる線を描画
                DrawDataID.ID_001602_GLSL_ZOOM_LINE -> GLSL01Renderer(context)
                // 放射状に広がる線を描画
                DrawDataID.ID_001603_GLSL_CIRCLE10_RUN -> GLSL01Renderer(context)
                // 該当なし
                else -> throw RuntimeException("Not Found GLSurfaceView.Renderer")
            }

            if ( notifyCallback != null ) {
            }

            return renderer
        }
    }
}