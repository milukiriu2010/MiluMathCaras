package milu.kiriu2010.milumathcaras.gui.drawfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.R

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.entity.DrawFragmentType
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawable
import milu.kiriu2010.milumathcaras.gui.draw.d2.MyDrawableFactory
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

private const val ARG_PARAM1 = "drawdata"

// --------------------------------------------------
// Drawableを使った描画を実施するフラグメントを生成
// --------------------------------------------------
// ・描画用のImageView１つ
// ・通知用のTextView １つ
// --------------------------------------------------
class D2x01Fragment : androidx.fragment.app.Fragment()
    , NotifyCallback {

    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var imageView: ImageView

    // 描画するDrawable
    private lateinit var drawable: MyDrawable

    // 描画に使っている媒介変数の値を表示するビュー
    private lateinit var textView: TextView

    // メニュー(再開)
    private var menuItemResume: MenuItem? = null

    // メニュー(停止)
    private var menuItemPause: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawData = it.getParcelable(ARG_PARAM1) ?: DrawData(DrawDataID.ID_000001_CYCLOID,DrawFragmentType.FT_D2_01,"")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_d2_01, container, false)

        // 描画するビュー
        imageView = view.findViewById(R.id.imageViewD2x01)
        drawable = MyDrawableFactory.createInstance(drawData.id,this)
        imageView.setImageDrawable(drawable)

        imageView.setOnTouchListener { _, event ->
            // ---------------------------------------------
            // x,y    => view    の左上からの位置
            // rawX,Y => デバイスの左上からの位置
            // らしいが、なんかずれてる
            // ---------------------------------------------
            /*
            val loc = IntArray(2)
            v.getLocationOnScreen(loc)
            Log.d(javaClass.simpleName,"===========================================" )
            Log.d(javaClass.simpleName,"V    :x[${v.left}]y[${v.top}]" )
            Log.d(javaClass.simpleName,"Loc  :x[${loc[0]}]y[${loc[1]}]" )
            Log.d(javaClass.simpleName,"Touch:x[${event.x}]y[${event.y}]" )
            Log.d(javaClass.simpleName,"Preci:x[${event.xPrecision}]y[${event.yPrecision}]" )
            Log.d(javaClass.simpleName,"Raw  :x[${event.rawX}]y[${event.rawY}]" )
            */
            when ( event.action ) {
                // タッチしたとき
                MotionEvent.ACTION_DOWN -> drawable.receiveTouchPoint(event)
                // タッチを離したとき
                MotionEvent.ACTION_UP -> drawable.receiveTouchPoint(event)
                // タッチ点を移動したとき
                MotionEvent.ACTION_MOVE -> drawable.receiveTouchPoint(event)
            }

            true
        }

        // 描画に使っている媒介変数の値を表示するビュー
        textView = view.findViewById(R.id.textViewD2x01)

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
        textView.text = when (numOfDecimals) {
            0 -> data.toInt().toString()
            else -> data.toString()
        }
    }

    // ----------------------------------------------------
    // アクションバーにメニューを表示するためのおまじない
    // ----------------------------------------------------
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // ----------------------------------------------------
    // アクションバーにメニューを表示
    // ----------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_basic,menu)
        // メニュー(再開)
        menuItemResume = menu.findItem(R.id.menuItemResume)
        // メニュー(停止)
        menuItemPause  = menu.findItem(R.id.menuItemPause)
        menuItemVisible(false,true)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // ----------------------------------------------------
    // 再開・停止メニューをON/OFFする
    // ----------------------------------------------------
    private fun menuItemVisible(flgResume: Boolean,flgPause: Boolean) {
        menuItemResume?.isVisible = flgResume
        menuItemPause?.isVisible = flgPause
    }

    // ----------------------------------------------------
    // アクションバーのアイコンをタップすると呼ばれる
    // ----------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 再開
            R.id.menuItemResume -> {
                drawable.resume()
                menuItemVisible(false,true)
                true
            }
            // 停止
            R.id.menuItemPause -> {
                drawable.pause()
                menuItemVisible(true,false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            D2x01Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
