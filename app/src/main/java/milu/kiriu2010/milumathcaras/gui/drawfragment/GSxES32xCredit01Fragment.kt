package milu.kiriu2010.milumathcaras.gui.drawfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TextView
import milu.kiriu2010.gui.view.MyGLES32View
import milu.kiriu2010.milumathcaras.R

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.entity.DrawFragmentType
import milu.kiriu2010.milumathcaras.gui.draw.d3.MgRendererFactory
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

private const val ARG_PARAM1 = "drawdata"

// ----------------------------------------
// GLSLコンテンツを描画をするフラグメント
// ----------------------------------------
// 製作者名を表示
// ----------------------------------------
class GSxES32xCredit01Fragment : androidx.fragment.app.Fragment()
    , NotifyCallback {

    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var myGLES32View: MyGLES32View

    // レンダ―
    private lateinit var renderer: MgRenderer

    // 製作者の名前を表示するビュー
    private lateinit var textViewName: TextView

    // 製作者のURLを表示するビュー
    private lateinit var textView: TextView

    // メニュー(再開)
    private var menuItemResume: MenuItem? = null

    // メニュー(停止)
    private var menuItemPause: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawData = it.getParcelable(ARG_PARAM1) ?: DrawData(
                DrawDataID.ID_000001_CYCLOID,
                DrawFragmentType.FT_D2_01,"")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_d3_es32_credit_01, container, false)

        // 描画するビュー
        myGLES32View = view.findViewById(R.id.myGLES32ViewD3x02)
        renderer = MgRendererFactory.createInstance(drawData.id,context!!,this)
        drawData.motionImageV2Param.forEach {
            Log.d(javaClass.simpleName,"motion:${it.key}:${it.value.toInt()}")
        }
        renderer.setMotionParam(drawData.motionImageV2Param)
        myGLES32View.setRenderer(renderer)
        myGLES32View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.touchP.x = event.x/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.touchP.x = event.x/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y/renderer.renderH.toFloat()
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.touchP.x = event.x/renderer.renderW.toFloat()
                    renderer.touchP.y = event.y/renderer.renderH.toFloat()
                }
                else -> {
                }
            }
            true
        }

        // 製作者の名前を表示するビュー
        textViewName = view.findViewById(R.id.textViewNameD3x20xC1)
        // 製作者名
        val name = drawData.creditMap["name"]
        textViewName.text = name

        // 製作者のURLを表示するビュー
        textView = view.findViewById(R.id.textViewD3x20xC1)
        // URL
        val url = drawData.creditMap["url"]
        textView.text = url

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
        //val numOfDecimals = MyMathUtil.getNumberOfDecimals(data)

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
                renderer.isRunning = true
                menuItemVisible(false,true)
                true
            }
            // 停止
            R.id.menuItemPause -> {
                renderer.isRunning = false
                menuItemVisible(true,false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(drawData: DrawData) =
            GSxES32xCredit01Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
