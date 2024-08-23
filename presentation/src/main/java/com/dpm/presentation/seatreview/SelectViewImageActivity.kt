package com.dpm.presentation.seatreview

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.presentation.databinding.ActivitySelectViewImageBinding
import com.dpm.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectViewImageActivity : BaseActivity<ActivitySelectViewImageBinding>({
    ActivitySelectViewImageBinding.inflate(it)
}) {
    private val viewModel by viewModels<ReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
