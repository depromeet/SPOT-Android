package com.depromeet.presentation.seatReview

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewDoneBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewDoneActivity : BaseActivity<ActivityReviewDoneBinding>({
    ActivityReviewDoneBinding.inflate(it)
}) {

    private val viewModel by viewModels<ReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(2000L)
            finish()
        }
    }
}
