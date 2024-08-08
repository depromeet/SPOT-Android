package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentKakaoSignupBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.HomeActivity
import com.depromeet.presentation.login.ui.compose.KakaoSignupScreen
import com.depromeet.presentation.login.viewmodel.KakaoSignupViewModel
import com.depromeet.presentation.login.viewmodel.LoginUiState
import com.depromeet.presentation.viewfinder.compose.StadiumDetailScreen
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class KakaoSignupActivity : BaseActivity<FragmentKakaoSignupBinding>({
    FragmentKakaoSignupBinding.inflate(it)
}) {
    private val signUpViewModel by viewModels<KakaoSignupViewModel>()

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            toast("카카오 로그인에 실패하였습니다.")
        } else if (token != null) {
            signUpViewModel.updateKakaoToken(token.accessToken)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initComposeView()
        initObservers()
    }

    private fun initComposeView() {
        binding.kakaoSignComposeView.setContent {
            MaterialTheme {
                KakaoSignupScreen() {
                    kakaoLoginCallBack()
                }
            }
        }
    }

    private fun kakaoLoginCallBack() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Timber.d("KakaoToken: $token")
                    signUpViewModel.updateKakaoToken(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun initObservers() {
        signUpViewModel.kakaoToken.asLiveData().observe(this) { token ->
            if (token.isNotEmpty()) {
                Intent(this, NicknameInputActivity::class.java).apply {
                    putExtra("kakaoToken", token)
                    startActivity(this)
                }
            }
        }

        signUpViewModel.loginUiState.asLiveData().observe(this) { state ->
            when (state) {
                LoginUiState.LoginSuccess -> {
                    Intent(this, HomeActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
                LoginUiState.Loading -> {

                }
                LoginUiState.Initial -> { }
            }
        }
    }
}