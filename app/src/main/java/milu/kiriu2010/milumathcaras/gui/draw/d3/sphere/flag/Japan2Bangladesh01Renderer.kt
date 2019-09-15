package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.flag

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 日本⇔バングラディッシュ01
// -----------------------------------
//
//
// -----------------------------------
// 2019.09.15
// -----------------------------------
class Japan2Bangladesh01Renderer(ctx: Context): MgRenderer(ctx) {

    private enum class ModePtn {
        PTN1
    }

    // 現在の描画パターン
    private var ptnNow = ModePtn.PTN1

    // 描画モデル(長方形)
    private val modelRectangles = mutableListOf<Rectangle01Model>()

    // 描画モデル(球体)
    private val modelSphere = Sphere01Model()

    // VAO(長方形)
    private val vaoRectangles = mutableListOf<ES32VAOIpc>()
    // VAO(球体)
    private val vaoSphere = ES32VAOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // モデルのサイズ係数
    private val a = 1f
    // 縦横ループ
    val b = 3

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(0f, 0f, 0f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        when (ptnNow) {
            ModePtn.PTN1 -> transformPtn1(t0)
        }

    }

    private fun transformPtn1(t0: Float) {
        val cos0 = MyMathUtil.cosf(t0)
        val sin0 = MyMathUtil.sinf(t0)

        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val k = (i+j)%2

                // 長方形を描画
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                val vaoRectangle = vaoRectangles[k]
                shaderSimple.draw(vaoRectangle,matMVP)

            }
        }

        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            val ll = when (j%2) {
                0 -> 1f
                1 -> -1f
                else -> 1f
            }
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val k = (i+j)%2
                val kk = when(k) {
                    0 -> -1f
                    1 -> 1f
                    else -> -1f
                }

                // 球体を描画
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a,a*jj-bbj*a,0f)
                Matrix.rotateM(matM,0,ll*t0,0f,1f,0f)
                Matrix.translateM(matM,0,kk*a,0f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoSphere,matMVP)

            }
        }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        //GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        modelRectangles.clear()
        vaoRectangles.forEach {
            it.deleteVIBO()
        }
        vaoRectangles.clear()

        (1..2).forEach {
            // 描画モデル(長方形)
            val modelRectangle = Rectangle01Model()
            modelRectangle.createPath(mapOf(
                "pattern" to it.toFloat()
            ))
            modelRectangles.add(modelRectangle)

            // VAO(長方形)
            val vaoRectangle = ES32VAOIpc()
            vaoRectangle.makeVIBO(modelRectangle)
            vaoRectangles.add(vaoRectangle)
        }

        // 描画モデル(球体)
        modelSphere.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f,
            "radius" to a*0.3f
        ))
        // VAO(球体)
        vaoSphere.makeVIBO(modelSphere)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoRectangles.forEach {
            it.deleteVIBO()
        }
        vaoSphere.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
