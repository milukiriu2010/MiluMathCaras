package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.vbo.es32.ES32VAOAbs

// ----------------------------------------
// 環境光
// ----------------------------------------
// 2019.06.01
// ----------------------------------------
class ES32AmbientLight01Shader(ctx: Context): ES32MgShader(ctx) {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            layout (location = 0) in vec3 a_Position;
            layout (location = 1) in vec3 a_Normal;
            layout (location = 2) in vec4 a_Color;
            
            uniform   mat4 u_matMVP;
            uniform   mat4 u_matINV;
            uniform   vec3 u_vecLight;
            uniform   vec4 u_ambientColor;
            
            out vec4 v_Color;

            // 環境光
            // 現実世界における自然光の乱反射をシミュレートする
            void main() {
                vec3  invLight = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                float diffuse  = clamp(dot(a_Normal,invLight), 0.1, 1.0);
                // 平行光源(頂点の色成分に拡散光の成分を掛ける)による色成分、環境光の成分を足す
                v_Color        = a_Color * vec4(vec3(diffuse), 1.0) + u_ambientColor;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es
            precision highp float;

            in   vec4 v_Color;
            
            out  vec4 o_FragColor;

            void main() {
                o_FragColor   = v_Color;
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
        hUNI = IntArray(4)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[1] = GLES32.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES32Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(平行光源)
        hUNI[2] = GLES32.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES32Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(環境光)
        hUNI[3] = GLES32.glGetUniformLocation(programHandle,"u_ambientColor")
        MyGLES32Func.checkGlError("u_ambientColor:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES32VAOAbs,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_ambientColor: FloatArray) {
        val model = vao.model

        GLES32.glUseProgram(programHandle)
        MyGLES32Func.checkGlError("UseProgram",this,model)

        // VAOをバインド
        GLES32.glBindVertexArray(vao.hVAO[0])
        MyGLES32Func.checkGlError("BindVertexArray",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES32.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES32Func.checkGlError("u_matMVP",this,model)

        // uniform(逆行列)
        GLES32.glUniformMatrix4fv(hUNI[1],1,false,u_matI,0)
        MyGLES32Func.checkGlError("u_matINV",this,model)

        // uniform(平行光源)
        GLES32.glUniform3fv(hUNI[2],1,u_vecLight,0)
        MyGLES32Func.checkGlError("u_vecLight",this,model)

        // uniform(環境光)
        GLES32.glUniform4fv(hUNI[3],1,u_ambientColor,0)
        MyGLES32Func.checkGlError("u_ambientColor",this,model)

        // モデルを描画
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, 0)

        // VAO解放
        GLES32.glBindVertexArray(0)
    }
}
