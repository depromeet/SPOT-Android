package com.depromeet.presentation.viewfinder

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.JavascriptInterface
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
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.util.getHTMLBody
import com.depromeet.presentation.viewfinder.sample.Stadium
import com.depromeet.presentation.viewfinder.viewmodel.StadiumViewModel
import kotlin.math.abs

class StadiumActivity : BaseActivity<ActivityStadiumBinding>({
    ActivityStadiumBinding.inflate(it)
}) {
    companion object {
        const val STADIUM_AREA = "stadium_area"
        private const val BASE_URL = "file:///android_asset/web/"
        private const val JAVASCRIPT_OBJ = "javascript_obj"
    }

    private val viewModel: StadiumViewModel by viewModels()
    private val stadiumDetailFragment by lazy {
        StadiumDetailFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpWebClient()

        val arg =
            intent?.getCompatibleParcelableExtra<Stadium>(StadiumSelectionActivity.STADIUM_EXTRA)

        with(binding) {
            tvStadiumTitle.text = arg?.title
            ivStadium.load(arg?.imageUrl) {
                transformations(RoundedCornersTransformation(10f))
            }
            arg?.team?.forEach { label ->
                llStadiumTeamLabel.addView(
                    SpotTeamLabel(this@StadiumActivity).apply {
                        teamType = label
                    }
                )
            }
        }
        binding.spotAppbar.setNavigationOnClickListener {
            finish()
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

    private fun setUpWebClient() {
        binding.wvStadium.settings.builtInZoomControls = true
        binding.wvStadium.settings.displayZoomControls = false
        binding.wvStadium.settings.javaScriptEnabled = true
        binding.wvStadium.settings.domStorageEnabled = true
        binding.wvStadium.addJavascriptInterface(
            JavaScriptInterface(),
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

    private fun resetZoom() {
        binding.wvStadium.evaluateJavascript("javascript:resetZoom()", null)
    }

    private fun injectJavaScriptFunction() {
        val textToAndroid = "javascript: window.androidObj.textToAndroid = function(message) { " +
                "javascript_obj" + ".textFromWeb(message) }"
        binding.wvStadium.loadUrl(textToAndroid)
    }

    inner class JavaScriptInterface {
        @JavascriptInterface
        fun textFromWeb(fromWeb: String) {
            val bundle = Bundle()
            bundle.putString(STADIUM_AREA, fromWeb)
            stadiumDetailFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_detail, stadiumDetailFragment, StadiumDetailFragment.TAG)
                .commit()
        }
    }

    override fun onDestroy() {
        binding.wvStadium.removeJavascriptInterface(JAVASCRIPT_OBJ)
        super.onDestroy()
    }
}