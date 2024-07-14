package com.depromeet.presentation.seatrecord

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
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
        const val DETAIL_RECORD_REVIEW_ID = "detail_record_review_id"
    }

    private lateinit var detailRecordAdapter: DetailRecordAdapter
    private val viewModel: SeatDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getReviewData()

        viewModel.uiState.observe(this) {
            detailRecordAdapter.submitList(it.list)
        }


        detailRecordAdapter = DetailRecordAdapter()
        binding.rvDetailRecord.adapter = detailRecordAdapter

        detailRecordAdapter.itemMoreClickListener =
            object : DetailRecordAdapter.OnDetailItemClickListener {
                override fun onItemMoreClickListener(item: ReviewDetailMockData) {
                    RecordEditDialog().apply {
                        arguments = bundleOf(DETAIL_RECORD_REVIEW_ID to item.reviewId)
                        show(supportFragmentManager, this.tag)
                    }
                }
            }
    }
}