package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSignupCompleteBinding
import com.depromeet.presentation.home.HomeActivity

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
            Intent(requireContext(), HomeActivity::class.java).apply {
                startActivity(this)
                requireActivity().finish()
            }
        }
    }
}