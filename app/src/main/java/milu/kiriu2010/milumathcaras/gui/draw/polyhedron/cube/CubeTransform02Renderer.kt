package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.cube

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.model.d3.Cube01Model
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.wvbo.ES20VBOSimple01Shader
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// 立方体座標変換02
// -----------------------------------
// (1) Z軸に平行な４つの線を回転
// (2) X軸に平行な４つの線を回転
// -----------------------------------
class CubeTransform02Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(立方体)
    private lateinit var model: MgModelAbs

    // VBO
    private lateinit var bo: ES20VBOIpc

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        //vecEye = qtnNow.toVecIII(floatArrayOf(-15f,15f,-15f))
        vecEye = qtnNow.toVecIII(floatArrayOf(20f,0f,0f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,30f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 静止したモデルを描画
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(model,bo,matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(右上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // Z軸回転
        val matA0 = transformModel(matM, floatArrayOf(-1f,-1f,0f), floatArrayOf(0f,0f,1f,t0))
        // X軸回転
        transformModel(matA0.copyOf(), floatArrayOf(0f,-1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA0.copyOf(), floatArrayOf(0f, 1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA0.copyOf(), floatArrayOf(0f,-1f, 1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA0.copyOf(), floatArrayOf(0f, 1f, 1f), floatArrayOf(1f,0f,0f,t0))
        // ----------------------------------------------
        // 回転するモデルを描画(右下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // Z軸回転
        val matA1 = transformModel(matM, floatArrayOf(-1f, 1f,0f), floatArrayOf(0f,0f,1f,t0))
        // X軸回転
        transformModel(matA1.copyOf(), floatArrayOf(0f,-1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA1.copyOf(), floatArrayOf(0f, 1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA1.copyOf(), floatArrayOf(0f,-1f, 1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA1.copyOf(), floatArrayOf(0f, 1f, 1f), floatArrayOf(1f,0f,0f,t0))
        // ----------------------------------------------
        // 回転するモデルを描画(左上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // Z軸回転
        val matA2 = transformModel(matM, floatArrayOf( 1f,-1f,0f), floatArrayOf(0f,0f,1f,t0))
        // X軸回転
        transformModel(matA2.copyOf(), floatArrayOf(0f,-1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA2.copyOf(), floatArrayOf(0f, 1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA2.copyOf(), floatArrayOf(0f,-1f, 1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA2.copyOf(), floatArrayOf(0f, 1f, 1f), floatArrayOf(1f,0f,0f,t0))
        // ----------------------------------------------
        // 回転するモデルを描画(左下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // Z軸回転
        val matA3 = transformModel(matM, floatArrayOf( 1f, 1f,0f), floatArrayOf(0f,0f,1f,t0))
        // X軸回転
        transformModel(matA3.copyOf(), floatArrayOf(0f,-1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA3.copyOf(), floatArrayOf(0f, 1f,-1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA3.copyOf(), floatArrayOf(0f,-1f, 1f), floatArrayOf(1f,0f,0f,t0))
        transformModel(matA3.copyOf(), floatArrayOf(0f, 1f, 1f), floatArrayOf(1f,0f,0f,t0))

    }

    // モデルを座標変換し描画する
    //     matA: 変換前の座標変換行列
    //        p: 移動位置(x,y,z)
    //     axis: 回転軸(x,y,z)と回転角度
    //   return: 変換後の座標変換行列
    private fun transformModel(matA: FloatArray, p: FloatArray, axis: FloatArray ): FloatArray {
        // モデルを移動する(元の位置に戻す)
        Matrix.translateM(matA,0,-p[0],-p[1],-p[2])
        // モデルを軸を中心に回転させる
        Matrix.rotateM(matA,0,axis[3],axis[0],axis[1],axis[2])
        // モデルを移動する(軸上にずらす)
        Matrix.translateM(matA,0,p[0],p[1],p[2])
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matA,0)
        shaderSimple.draw(model,bo,matMVP)

        return matA
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20VBOSimple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(立方体)
        model = Cube01Model()
        model.createPath()

        // VBO
        bo = ES20VBOIpc()
        bo.makeVIBO(model)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        bo.deleteVIBO()
        shaderSimple.deleteShader()
    }

}