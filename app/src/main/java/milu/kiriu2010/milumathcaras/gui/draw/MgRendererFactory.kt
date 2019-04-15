package milu.kiriu2010.milumathcaras.gui.draw

import android.content.Context
import android.opengl.GLSurfaceView
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.tetrahedron.tetrahedron01.Tetrahedron01Renderer
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import java.lang.RuntimeException

class MgRendererFactory {
    companion object {
        fun createInstance(id: DrawDataID, context: Context, notifyCallback: NotifyCallback? = null): MgRenderer {
            val renderer = when (id) {
                // 正四面体
                DrawDataID.ID_001000_GL_TETRAHEDRON_01 -> Tetrahedron01Renderer(context)
                else -> throw RuntimeException("Not Found GLSurfaceView.Renderer")
            }

            //if ( notifyCallback != null )
            return renderer
        }
    }
}