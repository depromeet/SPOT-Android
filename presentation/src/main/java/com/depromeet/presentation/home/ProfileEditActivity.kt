package com.depromeet.presentation.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityProfileEditBinding
import com.depromeet.presentation.extension.NickNameError
import com.depromeet.presentation.home.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.home.adapter.ProfileEditTeamAdapter
import com.depromeet.presentation.home.mockdata.TeamData
import com.depromeet.presentation.home.viewmodel.EditUiState
import com.depromeet.presentation.home.viewmodel.ProfileEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditActivity : BaseActivity<ActivityProfileEditBinding>(
    ActivityProfileEditBinding::inflate
) {
    companion object {
        private const val GRID_SPAN_COUNT = 2
        private const val GRID_SPACING = 40
    }

    private lateinit var adapter: ProfileEditTeamAdapter
    private val viewModel: ProfileEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCheerTeamList()
        navigateToPhotoPicker()
        onClickTeam()
        observeNickName()
        viewModel.getInformation()

        viewModel.uiState.asLiveData().observe(this) { state ->
            updateUI(state)
        }

        binding.ibProfileEditClose.setOnClickListener { finish() }
        binding.tvProfileEditComplete.setOnClickListener { finish() }

    }

    private fun updateUI(state: EditUiState) {
        binding.ivProfileEditImage.load(state.image) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_default_profile)
            error(R.drawable.ic_default_profile)
        }
        binding.etProfileEditNickname.setText(state.nickName)
        adapter.submitList(state.teamList)
    }

    private fun setCheerTeamList() {
        adapter = ProfileEditTeamAdapter()
        binding.rvProfileEditTeam.adapter = adapter
        binding.rvProfileEditTeam.addItemDecoration(
            GridSpacingItemDecoration(
                GRID_SPAN_COUNT,
                GRID_SPACING
            )
        )
    }

    private fun observeNickName() {
        binding.etProfileEditNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(nickName: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateNickName(nickName.toString())
            }
        })

        viewModel.nickNameError.asLiveData().observe(this) { error ->
            when (error) {
                is NickNameError.NoError -> {
                    updateCompletionStatus(
                        isError = false,
                        getString(R.string.profile_edit_error_no)
                    )
                }

                is NickNameError.LengthError -> {
                    updateCompletionStatus(
                        isError = true,
                        getString(R.string.profile_edit_error_length)
                    )
                }

                is NickNameError.InvalidCharacterError -> {
                    updateCompletionStatus(
                        isError = true,
                        getString(R.string.profile_edit_error_type)
                    )
                }

                is NickNameError.DuplicateError -> {
                    updateCompletionStatus(
                        isError = true,
                        getString(R.string.profile_edit_error_duplicate)
                    )
                }
            }
        }
    }

    private fun updateCompletionStatus(isError: Boolean, error: String) = if (isError) {
        with(binding) {
            etProfileEditNickname.setBackgroundResource(R.drawable.rect_warning01red_line_6)
            tvProfileEditNicknameError.visibility = View.VISIBLE
            tvProfileEditNicknameError.text = error
            tvProfileEditComplete.isEnabled = false
        }

    } else {
        with(binding) {
            etProfileEditNickname.setBackgroundResource(R.drawable.rect_gray100_line_6)
            tvProfileEditNicknameError.visibility = View.GONE
            tvProfileEditNicknameError.text = error
            tvProfileEditComplete.isEnabled = true
        }
    }

    private fun onClickTeam() {
        adapter.itemClubClickListener = object : ProfileEditTeamAdapter.OnItemClubClickListener {
            override fun onItemClubClick(item: TeamData) {
                viewModel.updateClickTeam(item)
                binding.tvProfileEditNoTeam.setBackgroundResource(R.drawable.rect_gray100_line_10)
            }
        }
        binding.tvProfileEditNoTeam.setOnClickListener {
            viewModel.deleteCheerTeam()
            binding.tvProfileEditNoTeam.setBackgroundResource(R.drawable.rect_gray50_fill_gray900_line_10)
        }
    }

    private fun navigateToPhotoPicker() {
        binding.ibProfileEditCamera.setOnClickListener { imageUpload() }
        binding.ivProfileEditImage.setOnClickListener { imageUpload() }
    }

    private fun imageUpload() {
        val fragment = ProfileImageUploadDialog()
        fragment.show(supportFragmentManager, fragment.tag)
    }

}