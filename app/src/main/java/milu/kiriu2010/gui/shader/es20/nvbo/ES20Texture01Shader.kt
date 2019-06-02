package milu.kiriu2010.gui.shader.es20.nvbo

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------
// シェーダ(テクスチャ)
// -------------------------------------
// 2019.05.22  リソース解放
// -------------------------------------
class ES20Texture01Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            attribute vec2 a_TextureCoord;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;
            varying   vec2 v_TextureCoord;

            void main() {
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;

            uniform  sampler2D  u_Texture;
            varying  vec4       v_Color;
            varying  vec2       v_TextureCoord;

            void main() {
                vec4 smpColor = texture2D(u_Texture, v_TextureCoord);
                gl_FragColor  = v_Color * smpColor;
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color","a_TextureCoord") )

        return this
    }

    fun draw(model: MgModelAbs,
             u_matMVP: FloatArray,
             u_Texture: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(色)
        model.bufCol.position(0)
        val hColor = GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        val hTextureCoord = GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle,"u_Texture").also {
            GLES20.glUniform1i(it,u_Texture)
        }
        MyGLES20Func.checkGlError2("u_Texture",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)


        // リソース解放
        GLES20.glDisableVertexAttribArray(hPosition)
        GLES20.glDisableVertexAttribArray(hColor)
        GLES20.glDisableVertexAttribArray(hTextureCoord)
    }
}
