package milu.kiriu2010.math

import milu.kiriu2010.gui.basic.MyPointF
import java.util.Stack
import kotlin.math.PI
import kotlin.math.atan

class MyMathUtil {
    companion object {
        // -----------------------------------------------------------
        // リストをスタックに変換する
        // -----------------------------------------------------------
        fun listToStack(list: List<Int>): Stack<Int> {
            // スタック
            val stack = Stack<Int>()

            for (e in list) {
                stack.push(e)
            }

            return stack
        }

        // -----------------------------------------------------------
        // 最大公約数を求める
        // -----------------------------------------------------------
        // https://qiita.com/SUZUKI_Masaya/items/8589672ec53fe2670ee1
        // -----------------------------------------------------------
        fun gcd(list: List<Int>): Int {
            // 最大公約数を求める対象となる数が格納されたスタック
            val stack = listToStack(list)

            // ユークリッドの互除法を用いて、最大公約数を導出する
            // (最終的にスタック内に1つだけ数が残り、それが最大公約数となる)
            while (1 < stack.size) {
                // スタックから2つの数をpop
                val pops = (0 until 2).map {
                    stack.pop()
                }

                // スタックからpopした2つの数のうち、小さい方の数のインデックス
                val minIndex = if (pops[1] < pops[0]) {
                    1
                } else {
                    0
                }

                // スタックからpopした2つの数のうち、小さい方の数をpush
                stack.push(pops[minIndex])

                // スタックからpopした2つの数の剰余
                val r = pops[(minIndex + 1) % 2] % pops[minIndex]

                // スタックからpopした2つの数に剰余があるならば、それをpush
                if (0 < r) {
                    stack.push(r)
                }
            }

            // 最大公約数を返す
            return stack.pop()
        }

        // -----------------------------------------------------------
        // 最大公倍数を求める
        // -----------------------------------------------------------
        // https://qiita.com/SUZUKI_Masaya/items/8589672ec53fe2670ee1
        // -----------------------------------------------------------
        fun lcm(list: List<Int>): Int {
            // 最大公約数を求める対象となる数が格納されたスタック
            val stack = listToStack(list)

            // 最小公倍数を導出する
            // (最終的にスタック内に1つだけ数が残り、それが最小公倍数となる)
            while (1 < stack.size) {
                // スタックから2つの数をpop
                val pops = (0 until 2).map {
                    stack.pop()
                }

                // スタックからpopした2つの数の最小公倍数をpush
                stack.push(pops[0] * pops[1] / gcd(pops))
            }

            // 最小公倍数を返す
            return stack.pop()
        }

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

        // --------------------------------------------
        // "線分A-B"の角度を求める
        // --------------------------------------------
        fun getAngle(a: MyPointF, b: MyPointF ): Double {
            return when {
                // -------------------------
                // BがAより右下(正:正)
                // 0 ～ 90度
                // -------------------------
                (b.x >= a.x) and (b.y >= a.y) -> {
                    atan((b.y-a.y)/(b.x-a.x)) *180f/ PI
                }
                // -----------------------------------------------------------------------
                // BがAより左下(負:正)
                // 90 ～ 180度
                // -----------------------------------------------------------------------
                // atanは"-π/2 ～+π/2"を返すが、
                // 360度形式にしたいのと、左右が反転しているので180度からマイナスしている
                // -----------------------------------------------------------------------
                (b.x < a.x) and (b.y >= a.y) -> {
                    180f - atan((b.y-a.y)/(a.x-b.x)) *180f/ PI
                }
                // -----------------------------------------------------------------------
                // BがAより左上(負:負)
                // 180 ～ 270度
                // -----------------------------------------------------------------------
                // atanは"-π/2 ～+π/2"を返すが、
                // 360度形式にしたいのと、上下左右が反転しているので180度を加算している
                // -----------------------------------------------------------------------
                (b.x < a.x) and (b.y < a.y) -> {
                    atan((a.y-b.y)/(a.x-b.x)) *180f/ PI + 180f
                }
                // ------------------------------------------------------------------
                // BがAより右上(正:負)
                // 270 ～ 360度
                // ------------------------------------------------------------------
                // atanは"-π/2 ～+π/2"を返すが360度形式にしたいので360度足している
                // ------------------------------------------------------------------
                else -> {
                    atan((b.y-a.y)/(b.x-a.x)) *180f/ PI + 360f
                }
            }
        }

    }
}