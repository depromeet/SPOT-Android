package com.depromeet.presentation.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentNicknameInputBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData

@AndroidEntryPoint
class NicknameInputFragment: BindingFragment<FragmentNicknameInputBinding>(
    R.layout.fragment_nickname_input, { inflater, container, attachToRoot ->
        FragmentNicknameInputBinding.inflate(inflater, container, attachToRoot)
    }
) {
    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
        initObserver()
    }

    private fun initClickListener() = with(binding) {
        ivBack.setOnClickListener {
             parentFragmentManager.popBackStack()
        }

        etProfileEditNickname.addTextChangedListener { text ->
            if (text.isNullOrEmpty()) {
                ivNicknameClear.visibility = View.GONE
            } else {
                ivNicknameClear.visibility = View.VISIBLE
            }
            signUpViewModel.validateNickname(text.toString())
        }

        etProfileEditNickname.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etProfileEditNickname.text.isNotEmpty()) {
                    //Todo : 서버 API 연동 및 닉네임 중복 검사
                }
                true
            } else {
                false
            }
        }

        ivNicknameClear.setOnClickListener {
            etProfileEditNickname.text.clear()
        }

        tvNicknameNextBtn.setOnClickListener {
            //Todo : 서버 API 연동 및 닉네임 중복 검사
            //임시로 응원하는 팀 선택 화면으로 이동
        }
    }

    private fun initObserver() {
        signUpViewModel.nicknameInputState.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                NicknameInputState.EMPTY -> {
                    binding.clNicknameInputWarning.visibility = View.GONE
                }
                NicknameInputState.VALID -> {
                    binding.clNicknameInputWarning.visibility = View.GONE
                }
                NicknameInputState.INVALID_LENGTH -> {
                    binding.clNicknameInputWarning.visibility = View.VISIBLE
                    binding.tvNicknameWarning.text = getString(R.string.profile_edit_error_length)
                }
                NicknameInputState.INVALID_CHARACTER -> {
                    binding.clNicknameInputWarning.visibility = View.VISIBLE
                    binding.tvNicknameWarning.text = getString(R.string.profile_edit_error_type)
                }
            }
        }
    }
}