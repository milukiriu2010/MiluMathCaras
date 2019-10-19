package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere.flag

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 日本⇔グリーンランド01
// -----------------------------------
// 0: 日本
// 1: グリーンランド
// 2: インドネシア・モナコ
// 3: ポーランド
// -----------------------------------
// 2019.10.02
// -----------------------------------
class Japan2Greenland01Renderer(ctx: Context): MgRenderer(ctx) {
    // 現在の描画パターン
    private var ptnNow = 0

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
    val b = 5
    val c = b

    // 時間停止
    val timeS = 0
    val timeE = 20
    var timeN = timeS

    // 回転角度
    var rot = 90

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        //GLES32.glClearColor(0f, 0f, 0f, 1f)
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

                angle[0] =(angle[0]+2)%rot
                if ( angle[0] == 0 ) {
                    ptnNow = (ptnNow+1)%4
                    timeN = timeS
                }
            }
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,12f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.orthoM(matP,0,-5f,5f,-5f,5f,0.1f,20f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        transformPtn1(t0)
    }

    // 旗の境を中心に球体が180度回転
    private fun transformPtn1(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j->
            val jj = j.toFloat()
            (0..b).forEach { i ->
                val ii = i.toFloat()

                val m = (i+j+ptnNow)%4

                // ------------------------------------------
                // 長方形(上)
                // ------------------------------------------
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                // 定常回転
                when (m) {
                    // 0:日本
                    //0 -> Matrix.rotateM(matM,0,-t0,1f,0f,0f)
                    // 1:グリーンランド
                    //1 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 2:ポーランド
                    2 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 3:インドネシア・モナコ
                    3 -> Matrix.rotateM(matM,0,-t0,1f,0f,0f)
                }
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                val vaoRectangleU = when (m) {
                    // 0:日本
                    0 -> vaoRectangles[0]
                    // 1:グリーンランド
                    1 -> vaoRectangles[0]
                    // 2:ポーランド
                    2 -> vaoRectangles[0]
                    // 3:インドネシア・モナコ
                    3 -> vaoRectangles[1]
                    else -> vaoRectangles[0]
                }
                shaderSimple.draw(vaoRectangleU,matMVP)

                // ------------------------------------------
                // 長方形(下)
                // ------------------------------------------
                Matrix.setIdentityM(matM,0)
                // 初期位置(シフト)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                // 定常回転
                when (m) {
                    // 0:日本
                    0 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 1:グリーンランド
                    //1 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 2:ポーランド
                    2 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 3:インドネシア・モナコ
                    3 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                }
                // 初期位置(シフト)
                Matrix.translateM(matM,0,0f,-a*0.5f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                val vaoRectangleD = when (m) {
                    // 0:日本
                    0 -> vaoRectangles[0]
                    // 1:グリーンランド
                    1 -> vaoRectangles[1]
                    // 2:ポーランド
                    2 -> vaoRectangles[1]
                    // 3:インドネシア・モナコ
                    3 -> vaoRectangles[0]
                    else -> vaoRectangles[0]
                }
                shaderSimple.draw(vaoRectangleD,matMVP)

                // ------------------------------------------
                // 長方形(後)
                // ------------------------------------------
                Matrix.setIdentityM(matM,0)
                // 初期位置(シフト)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                // 定常回転
                when (m) {
                    // 0:日本
                    0 -> Matrix.rotateM(matM,0,-t0,1f,0f,0f)
                    // 1:グリーンランド
                    //1 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 2:ポーランド
                    //2 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 3:インドネシア・モナコ
                    3 -> Matrix.rotateM(matM,0,-t0,1f,0f,0f)
                }
                // 初期位置(回転)
                Matrix.rotateM(matM,0,-90f,1f,0f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                val vaoRectangleB = when (m) {
                    // 0:日本
                    0 -> vaoRectangles[1]
                    // 1:グリーンランド
                    1 -> vaoRectangles[0]
                    // 2:ポーランド
                    2 -> vaoRectangles[1]
                    // 3:インドネシア・モナコ
                    3 -> vaoRectangles[0]
                    else -> vaoRectangles[0]
                }
                shaderSimple.draw(vaoRectangleB,matMVP)

                // ------------------------------------------
                // 球体
                // ------------------------------------------
                Matrix.setIdentityM(matM,0)
                // 初期位置(シフト)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                // 定常回転
                when (m) {
                    // 0:日本
                    0 -> Matrix.rotateM(matM,0,-t0,1f,0f,0f)
                    // 1:グリーンランド
                    1 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 2:ポーランド
                    2 -> Matrix.rotateM(matM,0,-2f*t0,1f,0f,0f)
                    // 3:インドネシア・モナコ
                    3 -> Matrix.rotateM(matM,0,-3f*t0,1f,0f,0f)
                }
                // 初期位置(回転)
                when (m) {
                    // 0:日本
                    0 -> Matrix.rotateM(matM,0,90f,1f,0f,0f)
                    // 2:ポーランド
                    2 -> Matrix.rotateM(matM,0,180f,1f,0f,0f)
                }
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

        (3..4).forEach {
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
            "hemi" to 1f,
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f,
            "colorR2" to 1f,
            "colorG2" to 1f,
            "colorB2" to 1f,
            "colorA2" to 1f,
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
