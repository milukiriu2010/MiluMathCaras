package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.vbo.es32.ES32VAOAbs

// ------------------------------------------
// GL_POINTSにて描画
// ------------------------------------------
// 2019.06.03  VBO
// ------------------------------------------
class ES32Points01Shader(ctx: Context): ES32MgShader(ctx) {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            layout (location = 0) in vec3 a_Position;
            layout (location = 1) in vec4  a_Color;
            
            uniform   mat4  u_matMVP;
            uniform   float u_pointSize;
            
            out  vec4  v_Color;

            void main() {
                v_Color      = a_Color;
                gl_Position  = u_matMVP * vec4(a_Position,1.0);
                gl_PointSize = u_pointSize;
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
        hUNI = IntArray(2)
        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(描画する点の大きさ)
        hUNI[1] = GLES32.glGetUniformLocation(programHandle,"u_pointSize")
        MyGLES32Func.checkGlError("u_pointSize:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES32VAOAbs,
             u_matMVP: FloatArray,
             u_pointSize: Float) {
        val model = vao.model

        GLES32.glUseProgram(programHandle)
        MyGLES32Func.checkGlError("UseProgram",this,model)

        // VAOをバインド
        GLES32.glBindVertexArray(vao.hVAO[0])
        MyGLES32Func.checkGlError("BindVertexArray",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES32.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES32Func.checkGlError("u_matMVP",this,model)

        // uniform(描画する点の大きさ)
        GLES32.glUniform1f(hUNI[1],u_pointSize)
        MyGLES32Func.checkGlError("u_pointSize",this,model)

        // モデルを描画
        GLES32.glDrawArrays(GLES32.GL_POINTS, 0, model.datPos.size/3)

        // VAO解放
        GLES32.glBindVertexArray(0)
    }
}
