package milu.kiriu2010.milumathcaras.gui.draw.d3.cube

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.shader.es32.ES32Texture01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpct
import milu.kiriu2010.math.MyMathUtil
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// 立方体座標変換10
// -----------------------------------------
// 立方体の中を立方体が回転
// -----------------------------------------
// 2019.06.14
// -----------------------------------------
class CubeTransform10Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(立方体)(内側)
    private val model1 = Cube01Model()
    // 描画モデル(立方体)(外側)
    private val model2 = Cube01Model()

    // VAO(特殊効果なし)
    private val vaoSimple = ES32VAOIpc()
    // VBO(テクスチャ)
    private val vaoTexture = ES32VAOIpct()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)
    // シェーダ(テクスチャ)
    private val shaderTexture = ES32Texture01Shader(ctx)

    val sqrt3 = MyMathUtil.SQRT3

    init {
        // フレームバッファ
        frameBuf = IntBuffer.allocate(1)
        // 深度バッファ用レンダ―バッファ
        depthRenderBuf = IntBuffer.allocate(1)
        // フレームバッファ用のテクスチャ
        frameTex = IntBuffer.allocate(1)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,80f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ------------------------------------
        // フレームバッファへ描画
        // ------------------------------------
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,frameBuf[0])

        // フレームバッファを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転する立方体
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,2f*t0,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoSimple,matMVP)

        // ------------------------------------
        // デフォルトバッファへ描画
        // ------------------------------------
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,0)

        // デフォルトバッファを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,80f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // フレームバッファの内容をテクスチャとして立方体に描画
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,frameTex[0])

        // 上
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,-45f,0f,0f,1f)
        Matrix.rotateM(matM,0,45f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(vaoTexture,matMVP,0)

        // 下
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-sqrt3,0f)
        Matrix.rotateM(matM,0,45f,0f,0f,1f)
        Matrix.rotateM(matM,0,225f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0+180f,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(vaoTexture,matMVP,0)

        // 右
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,sqrt3,0f,0f)
        Matrix.rotateM(matM,0,45f,0f,0f,1f)
        Matrix.rotateM(matM,0,45f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(vaoTexture,matMVP,0)

        // 左
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-sqrt3,0f,0f)
        Matrix.rotateM(matM,0,135f,0f,0f,1f)
        Matrix.rotateM(matM,0,225f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0+180f,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(vaoTexture,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)

        // フレームバッファ生成
        GLES32.glGenFramebuffers(1,frameBuf)
        // 深度バッファ用レンダ―バッファ生成
        GLES32.glGenRenderbuffers(1,depthRenderBuf)
        // フレームバッファ用テクスチャ生成
        GLES32.glGenTextures(1,frameTex)
        MyGLES32Func.createFrameBuffer(width,height,0,frameBuf,depthRenderBuf,frameTex)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glEnable(GLES32.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        // シェーダ(テクスチャ)
        shaderTexture.loadShader()

        // 描画モデル(立方体)(内側)
        model1.createPath()

        // 描画モデル(立方体)(外側)
        model2.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f
        ))

        // VAO(特殊効果なし)
        vaoSimple.makeVIBO(model1)
        // VAO(テクスチャ)
        vaoTexture.makeVIBO(model2)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoSimple.deleteVIBO()
        vaoTexture.deleteVIBO()
        shaderSimple.deleteShader()

        GLES32.glDeleteTextures(frameTex.capacity(),frameTex)
        GLES32.glDeleteRenderbuffers(depthRenderBuf.capacity(),depthRenderBuf)
        GLES32.glDeleteFramebuffers(frameBuf.capacity(),frameBuf)
    }
}
