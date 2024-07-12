package com.depromeet.presentation.viewfinder

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.databinding.ActivityStadiumDetailBinding
import com.depromeet.presentation.viewfinder.compose.StadiumDetailScreen
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumDetailActivity : BaseActivity<ActivityStadiumDetailBinding>({
    ActivityStadiumDetailBinding.inflate(it)
}) {
    private val viewModel: StadiumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.spotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.spotAppbar.setMenuOnClickListener {
            // go to home
        }

        binding.btnUp.setOnClickListener {
            viewModel.updateScrollState(true)
        }

        binding.composeView.setContent {
            StadiumDetailScreen(
                viewModel = viewModel
            )
        }
    }
}