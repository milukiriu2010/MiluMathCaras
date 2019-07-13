package milu.kiriu2010.milumathcaras.gui.draw.d3.net.icosahedron

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------
// 正二十面体の展開図01
// -------------------------------------
// ２面のなす角で回転の向きを変更
// -------------------------------------
// ２面のなす角
//   cos(t) = -sqrt(5)/3
//       t  = 138.189685256度
// -------------------------------------
// 2019.06.09
// -------------------------------------
class NetIcosahedron01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(三角形)
    private val modelLst = mutableListOf<Triangle4Icosahedron01Model>()

    // VAO
    private var vaoLst = mutableListOf<ES32VAOIpc>()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // 定数
    val sqrt3   = 1.73205f
    val l2_3    = 0.66667f

    // 41.810314744
    //  = 180 - 138.189685256
    var angleFDiv0 = 0.41810314744f
    var cnt = 0
    var cntMax = 100
    var cntMin = -100
    var cntDir = 1
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
                // 正八面体を形成した場合、
                // 回転する向きを変更する
                if ( cnt >= cntMax ) {
                    cntDir = -1
                    still=1
                }
                else if ( cnt <= cntMin ) {
                    cntDir = 1
                    still=1
                }
                else if ( cnt == 0 ) {
                    still = 1
                }
                else {
                    cnt += cntDir
                }
            }
            else {
                still = (still+1)%30
                if ( still == 0 ) {
                    cnt += cntDir
                }
            }
        }
        val t0 = angleFDiv0 * cnt.toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,17f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 回転するモデルを描画(０：上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１：下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(２：右←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(３：左←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(４←２←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(５←３←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(６←４←２←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[6],matMVP)

        // --------------------------------------------------------
        // 回転するモデルを描画(７←５←３←０)
        // --------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[7],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(８←２←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[8],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(９←３←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[9],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１０←４←２←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[10],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１１←５←３←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[11],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１２←１０←４←２←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[12],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１３←１１←５←３←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[13],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１４←１０←４←２←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,2f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[14],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(１５←１１←５←３←０)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-2f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[15],matMVP)

        // ------------------------------------------------
        // 回転するモデルを描画(１６←１４←１０←４←２←０)
        // ------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,2f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[16],matMVP)

        // ------------------------------------------------
        // 回転するモデルを描画(１７←１５←１１←５←３←０)
        // ------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-2f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[17],matMVP)

        // ------------------------------------------------
        // 回転するモデルを描画(１８←１５←１１←５←３←０)
        // ------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-2f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[18],matMVP)

        // ------------------------------------------------------
        // 回転するモデルを描画(１９←１８←１５←１１←５←３←０)
        // ------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-2f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.rotateM(matM,0,-t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,t0,1f,sqrt3,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[19],matMVP)
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

        val paramLst = mutableListOf<Float>(
            1f,    1f,    0f,    0f, 1f,  //  0
            2f,    1f, 0.25f,    0f, 1f,  //  1
            2f,    1f,  0.5f,    0f, 1f,  //  2
            2f,    1f, 0.75f,    0f, 1f,  //  3
            1f,    1f,    1f,    0f, 1f,  //  4
            1f, 0.75f,    1f,    0f, 1f,  //  5
            2f,  0.5f,    1f,    0f, 1f,  //  6
            2f, 0.25f,    1f,    0f, 1f,  //  7
            1f,    0f,    1f,    0f, 1f,  //  8
            1f,    0f,    1f, 0.25f, 1f,  //  9
            2f,    0f,    1f,  0.5f, 1f,  // 10
            2f,    0f,    1f, 0.75f, 1f,  // 11
            1f,    0f,    1f,    1f, 1f,  // 12
            1f,    0f, 0.75f,    1f, 1f,  // 13
            1f,    0f,  0.5f,    1f, 1f,  // 14
            1f,    0f, 0.25f,    1f, 1f,  // 15
            2f,    0f,    0f,    1f, 1f,  // 16
            2f, 0.25f,    0f,    1f, 1f,  // 17
            2f,  0.5f,    0f,    1f, 1f,  // 18
            1f, 0.75f,    0f,    1f, 1f   // 19
        )

        (0..19).forEach {
            val model = Triangle4Icosahedron01Model()
            model.createPath(mapOf(
                "pattern" to paramLst[5*it],
                "colorR" to paramLst[5*it+1],
                "colorG" to paramLst[5*it+2],
                "colorB" to paramLst[5*it+3],
                "colorA" to paramLst[5*it+4]
            ))
            modelLst.add(model)
        }

        modelLst.forEach { model ->
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
