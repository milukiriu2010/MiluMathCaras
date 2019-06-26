package milu.kiriu2010.gui.vbo.es32

import android.opengl.GLES32
import milu.kiriu2010.gui.model.MgModelAbs

// ----------------------------------------------
// VBO(頂点位置＋色)
// IBO
// ----------------------------------------------
// 2019.05.31
// 2019.06.14  位置のBufferData引数を可変にする
// ----------------------------------------------
class ES32VBOIpc: ES32VBOAbs() {

    override fun makeVIBO(modelAbs: MgModelAbs) {
        model = modelAbs

        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(2)
        GLES32.glGenBuffers(2, hVBO,0)

        // 位置
        model.bufPos.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[0])
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,model.bufPos.capacity()*4, model.bufPos,usagePos)

        // 色
        model.bufCol.position(0)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,hVBO[1])
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,model.bufCol.capacity()*4, model.bufCol,usageCol)

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES32.glGenBuffers(1, hIBO,0)

        model.bufIdx.position(0)
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER,model.bufIdx.capacity()*2, model.bufIdx,GLES32.GL_STATIC_DRAW)

        // リソース解放
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0)
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
