package milu.kiriu2010.gui.shader.es20

import android.opengl.GLES20

// --------------------------------------
// GLSL ES20用シェーダ
// --------------------------------------
// 2019.05.22 リソース解放
// 2019.05.30 attribute/uniformハンドル
// --------------------------------------
abstract class ES20MgShader {
    // 頂点シェーダのハンドル
    var svhandle: Int = -1
    // フラグメントシェーダのハンドル
    var sfhandle: Int = -1

    // シェーダが登録されたプログラムオブジェクトのハンドル
    protected var programHandle = -1

    // attributeのハンドル
    lateinit var hATTR: IntArray
    // uniformのハンドル
    lateinit var hUNI: IntArray

    // シェーダを削除
    fun deleteShader() {
        if ( svhandle != -1 ) {
            GLES20.glDeleteShader(svhandle)
        }
        if ( sfhandle != -1 ) {
            GLES20.glDeleteShader(sfhandle)
        }
        if ( programHandle != -1 ) {
            GLES20.glDeleteProgram(programHandle)
        }
        if ( this::hATTR.isInitialized ) {
            (0 until hATTR.size).forEach {
                GLES20.glDisableVertexAttribArray(it)
            }
        }
    }

    abstract fun loadShader(): ES20MgShader
}
