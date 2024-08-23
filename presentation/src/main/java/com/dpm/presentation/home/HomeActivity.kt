package com.dpm.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.presentation.databinding.ActivityHomeBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.home.ResponseHomeFeed
import com.dpm.domain.entity.response.viewfinder.ResponseStadiums
import com.dpm.presentation.extension.dpToPx
import com.dpm.presentation.home.adapter.StadiumAdapter
import com.dpm.presentation.home.dialog.LevelDescriptionDialog
import com.dpm.presentation.home.dialog.LevelupDialog
import com.dpm.presentation.home.viewmodel.HomeGuiViewModel
import com.dpm.presentation.seatrecord.SeatRecordActivity
import com.dpm.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.dpm.presentation.seatreview.dialog.ReviewTypeDialog
import com.dpm.presentation.setting.SettingActivity
import com.dpm.presentation.util.Utils
import com.dpm.presentation.viewfinder.StadiumActivity
import com.dpm.presentation.viewfinder.StadiumSelectionActivity
import dagger.hilt.android.AndroidEntryPoint

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
        initViewStatusBar()
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
                    stadiumAdapter.submitList(emptyList())
                    setStadiumShimmer(true)
                }

                is UiState.Loading -> {
                    stadiumAdapter.submitList(emptyList())
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
                homeViewModel.levelState.value = false
            }
        }
    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_secondary)
            setBlackSystemBarIconColor(window)
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
            shimmerHomeStadium.visibility = View.VISIBLE

        } else {
            shimmerHomeStadium.stopShimmer()
            shimmerHomeStadium.visibility = View.GONE

        }
    }

    private fun setHomeFeed(data: ResponseHomeFeed) = with(binding) {
        "Lv.${data.level}".also { tvHomeLevel.text = it }
        tvHomeTeam.text = if (data.teamId == null) {
            "모두를 응원하는"
        } else {
            "${data.teamName}의"
        }
        tvHomeTitle.text = data.levelTitle
        ivHomeCharacter.load(data.mascotImageUrl)
        if (data.reviewCntToLevelup == 0) {
            csbvHomeTitle.setTextPart("내가 바로 이 구역 직관왕!", number = null, suffix = null)
        } else {
            csbvHomeTitle.setTextPart("시야 사진 ", data.reviewCntToLevelup, "장 더 올리면 레벨업!")
        }

    }

    private fun navigateToReviewActivity() {
        ReviewTypeDialog().show(supportFragmentManager, "MyDialog")
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
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 94
        ).show()
    }
}