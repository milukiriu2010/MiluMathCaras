package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple01Shader
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOTexture01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpc
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpct
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
    private lateinit var model1: MgModelAbs
    // 描画モデル(立方体)(外側)
    private lateinit var model2: MgModelAbs

    // VBO(特殊効果なし)
    private lateinit var vboSimple: ES20VBOIpc
    // VBO(テクスチャ)
    private lateinit var vboTexture: ES20VBOIpct

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader
    // シェーダ(テクスチャ)
    private lateinit var shaderTexture: ES20VBOTexture01Shader

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
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,frameBuf[0])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転する立方体
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,2f*t0,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(model2,vboSimple,matMVP)

        // ------------------------------------
        // デフォルトバッファへ描画
        // ------------------------------------
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // デフォルトバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

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
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTex[0])

        // 上
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,sqrt3,0f)
        Matrix.rotateM(matM,0,-45f,0f,0f,1f)
        Matrix.rotateM(matM,0,45f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(model2,vboTexture,matMVP,0)

        // 下
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,-sqrt3,0f)
        Matrix.rotateM(matM,0,45f,0f,0f,1f)
        Matrix.rotateM(matM,0,225f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0+180f,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(model2,vboTexture,matMVP,0)

        // 右
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,sqrt3,0f,0f)
        Matrix.rotateM(matM,0,45f,0f,0f,1f)
        Matrix.rotateM(matM,0,45f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(model2,vboTexture,matMVP,0)

        // 左
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-sqrt3,0f,0f)
        Matrix.rotateM(matM,0,135f,0f,0f,1f)
        Matrix.rotateM(matM,0,225f,0f,1f,0f)
        Matrix.rotateM(matM,0,t0+180f,1f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderTexture.draw(model2,vboTexture,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,frameBuf)
        // 深度バッファ用レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,depthRenderBuf)
        // フレームバッファ用テクスチャ生成
        GLES20.glGenTextures(1,frameTex)
        MyGLES20Func.createFrameBuffer(width,height,0,frameBuf,depthRenderBuf,frameTex)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBOSimple01Shader()
        shaderSimple.loadShader()

        // シェーダ(テクスチャ)
        shaderTexture = ES20VBOTexture01Shader()
        shaderTexture.loadShader()

        // 描画モデル(立方体)(内側)
        model1 = Cube01Model()
        model1.createPath()

        // 描画モデル(立方体)(外側)
        model2 = Cube01Model()
        model2.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f
        ))

        // VBO(特殊効果なし)
        vboSimple = ES20VBOIpc()
        vboSimple.makeVIBO(model1)
        // VBO(テクスチャ)
        vboTexture = ES20VBOIpct()
        vboTexture.makeVIBO(model2)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vboSimple.deleteVIBO()
        vboTexture.deleteVIBO()
        shaderSimple.deleteShader()

        GLES20.glDeleteTextures(frameTex.capacity(),frameTex)
        GLES20.glDeleteRenderbuffers(depthRenderBuf.capacity(),depthRenderBuf)
        GLES20.glDeleteFramebuffers(frameBuf.capacity(),frameBuf)
    }

}