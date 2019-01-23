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

private const val ARG_PARAM1 = "drawdata"

// ----------------------------------------
// アニメーション描画をするフラグメント
// ----------------------------------------
// 正方形の領域に描画する
// ----------------------------------------
// ・描画用のImageView１つ
// ・通知用のTextView １つ
// ----------------------------------------
class Square02Fragment : Fragment()
    , NotifyCallback {

    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var imageView: ImageView

    // 描画するDrawable
    private lateinit var drawable: MyDrawable

    // 描画に使っている媒介変数の値を表示するビュー
    private lateinit var textView: TextView

    // 描画に使っている媒介変数の値を変更するシークバー
    private lateinit var seekBar: SeekBar

    // 描画に使っている媒介変数の値を変更するシークバーのカーソル位置
    //private var pos: Int = 0

    // シークバーの値を表示するビュー
    private lateinit var seekText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawData = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_square_02, container, false)

        // 描画するビュー
        imageView = view.findViewById(R.id.imageView)
        drawable = MyDrawableFactory.createInstance(drawData.id,this)
        imageView.setImageDrawable(drawable)

        // 描画に使っている媒介変数の値を表示するビュー
        textView = view.findViewById(R.id.textView)

        // 描画に使っている媒介変数の値を変更するシークバー
        seekBar = view.findViewById(R.id.seekBar)

        // シークバーの値を表示するビュー
        seekText = view.findViewById(R.id.seekText)
        // "実際の値"をシークバーの仮想位置に反映
        value2seekBar()

        seekBar.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // シークバーの位置から"実際の値"を取得
                seekBar2Value()
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

    // "実際の値"をシークバーの仮想位置に反映
    private fun value2seekBar() {
        // 編集可能な媒介変数の最小値
        val min = drawData.editParam[0]
        // 編集可能な媒介変数の最大値
        val max = drawData.editParam[1]
        // 実際の値=動画用パラメータから"編集可能な媒介変数の現在値"を取得
        val now = drawData.motionImageParam[drawData.editParam[2].toInt()]
        // シークバーの実際の可動範囲
        val size = max-min
        // 実際の値に対応するシークバーの仮想位置
        val pos = ((now-min)*seekBar.max/(max-min)).toInt()
        // 仮想位置をシークバーに反映
        seekBar.progress = pos
        // 実際の値をテキストに反映
        seekText.text = now.toString()

    }

    // シークバーの位置から"実際の値"を取得
    private fun seekBar2Value(): Float {
        val min = drawData.editParam[0]
        val max = drawData.editParam[1]
        val pos = seekBar.progress
        // シークバーの位置から"実際の値"を取得
        val now = min.toBigDecimal()+pos.toBigDecimal()*(max.toBigDecimal()-min.toBigDecimal())/seekBar.max.toBigDecimal()
        // 実際の値をテキストに反映
        seekText.text = now.toString()

        return now.toFloat()
    }

    // アニメーションをリスタート
    private fun drawRestart() {
        // シークバーの位置から"実際の値"を取得
        val now = seekBar2Value()
        // アニメーションをリスタート
        drawable.calStop()
        val param = drawData.motionImageParam.clone()
        param.set(drawData.editParam[2].toInt(),now)
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
            Square02Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
