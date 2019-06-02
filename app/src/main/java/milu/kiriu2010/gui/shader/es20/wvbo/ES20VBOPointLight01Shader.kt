package milu.kiriu2010.gui.shader.es20.wvbo

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

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
class ES20VBOPointLight01Shader: ES20MgShader() {
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
            """
            precision mediump float;
            // モデル座標変換行列の逆行列
            uniform   mat4 u_matINV;
            // 光源位置
            uniform   vec3 u_vecLight;
            // 視点位置
            uniform   vec3 u_vecEye;
            // 環境光の色
            uniform   vec4 u_ambientColor;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

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
                gl_FragColor    = destColor;
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(3)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // 属性(法線)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Normal").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Normal:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Normal:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Normal:glGetAttribLocation")

        // 属性(色)
        hATTR[2] = GLES20.glGetAttribLocation(programHandle, "a_Color").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Color:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Color:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Color:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(6)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(モデル座標変換行列)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES20Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(平行光源)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES20Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES20Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform(環境光)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle,"u_ambientColor")
        MyGLES20Func.checkGlError("u_ambientColor:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_matM: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_ambientColor: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)


        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[2])
        GLES20.glVertexAttribPointer(hATTR[2],4,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(モデル座標行列)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matM,0)
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(逆行列)
        GLES20.glUniformMatrix4fv(hUNI[2],1,false,u_matI,0)
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glUniform3fv(hUNI[3],1,u_vecLight,0)
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(視点位置)
        GLES20.glUniform3fv(hUNI[4],1,u_vecEye,0)
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        // uniform(環境光の色)
        GLES20.glUniform4fv(hUNI[5],1,u_ambientColor,0)
        MyGLES20Func.checkGlError2("u_ambientColor",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
