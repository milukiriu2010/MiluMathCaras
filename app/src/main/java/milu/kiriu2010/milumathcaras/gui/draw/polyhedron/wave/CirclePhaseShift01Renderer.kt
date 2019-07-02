package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.wave

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d2.Circle01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpc
import milu.kiriu2010.math.MyMathUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// 円の位相をずらして描画
// -----------------------------------------
// 2019.06.15
// -----------------------------------------
class CirclePhaseShift01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(円)
    private val model = Circle01Model()

    // VBO(特殊効果なし)
    private lateinit var vboSimple: ES20VBOIpc

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader

    // 位相角度
    private var angleMax = 45
    private var angleMin = -45
    private var angleDv = 1

    // 円の分割数
    val row = 64f
    // 北緯45度/南緯45度で位相のずれが最大となるように8で割っている
    val ta = 90f/(row/8)

    override fun onDrawFrame(gl: GL10?) {
        if ( isRunning == true ) {
            // 位相角度
            angle[0] = angle[0]+angleDv
            if ( angle[0] > angleMax ) {
                angleDv = -1
                angle[0] = angleMax
            }
            else if ( angle[0] < angleMin ) {
                angleDv = 1
                angle[0] = angleMin
            }
            // 回転角度
            angle[1] = (angle[1]+1)%360
        }
        val t0 = angle[0].toFloat()
        val t1 = angle[1].toFloat()


        // デフォルトバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,3f,3f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,10f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        (0..360 step 45).forEach { i ->
            Matrix.setIdentityM(matM,0)
            Matrix.rotateM(matM,0,t1+i.toFloat(),0f,1f,0f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            GLES20.glLineWidth(10f)
            shaderSimple.drawDynamicPos(model,vboSimple,matMVP,GLES20.GL_LINE_LOOP, {
                model.bufPos.position(0)
                val buf = ByteBuffer.allocateDirect(model.datPos.toArray().size*4).run {
                    order(ByteOrder.nativeOrder())

                    asFloatBuffer().apply {
                        put(model.datPos.toFloatArray())
                        position(0)
                    }
                }

                (0..row.toInt()).forEach { ii ->
                    val t = t0*MyMathUtil.sinf(ta*ii.toFloat())
                    val cos = MyMathUtil.cosf(t)
                    val sin = MyMathUtil.sinf(t)
                    val jj = 3*ii
                    val x = model.datPos[jj]
                    val y = model.datPos[jj+1]
                    val z = model.datPos[jj+2]
                    buf.put(jj,x*cos-z*sin)
                    buf.put(jj+1,y)
                    buf.put(jj+2,x*sin+z*cos)
                }

                buf.position(0)
                buf
            })
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBOSimple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(円)
        model.createPath(mapOf(
            "row" to row,
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f
        ))

        // VBO(特殊効果なし)
        vboSimple = ES20VBOIpc()
        vboSimple.usagePos = GLES20.GL_DYNAMIC_DRAW
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