package com.depromeet.presentation.seatReview

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewMainActivity : BaseActivity<ActivityReviewMainBinding>({
    ActivityReviewMainBinding.inflate(it)
}) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
