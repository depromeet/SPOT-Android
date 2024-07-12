package com.depromeet.presentation.seatReview

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewDoneBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewDoneActivity : BaseActivity<ActivityReviewDoneBinding>({
    ActivityReviewDoneBinding.inflate(it)
}) {

    private val viewModel by viewModels<ReviewViewModel>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coroutineScope.launch {
            delay(2000L)
            finish()
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}
