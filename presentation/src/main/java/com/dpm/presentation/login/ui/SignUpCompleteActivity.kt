package com.dpm.presentation.login.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.depromeet.presentation.databinding.ActivitySignupCompleteBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.extension.getCompatibleParcelableExtra
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.scheme.viewmodel.SchemeState

class SignUpCompleteActivity : BaseActivity<ActivitySignupCompleteBinding>(
    { ActivitySignupCompleteBinding.inflate(it) }
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
    }

    private fun initView() {
        welcomeSetText()
        welcomeImageSetAnimation()
    }

    private fun welcomeSetText() {
        val nickname = intent.getStringExtra("nickname") ?: ""
        val welcomeMessage = "${nickname}님 반가워요!"

        val spannableString = SpannableString(welcomeMessage)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#42D596")),
            0,
            nickname.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvSignupCompleteTitle.text = spannableString
    }

    private fun welcomeImageSetAnimation() {
        binding.ivSignupComplete.apply {
            alpha = 0f
            scaleX = 0.5f
            scaleY = 0.5f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .start()
        }
    }

    private fun initEvent() {
        binding.tvSignupCompleteBtn.setOnClickListener {
            Intent(this, HomeActivity::class.java).apply {
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
    }

    private fun handleIntentExtra(): SchemeState {
        val navReview = intent.getCompatibleParcelableExtra<SchemeState.NavReview>(SchemeKey.NAV_REVIEW)
        if (navReview != null) {
            return navReview
        }

        val navReviewDetail = intent.getCompatibleParcelableExtra<SchemeState.NavReviewDetail>(
            SchemeKey.NAV_REVIEW_DETAIL)
        if (navReviewDetail != null) {
            return navReviewDetail
        }

        return SchemeState.Nothing
    }
}