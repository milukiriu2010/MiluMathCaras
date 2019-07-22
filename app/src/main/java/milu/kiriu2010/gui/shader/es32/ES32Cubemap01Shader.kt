package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.vbo.es32.ES32VAOAbs

// ----------------------------------------
// キューブ環境マッピング01
// ----------------------------------------
// 2019.07.22
// ----------------------------------------
class ES32Cubemap01Shader(ctx: Context): ES32MgShader(ctx) {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            layout (location = 0) in vec3 a_Position;
            layout (location = 1) in vec3 a_Normal;
            layout (location = 2) in vec4 a_Color;
            
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            
            out  vec3  v_Position;
            out  vec3  v_Normal;
            out  vec4  v_Color;

            // モデル座標変換後にスケーリングを含んでいる場合、
            // このやり方では問題が発生するらしい。
            void main() {
                v_Position    = (u_matM  * vec4(a_Position,1.0)).xyz;
                v_Normal      = (u_matM  * vec4(a_Normal  ,0.0)).xyz;
                v_Color       = a_Color;
                gl_Position   = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es
            precision highp float;

            uniform   vec3        u_vecEye;
            uniform   samplerCube u_CubeTexture;
            uniform   int         u_Reflection;
            
            in  vec3  v_Position;
            in  vec3  v_Normal;
            in  vec4  v_Color;
            
            out vec4  o_FragColor;

            void main() {
                vec3  ref;
                if (bool(u_Reflection)) {
                    // 頂点シェーダから送られてきた頂点のモデル座標変換後の座標位置から
                    // カメラの座標位置を減算することで視線ベクトルを算出
                    // ----------------------------------------------------------
                    // 視線ベクトルと頂点シェーダから送られてきた法線を使って
                    // 視線ベクトルの反射ベクトルを導き出す
                    ref = reflect(v_Position - u_vecEye, v_Normal);
                }
                else {
                    // 背景をレンダリングする
                    ref = v_Normal;
                }
                // キューブマップテクスチャからフラグメントの情報を抜き出す
                // u_Reflection=1 ⇒ 反射に対応する色
                // u_Reflection=0 ⇒ 背景の色
                vec4  envColor  = texture(u_CubeTexture, ref);
                vec4  destColor = v_Color * envColor;
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

        // uniform(モデル)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matM")
        MyGLES32Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[1] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[2] = GLES32.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES32Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform(キューブテクスチャユニット)
        hUNI[3] = GLES32.glGetUniformLocation(programHandle, "u_CubeTexture")
        MyGLES32Func.checkGlError("u_CubeTexture:glGetUniformLocation")

        // uniform(反射するかどうか)
        hUNI[4] = GLES32.glGetUniformLocation(programHandle, "u_Reflection")
        MyGLES32Func.checkGlError("u_Reflection:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES32VAOAbs,
             u_matM: FloatArray,
             u_matMVP: FloatArray,
             u_vecEye: FloatArray,
             u_CubeTexture: Int,
             u_Reflection: Int) {
        val model = vao.model

        GLES32.glUseProgram(programHandle)
        MyGLES32Func.checkGlError("UseProgram",this,model)

        // VAOをバインド
        GLES32.glBindVertexArray(vao.hVAO[0])
        MyGLES32Func.checkGlError("BindVertexArray",this,model)

        // uniform(モデル)
        GLES32.glUniformMatrix4fv(hUNI[0],1,false,u_matM,0)
        MyGLES32Func.checkGlError("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES32.glUniformMatrix4fv(hUNI[1],1,false,u_matMVP,0)
        MyGLES32Func.checkGlError("u_matMVP",this,model)

        // uniform(視点座標)
        GLES32.glUniform3fv(hUNI[2],1,u_vecEye,0)
        MyGLES32Func.checkGlError("u_vecEye",this,model)

        if ( u_CubeTexture != -1 ) {
            // uniform(キューブテクスチャ)
            GLES32.glUniform1i(hUNI[3], u_CubeTexture)
            MyGLES32Func.checkGlError("u_CubeTexture",this,model)
        }

        // uniform(反射するかどうか)
        GLES32.glUniform1i(hUNI[4],u_Reflection)
        MyGLES32Func.checkGlError("u_Reflection",this,model)

        // モデルを描画
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, 0)

        // VAO解放
        GLES32.glBindVertexArray(0)
    }
}
