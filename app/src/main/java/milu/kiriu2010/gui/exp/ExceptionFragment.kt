package milu.kiriu2010.gui.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import milu.kiriu2010.gui.exp.OnRetryListener
import milu.kiriu2010.milumathcaras.R
import milu.kiriu2010.util.MyTool

private const val ARG_PARAM1 = "message"
private const val ARG_PARAM2 = "exp"

// Exceptionを表示するフラグメント
// 2018.09.04 ピンチイン・アウト
class ExceptionFragment: Fragment(), View.OnTouchListener {

    // エラーメッセージ
    private lateinit var strMsg: String
    // エラー詳細
    private lateinit var ex: Exception

    // ピンチイン・アウトを検出するオブジェクト
    private lateinit var detector: ScaleGestureDetector
    // ピンチイン・アウトに使うスケール
    private var scale = 1.0f

    // エラーを表示するフラグメントを生成
    companion object {
        fun newInstance( strMsg: String ) =
                ExceptionFragment().apply {
                    // フラグメントに渡すデータをセット
                    val args = Bundle()
                    args.putString( ARG_PARAM1, strMsg )
                    arguments = args
                }

        fun newInstance( strMsg: String, ex: Exception ) =
                ExceptionFragment().apply {
                    // フラグメントに渡すデータをセット
                    val args = Bundle()
                    args.putString( ARG_PARAM1, strMsg )
                    args.putSerializable( ARG_PARAM2, ex )
                    arguments = args
                }
    }

    // ----------------------------------------------------------
    // 呼び出し時に渡される引数から指定されたエラー情報を取り出す
    // ----------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = this.arguments ?: return
        this.strMsg = args.getString(ARG_PARAM1) ?: ""
        this.ex = args.getSerializable(ARG_PARAM2) as? Exception ?: return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        // XMLからエラー情報を表示するビューを生成
        val view = inflater.inflate( R.layout.fragment_exception, container, false )

        //val ctx = context ?: return view

        // エラーメッセージを表示
        val editMsg = view.findViewById<EditText>(R.id.editMsg)
        editMsg.setText( strMsg )

        // エラー詳細を表示
        val editExp = view.findViewById<EditText>(R.id.editExp)
        // 初期化されていればエラー詳細を表示する
        if ( ::ex.isInitialized ) {
            editExp.setText(MyTool.exp2str(ex))
        }

        // ピンチイン・アウトを検出
        detector = ScaleGestureDetector(activity?.applicationContext, object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                Log.d( javaClass.simpleName, "ScaleGestureDetector.onScale")

                detector?.let {
                    scale *= it.scaleFactor
                    editExp.scaleX = scale
                    editExp.scaleY = scale
                }
                return true
            }

        })

        // TouchイベントをScaleListenerに渡す
        view.setOnTouchListener { _, motionEvent ->
            Log.d( javaClass.simpleName, "MotionEvent[${motionEvent.action}]")
            detector.onTouchEvent(motionEvent)

            true
        }

        return view
    }

    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        Log.d( javaClass.simpleName, "MotionEvent[${motionEvent?.action}]")

        when (motionEvent?.action) {
            MotionEvent.ACTION_MOVE -> {
                detector.onTouchEvent(motionEvent)
            }
        }

        return true
    }
}