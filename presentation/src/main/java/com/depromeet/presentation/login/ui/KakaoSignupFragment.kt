package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class KakaoSignupFragment : BindingFragment<FragmentKakaoSignupBinding>(
    R.layout.fragment_kakao_signup, { inflater, container, attachToRoot ->
        FragmentKakaoSignupBinding.inflate(inflater, container, attachToRoot)
    }
) {
    private val signUpViewModel by activityViewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initObservers()
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
            if (token.isNotEmpty()) {
                parentFragmentManager.commit {
                    addToBackStack(null)
                    add(R.id.fl_signup_container, NicknameInputFragment())
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