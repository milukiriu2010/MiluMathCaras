package milu.kiriu2010.gui.model

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import java.nio.*


// 座標軸
class Axis01Model: MgModelAbs() {

    override fun createPath( opt: Map<String,Float> ){
        datPos.clear()
        datNor.clear()
        datCol.clear()
        datTxc.clear()
        datIdx.clear()

        var scale = opt["scale"] ?: 1f

        // 頂点データ
        // X軸
        datPos.addAll(arrayListOf(     -scale,         0f,        0f))   // X:0
        datPos.addAll(arrayListOf(      scale,         0f,        0f))   // X:1
        datPos.addAll(arrayListOf( 0.9f*scale, 0.1f*scale,        0f))   // X:2
        datPos.addAll(arrayListOf(      scale,         0f,        0f))   // X:3
        datPos.addAll(arrayListOf( 0.9f*scale,-0.1f*scale,        0f))   // X:4
        datPos.addAll(arrayListOf(      scale,         0f,        0f))   // X:5
        // Y軸
        datPos.addAll(arrayListOf(         0f,     -scale,        0f))   // Y:0
        datPos.addAll(arrayListOf(         0f,      scale,        0f))   // Y:1
        datPos.addAll(arrayListOf( 0.1f*scale, 0.9f*scale,        0f))   // Y:2
        datPos.addAll(arrayListOf(         0f,      scale,        0f))   // Y:3
        datPos.addAll(arrayListOf(-0.1f*scale, 0.9f*scale,        0f))   // Y:4
        datPos.addAll(arrayListOf(         0f,      scale,        0f))   // Y:5
        // Z軸
        datPos.addAll(arrayListOf(         0f,         0f,    -scale))   // Z:0
        datPos.addAll(arrayListOf(         0f,         0f,     scale))   // Z:1
        datPos.addAll(arrayListOf(         0f, 0.1f*scale,0.9f*scale))   // Z:2
        datPos.addAll(arrayListOf(         0f,         0f,     scale))   // Z:3
        datPos.addAll(arrayListOf(         0f,-0.1f*scale,0.9f*scale))   // Z:4
        datPos.addAll(arrayListOf(         0f,         0f,     scale))   // Z:5


        // 色データ
        // X軸
        (0..5).forEach {
            datCol.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        // Y軸
        (6..11).forEach {
            datCol.addAll(arrayListOf<Float>(0f,1f,0f,1f))
        }
        // Z軸
        (12..17).forEach {
            datCol.addAll(arrayListOf<Float>(0f,0f,1f,1f))
        }

        allocateBuffer()
    }
}
