package com.dpm.presentation.scheme

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.dpm.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySchemeBinding
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.login.ui.KakaoSignupActivity
import com.dpm.presentation.login.ui.SignUpActivity
import com.dpm.presentation.scheme.SchemeKey.NAV_REVIEW
import com.dpm.presentation.scheme.SchemeKey.NAV_REVIEW_DETAIL
import com.dpm.presentation.scheme.viewmodel.SchemeState
import com.dpm.presentation.scheme.viewmodel.SchemeViewModel
import dagger.hilt.android.AndroidEntryPoint

object SchemeKey {
    const val STADIUM_ID = "stadiumId"
    const val BLOCK_CODE = "blockCode"
    const val REVIEW_ID = "reviewId"

    const val NAV_REVIEW = "nav_review"
    const val NAV_REVIEW_DETAIL = "nav_review_detail"
}

@AndroidEntryPoint
class SchemeActivity : BaseActivity<ActivitySchemeBinding>(ActivitySchemeBinding::inflate) {

    companion object {
        /**
         * 딥링크 URL 파라미터인 KEY 상수 정의
         * kakaolink://[kakao_native_key]?[key1]=[value1]&[key2]=[value2]]
         * Link(
         *     androidExecutionParams = mapOf("section" to "오렌지석", "block" to "102")
         * )
         *
         * val section = handleQueryParameter(appLinkData, "section") // 오렌지석
         * val block = handleQueryParameter(appLinkData, "block") // 102
         */
        const val EXAMPLE_KEY = "example_key"
    }

    private val viewModel: SchemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        initObserve()
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            handleNavReview(appLinkData)
        }
    }

    private fun handleQueryParameter(
        uri: Uri?,
        key: String
    ): String? {
        return uri?.getQueryParameter(key)
    }


    private fun handlePathParameter(
        uri: Uri?
    ) {
        uri?.lastPathSegment?.also { pathParameter ->
            Uri.parse("content://com.depromeet.spot/")
                .buildUpon()
                .appendPath(pathParameter)
                .build().also { appData ->
                    toast(appData.toString())
                }
        }
    }

    private fun initObserve() {
        viewModel.state.asLiveData().observe(this) {
            when (it) {
                is SchemeState.NonLogin<*> -> {
                    when (val data = it.data) {
                        is SchemeState.NavReview -> {
                            navigateToSignUpActivity(data)
                        }
                        is SchemeState.NavReviewDetail -> {
                            navigateToSignUpActivity(data)
                        }
                    }
                }
                is SchemeState.NavReview -> {
                    navigateToHomeActivity(it)
                }
                is SchemeState.NavReviewDetail -> {
                    navigateToHomeActivity(it)
                }
                is SchemeState.Failed -> {
                    navigateToSignUpActivity(it)
                }
                SchemeState.Nothing -> Unit
            }
        }
    }

    private fun handleNavReview(
        uri: Uri?
    ) {
        if (handleQueryParameter(uri, SchemeKey.STADIUM_ID).isNullOrEmpty()
            || handleQueryParameter(uri, SchemeKey.BLOCK_CODE).isNullOrEmpty()
        ) {
            navigateToSignUpActivity()
            return
        }

        viewModel.navigateToDetailReview(handleQueryParameter(uri, SchemeKey.STADIUM_ID)?.toInt() ?: 0,  handleQueryParameter(uri, SchemeKey.BLOCK_CODE) ?: "")
    }

    private fun navigateToSignUpActivity() {
        finish()
        Intent(this, SignUpActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }.let { startActivity(it) }
    }

    private fun <T> navigateToSignUpActivity(data: T) {
        finish()
        Intent(this, SignUpActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            when (data) {
                is SchemeState.NavReview -> {
                    putExtra(NAV_REVIEW, data)
                }
                is SchemeState.NavReviewDetail -> {
                    putExtra(NAV_REVIEW_DETAIL, data)
                }
            }
        }.let { startActivity(it) }
    }

    private fun <T> navigateToHomeActivity(data: T) {
        finish()
        Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            when(data) {
                is SchemeState.NavReview -> {
                    putExtra(NAV_REVIEW, data)
                }
                is SchemeState.NavReviewDetail -> {
                    putExtra(NAV_REVIEW_DETAIL, data)
                }
            }
        }.let { startActivity(it) }
    }
}