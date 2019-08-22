package milu.kiriu2010.milumathcaras.gui.draw.d3.net.cube

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.shader.es32.ES32Simple01Shader
import milu.kiriu2010.gui.vbo.es32.ES32VAOIpc
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------
// 立方体の展開図02
// -------------------------------------
// ４×４の領域を展開しながら移動する
// -------------------------------------
// ２面のなす角
//       t  = 90度
// -------------------------------------
// https://sugaku.fun/development-view-of-cube/
// -------------------------------------
// パターン１
//   緑
//   赤青紫水
//   黄
// -------------------------------------
// パターン２
//     紫
//   水緑青黄
//   赤
// -------------------------------------
// パターン３
//       青
//   黄紫緑赤
//   水
// -------------------------------------
// 2019.08.20
// -------------------------------------
class NetCube02Renderer(ctx: Context): MgRenderer(ctx) {

    enum class ModePtn {
        // 展開図０１開
        PTN01_01,
        // 展開図０１閉
        PTN01_02,
        // 展開図０２開
        PTN02_01,
        // 展開図０２閉
        PTN02_02,
        // 展開図０３開
        PTN03_01,
        // 展開図０３閉
        PTN03_02,
        // 展開図０４開
        PTN04_01,
        // 展開図０４閉
        PTN04_02,
    }

    // 描画モデル(三角形)
    private val modelLst = mutableListOf<Triangle4Cube01Model>()

    // VAO
    private var vaoLst = mutableListOf<ES32VAOIpc>()

    // シェーダ(特殊効果なし)
    private val shaderSimple = ES32Simple01Shader(ctx)

    // 現在のモード
    var modeNow = ModePtn.PTN04_02

    var angleFDiv = 1f
    var cnt = 0
    var cntMax = 90
    var cntMin = 0
    var still = 0

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES32.glClearColor(1f, 1f, 1f, 1f)
        GLES32.glClearDepthf(1f)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning == true ) {

            // stillが0のときだけ、
            // 展開図が開閉するようにする
            if ( still == 0 ) {
                if ( cnt >= cntMax ) {
                    still = 1
                }
                else {
                    cnt++
                }
            }
            else {
                still = (still+1)%30
                if ( still == 0 ) {
                    cnt = cntMin
                    // 次のモードへ遷移する
                    modeNow = when (modeNow) {
                        ModePtn.PTN01_01 -> ModePtn.PTN01_02
                        ModePtn.PTN01_02 -> ModePtn.PTN02_01
                        ModePtn.PTN02_01 -> ModePtn.PTN02_02
                        ModePtn.PTN02_02 -> ModePtn.PTN03_01
                        ModePtn.PTN03_01 -> ModePtn.PTN03_02
                        ModePtn.PTN03_02 -> ModePtn.PTN04_01
                        ModePtn.PTN04_01 -> ModePtn.PTN04_02
                        ModePtn.PTN04_02 -> ModePtn.PTN01_01
                    }
                }
            }
        }
        val t0 = angleFDiv * cnt.toFloat()

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(5f,5f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
            vecEye[0], vecEye[1], vecEye[2],
            vecCenter[0], vecCenter[1], vecCenter[2],
            vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,60f,1f,0.1f,60f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        when (modeNow) {
            // 展開図０１開
            ModePtn.PTN01_01 -> doPtn01_01(t0)
            // 展開図０１閉
            ModePtn.PTN01_02 -> doPtn01_02(t0)
            // 展開図０２開
            ModePtn.PTN02_01 -> doPtn02_01(t0)
            // 展開図０２閉
            ModePtn.PTN02_02 -> doPtn02_02(t0)
            // 展開図０３開
            ModePtn.PTN03_01 -> doPtn03_01(t0)
            // 展開図０３閉
            ModePtn.PTN03_02 -> doPtn03_02(t0)
            // 展開図０４開
            ModePtn.PTN04_01 -> doPtn04_01(t0)
            // 展開図０４閉
            ModePtn.PTN04_02 -> doPtn04_02(t0)
        }


    }

    // 展開図０１開
    private fun doPtn01_01(t0: Float) {
        // ----------------------------------------------
        // (0)赤
        // 1:(-2,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-2f,0f,-2f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:Z軸-90度回転
        // 2:(-1,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 2:
        Matrix.translateM(matM,0,-1f,0f,-2f)
        // 1:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:X軸90度回転
        // 2:(-2,-1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 2:
        Matrix.translateM(matM,0,-2f,0f,-1f)
        // 1:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(-1,0)+Z軸90度回転
        // 2:(-2,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 2:
        Matrix.translateM(matM,0,-2f,0f,-2f)
        // 1:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:X軸90度回転
        // 2:(0,1)+X軸90度回転
        // 3:(0,1)+X軸90度回転
        // 4:(-2,-1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 4:
        Matrix.translateM(matM,0,-2f,0f,-1f)
        // 3:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 1:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:X軸90度回転
        // 2:(0,1)+X軸90度回転
        // 3:(-2,-1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 3:
        Matrix.translateM(matM,0,-2f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 1:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    // 展開図０１閉
    private fun doPtn01_02(t0: Float) {
        // ----------------------------------------------
        // (0)赤
        // 1:(0,-1)+X軸90度回転
        // 2:(0,-1)+X軸90度回転
        // 3:(0,-1)+X軸90度回転
        // 4:(-2,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM, 0)
        // 4:
        Matrix.translateM(matM, 0, -2f, 0f, 1f)
        // 3:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 1:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0)
        shaderSimple.draw(vaoLst[0], matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:Z軸90度
        // 2:(0,-1)+X軸90度回転
        // 3:(0,-1)+X軸90度回転
        // 4:(0,-1)+X軸90度回転
        // 5:(-1,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM, 0)
        // 5:
        Matrix.translateM(matM, 0, -1f, 0f, 1f)
        // 4:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 3:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 1:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0)
        shaderSimple.draw(vaoLst[1], matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(0,-1)+X軸90度回転
        // 2:(0,-1)+X軸90度回転
        // 3:(-2,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM, 0)
        // 3:
        Matrix.translateM(matM, 0, -2f, 0f, 1f)
        // 2:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 1:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0)
        shaderSimple.draw(vaoLst[2], matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(-1,0)+Z軸-90度
        // 2:(0,-1)+X軸90度回転
        // 3:(0,-1)+X軸90度回転
        // 4:(0,-1)+X軸90度回転
        // 5:(-2,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM, 0)
        // 5:
        Matrix.translateM(matM, 0, -2f, 0f, 1f)
        // 4:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 3:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 1:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0)
        shaderSimple.draw(vaoLst[3], matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:(-2,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM, 0)
        // 1:
        Matrix.translateM(matM, 0, -2f, 0f, 1f)
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0)
        shaderSimple.draw(vaoLst[4], matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(0,-1)+X軸90度回転
        // 2:(-2,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM, 0)
        // 2:
        Matrix.translateM(matM, 0, -2f, 0f, 1f)
        // 1:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0)
        shaderSimple.draw(vaoLst[5], matMVP)
    }

    // 展開図０２開
    private fun doPtn02_01(t0: Float) {
        // ----------------------------------------------
        // (0)赤
        // 1:(-2,2)
        // 2:90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-2f,0f,2f)
        // 2:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:(-1,1)
        // 2:-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(-1,1)
        // 2:(1.0)-90度Z軸
        // 3:-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(-1,1)
        // 2:(1.0)-90度Z軸
        // 3:(1.0)-90度Z軸
        // 4:-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:(-2,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-2f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(-1,1)
        // 2:-90度Z軸
        // 3:(0,-1)-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        // 3:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    // 展開図０２閉
    private fun doPtn02_02(t0: Float) {

        // ----------------------------------------------
        // (0)赤
        // 1:(1,1)
        // 2:(-1,0)-90度Z軸
        // 3:(-1,0)-90度Z軸
        // 4:(-1,0)-90度Z軸
        // 5:-90度X軸+(0,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 5:
        Matrix.translateM(matM,0,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:(1,1)
        // 2:(-1.0)-90度Z軸
        // 3:(-1.0)-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(1,1)
        // 2:(-1.0)-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(1,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:(1,1)
        // 2:(-1.0)-90度Z軸
        // 3:(-1.0)-90度Z軸
        // 4:(-1.0)-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(1,1)
        // 2:(-1,0)-90度Z軸
        // 3:(-1,0)-90度Z軸
        // 4:(0,-1)+90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    // 展開図０３開
    private fun doPtn03_01(t0: Float) {
        // ----------------------------------------------
        // (0)赤
        // 1:(1,1)
        // 2:(0,-1)-90度X軸
        // 3:(0,-1)-90度X軸
        // 4:(0,-1)-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 3:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 4:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:(1,1)
        // 2:(0,-1)-90度X軸
        // 3:(0,-1)-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 3:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(1,1)
        // 2:(0,-1)-90度X軸
        // 3:(0,-1)-90度X軸
        // 4:(-1,0)+90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 3:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        // 4:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(1,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:(2,1)
        // 2:-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,2f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,0f,0f,1f)
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(1,1)
        // 2:(0,-1)-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,1f)
        // 2:
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    // 展開図０３閉
    private fun doPtn03_02(t0: Float) {
        // ----------------------------------------------
        // (0)赤
        // 1:(1,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-2f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:(1,-1)
        // 2:-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(1,-1)
        // 2:-90度X軸
        // 3:(-1,0)-90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(1,-1)
        // 2:(0,1)-90度X軸
        // 3:(0,1)-90度X軸
        // 4:-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 3:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 4:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)


        // ----------------------------------------------
        // (4)水
        // 1:(1,-1)
        // 2:(0,1)-90度X軸
        // 3:(0,1)-90度X軸
        // 4:(1,0)-90度X軸
        // 5:90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 3:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 4:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 5:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(1,-1)
        // 2:(0,1)-90度X軸
        // 3:-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,1f)
        // 3:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    // 展開図０４閉
    private fun doPtn04_02(t0: Float) {

        // ----------------------------------------------
        // (1)赤
        // 1:(-1,-2)
        // 2:(1,0)+90度Z軸
        // 3:(1,0)+90度Z軸
        // 4:90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:(-2,-1)
        // 2:-90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-2f,0f,-1f)
        // 2:
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(-1,-2)
        // 2:(1,0)+90度Z軸
        // 3:90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)


        // ----------------------------------------------
        // (3)黄
        // 1:(-1,-2)
        // 2:(1,0)+90度Z軸
        // 3:(1,0)+90度Z軸
        // 4:90度Z軸
        // 5:(0,-1)+90度X軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        // 5:
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.translateM(matM,0,0f,0f,-1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:(-2,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-2f,0f,-2f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(-1,-2)
        // 2:90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,-1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    // 展開図０４開
    private fun doPtn04_01(t0: Float) {
        // ----------------------------------------------
        // (0)赤
        // 1:(1,-2)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-2f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[0],matMVP)

        // ----------------------------------------------
        // (1)緑
        // 1:(1,-2)
        // 2:(-1,0)+90度Z軸
        // 3:(-1,0)+90度Z軸
        // 4:(-1,0)+90度Z軸
        // 5:+90度X軸+(0,1)
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 5:
        Matrix.translateM(matM,0,0f,0f,1f)
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[1],matMVP)

        // ----------------------------------------------
        // (2)青
        // 1:(1,-2)
        // 2:(-1,0)+90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[2],matMVP)

        // ----------------------------------------------
        // (3)黄
        // 1:(1,-2)
        // 2:X軸-90度
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 2:
        Matrix.translateM(matM,0,1f,0f,-2f)
        // 1:
        Matrix.rotateM(matM,0,-90f,1f,0f,0f)
        Matrix.rotateM(matM,0,-t0,1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[3],matMVP)

        // ----------------------------------------------
        // (4)水
        // 1:(1,-2)
        // 2:(-1,0)+90度Z軸
        // 3:(-1,0)+90度Z軸
        // 4:(-1,0)+90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 4:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[4],matMVP)

        // ----------------------------------------------
        // (5)紫
        // 1:(1,-2)
        // 2:(-1,0)+90度Z軸
        // 3:(-1,0)+90度Z軸
        // ----------------------------------------------
        Matrix.setIdentityM(matM,0)
        // 1:
        Matrix.translateM(matM,0,1f,0f,-2f)
        // 2:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        // 3:
        Matrix.rotateM(matM,0,-90f,0f,0f,1f)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,-1f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderSimple.draw(vaoLst[5],matMVP)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 回転
        isRunning = true

        // 深度テストを有効にする
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)

        // シェーダ(特殊効果なし)
        shaderSimple.loadShader()

        val colorLst =
            floatArrayOf(
                // 0:赤
                1f,0f,0f,1f,
                // 1:緑
                0f,1f,0f,1f,
                // 2:青
                0f,0f,1f,1f,
                // 3:黄色
                1f,1f,0f,1f,
                // 4:水色,
                0f,1f,1f,1f,
                // 5:紫
                1f,0f,1f,1f
            )

        (0..5).forEach {
            val model = Triangle4Cube01Model()
            model.createPath(mapOf(
                "pattern" to 2f,
                "colorR" to colorLst[it*4+0],
                "colorG" to colorLst[it*4+1],
                "colorB" to colorLst[it*4+2],
                "colorA" to colorLst[it*4+3]
            ))
            modelLst.add(model)

            val vao = ES32VAOIpc()
            vao.makeVIBO(model)
            vaoLst.add(vao)
        }
    }


    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    // MgRenderer
    // シェーダ終了処理
    override fun closeShader() {
        vaoLst.forEach { vao ->
            vao.deleteVIBO()
        }
        shaderSimple.deleteShader()
    }
}
