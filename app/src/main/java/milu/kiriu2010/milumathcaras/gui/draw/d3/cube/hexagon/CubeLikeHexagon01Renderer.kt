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

    // 時間分割数
    val splitN = 200f

    // 立方体の回転
    val rotS = 0f
    val rotE = 90f
    var rotN = rotS
    val rotDv = (rotE-rotS)/(splitN*0.25f)
    val rotSgn = floatArrayOf(
        0f,-1f, 0f,
        0f, 0f,-1f,
        1f, 0f, 0f,
        0f, 0f,-1f
    )
    var rotI = 0

    // 立方体のスケール
    val scaleS = 2f
    val scaleE = 1f
    var scaleN = scaleS
    val scaleDv = (scaleE-scaleS)/splitN

    // 台形の位置
    val tpzS = 5f
    val tpzE = 0f
    var tpzN = tpzS
    val tpzDv = (tpzE-tpzS)/splitN
    val tpzSft = floatArrayOf(
         1f, 1f,-1f,
         1f,-1f,-1f,
         1f,-1f, 1f,
        -1f,-1f, 1f,
        -1f, 1f, 1f,
        -1f, 1f,-1f
    )

    // 時間停止
    val timeS = 0
    val timeE = 2
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
                    rotI = (rotI+1)%4
                    // 立方体が90度回転したら、時間停止させる
                    timeN = timeS
                }

                // 立方体のスケール
                scaleN += scaleDv
                if ( scaleN <= scaleE ) {
                    scaleN = scaleS
                }

                // 台形の位置
                tpzN += tpzDv
                if ( tpzN <= tpzE ) {
                    tpzN = tpzS
                    // 微妙な誤差があるので、
                    // "立方体のスケール"と"台形の位置"関係を一致させる
                    scaleN = scaleS
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

        // 描画(立方体)
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,scaleN,scaleN,scaleN)
        (rotI downTo 0).forEach {
            if (rotI == it) {
                Matrix.rotateM(matM,0,rotN,rotSgn[3*it],rotSgn[3*it+1],rotSgn[3*it+2])
            }
            else {
                Matrix.rotateM(matM,0,90f,rotSgn[3*it],rotSgn[3*it+1],rotSgn[3*it+2])
            }
        }
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoCube,matMVP)

        // 描画(台形)
        vaoTrapezoids.forEachIndexed { id, vao ->
            Matrix.setIdentityM(matM,0)
            val x = tpzSft[id*3+0]*tpzN
            val y = tpzSft[id*3+1]*tpzN
            val z = tpzSft[id*3+2]*tpzN
            Matrix.translateM(matM,0,x,y,z)
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
        modelCube.createPathFace(
            floatArrayOf(
                0f,0.392157f,0f,1f,
                0f,0f,0f,1f,
                0f,0.392157f,0f,1f,
                0f,0f,0f,1f,
                1f,0.752941f,0.796078f,1f,
                1f,0.752941f,0.796078f,1f
            )
        )

        // VAO
        vaoCube.makeVIBO(modelCube)

        // 描画モデル(台形)
        (1..6).forEach { i ->
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
