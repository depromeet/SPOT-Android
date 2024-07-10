package com.depromeet.presentation.seatReview

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewDoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewDoneActivity : BaseActivity<ActivityReviewDoneBinding>({
    ActivityReviewDoneBinding.inflate(it)
}) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
