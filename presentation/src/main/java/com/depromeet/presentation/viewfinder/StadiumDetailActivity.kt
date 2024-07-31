package com.depromeet.presentation.viewfinder

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumDetailBinding
import com.depromeet.presentation.viewfinder.compose.StadiumDetailScreen
import com.depromeet.presentation.viewfinder.dialog.ReportDialog
import com.depromeet.presentation.viewfinder.dialog.StadiumFilterMonthsDialog
import com.depromeet.presentation.viewfinder.dialog.StadiumSelectSeatDialog
import com.depromeet.presentation.viewfinder.uistate.StadiumDetailUiState
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumDetailActivity : BaseActivity<ActivityStadiumDetailBinding>({
    ActivityStadiumDetailBinding.inflate(it)
}) {
    companion object {
        const val REVIEW_ID = "review_id"
        const val REVIEW_INDEX = "review_index"
        const val STADIUM_HEADER = "stadium_header"
        const val STADIUM_REVIEW_CONTENT = "stadium_review_content"
    }

    private val viewModel: StadiumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        initObserver()

        binding.composeView.setContent {
            StadiumDetailScreen(
                blockNumber = viewModel.blockCode,
                viewModel = viewModel,
                onClickReviewPicture = { reviewContent, index ->
                    startToStadiumDetailPictureFragment(reviewContent, index)
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
                },
                onClickGoBack = {
                    finish()
                }
            )
        }
    }

    private fun initView() {
        getIdExtra { stadiumId, blockCode ->
            viewModel.updateRequestPathVariable(stadiumId, blockCode)
            viewModel.getBlockReviews(stadiumId, blockCode)
            viewModel.getBlockRow(stadiumId, blockCode)
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

    private fun initObserver() {
        viewModel.detailUiState.asLiveData().observe(this) { uiState ->
            when (uiState) {
                is StadiumDetailUiState.Empty -> binding.btnUp.visibility = View.GONE
                else -> Unit
            }
        }
    }

    private fun getIdExtra(callback: (stadiumId: Int, blockCode: String) -> Unit) {
        callback(
            intent?.getIntExtra(StadiumActivity.STADIUM_ID, 0) ?: 0,
            intent?.getStringExtra(StadiumActivity.STADIUM_BLOCK_ID) ?: ""
        )
    }

    private fun startToStadiumDetailPictureFragment(reviewContent: BlockReviewResponse.ReviewResponse, index: Int) {
        val fragment = StadiumDetailPictureFragment.newInstance().apply {
            arguments = bundleOf(REVIEW_ID to reviewContent.id, REVIEW_INDEX to index)
        }

        supportFragmentManager.commit {
            replace(R.id.fcv_detail_picture, fragment, StadiumDetailPictureFragment.TAG)
        }
    }

    private fun startToBottomSheetDialog(dialogInstance: DialogFragment, tag: String) {
        dialogInstance.show(supportFragmentManager, tag)
    }
}