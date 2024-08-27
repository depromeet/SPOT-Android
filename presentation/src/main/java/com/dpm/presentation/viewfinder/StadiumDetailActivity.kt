package com.dpm.presentation.viewfinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.dpm.core.base.BaseActivity
import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityStadiumDetailBinding
import com.dpm.designsystem.SpotSnackBar
import com.dpm.designsystem.extension.dpToPx
import com.dpm.domain.preference.SharedPreference
import com.dpm.presentation.extension.getCompatibleParcelableExtra
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.scrap.ScrapActivity
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.MixpanelManager
import com.dpm.presentation.util.Utils
import com.dpm.presentation.util.seatFeed
import com.dpm.presentation.util.toEmptyBlock
import com.dpm.presentation.viewfinder.compose.StadiumDetailScreen
import com.dpm.presentation.viewfinder.dialog.ReportDialog
import com.dpm.presentation.viewfinder.dialog.StadiumFilterMonthsDialog
import com.dpm.presentation.viewfinder.dialog.StadiumSelectSeatDialog
import com.dpm.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StadiumDetailActivity : BaseActivity<ActivityStadiumDetailBinding>({
    ActivityStadiumDetailBinding.inflate(it)
}) {
    companion object {
        const val REVIEW_ID = "review_id"
        const val REVIEW_INDEX = "review_index"
        const val REVIEW_TITLE_WITH_STADIUM = "review_title_with_stadium"
        const val REVIEW_TYPE = "review_type"

        const val STADIUM_HEADER = "stadium_header"
        const val STADIUM_REVIEW_CONTENT = "stadium_review_content"
    }

    @Inject
    lateinit var sharedPreference: SharedPreference

    private lateinit var scrapActiveSnackBar: SpotSnackBar
    private lateinit var scrapInActiveSnackBar: SpotSnackBar

    private val viewModel: StadiumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        getIdExtra { stadiumId, blockCode, reviewId ->
            viewModel.updateRequestPathVariable(stadiumId, blockCode)
            viewModel.getBlockReviews(stadiumId, blockCode)
            viewModel.getBlockRow(stadiumId, blockCode)
            viewModel.reviewId = reviewId
        }
        initComposeView()
        initSnackBar()
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            MaterialTheme {
                StadiumDetailScreen(
                    emptyBlockName = toEmptyBlock(viewModel.stadiumId, viewModel.blockCode),
                    isFirstShare = sharedPreference.isFirstShare,
                    viewModel = viewModel,
                    onClickReviewPicture = { id, index, title ->
                        startToStadiumDetailPictureFragment(id, index, title, DetailReviewEntryPoint.MAIN_REVIEW)
                    },
                    onClickSelectSeat = {
                        MixpanelManager.track("viewfinder_select_seat")
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
                    },
                    onClickTopImage = { id, index, title ->
                        startToStadiumDetailPictureFragment(id, index, title, DetailReviewEntryPoint.TOP_REVIEW)
                    },
                    onClickLike = { },
                    onClickScrap = { isScrap ->
                        if (isScrap) {
                            scrapActiveSnackBar.dismiss()
                            scrapInActiveSnackBar.show()
                        } else {
                            scrapInActiveSnackBar.dismiss()
                            scrapActiveSnackBar.show()
                        }
                    },
                    onClickShare = {
                        sharedPreference.isFirstShare = false
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

    private fun initSnackBar() {
        val navigationHeight = (Utils(this).navigationHeight / resources.displayMetrics.density).toInt()
        scrapActiveSnackBar = SpotSnackBar.make(
            view = binding.root.rootView,
            background = com.depromeet.designsystem.R.drawable.rect_body_subtitle_fill_60,
            message = getString(R.string.viewfinder_snackbar_scrap),
            endMessage = getString(R.string.viewfinder_underscore_snackbar_scrap),
            marginBottom = navigationHeight,
            onClick = {
                Intent(this, ScrapActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }.let { startActivity(it) }
            })
        scrapInActiveSnackBar = SpotSnackBar.make(
            view = binding.root.rootView,
            background = com.depromeet.designsystem.R.drawable.rect_body_subtitle_fill_60,
            message = getString(R.string.viewfinder_inactive_snackbar_scrap),
            marginBottom = navigationHeight,
            onClick = {}
        )
    }

    private fun getIdExtra(callback: (stadiumId: Int, blockCode: String, reviewId: Int) -> Unit) {
        callback(
            intent?.getIntExtra(SchemeKey.STADIUM_ID, 0) ?: 0,
            intent?.getStringExtra(SchemeKey.BLOCK_CODE) ?: "",
            intent?.getIntExtra(SchemeKey.REVIEW_ID, 0) ?: 0
        )
    }

    private fun startToStadiumDetailPictureFragment(
        id: Long,
        index: Int,
        title: String,
        type: DetailReviewEntryPoint
    ) {
        MixpanelManager.track("viewfinder_check_view")
        val fragment = StadiumDetailPictureFragment.newInstance().apply {
            arguments = bundleOf(
                REVIEW_ID to id,
                REVIEW_INDEX to index,
                REVIEW_TITLE_WITH_STADIUM to title,
                REVIEW_TYPE to type.name,
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