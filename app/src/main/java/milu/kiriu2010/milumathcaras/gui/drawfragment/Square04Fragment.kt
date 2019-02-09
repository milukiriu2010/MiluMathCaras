package milu.kiriu2010.milumathcaras.gui.drawfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
// ☆ Square01Fragmentから加えたビュー
// ----------------------------------------
// ☆関数式表示用のTextView １つ
// ・描画用のImageView１つ
// ・通知用のTextView １つ
// ☆パラメータ表示用のTextView １つ
// ----------------------------------------
class Square04Fragment : Fragment()
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
    private lateinit var textView1: TextView

    // パラメータを表示するビュー
    private lateinit var textView2: TextView

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
        val view = inflater.inflate(R.layout.fragment_square_04, container, false)

        // 関数式を表示するビュー
        textViewFuncDesc = view.findViewById(R.id.textViewFuncDesc)
        textViewFuncDesc.text = drawData.funcDescMap["funcDesc"] ?: ""

        // 描画するビュー
        imageView = view.findViewById(R.id.imageView)
        drawable = MyDrawableFactory.createInstance(drawData.id,this)
        imageView.setImageDrawable(drawable)

        // 描画に使っている媒介変数の値を表示するビュー
        textView1 = view.findViewById(R.id.textView1)

        // パラメータを表示するビュー
        textView2 = view.findViewById(R.id.textView2)

        return view
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
        textView1.text = when (numOfDecimals) {
            0 -> data.toInt().toString()
            else -> data.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            Square04Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
