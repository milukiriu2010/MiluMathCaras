package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.wave

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Line01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// 線でHelixを描画
// -----------------------------------------
// https://66.media.tumblr.com/4c437d88df4fa9bac581e50310a02a51/tumblr_milgzyY8vO1r2geqjo1_500.gif
// https://en.wikipedia.org/wiki/Helix
// -----------------------------------------
// 2019.06.15
// -----------------------------------------
class Helix01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(線)
    private val model = Line01Model()

    // VBO(特殊効果なし)
    private lateinit var vboSimple: ES20VBOIpc

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader

    override fun onDrawFrame(gl: GL10?) {
        if ( isRunning == true ) {
            // 回転角度
            angle[0] =(angle[0]+2)%360
        }
        val t0 = angle[0].toFloat()

        // デフォルトバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,6f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,40f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        (-360..360 step 15).forEach { i ->
            val h = i.toFloat()/180f
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,0f,h,0f)
            Matrix.rotateM(matM,0,t0+i,0f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            GLES20.glLineWidth(10f)
            shaderSimple.draw(model,vboSimple,matMVP,GLES20.GL_LINES)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBOSimple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(線)
        model.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f
        ))

        // VBO(特殊効果なし)
        vboSimple = ES20VBOIpc()
        vboSimple.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vboSimple.deleteVIBO()
        shaderSimple.deleteShader()
    }

}