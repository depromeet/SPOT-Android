package com.depromeet.presentation.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentLoginInfoBinding
import com.depromeet.presentation.login.ui.SignUpActivity

class LoginInfoFragment : BindingFragment<FragmentLoginInfoBinding>(
    layoutResId = R.layout.fragment_login_info,
    FragmentLoginInfoBinding::inflate
) {
    private val viewModel by activityViewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initClickListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.withdrawState.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is WithdrawState.Loading -> { }
                is WithdrawState.Success -> {
                    Intent(requireContext(), SignUpActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(this)
                    }
                }
                is WithdrawState.Error -> { }

                is WithdrawState.Init -> { }
            }
        }
    }

    private fun initView() {

    }

    private fun initClickListener() {
        binding.tvLoginInfoWithdraw.setOnClickListener {
            viewModel.withdraw()
        }
    }
}