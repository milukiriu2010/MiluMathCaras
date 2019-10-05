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
//
//
// -----------------------------------
// 2019.10.02
// -----------------------------------
class Japan2Greenland01Renderer(ctx: Context): MgRenderer(ctx) {

    private enum class ModePtn {
        PTN1,
        PTN2,
        PTN3,
        PTN4,
        PTN5,
        PTN6
    }

    // 現在の描画パターン
    private var ptnNow = ModePtn.PTN1

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
    val timeE = 10
    var timeN = timeS

    // 回転角度
    val rot1 = 180
    val rot2 = 90
    var rot = rot1

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(0f, 0f, 0f, 1f)
        //GLES32.glClearColor(1f, 1f, 1f, 1f)
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

                angle[0] =(angle[0]+1)%rot
                if ( angle[0] == 0 ) {
                    ptnNow = when (ptnNow) {
                        ModePtn.PTN1 -> {
                            rot = rot2
                            ModePtn.PTN1
                            //ModePtn.PTN3
                        }
                        ModePtn.PTN3 -> {
                            rot = rot2
                            ModePtn.PTN4
                        }
                        ModePtn.PTN4 -> {
                            rot = rot2
                            ModePtn.PTN5
                        }
                        ModePtn.PTN5 -> {
                            rot = rot2
                            ModePtn.PTN6
                        }
                        ModePtn.PTN6 -> {
                            rot = rot1
                            ModePtn.PTN1
                        }
                        // 2は使ってない
                        ModePtn.PTN2 -> ModePtn.PTN2
                    }
                    timeN = timeS
                }
            }
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,12f))
        //vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        when (ptnNow) {
            ModePtn.PTN1 -> transformPtn1(t0)
            /*
            ModePtn.PTN2 -> transformPtn2(t0)
            ModePtn.PTN3 -> transformPtn3(t0)
            ModePtn.PTN4 -> transformPtn4(t0)
            ModePtn.PTN5 -> transformPtn5(t0)
            ModePtn.PTN6 -> transformPtn6(t0)
             */
        }

        /*
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoSphere,matMVP)
        */
    }

    // 旗の境を中心に球体が180度回転
    private fun transformPtn1(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j->
            val jj = j.toFloat()
            (0..b).forEach { i ->
                val ii = i.toFloat()

                val m = (i+j)%4

                // 長方形(上)
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                val vaoRectangleU = when (m) {
                    0 -> vaoRectangles[0]
                    else -> vaoRectangles[0]
                }
                shaderSimple.draw(vaoRectangleU,matMVP)

                // 長方形(下)
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                when (m) {
                    // 日本
                    0 -> Matrix.translateM(matM,0,0f,-a*0.5f,0f)
                }
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                val vaoRectangleD = when (m) {
                    0 -> vaoRectangles[0]
                    else -> vaoRectangles[0]
                }
                shaderSimple.draw(vaoRectangleD,matMVP)

                // 球体
                Matrix.setIdentityM(matM,0)
                //Matrix.rotateM(matM,0,t0,0f,1f,0f)
                Matrix.translateM(matM,0,ii*2f*a-bbi*2f*a,jj*a-bbj*a,0f)
                when (m) {
                    // 日本
                    0 -> Matrix.rotateM(matM,0,90f,1f,0f,0f)
                }
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoSphere,matMVP)
            }
        }

    }

    /*
    // 旗の中央を中心に旗が180度回転
    // 使わないことにした
    private fun transformPtn2(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val p = (i+j)%2
                val q = when (i%2) {
                    0 -> 1f
                    1 -> -1f
                    else -> 1f
                }

                (0..c).forEach { k ->
                    val kk = k.toFloat()

                    // 長方形を描画
                    //   ⇒ その場で回転
                    // 球体を描画
                    //  ⇒ 静止
                    Matrix.setIdentityM(matM,0)
                    Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,-kk*2f*a)
                    Matrix.rotateM(matM,0,q*t0,0f,1f,0f)
                    Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                    val vaoRectangle = vaoRectangles[p]
                    shaderSimple.draw(vaoRectangle,matMVP)
                    shaderSimple.draw(vaoSphere,matMVP)
                }

            }
        }

    }

    // 旗の中央を中心に旗が90度回転
    // 奥に新たな白地の旗が登場
    private fun transformPtn3(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val p = (i+j)%2
                val q = when (i%2) {
                    0 -> 1f
                    1 -> -1f
                    else -> 1f
                }

                val vaoRectangle = vaoRectangles[p]

                // 長方形を描画
                //   ⇒ その場で回転
                // 球体を描画
                //  ⇒ 静止
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,0f)
                Matrix.rotateM(matM,0,q*t0,0f,1f,0f)
                //Matrix.rotateM(matM,0,90f,0f,1f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)
                shaderSimple.draw(vaoSphere,matMVP)

                // 長方形を描画
                // Z軸方向に１段下がったところに描画
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+2f*a,a*jj-bbj*a,-a)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)
            }
        }

    }

    // "手前の旗⇒奥の旗"へ球体が90度回転することで移動
    private fun transformPtn4(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            val ll = when (j%2) {
                0 -> 1f
                1 -> -1f
                else -> 1f
            }
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val p = (i+j)%2

                val vaoRectangle = vaoRectangles[p]

                // 長方形を描画
                //   ⇒ 90度回転した状態
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,0f)
                Matrix.rotateM(matM,0,90f,0f,1f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)

                // Z軸方向に１段下がったところに描画
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+2f*a,a*jj-bbj*a,-a)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)

                // 球体を描画
                //  "手前の旗⇒奥の旗"
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,-a)
                Matrix.rotateM(matM,0,ll*t0,0f,1f,0f)
                Matrix.translateM(matM,0,0f,0f,a)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoSphere,matMVP)
            }
        }

    }


    // 手前の旗⇒ 90度回転することで移動
    // 奥の旗  ⇒270度回転することで移動
    private fun transformPtn5(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            val ll = when (j%2) {
                0 -> 1f
                1 -> -1f
                else -> 1f
            }
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val p = (i+j)%2

                val vaoRectangle = vaoRectangles[p]

                // 手前の旗を描画
                //   ⇒ 90度回転した状態
                //   ⇒ 90度回転して奥に移動
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,-a)
                Matrix.rotateM(matM,0,ll*t0,0f,1f,0f)
                Matrix.translateM(matM,0,0f,0f,a)
                Matrix.rotateM(matM,0,90f,0f,1f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)

                // 奥の旗+球体を描画
                Matrix.setIdentityM(matM,0)
                //Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+2f*a,a*jj-bbj*a,-a)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,-a)
                Matrix.rotateM(matM,0,3f*ll*t0,0f,1f,0f)
                Matrix.translateM(matM,0,a,0f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)
                shaderSimple.draw(vaoSphere,matMVP)
            }
        }

    }

    // 旗の中央を中心に旗が90度回転
    // 奥に新たな白地の旗が登場
    private fun transformPtn6(t0: Float) {
        val bbi = (b+1).toFloat()*0.5f
        val bbj = b.toFloat()
        (0..2*b).forEach { j ->
            val jj = j.toFloat()
            (0..b).forEach { i ->
                val ii = i.toFloat()
                val p = (i+j)%2
                val q = when (i%2) {
                    0 -> 1f
                    1 -> -1f
                    else -> 1f
                }

                val vaoRectangle = vaoRectangles[p]

                // 長方形を描画
                //   ⇒ 90度回転
                //   ⇒ その場で90度回転
                // 球体を描画
                //  ⇒ 静止
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+a,a*jj-bbj*a,0f)
                Matrix.rotateM(matM,0,q*t0,0f,1f,0f)
                Matrix.rotateM(matM,0,90f,0f,1f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)
                shaderSimple.draw(vaoSphere,matMVP)

                // 長方形を描画
                // Z軸方向に１段下がったところに描画
                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,2f*a*ii-bbi*2f*a+2f*a,a*jj-bbj*a,-a)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vaoRectangle,matMVP)
            }
        }

    }

     */

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

        (3..3).forEach {
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
