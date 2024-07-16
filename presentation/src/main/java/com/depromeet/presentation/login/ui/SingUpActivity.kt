package com.depromeet.presentation.login.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySignupBinding
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingUpActivity : BaseActivity<ActivitySignupBinding>({
    ActivitySignupBinding.inflate(it)
}) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        supportFragmentManager.commit(false) {
            add(binding.flSignupContainer.id, KakaoSignupFragment())
        }
    }
}