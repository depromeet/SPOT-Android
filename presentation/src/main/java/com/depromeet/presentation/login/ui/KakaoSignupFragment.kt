package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.util.Utils
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentKakaoSignupBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.HomeActivity
import com.depromeet.presentation.login.viewmodel.LoginUiState
import com.depromeet.presentation.login.viewmodel.SignUpViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KakaoSignupFragment : BindingFragment<FragmentKakaoSignupBinding>(
    R.layout.fragment_kakao_signup, { inflater, container, attachToRoot ->
        FragmentKakaoSignupBinding.inflate(inflater, container, attachToRoot)
    }
) {
    private val signUpViewModel by activityViewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initClickListeners()
        initObservers()
    }

    private fun initView() {
        val signUpTextAndPoint = listOf(
            Triple("시야찾기로 원하는 야구장\n" +
                    "자리를 빠르게 알아봐요!", 0, 3),
            Triple("내 시야 후기를 올려서\n" +
                    "캐릭터를 성장시켜요!", 0, 6),
            Triple("내 소중한 시야 기록을\n" +
                    "한 자리에서 봐요!", 6, 10),
        )
        binding.vpSignupIntroduce.adapter = SignupViewPagerAdapter(signUpTextAndPoint)
        binding.vpSignupIntroduce.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.ciSignupIntroduceIndicator.setViewPager(binding.vpSignupIntroduce)
        binding.vpSignupIntroduce.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.ivSpotSignupImage.setImageResource(R.drawable.ic_signup_image_1)
                    }

                    1 -> {
                        binding.ivSpotSignupImage.setImageResource(R.drawable.ic_signup_image_3)
                    }

                    2 -> {
                        binding.ivSpotSignupImage.setImageResource(R.drawable.ic_signup_image_2)
                    }
                }
            }
        })
    }

    private fun initClickListeners() {
        binding.clKakaoSignupButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                    } else if (token != null) {
                        signUpViewModel.updateKakaoToken(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            toast("카카오 로그인에 실패하였습니다.")
        } else if (token != null) {
            signUpViewModel.updateKakaoToken(token.accessToken)
        }
    }

    private fun initObservers() {
        signUpViewModel.kakaoToken.asLiveData().observe(viewLifecycleOwner) { token ->
            if (token.isNotEmpty() && signUpViewModel.initKakaoLoginFragment.value) {
                signUpViewModel.initKakaoLoginFragment(false)
                parentFragmentManager.commit {
                    replace(R.id.fl_signup_container, NicknameInputFragment())
                    addToBackStack(null)
                }
            }
        }

        signUpViewModel.loginUiState.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                LoginUiState.LoginSuccess -> {
                    Intent(requireContext(), HomeActivity::class.java).apply {
                        startActivity(this)
                        requireActivity().finish()
                    }
                }
                LoginUiState.Loading -> {

                }
                LoginUiState.Initial -> { }
            }
        }
    }
}