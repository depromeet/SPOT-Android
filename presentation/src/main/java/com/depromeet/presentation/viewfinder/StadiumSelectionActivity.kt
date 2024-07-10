package com.depromeet.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.core.base.BaseActivity
import com.depromeet.designsystem.SpotSnackBar
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumSelectionBinding
import com.depromeet.presentation.extension.dpToPx
import com.depromeet.presentation.viewfinder.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.viewfinder.adapter.StadiumSelectionAdapter
import com.depromeet.presentation.viewfinder.sample.Stadium
import com.depromeet.presentation.viewfinder.sample.stadiums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumSelectionActivity : BaseActivity<ActivityStadiumSelectionBinding>({
    ActivityStadiumSelectionBinding.inflate(it)
}) {
    companion object {
        const val STADIUM_EXTRA = "stadium"
        private const val STADIUM_GRID_SPAN_COUNT = 2
    }

    private lateinit var stadiumSelectionAdapter: StadiumSelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureRecyclerViewAdapter()
        onClickStadium()
        stadiumSelectionAdapter.submitList(stadiums)
    }

    private fun configureRecyclerViewAdapter() {
        stadiumSelectionAdapter = StadiumSelectionAdapter()
        binding.rvStadium.apply {
            layoutManager = GridLayoutManager(context, STADIUM_GRID_SPAN_COUNT)
            adapter = stadiumSelectionAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = STADIUM_GRID_SPAN_COUNT,
                    spacing = 16.dpToPx(context)
                )
            )
        }
    }

    private fun onClickStadium() {
        stadiumSelectionAdapter.itemStadiumClickListener =
            object : StadiumSelectionAdapter.OnItemStadiumClickListener {
                override fun onItemStadiumClick(stadium: Stadium) {
                    if (stadium.lock) {
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

    private fun startStadiumActivity(stadium: Stadium) {
        val intent = Intent(
            this@StadiumSelectionActivity,
            StadiumActivity::class.java
        ).apply {
            putExtra(STADIUM_EXTRA, stadium)
        }
        startActivity(intent)
    }
}