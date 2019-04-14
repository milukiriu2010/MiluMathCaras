package milu.kiriu2010.milumathcaras.gui.draw

import android.opengl.GLSurfaceView

abstract class MgRenderer: GLSurfaceView.Renderer {
    // シェーダ終了処理
    abstract fun closeShader()
}