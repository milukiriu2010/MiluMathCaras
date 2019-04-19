package milu.kiriu2010.gui.shader

import android.opengl.GLES20

abstract class MgShader {
    // シェーダが登録されたプログラムオブジェクトのハンドル
    protected var programHandle = -1

    // シェーダを削除
    fun deleteShader() {
        GLES20.glDeleteProgram(programHandle)
    }

    abstract fun loadShader(): MgShader
}
