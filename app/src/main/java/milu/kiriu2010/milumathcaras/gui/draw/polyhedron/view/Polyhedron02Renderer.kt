package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.view

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.*
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.renderer.Tetrahedron01Model
import milu.kiriu2010.gui.shader.es20.*
import milu.kiriu2010.gui.shader.es20.nvbo.*
import milu.kiriu2010.gui.shader.es20.wvbo.*
import milu.kiriu2010.gui.vbo.es20.*
import milu.kiriu2010.milumathcaras.R
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ------------------------------------
// 多面体のビューを描画
// ------------------------------------
// 2019.06.03 VBO
// ------------------------------------
class Polyhedron02Renderer(ctx: Context): MgRenderer(ctx) {

    // シェーダ(モデル描画)
    private lateinit var shader: ES20MgShader
    // シェーダ(座標軸)
    private lateinit var shaderAxis: ES20VBOSimple01Shader

    // 描画モデル
    private lateinit var model: MgModelAbs
    // VBO(描画モデル用)
    private lateinit var boModel: ES20VBOAbs
    // 描画するモデルの種類
    private var modelType = -1
    // 座標軸モデル
    private lateinit var modelAxis: Axis01Model
    // VBO(座標軸モデル用)
    private lateinit var boAxis: ES20VBOpc


    // スケール
    private var scale = 1f

    // 前回利用したシェーダ
    private var shaderSwitchOld = shaderSwitch

    // テクスチャ配列
    private val textures = IntArray(1)

    /*
    init {
        shaderSwitch = 2
        shaderSwitchOld = shaderSwitch
    }
    */

    override fun onDrawFrame(gl: GL10) {
        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning ) {
            if ( ( rotateAxis[0] == true ) or ( rotateAxis[1] == true ) or ( rotateAxis[2] == true ) ) {
                angle[0] = (angle[0] + 1) % 360
            }
        }
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // クォータニオンによる回転が適用された状態の座標変換行列を取得する
        val matQ = qtnNow.toMatIV()

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデル座標変換行列にクォータニオンを適用する
        Matrix.multiplyMM(matM,0,matM,0,matQ,0)

        // 各座標軸を中心に回転
        val rotateVec = floatArrayOf(0f,0f,0f)
        rotateVec[0] = if ( rotateAxis[0] ) 1f else 0f
        rotateVec[1] = if ( rotateAxis[1] ) 1f else 0f
        rotateVec[2] = if ( rotateAxis[2] ) 1f else 0f

        // (0,0,0)をかけるとモデル座標変換行列が0になって、モデルが非表示になるため
        // どれかの軸が回転対象となっているとき、回転を施す
        if ( ( rotateAxis[0] == true ) or ( rotateAxis[1] == true ) or ( rotateAxis[2] == true ) ) {
            Matrix.rotateM(matM,0,t0,rotateVec[0],rotateVec[1],rotateVec[2])
        }

        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

       // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // 選択されたシェーダが前回と違う場合
        // モデルデータを再読み込みする
        if ( shaderSwitch != shaderSwitchOld ) {
            // シェーダ(モデル描画)生成
            createShader()
            // モデル生成
            createModel()
            // VBO生成
            createVBO()

            shaderSwitchOld = shaderSwitch
        }

        // モデルを描画
        drawModel()

        //Log.d(javaClass.simpleName, Arrays.toString(matMVP))

        // 座標軸を描画
        if ( displayAxis ) {
            GLES20.glLineWidth(5f)
            shaderAxis.draw(modelAxis,boAxis,matMVP,GLES20.GL_LINES)
        }
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

        // テクスチャに張り付けるビットマップをロード
        //val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.texture_rabbit)

        // テクスチャを生成
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.createTexture(0,textures,bmp)

        // シェーダ(モデル描画)生成
        createShader()

        // モデル生成
        createModel()

        // VBO生成
        createVBO()

        // シェーダ(座標軸)
        shaderAxis = ES20VBOSimple01Shader()
        shaderAxis.loadShader()

        // 座標軸モデル生成
        modelAxis = Axis01Model()
        modelAxis.createPath( mapOf("scale" to 2f))

        // VBO(座標軸モデル)
        boAxis = ES20VBOpc()
        boAxis.makeVIBO(modelAxis)
    }

    // シェーダ(モデル描画)生成
    private fun createShader() {
        if (this::shader.isInitialized) {
            shader.deleteShader()
        }
        shader = when (shaderSwitch) {
            // 特殊効果なし
            0 -> ES20VBOSimple01Shader()
            // 平行光源
            1 -> ES20VBODirectionalLight01Shader()
            // 環境光
            2 -> ES20VBOAmbientLight01Shader()
            // 反射光
            3 -> ES20VBOSpecularLight01Shader()
            // フォンシェーディング
            4 -> ES20VBOPhongShading01Shader()
            // 点光源
            5 -> ES20VBOPointLight01Shader()
            // 点で描画
            6 -> ES20VBOPoints01Shader()
            // 線で描画(LINES)
            7 -> ES20VBOSimple01Shader()
            // テクスチャ
            8 -> ES20VBOTexture01Shader()
            else -> ES20Simple01Shader()
        }
        shader.loadShader()
    }

    // モデル生成
    private fun createModel() {
        model = when (modelType) {
            // 正四面体
            0 -> Tetrahedron01Model()
            // 立方体
            1 -> Cube01Model()
            // 正八面体
            2 -> Octahedron01Model()
            // 正十二面体
            3 -> Dodecahedron01Model()
            // 正二十面体
            4 -> Icosahedron01Model()
            // 球
            5 -> Sphere01Model()
            // トーラス
            6 -> Torus01Model()
            // 円柱
            7 -> Cylinder01Model()
            else -> Tetrahedron01Model()
        }

        when (shaderSwitch) {
            // 点で描画(POINTS)
            6 -> model.createPath( mapOf("scale" to scale, "pattern" to 10f) )
            // 線で描画(LINES)
            7 -> model.createPath( mapOf("scale" to scale, "pattern" to 20f) )
            // テクスチャ
            // テクスチャを張る場合は、モデルを白で描画する
            8 -> model.createPath( mapOf("scale" to scale, "colorR" to 1f, "colorG" to 1f, "colorB" to 1f,  "colorA" to 1f) )
            // 上記以外
            else -> {
                model.createPath( mapOf("scale" to scale) )
            }
        }
    }

    // VBO生成
    private fun createVBO() {
        if (this::boModel.isInitialized) {
            boModel.deleteVIBO()
        }
        boModel = when (shaderSwitch) {
            // 特殊効果なし
            0 -> ES20VBOIpc()
            // 平行光源
            1 -> ES20VBOIpnc()
            // 環境光
            2 -> ES20VBOIpnc()
            // 反射光
            3 -> ES20VBOIpnc()
            // フォンシェーディング
            4 -> ES20VBOIpnc()
            // 点光源
            5 -> ES20VBOIpnc()
            // 点で描画
            6 -> ES20VBOpc()
            // 線で描画(LINES)
            7 -> ES20VBOpc()
            // テクスチャ
            8 -> ES20VBOIpct()
            else -> ES20VBOIpc()
        }
        boModel.makeVIBO(model)
    }

    // モデルを描画
    private fun drawModel() {
        when (shaderSwitch) {
            // 特殊効果なし
            0 -> (shader as ES20VBOSimple01Shader).draw(model,boModel,matMVP)
            // 平行光源
            1 -> (shader as ES20VBODirectionalLight01Shader).draw(model,boModel,matMVP,matI,vecLight)
            // 環境光
            2 -> (shader as ES20VBOAmbientLight01Shader).draw(model,boModel,matMVP,matI,vecLight,vecAmbientColor)
            // 反射光
            3  -> (shader as ES20VBOSpecularLight01Shader).draw(model,boModel,matMVP,matI,vecLight,vecEye,vecAmbientColor)
            // フォンシェーディング
            4 -> (shader as ES20VBOPhongShading01Shader).draw(model,boModel,matMVP,matI,vecLight,vecEye,vecAmbientColor)
            // 点光源
            5 -> (shader as ES20VBOPointLight01Shader).draw(model,boModel,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)
            // 点で描画
            6 -> {
                val u_pointSize = when (modelType) {
                    // トーラス
                    6 -> 10f
                    else -> 30f
                }
                (shader as ES20VBOPoints01Shader).draw(model,boModel,matMVP,u_pointSize)
            }
            // 線で描画(LINES)
            7 -> {
                when (modelType) {
                    // 球
                    5 -> (shader as ES20VBOSimple01Shader).draw(model,boModel,matMVP,GLES20.GL_LINE_STRIP)
                    // トーラス
                    6 -> (shader as ES20VBOSimple01Shader).draw(model,boModel,matMVP,GLES20.GL_LINE_STRIP)
                    // 円柱
                    7 -> (shader as ES20VBOSimple01Shader).draw(model,boModel,matMVP,GLES20.GL_LINE_STRIP)
                    // その他
                    else -> (shader as ES20VBOSimple01Shader).draw(model,boModel,matMVP,GLES20.GL_LINES)
                }
            }
            // テクスチャ
            8 -> {
                // テクスチャをバインド
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
                (shader as ES20VBOTexture01Shader).draw(model,boModel,matMVP,0)
                // テクスチャのバインドを解除
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
            }
            else -> (shader as ES20VBOSimple01Shader).draw(model,boModel,matMVP)
        }
    }

    // 描画に利用するデータを設定する
    override fun setMotionParam(motionParam: MutableMap<String,Float> ) {
        modelType = motionParam["modelType"]?.toInt() ?: 0
        scale = motionParam["scale"] ?: 1f
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        boModel.deleteVIBO()
        boAxis.deleteVIBO()
        // シェーダ(モデル描画)
        shader.deleteShader()
        // シェーダ(座標軸)
        shaderAxis.deleteShader()
    }

}