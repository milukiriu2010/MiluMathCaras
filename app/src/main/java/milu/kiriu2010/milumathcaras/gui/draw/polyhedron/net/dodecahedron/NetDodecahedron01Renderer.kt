package milu.kiriu2010.milumathcaras.gui.draw.polyhedron.net.dodecahedron

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es20.ES20Simple01Shader
import milu.kiriu2010.math.MyMathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// -------------------------------------
// 正十二面体の展開図01
// -------------------------------------
// ２面のなす角で回転の向きを変更
// -------------------------------------
// ２面のなす角
//   cos(t) = -1/sqrt(5)
//       t  = 116.565051051度
// -------------------------------------
class NetDodecahedron01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画モデル(五角形)
    private val modelLst = mutableListOf<Pentagon4Dodecahedron01Model>()

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20Simple01Shader

    // 定数
    val cos36 = MyMathUtil.COS36F
    val sin36 = MyMathUtil.SIN36F
    val cos72 = MyMathUtil.COS72F
    val sin72 = MyMathUtil.SIN72F
    val hH = sqrt(5f+2f* sqrt(5f))
    val hh = sqrt(10f+2f* sqrt(5f)) /2f
    val ww = (sqrt(5f) +1f)/2f


    // 63.434948949
    // = (180-116.565051051)
    val angleFDiv0 =  0.63434948949f
    var cnt = 0
    var cntMax = 100
    var cntMin = -100
    var cntDir = 1
    var still = 0
    // 初回サークルかどうか
    var isFirstCycle = true

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {

            // stillが0のときだけ、
            // 展開図が開閉するようにする
            if ( still == 0 ) {
                // 十二面体を形成した場合、
                // 回転する向きを変更する
                if ( cnt >= cntMax ) {
                    cntDir = -1
                    still=1
                }
                else if ( cnt <= cntMin ) {
                    cntDir = 1
                    still=1
                }
                else if ( cnt == 0 ) {
                    still = 1
                }
                else {
                    cnt += cntDir
                }
            }
            else {
                still = (still+1)%30
                if ( still == 0 ) {
                    cnt += cntDir
                    if (isFirstCycle === true) {
                        isFirstCycle = false
                    }
                }
            }
        }
        val t0 = angleFDiv0 * cnt.toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,24f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // ----------------------------------------------
        // 回転するモデルを描画(１：上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[0],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(２：下)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[1],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(３：左上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-ww,hh,0f)
        Matrix.rotateM(matM,0,t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,0f,hH,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[2],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(４：右上←３：左上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-cos36,hH-sin36,0f)
        Matrix.rotateM(matM,0,t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,cos36,sin36,0f)
        Matrix.rotateM(matM,0,t0,cos72,-sin72,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[3],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(５：上←３：左上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-cos36,hh+sin36,0f)
        Matrix.rotateM(matM,0,t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,-cos36,hH-sin36,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[4],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(６：左上←３：左上)
        // ----------------------------------------------
        // 回転したとき、ちょっとずれてる
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-2f*cos36,hh,0f)
        Matrix.rotateM(matM,0,t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,-2f*cos36,2f*sin36,0f)
        Matrix.rotateM(matM,0,t0,cos72,sin72,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[5],matMVP)

        // ----------------------------------------------
        // 回転するモデルを描画(７：左２上←４：左上)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,-cos36,hH-sin36,0f)
        Matrix.rotateM(matM,0,t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,-2f*cos36,0f,0f)
        Matrix.rotateM(matM,0,-t0,cos36,-sin36,0f)
        Matrix.translateM(matM,0,-cos36,-sin36-hh,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[6],matMVP)

        // --------------------------------------------------------
        // 回転するモデルを描画(８：右下←２：下)
        // --------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,cos36,-hH+sin36,0f)
        Matrix.rotateM(matM,0,-t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,cos36,-hH+sin36,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[7],matMVP)

        // --------------------------------------------------------
        // 回転するモデルを描画(９：左←８：右下←２：下)
        // --------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,-hH,0f)
        Matrix.rotateM(matM,0,-t0,cos36,sin36,0f)
        Matrix.rotateM(matM,0,-t0,cos72,-sin72,0f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[8],matMVP)

        // --------------------------------------------------------
        // 回転するモデルを描画(１０：下←８：右下←２：下)
        // --------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,ww,-hh,0f)
        Matrix.rotateM(matM,0,-t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,0f,-hH,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[9],matMVP)

        // --------------------------------------------------------
        // 回転するモデルを描画(１１：右←８：右下←２：下)
        // --------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,2f*cos36,-hh,0f)
        Matrix.rotateM(matM,0,-t0,cos36,sin36,0f)
        Matrix.translateM(matM,0,2f*cos36,hh-hH,0f)
        Matrix.rotateM(matM,0,-t0,cos72,sin72,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[10],matMVP)

        // --------------------------------------------------------
        // 回転するモデルを描画(１２：右上←８：右下←２：下)
        // --------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,2f*cos36,-hh,0f)
        Matrix.rotateM(matM,0,-t0,cos36,sin36,0f)
        Matrix.rotateM(matM,0,t0,cos36,-sin36,0f)
        Matrix.translateM(matM,0,2f*cos36,hh,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(modelLst[11],matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 回転
        isRunning = true

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ(特殊効果なし)
        shaderSimple = ES20Simple01Shader()
        shaderSimple.loadShader()

        // 描画モデル(五角形)
        val model1 = Pentagon4Dodecahedron01Model()
        model1.createPath(mapOf(
            "pattern" to 1f,
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model1)

        // 描画モデル(五角形)
        val model2 = Pentagon4Dodecahedron01Model()
        model2.createPath(mapOf(
            "pattern" to 2f,
            "colorR" to 1f,
            "colorG" to 0.5f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model2)

        // 描画モデル(五角形)
        val model3 = Pentagon4Dodecahedron01Model()
        model3.createPath(mapOf(
            "pattern" to 2f,
            "colorR" to 1f,
            "colorG" to 1f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model3)

        // 描画モデル(五角形)
        val model4 = Pentagon4Dodecahedron01Model()
        model4.createPath(mapOf(
            "pattern" to 1f,
            "colorR" to 0.5f,
            "colorG" to 1f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model4)

        // 描画モデル(五角形)
        val model5 = Pentagon4Dodecahedron01Model()
        model5.createPath(mapOf(
            "pattern" to 1f,
            "colorR" to 0f,
            "colorG" to 1f,
            "colorB" to 0f,
            "colorA" to 1f
        ))
        modelLst.add(model5)

        // 描画モデル(五角形)
        val model6 = Pentagon4Dodecahedron01Model()
        model6.createPath(mapOf(
            "pattern" to 1f,
            "colorR" to 0f,
            "colorG" to 1f,
            "colorB" to 0.5f,
            "colorA" to 1f
        ))
        modelLst.add(model6)

        // 描画モデル(五角形)
        val model7 = Pentagon4Dodecahedron01Model()
        model7.createPath(mapOf(
            "pattern" to 1f,
            "colorR" to 0f,
            "colorG" to 1f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model7)

        // 描画モデル(五角形)
        val model8 = Pentagon4Dodecahedron01Model()
        model8.createPath(mapOf(
            "pattern" to 1f,
            "colorR" to 0f,
            "colorG" to 0.5f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model8)

        // 描画モデル(五角形)
        val model9 = Pentagon4Dodecahedron01Model()
        model9.createPath(mapOf(
            "pattern" to 2f,
            "colorR" to 0f,
            "colorG" to 0f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model9)

        // 描画モデル(五角形)
        val model10 = Pentagon4Dodecahedron01Model()
        model10.createPath(mapOf(
            "pattern" to 2f,
            "colorR" to 0.5f,
            "colorG" to 0f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model10)

        // 描画モデル(五角形)
        val model11 = Pentagon4Dodecahedron01Model()
        model11.createPath(mapOf(
            "pattern" to 2f,
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 1f,
            "colorA" to 1f
        ))
        modelLst.add(model11)

        // 描画モデル(五角形)
        val model12 = Pentagon4Dodecahedron01Model()
        model12.createPath(mapOf(
            "pattern" to 2f,
            "colorR" to 1f,
            "colorG" to 0f,
            "colorB" to 0.5f,
            "colorA" to 1f
        ))
        modelLst.add(model12)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        // シェーダ(特殊効果なし)
        shaderSimple.deleteShader()
    }

}