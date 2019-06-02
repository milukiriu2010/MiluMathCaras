package milu.kiriu2010.gui.basic

import android.graphics.Bitmap
import android.opengl.GLES20
import android.util.Log
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import java.nio.ByteBuffer
import java.nio.IntBuffer

// ----------------------------------------------------------------------
// GLSL ES2.0用
// ----------------------------------------------------------------------
// 2019.04.27 ビットマップをロードしテクスチャを生成
// 2019.05.11 OpenGLのエラー状態を出力2
// 2019.05.18 テクスチャパラメータの設定をしないパラメータ追加
// 2019.05.19 フレームバッファを生成
// 2019.05.24 フレームバッファ生成の引数に"浮動小数点数テクスチャ"用を追加
// 2019.06.02 createProgramの属性引数デフォルト化
// ----------------------------------------------------------------------
class MyGLES20Func {

    companion object {
        private const val TAG = "MyGLES20Func"

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
                    // 何行目に誤りがあるか出力
                    Log.d(TAG,GLES20.glGetShaderInfoLog(shader))
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
        fun createProgram(svhandle: Int, sfhandle: Int, attrStrArray: Array<String> = arrayOf()): Int {
            val programHandle = GLES20.glCreateProgram().also {
                // 頂点シェーダをプログラムに追加
                GLES20.glAttachShader(it,svhandle)
                MyGLES20Func.printShaderInfoLog(svhandle,"vertex shader")

                // フラグメントシェーダをプログラムに追加
                GLES20.glAttachShader(it,sfhandle)
                MyGLES20Func.printShaderInfoLog(sfhandle,"fragment shader")

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
                    MyGLES20Func.printProgramInfoLog(it)
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
        // OpenGLのエラー状態を出力2
        // -------------------------------------
        fun checkGlError2(str: String, shader: ES20MgShader, model: MgModelAbs ) {
            var error = GLES20.glGetError()
            while ( error != GLES20.GL_NO_ERROR ) {
                Log.d(TAG, "${shader.javaClass.simpleName}:${str}:${model.javaClass.simpleName}:${error}")
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

        // ----------------------------------------
        // ビットマップをロードしテクスチャを生成する
        // ----------------------------------------
        fun createTexture(id: Int, textures: IntArray, bmp: Bitmap, size: Int = -1, wrapParam: Int = GLES20.GL_REPEAT) {

            // テクスチャをバインドする
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[id])
            MyGLES20Func.checkGlError("glBindTexture")

            if ( size > 0 ) {
                val resizedBmp = Bitmap.createScaledBitmap(bmp,size,size,false)

                // ビットマップ⇒バッファへ変換
                val buffer = ByteBuffer.allocate(resizedBmp.byteCount)
                resizedBmp.copyPixelsToBuffer(buffer)
                buffer.rewind()

                // テクスチャへイメージを適用
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,resizedBmp.width,resizedBmp.height,0,
                    GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,buffer)
            }
            else {
                // ビットマップ⇒バッファへ変換
                val buffer = ByteBuffer.allocate(bmp.byteCount)
                bmp.copyPixelsToBuffer(buffer)
                buffer.rewind()

                // テクスチャへイメージを適用
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,bmp.width,bmp.height,0,
                    GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,buffer)
            }

            /*
            // GLES20.glTexImage2Dを使わないやり方
            // ビットマップをテクスチャに設定
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
            MyGLES20Func.checkGlError("texImage2D")
            */

            // ミップマップを生成
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

            // テクスチャパラメータを設定
            if ( wrapParam != -1 ) {
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrapParam)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrapParam)
            }

            // テクスチャのバインドを無効化
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

            if ( bmp.isRecycled == false ) {
                bmp.recycle()
            }

            if (textures[id] == 0) {
                throw java.lang.RuntimeException("Error loading texture[${id}]")
            }
        }

        // --------------------------------------------------
        // フレームバッファを生成する
        // --------------------------------------------------
        fun createFrameBuffer(width: Int, height: Int, id: Int, bufFrame: IntBuffer, bufDepthRender: IntBuffer, frameTexture: IntBuffer, type: Int = GLES20.GL_UNSIGNED_BYTE) {
            // フレームバッファのバインド
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[id])

            // 深度バッファ用レンダ―バッファのバインド
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,bufDepthRender[id])

            // レンダ―バッファを深度バッファとして設定
            GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height)

            // フレームバッファにレンダ―バッファを関連付ける
            GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER,bufDepthRender[id])

            // フレームバッファ用のテクスチャをバインド
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[id])

            // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
            //   type:
            //     浮動小数点数テクスチャ⇒GLES20.GL_FLOATを指定
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,
                GLES20.GL_RGBA,type,null)

            // テクスチャパラメータ
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)

            // フレームバッファにテクスチャを関連付ける
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,frameTexture[id],0)

            // 各種オブジェクトのバインドを解除
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,0)
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
        }
    }
}
