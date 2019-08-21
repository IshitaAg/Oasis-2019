package com.dvm.appd.bosm.dbg.more

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dvm.appd.bosm.dbg.R
import kotlinx.android.synthetic.main.fragment_fragment_web_page.*

class FragmentWebPage : Fragment() {

    lateinit var link : String
    lateinit var title : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            link = it.getString("link")!!
            title = it.getString("title")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_web_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_commonWebView_title.text = title
        webView_commonWebView_webPage.loadUrl(link)
    }
}