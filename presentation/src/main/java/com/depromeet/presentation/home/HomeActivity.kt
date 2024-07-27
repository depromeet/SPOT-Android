package com.depromeet.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.ProfileResponse
import com.depromeet.domain.entity.response.home.RecentReviewResponse
import com.depromeet.presentation.databinding.ActivityHomeBinding
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.viewmodel.HomeViewModel
import com.depromeet.presentation.seatReview.ReviewActivity
import com.depromeet.presentation.seatrecord.SeatRecordActivity
import com.depromeet.presentation.util.CalendarUtil
import com.depromeet.presentation.util.applyBoldAndSizeSpan
import com.depromeet.presentation.viewfinder.StadiumActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
) {
    companion object {
        const val PROFILE_NAME = "profile_name"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_CHEER_TEAM = "profile_cheer_team"
    }

    private val viewModel: HomeViewModel by viewModels()
    private val sightList: List<View> by lazy {
        listOf(
            binding.clHomeNoRecord,
            binding.clHomeOneRecord,
            binding.clHomeTwoRecord,
            binding.clHomeThreeRecord
        )
    }

    private val editProfileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val nickname = data?.getStringExtra(ProfileEditActivity.PROFILE_NAME) ?: ""
                val profileImage = data?.getStringExtra(ProfileEditActivity.PROFILE_IMAGE) ?: ""
                val teamId = data?.getIntExtra(ProfileEditActivity.PROFILE_CHEER_TEAM, 0) ?: 0
                val teamIdUrl =
                    data?.getStringExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_URL) ?: ""

                viewModel.updateTest(nickname, profileImage, teamId, teamIdUrl)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInformation()
        initView()
        initEvent()
        initObserver()

    }

    private fun initView() {
        binding.clMainFindSight.clipToOutline = true
    }


    private fun initEvent() = with(binding) {

        /** 프로필 수정 */
        ivHomeProfile.setOnClickListener { navigateToProfileEditActivity() }
        ibHomeEdit.setOnClickListener { navigateToProfileEditActivity() }
        /** 내 시야 기록 */
        clHomeSightRecord.setOnClickListener { navigateToSeatRecordActivity() }
        tvHomeMore.setOnClickListener { navigateToSeatRecordActivity() }
        clHomeOneRecord.setOnClickListener { navigateToSeatRecordActivity() }
        clHomeTwoRecord.setOnClickListener { navigateToSeatRecordActivity() }
        clHomeThreeRecord.setOnClickListener { navigateToSeatRecordActivity() }
        /** 시야후기 등록*/
        clHomeRegisterSight.setOnClickListener { navigateToReviewActivity() }
        clHomeNoRecord.setOnClickListener { navigateToReviewActivity() }
        /** 시야 찾기 */
        clMainFindSight.setOnClickListener { navigateToStadiumActivity() }

        ibHomeSetting.setOnClickListener { /** 셋팅 이동 **/ }

    }

    private fun initObserver() {
        viewModel.profile.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    updateProfile(state.data)
                }

                is UiState.Failure -> {
                    toast("실패")
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }
        }
        viewModel.recentReview.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    updateRecentReview(state.data)
                }

                is UiState.Failure -> {
                    toast("실패")
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }
        }
    }

    private fun updateRecentReview(data: RecentReviewResponse) = with(binding) {
        setSpannableString(viewModel.nickname.value, viewModel.reviewCount.value)
        tvHomeRecentRecordName.text = data.review.stadiumName
        tvHomeRecentRecordDate.text = CalendarUtil.getFormattedDate(data.review.baseReview.dateTime)

        sightList.forEachIndexed { index, view ->
            view.visibility =
                if (index == data.review.baseReview.images.size) VISIBLE else GONE
        }
        when (data.review.baseReview.images.size) {
            0 -> {
                tvHomeRecentRecordDate.visibility = GONE
                tvHomeRecentRecordName.visibility = GONE
            }

            1 -> {
                ivHomeRecentOneRecord1.loadAndClip(data.review.baseReview.images[0])
                "${data.review.sectionName} ${data.review.blockCode}블록".also {
                    tvHomeRecentOneSection.text = it
                }
            }

            2 -> {
                ivHomeRecentTwoRecord1.loadAndClip(data.review.baseReview.images[0])
                ivHomeRecentTwoRecord2.loadAndClip(data.review.baseReview.images[1])
                "${data.review.sectionName} ${data.review.blockCode}블록".also {
                    tvHomeRecentOneSection.text = it
                }
            }

            3 -> {
                ivHomeRecentThreeRecord1.loadAndClip(data.review.baseReview.images[0])
                ivHomeRecentThreeRecord2.loadAndClip(data.review.baseReview.images[1])
                ivHomeRecentThreeRecord3.loadAndClip(data.review.baseReview.images[2])
                "${data.review.sectionName} ${data.review.blockCode}블록".also {
                    tvHomeRecentOneSection.text = it
                }
            }

            else -> {}
        }
    }

    private fun updateProfile(profile: ProfileResponse) = with(binding) {
        setSpannableString(viewModel.nickname.value, viewModel.reviewCount.value)
        if (profile.profileImage.isEmpty()) {
            ivHomeProfile.setImageResource(com.depromeet.presentation.R.drawable.ic_default_profile)
        } else {
            ivHomeProfile.load(profile.profileImage) {
                transformations(CircleCropTransformation())
            }
        }
        "Lv.${profile.level} ${profile.levelTitle}".also { tvHomeLevel.text = it }
        ivHomeCheerTeam.load(profile.teamImage)

    }

    private fun setSpannableString(
        nickName: String,
        writeCount: Int,
    ) {
        val text = "${nickName}님, 지금까지\n시야후기를\n${writeCount}번 작성했어요!"
        val spannableBuilder = SpannableStringBuilder(text)

        val startIndexNickname = 0
        val endIndexNickname = nickName.length
        applyBoldAndSizeSpan(spannableBuilder, startIndexNickname, endIndexNickname)

        val startIndexWriteCount = text.indexOf(writeCount.toString())
        val endIndexWriteCount = startIndexWriteCount + writeCount.toString().length
        applyBoldAndSizeSpan(spannableBuilder, startIndexWriteCount, endIndexWriteCount)

        binding.tvHomeSightChance.text = spannableBuilder
    }


    private fun navigateToProfileEditActivity() {
        val currentState = viewModel.profile.value
        if (currentState is UiState.Success) {
            editProfileLauncher.launch(Intent(this, ProfileEditActivity::class.java).apply {
                with(currentState.data) {
                    putExtra(PROFILE_NAME, this.nickname)
                    putExtra(PROFILE_IMAGE, this.profileImage)
                    putExtra(PROFILE_CHEER_TEAM, this.teamId)
                }
            })
        }

    }

    private fun navigateToSeatRecordActivity() {
        Intent(this, SeatRecordActivity::class.java).apply { startActivity(this) }
    }

    private fun navigateToReviewActivity() {
        Intent(this, ReviewActivity::class.java).apply { startActivity(this) }
    }

    private fun navigateToStadiumActivity() {
        Intent(this, StadiumActivity::class.java).apply { startActivity(this) }
    }
}