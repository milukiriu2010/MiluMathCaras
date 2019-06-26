package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.vbo.es32.ES32VAOAbs

// ---------------------------------------------------------------
// フォンシェーディング
// ---------------------------------------------------------------
// グーローシェーディング
//   レンダリングされるポリゴンの色は頂点間で補間される
//   よって頂点の数が少ないと美しいライティングを行うことが難しい
//   頂点ごとに色を補間するため、色が変化する協会にジャギーが発生
// フォンシェーディング
//   各ピクセルごとに色の補間が行われる
//   少ない頂点数のモデルをレンダリングする際のライティングでも
//   自然な照明効果を得られる。
//   ピクセルごとに色の補間が行われることで、
//   不自然なジャギーが発生しなくなる
// ---------------------------------------------------------------
// 2019.06.01
// ---------------------------------------------------------------
class ES32PhongShading01Shader(ctx: Context): ES32MgShader(ctx) {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            layout (location = 0) in vec3 a_Position;
            layout (location = 1) in vec3 a_Normal;
            layout (location = 2) in vec4 a_Color;
            
            uniform   mat4 u_matMVP;
            
            out  vec3 v_Normal;
            out  vec4 v_Color;

            void main() {
                v_Normal    = a_Normal;
                v_Color     = a_Color;
                gl_Position = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es
            precision highp float;

            uniform   mat4 u_matINV;
            uniform   vec3 u_vecLight;
            uniform   vec3 u_vecEye;
            uniform   vec4 u_ambientColor;
            
            in   vec3 v_Normal;
            in   vec4 v_Color;
            
            out  vec4 o_FragColor;

            void main() {
                vec3  invLight  = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                vec3  invEye    = normalize(u_matINV * vec4(u_vecEye  ,0.0)).xyz;
                vec3  halfLE    = normalize(invLight + invEye);
                float diffuse   = clamp(dot(v_Normal,invLight), 0.0, 1.0);
                float specular  = pow(clamp(dot(v_Normal, halfLE), 0.0, 1.0), 50.0);
                vec4  destColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                destColor       = destColor + u_ambientColor;
                o_FragColor     = destColor;
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
        hUNI = IntArray(5)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[1] = GLES32.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES32Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(平行光源)
        hUNI[2] = GLES32.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES32Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[3] = GLES32.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES32Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform(環境光)
        hUNI[4] = GLES32.glGetUniformLocation(programHandle,"u_ambientColor")
        MyGLES32Func.checkGlError("u_ambientColor:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES32VAOAbs,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
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

        // uniform(視点座標)
        GLES32.glUniform3fv(hUNI[3],1,u_vecEye,0)
        MyGLES32Func.checkGlError("u_vecEye",this,model)

        // uniform(環境光)
        GLES32.glUniform4fv(hUNI[4],1,u_ambientColor,0)
        MyGLES32Func.checkGlError("u_ambientColor",this,model)

        // モデルを描画
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, 0)

        // VAO解放
        GLES32.glBindVertexArray(0)
    }
}
