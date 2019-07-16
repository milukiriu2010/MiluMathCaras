package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.vbo.es32.ES32VAOAbs
import milu.kiriu2010.gui.vbo.es32.ES32VBOAbs
import java.nio.FloatBuffer

// ------------------------------------------
// 特殊効果なし
// ------------------------------------------
// ES3.2用
// ------------------------------------------
// 2019.04.27 コメントアウト
// 2019.05.22 リソース解放
// 2019.06.17 ES3.0用をコピー
// 2019.07.04 動的位置変更
// 2019.07.16 動的色変更
// ------------------------------------------
class ES32Simple01Shader(ctx: Context): ES32MgShader(ctx) {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            layout (location = 0) in vec3 a_Position;
            layout (location = 1) in vec4 a_Color;

            uniform  mat4 u_matMVP;

            out vec4 v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es
            precision highp float;

            in  vec4 v_Color;

            out vec4 o_FragColor;

            void main() {
                o_FragColor = v_Color;
            }
            """.trimIndent()

    override fun loadShader(): ES32MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES32Func.loadShader(GLES32.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES32Func.loadShader(GLES32.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES32Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(1)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        return this
    }
    
    fun loadShaderDynamic(): ES32MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES32Func.loadShader(GLES32.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES32Func.loadShader(GLES32.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES32Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(2)
        // 属性(頂点)
        hATTR[0] = GLES32.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES32.glEnableVertexAttribArray(it)
            MyGLES32Func.checkGlError("a_Position:glEnableVertexAttribArray:${it}")
            // attribute属性を登録
            GLES32.glVertexAttribPointer(it,3,GLES32.GL_FLOAT,false,0,0)
            MyGLES32Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES32Func.checkGlError("a_Position:glGetAttribLocation")

        // 属性(色)
        hATTR[1] = GLES32.glGetAttribLocation(programHandle, "a_Color").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES32.glEnableVertexAttribArray(it)
            MyGLES32Func.checkGlError("a_Color:glEnableVertexAttribArray:${it}")
            // attribute属性を登録
            GLES32.glVertexAttribPointer(it,4,GLES32.GL_FLOAT,false,0,0)
            MyGLES32Func.checkGlError("a_Color:glVertexAttribPointer")
        }
        MyGLES32Func.checkGlError("a_Color:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(1)
        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES32VAOAbs,
             u_matMVP: FloatArray,
             mode: Int = GLES32.GL_TRIANGLES) {
        val model = vao.model

        GLES32.glUseProgram(programHandle)
        MyGLES32Func.checkGlError("UseProgram",this,model)

        // VAOをバインド
        GLES32.glBindVertexArray(vao.hVAO[0])
        MyGLES32Func.checkGlError("BindVertexArray",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES32.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES32Func.checkGlError("u_matMVP",this,model)

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES32.GL_TRIANGLES -> {
                GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, 0)
                MyGLES32Func.checkGlError("glDrawElements",this,model)
            }
            // 線を描画
            GLES32.GL_LINES -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINES,0,cnt)
                MyGLES32Func.checkGlError("glDrawArrays:GL_LINES",this,model)
            }
            // 線を描画
            GLES32.GL_LINE_STRIP -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINE_STRIP,0,cnt)
                MyGLES32Func.checkGlError("glDrawArrays:GL_LINE_STRIP",this,model)
            }
            // 線を描画
            GLES32.GL_LINE_LOOP -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINE_LOOP,0,cnt)
                MyGLES32Func.checkGlError("glDrawArrays:GL_LINE_LOOP",this,model)
            }
        }

        // リソース解放
        // ここで呼ぶと描画されない
        //GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0)
        //GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0)
        //GLES32.glDisableVertexAttribArray(hATTR[0])

        // VAO解放
        GLES32.glBindVertexArray(0)
    }

    fun drawDynamicPos(vbo: ES32VBOAbs,
                       u_matMVP: FloatArray,
                       mode: Int,
                       chgBuf: () -> FloatBuffer ) {
        val model = vbo.model

        GLES32.glUseProgram(programHandle)
        MyGLES32Func.checkGlError("UseProgram",this,model)

        // attribute(位置)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo.hVBO[0])
        GLES32.glVertexAttribPointer(hATTR[0],3,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Position",this,model)
        val buf = chgBuf()
        GLES32.glBufferSubData(GLES32.GL_ARRAY_BUFFER,0,buf.capacity()*4,buf)
        MyGLES32Func.checkGlError("a_Position:glBufferSubData",this,model)

        // attribute(色)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo.hVBO[1])
        GLES32.glVertexAttribPointer(hATTR[1],4,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES32.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES32Func.checkGlError("u_matMVP",this,model)

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES32.GL_TRIANGLES -> {
                GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, model.bufIdx)
            }
            // 線を描画
            GLES32.GL_LINES -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINES,0,cnt)
            }
            // 線を描画
            GLES32.GL_LINE_STRIP -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINE_STRIP,0,cnt)
            }
            // 線を描画
            GLES32.GL_LINE_LOOP -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINE_LOOP,0,cnt)
            }
        }

        // リソース解放
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0)
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0)
        // ここで呼ぶと描画されない
        //GLES32.glDisableVertexAttribArray(hATTR[0])
    }

    fun drawDynamicCol(vbo: ES32VBOAbs,
                       u_matMVP: FloatArray,
                       mode: Int,
                       chgBuf: () -> FloatBuffer ) {
        val model = vbo.model

        GLES32.glUseProgram(programHandle)
        MyGLES32Func.checkGlError("UseProgram",this,model)

        // attribute(位置)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo.hVBO[0])
        GLES32.glVertexAttribPointer(hATTR[0],3,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Position",this,model)

        // attribute(色)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo.hVBO[1])
        GLES32.glVertexAttribPointer(hATTR[1],4,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Color",this,model)
        val buf = chgBuf()
        GLES32.glBufferSubData(GLES32.GL_ARRAY_BUFFER,0,buf.capacity()*4,buf)
        MyGLES32Func.checkGlError("a_Color:glBufferSubData",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES32.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES32Func.checkGlError("u_matMVP",this,model)

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES32.GL_TRIANGLES -> {
                GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, model.bufIdx)
            }
            // 線を描画
            GLES32.GL_LINES -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINES,0,cnt)
            }
            // 線を描画
            GLES32.GL_LINE_STRIP -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINE_STRIP,0,cnt)
            }
            // 線を描画
            GLES32.GL_LINE_LOOP -> {
                val cnt = model.datPos.size/3
                GLES32.glDrawArrays(GLES32.GL_LINE_LOOP,0,cnt)
            }
        }

        // リソース解放
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0)
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0)
        // ここで呼ぶと描画されない
        //GLES32.glDisableVertexAttribArray(hATTR[0])
    }
}
