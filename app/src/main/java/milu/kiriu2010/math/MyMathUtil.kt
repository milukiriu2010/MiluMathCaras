package milu.kiriu2010.math

class MyMathUtil {
    companion object {
        // --------------------------------------------
        // 小数点以下の桁数を取得
        // --------------------------------------------
        // http://d.hatena.ne.jp/fyts/20080916/snippet
        // --------------------------------------------
        fun getNumberOfDecimals(num: Float): Int {
            val strNum = num.toString().trimEnd('0')
            val index = strNum.indexOf('.')
            return when (index) {
                // 整数の場合、小数点以下の桁数は0
                -1 -> 0
                // 少数の場合、小数点以下の桁数は"."より右の文字数
                else -> strNum.substring(index+1).length
            }
        }
    }
}