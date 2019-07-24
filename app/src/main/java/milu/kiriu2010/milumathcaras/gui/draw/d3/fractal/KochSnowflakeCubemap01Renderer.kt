package milu.kiriu2010.milumathcaras.gui.draw.d3.fractal

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.model.d2.Board01Model
import milu.kiriu2010.gui.model.d2.KochSnowflake01Model
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Cubemap01Shader
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.shader.es32.ES32Texture01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpct
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpnc
import milu.kiriu2010.milumathcaras.R
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// コッホ雪片inキューブ環境マップ01
// -----------------------------------
// テクスチャを表示
// -----------------------------------
// 2019.07.24
// -----------------------------------
class KochSnowflakeCubemap01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(立方体)
    private val modelCube = Cube01Model()
    // 描画モデル(球体)
    private val modelSphere = Sphere01Model()
    // 描画モデル(コッホ雪片)
    private val modelKoch = KochSnowflake01Model()
    // 描画モデル(板ポリゴン)
    private val modelBoard = Board01Model()

    // VAO(立方体)
    private val vaoCube = ES32VAOIpnc()
    // VAO(球体)
    private val vaoSphere = ES32VAOIpnc()
    // VAO(コッホ雪片)
    private val vaoKoch = ES32VAOIpc()
    // VAO(板ポリゴン)
    private val vaoBoard = ES32VAOIpct()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)
    // シェーダ(キューブ環境マッピング)
    private val shaderCubemap = ES32Cubemap01Shader(ctx)
    // シェーダ(テクスチャ)
    private val shaderTexture = ES32Texture01Shader(ctx)

    // キューブ環境マッピング用テクスチャ
    val cubeTextures = IntArray(1)

    // カメラが見る方向を表すベクトル
    val camDir = floatArrayOf(
        // GL_TEXTURE_CUBE_MAP_POSITIVE_X
         1f,  0f,  0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Y
         0f,  1f,  0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Z
         0f,  0f,  1f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_X
        -1f,  0f,  0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
         0f, -1f,  0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
         0f,  0f, -1f
    )
    // カメラの上方向を表すベクトル
    val camUp = floatArrayOf(
        // GL_TEXTURE_CUBE_MAP_POSITIVE_X
        0f, -1f,  0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Y
        0f,  0f,  1f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Z
        0f, -1f,  0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_X
        0f, -1f,  0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
        0f,  0f, -1f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
        0f, -1f,  0f
    )
    // 板ポリゴンの座標
    val objPos = floatArrayOf(
        // GL_TEXTURE_CUBE_MAP_POSITIVE_X
         15f,  0f,  0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Y
          0f, 15f,  0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Z
          0f,  0f, 15f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_X
        -15f,  0f,  0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
          0f,-15f,  0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
          0f,  0f,-15f
    )
    // 板ポリゴンの回転
    val objRot = floatArrayOf(
        // GL_TEXTURE_CUBE_MAP_POSITIVE_X
        -90f, 0f,  1f, 0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Y
         90f, 1f,  0f, 0f,
        // GL_TEXTURE_CUBE_MAP_POSITIVE_Z
        180f, 0f,  0f, 1f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_X
         90f, 0f,  1f, 0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
        -90f, 1f,  0f, 0f,
        // GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
          0f, 0f,  0f, 1f
    )

    // スケール(コッホ雪片)
    private var scaleMax = 1f
    private var scaleMin = 0f
    private var scaleNow = scaleMin
    private var scaleDv = 0.01f

    // キューブマップ用のターゲットを格納する配列
    val targetArray = arrayListOf<Int>(
        GLES32.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
        GLES32.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
        GLES32.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
        GLES32.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
        GLES32.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
        GLES32.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    )

    init {
        // テクスチャ
        textures = IntArray(1)
        // フレームバッファ
        frameBuf = IntBuffer.allocate(2)
        // 深度バッファ用レンダ―バッファ
        depthRenderBuf = IntBuffer.allocate(2)
        // フレームバッファ用のテクスチャ
        frameTex = IntBuffer.allocate(2)

        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_rabbit)
        val bmp1 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_rabbit)
        val bmp2 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_rabbit)
        val bmp3 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_rabbit)
        val bmp4 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_rabbit)
        val bmp5 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_rabbit)
        bmpArray.add(bmp0)
        bmpArray.add(bmp1)
        bmpArray.add(bmp2)
        bmpArray.add(bmp3)
        bmpArray.add(bmp4)
        bmpArray.add(bmp5)
    }

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
            scaleNow += scaleDv
            if ( scaleNow >= scaleMax ) {
                scaleNow = scaleMin
            }
        }
        val t0 = angle[0].toFloat()

        // -----------------------------------------
        // 通常フレームバッファにコッホ雪片を描画
        // -----------------------------------------
        drawKoch()

        // キューブ環境マッピングをフレームバッファにレンダリング
        drawCubemap()

        // デフォルトバッファへ描画
        drawDefault()
    }

    // 通常フレームバッファにコッホ雪片を描画
    private fun drawKoch() {
        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.orthoM(matP,0,-2f,2f,-2f,2f,0.1f,20f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ------------------------------------
        // フレームバッファへ描画
        // ------------------------------------
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,frameBuf[1])

        // フレームバッファを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // コッホ雪片
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,scaleNow,scaleNow,scaleNow)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        GLES32.glLineWidth(10f)
        shaderSimple.draw(vaoKoch,matMVP,GLES32.GL_LINE_LOOP)
    }

    // キューブ環境マッピングをフレームバッファにレンダリング
    private fun drawCubemap() {
        // フレームバッファをバインド
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,frameBuf[0])

        targetArray.forEachIndexed { id, target ->
            // フレームバッファにテクスチャを関連付ける
            GLES32.glFramebufferTexture2D(GLES32.GL_FRAMEBUFFER,GLES32.GL_COLOR_ATTACHMENT0,target,frameTex[0],0)

            // フレームバッファを初期化
            GLES32.glClearColor(1f, 1f, 1f, 1f)
            GLES32.glClearDepthf(1f)
            GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

            // -------------------------------------------------------
            // キューブマップテクスチャは、
            // 原点から６方向を継ぎ目なくつながる形で撮影する
            // ・カメラを原点におく
            // ・カメラの上方向を撮影方向に応じて適切に設定
            // ・アスペクト比が1.0で画角を90度にする
            // -------------------------------------------------------

            // カメラからみた
            // ビュー×プロジェクション座標変換行列
            Matrix.setLookAtM(matV,0,
                0f,0f,0f,
                camDir[0+id*3], camDir[1+id*3], camDir[2+id*3],
                camUp[0+id*3], camUp[1+id*3], camUp[2+id*3]
            )
            Matrix.perspectiveM(matP,0,90f,1f,0.1f,200f)
            Matrix.multiplyMM(matVP,0,matP,0,matV,0)

            // キューブマップテクスチャで背景用キューブをレンダリング
            GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
            GLES32.glBindTexture(GLES32.GL_TEXTURE_CUBE_MAP, cubeTextures[0])
            Matrix.setIdentityM(matM,0)
            Matrix.scaleM(matM,0,100f,100f,100f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderCubemap.draw(vaoCube,matM,matMVP, floatArrayOf(0f,0f,0f),0,0)

            // コッホ雪片のテクスチャを板ポリゴンにレンダリング
            GLES32.glActiveTexture(GLES32.GL_TEXTURE1)
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, frameTex[1])
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,objPos[0+id*3],objPos[1+id*3],objPos[2+id*3])
            Matrix.rotateM(matM,0,objRot[0+id*4],objRot[1+id*4],objRot[2+id*4],objRot[3+id*4])
            Matrix.scaleM(matM,0,20f,20f,20f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderTexture.draw(vaoBoard,matMVP,1)
        }
    }

    // デフォルトバッファへ描画
    private fun drawDefault() {
        // フレームバッファのバインドを解除
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,0)

        // フレームバッファを初期化
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
        Matrix.perspectiveM(matP,0,90f,1f,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // キューブマップテクスチャで背景用キューブをレンダリング
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_CUBE_MAP, cubeTextures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderCubemap.draw(vaoCube,matM,matMVP, vecEye,0,0)

        // コッホ雪片のテクスチャを板ポリゴンにレンダリング
        GLES32.glActiveTexture(GLES32.GL_TEXTURE1)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, frameTex[1])
        (0..5).forEach { id ->
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,objPos[0+id*3],objPos[1+id*3],objPos[2+id*3])
            Matrix.rotateM(matM,0,objRot[0+id*4],objRot[1+id*4],objRot[2+id*4],objRot[3+id*4])
            Matrix.scaleM(matM,0,20f,20f,20f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderTexture.draw(vaoBoard,matMVP,1)
        }

        // キューブマップテクスチャで球体をレンダリング
        GLES32.glActiveTexture(GLES32.GL_TEXTURE2)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_CUBE_MAP,frameTex[0])
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderCubemap.draw(vaoSphere,matM,matMVP,vecEye,2,1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)

        renderW = width
        renderH = height

        // フレームバッファ生成
        GLES32.glGenFramebuffers(2,frameBuf)
        // 深度バッファ用レンダ―バッファ生成
        GLES32.glGenRenderbuffers(2,depthRenderBuf)
        // フレームバッファ用テクスチャ生成
        GLES32.glGenTextures(2,frameTex)
        // キューブマップ用のフレームバッファを生成
        MyGLES32Func.createFrameBuffer4CubeMap(renderW,renderH,0,frameBuf,depthRenderBuf,frameTex)
        // 通常のフレームバッファを生成
        MyGLES32Func.createFrameBuffer(renderW,renderH,1,frameBuf,depthRenderBuf,frameTex)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        // シェーダ(キューブ環境マッピング)
        shaderCubemap.loadShader()

        // シェーダ(テクスチャ)
        shaderTexture.loadShader()

        // 描画モデル(立方体)
        modelCube.createPath(mapOf(
            "pattern" to 2f,
            "scale"   to 2f,
            "colorR"  to 1f,
            "colorG"  to 1f,
            "colorB"  to 1f,
            "colorA"  to 1f
        ))

        // 描画モデル(球体)
        modelSphere.createPath(mapOf(
            "row"     to 32f,
            "column"  to 32f,
            "radius"  to 2.5f,
            "colorR"  to 1f,
            "colorG"  to 1f,
            "colorB"  to 1f,
            "colorA"  to 1f
        ))

        // 描画モデル(コッホ雪片)
        modelKoch.createPath()

        // 描画モデル(板ポリゴン)
        modelBoard.createPath(mapOf(
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f
        ))

        // VAO(立方体)
        vaoCube.makeVIBO(modelCube)
        // VAO(球体)
        vaoSphere.makeVIBO(modelSphere)
        // VAO(コッホ雪片)
        vaoKoch.makeVIBO(modelKoch)
        // VAO(板ポリゴン)
        vaoBoard.makeVIBO(modelBoard)

        // キューブマップを生成
        MyGLES32Func.generateCubeMap(cubeTextures,bmpArray)

        // 視点位置
        vecEye[0] =  0f
        vecEye[1] =  0f
        vecEye[2] = 20f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoSphere.deleteVIBO()
        vaoCube.deleteVIBO()
        vaoKoch.deleteVIBO()
        vaoBoard.deleteVIBO()
        shaderCubemap.deleteShader()
        shaderSimple.deleteShader()
        shaderTexture.deleteShader()

        GLES32.glDeleteTextures(textures.size,textures,0)
        GLES32.glDeleteTextures(cubeTextures.size,cubeTextures,0)
    }
}
