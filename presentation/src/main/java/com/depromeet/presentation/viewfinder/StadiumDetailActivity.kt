package com.depromeet.presentation.viewfinder

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumDetailBinding
import com.depromeet.presentation.viewfinder.compose.StadiumDetailScreen
import com.depromeet.presentation.viewfinder.dialog.StadiumSelectSeatDialog
import com.depromeet.presentation.viewfinder.sample.ReviewContent
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumDetailActivity : BaseActivity<ActivityStadiumDetailBinding>({
    ActivityStadiumDetailBinding.inflate(it)
}) {
    companion object {
        const val REVIEW_PICTURE_CONTENT = "review_picture_content"
        const val STADIUM_HEADER = "stadium_header"
        const val STADIUM_REVIEW_CONTENT = "stadium_review_content"
    }

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
                viewModel = viewModel,
                onClickReviewPicture = { reviewContent ->
                    startToStadiumDetailPictureFragment(reviewContent)
                },
                onClickSelectSeat = {
                    StadiumSelectSeatDialog.newInstance()
                        .show(supportFragmentManager, StadiumSelectSeatDialog.TAG)
                },
                }
            )
        }
    }

    private fun startToStadiumDetailPictureFragment(reviewContent: ReviewContent) {
        val fragment = StadiumDetailPictureFragment.newInstance().apply {
            arguments = bundleOf(REVIEW_PICTURE_CONTENT to reviewContent)
        }

        supportFragmentManager.commit {
            replace(R.id.fcv_detail_picture, fragment, StadiumDetailPictureFragment.TAG)
        }
    }
}