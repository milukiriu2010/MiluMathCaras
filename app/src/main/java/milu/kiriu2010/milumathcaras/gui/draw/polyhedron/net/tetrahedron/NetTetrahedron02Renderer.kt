package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.tetrahedron

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.Simple01Shader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------
// 正四面体の展開図02
// -------------------------------------
// ２面のなす角で回転の向きを変更
// -------------------------------------
// ２面のなす角
//   cos(t) = 1/3
//       t  = 70.52877951805度
// -------------------------------------
class NetTetrahedron02Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(三角形)
    private val modelLst = mutableListOf<Triangle4Tetrahedron02Model>()

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: Simple01Shader

    // 定数
    val sqrt3   = 1.73205f
    val sqrt3_2 = 0.86603f
    val sqrt3_3 = 0.57735f
    val l2_3    = 0.66667f


    var angleF = 0f
    // 109.471220482
    //  = 180 - 70.528
    var angleFDiv = 1.09471f
    var cnt = 0
    var cntMax = 100
    var cntMin = -100
    var cntDir = 1
    var still = 0

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {

            // stillが0のときだけ、
            // 展開図が開閉するようにする
            if ( still == 0 ) {
                // 正四面体を形成した場合、
                // 回転する向きを変更する
                if ( cnt >= cntMax ) {
                    cntDir = -1
                    still=1
                }
                else if ( cnt <= cntMin ) {
                    cntDir = 1
                    still=1
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
        val t0 = angleFDiv * cnt.toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,6f))
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
        Matrix.translateM(matM,0,0f,-sqrt3_3,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[0],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-sqrt3_3,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[1],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(右上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0, l2_3,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,-sqrt3,0f)
        Matrix.translateM(matM,0,-l2_3,-sqrt3_3,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[2],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(右下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-l2_3,0f,0f)
        Matrix.rotateM(matM,0,t0,1f, sqrt3,0f)
        Matrix.translateM(matM,0, l2_3,-sqrt3_3,0f)
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

        // 描画モデル(三角形)
        (1..4).forEach {
            val model = Triangle4Tetrahedron02Model()
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