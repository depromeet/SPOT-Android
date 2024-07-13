package com.depromeet.presentation.seatrecord

import android.os.Build
import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.seatrecord.mockdata.ReviewMockData

class SeatDetailRecordActivity : BaseActivity<ActivitySeatDetailRecordBinding>(
    ActivitySeatDetailRecordBinding::inflate
) {
    private val data : ReviewMockData? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                "setData", ReviewMockData::class.java
            )
        } else {
            intent?.getParcelableExtra(
                "setData"
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toast("${data?.date}, ${data?.id}")
    }
}