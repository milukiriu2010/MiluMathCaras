package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.sphere

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.model.d3.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 球体座標変換01
// -----------------------------------
// トーラスを円を描くように並べ
// その間を球体が通り抜ける
// -----------------------------------
// 2019.06.08
// -----------------------------------
class SphereTransform01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(球体)
    private lateinit var modelSphere: MgModelAbs
    // 描画モデル(トーラス)
    private lateinit var modelTorus: MgModelAbs

    // VBO(球体)
    private lateinit var boSphere: ES20VBOIpc
    // VBO(トーラス)
    private lateinit var boTorus: ES20VBOIpc

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader

    // 係数
    private val a = 6f
    private val b = a-4.5f

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()
        val cos0 = MyMathUtil.cosf(t0)
        val sin0 = MyMathUtil.sinf(t0)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,-20f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 球体モデルを描画
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,a*cos0,a*sin0,0f)
        Matrix.rotateM(matM,0,8f*t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelSphere,boSphere,matMVP)

        // ----------------------------------------------
        // トーラスを描画
        // ----------------------------------------------
        (0..7).forEach {
            val t = (45*it).toFloat()
            val cos = MyMathUtil.cosf(t)
            val sin = MyMathUtil.sinf(t)
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,a*cos,a*sin,0f)
            // なぜか２倍にしないと、球体の通過がトーラスの面と一致しない
            Matrix.rotateM(matM,0,2f*t0,0f,0f,1f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(modelTorus,boTorus,matMVP)
        }

    }

    // モデルを座標変換し描画する
    //     matA: 変換前の座標変換行列
    //        p: 移動位置(x,y,z)
    //     axis: 回転軸(x,y,z)と回転角度
    //   return: 変換後の座標変換行列
    private fun transformModel(matA: FloatArray, p: FloatArray, axis: FloatArray ): FloatArray {
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matA,0,-p[0],-p[1],-p[2])
        // モデルを軸を中心に回転させる
        Matrix.rotateM(matA,0,axis[3],axis[0],axis[1],axis[2])
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matA,0,p[0],p[1],p[2])
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matA,0)
        shaderSimple.draw(modelSphere,boSphere,matMVP)

        return matA
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBOSimple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath()

        // 描画モデル(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
            "iradius" to 0.5f,
            "oradius" to b
        ))

        // VBO(球体)
        boSphere = ES20VBOIpc()
        boSphere.makeVIBO(modelSphere)

        // VBO(トーラス)
        boTorus = ES20VBOIpc()
        boTorus.makeVIBO(modelTorus)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        boSphere.deleteVIBO()
        boTorus.deleteVIBO()
        shaderSimple.deleteShader()
    }

}