package com.depromeet.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.designsystem.SpotTeamLabel
import com.depromeet.presentation.databinding.ActivityStadiumBinding
import com.depromeet.presentation.extension.getCompatibleParcelableExtra
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.viewfinder.sample.Stadium
import com.depromeet.presentation.viewfinder.viewmodel.StadiumViewModel
import com.depromeet.presentation.viewfinder.web.AndroidBridge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumActivity : BaseActivity<ActivityStadiumBinding>({
    ActivityStadiumBinding.inflate(it)
}) {
    companion object {
        const val STADIUM_AREA = "stadium_area"
        private const val BASE_URL = "file:///android_asset/web/"
        private const val ENCODING_UTF8 = "UTF-8"
        private const val MIME_TYPE_TEXT_HTML = "text/html"

        /** @test */
        private const val SVG_URL = "https://svgshare.com/i/18Br.svg"
    }

    private val viewModel: StadiumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getStadiumExtra()
        configureWebViewSetting()
        interactionWebView()
        initEvent()

        viewModel.downloadFileFromServer(SVG_URL)
        observeData()
    }

    private fun initEvent() {
        binding.spotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.spotAppbar.setMenuOnClickListener {
            // go to main activity
        }

        binding.ivClose.setOnClickListener {
            binding.clZoomDescription.visibility = View.INVISIBLE
        }
    }

    private fun getStadiumExtra() {
        intent?.getCompatibleParcelableExtra<Stadium>(StadiumSelectionActivity.STADIUM_EXTRA)
            ?.let { stadium ->
                binding.tvStadiumTitle.text = stadium.title
                binding.ivStadium.load(stadium.imageUrl) {
                    transformations(RoundedCornersTransformation(10f))
                }
                stadium.team.forEach { label ->
                    binding.llStadiumTeamLabel.addView(
                        SpotTeamLabel(this).apply {
                            teamType = label
                        }
                    )
                }
            }
    }

    private fun configureWebViewSetting() {
        binding.wvStadium.setInitialScale(0)
        binding.wvStadium.settings.apply {
            builtInZoomControls = true
            displayZoomControls = false
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
        }
        binding.wvStadium.addJavascriptInterface(
            AndroidBridge { sectionId ->
                val id = sectionId.split("_").last()
                if (id.isEmpty()) return@AndroidBridge

                startToStadiumDetailActivity(id)
            },
            AndroidBridge.JAVASCRIPT_OBJ
        )

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if (binding.clZoomDescription.isVisible) {
            super.dispatchTouchEvent(ev)
        } else {
            if (ev?.action == MotionEvent.ACTION_UP) {
                binding.tvZoomDescription.visibility = View.VISIBLE
            } else {
                if ((ev?.y ?: 0f) >= resources.displayMetrics.heightPixels * (1 / 6.0)) {
                    binding.tvZoomDescription.visibility = View.INVISIBLE
                }
            }
            super.dispatchTouchEvent(ev)
        }
    }

    private fun interactionWebView() {
        binding.wvStadium.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectJavaScriptFunction()
            }
        }
    }

    private fun observeData() {
        viewModel.htmlBody.asLiveData().observe(this) { uiState ->
            when (uiState) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    toast("실패")
                }

                is UiState.Loading -> {
                    toast("로딩중")
                }

                is UiState.Success -> {
                    binding.wvStadium.loadDataWithBaseURL(
                        BASE_URL, uiState.data, MIME_TYPE_TEXT_HTML,
                        ENCODING_UTF8, null
                    )
                    binding.wvStadium.webChromeClient = WebChromeClient()
                }
            }
        }
    }

    private fun startToStadiumDetailActivity(fromWeb: String) {
        Intent(this, StadiumDetailActivity::class.java).apply {
            putExtra(STADIUM_AREA, fromWeb)
        }.let(::startActivity)
    }

    private fun injectJavaScriptFunction() {
        binding.wvStadium.loadUrl(AndroidBridge.INJECT_STADIUM_BLOCK_NUMBER)
    }

    override fun onDestroy() {
        clearWebViewObject()
        super.onDestroy()
    }

    private fun clearWebViewObject() {
        binding.wvStadium.apply {
            removeJavascriptInterface(AndroidBridge.JAVASCRIPT_OBJ)
            webChromeClient = null
            clearCache(true)
            clearHistory()
            removeAllViews()
            destroy()
        }
    }
}