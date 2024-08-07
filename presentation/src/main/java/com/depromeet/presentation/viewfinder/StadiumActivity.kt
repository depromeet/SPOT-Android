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
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.HomeActivity
import com.depromeet.presentation.util.SpannableStringUtils
import com.depromeet.presentation.viewfinder.viewmodel.StadiumViewModel
import com.depromeet.presentation.viewfinder.web.AndroidBridge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumActivity : BaseActivity<ActivityStadiumBinding>({
    ActivityStadiumBinding.inflate(it)
}) {
    companion object {
        const val STADIUM_ID = "stadium_id"
        const val STADIUM_BLOCK_ID = "stadium_block_id"
        private const val BASE_URL = "file:///android_asset/web/"
        private const val ENCODING_UTF8 = "UTF-8"
        private const val MIME_TYPE_TEXT_HTML = "text/html"
    }

    private val viewModel: StadiumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        initObserve()
    }

    private fun initView() {
        binding.root.isClickable = !binding.clZoomDescription.isVisible
        setTextZoomDescriptionColor()
        getStadiumIdExtra()
        configureWebViewSetting()
    }

    private fun initEvent() {
        interactionWebView()
        onClickBack()
        onClickClose()
        onClickZoomDescriptionDim()
    }

    private fun initObserve() {
        viewModel.stadium.asLiveData().observe(this) { stadium ->
            when (stadium) {
                is UiState.Empty -> Unit
                is UiState.Failure -> toast(stadium.msg)
                is UiState.Loading -> {
                    startShimmer()
                }

                is UiState.Success -> {
                    viewModel.stadiumId = stadium.data.id
                    with(binding) {
                        tvStadiumTitle.text = stadium.data.name
                        ivStadium.load(stadium.data.thumbnail) {
                            transformations(RoundedCornersTransformation(10f))
                        }
                        llStadiumTeamLabel.removeAllViews()
                        stadium.data.homeTeams.forEach { homeTeam ->
                            llStadiumTeamLabel.addView(
                                SpotTeamLabel(this@StadiumActivity).apply {
                                    teamType = homeTeam.alias
                                }
                            )
                        }
                    }
                    viewModel.downloadFileFromServer(stadium.data.stadiumUrl)
                }
            }
        }
        viewModel.htmlBody.asLiveData().observe(this) { uiState ->
            when (uiState) {
                is UiState.Empty -> Unit
                is UiState.Failure -> toast(uiState.msg)
                is UiState.Loading -> Unit
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

    private fun getStadiumIdExtra() {
        intent?.getIntExtra(StadiumSelectionActivity.STADIUM_EXTRA_ID, 0)?.let { stadiumId ->
            viewModel.getStadium(stadiumId)
        } ?: return
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

    private fun setTextZoomDescriptionColor() {
        binding.tvZoomDescription.text = SpannableStringUtils(this).toColorSpan(
            com.depromeet.designsystem.R.color.color_action_enabled,
            binding.tvZoomDescription.text, 4, 12
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
                stopShimmer()
            }
        }
    }

    private fun startShimmer() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.clMainContainer.visibility = View.INVISIBLE
    }

    private fun stopShimmer() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.INVISIBLE
        binding.clMainContainer.visibility = View.VISIBLE
    }

    private fun onClickBack() {
        binding.spotAppbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun onClickClose() {
        binding.spotAppbar.setMenuOnClickListener {
            startToHomeActivity()
        }

        binding.ivClose.setOnClickListener {
            binding.clZoomDescription.visibility = View.INVISIBLE
        }
    }

    private fun onClickZoomDescriptionDim() {
        binding.clZoomDescription.setOnClickListener {
            binding.clZoomDescription.visibility = View.INVISIBLE
        }
    }

    private fun startToStadiumDetailActivity(id: String) {
        Intent(this, StadiumDetailActivity::class.java).apply {
            putExtra(STADIUM_ID, viewModel.stadiumId)
            putExtra(STADIUM_BLOCK_ID, id)
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

    private fun startToHomeActivity() {
        Intent(
            this,
            HomeActivity::class.java
        ).apply {
            startActivity(this)
            finishAffinity()
        }
    }
}