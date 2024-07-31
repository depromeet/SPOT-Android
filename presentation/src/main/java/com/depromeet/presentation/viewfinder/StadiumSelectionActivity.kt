package com.depromeet.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.designsystem.SpotSnackBar
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumSelectionBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.util.SpannableStringUtils
import com.depromeet.presentation.viewfinder.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.viewfinder.adapter.StadiumSelectionAdapter
import com.depromeet.presentation.viewfinder.viewmodel.StadiumSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumSelectionActivity : BaseActivity<ActivityStadiumSelectionBinding>({
    ActivityStadiumSelectionBinding.inflate(it)
}) {
    companion object {
        const val STADIUM_EXTRA_ID = "stadium_id"
        private const val STADIUM_GRID_SPAN_COUNT = 2
        private const val STADIUM_GRID_SPACING = 8
    }

    private val viewModel: StadiumSelectionViewModel by viewModels()
    private lateinit var stadiumSelectionAdapter: StadiumSelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        observeData()
    }

    private fun initView() {
        viewModel.getStadiums()
        setTextTitleColor()
        configureRecyclerViewAdapter()
    }

    private fun initEvent() {
        onClickStadium()
        onClickClose()
    }

    private fun observeData() {
        viewModel.stadiums.asLiveData().observe(this) { stadiums ->
            when (stadiums) {
                is UiState.Empty -> Unit
                is UiState.Failure -> toast(stadiums.msg)
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    stadiumSelectionAdapter.submitList(stadiums.data)
                }
            }
        }
    }

    private fun setTextTitleColor() {
        binding.tvTitle.text = SpannableStringUtils(this).toColorSpan(
            color = com.depromeet.designsystem.R.color.color_stroke_positive_primary,
            text = binding.tvTitle.text,
            start = 0,
            end = 6
        )
    }

    private fun configureRecyclerViewAdapter() {
        stadiumSelectionAdapter = StadiumSelectionAdapter()
        binding.rvStadium.apply {
            layoutManager = GridLayoutManager(context, STADIUM_GRID_SPAN_COUNT)
            adapter = stadiumSelectionAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = STADIUM_GRID_SPAN_COUNT,
                    spacing = STADIUM_GRID_SPACING.dpToPx(context)
                )
            )
        }
    }

    private fun onClickStadium() {
        stadiumSelectionAdapter.itemStadiumClickListener =
            object : StadiumSelectionAdapter.OnItemStadiumClickListener {
                override fun onItemStadiumClick(stadium: StadiumsResponse) {
                    if (!stadium.isActive) {
                        SpotSnackBar.make(
                            view = binding.root,
                            message = getString(R.string.viewfinder_lock_warning_description),
                            endMessage = getString(R.string.viewfinder_lock_warning_trigger),
                            onClick = {
                                startStadiumActivity(stadium)
                            }).show()
                    } else {
                        startStadiumActivity(stadium)
                    }
                }
            }
    }

    private fun onClickClose() {
        binding.ivClose.setOnClickListener {
            // go to main activity
        }
    }

    private fun startStadiumActivity(stadium: StadiumsResponse) {
        val intent = Intent(
            this@StadiumSelectionActivity,
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