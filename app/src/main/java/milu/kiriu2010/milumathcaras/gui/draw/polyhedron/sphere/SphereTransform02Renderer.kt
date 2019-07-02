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
// 球体座標変換02
// -----------------------------------
// 半透明のトーラスの中を
// 球体が通り抜ける
// -----------------------------------
// 2019.06.11
// -----------------------------------
class SphereTransform02Renderer(ctx: Context): MgRenderer(ctx) {

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
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,10f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 球体モデルを描画(X軸公転)
        // ----------------------------------------------
        (0..2).forEach {
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t0+120f*it.toFloat(),1f,0f,0f)
            Matrix.translateM(matM,0,0f,a*cos0,a*sin0)
            Matrix.rotateM(matM,0,8f*t0,0f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(modelSphere,boSphere,matMVP)
        }

        // ----------------------------------------------
        // 球体モデルを描画(Y軸公転)
        // ----------------------------------------------
        (0..2).forEach {
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t0+120f*it.toFloat(),0f,1f,0f)
            Matrix.translateM(matM,0,a*sin0,0f,a*cos0)
            Matrix.rotateM(matM,0,8f*t0,0f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(modelSphere,boSphere,matMVP)
        }

        // ----------------------------------------------
        // 球体モデルを描画(Z軸公転)
        // ----------------------------------------------
        (0..2).forEach {
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t0+120f*it.toFloat(),0f,0f,1f)
            Matrix.translateM(matM,0,a*cos0,a*sin0,0f)
            Matrix.rotateM(matM,0,8f*t0,0f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(modelSphere,boSphere,matMVP)
        }

        // ----------------------------------------------
        // トーラスを描画(YZ平面)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelTorus,boTorus,matMVP)

        // ----------------------------------------------
        // トーラスを描画(ZX平面)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelTorus,boTorus,matMVP)

        // ----------------------------------------------
        // トーラスを描画(XY平面)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelTorus,boTorus,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // ブレンドの有効化
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBOSimple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
            "colorA" to 0.8f
        ))

        // 描画モデル(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
            "iradius" to 1.0f,
            "oradius" to a,
            "colorA"  to 0.2f
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