package milu.kiriu2010.gui.vbo.es20

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs

// ----------------------------------------------------------------
// VBO親玉クラス
// ----------------------------------------------------------------
// 2019.05.31
// 2019.06.07 バッファにデータを設定するときの変数usageを追加
// 2019.06.14 バッファにデータを設定するときの変数名を位置用に変更
// ----------------------------------------------------------------
abstract class ES20VBOAbs {
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    // VBOバッファにデータを設定するときのusage(位置)
    var usagePos = GLES20.GL_STATIC_DRAW

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
