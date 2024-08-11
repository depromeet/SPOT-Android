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
import com.depromeet.domain.entity.response.viewfinder.ResponseStadiums
import com.depromeet.presentation.databinding.ActivityHomeBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.home.adapter.StadiumAdapter
import com.depromeet.presentation.home.viewmodel.HomeGuiViewModel
import com.depromeet.presentation.seatReview.ReviewActivity
import com.depromeet.presentation.seatrecord.SeatRecordActivity
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.setting.SettingActivity
import com.depromeet.presentation.viewfinder.StadiumActivity
import com.depromeet.presentation.viewfinder.StadiumSelectionActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
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

    override fun onResume() {
        super.onResume()
        homeViewModel.getHomeFeed()
    }

    private fun initView() {
        homeViewModel.getStadiums()
        setStadiumAdapter()
    }

    private fun initEvent() = with(binding) {
        clHomeArchiving.setOnClickListener { startSeatRecordActivity() }
        ivHomeInfo.setOnClickListener { showLevelDescriptionDialog() }
        clHomeScrap.setOnClickListener {
            makeSpotImageAppbar("스크랩이 잠겨있어요\uD83E\uDEE2 곧 업데이트 예정이에요")
        }
        clHomeUpload.setOnClickListener { navigateToReviewActivity() }
        ivHomeSetting.setOnClickListener { navigateToSettingActivity() }
    }

    private fun initObserver() {
        homeViewModel.stadiums.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    makeSpotImageAppbar("경기장 정보를 불러오는데 실패하였습니다.")
                    setStadiumShimmer(true)
                }

                is UiState.Loading -> {
                    setStadiumShimmer(true)
                }

                is UiState.Success -> {
                    setStadiumShimmer(false)
                    stadiumAdapter.submitList(state.data)
                    binding.rvHomeStadium.scrollToPosition(0)
                }
            }
        }

        homeViewModel.homeFeed.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    makeSpotImageAppbar("내 정보 불러오기를 실패하였습니다.\uD83E\uDEE2")
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

        homeViewModel.levelState.asLiveData().observe(this) {
            val currentState = homeViewModel.homeFeed.value
            if (it && currentState is UiState.Success) {
                LevelupDialog(
                    currentState.data.levelTitle,
                    currentState.data.level,
                    currentState.data.mascotImageUrl
                ).show(supportFragmentManager, LevelupDialog.TAG)
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
                    makeSpotImageAppbar("${it.name} 야구장은 곧 업데이트 예정이에요")
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
        binding.rvHomeStadium.itemAnimator = null
    }


    private fun startStadiumSelectionActivity() {
        Intent(this@HomeActivity, StadiumSelectionActivity::class.java).apply {
            startActivity(
                this
            )
        }
    }

    private fun startSeatRecordActivity() {
        Intent(this, SeatRecordActivity::class.java).apply { startActivity(this) }
    }

    private fun startStadiumActivity(stadium: ResponseStadiums) {
        val intent = Intent(
            this@HomeActivity,
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
            rvHomeStadium.visibility = View.INVISIBLE
            shimmerHomeStadium.visibility = View.VISIBLE

        } else {
            shimmerHomeStadium.stopShimmer()
            rvHomeStadium.visibility = View.VISIBLE
            shimmerHomeStadium.visibility = View.GONE

        }
    }

    private fun setHomeFeed(data: HomeFeedResponse) = with(binding) {
        "Lv.${data.level}".also { tvHomeLevel.text = it }
        tvHomeTeam.text = if (data.teamId == null) {
            "모두를 응원하는"
        } else {
            "${data.teamName}의"
        }
        tvHomeTitle.text = data.levelTitle
        ivHomeCharacter.load(data.mascotImageUrl)
        csbvHomeTitle.setTextPart("시야 사진 ", data.reviewCntToLevelup,"장 더 올리면 레벨업!")
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

    private fun navigateToSettingActivity() {
        Intent(this, SettingActivity::class.java).apply { startActivity(this) }
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary
        ).show()
    }
}