package milu.kiriu2010.milumathcaras.gui.draw.d3.cube.hexagon

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------------------------------
// 六角形にみえる立方体座標変換01
// -----------------------------------------------------------------
// ピンク(上:Y軸)・緑(左:Z軸)・黒(右:X軸)の領域に分かれ４回回転
// -----------------------------------------------------------------
// https://twitter.com/InfinityLoopGIF/status/1149451600248868864
// -----------------------------------------------------------------
// 2019.07.25
// -----------------------------------------------------------------
class CubeLikeHexagon01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(立方体)
    private val modelCube = Cube01Model()
    // 描画モデル(台形リスト)
    private val modelTrapezoids = mutableListOf<Trapezoid01Model>()

    // VAO(立方体)
    private val vaoCube = ES32VAOIpc()
    // VAO(台形)
    private val vaoTrapezoids = mutableListOf<ES32VAOIpc>()

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
            angle[0] =(angle[0]+2)%90
            hNow -= hDv1
        }
        if ( angle[0] == 0 ) {
            hNow = hMax
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(6f,6f,6f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.orthoM(matP,0,-6f,6f,-6f,6f,0.1f,80f)
        //Matrix.perspectiveM(matP,0,45f,1f,0.1f,80f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        Matrix.setIdentityM(matM,0)
        //Matrix.rotateM(matM,0,t,0f,0f,1f)
        //Matrix.translateM(matM,0,cos,sin,h)
        //Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoCube,matMVP)


        vaoTrapezoids.forEachIndexed { id, vao ->
            Matrix.setIdentityM(matM,0)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.draw(vao,matMVP)
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
        shaderSimple.loadShader()

        // 描画モデル(立方体)
        modelCube.createPath()

        // VAO
        vaoCube.makeVIBO(modelCube)

        // 描画モデル(台形)
        (1..2).forEach { i ->
            val ii = i.toFloat()
            val modelTrapezoid = Trapezoid01Model()
            modelTrapezoid.createPath(mapOf(
                "pattern" to ii
            ))
            modelTrapezoids.add(modelTrapezoid)

            val vaoTrapezoid = ES32VAOIpc()
            vaoTrapezoid.makeVIBO(modelTrapezoid)
            vaoTrapezoids.add(vaoTrapezoid)
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoCube.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
