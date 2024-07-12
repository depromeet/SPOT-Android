package com.depromeet.presentation.seatReview

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewDoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewDoneActivity : BaseActivity<ActivityReviewDoneBinding>({
    ActivityReviewDoneBinding.inflate(it)
}) {

    private val viewModel by viewModels<ReviewViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
