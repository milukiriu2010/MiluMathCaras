package milu.kiriu2010.milumathcaras.gui.draw.polyhedron

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.model.Octahedron01Model
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.renderer.Tetrahedron01Model
import milu.kiriu2010.gui.shader.DirectionalLight01Shader
import milu.kiriu2010.gui.shader.Simple01Shader
import milu.kiriu2010.gui.shader.Tetrahedron01Shader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Polyhedron01Renderer(ctx: Context): MgRenderer(ctx) {

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: Simple01Shader
    // シェーダ(平行光源)
    private lateinit var shaderDirectionalLight: DirectionalLight01Shader

    // 描画モデル
    private lateinit var model: MgModelAbs
    private var modelType = -1

    // スケール
    private var scale = 1f

    // 描画に利用するデータを設定する
    override fun setMotionParam( vararg values: Float ) {
        values.forEachIndexed { id, fl ->
            when (id) {
                0 -> modelType = fl.toInt()
                1 -> scale = fl
            }
        }
    }

    override fun onDrawFrame(gl: GL10) {
        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning ) {
            angle[0] = (angle[0] + 1) % 360
        }
        val t0 = angle[0].toFloat()

        val x = MyMathUtil.cosf(t0)
        val y = MyMathUtil.sinf(t0)

        // ビュー×プロジェクション
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを平行移動する
        Matrix.translateM(matM,0,x,y,0f)
        // モデルを"Y軸"を中心に回転する
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        // モデルを"X軸45度Y軸45度/Z軸45度"を中心に回転する
        //Matrix.rotateM(matM,0,t1,1f,1f,1f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // 描画
        // モデルを描画
        when (shaderSwitch) {
            0 -> shaderSimple.draw(model,matMVP)
            1 -> shaderDirectionalLight.draw(model,matMVP,matI,vecLight)
            else -> shaderSimple.draw(model,matMVP)
        }

        //Log.d(javaClass.simpleName, Arrays.toString(matMVP))
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])

        // シェーダプログラム登録(特殊効果なし)
        shaderSimple = Simple01Shader()
        shaderSimple.loadShader()

        // シェーダプログラム登録(平行光源)
        shaderDirectionalLight = DirectionalLight01Shader()
        shaderDirectionalLight.loadShader()


        // モデル生成
        model = when (modelType) {
            // 正四面体
            0 -> Tetrahedron01Model()
            // 立方体
            1 -> Cube01Model()
            // 正八面体
            2 -> Octahedron01Model()
            else -> Tetrahedron01Model()
        }
        model.createPath( mapOf("scale" to scale) )
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        shaderSimple.deleteShader()

    }

}