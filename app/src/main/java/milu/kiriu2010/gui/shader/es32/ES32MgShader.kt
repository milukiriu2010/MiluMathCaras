package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLES32Func

// --------------------------------------------
// GLSL ES32用シェーダ
// --------------------------------------------
// 2019.05.28
// 2019.06.07 attribute/uniformハンドル追加
// 2019.06.17 UBIハンドル追加
// 2019.06.17 ES30用よりコピー
// 2019.06.26 フラグメントシェーダのリソースID
// --------------------------------------------
abstract class ES32MgShader(val ctx: Context) {
    // 頂点シェーダのハンドル
    var svhandle: Int = -1
    // フラグメントシェーダのハンドル
    var sfhandle: Int = -1
    // フラグメントシェーダのリソースID
    var sfResId: Int = -1

    // シェーダが登録されたプログラムオブジェクトのハンドル
    protected var programHandle = -1

    // attributeのハンドル
    lateinit var hATTR: IntArray
    // uniformのハンドル
    lateinit var hUNI: IntArray
    // UBIのハンドル(uniform block index)
    lateinit var hUBI: IntArray

    // シェーダを削除
    fun deleteShader() {
        if ( this::hATTR.isInitialized ) {
            (0 until hATTR.size).forEach {
                Log.d(javaClass.simpleName,"glDisableVertexAttribArray:${hATTR[it]}")
                GLES32.glDisableVertexAttribArray(hATTR[it])
                MyGLES32Func.checkGlError("glDisableVertexAttribArray:${hATTR[it]}")
            }
        }
        if ( svhandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteShader:sv:$svhandle")
            GLES32.glDeleteShader(svhandle)
            MyGLES32Func.checkGlError("glDeleteShader:sv:$svhandle")
        }
        if ( sfhandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteShader:sf:$sfhandle")
            GLES32.glDeleteShader(sfhandle)
            MyGLES32Func.checkGlError("glDeleteShader:sf:$sfhandle")
        }
        if ( programHandle != -1 ) {
            Log.d(javaClass.simpleName,"glDeleteProgram:$programHandle")
            GLES32.glDeleteProgram(programHandle)
            MyGLES32Func.checkGlError("glDeleteProgram:$programHandle")
        }
    }

    abstract fun loadShader(): ES32MgShader
}
