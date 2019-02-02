package milu.kiriu2010.milumathcaras.gui.drawfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import milu.kiriu2010.milumathcaras.R

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawable
import milu.kiriu2010.milumathcaras.gui.draw.MyDrawableFactory

private const val ARG_PARAM1 = "drawdata"

// ----------------------------------------
// Touchイベントを受け付けるフラグメント
// ----------------------------------------
// 長方形の領域に描画する
// ----------------------------------------
// ・描画用のImageView１つ
// ----------------------------------------
class Touch01Fragment : Fragment() {

    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var imageView: ImageView

    // 描画するDrawable
    private lateinit var drawable: MyDrawable

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
        val view = inflater.inflate(R.layout.fragment_rectangle_01, container, false)

        // 描画するビュー
        imageView = view.findViewById(R.id.imageView)
        drawable = MyDrawableFactory.createInstance(drawData.id)
        imageView.setImageDrawable(drawable)

        imageView.setOnTouchListener { v, event ->
            // ---------------------------------------------
            // x,y    => view    の左上からの位置
            // rawX,Y => デバイスの左上からの位置
            // らしいが、なんかずれてる
            // ---------------------------------------------
            Log.d(javaClass.simpleName,"Touch:x[${event.x}]xp[${event.xPrecision}]xr[${event.rawX}]y[${event.y}]" )
            when ( event.action ) {
                // タッチしたとき
                MotionEvent.ACTION_DOWN -> drawable.receiveTouchPoint(event.x,event.y)
                // タッチを離したとき
                MotionEvent.ACTION_UP -> drawable.receiveTouchPoint(-1f,-1f)
                // タッチ点を移動したとき
                MotionEvent.ACTION_MOVE -> drawable.receiveTouchPoint(event.x,event.y)
            }

            true
        }

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

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            Touch01Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
