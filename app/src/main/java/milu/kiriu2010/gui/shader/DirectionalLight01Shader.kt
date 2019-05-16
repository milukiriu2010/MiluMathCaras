package milu.kiriu2010.gui.shader

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// --------------------------------------
// 平行光源
// --------------------------------------
// 2019.05.13 コメント追加
// --------------------------------------
class DirectionalLight01Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
        """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            // モデル座標変換行列の逆行列
            uniform   mat4 u_matINV;
            // 光の向きを表すベクトル
            uniform   vec3 u_vecLight;
            varying   vec4 v_Color;

            void main() {
                // ---------------------------------------------------------
                // 光の向きベクトルにモデル座標変換行列の逆行列を掛ける
                // ---------------------------------------------------------
                // モデル座標変換行列は外部プログラムで座標変換されるのに対し、
                // 光の向きは一定でなければならないので、こうしている
                // ---------------------------------------------------------
                vec3  invLight = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                // ---------------------------------------------------------
                // 光の拡散の強さをライトベクトルと法線ベクトルの内積で表す
                // ---------------------------------------------------------
                // ライトベクトルと法線ベクトルによって形成される角度が
                // 90度以上の場合は、光の影響力がなくなる。
                // これを内積で実装している。
                // ---------------------------------------------------------
                float diffuse  = clamp(dot(a_Normal,invLight), 0.1, 1.0);
                // 頂点の色成分に拡散光の成分を掛ける
                v_Color        = a_Color * vec4(vec3(diffuse), 1.0);
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
        """
            precision mediump float;

            varying   vec4 v_Color;

            void main() {
                gl_FragColor   = v_Color;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(modelAbs: MgModelAbs,
             matMVP: FloatArray,
             matI: FloatArray,
             vecLight: FloatArray) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        modelAbs.bufPos.position(0)
        // get handle to vertex shader's vPosition member
        GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,modelAbs.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        // attribute(法線)
        modelAbs.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, modelAbs.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal")

        // attribute(色)
        modelAbs.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, modelAbs.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")


        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        +        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }
        // uniform(平行光源)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,vecLight,0)
        }

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
    }
}
