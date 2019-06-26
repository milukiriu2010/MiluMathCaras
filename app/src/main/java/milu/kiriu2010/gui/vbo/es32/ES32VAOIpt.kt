package milu.kiriu2010.gui.vbo.es32

import android.opengl.GLES32
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VAO
// VBO
//   0:頂点位置
//   1:テクスチャ座標
// IBO
// --------------------------------
// ES3.2用
// --------------------------------
// 2019.06.19
// --------------------------------
class ES32VAOIpt: ES32VAOAbs() {

    override fun makeVIBO(modelAbs: MgModelAbs) {
        //Log.d(javaClass.simpleName,"makeVIBO:${modelAbs.javaClass.simpleName}")
        model = modelAbs

        // ------------------------------------------------
        // VAOの生成
        // ------------------------------------------------
        hVAO = IntArray(1)
        GLES32.glGenVertexArrays(1,hVAO,0)
        MyGLES32Func.checkGlError("glGenVertexArrays")
        GLES32.glBindVertexArray(hVAO[0])
        MyGLES32Func.checkGlError("glBindVertexArray")

        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(2)
        GLES32.glGenBuffers(2, hVBO,0)
        MyGLES32Func.checkGlError("hVBO:glGenBuffers")

        // 位置
        modelAbs.bufPos.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[0])
        MyGLES32Func.checkGlError("a_Position:glBindBuffer")
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,modelAbs.bufPos.capacity()*4, modelAbs.bufPos,usagePos)
        MyGLES32Func.checkGlError("a_Position:glBufferData")
        GLES32.glEnableVertexAttribArray(0)
        MyGLES32Func.checkGlError("a_Position:glEnableVertexAttribArray")
        GLES32.glVertexAttribPointer(0,3,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Position:glVertexAttribPointer")
        //GLES32.glVertexAttribPointer(0,3,GLES32.GL_FLOAT,false,3*4,0)

        // テクスチャ座標
        modelAbs.bufTxc.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[1])
        MyGLES32Func.checkGlError("a_TexCoord:glBindBuffer")
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,modelAbs.bufTxc.capacity()*4, modelAbs.bufTxc,GLES32.GL_STATIC_DRAW)
        MyGLES32Func.checkGlError("a_TexCoord:glBufferData")
        GLES32.glEnableVertexAttribArray(1)
        MyGLES32Func.checkGlError("a_TexCoord:glEnableVertexAttribArray")
        GLES32.glVertexAttribPointer(1,2,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_TexCoord:glVertexAttribPointer")
        //GLES32.glVertexAttribPointer(2,2,GLES32.GL_FLOAT,false,2*4,0)

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES32.glGenBuffers(1, hIBO,0)
        MyGLES32Func.checkGlError("hIBO:glGenBuffers")

        modelAbs.bufIdx.position(0)
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        MyGLES32Func.checkGlError("idx:glBindBuffer")
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER,modelAbs.bufIdx.capacity()*2, modelAbs.bufIdx,GLES32.GL_STATIC_DRAW)
        MyGLES32Func.checkGlError("idx:glBufferData")

        // リソース解放
        GLES32.glBindVertexArray(0)
        MyGLES32Func.checkGlError("glBindVertexArray")
        //GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0)
        //GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
