package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySignupCompleteBinding
import com.depromeet.presentation.home.HomeActivity

class SignUpCompleteActivity : BaseActivity<ActivitySignupCompleteBinding>(
    { ActivitySignupCompleteBinding.inflate(it) }
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClickListener()
    }

    private fun initClickListener() {
        binding.tvSignupCompleteBtn.setOnClickListener {
            Intent(this, HomeActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}