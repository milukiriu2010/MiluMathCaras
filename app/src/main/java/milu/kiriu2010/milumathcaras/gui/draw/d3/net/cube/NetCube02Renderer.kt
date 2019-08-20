package milu.kiriu2010.milumathcaras.gui.draw.d3.net.cube

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------
// 立方体の展開図02
// -------------------------------------
// ４×４の領域を展開しながら移動する
// -------------------------------------
// ２面のなす角
//       t  = 90度
// -------------------------------------
// https://sugaku.fun/development-view-of-cube/
// -------------------------------------
// 2019.08.20
// -------------------------------------
class NetCube02Renderer(ctx: Context): MgRenderer(ctx) {

    enum class ModePtn {
        // 展開図０１開
        PTN01_01
    }

    // 描画モデル(三角形)
    private val modelLst = mutableListOf<Triangle4Cube01Model>()

    // VAO
    private var vaoLst = mutableListOf<ES32VAOIpc>()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // 現在のモード
    var modeNow = ModePtn.PTN01_01

    var angleFDiv = 1f
    var cnt = 0
    var cntMax = 90
    var cntMin = 0
    var still = 0

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {

            // stillが0のときだけ、
            // 展開図が開閉するようにする
            if ( still == 0 ) {
                // 立方体を形成した場合、
                // 回転する向きを変更する
                if ( cnt >= cntMax ) {
                    still = 1
                }
                else {
                    cnt++
                }
            }
            else {
                still = (still+1)%30
                if ( still == 0 ) {
                    cnt = cntMin
                }
            }
        }
        val t0 = angleFDiv * cnt.toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(10f,10f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,60f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        when (modeNow) {
            // 展開図０１開
            ModePtn.PTN01_01 -> doPtn01_01(t0)
        }


        /*
        // ----------------------------------------------
        // 静止したモデルを描画
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)


        // ----------------------------------------------
        // 回転するモデルを描画(右)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(左)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,1f,0f)
        Matrix.rotateM(matM,0,t0,1f, 0f,0f)
        Matrix.translateM(matM,0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-1f,0f)
        Matrix.rotateM(matM,0,-t0,1f, 0f,0f)
        Matrix.translateM(matM,0,0f,-1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(奥)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 公転
        Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.translateM(matM,0,2f,0f,0f)
        // 自転
        Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
        */
    }

    // 展開図０１開
    private fun doPtn01_01(t0: Float) {
        // ----------------------------------------------
        // 0:赤
        // (-2,-2)で静止
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-2f,0f,-2f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // 1:緑
        // Z軸90度回転+(-1,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,-2f)
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)



        // ----------------------------------------------
        // 3:黄
        // X軸90度回転+(-2,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-2f,0f,-2f)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 回転
        isRunning = true

        // 深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        /*
        // 描画モデル(四角形)
        val model1 = Triangle4Cube01Model()
        model1.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model1)

        // 描画モデル(四角形)
        val model2 = Triangle4Cube01Model()
        model2.createPath(mapOf(
            "colorR" to 0f,
            "colorG" to 1f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model2)

        // 描画モデル(四角形)
        val model3 = Triangle4Cube01Model()
        model3.createPath(mapOf(
            "colorR" to 0f,
            "colorG" to 0f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model3)

        // 描画モデル(四角形)
        val model4 = Triangle4Cube01Model()
        model4.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model4)

        // 描画モデル(四角形)
        val model5 = Triangle4Cube01Model()
        model5.createPath(mapOf(
            "colorR" to 0f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model5)

        // 描画モデル(四角形)
        val model6 = Triangle4Cube01Model()
        model6.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model6)

        modelLst.forEach { model ->
            val vao = ES32VAOIpc()
            vao.makeVIBO(model)
            vaoLst.add(vao)
        }
        */

        val colorLst =
            floatArrayOf(
                // 0:赤
                1f,0f,0f,1f,
                // 1:緑
                0f,1f,0f,1f,
                // 2:青
                0f,0f,1f,1f,
                // 3:黄色
                1f,1f,0f,1f,
                // 4:水色,
                0f,1f,1f,1f,
                // 5:紫
                1f,0f,1f,1f
            )

        (0..5).forEach {
            val model = Triangle4Cube01Model()
            model.createPath(mapOf(
                "pattern" to 2f,
                "colorR" to colorLst[it*4+0],
                "colorG" to colorLst[it*4+1],
                "colorB" to colorLst[it*4+2],
                "colorA" to colorLst[it*4+3]
            ))
            modelLst.add(model)

            val vao = ES32VAOIpc()
            vao.makeVIBO(model)
            vaoLst.add(vao)
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoLst.forEach { vao ->
            vao.deleteVIBO()
        }
        shaderSimple.deleteShader()
    }
}
