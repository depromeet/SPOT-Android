package com.dpm.presentation.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.dpm.core.base.BindingFragment
import com.dpm.domain.preference.SharedPreference
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentLoginInfoBinding
import com.dpm.presentation.login.ui.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginInfoFragment : BindingFragment<FragmentLoginInfoBinding>(
    layoutResId = R.layout.fragment_login_info,
    FragmentLoginInfoBinding::inflate
) {
    @Inject
    lateinit var sharedPreference: SharedPreference

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
        binding.tvSettingNickname.text = sharedPreference.nickname
    }

    private fun initClickListener() {
        binding.root.setOnClickListener {
            return@setOnClickListener
        }

        binding.tvLoginInfoWithdraw.setOnClickListener {
            WithdrawDialog.newInstance("tag")
                .show(parentFragmentManager, "tag")
        }

        binding.ivSettingBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}