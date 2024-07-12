package com.depromeet.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.designsystem.SpotTeamLabel
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumBinding
import com.depromeet.presentation.extension.getCompatibleParcelableExtra
import com.depromeet.presentation.util.getHTMLBody
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
        private const val JAVASCRIPT_OBJ = "javascript_obj"
    }

    private val viewModel: StadiumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStadiumExtra()
        setUpWebClient()

        binding.spotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.spotAppbar.setMenuOnClickListener {
            // go to main activity
        }

        binding.ivClose.setOnClickListener {
            binding.clZoomDescription.visibility = View.INVISIBLE
        }

        binding.btnRefresh.setOnClickListener {
            binding.btnRefresh.visibility = View.INVISIBLE
//            resetZoom()
        }

        viewModel.downloadFileFromServer("https://svgshare.com/i/184q.svg")

        viewModel.svgString.observe(this) { svgBody ->
            binding.wvStadium.loadDataWithBaseURL(
                BASE_URL, getHTMLBody(svgBody), "text/html",
                "UTF-8", null
            )
        }
    }

    private fun setStadiumExtra() {
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

    private fun setUpWebClient() {
        binding.wvStadium.settings.apply {
            builtInZoomControls = true
            displayZoomControls = false
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
        }
        binding.wvStadium.addJavascriptInterface(
            AndroidBridge { fromWeb ->
                startToStadiumDetailActivity(fromWeb)
            },
            JAVASCRIPT_OBJ
        )
        binding.wvStadium.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectJavaScriptFunction()
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
//                binding.btnRefresh.visibility = View.VISIBLE
//
//                if (abs(oldScale - newScale) > 0.1) {
//                    binding.tvZoomDescription.visibility = View.INVISIBLE
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        binding.tvZoomDescription.visibility = View.VISIBLE
//                    }, 500)
//                }
            }
        }

        binding.wvStadium.webChromeClient = WebChromeClient()
    }

    private fun startToStadiumDetailActivity(fromWeb: String) {
        val intent = Intent(
            this,
            StadiumDetailActivity::class.java
        ).apply {
            putExtra(STADIUM_AREA, fromWeb)
        }
        startActivity(intent)
    }

    private fun resetZoom() {
        binding.wvStadium.evaluateJavascript("javascript:resetZoom()", null)
    }

    private fun injectJavaScriptFunction() {
        val textToAndroid = "javascript: window.androidObj.textToAndroid = function(message) { " +
                "javascript_obj" + ".textFromWeb(message) }"
        binding.wvStadium.loadUrl(textToAndroid)
    }

    override fun onDestroy() {
        binding.wvStadium.apply {
            removeJavascriptInterface(JAVASCRIPT_OBJ)
            webChromeClient = null
            clearCache(true)
            clearHistory()
            removeAllViews()
            destroy()
        }
        super.onDestroy()
    }
}