package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.tetrahedron.tetrahedron01

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.milumathcaras.gui.draw.polyhedron.MgModelAbs

class Tetrahedron01Shader {
    // 頂点シェーダ
    private val scv =
        """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            // モデル座標変換行列
            uniform   mat4 u_matM;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

            void main() {
                v_Position     = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal       = a_Normal;
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
        """
            precision mediump float;
            // 逆行列
            uniform   mat4 u_matINV;
            // 平行光源
            uniform   vec3 u_vecLight;
            // 視線ベクトル
            uniform   vec3 u_vecEye;
            // 環境光の色
            uniform   vec4 u_vecAmbientColor;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

            void main() {
                // 点光源から頂点へ向かうベクトル
                vec3  vecLight = u_vecLight - v_Position;
                // 光の逆ベクトル
                vec3  invLight = normalize(u_matINV * vec4(vecLight,0.0)).xyz;
                // 視線の逆ベクトル
                vec3  invEye   = normalize(u_matINV * vec4(u_vecEye,0.0)).xyz;
                // 光ベクトルと視線ベクトルからハーフベクトルを算出
                vec3  halfLE   = normalize(invLight + invEye);
                // 拡散度("頂点の法線ベクトル"と"光の逆ベクトル"の内積をとり、0.0～1.0の値を返す
                float diffuse  = clamp(dot(v_Normal,invLight), 0.0, 1.0) + 0.2;
                // 面の法線ベクトルとハーフベクトルの内積をとることで反射光の強さを決定する
                float specular = pow(clamp(dot(v_Normal, halfLE),0.0,1.0), 50.0);
                // 頂点の色に拡散光と反射光を足す
                vec4  vecColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + u_vecAmbientColor;
                gl_FragColor   = vecColor;
            }
            """.trimIndent()

    // プログラムハンドル
    var programHandle: Int = -1

    // シェーダをロードする
    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

        return programHandle
    }

    // シェーダを削除
    fun deleteShader() {
        GLES20.glDeleteProgram(programHandle)
    }

    // モデルを描画する
    fun drawObj(model: MgModelAbs,
                matMVP: FloatArray,
                matM: FloatArray,
                matI: FloatArray,
                vecLight: FloatArray,
                vecEye: FloatArray,
                vecAmbientColor: FloatArray) {

        // attribute(頂点)
        model.bufPos.position(0)
        // get handle to vertex shader's vPosition member
        val a_Position = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        // attribute(法線)
        model.bufNor.position(0)
        val a_Normal = GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal")

        // attribute(色)
        model.bufCol.position(0)
        val a_Color = GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }

        // uniform(座標行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }

        // uniform(平行光源)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,vecLight,0)
        }

        // uniform(視線ベクトル)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,vecEye,0)
        }

        // uniform(環境光の色)
        GLES20.glGetUniformLocation(programHandle,"u_vecAmbientColor").also {
            GLES20.glUniform4fv(it,1,vecAmbientColor,0)
        }

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        GLES20.glDisableVertexAttribArray(a_Position)
        GLES20.glDisableVertexAttribArray(a_Normal)
        GLES20.glDisableVertexAttribArray(a_Color)
    }
}
