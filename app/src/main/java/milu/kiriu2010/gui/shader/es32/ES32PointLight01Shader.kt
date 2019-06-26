package milu.kiriu2010.gui.shader.es32

import android.content.Context
import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.vbo.es32.ES32VAOAbs

// -------------------------------------------------------------------
// 点光源
// -------------------------------------------------------------------
// 平行光源は無限遠から降り注ぐような一定方向のベクトルに対し、
// 点光源は、光源の位置が３次元空間上に固定されるため、
// ３次元空間上のどこにモデリングされるかによって光の当たり方が変わる
//
// 光源から頂点に向かうベクトルをライトベクトルとする
// -------------------------------------------------------------------
// https://wgld.org/d/webgl/w025.html
// -------------------------------------------------------------------
// 2019.06.01
// -------------------------------------------------------------------
class ES32PointLight01Shader(ctx: Context): ES32MgShader(ctx) {
    // 頂点シェーダ
    private val scv =
            """#version 300 es
            layout (location = 0) in vec3 a_Position;
            layout (location = 1) in vec3 a_Normal;
            layout (location = 2) in vec4 a_Color;
            
            uniform   mat4 u_matMVP;
            // モデル座標変換行列
            uniform   mat4 u_matM;
            
            out vec3 v_Position;
            out vec3 v_Normal;
            out vec4 v_Color;

            void main() {
                // モデル座標変換を行ったあとの頂点位置をフラグメントシェーダに渡す
                //   a_Position ⇒ ローカル座標系
                //   v_Position ⇒ ワールド座標系
                v_Position     = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal       = a_Normal;
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es
            precision highp float;
            
            // モデル座標変換行列の逆行列
            uniform   mat4 u_matINV;
            // 光源位置
            uniform   vec3 u_vecLight;
            // 視点位置
            uniform   vec3 u_vecEye;
            // 環境光の色
            uniform   vec4 u_ambientColor;
            
            in  vec3 v_Position;
            in  vec3 v_Normal;
            in  vec4 v_Color;
            
            out vec4 o_FragColor;

            void main() {
                // 点光源から頂点へ向かうベクトル
                vec3  vecLight  = u_vecLight - v_Position;
                // 光の逆ベクトル
                vec3  invLight  = normalize(u_matINV * vec4(vecLight,0.0)).xyz;
                // 視線の逆ベクトル
                vec3  invEye    = normalize(u_matINV * vec4(u_vecEye,0.0)).xyz;
                // 光ベクトルと視線ベクトルからハーフベクトルを算出
                vec3  halfLE    = normalize(invLight + invEye);
                // 拡散度("頂点の法線ベクトル"と"光の逆ベクトル"の内積をとり、0.0～1.0の値を返す
                float diffuse   = clamp(dot(v_Normal,invLight), 0.0, 1.0) + 0.2;
                // 面の法線ベクトルとハーフベクトルの内積をとることで反射光の強さを決定する
                float specular  = pow(clamp(dot(v_Normal, halfLE),0.0,1.0), 50.0);
                // 頂点の色に拡散光と反射光を足す
                vec4  destColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + u_ambientColor;
                o_FragColor    = destColor;
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
        hUNI = IntArray(6)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES32.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES32Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(モデル座標変換行列)
        hUNI[1] = GLES32.glGetUniformLocation(programHandle,"u_matM")
        MyGLES32Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[2] = GLES32.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES32Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(平行光源)
        hUNI[3] = GLES32.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES32Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[4] = GLES32.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES32Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform(環境光)
        hUNI[5] = GLES32.glGetUniformLocation(programHandle,"u_ambientColor")
        MyGLES32Func.checkGlError("u_ambientColor:glGetUniformLocation")

        return this
    }

    fun draw(vao: ES32VAOAbs,
             u_matMVP: FloatArray,
             u_matM: FloatArray,
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

        // uniform(モデル座標行列)
        GLES32.glUniformMatrix4fv(hUNI[1],1,false,u_matM,0)
        MyGLES32Func.checkGlError("u_matM",this,model)

        // uniform(逆行列)
        GLES32.glUniformMatrix4fv(hUNI[2],1,false,u_matI,0)
        MyGLES32Func.checkGlError("u_matINV",this,model)

        // uniform(光源位置)
        GLES32.glUniform3fv(hUNI[3],1,u_vecLight,0)
        MyGLES32Func.checkGlError("u_vecLight",this,model)

        // uniform(視点位置)
        GLES32.glUniform3fv(hUNI[4],1,u_vecEye,0)
        MyGLES32Func.checkGlError("u_vecEye",this,model)

        // uniform(環境光の色)
        GLES32.glUniform4fv(hUNI[5],1,u_ambientColor,0)
        MyGLES32Func.checkGlError("u_ambientColor",this,model)

        // モデルを描画
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, model.datIdx.size, GLES32.GL_UNSIGNED_SHORT, 0)

        // VAO解放
        GLES32.glBindVertexArray(0)
    }
}
