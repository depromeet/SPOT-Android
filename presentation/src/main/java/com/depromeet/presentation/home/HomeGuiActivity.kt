package com.depromeet.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
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
    }

    private fun initObserver() {
        stadiumViewModel.stadiums.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> toast(state.msg)
                is UiState.Loading -> toast("로딩중")
                is UiState.Success -> {
                    stadiumAdapter.submitList(state.data)
                    binding.rvHomeStadium.scrollToPosition(0)
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
}