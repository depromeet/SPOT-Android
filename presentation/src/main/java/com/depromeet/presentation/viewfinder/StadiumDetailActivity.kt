package com.depromeet.presentation.viewfinder

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumDetailBinding
import com.depromeet.presentation.viewfinder.compose.StadiumDetailScreen
import com.depromeet.presentation.viewfinder.dialog.ReportDialog
import com.depromeet.presentation.viewfinder.dialog.StadiumFilterMonthsDialog
import com.depromeet.presentation.viewfinder.dialog.StadiumSelectSeatDialog
import com.depromeet.presentation.viewfinder.sample.ReviewContent
import com.depromeet.presentation.viewfinder.sample.stadiums
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
        initView()
        initEvent()

        binding.composeView.setContent {
            StadiumDetailScreen(
                viewModel = viewModel,
                onClickReviewPicture = { reviewContent ->
                    startToStadiumDetailPictureFragment(reviewContent)
                },
                onClickSelectSeat = {
                    startToBottomSheetDialog(
                        StadiumSelectSeatDialog.newInstance(),
                        StadiumSelectSeatDialog.TAG
                    )
                },
                onClickFilterMonthly = {
                    startToBottomSheetDialog(
                        StadiumFilterMonthsDialog.newInstance(),
                        StadiumFilterMonthsDialog.TAG
                    )
                },
                onClickReport = {
                    startToBottomSheetDialog(ReportDialog.newInstance(), ReportDialog.TAG)
                }
            )
        }
    }

    private fun initView() {
        getIdExtra { stadiumId, blockId ->
            viewModel.getBlockReviews(stadiumId, blockId)
        }
    }

    private fun initEvent() {
        binding.spotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.spotAppbar.setMenuOnClickListener {
            // go to home
        }

        binding.btnUp.setOnClickListener {
            viewModel.updateScrollState(true)
        }
    }

    private fun getIdExtra(callback: (stadiumId: Int, blockId: String) -> Unit) {
        callback(
            intent?.getIntExtra(StadiumActivity.STADIUM_ID, 0) ?: 0,
            intent?.getStringExtra(StadiumActivity.STADIUM_BLOCK_ID) ?: ""
        )
    }

    private fun startToStadiumDetailPictureFragment(reviewContent: ReviewContent) {
        val fragment = StadiumDetailPictureFragment.newInstance().apply {
            arguments = bundleOf(REVIEW_PICTURE_CONTENT to reviewContent)
        }

        supportFragmentManager.commit {
            replace(R.id.fcv_detail_picture, fragment, StadiumDetailPictureFragment.TAG)
        }
    }

    private fun startToBottomSheetDialog(dialogInstance: DialogFragment, tag: String) {
        dialogInstance.show(supportFragmentManager, tag)
    }
}