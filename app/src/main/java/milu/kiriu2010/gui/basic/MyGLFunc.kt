package milu.kiriu2010.gui.basic

import android.opengl.GLES20
import android.util.Log

class MyGLFunc {

    companion object {
        private const val TAG = "MyGLFunc"

        // -------------------------------------
        // シェーダをロードする
        // -------------------------------------
        // type
        //   GLES20.GL_VERTEX_SHADER
        //   GLES20.GL_FRAGMENT_SHADER
        // -------------------------------------
        fun loadShader(type: Int, shaderCode: String): Int {
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)

                // コンパイル結果のチェック
                val compileStatus = IntArray(1)
                GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus,0)
                if ( compileStatus[0] == 0 ) {
                    // コンパイル失敗
                    GLES20.glDeleteShader(shader)
                    throw RuntimeException("Compile Error:"+shaderCode)
                }
            }
        }

        // -------------------------------------
        // プログラムオブジェクトの生成とリンク
        // -------------------------------------
        // svhandle
        //  頂点シェーダのハンドル
        // sfhandle
        //  フラグメントシェーダのハンドル
        fun createProgram(svhandle: Int, sfhandle: Int, attrStrArray: Array<String>): Int {
            val programHandle = GLES20.glCreateProgram().also {
                // 頂点シェーダをプログラムに追加
                GLES20.glAttachShader(it,svhandle)
                MyGLFunc.printShaderInfoLog(svhandle,"vertex shader")

                // フラグメントシェーダをプログラムに追加
                GLES20.glAttachShader(it,sfhandle)
                MyGLFunc.printShaderInfoLog(sfhandle,"fragment shader")

                // シェーダオブジェクトを削除
                GLES20.glDeleteShader(svhandle)
                GLES20.glDeleteShader(sfhandle)

                // attributeのindexを設定
                attrStrArray.forEachIndexed { id, attr ->
                    GLES20.glBindAttribLocation(it,id,attr)
                }

                // リンクする
                GLES20.glLinkProgram(it)

                // リンク結果のチェック
                val linkStatus = IntArray(1)
                GLES20.glGetProgramiv(it,GLES20.GL_LINK_STATUS,linkStatus,0)
                // リンク失敗
                if (linkStatus[0] == 0) {
                    MyGLFunc.printProgramInfoLog(it)
                    GLES20.glDeleteProgram(it)
                    throw RuntimeException("Error creating program.")
                }

                // シェーダプログラムを適用する
                GLES20.glUseProgram(it)
            }
            return programHandle
        }

        // -------------------------------------
        // OpenGLのエラー状態を出力
        // -------------------------------------
        fun checkGlError( str: String ) {
            var error = GLES20.glGetError()
            while ( error != GLES20.GL_NO_ERROR ) {
                Log.d(TAG, "${str}:${error}")
                error = GLES20.glGetError()
            }
        }

        // -------------------------------------
        // シェーダの情報を表示する
        // -------------------------------------
        fun printShaderInfoLog(shaderHandle: Int, tag: String) {
            Log.d(TAG,"=== shader compile[${tag}] ===================")
            // シェーダのコンパイル時のログの内容を取得する
            Log.d(TAG,GLES20.glGetShaderInfoLog(shaderHandle))
            /*
            val len = IntArray(1)
            GLES20.glGetProgramiv(shaderHandle,GLES20.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのコンパイル時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES20.glGetShaderInfoLog(shaderHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for shader.")
            }
            */
        }

        // -------------------------------------
        // プログラムの情報を表示する
        // -------------------------------------
        fun printProgramInfoLog(programHandle: Int) {
            Log.d(TAG,"=== shader link ============================")
            // シェーダのリンク時のログの内容を取得する
            Log.d(TAG, GLES20.glGetProgramInfoLog(programHandle))
            /*
            val len = IntArray(1)
            GLES20.glGetProgramiv(programHandle,GLES20.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのリンク時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES20.glGetProgramInfoLog(programHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for program.")
            }
            */
        }

    }
}
