package milu.kiriu2010.milucal.gui.misc


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import milu.kiriu2010.milumathcaras.R


class AboutFragment : androidx.fragment.app.Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val ctx = context ?: return view

        // バージョンを表示
        val textViewVer = view.findViewById<TextView>(R.id.textViewVer)
        val packageInfo = ctx.packageManager?.getPackageInfo(context?.packageName!!, 0)
        textViewVer.text = "ver %s".format(packageInfo?.versionName)

        // "Rate Me"ボタン
        val btnRateMe = view.findViewById<Button>(R.id.btnRateMe)
        btnRateMe.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + ctx.packageName))
                )
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.packageName )))
            }
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            AboutFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
