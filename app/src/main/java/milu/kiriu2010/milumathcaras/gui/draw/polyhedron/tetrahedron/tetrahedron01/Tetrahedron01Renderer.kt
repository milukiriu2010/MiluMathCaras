package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.tetrahedron.tetrahedron01

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.gui.draw.MgRenderer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Tetrahedron01Renderer(ctx: Context): MgRenderer(ctx) {

    // シェーダ
    private lateinit var shader: Tetrahedron01Shader
    // 描画モデル
    private lateinit var model: Tetrahedron01Model

    // モデル変換行列
    private val matM = FloatArray(16)
    // モデル変換行列の逆行列
    private val matI = FloatArray(16)
    // ビュー変換行列
    private val matV = FloatArray(16)
    // プロジェクション変換行列
    private val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    private val matMVP = FloatArray(16)
    // テンポラリ行列
    private val matT = FloatArray(16)
    // 点光源の位置
    private val vecLight = floatArrayOf(0f,0f,3f)
    // 環境光の色
    private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private val vecEye = floatArrayOf(0f,0f,5f)
    // カメラの上方向を表すベクトル
    private val vecEyeUp = floatArrayOf(0f,1f,0f)
    // カメラが見ている位置
    private val vecCenter = floatArrayOf(0f,0f,0f)


    // 回転角度
    private var angle1 = 0

    override fun onDrawFrame(gl: GL10) {
        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning ) {
            angle1 = (angle1 + 1) % 360
        }
        val t1 = angle1.toFloat()

        val x = MyMathUtil.cosf(t1)
        val y = MyMathUtil.sinf(t1)

        // ビュー×プロジェクション
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルを平行移動する
        Matrix.translateM(matM,0,x,y,0f)
        // モデルを"Y軸"を中心に回転する
        Matrix.rotateM(matM,0,t1,0f,1f,0f)
        // モデルを"X軸45度Y軸45度/Z軸45度"を中心に回転する
        //Matrix.rotateM(matM,0,t1,1f,1f,1f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // 描画
        shader.drawObj(model,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)

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

        // シェーダプログラム登録
        shader = Tetrahedron01Shader()
        shader.loadShader()

        // モデル生成
        model = Tetrahedron01Model()
        model.createPath()
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        shader.deleteShader()
    }

}