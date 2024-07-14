package com.depromeet.presentation.seatrecord

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DetailRecordAdapter
import com.depromeet.presentation.seatrecord.mockdata.makeReviewDetailListData

class SeatDetailRecordActivity : BaseActivity<ActivitySeatDetailRecordBinding>(
    ActivitySeatDetailRecordBinding::inflate
) {
    private lateinit var detailRecordAdapter: DetailRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        detailRecordAdapter = DetailRecordAdapter()
        binding.rvDetailRecord.adapter = detailRecordAdapter
        detailRecordAdapter.submitList(makeReviewDetailListData())


    }
}