package com.depromeet.presentation.home

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityProfileEditBinding
import com.depromeet.presentation.extension.NickNameError
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.adapter.BaseballTeamAdapter
import com.depromeet.presentation.home.adapter.GridSpacingItemDecoration
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

    private lateinit var adapter: BaseballTeamAdapter
    private val viewModel: ProfileEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCheerTeamList()
        navigateToPhotoPicker()
        onClickTeam()
        observeNickName()
        viewModel.getBaseballTeam()

        viewModel.team.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data)
                }

                is UiState.Loading -> {
                    toast("로딩 중")
                }

                is UiState.Empty -> {
                    toast("빈값 에러")
                }

                is UiState.Failure -> {
                    toast("통신 실패")
                }
            }
        }

        binding.ibProfileEditClose.setOnClickListener { finish() }
        binding.tvProfileEditComplete.setOnClickListener { finish() }

    }

    private fun setCheerTeamList() {
        adapter = BaseballTeamAdapter()
        binding.rvProfileEditTeam.adapter = adapter
        binding.rvProfileEditTeam.addItemDecoration(
            GridSpacingItemDecoration(
                GRID_SPAN_COUNT,
                GRID_SPACING
            )
        )
    }

    private fun observeNickName() {
        binding.etProfileEditNickname.addTextChangedListener { text: Editable? ->
            viewModel.updateNickName(text.toString())
        }

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
        adapter.itemClubClickListener = object : BaseballTeamAdapter.OnItemClubClickListener {
            override fun onItemClubClick(item: BaseballTeamResponse) {
                viewModel.setClickedBaseballTeam(item.id)
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