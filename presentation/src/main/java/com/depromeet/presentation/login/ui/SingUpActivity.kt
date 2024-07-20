package com.depromeet.presentation.login.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.depromeet.core.base.BaseActivity
import com.depromeet.domain.preference.SharedPreference
import com.depromeet.presentation.databinding.ActivitySignupBinding
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingUpActivity : BaseActivity<ActivitySignupBinding>({
    ActivitySignupBinding.inflate(it)
}) {
    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        if (sharedPreference.token.isNotEmpty()) {
            // TODO 메인으로 이동
            // TODO 메인에서 해당 토큰으로 실행했을 때 만료되면 토큰 초기화 시키고 다시 로그인 화면으로 이동
        } else {
            supportFragmentManager.commit(false) {
                add(binding.flSignupContainer.id, KakaoSignupFragment())
            }
        }
    }
}