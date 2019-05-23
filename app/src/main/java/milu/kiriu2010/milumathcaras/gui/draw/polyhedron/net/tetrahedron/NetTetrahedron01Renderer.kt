package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.tetrahedron

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.Simple01Shader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// -------------------------------------
// 正四面体の展開図
// -------------------------------------
// ぴったり合わないのでボツ
// -------------------------------------
class NetTetrahedron01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(三角形)
    private val modelLst = mutableListOf<Triangle4Tetrahedron01Model>()

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: Simple01Shader

    // 定数
    val sa = sqrt(3f)/6f
    val sb = sqrt(3f)/12f
    val sc = 0.25f
    val sd = sqrt(3f)

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

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,4f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 静止したモデルを描画
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[0],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(左下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-sb,-sc,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sd,0f)
        Matrix.translateM(matM,0,sb,sc,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[1],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(右下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,sb,-sc,0f)
        Matrix.rotateM(matM,0,-t0,1f,sd,0f)
        Matrix.translateM(matM,0,-sb,sc,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[2],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sa,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,-sa,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[3],matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 回転
        isRunning = true

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ(特殊効果なし)
        shaderSimple = Simple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(三角形)
        (1..4).forEach {
            val model = Triangle4Tetrahedron01Model()
            model.createPath(mapOf(
                "pattern" to it.toFloat()
            ))
            modelLst.add(model)
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        // シェーダ(特殊効果なし)
        shaderSimple.deleteShader()
    }

}