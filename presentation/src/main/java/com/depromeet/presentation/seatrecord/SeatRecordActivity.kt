package com.depromeet.presentation.seatrecord

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatRecordActivity : BaseActivity<ActivitySeatRecordBinding>(
    ActivitySeatRecordBinding::inflate
) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recordSpotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.recordSpotAppbar.setMenuOnClickListener {
            //셋팅 이동
        }
    }
}