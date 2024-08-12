package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.depromeet.core.base.BaseActivity
import com.depromeet.domain.preference.SharedPreference
import com.depromeet.presentation.databinding.ActivitySignupBinding
import com.depromeet.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                navigateToNextScreen(HomeActivity::class.java)
            } else {
                navigateToNextScreen(KakaoSignupActivity::class.java)
            }
        }
    }

    private fun navigateToNextScreen(destinationClass: Class<*>) {
        Intent(this@SignUpActivity, destinationClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }
}