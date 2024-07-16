package com.depromeet.presentation.login.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentKakaoSignupBinding
import com.depromeet.presentation.login.viewmodel.SignUpViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun initClickListeners() {
        binding.clKakaoSignupButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Log.e("QQKAKAO", "로그인 실패 $error")
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoTalk
                        }

                        else {
                            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback) // 카카오 이메일 로그인
                        }
                    }

                    else if (token != null) {
                        Log.e("QQKAKAO", "로그인 성공 ${token.accessToken}")
                    }

                     else {
                        Log.e("QQKAKAO", "로그인 실패")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback)
            }
        }
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("QQKAKAO", "로그인 실패 $error")
        } else if (token != null) {
            Log.e("QQKAKAO", "로그인 성공 ${token.accessToken}")
        } else {
            Log.e("QQKAKAO", "로그인 실패")
        }
    }
}