package com.depromeet.presentation.seatrecord

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DetailRecordAdapter
import com.depromeet.presentation.seatrecord.mockdata.ReviewDetailMockData
import com.depromeet.presentation.seatrecord.viewmodel.SeatDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatDetailRecordActivity : BaseActivity<ActivitySeatDetailRecordBinding>(
    ActivitySeatDetailRecordBinding::inflate
) {
    companion object {
        const val SEAT_DETAIL_TAG = "SEAT_DETAIL"
    }

    private lateinit var detailRecordAdapter: DetailRecordAdapter
    private val viewModel: SeatDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDetailRecordAdapter()
        viewModel.getReviewData()
        viewModel.uiState.asLiveData().observe(this) {
            detailRecordAdapter.submitList(it.list)
        }

        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state) moveConfirmationDialog()
        }

        setClickListener()

    }

    private fun setDetailRecordAdapter() {
        detailRecordAdapter = DetailRecordAdapter()
        binding.rvDetailRecord.adapter = detailRecordAdapter

        detailRecordAdapter.itemMoreClickListener =
            object : DetailRecordAdapter.OnDetailItemClickListener {
                override fun onItemMoreClickListener(item: ReviewDetailMockData) {
                    viewModel.setEditReviewId(item.reviewId)
                    RecordEditDialog.newInstance(SEAT_DETAIL_TAG)
                        .apply { show(supportFragmentManager, this.tag) }
                }
            }
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_DETAIL_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }

    private fun setClickListener() {
        with(binding){
            fabDetailUp.setOnClickListener {
                rvDetailRecord.smoothScrollToPosition(0)
            }
        }
    }

}