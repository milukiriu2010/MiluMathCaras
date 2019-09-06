package milu.kiriu2010.milumathcaras.gui.draw.d3.wave

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Line02Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// サイン波01
// -----------------------------------------
// https://66.media.tumblr.com/e89579738684834b107dd3d26fbf02ac/tumblr_ms52ec6PHY1r2geqjo1_500.gif
// -----------------------------------------
// 2019.09.06
// -----------------------------------------
class WaveSin01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(サイン波)
    private val model = Line02Model()

    // VBO(特殊効果なし)
    private val vaoSimple = ES32VAOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    override fun onDrawFrame(gl: GL10?) {
        if ( isRunning == true ) {
            // 位相角度
            angle[0] = (angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // デフォルトバッファを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(20f,20f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        GLES32.glLineWidth(10f)

        (-20..20).forEach { z ->
            val zz = z.toFloat()
            val z1 = zz * 0.5f
            val z2 = zz * 18f
            Matrix.setIdentityM(matM,0)
            val y = MyMathUtil.sinf(t0+z2)
            Matrix.translateM(matM,0,0f,y,z1)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(vaoSimple,matMVP,GLES32.GL_LINE_STRIP)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShaderDynamic()

        // 描画モデル(サイン波)
        model.createPath()

        // VBO(特殊効果なし)
        vaoSimple.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoSimple.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
