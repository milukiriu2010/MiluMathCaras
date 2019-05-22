package milu.kiriu2010.gui.shader

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// --------------------------------------
// 平行光源
// --------------------------------------
// 2019.05.13 コメント追加
// 2019.05.22 リソース解放
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
        svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             matI: FloatArray,
             vecLight: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        val hNormal = GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Normal",this,model)

        // attribute(色)
        model.bufCol.position(0)
        val hColor = GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }
        MyGLFunc.checkGlError2("u_matINV",this,model)

        // uniform(平行光源)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,vecLight,0)
        }
        MyGLFunc.checkGlError2("u_vecLight",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glDisableVertexAttribArray(hPosition)
        GLES20.glDisableVertexAttribArray(hNormal)
        GLES20.glDisableVertexAttribArray(hColor)
    }
}
