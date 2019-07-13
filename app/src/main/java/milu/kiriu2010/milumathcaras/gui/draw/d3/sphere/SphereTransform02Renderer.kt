package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.model.d3.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
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
    private val modelSphere = Sphere01Model()
    // 描画モデル(トーラス)
    private val modelTorus = Torus01Model()

    // VAO(球体)
    private val vaoSphere = ES32VAOIpc()
    // VAO(トーラス)
    private val vaoTorus = ES32VAOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // 係数
    private val a = 6f

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

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
            shaderSimple.draw(vaoSphere,matMVP)
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
            shaderSimple.draw(vaoSphere,matMVP)
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
            shaderSimple.draw(vaoSphere,matMVP)
        }

        // ----------------------------------------------
        // トーラスを描画(YZ平面)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoTorus,matMVP)

        // ----------------------------------------------
        // トーラスを描画(ZX平面)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoTorus,matMVP)

        // ----------------------------------------------
        // トーラスを描画(XY平面)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoTorus,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glEnable(GLES32.GL_CULL_FACE)

        // ブレンドの有効化
        GLES32.glEnable(GLES32.GL_BLEND)
        GLES32.glBlendFunc(GLES32.GL_SRC_ALPHA,GLES32.GL_ONE_MINUS_SRC_ALPHA)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        // 描画モデル(球体)
        modelSphere.createPath(mapOf(
            "colorA" to 0.8f
        ))

        // 描画モデル(トーラス)
        modelTorus.createPath(mapOf(
            "iradius" to 1.0f,
            "oradius" to a,
            "colorA"  to 0.2f
        ))

        // VAO(球体)
        vaoSphere.makeVIBO(modelSphere)

        // VAO(トーラス)
        vaoTorus.makeVIBO(modelTorus)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoSphere.deleteVIBO()
        vaoTorus.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
