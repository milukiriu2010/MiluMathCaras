package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// 立方体座標変換09
// -----------------------------------------
// 等間隔のスパイラルを描く
// 見ていると、ちょっと気持ち悪くなる
// -----------------------------------------
// 2019.06.08
// -----------------------------------------
class CubeTransform09Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(立方体)
    private val model = Cube01Model()

    // VAO
    private val vao = ES32VAOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    val a = 5f

    val hMax = 30f
    var hNow = hMax
    val hDv1 = 0.1f
    val hDv2 = 1f

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+2)%360
            hNow -= hDv1
        }
        if ( angle[0] == 0 ) {
            hNow = hMax
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,80f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        (0..83).forEach {
            val t = 30f*it.toFloat()
            val cos = a*MyMathUtil.cosf(t+t0)
            val sin = a*MyMathUtil.sinf(t+t0)
            val h = hNow - hDv2*it.toFloat()

            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t,0f,0f,1f)
            Matrix.translateM(matM,0,cos,sin,h)
            Matrix.rotateM(matM,0,t+4f*t0,0f,0f,1f)
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

        // 描画モデル(立方体)
        model.createPath()

        // VAO
        vao.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vao.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
