package milu.kiriu2010.gui.vbo.es32

import android.opengl.GLES32
import milu.kiriu2010.gui.basic.MyGLES32Func
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VAO
// VBO
//   0:頂点位置
//   1:法線
//   2:色
//   3:テクスチャ座標
// IBO
// --------------------------------
// ES3.2用
// --------------------------------
// 2019.06.20
// --------------------------------
class ES32VAOIpnct: ES32VAOAbs() {

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
        hVBO = IntArray(4)
        GLES32.glGenBuffers(4, hVBO,0)
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

        // 法線
        modelAbs.bufNor.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[1])
        MyGLES32Func.checkGlError("a_Normal:glBindBuffer")
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,modelAbs.bufNor.capacity()*4, modelAbs.bufNor,GLES32.GL_STATIC_DRAW)
        MyGLES32Func.checkGlError("a_Normal:glBufferData")
        GLES32.glEnableVertexAttribArray(1)
        MyGLES32Func.checkGlError("a_Normal:glEnableVertexAttribArray")
        GLES32.glVertexAttribPointer(1,3,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Normal:glVertexAttribPointer")
        //GLES32.glVertexAttribPointer(1,3,GLES32.GL_FLOAT,false,3*4,0)

        // 色
        modelAbs.bufCol.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[2])
        MyGLES32Func.checkGlError("a_Color:glBindBuffer")
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,modelAbs.bufCol.capacity()*4, modelAbs.bufCol,GLES32.GL_STATIC_DRAW)
        MyGLES32Func.checkGlError("a_Color:glBufferData")
        GLES32.glEnableVertexAttribArray(2)
        MyGLES32Func.checkGlError("a_Color:glEnableVertexAttribArray")
        GLES32.glVertexAttribPointer(2,4,GLES32.GL_FLOAT,false,0,0)
        MyGLES32Func.checkGlError("a_Color:glVertexAttribPointer")
        //GLES32.glVertexAttribPointer(1,4,GLES32.GL_FLOAT,false,3*4,0)

        // テクスチャ座標
        modelAbs.bufTxc.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[3])
        MyGLES32Func.checkGlError("a_TexCoord:glBindBuffer")
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,modelAbs.bufTxc.capacity()*4, modelAbs.bufTxc,GLES32.GL_STATIC_DRAW)
        MyGLES32Func.checkGlError("a_TexCoord:glBufferData")
        GLES32.glEnableVertexAttribArray(3)
        MyGLES32Func.checkGlError("a_TexCoord:glEnableVertexAttribArray")
        GLES32.glVertexAttribPointer(3,2,GLES32.GL_FLOAT,false,0,0)
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
