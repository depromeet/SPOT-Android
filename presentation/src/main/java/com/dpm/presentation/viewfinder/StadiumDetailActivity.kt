package com.dpm.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.dpm.core.base.BaseActivity
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumDetailBinding
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.viewfinder.compose.StadiumDetailScreen
import com.dpm.presentation.viewfinder.dialog.ReportDialog
import com.dpm.presentation.viewfinder.dialog.StadiumFilterMonthsDialog
import com.dpm.presentation.viewfinder.dialog.StadiumSelectSeatDialog
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumDetailActivity : BaseActivity<ActivityStadiumDetailBinding>({
    ActivityStadiumDetailBinding.inflate(it)
}) {
    companion object {
        const val REVIEW_ID = "review_id"
        const val REVIEW_INDEX = "review_index"
        const val REVIEW_TITLE_WITH_STADIUM = "review_title_with_stadium"
        const val STADIUM_HEADER = "stadium_header"
        const val STADIUM_REVIEW_CONTENT = "stadium_review_content"
    }

    private val viewModel: StadiumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        getIdExtra { stadiumId, blockCode ->
            viewModel.updateRequestPathVariable(stadiumId, blockCode)
            viewModel.getBlockReviews(stadiumId, blockCode)
            viewModel.getBlockRow(stadiumId, blockCode)
        }
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            MaterialTheme {
                StadiumDetailScreen(
                    blockNumber = viewModel.blockCode,
                    viewModel = viewModel,
                    onClickReviewPicture = { reviewContent, index, title ->
                        startToStadiumDetailPictureFragment(reviewContent, index, title)
                    },
                    onClickSelectSeat = {
                        StadiumSelectSeatDialog.apply {
                            newInstance().show(
                                supportFragmentManager, TAG
                            )
                        }
                    },
                    onClickFilterMonthly = {
                        StadiumFilterMonthsDialog.apply {
                            newInstance().show(
                                supportFragmentManager, TAG
                            )
                        }
                    },
                    onClickReport = {
                        ReportDialog.apply {
                            newInstance().show(
                                supportFragmentManager, TAG
                            )
                        }
                    },
                    onClickGoBack = {
                        finish()
                    },
                    onRefresh = {
                        viewModel.getBlockReviews()
                    }
                )
            }
        }
    }

    private fun initEvent() {
        binding.spotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.spotAppbar.setMenuOnClickListener {
            startToHomeActivity()
        }

        binding.btnUp.setOnClickListener {
            viewModel.updateScrollState(false)
        }
    }

    private fun initObserver() {
        viewModel.scrollState.asLiveData().observe(this) {
            if (it) binding.btnUp.visibility = View.VISIBLE
            else binding.btnUp.visibility = View.INVISIBLE
        }
    }

    private fun getIdExtra(callback: (stadiumId: Int, blockCode: String) -> Unit) {
        callback(
            intent?.getIntExtra(StadiumActivity.STADIUM_ID, 0) ?: 0,
            intent?.getStringExtra(StadiumActivity.STADIUM_BLOCK_ID) ?: ""
        )
    }

    private fun startToStadiumDetailPictureFragment(
        reviewContent: ResponseBlockReview.ResponseReview,
        index: Int,
        title: String
    ) {
        val fragment = StadiumDetailPictureFragment.newInstance().apply {
            arguments = bundleOf(
                REVIEW_ID to reviewContent.id,
                REVIEW_INDEX to index,
                REVIEW_TITLE_WITH_STADIUM to title
            )
        }

        supportFragmentManager.commit {
            replace(R.id.fcv_detail_picture, fragment, StadiumDetailPictureFragment.TAG)
        }
    }

    private fun startToHomeActivity() {
        Intent(
            this,
            HomeActivity::class.java
        ).apply {
            startActivity(this)
            finishAffinity()
        }
    }
}