package milu.kiriu2010.gui.vbo.es32

import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs

// ----------------------------------------------------------------
// VBO親玉クラス
// ----------------------------------------------------------------
// 2019.05.31
// 2019.06.07 バッファにデータを設定するときの変数usageを追加
// 2019.06.14 バッファにデータを設定するときの変数名を位置用に変更
// 2019.07.12 バッファにデータを設定するときの変数usageOffを追加
// ----------------------------------------------------------------
abstract class ES32VBOAbs {
    // 描画モデル
    lateinit var model: MgModelAbs
    // VBOのハンドル
    lateinit var hVBO: IntArray
    // IBOのハンドル
    lateinit var hIBO: IntArray

    // VBOバッファにデータを設定するときのusage(位置)
    var usagePos = GLES32.GL_STATIC_DRAW
    // VBOバッファにデータを設定するときのusage(色)
    var usageCol = GLES32.GL_STATIC_DRAW
    // VBOバッファにデータを設定するときのusage(オフセット)
    var usageOff = GLES32.GL_STATIC_DRAW

    abstract fun makeVIBO(modelAbs: MgModelAbs)

    fun deleteVIBO() {
        if (this::hVBO.isInitialized) {
            GLES32.glDeleteBuffers(hVBO.size,hVBO,0)
        }
        if (this::hIBO.isInitialized) {
            GLES32.glDeleteBuffers(hIBO.size,hIBO,0)
        }
    }
}
