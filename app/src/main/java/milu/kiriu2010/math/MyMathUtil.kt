package milu.kiriu2010.math

import android.util.Log
import milu.kiriu2010.gui.basic.MyPointF
import java.util.Stack
import kotlin.math.*

// --------------------------------------
// 数学演算ユーティリティ
// --------------------------------------
// 2019.05.04 小数点kから倍率を求める
// 2019.05.10 Float配列正規化
// 2019.05.22 定義追加:SQRT3
// 2019.05.28 gaussianブラーの重みを計算
// --------------------------------------
class MyMathUtil {
    companion object {
        // -----------------------------------------------------------
        // 外積２
        // -----------------------------------------------------------
        // id1: ベクトルA
        // id2: ベクトルB
        // id3: ベクトルA,Bの原点
        // -----------------------------------------------------------
        fun crossProduct3Dv2( dat: ArrayList<Float>, id1: Int, id2: Int, id3: Int ): ArrayList<Float> {
            if ( dat.size < 6 ) {
                throw RuntimeException("dat.size is short.")
            }

            val a = arrayListOf<Float>(dat[id1]-dat[id3],dat[id1+1]-dat[id3+1],dat[id1+2]-dat[id3+2])
            val b = arrayListOf<Float>(dat[id2]-dat[id3],dat[id2+1]-dat[id3+1],dat[id2+2]-dat[id3+2])

            val nx = a[1]*b[2]-a[2]*b[1]
            val ny = a[2]*b[0]-a[0]*b[2]
            val nz = a[0]*b[1]-a[1]*b[0]
            val ns = sqrt(nx*nx+ny*ny+nz*nz)

            return arrayListOf( nx/ns, ny/ns , nz/ns )
        }

        // -----------------------------------------------------------
        // 外積
        // -----------------------------------------------------------
        fun crossProduct3D( a: List<Float>, b: List<Float> ): ArrayList<Float> {
            if ( a.size != 3 ) {
                throw RuntimeException("a is not vec3.")
            }
            if ( b.size != 3 ) {
                throw RuntimeException("b is not vec3.")
            }

            val nx = a[1]*b[2]-a[2]*b[1]
            val ny = a[2]*b[0]-a[0]*b[2]
            val nz = a[0]*b[1]-a[1]*b[0]
            val ns = sqrt(nx*nx+ny*ny+nz*nz)

            return arrayListOf( nx/ns, ny/ns , nz/ns )
        }

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
        // 小数点kから倍率を求める
        //   ちょっとだめだが、とりあえずよしとする
        // --------------------------------------------
        // 例:cos(kt)とcos(t)を同期させるために使う
        // --------------------------------------------
        // 以下参考
        //   Limacon03Drawable
        //
        //   k=0.5 => t=720
        //   k=0.7 => t=2520たぶん
        //     360*7,360*10
        //     252,360の最小公倍数
        //   k=0.9 => t=3600
        //     400=360/0.9
        //     400,360の最小公倍数
        //   k=1.5 => t=720
        //     240=360/1.5
        //     240,360の最小公倍数
        //   k=1.8 => t=1800
        //     200=360/1.8
        //     200,360の最小公倍数
        // ------------------------------------------------
        fun getScaleFromNumberOfDecimal(k: Float): Float {
            if ( ( k == 0f ) or ( k == 1f ) ) return 1f

            Log.d("getScale", "k[$k]")
            // 小数点以下の桁数
            val nod = MyMathUtil.getNumberOfDecimals(k)
            Log.d("getScale", "nod[$nod]")
            // 比較対象の数
            //   a0: 10の倍数
            //   a1: numの小数点がなくなるまで桁を左シフト
            val a0 = (10f.pow(nod)).toInt()
            val a1 = (a0*k).toInt()
            Log.d("getScale", "a0[$a0]")
            Log.d("getScale", "a1[$a1]")
            // a0とa1の最大公約数
            val agcd = MyMathUtil.gcd(listOf(a0,a1))
            Log.d("getScale", "agcd[$agcd]")
            // 比較対象の数を最大公約数で割る
            val b0 = a0/agcd
            val b1 = a1/agcd
            Log.d("getScale", "b0[$b0]")
            Log.d("getScale", "b1[$b1]")

            return when {
                ( k > 1f ) -> {
                    b1.toFloat()
                }
                ( k < 1f ) -> {
                    b0.toFloat()
                }
                else -> {
                    1f
                }
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

        // --------------------------------------------
        // 角度を0～360度の範囲に補正
        // --------------------------------------------
        fun correctAngle(a: Float): Float {
            if ( ( a >= 0f ) and ( a < 360f ) ) {
                return a
            }
            else if ( a >= 360f ) {
                var b = a-360f
                while ( b >= 360f ) b = b - 360f
                return b
            }
            else {
                var b = a+360f
                while ( b < 0f ) b = b + 360f
                return b
            }
        }

        // --------------------------------------------
        // Float配列正規化
        // --------------------------------------------
        fun normalize(dat1: FloatArray): FloatArray {
            val dat2 = FloatArray(dat1.size)
            var sq = 0f
            dat1.forEachIndexed { id, fl ->
                dat2[id] = dat1[id]*dat1[id]
                sq += dat2[id]
            }
            val sq2 = 1f/ sqrt(sq)
            dat2.forEachIndexed { id, fl ->
                dat2[id] *= sq2
            }
            return dat2
        }


        // ------------------------------------------------
        // gaussianブラーの重みを計算
        // ------------------------------------------------
        fun gaussianWeigt(cnt: Int, dis: Float, denominator: Float = 10f): FloatArray {
            val weight = FloatArray(cnt)
            var t = 0f
            var d = dis*dis/denominator
            (0 until weight.size).forEach { i ->
                val r = 1f + 2f*i.toFloat()
                var w = exp(-0.5f*(r*r)/d)
                weight[i] = w;
                if (i > 0) w *= 2f
                t += w
            }
            (0 until weight.size).forEach { i ->
                weight[i] /= t
            }
            return weight
        }

        // --------------------------------------------
        // cos(度)
        // --------------------------------------------
        fun cosf(angle: Float): Float {
            return cos(angle*PI/180f).toFloat()
        }

        // --------------------------------------------
        // sin(度)
        // --------------------------------------------
        fun sinf(angle: Float): Float {
            return sin(angle*PI/180f).toFloat()
        }

        // --------------------------------------------
        // tan(度)
        // --------------------------------------------
        fun tanf(angle: Float): Float {
            return tan(angle*PI/180f).toFloat()
        }

        // 定数
        val GOLDEN_RATIO = (sqrt(5f)+1f)/2f
        val SQRT2  = sqrt(2f)
        val SQRT3  = sqrt(3f)
        val COS36F = (sqrt(5f)+1f)/4f
        val SIN36F = sqrt(10f-2f*sqrt(5f))/4f
        val COS72F = (sqrt(5f)-1f)/4f
        val SIN72F = sqrt(10f+2f*sqrt(5f))/4f
    }
}
