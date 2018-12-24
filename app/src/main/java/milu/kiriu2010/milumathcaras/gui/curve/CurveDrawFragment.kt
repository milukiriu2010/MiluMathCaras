package milu.kiriu2010.milumathcaras.gui.curve


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.gui.main.MyDrawable
import milu.kiriu2010.milumathcaras.gui.main.MyDrawableFactory

private const val ARG_PARAM1 = "drawdata"

class CurveDrawFragment : Fragment() {
    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var imageView: ImageView

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
        val view = inflater.inflate(R.layout.fragment_curve_draw, container, false)

        // 描画するビュー
        imageView = view.findViewById(R.id.imageView)
        val drawable = MyDrawableFactory.createInstance(drawData.id)
        imageView.setImageDrawable(drawable)

        // 描画
        drawable.cal(0)
        drawable.invalidateSelf()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            CurveDrawFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
