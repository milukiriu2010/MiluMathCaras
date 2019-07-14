package milu.kiriu2010.milumathcaras.gui.draw.d3.wave.floor

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Line02Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VBOIpc
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs

// -----------------------------------------
// 波うつ床02
// -----------------------------------------
// 2019.07.14
// -----------------------------------------
class WaveFloor02Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(サイン波)
    private val model = Line02Model()

    // VBO(特殊効果なし)
    private val vboSimple = ES32VBOIpc()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    private lateinit var hs: FloatArray

    init {
        hs = FloatArray(21)
        (-10..10).forEach { i ->
            val ii = i.toFloat()
            hs[i+10] = 3f*ii
        }

    }

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
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,15f,30f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        GLES32.glLineWidth(10f)

        // X軸に平行に並べる
        (-10..10 step 2).forEach { j ->
            val jj = j.toFloat()
            Matrix.setIdentityM(matM,0)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.drawDynamicPos(vboSimple,matMVP,GLES32.GL_LINE_STRIP, {
                model.bufPos.position(0)
                val buf = ByteBuffer.allocateDirect(model.datPos.size*4).run {
                    order(ByteOrder.nativeOrder())

                    asFloatBuffer().apply {
                        put(model.datPos.toFloatArray())
                        position(0)
                    }
                }

                (-72..72).forEach { i ->
                    val ii = i.toFloat()
                    val i2 = i+72
                    buf.put(3*i2,ii*5f/36f)
                    buf.put(3*i2+1,MyMathUtil.sinf(hs[(abs(i)+angle[0])%hs.size]+t0))
                    //buf.put(3*i2+1,MyMathUtil.sinf(hs[abs(i)%hs.size]+t0))
                    buf.put(3*i2+2,jj)
                }

                buf.position(0)
                buf
            })
        }

        // Z軸に平行に並べる
        (-10..10 step 2).forEach { j ->
            val jj = j.toFloat()
            Matrix.setIdentityM(matM,0)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderSimple.drawDynamicPos(vboSimple,matMVP,GLES32.GL_LINE_STRIP, {
                model.bufPos.position(0)
                val buf = ByteBuffer.allocateDirect(model.datPos.size*4).run {
                    order(ByteOrder.nativeOrder())

                    asFloatBuffer().apply {
                        put(model.datPos.toFloatArray())
                        position(0)
                    }
                }

                (-72..72).forEach { i ->
                    val ii = i.toFloat()
                    val i2 = i+72
                    buf.put(3*i2,jj)
                    buf.put(3*i2+1,MyMathUtil.sinf(hs[(abs(i)+angle[0])%hs.size]+t0))
                    buf.put(3*i2+2,ii*5f/36f)
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
        shaderSimple.loadShaderDynamicPos()

        // 描画モデル(サイン波)
        model.createPath()

        // VBO(特殊効果なし)
        vboSimple.usagePos = GLES32.GL_DYNAMIC_DRAW
        vboSimple.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vboSimple.deleteVIBO()
        shaderSimple.deleteShader()
    }
}
