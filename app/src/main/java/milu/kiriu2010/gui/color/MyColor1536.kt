package milu.kiriu2010.gui.color

class MyColor1536: MyColor {

    private val max = 1536

    override fun getColorSize(): Int = max

    override fun create(p: Int, size: Int): Long {
        val dv = max.toFloat()/size.toFloat()
        val pdv = p*dv

        return when {
            // 0xff0000 - 0xffff00
            // Gの要素を増やす
            pdv < max/6 -> {
                0xffff0000 or (pdv.toInt() shl 8).toLong()
            }
            // 0xffff00 - 0x00ff00
            // Rの要素を減らす
            pdv < max/3 -> {
                ((0xff-(pdv.toInt()-256)) shl 16).toLong() or (0xff00ff00)
            }
            // 0x00ff00 - 0x00ffff
            // Bの要素を増やす
            pdv < max/2 -> {
                0xff00ff00 or (pdv.toInt()-512).toLong()
            }
            // 0x00ffff - 0x0000ff
            // Gの要素を減らす
            pdv < max*2/3 -> {
                ((0xff-(pdv.toInt()-768)) shl 8).toLong() or 0xff0000ff
            }
            // 0x0000ff - 0xff00ff
            // Rの要素を増やす
            pdv < max*5/6 -> {
                ((pdv.toInt()-1024) shl 16).toLong() or 0xff0000ff
            }
            // 0xff00ff - 0x0000ff
            // Bの要素を減らす
            pdv < max -> {
                0xffff0000 or (0xff-(pdv.toInt()-1280)).toLong()
            }
            else -> {
                 0xffff0000
            }
        }
    }
}