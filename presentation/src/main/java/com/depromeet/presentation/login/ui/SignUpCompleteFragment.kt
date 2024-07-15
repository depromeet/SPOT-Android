package com.depromeet.presentation.login.ui

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSignupCompleteBinding

class SignUpCompleteFragment : BindingFragment<FragmentSignupCompleteBinding>(
    R.layout.fragment_signup_complete, { inflater, container, attachToRoot ->
        FragmentSignupCompleteBinding.inflate(inflater, container, attachToRoot)
    }
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
    }

    private fun initClickListener() {
        binding.tvSignupCompleteBtn.setOnClickListener {
            //Todo Main 으로 이동
        }
    }
}