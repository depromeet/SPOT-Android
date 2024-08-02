package com.depromeet.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.designsystem.SpotImageSnackBar
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.presentation.databinding.ActivityHomeGuiBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.adapter.StadiumAdapter
import com.depromeet.presentation.home.viewmodel.HomeGuiViewModel
import com.depromeet.presentation.seatrecord.SeatRecordActivity
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.viewfinder.StadiumActivity
import com.depromeet.presentation.viewfinder.StadiumSelectionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeGuiActivity : BaseActivity<ActivityHomeGuiBinding>(
    ActivityHomeGuiBinding::inflate
) {
    companion object {
        const val STADIUM_EXTRA_ID = "stadium_id"
        private const val START_SPACING_DP = 16
        private const val BETWEEN_SPADING_DP = 8
    }

    private val stadiumViewModel: HomeGuiViewModel by viewModels()

    private lateinit var stadiumAdapter: StadiumAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        stadiumViewModel.getStadiums()
        setStadiumAdapter()
    }

    private fun initEvent() {
        binding.clHomeScrap.setOnClickListener { toast("아직 열리지 않음") }
        binding.clHomeArchiving.setOnClickListener { startSeatRecordActivity() }
        binding.ivHomeInfo.setOnClickListener { showLevelDescriptionDialog() }
        binding.clHomeScrap.setOnClickListener {
            SpotImageSnackBar.make(
                view = binding.root,
                message = "스크랩이 잠겨있어요\uD83E\uDEE2 곧 업데이트 예정이에요",
                messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
                icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
                iconColor = com.depromeet.designsystem.R.color.color_error_secondary
            ).show()
        }
    }

    private fun initObserver() {
        stadiumViewModel.stadiums.asLiveData().observe(this) { state ->
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
}