package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.glsl

import android.content.Context
import android.opengl.GLES32
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.os.SystemClock
import milu.kiriu2010.gui.model.d2.Board01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32GLSL01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIp
import milu.kiriu2010.milumathcaras.R

// ----------------------------------------------
// GLSLの内容を描く
// ----------------------------------------------
// 2019.06.26
// ----------------------------------------------
class GLSL01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル
    private val model = Board01Model()

    // VAO
    private val vao = ES32VAOIp()

    // シェーダ
    private val shader = ES32GLSL01Shader(ctx)

    // 時間管理
    private var startTime = SystemClock.uptimeMillis()
    // サンプルが動作する際に、どの程度時間が経過しているのかをシェーダに渡す
    private var u_time = 0f

    // アニメーションのスピード
    var u_speed = 5f

    // 同心円の間隔
    var u_gap = 5f

    override fun onDrawFrame(gl: GL10) {
        // canvasを初期化
        // canvasを初期化する色を設定する
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)

        // サンプルが動作する際に、どの程度時間が経過しているのかをシェーダに渡す
        u_time = (SystemClock.uptimeMillis() - startTime).toFloat() * 0.001f

        // 描画
        shader.draw(vao,
                u_time,
                floatArrayOf(touchP.x,touchP.y),
                floatArrayOf(renderW.toFloat(),renderH.toFloat())
        )
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)

        renderW = width
        renderH = height

        startTime = SystemClock.uptimeMillis()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {

        // シェーダ
        shader.loadShader()

        // モデル生成
        model.createPath(mapOf(
                "pattern" to 1f
        ))

        // VBO
        vao.makeVIBO(model)

        // タッチ位置
        // 左上が原点で0.0～1.0とする
        touchP.also {
            it.x = 0.5f
            it.y = 0.5f
        }
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
        /*
        shader.sfResId = motionParam["shader"]?.toInt() ?: -1
        // app\build\generated\not_namespaced_r_class_sources\debug\processDebugResources\r\milu\kiriu2010\milumathcaras
        // 0x7f0d0000<=>2131558400
        Log.d(javaClass.simpleName,"sfResId[${shader.sfResId}]")
        Log.d(javaClass.simpleName,"cir:${R.raw.es32_1600}")
        Log.d(javaClass.simpleName,"orb:${R.raw.es32_1601}")
        */
        val id = motionParam["shader"]?.toInt() ?: -1
        shader.sfResId = when (id) {
            1600 -> R.raw.es32_1600
            1601 -> R.raw.es32_1601
            1602 -> R.raw.es32_1602
            else -> -1
        }
    }

    override fun closeShader() {
        vao.deleteVIBO()
        shader.deleteShader()
    }
}
