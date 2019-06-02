package milu.kiriu2010.gui.vbo.es20

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs

// --------------------------------
// VBO(頂点位置＋色)
// IBO
// --------------------------------
// 2019.05.31
// --------------------------------
class ES20VBOIpc: ES20VBOAbs() {

    override fun makeVIBO(model: MgModelAbs) {
        // ------------------------------------------------
        // VBOの生成
        // ------------------------------------------------
        hVBO = IntArray(2)
        GLES20.glGenBuffers(2, hVBO,0)

        // 位置
        model.bufPos.position(0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,hVBO[0])
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,model.bufPos.capacity()*4, model.bufPos,GLES20.GL_STATIC_DRAW)

        // 色
        model.bufCol.position(0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,hVBO[1])
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,model.bufCol.capacity()*4, model.bufCol,GLES20.GL_STATIC_DRAW)

        // ------------------------------------------------
        // IBOの生成
        // ------------------------------------------------
        hIBO = IntArray(1)
        GLES20.glGenBuffers(1, hIBO,0)

        model.bufIdx.position(0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,hIBO[0])
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,model.bufIdx.capacity()*2, model.bufIdx,GLES20.GL_STATIC_DRAW)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
