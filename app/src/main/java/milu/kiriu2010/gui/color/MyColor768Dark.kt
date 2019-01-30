package milu.kiriu2010.gui.color

class MyColor768Dark: MyColor {
    private val max = 768

    override fun create(p: Int, size: Int): Long {
        val dv = max.toFloat()/size.toFloat()
        val pdv = p*dv

        return when {
            // 0x7f0000 - 0x7f7f00
            // Gの要素を増やす
            pdv < max/6 -> {
                0xff7f0000 or (pdv.toInt() shl 8).toLong()
            }
            // 0x7f7f00 - 0x007f00
            // Rの要素を減らす
            pdv < max/3 -> {
                ((0x7f-(pdv.toInt()-128)) shl 16).toLong() or (0xff007f00)
            }
            // 0x007f00 - 0x007f7f
            // Bの要素を増やす
            pdv < max/2 -> {
                0xff007f00 or (pdv.toInt()-256).toLong()
            }
            // 0x007f7f - 0x00007f
            // Gの要素を減らす
            pdv < max*2/3 -> {
                ((0x7f-(pdv.toInt()-384)) shl 8).toLong() or 0xff00007f
            }
            // 0x00007f - 0x7f007f
            // Rの要素を増やす
            pdv < max*5/6 -> {
                ((pdv.toInt()-512) shl 16).toLong() or 0xff00007f
            }
            // 0x7f007f - 0x00007f
            // Bの要素を減らす
            pdv < max -> {
                0xff7f0000 or (0x7f-(pdv.toInt()-640)).toLong()
            }
            else -> {
                 0xff7f0000
            }
        }
    }
}