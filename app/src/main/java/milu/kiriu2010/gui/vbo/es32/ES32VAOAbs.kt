package milu.kiriu2010.gui.vbo.es32

import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs

// ----------------------------------------------------------------
// VAO親玉クラス
// ----------------------------------------------------------------
// ES3.2用
// ----------------------------------------------------------------
// 2019.05.31
// 2019.06.07 バッファにデータを設定するときの変数usageを追加
// 2019.06.11 VAO追加
// 2019.06.14 バッファにデータを設定するときの変数名を位置用に変更
// 2019.06.17 ES3.0用をコピー
// ----------------------------------------------------------------
abstract class ES32VAOAbs {
    // 描画モデル
    lateinit var model: MgModelAbs
    // VAOのハンドル
    lateinit var hVAO: IntArray
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    // VBOバッファにデータを設定するときのusage(位置)
    var usagePos = GLES32.GL_STATIC_DRAW

    abstract fun makeVIBO(modelAbs: MgModelAbs)

    fun deleteVIBO() {
        if (this::hVAO.isInitialized) {
            GLES32.glDeleteVertexArrays(hVAO.size,hVAO,0)
        }
        if (this::hVBO.isInitialized) {
            GLES32.glDeleteBuffers(hVBO.size,hVBO,0)
        }
        if (this::hIBO.isInitialized) {
            GLES32.glDeleteBuffers(hIBO.size,hIBO,0)
        }
    }
}
