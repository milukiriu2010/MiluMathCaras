package milu.kiriu2010.milumathcaras.gui.drawfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.R

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawableFactory
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback
import java.lang.RuntimeException

private const val ARG_PARAM1 = "drawdata"

// ----------------------------------------------
// アニメーション描画をするフラグメント
// ----------------------------------------------
// 正方形の領域に描画する
// ----------------------------------------------
// ☆ Square03Fragmentから加えたビュー
// ----------------------------------------------
// ☆関数式表示用のTextView １つ
// ・描画用のImageView１つ
// ・通知用のTextView １つ
// ・媒介変数を変更するSeekBar ２つ
// ・変更した媒介変数の値を表示するTextView ２つ
// ----------------------------------------------
class Square06Fragment : Fragment()
    , NotifyCallback {

    // 描画データ
    private lateinit var drawData: DrawData

    // 関数式を表示するビュー
    private lateinit var textViewFuncDesc: TextView

    // 描画するビュー
    private lateinit var imageView: ImageView

    // 描画するDrawable
    private lateinit var drawable: MyDrawable

    // 描画に使っている媒介変数の値を表示するビュー
    private lateinit var textView: TextView

    // 描画に使っている媒介変数の値を変更するシークバーA
    private lateinit var seekBarA: SeekBar

    // シークバーAの値を表示するビュー
    private lateinit var seekTextA: TextView

    // 描画に使っている媒介変数の値を変更するシークバーB
    private lateinit var seekBarB: SeekBar

    // シークバーBの値を表示するビュー
    private lateinit var seekTextB: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawData = it.getParcelable(ARG_PARAM1)
            if ( drawData.editParam.size < 6 ) {
                throw RuntimeException("Short of editParam size. 6 params are required, at least.")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_square_06, container, false)

        // 関数式を表示するビュー
        textViewFuncDesc = view.findViewById(R.id.textViewFuncDesc)
        textViewFuncDesc.text = drawData.funcDescMap["funcDesc"] ?: ""

        // 描画するビュー
        imageView = view.findViewById(R.id.imageView)
        drawable = MyDrawableFactory.createInstance(drawData.id,this)
        imageView.setImageDrawable(drawable)

        // 描画に使っている媒介変数の値を表示するビュー
        textView = view.findViewById(R.id.textView)

        // 描画に使っている媒介変数の値を変更するシークバーA
        seekBarA = view.findViewById(R.id.seekBarA)

        // シークバーAの値を表示するビュー
        seekTextA = view.findViewById(R.id.seekTextA)

        // 描画に使っている媒介変数の値を変更するシークバーB
        seekBarB = view.findViewById(R.id.seekBarB)

        // シークバーBの値を表示するビュー
        seekTextB = view.findViewById(R.id.seekTextB)

        // "実際の値"をシークバーの仮想位置に反映
        value2seekBar(seekBarA,seekTextA, intArrayOf(0,1,2))
        value2seekBar(seekBarB,seekTextB, intArrayOf(3,4,5))

        seekBarA.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // シークバーの位置から"実際の値"を取得
                seekBar2Value(seekBarA,seekTextA, intArrayOf(0,1,2))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            // アニメーションをリスタート
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                drawRestart()
            }
        })

        seekBarB.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // シークバーの位置から"実際の値"を取得
                seekBar2Value(seekBarB,seekTextB, intArrayOf(3,4,5))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            // アニメーションをリスタート
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                drawRestart()
            }
        })

        return view
    }

    // ----------------------------------------------
    // "実際の値"をシークバーの仮想位置に反映
    // ----------------------------------------------
    // seekBarAの場合 ⇒ idArray = intArrayOf(0,1,2)
    // seekBarBの場合 ⇒ idArray = intArrayOf(3,4,5)
    // ----------------------------------------------
    private fun value2seekBar(seekBar: SeekBar, seekText: TextView, idArray: IntArray ) {
        // 編集可能な媒介変数の最小値
        val min = drawData.editParam[idArray[0]].toBigDecimal()
        // 編集可能な媒介変数の最大値
        val max = drawData.editParam[idArray[1]].toBigDecimal()
        // 実際の値=動画用パラメータから"編集可能な媒介変数の現在値"を取得
        val now = drawData.motionImageParam[drawData.editParam[idArray[2]].toInt()].toBigDecimal()
        // シークバーの実際の可動範囲
        val size = max-min
        // 実際の値に対応するシークバーの仮想位置
        val pos = ((now-min)*seekBar.max.toBigDecimal()/(max-min)).toInt()
        // 仮想位置をシークバーに反映
        seekBar.progress = pos
        // 実際の値をテキストに反映
        val key = when {
            seekBar == seekBarA ->"handParam1"
            seekBar == seekBarB ->"handParam2"
            else -> ""
        }
        val handParam = drawData.funcDescMap[key] ?: ""
        seekText.text = "$handParam=${now.toString()}"

        Log.d(javaClass.simpleName,"1:pos[$pos]min[$min]max[$max]now[$now]")
    }

    // ----------------------------------------------
    // シークバーの位置から"実際の値"を取得
    // ----------------------------------------------
    // seekBarAの場合 ⇒ idArray = intArrayOf(0,1,2)
    // seekBarBの場合 ⇒ idArray = intArrayOf(3,4,5)
    // ----------------------------------------------
    private fun seekBar2Value(seekBar: SeekBar, seekText: TextView, idArray: IntArray ): Float {
        val min = drawData.editParam[idArray[0]].toBigDecimal()
        val max = drawData.editParam[idArray[1]].toBigDecimal()
        val pos = seekBar.progress
        val mag = max-min
        // シークバーの位置から"実際の値"を取得
        val now = min+(pos.toBigDecimal()*100.0.toBigDecimal())*mag/(seekBar.max.toBigDecimal()*100.0.toBigDecimal())
        // 実際の値をテキストに反映
        val key = when {
            seekBar == seekBarA ->"handParam1"
            seekBar == seekBarB ->"handParam2"
            else -> ""
        }
        val handParam = drawData.funcDescMap[key] ?: ""
        seekText.text = "$handParam=${now.toString()}"

        return now.toFloat()
    }

    // アニメーションをリスタート
    private fun drawRestart() {
        // シークバーAの位置から"実際の値"を取得
        val nowA = seekBar2Value(seekBarA,seekTextA, intArrayOf(0,1,2))
        // シークバーBの位置から"実際の値"を取得
        val nowB = seekBar2Value(seekBarB,seekTextB, intArrayOf(3,4,5))
        // アニメーションをリスタート
        drawable.calStop()
        val param = drawData.motionImageParam.clone()
        param.set(drawData.editParam[2].toInt(),nowA)
        param.set(drawData.editParam[5].toInt(),nowB)
        drawable.calStart(true,*param)
    }

    override fun onResume() {
        //Log.d(javaClass.simpleName,"motionImageParam.size[${drawData.motionImageParam.size}]")

        // アニメーション描画開始
        drawable.calStart(true,*drawData.motionImageParam)
        super.onResume()
    }

    override fun onPause() {
        // 描画に使うスレッドを解放する
        // 画面を消したときスレッドが止まるようにするためonPauseで呼び出している
        drawable.calStop()
        super.onPause()
    }

    // ---------------------------------
    // 通知データを受信する
    // ---------------------------------
    override fun receive(data: Float) {
        // 小数点以下の桁数
        val numOfDecimals = MyMathUtil.getNumberOfDecimals(data)

        // 媒介変数の値をビューに表示する
        textView.text = when (numOfDecimals) {
            0 -> data.toInt().toString()
            else -> data.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            Square06Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
