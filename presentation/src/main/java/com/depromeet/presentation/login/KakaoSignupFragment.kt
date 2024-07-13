package com.depromeet.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentKakaoSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KakaoSignupFragment : BindingFragment<FragmentKakaoSignupBinding>(
    R.layout.fragment_kakao_signup, { inflater, container, attachToRoot ->
        FragmentKakaoSignupBinding.inflate(inflater, container, attachToRoot)
    }
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {
        binding.clKakaoSignupButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fl_signup_container, NicknameInputFragment())
                addToBackStack(null)
            }
        }
    }
}