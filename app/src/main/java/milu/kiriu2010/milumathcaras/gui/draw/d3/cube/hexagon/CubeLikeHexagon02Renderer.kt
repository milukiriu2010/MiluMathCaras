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
// 六角形にみえる立方体座標変換02
// -----------------------------------------------------------------
// 黒い立方体の中を白い立方体が回転
// -----------------------------------------------------------------
// https://66.media.tumblr.com/53d72090e13f53ecf74bbd317c684383/tumblr_on53m1dLCm1r2geqjo1_540.gif
// -----------------------------------------------------------------
// 2019.09.12
// -----------------------------------------------------------------
class CubeLikeHexagon02Renderer(ctx: Context): MgRenderer(ctx) {

    private enum class Pattern {
        PTN1
    }

    // 現在のパターン
    private var ptnNow = Pattern.PTN1

    // 描画モデル(立方体(大))
    private val modelCube1 = Cube01Model()
    // 描画モデル(立方体(小))
    private val modelCube2 = Cube01Model()

    // VAO(立方体(大))
    private val vaoCube11 = ES32VAOIpc()
    // VAO(立方体(小))
    private val vaoCube12 = ES32VAOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // 時間分割数
    val splitN = 200f

    // 立方体の回転
    val rotS = 0f
    val rotE = 90f
    var rotN = rotS
    val rotDv = (rotE-rotS)/(splitN*0.5f)

    // 時間停止
    val timeS = 0
    val timeE = 5
    var timeN = timeS

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            // 時間停止
            if ( timeN < timeE ) {
                timeN++
            }
            // 時間復活
            else {
                // 立方体の回転
                rotN += rotDv
                if ( rotN >= rotE ) {
                    rotN = rotS
                    // 立方体が90度回転したら、時間停止させる
                    timeN = timeS
                }

            }
        }

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(6f,6f,6f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.orthoM(matP,0,-6f,6f,-6f,6f,0.1f,80f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 描画(立方体(大))
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoCube11,matMVP)

        // 描画(立方体(小))
        when (ptnNow) {
            Pattern.PTN1 -> rotatePtn1()
        }
    }

    // 描画(立方体(小))
    // パターン１
    private fun rotatePtn1() {
        Matrix.setIdentityM(matM,0)
        //Matrix.translateM(matM,0,-1f,-1f,1f)
        Matrix.translateM(matM,0,-2f,0f,1f)
        Matrix.rotateM(matM,0,rotN,0f,0f,1f)
        Matrix.translateM(matM,0,1f,-1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoCube12,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        //GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        //GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        //GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        // 描画モデル(立方体(大))
        modelCube1.createPath(
            mapOf(
                "pattern" to 1f,
                "scale" to 2f,
                "colorR" to 0f,
                "colorG" to 0f,
                "colorB" to 0f,
                "colorA" to 1f
            )
        )

        // 描画モデル(立方体(小))
        modelCube2.createPath(
            mapOf(
                "pattern" to 1f,
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
            )
        )

        // VAO(立方体(大))
        vaoCube11.makeVIBO(modelCube1)
        // VAO(立方体(小))
        vaoCube12.makeVIBO(modelCube2)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoCube11.deleteVIBO()
        vaoCube12.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
