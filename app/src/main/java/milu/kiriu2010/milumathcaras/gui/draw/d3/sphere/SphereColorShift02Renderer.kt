package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyPointF
import milu.kiriu2010.gui.color.MgColor
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
// 球体色シフト02
// -----------------------------------
// 2019.07.20
// -----------------------------------
class SphereColorShift02Renderer(ctx: Context): MgRenderer(ctx) {

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

    // レベル(最大)
    val lvMax = 3
    // レベル(シフト)
    val lvSft = 360/(lvMax+1)

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
            angle[1] =(angle[1]+5)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,10f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)




        // レベル
        (0..lvMax).forEach { lv0 ->
            // レベル１以上
            if (lv0 > 0) {
                val lv0f = lv0.toFloat()

                // 正六角形の頂点位置に並べる
                (0..5).forEach { lv1 ->
                    val lv1f0 = lv1.toFloat()*60f
                    val sx0 = lv0f*a*MyMathUtil.cosf(lv1f0)
                    val sy0 = lv0f*a*MyMathUtil.sinf(lv1f0)
                    draw(sx0,sy0,lv0*lvSft,t0)

                    // レベル２以上
                    // 正六角形の辺上に並べる
                    val lv1f1 = lv1f0+60f
                    val sx1 = lv0f*a*MyMathUtil.cosf(lv1f1)
                    val sy1 = lv0f*a*MyMathUtil.sinf(lv1f1)
                    val p0 = MyPointF(sx0,sy0)
                    val p1 = MyPointF(sx1,sy1)
                    (1..(lv0-1)).forEach { sft ->
                        val sftf = sft.toFloat()
                        val p = p0.lerp(p1,sftf,lv0f-sftf)
                        draw(p.x,p.y,lv0*lvSft,t0)
                    }
                }

            }
            // レベル０
            else {
                draw(0f,0f,lv0,t0)
            }
        }
    }

    // 球体を描画
    private fun draw(cos: Float, sin: Float, sft: Int,t0: Float) {
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
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
                var tc = MgColor.hsva(360/row*i+angle[1]+sft,1f,1f,1f)
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
