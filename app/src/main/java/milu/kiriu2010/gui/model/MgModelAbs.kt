package milu.kiriu2010.gui.model

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

// ----------------------------------
// モデル抽象クラス
// ----------------------------------
// createPathの引数で使うマップのキー
//   scale
//     モデルの大きさを変更する
//   pattern
//     1:面(その１)
//     2:面(その２)
//    20:線
// ----------------------------------
// 2019.04.25
// 2019.06.12 オフセット
// 2019.07.13 warning消す
// ----------------------------------
abstract class MgModelAbs {
    // 頂点バッファ
    lateinit var bufPos: FloatBuffer
    // 法線バッファ
    lateinit var bufNor: FloatBuffer
    // 色バッファ
    lateinit var bufCol: FloatBuffer
    // テクスチャ座標バッファ
    lateinit var bufTxc: FloatBuffer
    // インデックスバッファ
    lateinit var bufIdx: ShortBuffer
    // オフセットバッファ
    lateinit var bufOff: FloatBuffer

    // 頂点データ
    var datPos = arrayListOf<Float>()
    // 法線データ
    var datNor = arrayListOf<Float>()
    // 色データ
    var datCol = arrayListOf<Float>()
    // テクスチャ座標データ
    var datTxc = arrayListOf<Float>()
    // インデックスデータ
    var datIdx = arrayListOf<Short>()
    // オフセットデータ
    var datOff = arrayListOf<Float>()

    protected fun allocateBuffer() {
        // 頂点バッファ
        bufPos = ByteBuffer.allocateDirect(datPos.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datPos.toFloatArray())
                position(0)
            }
        }

        // 法線バッファ
        bufNor = ByteBuffer.allocateDirect(datNor.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datNor.toFloatArray())
                position(0)
            }
        }

        // 色バッファ
        bufCol = ByteBuffer.allocateDirect(datCol.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datCol.toFloatArray())
                position(0)
            }
        }

        // テクスチャ座標バッファ
        bufTxc = ByteBuffer.allocateDirect(datTxc.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datTxc.toFloatArray())
                position(0)
            }
        }

        // インデックスバッファ
        bufIdx = ByteBuffer.allocateDirect(datIdx.size * 2).run {
            order(ByteOrder.nativeOrder())

            asShortBuffer().apply {
                put(datIdx.toShortArray())
                position(0)
            }
        }

        // オフセットバッファ
        bufOff = ByteBuffer.allocateDirect(datOff.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(datOff.toFloatArray())
                position(0)
            }
        }
    }

    abstract fun createPath( opt: Map<String,Float> = mapOf() )
}
