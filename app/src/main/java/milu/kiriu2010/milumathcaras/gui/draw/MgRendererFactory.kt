package milu.kiriu2010.milumathcaras.gui.draw

import android.content.Context
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.Polyhedron01Renderer
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
                // 球
                DrawDataID.ID_001005_GL_SPHERE_01 -> Polyhedron01Renderer(context)
                // トーラス
                DrawDataID.ID_001006_GL_TORUS_01 -> Polyhedron01Renderer(context)
                else -> throw RuntimeException("Not Found GLSurfaceView.Renderer")
            }

            //if ( notifyCallback != null )
            return renderer
        }
    }
}