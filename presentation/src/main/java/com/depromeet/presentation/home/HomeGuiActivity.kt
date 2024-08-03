package com.depromeet.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.designsystem.SpotImageSnackBar
import com.depromeet.domain.entity.response.home.HomeFeedResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.presentation.databinding.ActivityHomeGuiBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.adapter.StadiumAdapter
import com.depromeet.presentation.home.viewmodel.HomeGuiViewModel
import com.depromeet.presentation.seatReview.ReviewActivity
import com.depromeet.presentation.seatrecord.SeatRecordActivity
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.viewfinder.StadiumActivity
import com.depromeet.presentation.viewfinder.StadiumSelectionActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeGuiActivity : BaseActivity<ActivityHomeGuiBinding>(
    ActivityHomeGuiBinding::inflate
) {
    companion object {
        const val STADIUM_EXTRA_ID = "stadium_id"
        private const val START_SPACING_DP = 16
        private const val BETWEEN_SPADING_DP = 8
    }

    private val homeViewModel: HomeGuiViewModel by viewModels()

    private lateinit var stadiumAdapter: StadiumAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        homeViewModel.getStadiums()
        homeViewModel.getHomeFeed()
        setStadiumAdapter()
    }

    private fun initEvent() = with(binding) {
        clHomeScrap.setOnClickListener { toast("아직 열리지 않음") }
        clHomeArchiving.setOnClickListener { startSeatRecordActivity() }
        ivHomeInfo.setOnClickListener { showLevelDescriptionDialog() }
        clHomeScrap.setOnClickListener {
            SpotImageSnackBar.make(
                view = binding.root,
                message = "스크랩이 잠겨있어요\uD83E\uDEE2 곧 업데이트 예정이에요",
                messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
                icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
                iconColor = com.depromeet.designsystem.R.color.color_error_secondary
            ).show()
        }
        clHomeUpload.setOnClickListener { navigateToReviewActivity() }
    }

    private fun initObserver() {
        homeViewModel.stadiums.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    toast(state.msg)
                    setStadiumShimmer(true)
                }

                is UiState.Loading -> {
                    setStadiumShimmer(true)
                }

                is UiState.Success -> {
                    stadiumAdapter.submitList(state.data)
                    binding.rvHomeStadium.scrollToPosition(0)
                    setStadiumShimmer(false)
                }
            }
        }

        homeViewModel.homeFeed.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    toast("내 정보 불러오기 실패")
                }

                is UiState.Loading -> {
                    setHomeFeedShimmer(true)
                }

                is UiState.Success -> {
                    setHomeFeed(state.data)
                    setHomeFeedShimmer(false)
                }
            }

        }
    }

    private fun setStadiumAdapter() {
        stadiumAdapter = StadiumAdapter(
            searchClick = {
                startStadiumSelectionActivity()
            },
            stadiumClick = {
                if (!it.isActive) {
                    toast("현재 잠실야구장만 이용할 수 있어요!")
                } else {
                    startStadiumActivity(it)
                }
            }
        )
        binding.rvHomeStadium.adapter = stadiumAdapter
        binding.rvHomeStadium.addItemDecoration(
            LinearSpacingItemDecoration(
                START_SPACING_DP.dpToPx(this),
                BETWEEN_SPADING_DP.dpToPx(this)
            )
        )

    }


    private fun startStadiumSelectionActivity() {
        Intent(this@HomeGuiActivity, StadiumSelectionActivity::class.java).apply {
            startActivity(
                this
            )
        }
    }

    private fun startSeatRecordActivity() {
        Intent(this, SeatRecordActivity::class.java).apply { startActivity(this) }
    }

    private fun startStadiumActivity(stadium: StadiumsResponse) {
        val intent = Intent(
            this@HomeGuiActivity,
            StadiumActivity::class.java
        ).apply {
            if (stadium.id != 1) {
                putExtra(STADIUM_EXTRA_ID, 1)
            } else {
                putExtra(STADIUM_EXTRA_ID, stadium.id)
            }
        }
        startActivity(intent)
    }

    private fun showLevelDescriptionDialog() {
        LevelDescriptionDialog().apply { show(supportFragmentManager, this.tag) }
    }

    private fun setStadiumShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            shimmerHomeStadium.startShimmer()
            shimmerHomeStadium.visibility = View.VISIBLE
            rvHomeStadium.visibility = View.INVISIBLE
        } else {
            shimmerHomeStadium.stopShimmer()
            shimmerHomeStadium.visibility = View.GONE
            rvHomeStadium.visibility = View.VISIBLE
        }
    }

    private fun setHomeFeed(data: HomeFeedResponse) = with(binding) {
        Timber.d("test ${data}")
        "Lv.${data.level}".also { tvHomeLevel.text = it }
        tvHomeTeam.text = if (data.teamId == null) {
            "모두를 응원하는"
        } else {
            "${data.teamName}의"
        }
        tvHomeTitle.text = data.levelTitle
        ivHomeCharacter.load(data.mascotImageUrl)
        csbvHomeTitle.setText("시야 사진 ${data.reviewCntToLevelup}장 더 올리면 레벨업!")
    }

    private fun navigateToReviewActivity() {
        Intent(this, ReviewActivity::class.java).apply { startActivity(this) }
    }

    private fun setHomeFeedShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            shimmerHomeProfile.startShimmer()
            shimmerHomeProfile.visibility = View.VISIBLE
        } else {
            shimmerHomeProfile.stopShimmer()
            shimmerHomeProfile.visibility = View.GONE
        }
    }
}