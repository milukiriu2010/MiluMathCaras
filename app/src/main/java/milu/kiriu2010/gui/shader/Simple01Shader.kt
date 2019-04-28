package milu.kiriu2010.gui.shader

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// ------------------------------------------
// 特殊効果なし
// ------------------------------------------
// 2019.04.27 コメントアウト
// ------------------------------------------
class Simple01Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;

            void main() {
                v_Color        = a_Color;
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
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )

        return this
    }

    // 面塗りつぶし
    fun draw(modelAbs: MgModelAbs,
             u_matMVP: FloatArray,
             mode: Int = GLES20.GL_TRIANGLES) {
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

        // モデルを描画
        when (mode) {
            // 面を描画
            GLES20.GL_TRIANGLES -> {
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
            }
            // 線を描画
            GLES20.GL_LINES -> {
                val cnt = modelAbs.datPos.size/3
                GLES20.glDrawArrays(GLES20.GL_LINES,0,cnt)
            }
            // 線を描画
            GLES20.GL_LINE_STRIP -> {
                val cnt = modelAbs.datPos.size/3
                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,0,cnt)
            }
        }
    }

    /*
    // 頂点同士を線で結ぶ(GL_LINES)
    // ----------------------------------------------------
    //   unit
    //     頂点リストを分割する単位
    //     例:XYZ軸に矢印を描画
    //     XYZ軸の３方向あるので３を指定する
    // ----------------------------------------------------
    fun drawLines(modelAbs: MgModelAbs,
                  matMVP: FloatArray,
                  unit: Int) {
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
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }

        // モデルを描画
        //GLES20.glDrawArrays(GLES20.GL_LINES,0,4)
        //GLES20.glDrawArrays(GLES20.GL_LINES,4,4)
        //GLES20.glDrawArrays(GLES20.GL_LINES,8,4)
        // 描画点がXYZの３座標保持するため３割る
        val cnt = modelAbs.datPos.size/3/unit
        (0..(unit-1)).forEach { i ->
            GLES20.glDrawArrays(GLES20.GL_LINES,i*cnt,cnt)
        }
    }
    */

    /*
    // ----------------------------------------------------
    // 頂点同士を線で結ぶ(GL_LINES)
    // ----------------------------------------------------
    fun drawLines(modelAbs: MgModelAbs,
                  matMVP: FloatArray) {
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
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }

        // モデルを描画
        val cnt = modelAbs.datPos.size/3
        GLES20.glDrawArrays(GLES20.GL_LINES,0,cnt)
    }
    */
}
