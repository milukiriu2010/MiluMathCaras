package milu.kiriu2010.gui.renderer

import android.content.Context
import android.opengl.GLSurfaceView

abstract class MgRenderer(val context: Context): GLSurfaceView.Renderer {
    // モデル変換行列
    protected val matM = FloatArray(16)
    // モデル変換行列の逆行列
    protected val matI = FloatArray(16)
    // ビュー変換行列
    protected val matV = FloatArray(16)
    // プロジェクション変換行列
    protected val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    protected val matMVP = FloatArray(16)
    // ビュー・プロジェクション行列
    protected val matVP = FloatArray(16)
    // 点光源の位置
    protected val vecLight = floatArrayOf(2f,2f,2f)
    // 環境光の色
    protected val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    protected val vecEye = floatArrayOf(0f,0f,5f)
    // カメラの上方向を表すベクトル
    protected val vecEyeUp = floatArrayOf(0f,1f,0f)
    // 中心座標
    protected val vecCenter = floatArrayOf(0f,0f,0f)



    // レンダリング領域の幅
    var renderW: Int = 512
    // レンダリング領域の高さ
    var renderH: Int = 512

    // 回転角度
    protected var angle = intArrayOf(0,0)

    // 状態
    // -------------------------------
    //   true  => 動作
    //   false => 停止
    var isRunning = true

    // シェーダスイッチ
    var shaderSwitch = 0

    /*
    // 描画したものをビットマップにキャプチャする
    // http://d.hatena.ne.jp/orangesignal/20120814/1344923993
    // -----------------------------------------------------------
    // 真っ白
    fun createBitmapFromGLSurface(): Bitmap {
        val pixels = IntArray(renderW*renderH)
        val buf = IntBuffer.wrap(pixels)
        buf.position(0)

        GLES20.glReadPixels(0,0,renderW,renderH,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,buf)

        // -------------------------------------------------------------------------------
		// カラーチャネルを交換するために ColorMatrix と ColorMatrixFilter を使用します。
		//
		// 5x4 のマトリックス: [
		//   a, b, c, d, e,
		//   f, g, h, i, j,
		//   k, l, m, n, o,
		//   p, q, r, s, t
		// ]
		//
		// RGBA カラーへ適用する場合、以下のように計算します:
		//
		// R' = a * R + b * G + c * B + d * A + e;
		// G' = f * R + g * G + h * B + i * A + j;
		// B' = k * R + l * G + m * B + n * A + o;
		// A' = p * R + q * G + r * B + s * A + t;
		//
		// R (赤) と B (青) を交換したいので以下の様になります。
		//
		// R' = B => 0, 0, 1, 0, 0
		// G' = G => 0, 1, 0, 0, 0
		// B' = R => 1, 0, 0, 0, 0
		// A' = A => 0, 0, 0, 1, 0
		// -------------------------------------------------------------------------------
        val paint = Paint(Paint.FILTER_BITMAP_FLAG).also {
            // R(赤)とB(青)が逆なので交換する
            it.colorFilter = ColorMatrixColorFilter(ColorMatrix(floatArrayOf(
                0f,0f,1f,0f,0f,
                0f,1f,0f,0f,0f,
                1f,0f,0f,0f,0f,
                0f,0f,0f,1f,0f
            )))
        }

        val bitmap = Bitmap.createBitmap(renderW,renderH,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 上下が逆さまなので垂直方向に反転させる
        val matrix = Matrix()
        matrix.postScale(1f,-1f)
        matrix.postTranslate(0f,renderH.toFloat())
        canvas.concat(matrix)

        // 描画する
        // deprecated
        canvas.drawBitmap(pixels,0,renderW,0,0,renderW,renderH,false,paint)


        return bitmap
    }
    */

    /*
    // 真っ白
    fun createBitmapFromGLSurface(): Bitmap {
        val bitmapBuffer = IntArray(renderW * renderH)
        val bitmapSource = IntArray(renderW * renderH)
        val intBuffer = IntBuffer.wrap(bitmapBuffer)
        intBuffer.position(0)

        try {
            GLES20.glReadPixels(x, y, renderW, renderH, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, intBuffer)
            var offset1: Int
            var offset2: Int
            for (i in 0 until renderH) {
                offset1 = i * renderW
                offset2 = (renderH - i - 1) * renderW
                for (j in 0 until renderW) {
                    val texturePixel = bitmapBuffer[offset1 + j]
                    val blue = texturePixel shr 16 and 0xff
                    val red = texturePixel shl 16 and 0x00ff0000
                    val pixel = texturePixel and -0xff0100 or red or blue
                    bitmapSource[offset2 + j] = pixel
                }
            }
        } catch (e: GLException) {
            throw RuntimeException( "GLException:${e.message}" )
        }

        return Bitmap.createBitmap(bitmapSource, renderW, renderH, Bitmap.Config.ARGB_8888)
    }
    */

    // 描画に利用するデータを設定する
    abstract fun setMotionParam(motionParam: MutableMap<String,Float> )

    // シェーダ終了処理
    abstract fun closeShader()
}