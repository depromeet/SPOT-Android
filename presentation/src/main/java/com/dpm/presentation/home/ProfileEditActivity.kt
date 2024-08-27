package com.dpm.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityProfileEditBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.home.ResponseBaseballTeam
import com.dpm.presentation.extension.dpToPx
import com.dpm.presentation.extension.loadAndCircleProfile
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.adapter.BaseballTeamAdapter
import com.dpm.presentation.home.adapter.GridSpacingItemDecoration
import com.dpm.presentation.home.dialog.ProfileImageUploadDialog
import com.dpm.presentation.home.viewmodel.ProfileEditViewModel
import com.dpm.presentation.home.viewmodel.ProfileEvents
import com.dpm.presentation.login.viewmodel.NicknameInputState
import com.dpm.presentation.seatrecord.SeatRecordActivity
import com.dpm.presentation.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileEditActivity : BaseActivity<ActivityProfileEditBinding>(
    ActivityProfileEditBinding::inflate
) {
    companion object {
        private const val GRID_SPAN_COUNT = 2
        private const val GRID_SPACING = 10
        const val PROFILE_NAME = "profile_name"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_CHEER_TEAM_ID = "profile_cheer_team_id"
        const val PROFILE_CHEER_TEAM_NAME = "profile_cheer_team_name"
    }

    private lateinit var adapter: BaseballTeamAdapter
    private val viewModel: ProfileEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        initViewStatusBar()
        getDataExtra { name, image, cheerTeam ->
            viewModel.initProfile(name, image, cheerTeam)
            binding.etProfileEditNickname.setText(name)
        }
        viewModel.getBaseballTeam()
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

    private fun saveProfile() {
        val resultIntent = Intent().apply {
            putExtra(PROFILE_NAME, viewModel.nickname.value)
            putExtra(PROFILE_IMAGE, viewModel.getPresignedUrlOrProfileImage())
            putExtra(PROFILE_CHEER_TEAM_ID, viewModel.cheerTeam.value)
            putExtra(PROFILE_CHEER_TEAM_NAME, viewModel.getTeamName())
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun setCheerTeamList() {
        adapter = BaseballTeamAdapter()
        binding.rvProfileEditTeam.adapter = adapter
        binding.rvProfileEditTeam.addItemDecoration(
            GridSpacingItemDecoration(
                GRID_SPAN_COUNT,
                GRID_SPACING.dpToPx(this)
            )
        )
    }

    private fun observeEvent() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is ProfileEvents.ShowSnackMessage -> {
                            //
                        }
                    }
                }
            }

        }
    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_white)
            setBlackSystemBarIconColor(window)
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

                else -> {}
            }
        }
    }

    private fun observeProfileImage() {
        viewModel.profileImage.asLiveData().observe(this) { state ->
            binding.ivProfileEditImage.loadAndCircleProfile(state)
        }

        viewModel.presignedUrl.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Success -> {}
                is UiState.Failure -> {
                    makeSpotImageAppbar("이미지 업로드를 실패하였습니다. 다시 선택해주세요.")
                }

                is UiState.Empty -> {
                    makeSpotImageAppbar("이미지 업로드를 실패하였습니다. 다시 선택해주세요.")
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
                    saveProfile()
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("프로필 변경을 실패하였습니다. 다시 시도 바랍니다")
                }

                is UiState.Empty -> {
                    makeSpotImageAppbar("프로필 변경을 실패하였습니다. 다시 시도 바랍니다")
                }

                is UiState.Loading -> {}
            }

        }
    }

    private fun observeTeam() {
        viewModel.team.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    if (!state.data.any { it.isClicked }) {
                        binding.clProfileEditAllTeam.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_positive_fill_positive_secondary_stroke_8)
                    }
                    adapter.submitList(state.data)
                }

                is UiState.Loading -> {

                }

                is UiState.Empty -> {
                    makeSpotImageAppbar("팀불러오기를 실패하였습니다.")
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("팀불러오기를 실패하였습니다.")
                }
            }
        }
    }

    private fun updateCompletionStatus(isError: Boolean, error: String) = if (isError) {
        with(binding) {
            etProfileEditNickname.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_error_primary_stroke_6)
            tvProfileEditNicknameError.visibility = VISIBLE
            tvProfileEditNicknameError.text = error
        }

    } else {
        with(binding) {
            etProfileEditNickname.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_6)
            tvProfileEditNicknameError.visibility = GONE
            tvProfileEditNicknameError.text = error
        }
    }

    private fun onClickTeam() {
        adapter.itemClubClickListener = object : BaseballTeamAdapter.OnItemClubClickListener {
            override fun onItemClubClick(item: ResponseBaseballTeam) {
                viewModel.setClickedBaseballTeam(item.id)
                binding.clProfileEditAllTeam.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_tertiary_fill_8)
            }
        }
        binding.clProfileEditAllTeam.setOnClickListener {
            viewModel.deleteCheerTeam()
            binding.clProfileEditAllTeam.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_positive_fill_positive_secondary_stroke_8)
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
            intent?.getStringExtra(SeatRecordActivity.PROFILE_NAME) ?: "",
            intent?.getStringExtra(SeatRecordActivity.PROFILE_IMAGE) ?: "",
            intent?.getIntExtra(SeatRecordActivity.PROFILE_CHEER_TEAM, 0) ?: 0
        )
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 20
        ).show()
    }

}