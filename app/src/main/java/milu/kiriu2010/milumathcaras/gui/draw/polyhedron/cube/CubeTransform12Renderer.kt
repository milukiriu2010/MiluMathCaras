package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple02Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpco
import milu.kiriu2010.gui.vbo.es32.ES32VBOIpco
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
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

    // 描画モデル(立方体)
    private val model = Cube01Model()

    // VAO
    private val vao = ES32VAOIpco()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple02Shader(ctx)

    val a = 2f

    val cnt = 10

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
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,100f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vao,matMVP,GLES32.GL_TRIANGLES)
        /*
        shaderSimple.drawDynamicOff(vao,matMVP,GLES32.GL_TRIANGLES,{
            model.bufOff.position(0)
            val buf = ByteBuffer.allocateDirect(model.datOff.toArray().size*4).run {
                order(ByteOrder.nativeOrder())

                asFloatBuffer().apply {
                    put(model.datOff.toFloatArray())
                    position(0)
                }
            }

            (-cnt..cnt).forEach {
                val ii = it.toFloat()
                val offX = a * ii
                val offY = a * ii * MyMathUtil.cosf(t0+ii)
                val offZ = a * ii * MyMathUtil.sinf(t0+ii)
                buf.put(it+cnt,offX)
                buf.put(it+cnt+1,offY)
                buf.put(it+cnt+2,offZ)
            }

            buf.position(0)
            buf
        })
        */
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
        //shaderSimple.loadShaderDynamic()
        shaderSimple.loadShader()

        // モデルにオフセットを設定
        (-cnt..cnt).forEach {
            val off = a * it.toFloat()
            model.datOff.addAll(arrayListOf(off,0f,0f))
        }

        // 描画モデル(立方体)
        model.createPath()

        // VAO
        //vao.usageOff = GLES32.GL_DYNAMIC_DRAW
        vao.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vao.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
