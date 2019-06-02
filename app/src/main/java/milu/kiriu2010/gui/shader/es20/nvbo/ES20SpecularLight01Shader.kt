package milu.kiriu2010.gui.shader.es20.nvbo

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------------------------------------------
// 反射光
// -------------------------------------------------------------------------
// 反射光を取り入れることでモデルに光沢や輝きを持たせることが可能になる
// 金属のような輝きのある面や、表面のツルツルした質感を表現することができる
//
// モデルを見つめる視線と光の向きとを考慮してライティングすることで、
// 自然なハイライトを表現する。
// 光源から放たれた光がモデルにぶつかって反射し、
// その反射した光と視線がまっすぎに向き合ている場合、
// 最も強く光が視線に向かう。
// -------------------------------------------------------------------------
// 2019.05.14  コメント追加
// 2019.05.22  リソース解放
// -------------------------------------------------------------------------
class ES20SpecularLight01Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
        """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            uniform   mat4 u_matINV;
            uniform   vec3 u_vecLight;
            uniform   vec3 u_vecEye;
            uniform   vec4 u_ambientColor;
            varying   vec4 v_Color;

            void main() {
                vec3  invLight = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                vec3  invEye   = normalize(u_matINV * vec4(u_vecEye  ,0.0)).xyz;
                // ライトベクトルと視線ベクトルとのハーフベクトル
                vec3  halfLE   = normalize(invLight + invEye);
                float diffuse  = clamp(dot(a_Normal,invLight), 0.0, 1.0);
                // ハーフベクトルと面の法線ベクトルとの内積を取ることで反射光の強さを決定する
                // powを使うことで、弱い光をさらに弱く、強い光は、そのまま残している
                float specular = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                vec4  light    = a_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                // 色 = 頂点色 * 拡散光 + 反射光 + 環境光
                v_Color        = light + u_ambientColor;
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

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

        return this
    }

    fun draw(model: MgModelAbs,
             u_matMVP: FloatArray,
             u_matINV: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_ambientColor: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        val hNormal = GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        model.bufCol.position(0)
        val hColor = GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        +       // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matINV,0)
        }
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(平行光源)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        // uniform(環境光)
        GLES20.glGetUniformLocation(programHandle,"u_ambientColor").also {
            GLES20.glUniform4fv(it,1,u_ambientColor,0)
        }
        MyGLES20Func.checkGlError2("u_ambientColor",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glDisableVertexAttribArray(hPosition)
        GLES20.glDisableVertexAttribArray(hNormal)
        GLES20.glDisableVertexAttribArray(hColor)
    }
}
