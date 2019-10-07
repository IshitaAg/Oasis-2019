package com.dvm.appd.oasis.dbg.more.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity

import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fragment_web_page.*
import java.lang.Exception

class FragmentWebPage : Fragment() {

    lateinit var link : String
    lateinit var title : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            link = it.getString("link")!!
            title = it.getString("title")!!
        }
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        activity!!.search.isVisible = false
        activity!!.textView7.isVisible = false

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_web_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_commonWebView_title.text = title
        backBtn.setOnClickListener {
            view.findNavController().popBackStack()
        }
        text_commonWebView_title.text = title
        webView_commonWebView_webPage.webViewClient = CustomWebViewClient()
        webView_commonWebView_webPage.loadUrl(link)
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            progress_commonWebView.visibility = View.VISIBLE
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            try {
                progress_commonWebView.visibility = View.INVISIBLE
            } catch (e: Exception) {
                Log.e("WebPage", "An Error Occoured")
            }
            super.onPageFinished(view, url)
        }
    }
}
