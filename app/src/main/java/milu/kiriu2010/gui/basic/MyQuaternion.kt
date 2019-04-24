package milu.kiriu2010.gui.basic

import milu.kiriu2010.math.MyMathUtil
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt

// ------------------------------------
// https://wgld.org/d/webgl/w032.html
// のライブラリをコピー
// ------------------------------------
//  0:X座標
//  1:Y座標
//  2:Z座標
//  3:角度
// ------------------------------------
// 2019.04.24  マージ
// ------------------------------------
data class MyQuaternion(
    val q: FloatArray = floatArrayOf(0f,0f,0f,1f)
) {

    // クォータニオンを単位化する
    fun identity(): MyQuaternion {
        q[0] = 0f
        q[1] = 0f
        q[2] = 0f
        q[3] = 1f

        return this
    }

    // クォータニオンを反転し、共役四元数を生成する
    fun inverse(): MyQuaternion {
        q[0] = -q[0]
        q[1] = -q[1]
        q[2] = -q[2]
        q[3] = q[3]

        return this
    }

    // クォータニオンを正規化する
    fun normalize(): MyQuaternion {
        val x = q[0]
        val y = q[1]
        val z = q[2]
        val w = q[3]
        val l = sqrt(x*x+y*y+z*z+w*w)
        if ( l === 0f ) {
            q[0] = 0f
            q[1] = 0f
            q[2] = 0f
            q[3] = 1f
        }
        else {
            q[0] /= l
            q[1] /= l
            q[2] /= l
            q[3] /= l
        }

        return this
    }

    // クォータニオンを掛け合わせる
    //  Q  = (q        ; V)
    //  R  = (r        ; W)
    //  QR = (qr - V・W; qW + rV + V x W)
    fun multiply( qtn: MyQuaternion ): MyQuaternion {
        val ax = q[0]
        val ay = q[1]
        val az = q[2]
        val aw = q[3]
        val bx = qtn.q[0]
        val by = qtn.q[1]
        val bz = qtn.q[2]
        val bw = qtn.q[3]

        q[0] = ax * bw + aw * bx + ay * bz - az * by
        q[1] = ay * bw + aw * by + az * bx - ax * bz
        q[2] = az * bw + aw * bz + ax * by - ay * bx
        q[3] = aw * bw - ax * bx - ay * by - az * bz

        return this
    }

    // 4x4の正方行列にクォータニオンを掛け合わせる
    // ----------------------------------------------------------------------
    // X軸を中心に90度回転したものを出力したら、こっちの方がしっくりくる
    // ----------------------------------------------------------------------
    // http://marupeke296.com/DXG_No58_RotQuaternionTrans.html
    // ----------------------------------------------------------------------
    fun toMatIV(): FloatArray {
        var x = q[0]
        var y = q[1]
        var z = q[2]
        var w = q[3]

        var xx = 2f*x*x
        var yy = 2f*y*y
        var zz = 2f*z*z
        var xy = 2f*x*y
        var xz = 2f*x*z
        var yz = 2f*y*z
        var wx = 2f*w*x
        var wy = 2f*w*y
        var wz = 2f*w*z

        return floatArrayOf(
                1f-yy-zz,   //  0:1-2yy-2zz
                xy+wz,      //  1:2xy+2wz
                xz-wy,      //  2:2xz-2wy
                0f,         //  3:0
                xy-wz,      //  4:2xy-2wz
                1-xx-zz,    //  5:1-2xx-2zz
                yz+wx,      //  6:2yz+2wx
                0f,         //  7:0
                xz+wy,      //  8:2xz+2wy
                yz-wx,      //  9:2yz-2wx
                1-xx-yy,    // 10:1-2xx-2zz
                0f,         // 11:0
                0f,         // 12:0
                0f,         // 13:0
                0f,         // 14:0
                1f          // 15:1
        )
    }

    // 符号が上と逆
    fun toMatIVwReverse(): FloatArray {
        var x = q[0]
        var y = q[1]
        var z = q[2]
        var w = q[3]
        var x2 = x+x
        var y2 = y+y
        var z2 = z+z
        var xx = x*x2
        var xy = x*y2
        var xz = x*z2
        var yy = y*y2
        var yz = y*z2
        var zz = z*z2
        var wx = w*x2
        var wy = w*y2
        var wz = w*z2

        return floatArrayOf(
                1f-(yy+zz), // 0
                xy-wz,      // 1
                xz+wy,      // 2
                0f,         // 3
                xy+wz,      // 4
                1-(xx+zz),  // 5
                yz-wx,      // 6
                0f,         // 7
                xz-wy,      // 8
                yz+wx,      // 9
                1-(xx+yy),  // 10
                0f,         // 11
                0f,         // 12
                0f,         // 13
                0f,         // 14
                1f          // 15
        )
    }

    // ---------------------------------------------------------
    // クォータニオンで３次元ベクトルを回転させる
    // ---------------------------------------------------------
    //   vec
    //     回転させたいベクトル
    //   return
    //     計算結果
    // ---------------------------------------------------------
    //  ３次元空間上の座標
    //    P = (0; x,y,z)
    //  Pを回転させるための計算は
    //    R * P * Q = (0; X,Y,Z)
    //  回転要素を持つクォータニオンQ(=this)
    // ---------------------------------------------------------
    fun toVecIII(vec: FloatArray): FloatArray {
        // 座標P(３次元空間の座標)をクォータニオン形式にする
        var qp = MyQuaternion(floatArrayOf(vec[0],vec[1],vec[2],0f))
        // クォータニオンQに対応する共役四元数R
        var qr = MyQuaternion(floatArrayOf(q[0],q[1],q[2],q[3]))
        qr.inverse()
        // R*P
        var qq = MyQuaternion(floatArrayOf(qr.q[0],qr.q[1],qr.q[2],qr.q[3]))
                .multiply(qp)
        // (R*P)*Q
        var qs = MyQuaternion(floatArrayOf(qq.q[0],qq.q[1],qq.q[2],qq.q[3]))
                .multiply(this)
        return floatArrayOf(qs.q[0],qs.q[1],qs.q[2])
    }


    companion object {
        // ------------------------------------------------------
        // 任意軸での任意角回転を粟原素クォータニオンを生成する
        // ------------------------------------------------------
        //   angle
        //     任意軸回転の角度(度)
        //   axis
        //     回転軸を表すベクトル
        //   return
        //     生成されたクォータニオン
        // ------------------------------------------------------
        fun rotate( angle: Float, axis: FloatArray ): MyQuaternion {
            val qtn = MyQuaternion()
            var sq = sqrt(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2])
            if (sq == 0f) {
                qtn.q[0] = 0f
                qtn.q[1] = 0f
                qtn.q[2] = 0f
                qtn.q[3] = 1f
            }
            else {
                var a = axis[0]
                var b = axis[1]
                var c = axis[2]
                // 軸ベクトルを正規化する
                if ( sq != 1f ) {
                    sq = 1f/sq
                    a *= sq
                    b *= sq
                    c *= sq
                }

                var sin = MyMathUtil.sinf(angle/2f)
                qtn.q[0] = a * sin
                qtn.q[1] = b * sin
                qtn.q[2] = c * sin
                qtn.q[3] = MyMathUtil.cosf(angle/2f)
            }

            return qtn
        }

        // 球面線形補間
        //   ktime: 0 - 1
        fun slerp(qtn1: MyQuaternion,qtn2: MyQuaternion,ktime: Float): MyQuaternion {
            var ht = qtn1.q[0]*qtn2.q[0] + qtn1.q[1]*qtn2.q[1] + qtn1.q[2]*qtn2.q[2] + qtn1.q[3]*qtn2.q[3]
            var hs = 1f - ht*ht
            var qtn0 = MyQuaternion()
            if ( hs <= 0f ) {
                qtn0.q[0] = qtn1.q[0]
                qtn0.q[1] = qtn1.q[1]
                qtn0.q[2] = qtn1.q[2]
                qtn0.q[3] = qtn1.q[3]
            }
            else {
                hs = sqrt(hs)
                if ( abs(hs) < 0.001 ) {
                    qtn0.q[0] = qtn1.q[0]*0.5f + qtn2.q[0]*0.5f
                    qtn0.q[1] = qtn1.q[1]*0.5f + qtn2.q[1]*0.5f
                    qtn0.q[2] = qtn1.q[2]*0.5f + qtn2.q[2]*0.5f
                    qtn0.q[3] = qtn1.q[3]*0.5f + qtn2.q[3]*0.5f
                }
                else {
                    var ph = acos(ht)
                    var pt = ph * ktime
                    var t0 = sin(ph-pt)/hs
                    var t1 = sin(pt)/hs
                    qtn0.q[0] = qtn1.q[0]*t0 + qtn2.q[0]*t1
                    qtn0.q[1] = qtn1.q[1]*t0 + qtn2.q[1]*t1
                    qtn0.q[2] = qtn1.q[2]*t0 + qtn2.q[2]*t1
                    qtn0.q[3] = qtn1.q[3]*t0 + qtn2.q[3]*t1
                }
            }
            return qtn0
        }
    }
}
