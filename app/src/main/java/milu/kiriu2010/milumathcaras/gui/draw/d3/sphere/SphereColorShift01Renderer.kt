package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.d3.Cylinder01Model
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VBOIpc
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 球体色シフト01
// -----------------------------------
// 2019.07.16
// -----------------------------------
class SphereColorShift01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(球体)
    private val model = Sphere01Model()

    // VBO(球体)
    private val vbo = ES32VBOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // 球体が公転する半径
    val a = 2f

    // モデルの描画パラメータ
    val row = 32
    val column = 32
    val radius = 0.5f
    val height = 1f

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,5f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 球体モデルを描画
        // ----------------------------------------------
        (0..5).forEach {
            val t = it.toFloat()*60f
            val cos = a*MyMathUtil.cosf(t+t0)
            val sin = a*MyMathUtil.sinf(t+t0)

            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,sin,0f,cos)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.drawDynamicCol(vbo,matMVP,GLES32.GL_TRIANGLES,{
                model.bufCol.position(0)
                val buf = ByteBuffer.allocateDirect(model.datCol.size*4).run {
                    order(ByteOrder.nativeOrder())

                    asFloatBuffer().apply {
                        put(model.datCol.toFloatArray())
                        position(0)
                    }
                }

                (0..row).forEach { i ->
                    var tc = MgColor.hsva(360/row*i+angle[0]+t.toInt(),1f,1f,1f)
                    (0..column).forEach { j ->
                        (0..3).forEach { k ->
                            buf.put((i*column+j)*4+k,tc[k])
                        }
                    }
                }

                buf.position(0)
                buf
            })
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
        shaderSimple.loadShaderDynamic()

        // 描画モデル(球体)
        model.createPath(mapOf(
            "row" to row.toFloat(),
            "column" to column.toFloat(),
            "radius" to radius,
            "height" to height,
            "latlng" to 2f
        ))

        // VBO(球体)
        vbo.usageCol = GLES32.GL_DYNAMIC_DRAW
        vbo.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vbo.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
