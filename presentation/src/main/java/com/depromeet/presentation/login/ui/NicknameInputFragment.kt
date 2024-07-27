package com.depromeet.presentation.login.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentNicknameInputBinding
import com.depromeet.presentation.login.viewmodel.NicknameInputState
import com.depromeet.presentation.login.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        tvNicknameNextBtn.setOnClickListener {
            //Todo : 서버 API 연동 및 닉네임 중복 검사
            //임시로 응원하는 팀 선택 화면으로 이동
            parentFragmentManager.commit {
                replace(R.id.fl_signup_container, TeamSelectFragment())
                addToBackStack(null)
            }
        }
    }

    private fun initObserver() = with(binding) {
        signUpViewModel.nicknameInputState.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                NicknameInputState.EMPTY -> {
                    clNicknameInputWarning.visibility = View.GONE
                    updateButtonEnabled(false)
                }
                NicknameInputState.VALID -> {
                    clNicknameInputWarning.visibility = View.GONE
                    updateButtonEnabled(true)
                }
                NicknameInputState.INVALID_LENGTH -> {
                    clNicknameInputWarning.visibility = View.VISIBLE
                    tvNicknameWarning.text = getString(R.string.profile_edit_error_length)
                    updateButtonEnabled(false)
                }
                NicknameInputState.INVALID_CHARACTER -> {
                    clNicknameInputWarning.visibility = View.VISIBLE
                    tvNicknameWarning.text = getString(R.string.profile_edit_error_type)
                    updateButtonEnabled(false)
                }
                NicknameInputState.DUPLICATE -> {
                    clNicknameInputWarning.visibility = View.VISIBLE
                    tvNicknameWarning.text = getString(R.string.profile_edit_error_duplicate)
                    updateButtonEnabled(false)
                }
            }
        }
    }

    private fun updateButtonEnabled(isEnabled: Boolean) = with(binding) {
        tvNicknameNextBtn.isClickable = isEnabled
        if (isEnabled) {
            tvNicknameNextBtn.setBackgroundResource(R.drawable.rect_gray800_fill_6)
        } else {
            tvNicknameNextBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
        }
    }
}