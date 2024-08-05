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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initComposeView()
//        initView()
//        initClickListeners()
//        initObservers()
    }

    private fun initComposeView() {
        binding.kakaoSignComposeView.setContent {
            MaterialTheme {
                KakaoSignupScreen()
            }
        }
    }

    private fun initView() {
//        val signUpTextAndPoint = listOf(
//            Triple("시야찾기로 원하는 야구장\n" +
//                    "자리를 빠르게 알아봐요!", 0, 3),
//            Triple("내 시야 후기를 올려서\n" +
//                    "캐릭터를 성장시켜요!", 0, 6),
//            Triple("내 소중한 시야 기록을\n" +
//                    "한 자리에서 봐요!", 6, 10),
//        )
//        binding.vpSignupIntroduce.adapter = SignupViewPagerAdapter(signUpTextAndPoint)
//        binding.vpSignupIntroduce.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        binding.ciSignupIntroduceIndicator.setViewPager(binding.vpSignupIntroduce)
//        binding.vpSignupIntroduce.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 -> {
//                        binding.ivSpotSignupImage.setImageResource(R.drawable.ic_signup_image_1)
//                    }
//
//                    1 -> {
//                        binding.ivSpotSignupImage.setImageResource(R.drawable.ic_signup_image_3)
//                    }
//
//                    2 -> {
//                        binding.ivSpotSignupImage.setImageResource(R.drawable.ic_signup_image_2)
//                    }
//                }
//            }
//        })
    }

    private fun initClickListeners() {
//        binding.clKakaoSignupButton.setOnClickListener {
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
//                    if (error != null) {
//                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
//                            return@loginWithKakaoTalk
//                        }
//                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//                    } else if (token != null) {
//                        Timber.d("KakaoToken: $token")
//                        signUpViewModel.updateKakaoToken(token.accessToken)
//                    }
//                }
//            } else {
//                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//            }
//        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            toast("카카오 로그인에 실패하였습니다.")
        } else if (token != null) {
            signUpViewModel.updateKakaoToken(token.accessToken)
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