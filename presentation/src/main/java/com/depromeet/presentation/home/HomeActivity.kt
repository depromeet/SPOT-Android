package com.depromeet.presentation.home

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.ProfileResponse
import com.depromeet.domain.entity.response.home.RecentReviewResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityHomeBinding
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.viewmodel.HomeViewModel
import com.depromeet.presentation.seatrecord.SeatRecordActivity
import com.depromeet.presentation.util.applyBoldAndSizeSpan
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInformation()
        initView()
        initEvent()
        initObserver()

    }

    private fun initView(){
        binding.clMainSight.clipToOutline = true
    }

    private fun initEvent(){
        binding.ivHomeProfile.setOnClickListener { navigateToProfileEditActivity() }
        binding.ibHomeEdit.setOnClickListener { navigateToProfileEditActivity() }
        binding.clHomeSightRecord.setOnClickListener { navigateToSeatRecordActivity() }
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

    private fun updateRecentReview(review: RecentReviewResponse) = with(binding) {
        setSpannableString(viewModel.nickname.value, viewModel.reviewCount.value)
        tvHomeRecentRecordName.text = review.stadiumName
        tvHomeRecentRecordDate.text = review.date //TODO : 여기 바꿔야함 CALENDER UTIL로

        sightList.forEachIndexed { index, view ->
            view.visibility =
                if (index == review.reviewImages.size) View.VISIBLE else View.GONE
        }
        when (review.reviewImages.size) {
            0 -> {
                tvHomeRecentRecordDate.visibility = View.GONE
                tvHomeRecentRecordName.visibility = View.GONE
            }

            1 -> {
                ivHomeRecentOneRecord1.loadAndClip(review.reviewImages[0])
                "${review.blockName}${review.seatNumber}".also { tvHomeRecentOneSection.text = it }
            }

            2 -> {
                ivHomeRecentTwoRecord1.loadAndClip(review.reviewImages[0])
                ivHomeRecentTwoRecord2.loadAndClip(review.reviewImages[1])
                "${review.blockName}${review.seatNumber}".also { tvHomeRecentOneSection.text = it }
            }

            3 -> {
                ivHomeRecentThreeRecord1.loadAndClip(review.reviewImages[0])
                ivHomeRecentThreeRecord2.loadAndClip(review.reviewImages[1])
                ivHomeRecentThreeRecord3.loadAndClip(review.reviewImages[2])
                "${review.blockName}${review.seatNumber}".also { tvHomeRecentOneSection.text = it }
            }

            else -> {}
        }
    }

    private fun updateProfile(profile: ProfileResponse) = with(binding) {
        ivHomeProfile.load(profile.profileImage) { transformations(CircleCropTransformation()) }
        "Lv.${profile.level} ${profile.level.levelToTitle()}".also { tvHomeLevel.text = it }
        ivHomeCheerTeam.load(profile.teamImage)

    }

    /** 임시 처리 서버에서 내려주면 다시 바꿔주고 아니면 그대로 유지*/
    private fun Int.levelToTitle(): String {
        return when (this) {
            1 -> getString(R.string.one_level_to_title)
            2 -> getString(R.string.two_level_to_title)
            3 -> getString(R.string.three_level_to_title)
            4 -> getString(R.string.four_level_to_title)
            5 -> getString(R.string.five_level_to_title)
            6 -> getString(R.string.six_level_to_title)
            else -> getString(R.string.unknown_level_to_title)
        }
    }

    private fun navigateToProfileEditActivity() {
        val currentState = viewModel.profile.value

        if(currentState is UiState.Success){
            Intent(this, ProfileEditActivity::class.java).apply {
            with(currentState.data) {
                putExtra(PROFILE_NAME, this.nickname)
                putExtra(PROFILE_IMAGE, this.profileImage)
                putExtra(PROFILE_CHEER_TEAM, this.teamId)
            }
            }.let(::startActivity)
        }

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

    private fun navigateToSeatRecordActivity() {
        Intent(this, SeatRecordActivity::class.java).apply { startActivity(this) }
    }

}