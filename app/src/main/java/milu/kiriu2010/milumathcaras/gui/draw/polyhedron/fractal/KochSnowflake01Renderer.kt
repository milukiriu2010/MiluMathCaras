package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.fractal

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.model.d2.KochSnowflake01Model
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
// 立方体の中をコッホ雪片が回転
// -----------------------------------------
// 2019.06.15
// -----------------------------------------
class KochSnowflake01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(コッホ雪片)
    private val modelKoch = KochSnowflake01Model()
    // 描画モデル(立方体)
    private val modelCube = Cube01Model()

    // VBO(特殊効果なし)
    private lateinit var vboSimple: ES20VBOIpc
    // VBO(テクスチャ)
    private lateinit var vboTexture: ES20VBOIpct

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader
    // シェーダ(テクスチャ)
    private lateinit var shaderTexture: ES20VBOTexture01Shader

    private val sqrt3 = MyMathUtil.SQRT3

    // スケール
    private var scaleMax = 1f
    private var scaleMin = 0f
    private var scaleNow = scaleMin
    private var scaleDv = 0.1f

    init {
        // フレームバッファ
        frameBuf = IntBuffer.allocate(1)
        // 深度バッファ用レンダ―バッファ
        depthRenderBuf = IntBuffer.allocate(1)
        // フレームバッファ用のテクスチャ
        frameTex = IntBuffer.allocate(1)

    }

    override fun onDrawFrame(gl: GL10?) {
        if ( isRunning == true ) {
            // 回転角度
            angle[0] =(angle[0]+2)%360
            scaleNow += scaleDv
            if ( scaleNow >= scaleMax ) {
                scaleNow = scaleMin
            }
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.orthoM(matP,0,-2f,2f,-2f,2f,0.1f,10f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ------------------------------------
        // フレームバッファへ描画
        // ------------------------------------
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,frameBuf[0])

        // フレームバッファを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // コッホ雪片
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,scaleNow,scaleNow,scaleNow)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        GLES20.glLineWidth(10f)
        shaderSimple.draw(modelKoch,vboSimple,matMVP,GLES20.GL_LINE_LOOP)

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

        (-1..1).forEach { j ->
            val jj = j.toFloat() * sqrt3 * 1.5f
            (-1..1).forEach { i ->
                val ii = i.toFloat() * sqrt3 * 1.5f

                Matrix.setIdentityM(matM,0)
                Matrix.translateM(matM,0,ii,jj,0f)
                Matrix.rotateM(matM,0,-45f,0f,0f,1f)
                Matrix.rotateM(matM,0,45f,0f,1f,0f)
                Matrix.rotateM(matM,0,t0,1f,1f,1f)
                Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                shaderTexture.draw(modelCube,vboTexture,matMVP,0)
            }
        }
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

        // 描画モデル(コッホ雪片)
        modelKoch.createPath()

        // 描画モデル(立方体)
        modelCube.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f
        ))

        // VBO(特殊効果なし)
        vboSimple = ES20VBOIpc()
        vboSimple.makeVIBO(modelKoch)
        // VBO(テクスチャ)
        vboTexture = ES20VBOIpct()
        vboTexture.makeVIBO(modelCube)
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