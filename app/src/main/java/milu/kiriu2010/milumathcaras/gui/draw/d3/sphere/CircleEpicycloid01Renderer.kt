package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.d2.Circle01Model
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.model.d3.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 円のエピサイクロイド01
// -----------------------------------
// 円がエピサイクロイド運動する
// -----------------------------------
// 2019.09.24
// -----------------------------------
class CircleEpicycloid01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(円)
    private val modelCircles = mutableListOf<Circle01Model>()

    // VAO(円)
    private val vaoCircles = mutableListOf<ES32VAOIpc>()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // エピサイクロイドの係数r
    private val r = 1f
    // エピサイクロイドの係数k
    private val k = 2f
    // エピサイクロイドの分割数
    private val splitN = 10
    // エピサイクロイドの係数t()
    private val td = 360f/splitN.toFloat()

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
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,10f))
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
        /*
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,a*cos0,a*sin0,0f)
        Matrix.rotateM(matM,0,8f*t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoSphere,matMVP)

         */

        vaoCircles.forEachIndexed { id, vao ->
            val t = id.toFloat() * td

            val x = r * (1f+1f/k) * MyMathUtil.cosf(t) - r/k * MyMathUtil.cosf((k+1f)*t)
            val y = r * (1f+1f/k) * MyMathUtil.sinf(t) - r/k * MyMathUtil.sinf((k+1f)*t)

            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,x,y,0f)
            Matrix.rotateM(matM,0,8f*t0,0f,0f,1f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(vao,matMVP)

        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES32.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        (0 until 360 step splitN).forEach { i ->
            val colors = MgColor.hsva(i,1f,1f,1f)

            // 描画モデル(円)
            val model = Circle01Model()
            model.createPath(mapOf(
                "scale" to 0.1f,
                "colorR" to colors[0],
                "colorG" to colors[1],
                "colorB" to colors[2],
                "colorA" to colors[3]
            ))

            // VAO(円)
            val vao = ES32VAOIpc()
            vao.makeVIBO(model)

            modelCircles.add(model)
            vaoCircles.add(vao)
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoCircles.forEach {
            it.deleteVIBO()
        }
        shaderSimple.deleteShader()
    }

}