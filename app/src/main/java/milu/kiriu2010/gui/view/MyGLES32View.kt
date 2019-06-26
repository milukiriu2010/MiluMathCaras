package milu.kiriu2010.gui.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

// ------------------------------------
// GLSL ES 3.2用ビュー
// ------------------------------------
// 2019.06.17
// ------------------------------------
class MyGLES32View: GLSurfaceView {
    constructor(ctx: Context): super(ctx) {

    }

    /* @JvmOverloads */
    constructor(ctx: Context, attrs: AttributeSet? = null) : super(ctx, attrs) {

    }

    init {
        // OpenGL ES 3.2 contextを生成
        setEGLContextClientVersion(3)
    }
}
