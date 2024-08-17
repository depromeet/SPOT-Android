package com.dpm.presentation.seatreview

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.dpm.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityReviewDoneBinding
import com.dpm.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewDoneActivity : BaseActivity<ActivityReviewDoneBinding>({
    ActivityReviewDoneBinding.inflate(it)
}) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(2000L)
            startActivity(Intent(this@ReviewDoneActivity, HomeActivity::class.java))
            finish()
        }
    }
}
