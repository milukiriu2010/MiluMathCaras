package milu.kiriu2010.gui.vbo.es20

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VBO親玉クラス
// --------------------------------
// 2019.05.31
// --------------------------------
abstract class ES20VBOAbs {
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    abstract fun makeVIBO(model: MgModelAbs)

    fun deleteVIBO() {
        if (this::hVBO.isInitialized) {
            GLES20.glDeleteBuffers(hVBO.size,hVBO,0)
        }
        if (this::hIBO.isInitialized) {
            GLES20.glDeleteBuffers(hIBO.size,hIBO,0)
        }
    }
}
