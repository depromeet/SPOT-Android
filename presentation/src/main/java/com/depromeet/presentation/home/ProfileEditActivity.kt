package com.depromeet.presentation.home

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
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

        viewModel.getBaseballTeam()
        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        getDataExtra { name, image, cheerTeam ->
            viewModel.initProfile(name, image, cheerTeam)
            binding.etProfileEditNickname.setText(name)
        }
        setCheerTeamList()
    }

    private fun initEvent() {
        binding.ibProfileEditClose.setOnClickListener { finish() }
        binding.tvProfileEditComplete.setOnClickListener { finish() }
        navigateToPhotoPicker()
        onClickTeam()
    }

    private fun initObserver() {
        observeNickName()
        observeTeam()
        observeProfileImage()
        observeChange()
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

    private fun observeProfileImage() {
        viewModel.profileImage.asLiveData().observe(this) { state ->
            binding.ivProfileEditImage.load(state) {
                transformations(CircleCropTransformation())
            }
        }
        viewModel.presignedUrl.asLiveData().observe(this) {state ->
            when(state){
                is UiState.Success -> {}
                is UiState.Failure -> {toast("이미지 업로드를 실패하였습니다. 다시 선택해주세요.")}
                is UiState.Empty -> {toast("실패")}
                is UiState.Loading -> {}
            }
        }
    }

    private fun observeChange() {
        viewModel.changeEnabled.asLiveData().observe(this) { state ->
            binding.tvProfileEditComplete.isEnabled = state

        }
    }

    private fun observeTeam() {
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
    }

    private fun updateCompletionStatus(isError: Boolean, error: String) = if (isError) {
        with(binding) {
            etProfileEditNickname.setBackgroundResource(R.drawable.rect_warning01red_line_6)
            tvProfileEditNicknameError.visibility = View.VISIBLE
            tvProfileEditNicknameError.text = error
        }

    } else {
        with(binding) {
            etProfileEditNickname.setBackgroundResource(R.drawable.rect_gray100_line_6)
            tvProfileEditNicknameError.visibility = View.GONE
            tvProfileEditNicknameError.text = error
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

    private fun getDataExtra(callback: (name: String, profileImage: String, cheerTeam: Int) -> Unit) {
        callback(
            intent?.getStringExtra(HomeActivity.PROFILE_NAME) ?: "",
            intent?.getStringExtra(HomeActivity.PROFILE_IMAGE) ?: "",
            intent?.getIntExtra(HomeActivity.PROFILE_CHEER_TEAM, 0) ?: 0
        )
    }

}