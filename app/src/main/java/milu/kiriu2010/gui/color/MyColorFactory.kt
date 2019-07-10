package milu.kiriu2010.gui.color

import java.lang.RuntimeException

enum class ColorType {
    // 1536色
    COLOR_1536,
    // 768色(暗色系)
    COLOR_768_DARK
}

class MyColorFactory {
    companion object {
        fun createInstance(colorType: ColorType): MyColor {
            return when (colorType) {
                // 1536色
                ColorType.COLOR_1536 -> MyColor1536()
                // 768色(暗色系)
                ColorType.COLOR_768_DARK -> MyColor768Dark()
                //else -> throw RuntimeException("Not Found ColorType")
            }
        }
    }
}