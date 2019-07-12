package milu.kiriu2010.milumathcaras.gui.drawfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Switch
import milu.kiriu2010.gui.view.MyGLES32View
import milu.kiriu2010.math.MyMathUtil
import milu.kiriu2010.milumathcaras.R

import milu.kiriu2010.milumathcaras.entity.DrawData
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.milumathcaras.entity.DrawDataID
import milu.kiriu2010.milumathcaras.entity.DrawFragmentType
import milu.kiriu2010.milumathcaras.gui.draw.MgRendererFactory
import milu.kiriu2010.milumathcaras.gui.main.NotifyCallback

private const val ARG_PARAM1 = "drawdata"

// ----------------------------------------
// OpenGLコンテンツを描画をするフラグメント
// ----------------------------------------
// ・描画用のMyGLES32View１つ
// ----------------------------------------
class D3xES32x01Fragment : Fragment()
    , NotifyCallback {

    // 描画データ
    private lateinit var drawData: DrawData

    // 描画するビュー
    private lateinit var myGLES32View: MyGLES32View

    // レンダ―
    private lateinit var renderer: MgRenderer

    // シェーダ選択用スピナ―
    private lateinit var spinnerShader: Spinner

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
        val view = inflater.inflate(R.layout.fragment_d3_es32_01, container, false)

        // 描画するビュー
        myGLES32View = view.findViewById(R.id.myGLES32ViewD3x01)
        renderer = MgRendererFactory.createInstance(drawData.id,context!!,this)
        //renderer.setMotionParam(*drawData.motionImageParam)
        renderer.setMotionParam(drawData.motionImageV2Param)
        myGLES32View.setRenderer(renderer)
        myGLES32View.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.receiveTouch(event,myGLES32View.width,myGLES32View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.receiveTouch(event,myGLES32View.width,myGLES32View.height)
                }
                else -> {
                }
            }
            true
        }

        // シェーダ選択用スピナ―
        spinnerShader = view.findViewById<Spinner>(R.id.spinnerShaderD3x01)
        spinnerShader.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // http://android-note.open-memo.net/sub/spinner--get-resource-id-for-selected-item.html
                val array = resources.obtainTypedArray(R.array.shaderlist)
                val itemId = array.getResourceId(position,R.string.shader_simple)
                renderer.shaderSwitch = when (itemId) {
                    // 特殊効果なし
                    R.string.shader_simple -> 0
                    // 平行光源
                    R.string.shader_directional_light -> 1
                    // 環境光
                    R.string.shader_ambient_light -> 2
                    // 拡散光
                    R.string.shader_specular_light -> 3
                    // フォーンシェーディング
                    R.string.shader_phong_shading -> 4
                    // 点光源
                    R.string.shader_point_light -> 5
                    // 点で描画(POINTS)
                    R.string.shader_gl_points -> 6
                    // 線で描画(LINES)
                    R.string.shader_gl_lines -> 7
                    // テクスチャ
                    R.string.shader_texture -> 8
                    else -> 0
                }
                // 使わなくなったら解放
                array.recycle()
            }
        }

        // 座標軸ON/OFF
        val switchAxis = view.findViewById<Switch>(R.id.switchAxisD3x01)
        switchAxis.setOnCheckedChangeListener { _, isChecked ->
            renderer.displayAxis = isChecked
        }

        // X座標軸による回転ON/OFF
        val checkBoxD3x01X = view.findViewById<CheckBox>(R.id.checkBoxXD3x01)
        checkBoxD3x01X.setOnCheckedChangeListener { _, isChecked ->
            renderer.rotateAxis[0] = isChecked
        }

        // Y座標軸による回転ON/OFF
        val checkBoxD3x01Y = view.findViewById<CheckBox>(R.id.checkBoxYD3x01)
        checkBoxD3x01Y.setOnCheckedChangeListener { _, isChecked ->
            renderer.rotateAxis[1] = isChecked
        }

        // Z座標軸による回転ON/OFF
        val checkBoxD3x01Z = view.findViewById<CheckBox>(R.id.checkBoxZD3x01)
        checkBoxD3x01Z.setOnCheckedChangeListener { _, isChecked ->
            renderer.rotateAxis[2] = isChecked
        }

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
            D3xES32x01Fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,drawData)
                }
            }
    }
}
