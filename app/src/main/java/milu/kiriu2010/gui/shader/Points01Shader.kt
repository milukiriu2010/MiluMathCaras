package milu.kiriu2010.gui.shader

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// ------------------------------------------
// GL_POINTSにて描画
// ------------------------------------------
// 2019.04.25
// ------------------------------------------
class Points01Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   float u_pointSize;
            varying   vec4  v_Color;

            void main() {
                v_Color      = a_Color;
                gl_Position  = u_matMVP * vec4(a_Position,1.0);
                gl_PointSize = u_pointSize;
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
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )

        return this
    }

    fun draw(modelAbs: MgModelAbs,
             u_matMVP: FloatArray,
             u_pointSize: Float) {
        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        modelAbs.bufPos.position(0)
        // get handle to vertex shader's vPosition member
        GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,modelAbs.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        // attribute(色)
        modelAbs.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, modelAbs.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }

        // uniform(描画する点の大きさ)
        GLES20.glGetUniformLocation(programHandle,"u_pointSize").also {
            GLES20.glUniform1f(it,u_pointSize)
        }

        // モデルを描画
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, modelAbs.datPos.size/3)
    }

}
