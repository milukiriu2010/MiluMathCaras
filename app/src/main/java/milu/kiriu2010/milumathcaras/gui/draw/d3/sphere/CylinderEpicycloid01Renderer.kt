package milu.kiriu2010.milumathcaras.gui.draw.d3.sphere

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.color.MgColor
import milu.kiriu2010.gui.model.d3.Cylinder01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 円柱のエピサイクロイド01(k=2.0)
// -----------------------------------
// 円柱がエピサイクロイド運動する
// -----------------------------------
// 2019.09.24
// -----------------------------------
class CylinderEpicycloid01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(円柱)
    private val models = mutableListOf<Cylinder01Model>()

    // VAO(円)
    private val vaos = mutableListOf<ES32VAOIpc>()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // エピサイクロイドの係数r
    private val r = 1f
    // エピサイクロイドの係数k
    private val k = 2f
    // エピサイクロイドの分割数
    private val splitN = 20
    // エピサイクロイドの係数t()
    private val td = 360f/splitN.toFloat()

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
        val cos0 = MyMathUtil.cosf(t0)
        val sin0 = MyMathUtil.sinf(t0)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,3f,6f))
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
        (0 until 360 step splitN).forEach { i ->
            val ii = i.toFloat()

            vaos.forEachIndexed { id, vao ->
                val t = id.toFloat() * td + t0

                val x = r * (1f+1f/k) * MyMathUtil.sinf(t) - r/k * MyMathUtil.sinf((k+1f)*t)
                val y = r * (1f+1f/k) * MyMathUtil.cosf(t) - r/k * MyMathUtil.cosf((k+1f)*t)

                Matrix.setIdentityM(matM,0)
                Matrix.rotateM(matM,0,ii,0f,1f,0f)
                Matrix.translateM(matM,0,x,y,0f)
                Matrix.rotateM(matM,0,8f*t0,0f,1f,0f)
                Matrix.rotateM(matM,0,90f,1f,0f,0f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderSimple.draw(vao,matMVP)
            }

        }
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

        (0..360 step splitN).forEach { i ->
            val colorst = MgColor.hsva(i,1f,1f,1f)
            val colorsb = MgColor.hsva(i+180,1f,1f,1f)

            // 描画モデル(円柱)
            val model = Cylinder01Model()
            model.createPath(mapOf(
                "scale" to 0.1f,
                "colorR" to colorst[0],
                "colorG" to colorst[1],
                "colorB" to colorst[2],
                "colorA" to colorst[3],
                "colorRT" to colorst[0],
                "colorGT" to colorst[1],
                "colorBT" to colorst[2],
                "colorAT" to colorst[3],
                "colorRB" to colorsb[0],
                "colorGB" to colorsb[1],
                "colorBB" to colorsb[2],
                "colorAB" to colorsb[3]
            ))

            // VAO(円)
            val vao = ES32VAOIpc()
            vao.makeVIBO(model)

            models.add(model)
            vaos.add(vao)
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaos.forEach {
            it.deleteVIBO()
        }
        shaderSimple.deleteShader()
    }

}