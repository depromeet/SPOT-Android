package com.depromeet.presentation.home

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityProfileEditBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.adapter.BaseballTeamAdapter
import com.depromeet.presentation.home.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.home.viewmodel.ProfileEditViewModel
import com.depromeet.presentation.home.viewmodel.ProfileEvents
import com.depromeet.presentation.login.viewmodel.NicknameInputState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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
            Timber.d("test = $name  /// $image /// $cheerTeam")
            binding.etProfileEditNickname.setText(name)
        }
        setCheerTeamList()
    }

    private fun initEvent() {
        binding.ibProfileEditClose.setOnClickListener { finish() }
        binding.tvProfileEditComplete.setOnClickListener {
            viewModel.uploadProfileEdit()
        }
        navigateToPhotoPicker()
        onClickTeam()
    }

    private fun initObserver() {
        observeEvent()
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

    private fun observeEvent() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is ProfileEvents.ShowSnackMessage -> {
                            toast(event.message)
                        }
                    }
                }
            }

        }
    }

    private fun observeNickName() {
        binding.etProfileEditNickname.addTextChangedListener { text: Editable? ->
            viewModel.updateNickName(text.toString())
        }

        viewModel.nickNameError.asLiveData().observe(this) { state ->
            when (state) {
                NicknameInputState.EMPTY -> {}

                NicknameInputState.VALID -> {
                    updateCompletionStatus(
                        isError = false,
                        getString(R.string.profile_edit_error_no)
                    )
                }

                NicknameInputState.INVALID_LENGTH -> {
                    updateCompletionStatus(
                        isError = true,
                        getString(R.string.profile_edit_error_length)
                    )
                }

                NicknameInputState.INVALID_CHARACTER -> {
                    updateCompletionStatus(
                        isError = true,
                        getString(R.string.profile_edit_error_type)
                    )
                }

                NicknameInputState.DUPLICATE -> {
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
            with(binding.ivProfileEditImage) {
                if (state.isEmpty()) {
                    setImageResource(R.drawable.ic_default_profile)
                } else {
                    load(state) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

        viewModel.presignedUrl.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Success -> {}
                is UiState.Failure -> {
                    toast("이미지 업로드를 실패하였습니다. 다시 선택해주세요.")
                }

                is UiState.Empty -> {
                    toast("실패")
                }
            }
        }
    }

    private fun observeChange() {
        viewModel.changeEnabled.asLiveData().observe(this) { state ->
            binding.tvProfileEditComplete.isEnabled = state

        }

        viewModel.profileEdit.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    finish()
                }

                is UiState.Failure -> {
                    toast("프로필 변경에 실패\n다시 시도바람")
                }

                is UiState.Empty -> {
                    toast("프로필 변경 실패\n다시 시도바람(빈값")
                }

                is UiState.Loading -> {}
            }

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