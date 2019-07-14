package milu.kiriu2010.milumathcaras.gui.draw.d3.wave

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Board01Model
import milu.kiriu2010.gui.model.d2.Circle01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.shader.es32.ES32Simple02Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpco
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs

// -----------------------------------------
// 円が床を境に円運動
// -----------------------------------------
// https://twitter.com/i/status/1103458994855858177
// -----------------------------------------
// 2019.07.14
// -----------------------------------------
class WaveCircle01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(円)
    private val modelCircle = Circle01Model()
    // 描画モデル(床)
    private val modelBoard = Board01Model()

    // VAO(円)
    private val vaoCircle = ES32VAOIpc()
    // VAO(床)
    private val vaoBoard = ES32VAOIpco()

    // シェーダ(円)
    private val shaderCircle = ES32Simple01Shader(ctx)
    // シェーダ(床)
    private val shaderBoard = ES32Simple02Shader(ctx)

    // タイルの数
    val n = 10
    val nn = n.toFloat()

    // タイル１辺の長さ
    val a = 2f

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
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,40f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 円を描画
        (-n..n).forEach { i ->
            val ii = i.toFloat()
            if (i==0) return@forEach

            (-n..n).forEach { j ->
                val jj = j.toFloat()
                if (((abs(i+j))%2)==1) {
                    Matrix.setIdentityM(matM,0)
                    Matrix.translateM(matM,0,0f,a*(ii-1),0f)
                    Matrix.rotateM(matM,0,t0-abs(jj)*15f,1f,0f,0f)
                    Matrix.translateM(matM,0,a*jj,a*ii/abs(ii),0f)
                    Matrix.rotateM(matM,0,-(t0-abs(jj)*15f),1f,0f,0f)
                    Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                    shaderCircle.draw(vaoCircle,matMVP)
                }
            }
        }

        // 床を描画
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderBoard.draw(vaoBoard,matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        //GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(円)
        shaderCircle.loadShader()

        // シェーダ(床)
        shaderBoard.loadShader()

        // 描画モデル(円)
        modelCircle.createPath(mapOf(
            "row" to 32f,
            "colorR" to 0f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f,
            "pattern" to 2f,
            "scale" to 0.5f
        ))

        // VAO(円)
        vaoCircle.makeVIBO(modelCircle)

        // 床のオフセット
        (-n..n).forEach { i ->
            val ii = i.toFloat()
            (-n..n).forEach { j ->
                val jj = j.toFloat()
                if (((i+j)%2)==0) {
                    modelBoard.datOff.addAll(arrayListOf(a*jj,a*ii,0f))
                }
            }
        }

        // 描画モデル(床)
        modelBoard.createPath(mapOf(
            "colorR" to 0f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f
        ))

        // VAO(床)
        vaoBoard.makeVIBO(modelBoard)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoCircle.deleteVIBO()
        vaoBoard.deleteVIBO()
        shaderCircle.deleteShader()
    }
}
