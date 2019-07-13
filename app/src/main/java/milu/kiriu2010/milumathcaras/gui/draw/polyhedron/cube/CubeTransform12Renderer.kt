package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple02Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpco
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// 立方体座標変換12
// -----------------------------------------
// 等間隔でうねる
// -----------------------------------------
// 2019.07.12
// -----------------------------------------
class CubeTransform12Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(立方体:X軸回転)
    private val model1 = Cube01Model()
    // 描画モデル(立方体:Y軸回転)
    private val model2 = Cube01Model()

    // VAO(X軸)
    private val vao1 = ES32VAOIpco()
    // VAO(Y軸)
    private val vao2 = ES32VAOIpco()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple02Shader(ctx)

    val a = 2f
    val b = 0.5f

    val cnt = 45

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+2)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,150f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,1000f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // X軸回転
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vao1,matMVP,GLES32.GL_TRIANGLES)

        // Y軸回転
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vao2,matMVP,GLES32.GL_TRIANGLES)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES32.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        // X軸を中心に回転
        (0..5).forEach { j ->
            val jj = j.toFloat()
            (-cnt..cnt).forEach { i ->
                val ii = i.toFloat()
                Matrix.setIdentityM(matM,0)
                val rx = 1f
                val ry = MyMathUtil.cosf(3f*ii+60f*jj)
                val rz = MyMathUtil.sinf(3f*ii+60f*jj)
                model1.datOff.addAll(arrayListOf(a*ii*rx,b*ii*ry,b*ii*rz))
            }
        }

        // 描画モデル(立方体:X軸)
        model1.createPath()

        // VAO:X軸
        vao1.makeVIBO(model1)

        // -------------------------------------------------------------

        // Y軸を中心に回転
        (0..5).forEach { j ->
            val jj = j.toFloat()
            (-cnt..cnt).forEach { i ->
                val ii = i.toFloat()
                Matrix.setIdentityM(matM,0)
                val rx = MyMathUtil.cosf(3f*ii+60f*jj)
                val ry = 1f
                val rz = MyMathUtil.sinf(3f*ii+60f*jj)
                model2.datOff.addAll(arrayListOf(b*ii*rx,a*ii*ry,b*ii*rz))
            }
        }

        // 描画モデル(立方体:X軸)
        model2.createPath()

        // VAO:Y軸
        vao2.makeVIBO(model2)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vao1.deleteVIBO()
        vao2.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
