package milu.kiriu2010.gui.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLES20View: GLSurfaceView {
    constructor(ctx: Context): super(ctx) {

    }

    /* @JvmOverloads */
    constructor(ctx: Context, attrs: AttributeSet? = null) : super(ctx, attrs) {

    }

    init {
        // OpenGL ES 2.0 contextを生成
        setEGLContextClientVersion(2)
    }
}
