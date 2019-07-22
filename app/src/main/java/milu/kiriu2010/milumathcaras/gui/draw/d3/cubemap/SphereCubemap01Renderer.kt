package milu.kiriu2010.milumathcaras.gui.draw.d3.cubemap

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES32
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.model.d3.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Cubemap01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpnc
import milu.kiriu2010.milumathcaras.R
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------
// 球体:キューブ環境マップ01
// -----------------------------------
// テクスチャを表示
// -----------------------------------
// 2019.07.22
// -----------------------------------
class SphereCubemap01Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画モデル(立方体)
    private val modelCube = Cube01Model()
    // 描画モデル(球体)
    private val modelSphere = Sphere01Model()

    // VAO(立方体)
    private val vaoCube = ES32VAOIpnc()
    // VAO(球体)
    private val vaoSphere = ES32VAOIpnc()

    // シェーダ(キューブ環境マッピング)
    private val shader = ES32Cubemap01Shader(ctx)

    init {
        // テクスチャ
        textures = IntArray(1)

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
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 背景用キューブをレンダリング
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(vaoCube,matM,matMVP,vecEye,0,0)

        // 球体をレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,5f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(vaoSphere,matM,matMVP,vecEye,-1,1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)

        renderW = width
        renderH = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES32.glClearColor(1f, 1f, 1f, 1f)

        // canvasを初期化する際の深度を設定する
        GLES32.glClearDepthf(1f)

        // 深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)

        // シェーダ(キューブ環境マッピング)
        shader.loadShader()

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

        // VAO(立方体)
        vaoCube.makeVIBO(modelCube)
        // VAO(球体)
        vaoSphere.makeVIBO(modelSphere)

        // キューブマップを生成
        MyGLES32Func.generateCubeMap(textures,bmpArray)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoSphere.deleteVIBO()
        vaoCube.deleteVIBO()
        shader.deleteShader()

        GLES32.glDeleteTextures(textures.size,textures,0)
    }
}
