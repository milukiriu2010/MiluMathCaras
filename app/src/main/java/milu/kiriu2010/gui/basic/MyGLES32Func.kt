package milu.kiriu2010.gui.basic

import android.graphics.Bitmap
import android.opengl.GLES32
import android.util.Log
import java.nio.ByteBuffer
import java.nio.IntBuffer

// --------------------------------------------------------------------------
// GLSL ES3.2用
// --------------------------------------------------------------------------
// 2019.06.17 GLSL ES3.0用をコピー
// --------------------------------------------------------------------------
// from MyGLES32Func
// 2019.04.27 ビットマップをロードしテクスチャを生成
// 2019.05.11 OpenGLのエラー状態を出力2
// 2019.05.18 テクスチャパラメータの設定をしないパラメータ追加
// 2019.05.19 フレームバッファを生成
// 2019.05.24 フレームバッファ生成の引数に"浮動小数点数テクスチャ"用を追加
// 2019.05.28 GLSL ES2.0用をコピー
// 2019.06.11 TAG変更
// 2019.06.19 MRT用のフレームバッファを生成
// 2019.06.20 プログラムオブジェクトの生成(TransformFeedback用)
// --------------------------------------------------------------------------
class MyGLES32Func {

    companion object {
        private const val TAG = "MyGLES32Func"

        // -------------------------------------
        // シェーダをロードする
        // -------------------------------------
        // type
        //   GLES32.GL_VERTEX_SHADER
        //   GLES32.GL_FRAGMENT_SHADER
        // -------------------------------------
        fun loadShader(type: Int, shaderCode: String): Int {
            return GLES32.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES32.glShaderSource(shader, shaderCode)
                GLES32.glCompileShader(shader)

                // コンパイル結果のチェック
                val compileStatus = IntArray(1)
                GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compileStatus,0)
                if ( compileStatus[0] == 0 ) {
                    // 何行目に誤りがあるか出力
                    Log.d(TAG,GLES32.glGetShaderInfoLog(shader))
                    // コンパイル失敗
                    GLES32.glDeleteShader(shader)
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
            val programHandle = GLES32.glCreateProgram().also {
                // 頂点シェーダをプログラムに追加
                GLES32.glAttachShader(it,svhandle)
                printShaderInfoLog(svhandle,"vertex shader")

                // フラグメントシェーダをプログラムに追加
                GLES32.glAttachShader(it,sfhandle)
                printShaderInfoLog(sfhandle,"fragment shader")

                // シェーダオブジェクトを削除
                GLES32.glDeleteShader(svhandle)
                GLES32.glDeleteShader(sfhandle)

                // attributeのindexを設定
                attrStrArray.forEachIndexed { id, attr ->
                    GLES32.glBindAttribLocation(it,id,attr)
                }

                // リンクする
                GLES32.glLinkProgram(it)

                // リンク結果のチェック
                val linkStatus = IntArray(1)
                GLES32.glGetProgramiv(it,GLES32.GL_LINK_STATUS,linkStatus,0)
                // リンク失敗
                if (linkStatus[0] == 0) {
                    printProgramInfoLog(it)
                    GLES32.glDeleteProgram(it)
                    throw RuntimeException("Error creating program.")
                }

                // シェーダプログラムを適用する
                GLES32.glUseProgram(it)
            }
            return programHandle
        }

        // ----------------------------------------------------------
        // プログラムオブジェクトの生成とリンク(TransformFeedback用)
        // ----------------------------------------------------------
        // svhandle
        //  頂点シェーダのハンドル
        // sfhandle
        //  フラグメントシェーダのハンドル
        // ----------------------------------------------------------
        fun createProgramTransformFeedback(svhandle: Int, sfhandle: Int, varyings: Array<String>): Int {
            val programHandle = GLES32.glCreateProgram().also {
                // 頂点シェーダをプログラムに追加
                GLES32.glAttachShader(it,svhandle)
                printShaderInfoLog(svhandle,"vertex shader")

                // フラグメントシェーダをプログラムに追加
                GLES32.glAttachShader(it,sfhandle)
                printShaderInfoLog(sfhandle,"fragment shader")

                // シェーダオブジェクトを削除
                //GLES32.glDeleteShader(svhandle)
                //GLES32.glDeleteShader(sfhandle)


                GLES32.glTransformFeedbackVaryings(it,varyings,GLES32.GL_SEPARATE_ATTRIBS)

                // リンクする
                GLES32.glLinkProgram(it)

                // リンク結果のチェック
                val linkStatus = IntArray(1)
                GLES32.glGetProgramiv(it,GLES32.GL_LINK_STATUS,linkStatus,0)
                // リンク失敗
                if (linkStatus[0] == 0) {
                    printProgramInfoLog(it)
                    GLES32.glDeleteProgram(it)
                    throw RuntimeException("Error creating program.")
                }

                // シェーダプログラムを適用する
                GLES32.glUseProgram(it)
            }
            return programHandle
        }

        // -------------------------------------
        // OpenGLのエラー状態を出力
        // -------------------------------------
        fun checkGlError(str: String, vararg anys: Any ) {
            var arg = anys.joinToString( separator = ":" ) { it.javaClass.simpleName }

            var error = GLES32.glGetError()
            while ( error != GLES32.GL_NO_ERROR ) {
                Log.d(TAG, "${arg}:${str}:${error}")
                error = GLES32.glGetError()
            }
        }

        // -------------------------------------
        // シェーダの情報を表示する
        // -------------------------------------
        fun printShaderInfoLog(shaderHandle: Int, tag: String) {
            Log.d(TAG,"=== shader compile[${tag}] ===================")
            // シェーダのコンパイル時のログの内容を取得する
            Log.d(TAG,GLES32.glGetShaderInfoLog(shaderHandle))
            /*
            val len = IntArray(1)
            GLES32.glGetProgramiv(shaderHandle,GLES32.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのコンパイル時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES32.glGetShaderInfoLog(shaderHandle))
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
            Log.d(TAG, GLES32.glGetProgramInfoLog(programHandle))
            /*
            val len = IntArray(1)
            GLES32.glGetProgramiv(programHandle,GLES32.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのリンク時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES32.glGetProgramInfoLog(programHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for program.")
            }
            */
        }

        // ----------------------------------------
        // ビットマップをロードしテクスチャを生成する
        // ----------------------------------------
        fun createTexture(id: Int, textures: IntArray, bmp: Bitmap, size: Int = -1, wrapParam: Int = GLES32.GL_REPEAT) {

            // テクスチャをバインドする
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textures[id])
            checkGlError("glBindTexture")

            if ( size > 0 ) {
                val resizedBmp = Bitmap.createScaledBitmap(bmp,size,size,false)

                // ビットマップ⇒バッファへ変換
                val buffer = ByteBuffer.allocate(resizedBmp.byteCount)
                resizedBmp.copyPixelsToBuffer(buffer)
                buffer.rewind()

                // テクスチャへイメージを適用
                GLES32.glTexImage2D(GLES32.GL_TEXTURE_2D,0,GLES32.GL_RGBA,resizedBmp.width,resizedBmp.height,0,
                        GLES32.GL_RGBA,GLES32.GL_UNSIGNED_BYTE,buffer)
            }
            else {
                // ビットマップ⇒バッファへ変換
                val buffer = ByteBuffer.allocate(bmp.byteCount)
                bmp.copyPixelsToBuffer(buffer)
                buffer.rewind()

                // テクスチャへイメージを適用
                GLES32.glTexImage2D(GLES32.GL_TEXTURE_2D,0,GLES32.GL_RGBA,bmp.width,bmp.height,0,
                        GLES32.GL_RGBA,GLES32.GL_UNSIGNED_BYTE,buffer)
            }

            /*
            // GLES32.glTexImage2Dを使わないやり方
            // ビットマップをテクスチャに設定
            GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bmp, 0)
            MyGLES32Func.checkGlError("texImage2D")
            */

            // ミップマップを生成
            GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D)

            // テクスチャパラメータを設定
            if ( wrapParam != -1 ) {
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR)
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR)
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, wrapParam)
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, wrapParam)
            }

            // テクスチャのバインドを無効化
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0)

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
        fun createFrameBuffer(width: Int, height: Int, id: Int, bufFrame: IntBuffer, bufDepthRender: IntBuffer, frameTexture: IntBuffer, type: Int = GLES32.GL_UNSIGNED_BYTE) {
            // フレームバッファのバインド
            GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,bufFrame[id])

            // 深度バッファ用レンダ―バッファのバインド
            GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER,bufDepthRender[id])

            // レンダ―バッファを深度バッファとして設定
            GLES32.glRenderbufferStorage(GLES32.GL_RENDERBUFFER, GLES32.GL_DEPTH_COMPONENT16, width, height)

            // フレームバッファにレンダ―バッファを関連付ける
            GLES32.glFramebufferRenderbuffer(GLES32.GL_FRAMEBUFFER, GLES32.GL_DEPTH_ATTACHMENT, GLES32.GL_RENDERBUFFER,bufDepthRender[id])

            // フレームバッファ用のテクスチャをバインド
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,frameTexture[id])

            // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
            //   type:
            //     浮動小数点数テクスチャ⇒GLES32.GL_FLOATを指定
            GLES32.glTexImage2D(GLES32.GL_TEXTURE_2D,0,GLES32.GL_RGBA,width,height,0,
                    GLES32.GL_RGBA,type,null)

            // テクスチャパラメータ
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MAG_FILTER,GLES32.GL_LINEAR)
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MIN_FILTER,GLES32.GL_LINEAR)
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_WRAP_S,GLES32.GL_CLAMP_TO_EDGE)
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_WRAP_T,GLES32.GL_CLAMP_TO_EDGE)

            // フレームバッファにテクスチャを関連付ける
            GLES32.glFramebufferTexture2D(GLES32.GL_FRAMEBUFFER, GLES32.GL_COLOR_ATTACHMENT0,GLES32.GL_TEXTURE_2D,frameTexture[id],0)

            // 各種オブジェクトのバインドを解除
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,0)
            GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER,0)
            GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,0)
        }


        // --------------------------------------------------
        // MRT用フレームバッファを生成する
        // --------------------------------------------------
        fun createFrameBuffer4MRT(width: Int, height: Int, cnt: Int, id: Int, bufFrame: IntBuffer, bufDepthRender: IntBuffer, frameTexture: IntBuffer) {
            // フレームバッファのバインド
            GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,bufFrame[id])

            (0 until cnt).forEach { i ->
                // フレームバッファ用のテクスチャをバインド
                GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,frameTexture[i])

                // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
                GLES32.glTexImage2D(GLES32.GL_TEXTURE_2D,0,GLES32.GL_RGBA,width,height,0,
                    GLES32.GL_RGBA,GLES32.GL_UNSIGNED_BYTE,null)

                // テクスチャパラメータ
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MAG_FILTER,GLES32.GL_LINEAR)
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MIN_FILTER,GLES32.GL_LINEAR)
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_WRAP_S,GLES32.GL_CLAMP_TO_EDGE)
                GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_WRAP_T,GLES32.GL_CLAMP_TO_EDGE)

                // フレームバッファにテクスチャを関連付ける
                GLES32.glFramebufferTexture2D(GLES32.GL_FRAMEBUFFER, GLES32.GL_COLOR_ATTACHMENT0+i,GLES32.GL_TEXTURE_2D,frameTexture[i],0)
            }

            // 深度バッファ用レンダ―バッファのバインド
            GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER,bufDepthRender[id])
            // レンダ―バッファを深度バッファとして設定
            GLES32.glRenderbufferStorage(GLES32.GL_RENDERBUFFER, GLES32.GL_DEPTH_COMPONENT16, width, height)
            // フレームバッファにレンダ―バッファを関連付ける
            GLES32.glFramebufferRenderbuffer(GLES32.GL_FRAMEBUFFER, GLES32.GL_DEPTH_ATTACHMENT, GLES32.GL_RENDERBUFFER,bufDepthRender[id])

            // 各種オブジェクトのバインドを解除
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,0)
            GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER,0)
            GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,0)
        }
    }


}
