package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import com.depromeet.core.base.BaseActivity
import com.depromeet.domain.preference.SharedPreference
import com.depromeet.presentation.databinding.ActivitySignupBinding
import com.depromeet.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
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
        if (sharedPreference.token.isNotEmpty()) {
            // 메인에서 해당 토큰으로 실행했을 때 만료되면 토큰 초기화 시키고 다시 로그인 화면으로 이동
            Intent(this@SignUpActivity, HomeActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        } else {
            supportFragmentManager.commit(false) {
                add(binding.flSignupContainer.id, KakaoSignupFragment())
            }
        }
    }
}