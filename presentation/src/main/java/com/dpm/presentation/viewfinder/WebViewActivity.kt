package com.dpm.presentation.viewfinder

import android.os.Bundle
import android.webkit.WebViewClient
import com.dpm.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityWebViewBinding

class WebViewActivity : BaseActivity<ActivityWebViewBinding>(ActivityWebViewBinding::inflate) {
    companion object {
        const val WEB_VIEW_URL = "web_view_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        configurationWebViewSetting()
        loadWebView()
    }

    private fun configurationWebViewSetting() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            domStorageEnabled = true
        }
    }

    private fun loadWebView() {
        intent?.getStringExtra(WEB_VIEW_URL)?.let {
            binding.webView.webViewClient = WebViewClient()
            binding.webView.loadUrl(it)
        } ?: return
    }

    override fun onDestroy() {
        binding.webView.apply {
            clearCache(true)
            clearHistory()
            removeAllViews()
            destroy()
        }
        super.onDestroy()
    }
}