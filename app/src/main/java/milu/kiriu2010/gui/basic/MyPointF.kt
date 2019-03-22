package milu.kiriu2010.gui.basic

import milu.kiriu2010.math.MyMathUtil
import java.lang.RuntimeException
import kotlin.math.*

// X,Y座標
data class MyPointF(
        var x: Float = 0f,
        var y: Float = 0f
) {
    // 距離
    fun distance(p: MyPointF): Float {
        val dx = x - p.x
        val dy = y - p.y
        return sqrt(dx*dx+dy*dy)
    }

    // 大きさ
    fun magnitude(): Float {
        return sqrt(x*x+y*y)
    }

    fun plusSelf(p: MyPointF): MyPointF {
        x = x + p.x
        y = y + p.y
        return this
    }

    fun plus(p: MyPointF): MyPointF {
        return MyPointF().also {
            it.x = x + p.x
            it.y = y + p.y
        }
    }


    fun subtractSelf(p: MyPointF): MyPointF {
        x = x - p.x
        y = y - p.y
        return this
    }

    fun diff(p: MyPointF): MyPointF {
        val dx = x - p.x
        val dy = y - p.y
        return MyPointF(dx,dy)
    }

    fun multiply(f: Float): MyPointF {
        return MyPointF().also {
            it.x = x * f
            it.y = y * f
        }
    }

    fun divide(f: Float): MyPointF {
        if ( f == 0f ) {
            throw RuntimeException("cannot divide by 0.")
        }
        return MyPointF().also {
            it.x = x / f
            it.y = y / f
        }
    }

    // -----------------------------------
    // 中点
    // -----------------------------------
    fun mid(p: MyPointF): MyPointF {
        return MyPointF().also {
            it.x = (x+p.x)/2f
            it.y = (y+p.y)/2f
        }
    }

    // -----------------------------------
    // 重心
    // -----------------------------------
    fun cog(p: MyPointF,q: MyPointF): MyPointF {
        return MyPointF().also {
            it.x = (x+p.x+q.x)/3f
            it.y = (y+p.y+q.y)/3f
        }
    }

    // -----------------------------------
    // 原点を中心とした回転
    // -----------------------------------
    fun rotate(angleDv: Float): MyPointF {
        val mag = magnitude()
        val angle = MyMathUtil.getAngle(MyPointF(0f,0f),this)
        x = mag * cos((angle+angleDv)*PI/180f).toFloat()
        y = mag * sin((angle+angleDv)*PI/180f).toFloat()
        return this
    }

    // -----------------------------------
    // 座標pを中心とした回転
    // -----------------------------------
    fun rotate(angleDv: Float, p: MyPointF): MyPointF {
        val dx = x - p.x
        val dy = y - p.y

        val ex = dx * MyMathUtil.cosf(angleDv) - dy * MyMathUtil.sinf(angleDv)
        val ey = dx * MyMathUtil.sinf(angleDv) + dy * MyMathUtil.cosf(angleDv)

        x = ex + p.x
        y = ey + p.y

        return this
    }

    // -------------------------------------
    // "点A-自点"と"点B-自点"のなす角度を取得
    // -------------------------------------
    fun getAngle(a: MyPointF, b: MyPointF): Float {
        val va = diff(a)
        val vb = diff(b)

        // 内積からcos値を求める
        val cost = (va.x*vb.x + va.y*vb.y)/(va.magnitude()*vb.magnitude())
        // cos値から角度を求める
        val acos = acos(cost).toFloat()

        // acosの値は、-PI/2～PI/2なので,負の場合はPI足して補正する
        return when {
            (acos >= 0f) -> {
                (acos * 180f/PI).toFloat()
            }
            else -> {
                ((acos+PI) * 180f/PI).toFloat()
            }
        }
    }

}
