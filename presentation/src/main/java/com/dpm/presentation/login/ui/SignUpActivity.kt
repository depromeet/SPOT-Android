package com.dpm.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.depromeet.presentation.databinding.ActivitySignupBinding
import com.dpm.core.base.BaseActivity
import com.dpm.domain.preference.SharedPreference
import com.dpm.presentation.extension.getCompatibleParcelableExtra
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.scheme.viewmodel.SchemeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignupBinding>({
    ActivitySignupBinding.inflate(it)
}) {
    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        lifecycleScope.launch {
            delay(1500)
            if (sharedPreference.token.isNotEmpty()) {
                Timber.d("access token: ${sharedPreference.token}")
                navigateToNextScreen(HomeActivity::class.java)
            } else {
                navigateToNextScreen(KakaoSignupActivity::class.java)
            }
        }
    }

    private fun navigateToNextScreen(destinationClass: Class<*>) {
        Intent(this@SignUpActivity, destinationClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            when (val data = handleIntentExtra()) {
                is SchemeState.NavReview -> {
                    putExtra(SchemeKey.NAV_REVIEW, data)
                }
                is SchemeState.NavReviewDetail -> {
                    putExtra(SchemeKey.NAV_REVIEW_DETAIL, data)
                }
                else -> Unit
            }
            startActivity(this)
            finish()
        }
    }

    private fun handleIntentExtra(): SchemeState {
        val navReview = intent.getCompatibleParcelableExtra<SchemeState.NavReview>(SchemeKey.NAV_REVIEW)
        if (navReview != null) {
            return navReview
        }

        val navReviewDetail = intent.getCompatibleParcelableExtra<SchemeState.NavReviewDetail>(SchemeKey.NAV_REVIEW_DETAIL)
        if (navReviewDetail != null) {
            return navReviewDetail
        }

        return SchemeState.Nothing
    }
}