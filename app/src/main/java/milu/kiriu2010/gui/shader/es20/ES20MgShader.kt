package milu.kiriu2010.gui.shader.es20

import android.opengl.GLES20
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLES20Func

// ---------------------------------------------------
// GLSL ES20用シェーダ
// ---------------------------------------------------
// 2019.05.22 リソース解放
// 2019.05.30 attribute/uniformハンドル
// 2019.06.03 hATTRの解放をシェーダの解放より後に修正
// ---------------------------------------------------
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
        if ( this::hATTR.isInitialized ) {
            (0 until hATTR.size).forEach {
                Log.d(javaClass.simpleName,"glDisableVertexAttribArray:${hATTR[it]}")
                GLES20.glDisableVertexAttribArray(hATTR[it])
                MyGLES20Func.checkGlError("glDisableVertexAttribArray:${hATTR[it]}")
            }
        }
        if ( svhandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteShader:sv:$svhandle")
            GLES20.glDeleteShader(svhandle)
            MyGLES20Func.checkGlError("glDeleteShader:sv:$svhandle")
        }
        if ( sfhandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteShader:sf:$sfhandle")
            GLES20.glDeleteShader(sfhandle)
            MyGLES20Func.checkGlError("glDeleteShader:sf:$sfhandle")
        }
        if ( programHandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteProgram:$programHandle")
            GLES20.glDeleteProgram(programHandle)
            MyGLES20Func.checkGlError("glDeleteProgram:$programHandle")
        }
    }

    abstract fun loadShader(): ES20MgShader
}
