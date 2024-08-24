package com.dpm.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentNicknameInputBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.login.viewmodel.NicknameInputState
import com.dpm.presentation.login.viewmodel.NicknameInputViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NicknameInputActivity: BaseActivity<FragmentNicknameInputBinding>({
        FragmentNicknameInputBinding.inflate(it)
}) {
    private val signUpViewModel by viewModels<NicknameInputViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initClickListener()
        initObserver()
    }

    private fun initView() {
        binding.etProfileEditNickname.backgroundTintList = getColorStateList(
            this@NicknameInputActivity,
            com.depromeet.designsystem.R.color.color_gray_200
        )
    }

    private fun initClickListener() = with(binding) {
        ivBack.setOnClickListener {
             finish()
        }

        etProfileEditNickname.addTextChangedListener { text ->
            if (text.isNullOrEmpty()) {
                ivNicknameClear.visibility = View.GONE
                etProfileEditNickname.backgroundTintList = getColorStateList(
                    this@NicknameInputActivity,
                    com.depromeet.designsystem.R.color.color_gray_200
                )
            } else {
                ivNicknameClear.visibility = View.VISIBLE
                etProfileEditNickname.backgroundTintList = getColorStateList(
                    this@NicknameInputActivity,
                    com.depromeet.designsystem.R.color.color_green_600
                )
            }
            signUpViewModel.validateNickname(text.toString())
        }

        etProfileEditNickname.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etProfileEditNickname.text.isNotEmpty()) {
                    signUpViewModel.checkDuplicateNickname(etProfileEditNickname.text.toString())
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
            signUpViewModel.checkDuplicateNickname(etProfileEditNickname.text.toString())
        }
    }

    private fun initObserver() = with(binding) {
        signUpViewModel.nicknameInputState.observe(this@NicknameInputActivity) { state ->
            when (state) {
                NicknameInputState.EMPTY -> {
                    clNicknameInputWarning.visibility = View.GONE
                    updateButtonEnabled(false)
                }
                NicknameInputState.VALID -> {
                    clNicknameInputWarning.visibility = View.GONE
                    updateButtonEnabled(true)
                }
                NicknameInputState.NICKNAME_SUCCESS -> {
                    Intent(this@NicknameInputActivity, TeamSelectActivity::class.java).apply {
                        putExtra("nickname", etProfileEditNickname.text.toString())
                        putExtra("kakaoToken", intent.getStringExtra("kakaoToken"))
                        startActivity(this)
                    }
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
            tvNicknameNextBtn.setBackgroundResource(R.drawable.rect_main_fill_6)
        } else {
            tvNicknameNextBtn.setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
        }
    }
}