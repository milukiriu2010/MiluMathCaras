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

// 立方体座標変換07
// -----------------------------------------
// ５×５
//   １つが中央で回転
// 　⇔
// 　分身が周りで回転
// -----------------------------------------
// 2019.06.06
// -----------------------------------------
class CubeTransform07Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(立方体)
    private lateinit var model: MgModelAbs

    // VBO
    private lateinit var bo: ES20VBOIpc

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20VBOSimple01Shader

    private enum class Mode {
        SELF,
        ALTER_EGO,
        EXPAND,
        SHRINK
    }

    private var modeNow = Mode.SELF

    private var cnt = 0
    private var cntDv = 4

    val a = 5f

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {
            angle[0] =(angle[0]+2)%360
            cnt = (cnt+cntDv)%360
            if ( cnt == 0 ) {
                when (modeNow) {
                    Mode.SELF -> {
                        // 拡大するときは、ゆっくりめ
                        cntDv = 40
                        modeNow = Mode.EXPAND
                    }
                    Mode.ALTER_EGO -> {
                        // 縮小するときは、はやめ
                        cntDv = 60
                        modeNow = Mode.SHRINK
                    }
                    Mode.EXPAND -> {
                        // 分裂したときは長め
                        cntDv = 2
                        modeNow = Mode.ALTER_EGO
                    }
                    Mode.SHRINK -> {
                        // 固まったときは短め
                        cntDv = 4
                        modeNow = Mode.SELF
                    }
                }
            }
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,10f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        when (modeNow) {
            // ----------------------------------------------
            // １つが中央で回転
            // ----------------------------------------------
            Mode.SELF -> {
                transformModel(1f,t0)
            }
            // ----------------------------------------------
            // 分身が周りで回転
            // ----------------------------------------------
            Mode.ALTER_EGO -> {
                transformModel(a+1f,t0)
            }
            // ----------------------------------------------
            // 拡大
            // ----------------------------------------------
            Mode.EXPAND -> {
                val d = a*cnt.toFloat()/360f+1f
                transformModel(d,t0)
            }
            // ----------------------------------------------
            // 縮小
            // ----------------------------------------------
            Mode.SHRINK -> {
                val d = a*(360-cnt).toFloat()/360f+1f
                transformModel(d,t0)
            }
        }

    }

    // モデルを座標変換し描画する
    //   d: 移動距離
    //   t: 回転角度
    private fun transformModel(d: Float, t: Float) {
        (-2..2).forEach { i ->
            (-2..2).forEach { j ->
                (-2..2).forEach { k ->
                    val ii = d*i.toFloat()
                    val jj = d*j.toFloat()
                    val kk = d*k.toFloat()
                    Matrix.setIdentityM(matM,0)
                    Matrix.scaleM(matM,0,0.333f,0.333f,0.333f)
                    Matrix.rotateM(matM,0,t,1f,0f,0f)
                    Matrix.translateM(matM,0,ii,jj,kk)
                    Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
                    shaderSimple.draw(model,bo,matMVP)
                }
            }
        }
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