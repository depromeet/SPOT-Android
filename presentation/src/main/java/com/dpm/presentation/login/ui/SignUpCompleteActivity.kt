package com.dpm.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import com.depromeet.presentation.databinding.ActivitySignupCompleteBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.home.HomeActivity

class SignUpCompleteActivity : BaseActivity<ActivitySignupCompleteBinding>(
    { ActivitySignupCompleteBinding.inflate(it) }
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEvent()
    }

    private fun initEvent() {
        binding.tvSignupCompleteBtn.setOnClickListener {
            Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                finish()
            }
        }
    }
}