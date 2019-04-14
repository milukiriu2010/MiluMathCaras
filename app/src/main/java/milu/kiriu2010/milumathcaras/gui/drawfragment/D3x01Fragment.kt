package milu.kiriu2010.milumathcaras.gui.drawfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import milu.kiriu2010.gui.view.MyGLES20View
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.R

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.milumathcaras.gui.draw.MgRenderer
import milu.kiriu2010.milumathcaras.gui.draw.MgRendererFactory
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

private const val ARG_PARAM1 = "drawdata"

// ----------------------------------------
// OpenGLコンテンツを描画をするフラグメント
// ----------------------------------------
// ・描画用のMyGLES20View１つ
// ----------------------------------------
class D3x01Fragment : Fragment()
    , NotifyCallback {

    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var myGLES20View: MyGLES20View

    // レンダ―
    private lateinit var renderer: MgRenderer

    // メニュー(再開)
    private var menuItemResume: MenuItem? = null

    // メニュー(停止)
    private var menuItemPause: MenuItem? = null

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
        val view = inflater.inflate(R.layout.fragment_d3_01, container, false)

        // 描画するビュー
        myGLES20View = view.findViewById(R.id.myGLES20View)
        renderer = MgRendererFactory.createInstance(drawData.id,this)
        myGLES20View.setRenderer(renderer)

        return view
    }

    override fun onResume() {
        //Log.d(javaClass.simpleName,"motionImageParam.size[${drawData.motionImageParam.size}]")

        // アニメーション描画開始
        //drawable.calStart(true,*drawData.motionImageParam)
        super.onResume()
    }

    override fun onPause() {
        // 描画に使うスレッドを解放する
        // 画面を消したときスレッドが止まるようにするためonPauseで呼び出している
        //drawable.calStop()

        // シェーダ終了処理
        renderer.closeShader()
        super.onPause()
    }

    // ---------------------------------
    // 通知データを受信する
    // ---------------------------------
    override fun receive(data: Float) {
        // 小数点以下の桁数
        val numOfDecimals = MyMathUtil.getNumberOfDecimals(data)

        // 媒介変数の値をビューに表示する
        /*
        textView.text = when (numOfDecimals) {
            0 -> data.toInt().toString()
            else -> data.toString()
        }
        */
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
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_basic,menu)
        // メニュー(再開)
        menuItemResume = menu?.findItem(R.id.menuItemResume)
        // メニュー(停止)
        menuItemPause  = menu?.findItem(R.id.menuItemPause)
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
        return when (item?.itemId) {
            /*
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
            */
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            D3x01Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
