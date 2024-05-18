package com.asyabab.endora.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_favorit.*
import kotlinx.android.synthetic.main.fragment_favorit.view.*

class WebViewActivity : BaseActivity() {
    var webView: WebView? = null
    var bar: ProgressBar? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        webView = findViewById(R.id.webView)
        bar = findViewById(R.id.progressBar2)
        webView!!.webViewClient = AppWebViewClients(bar!!)
        webView!!.settings.javaScriptEnabled = true
        webView!!.loadUrl("https://www.endoracare.com")

        swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            webView!!.webViewClient = AppWebViewClients(bar!!)
            webView!!.settings.javaScriptEnabled = true
            webView!!.loadUrl("https://www.endoracare.com")
            swipeResfresh.isRefreshing = false;

        })
    }

    class AppWebViewClients(private val progressBar: ProgressBar) : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            // TODO Auto-generated method stub
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }

        init {
            progressBar.visibility = View.VISIBLE
        }
    }
}