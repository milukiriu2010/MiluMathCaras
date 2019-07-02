package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.model.d2.Board01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// 立方体座標変換11
// -----------------------------------------
// 立方体が床から出てくる
// -----------------------------------------
// 2019.06.30
// -----------------------------------------
class CubeTransform11Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(床:黒)
    private val modelBoardB = Board01Model()
    // 描画モデル(床:白)
    private val modelBoardW = Board01Model()
    // 描画モデル(立方体)
    private val modelCube = Cube01Model()

    // VAO(床:黒)
    private val vaoBoardB = ES32VAOIpc()
    // VAO(床:白)
    private val vaoBoardW = ES32VAOIpc()
    // VAO(立方体)
    private val vaoCube = ES32VAOIpc()

    // シェーダ(特殊効果なし)
    private val shader = ES32Simple01Shader(ctx)

    // タイルの数
    val n = 10
    val nn = n.toFloat()
    val nn2 = nn*0.5f

    // 隣の立方体の位相差
    val d = 10f

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,4f,8f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,80f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // デフォルトバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        (0..n).forEach { i ->
            val ii = i.toFloat()
            (0..n).forEach { j ->
                val jj = j.toFloat()
                when ((i+j)%2) {
                    0 -> {
                        // 床:黒
                        Matrix.setIdentityM(matM,0)
                        Matrix.translateM(matM,0,jj-nn2,0f,ii-nn2)
                        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                        shader.draw(vaoBoardB,matMVP)
                    }
                    1 -> {
                        // 床:白
                        Matrix.setIdentityM(matM,0)
                        Matrix.translateM(matM,0,jj-nn2,0f,ii-nn2)
                        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                        shader.draw(vaoBoardW,matMVP)

                        // 立方体
                        val y = 2f*MyMathUtil.sinf(t0-d*(ii*2f+jj))
                        Matrix.setIdentityM(matM,0)
                        Matrix.translateM(matM,0,jj-nn2,y,ii-nn2)
                        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                        shader.draw(vaoCube,matMVP)
                    }
                }
            }
        }


    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        //GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shader.loadShader()

        // 描画モデル(床:黒)
        modelBoardB.createPath(mapOf(
            "colorR" to 0f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f,
            "scale" to 0.5f,
            "pattern" to 2f
        ))

        // 描画モデル(床:白)
        modelBoardW.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f,
            "scale" to 0.5f,
            "pattern" to 2f
        ))

        // 描画モデル(立方体)
        modelCube.createPath(mapOf(
            "scale" to 0.5f
        ))

        // VAO(床:黒)
        vaoBoardB.makeVIBO(modelBoardB)
        // VAO(床:白)
        vaoBoardW.makeVIBO(modelBoardW)
        // VAO(立方体)
        vaoCube.makeVIBO(modelCube)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoBoardB.deleteVIBO()
        vaoBoardW.deleteVIBO()
        vaoCube.deleteVIBO()
        shader.deleteShader()
    }
}
