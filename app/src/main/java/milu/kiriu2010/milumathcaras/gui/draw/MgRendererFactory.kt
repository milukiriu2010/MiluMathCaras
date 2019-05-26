package milu.kiriu2010.milumathcaras.gui.draw

import android.content.Context
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube.*
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.cube.NetCube01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.octahedron.NetOctahedron01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.tetrahedron.NetTetrahedron01Renderer
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.tetrahedron.NetTetrahedron02Renderer
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.view.Polyhedron01Renderer
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import java.lang.RuntimeException

class MgRendererFactory {
    companion object {
        fun createInstance(id: DrawDataID, context: Context, notifyCallback: NotifyCallback? = null): MgRenderer {
            val renderer = when (id) {
                // 正四面体
                DrawDataID.ID_001000_GL_TETRAHEDRON_01 -> Polyhedron01Renderer(context)
                // 立方体
                DrawDataID.ID_001001_GL_CUBE_01 -> Polyhedron01Renderer(context)
                // 正八面体
                DrawDataID.ID_001002_GL_OCTAHEDRON_01 -> Polyhedron01Renderer(context)
                // 正十二面体
                DrawDataID.ID_001003_GL_DODECAHEDRON_01 -> Polyhedron01Renderer(context)
                // 正二十面体
                DrawDataID.ID_001004_GL_ICOSAHEDRON_01 -> Polyhedron01Renderer(context)
                // 球
                DrawDataID.ID_001005_GL_SPHERE_01 -> Polyhedron01Renderer(context)
                // トーラス
                DrawDataID.ID_001006_GL_TORUS_01 -> Polyhedron01Renderer(context)
                // 円柱
                DrawDataID.ID_001007_GL_CYLINDER_01 -> Polyhedron01Renderer(context)
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
                // 正四面体の展開図01
                DrawDataID.ID_001200_GL_NET_TETRAHEDRON_01 -> NetTetrahedron01Renderer(context)
                // 正四面体の展開図02
                DrawDataID.ID_001201_GL_NET_TETRAHEDRON_02 -> NetTetrahedron02Renderer(context)
                // 立方体の展開図01
                DrawDataID.ID_001202_GL_NET_CUBE_01 -> NetCube01Renderer(context)
                // 正八面体の展開図01
                DrawDataID.ID_001203_GL_NET_OCTAHEDRON_01 -> NetOctahedron01Renderer(context)
                else -> throw RuntimeException("Not Found GLSurfaceView.Renderer")
            }

            //if ( notifyCallback != null )
            return renderer
        }
    }
}