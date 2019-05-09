package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.view

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.*
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.renderer.Tetrahedron01Model
import milu.kiriu2010.gui.shader.*
import milu.kiriu2010.milumathcaras.R
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Polyhedron01XRenderer(ctx: Context): MgRenderer(ctx) {

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: Simple01Shader
    // シェーダ(点で描画)
    private lateinit var shaderPoints: Points01Shader
    // シェーダ(平行光源)
    private lateinit var shaderDirectionalLight: DirectionalLight01Shader
    // シェーダ(環境光)
    private lateinit var shaderAmbientLight: AmbientLight01Shader
    // シェーダ(反射光)
    private lateinit var shaderSpecularLight: SpecularLight01Shader
    // シェーダ(フォンシェーディング)
    private lateinit var shaderPhongShading: PhongShading01Shader
    // シェーダ(点光源)
    private lateinit var shaderPointLight: PointLight01Shader
    // シェーダ(テクスチャ)
    private lateinit var shaderTexture: Texture01Shader

    // 描画モデル
    private lateinit var model: MgModelAbs
    // 描画するモデルの種類
    private var modelType = -1
    // 座標軸モデル
    private lateinit var axisModel: Axis01Model

    // スケール
    private var scale = 1f

    // 前回利用したシェーダ
    private var shaderSwitchOld = shaderSwitch

    // テクスチャ配列
    private val textures = IntArray(1)

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
            shaderSwitchOld = shaderSwitch
        }

        // 描画
        // モデルを描画
        when (shaderSwitch) {
            // 特殊効果なし
            0 -> shaderSimple.draw(model,matMVP)
            // 平行光源
            1 -> shaderDirectionalLight.draw(model,matMVP,matI,vecLight)
            // 環境光
            2 -> shaderAmbientLight.draw(model,matMVP,matI,vecLight,vecAmbientColor)
            // 反射光
            3 -> shaderSpecularLight.draw(model,matMVP,matI,vecLight,vecEye,vecAmbientColor)
            // フォンシェーディング
            4 -> shaderPhongShading.draw(model,matMVP,matI,vecLight,vecEye,vecAmbientColor)
            // 点光源
            5 -> shaderPointLight.draw(model,matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)
            // 点で描画
            6 -> {
                val u_pointSize = when (modelType) {
                    // トーラス
                    6 -> 10f
                    else -> 30f
                }
                shaderPoints.draw(model,matMVP,u_pointSize)
            }
            // 線で描画(LINES)
            7 -> {
                when (modelType) {
                    // 球
                    5 -> shaderSimple.draw(model,matMVP,GLES20.GL_LINE_STRIP)
                    // トーラス
                    6 -> shaderSimple.draw(model,matMVP,GLES20.GL_LINE_STRIP)
                    // その他
                    else -> shaderSimple.draw(model,matMVP,GLES20.GL_LINES)
                }
            }
            // テクスチャ
            8 -> {
                // テクスチャをバインド
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
                shaderTexture.draw(model,matMVP,0)
                // テクスチャのバインドを解除
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
            }
            else -> shaderSimple.draw(model,matMVP)
        }

        //Log.d(javaClass.simpleName, Arrays.toString(matMVP))

        // 座標軸を描画
        if ( displayAxis ) {
            GLES20.glLineWidth(5f)
            //shaderSimple.drawLines(axisModel,matMVP,3)
            shaderSimple.draw(axisModel,matMVP,GLES20.GL_LINES)
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
        MyGLFunc.createTexture(0,textures,bmp)

        // シェーダ(特殊効果なし)
        shaderSimple = Simple01Shader()
        shaderSimple.loadShader()

        // シェーダ(点で描画)
        shaderPoints = Points01Shader()
        shaderPoints.loadShader()

        // シェーダ(平行光源)
        shaderDirectionalLight = DirectionalLight01Shader()
        shaderDirectionalLight.loadShader()

        // シェーダ(環境光)
        shaderAmbientLight = AmbientLight01Shader()
        shaderAmbientLight.loadShader()

        // シェーダ(反射光)
        shaderSpecularLight = SpecularLight01Shader()
        shaderSpecularLight.loadShader()

        // シェーダ(フォンシェーディング)
        shaderPhongShading = PhongShading01Shader()
        shaderPhongShading.loadShader()

        // シェーダ(点光源)
        shaderPointLight = PointLight01Shader()
        shaderPointLight.loadShader()

        // シェーダ(テクスチャ)
        shaderTexture = Texture01Shader()
        shaderTexture.loadShader()

        // モデル生成
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
            else -> Tetrahedron01Model()
        }
        model.createPath( mapOf("scale" to scale) )

        // 座標軸モデル生成
        axisModel = Axis01Model()
        axisModel.createPath( mapOf("scale" to 2f))

    }

    // 描画に利用するデータを設定する
    override fun setMotionParam(motionParam: MutableMap<String,Float> ) {
        modelType = motionParam["modelType"]?.toInt() ?: 0
        scale = motionParam["scale"] ?: 1f
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        // シェーダ(特殊効果なし)
        shaderSimple.deleteShader()
        // シェーダ(点で描画)
        shaderPoints.deleteShader()
        // シェーダ(平行光源)
        shaderDirectionalLight.deleteShader()
        // シェーダ(環境光)
        shaderAmbientLight.deleteShader()
        // シェーダ(反射光)
        shaderSpecularLight.deleteShader()
        // シェーダ(フォンシェーディング)
        shaderPhongShading.deleteShader()
        // シェーダ(点光源)
        shaderPointLight.deleteShader()
        // シェーダ(テクスチャ)
        shaderTexture.deleteShader()
    }

}