package com.dpm.presentation.home

import ReviewData
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.presentation.databinding.ActivityHomeBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.designsystem.SpotSnackBar
import com.dpm.domain.entity.response.home.ResponseHomeFeed
import com.dpm.domain.entity.response.viewfinder.ResponseStadiums
import com.dpm.domain.model.seatreview.ReviewMethod
import com.dpm.domain.preference.SharedPreference
import com.dpm.presentation.extension.dpToPx
import com.dpm.presentation.extension.getCompatibleParcelableExtra
import com.dpm.presentation.home.adapter.StadiumAdapter
import com.dpm.presentation.home.dialog.LevelDescriptionDialog
import com.dpm.presentation.home.dialog.LevelupDialog
import com.dpm.presentation.home.viewmodel.HomeGuiViewModel
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.scheme.viewmodel.SchemeState
import com.dpm.presentation.scrap.ScrapActivity
import com.dpm.presentation.seatrecord.SeatRecordActivity
import com.dpm.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.dpm.presentation.seatreview.dialog.ReviewTypeDialog
import com.dpm.presentation.seatreview.dialog.feed.FeedUploadDialog
import com.dpm.presentation.seatreview.dialog.view.ViewUploadDialog
import com.dpm.presentation.seatreview.sample.LevelUpManager
import com.dpm.presentation.setting.SettingActivity
import com.dpm.presentation.util.MixpanelManager
import com.dpm.presentation.util.Utils
import com.dpm.presentation.viewfinder.StadiumActivity
import com.dpm.presentation.viewfinder.StadiumDetailActivity
import com.dpm.presentation.viewfinder.StadiumSelectionActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
) {
    @Inject
    lateinit var sharedPreference: SharedPreference

    companion object {
        const val STADIUM_EXTRA_ID = "stadium_id"
        private const val START_SPACING_DP = 16
        private const val BETWEEN_SPADING_DP = 0
        private const val VIEW_UPLOAD_DIALOG = "ViewUploadDialog"
        private const val FEED_UPLOAD_DIALOG = "FeedUploadDialog"
        private const val REVIEW_DATA = "REVIEW_DATA"
        private const val DIALOG_TYPE = "DIALOG_TYPE"
        private const val CANCEL_SNACKBAR = "CANCEL_SNACKBAR"
        private const val UPLOAD_SNACKBAR = "UPLOAD_SNACKBAR"
    }

    private val homeViewModel: HomeGuiViewModel by viewModels()
    private lateinit var stadiumAdapter: StadiumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initView()
        initEvent()
        initObserver()

        if (intent.getBooleanExtra("IS_VISIBLE_LEVELUP_DIALOG",false)) {
            homeViewModel.getHomeFeed(isVisibleDialog = true) {
                if (sharedPreference.level < it.level) {
                    LevelupDialog(
                        it.levelTitle,
                        it.level,
                        it.mascotImageUrl
                    ).show(supportFragmentManager, LevelupDialog.TAG)
                }
                sharedPreference.teamId = it.teamId ?: 0
                sharedPreference.levelTitle = it.levelTitle
                sharedPreference.teamName = it.teamName ?: ""
                sharedPreference.level = it.level
            }
        }

        LevelUpManager.setOnLevelUpListener {
            homeViewModel.getHomeFeed(isVisibleDialog = true) {
                if (sharedPreference.level < it.level) {
                    LevelupDialog(
                        it.levelTitle,
                        it.level,
                        it.mascotImageUrl
                    ).show(supportFragmentManager, LevelupDialog.TAG)
                }
                sharedPreference.teamId = it.teamId ?: 0
                sharedPreference.levelTitle = it.levelTitle
                sharedPreference.teamName = it.teamName ?: ""
                sharedPreference.level = it.level
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!(intent.getBooleanExtra("IS_VISIBLE_LEVELUP_DIALOG",false))) {
            homeViewModel.getHomeFeed()
        }
    }

    private fun initView() {
        initReviewDialog()
        initViewStatusBar()
        homeViewModel.getStadiums()
        setStadiumAdapter()
        handleIntentExtra()
    }

    private fun initReviewDialog() {
        val reviewData = intent.getParcelableExtra<ReviewData>(REVIEW_DATA)

        when (intent?.getSerializableExtra(DIALOG_TYPE) as? ReviewMethod) {
            ReviewMethod.VIEW -> ViewUploadDialog().apply {
                arguments = Bundle().apply { putParcelable(REVIEW_DATA, reviewData) }
            }.show(supportFragmentManager, VIEW_UPLOAD_DIALOG)

            ReviewMethod.FEED -> FeedUploadDialog().apply {
                arguments = Bundle().apply { putParcelable(REVIEW_DATA, reviewData) }
            }.show(supportFragmentManager, FEED_UPLOAD_DIALOG)

            else -> {}
        }

        intent.getBooleanExtra(CANCEL_SNACKBAR, false).takeIf { it }?.run {
            makeSpotImageAppbar("다음에는 좌석 시야 공유도 기대할게요!")
        }

        intent.getBooleanExtra(UPLOAD_SNACKBAR, false).takeIf { it }?.run {
            SpotSnackBar.make(
                view = binding.root,
                message = "시야찾기에 내 게시글이 올라갔어요!",
                endMessage = "확인하러 가기",
                marginBottom = 87,
            ) {
                val reviewData = intent.getCompatibleParcelableExtra<ReviewData>(REVIEW_DATA)
                if (reviewData != null) {
                    Intent(this@HomeActivity, StadiumDetailActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra(SchemeKey.STADIUM_ID, reviewData.stadiumId)
                        putExtra(SchemeKey.BLOCK_CODE, reviewData.blockCode)
                        putExtra(SchemeKey.REVIEW_ID, reviewData.reviewId)
                        putExtra("IMAGE_UPLOAD", true)
                    }.let { startActivity(it) }
                }
            }.show()
        }
    }



    private fun initEvent() = with(binding) {
        clHomeArchiving.setOnClickListener {
            MixpanelManager.track("home_archiving")
            startSeatRecordActivity() }
        ivHomeInfo.setOnClickListener { showLevelDescriptionDialog() }
        clHomeScrap.setOnClickListener{
            MixpanelManager.track("home_scrap")
            startScrapActivity()
        }
        clHomeUpload.setOnClickListener {
            MixpanelManager.track("home_upload_view")
            navigateToReviewActivity()
        }
        ivHomeSetting.setOnClickListener { navigateToSettingActivity() }
    }

    private fun initObserver() {
        homeViewModel.stadiums.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    makeSpotImageAppbar("경기장 정보를 불러오는데 실패하였습니다.")
                    stadiumAdapter.submitList(emptyList())
                    setStadiumShimmer(true)
                }

                is UiState.Loading -> {
                    stadiumAdapter.submitList(emptyList())
                    setStadiumShimmer(true)
                }

                is UiState.Success -> {
                    setStadiumShimmer(false)
                    stadiumAdapter.submitList(state.data) {
                        binding.rvHomeStadium.scrollToPosition(0)
                    }
                }
            }
        }

        homeViewModel.homeFeed.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Empty -> Unit
                is UiState.Failure -> {
                    makeSpotImageAppbar("내 정보 불러오기를 실패하였습니다.\uD83E\uDEE2")
                }

                is UiState.Loading -> {
                    setHomeFeedVisibility(false)
                    setHomeFeedShimmer(true)
                }

                is UiState.Success -> {
                    setHomeFeed(state.data)
                    setHomeFeedVisibility(true)
                    setHomeFeedShimmer(false)
                }
            }

        }

    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_secondary)
            setBlackSystemBarIconColor(window)
        }
    }

    private fun setStadiumAdapter() {
        stadiumAdapter = StadiumAdapter(
            searchClick = {
                MixpanelManager.track("home_find_view")
                startStadiumSelectionActivity()
            },
            stadiumClick = {
                if (!it.isActive) {
                    makeSpotImageAppbar("${it.name} 야구장은 곧 업데이트 예정이에요")
                    SpotSnackBar.make(
                        view = binding.root,
                        message = "현재 잠실야구장만 이용할 수 있어요!",
                        endMessage = "잠실야구장 보기",
                        marginBottom = 87,
                    ) {
                        startStadiumActivity(it)
                    }.show()
                } else {
                    MixpanelManager.track("home_find_view")
                    startStadiumActivity(it)
                }
            }
        )
        binding.rvHomeStadium.adapter = stadiumAdapter
        binding.rvHomeStadium.addItemDecoration(
            LinearSpacingItemDecoration(
                START_SPACING_DP.dpToPx(this),
                BETWEEN_SPADING_DP.dpToPx(this)
            )
        )
        binding.rvHomeStadium.itemAnimator = null
    }


    private fun startStadiumSelectionActivity() {
        Intent(this@HomeActivity, StadiumSelectionActivity::class.java).apply {
            startActivity(
                this
            )
        }
    }

    private fun startSeatRecordActivity() {
        Intent(this, SeatRecordActivity::class.java).apply { startActivity(this) }
    }

    private fun startScrapActivity() {
        Intent(this, ScrapActivity::class.java).apply { startActivity(this) }
    }

    private fun startStadiumActivity(stadium: ResponseStadiums) {
        val intent = Intent(
            this@HomeActivity,
            StadiumActivity::class.java
        ).apply {
            if (stadium.id != 1) {
                putExtra(STADIUM_EXTRA_ID, 1)
            } else {
                putExtra(STADIUM_EXTRA_ID, stadium.id)
            }
        }
        startActivity(intent)
    }

    private fun showLevelDescriptionDialog() {
        LevelDescriptionDialog().apply { show(supportFragmentManager, this.tag) }
    }

    private fun setStadiumShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            shimmerHomeStadium.startShimmer()
            shimmerHomeStadium.visibility = View.VISIBLE

        } else {
            shimmerHomeStadium.stopShimmer()
            shimmerHomeStadium.visibility = View.GONE

        }
    }
    private fun setHomeFeedVisibility(isSuccess: Boolean) {
        val visibility = if (isSuccess) View.VISIBLE else View.GONE
        with(binding) {
            csbvHomeTitle.visibility = visibility
            tvHomeLevel.visibility = visibility
            ivHomeInfo.visibility = visibility
        }
    }

    private fun setHomeFeed(data: ResponseHomeFeed) = with(binding) {
        "Lv.${data.level}".also { tvHomeLevel.text = it }
        tvHomeTeam.text = if (data.teamId == null) {
            "모두를 응원하는"
        } else {
            "${data.teamName}의"
        }
        tvHomeTitle.text = data.levelTitle
        ivHomeCharacter.load(data.mascotImageUrl)
        if (data.reviewCntToLevelup == 0) {
            csbvHomeTitle.setTextPart("내가 바로 이 구역 직관왕!", null, null)
        } else {
            csbvHomeTitle.setTextPart("시야 사진 ", data.reviewCntToLevelup, "장 더 올리면 레벨업!")
        }

    }

    private fun navigateToReviewActivity() {
        ReviewTypeDialog().show(supportFragmentManager, "MyDialog")
    }

    private fun setHomeFeedShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            shimmerHomeProfile.startShimmer()
            shimmerHomeProfile.visibility = View.VISIBLE
        } else {
            shimmerHomeProfile.stopShimmer()
            shimmerHomeProfile.visibility = View.GONE
        }
    }

    private fun navigateToSettingActivity() {
        Intent(this, SettingActivity::class.java).apply { startActivity(this) }
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 87
        ).show()
    }

    private fun handleIntentExtra() {
        val navReview =
            intent.getCompatibleParcelableExtra<SchemeState.NavReview>(SchemeKey.NAV_REVIEW)
        if (navReview != null) {
            Intent(this, StadiumDetailActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(SchemeKey.STADIUM_ID, navReview.stadiumId)
                putExtra(SchemeKey.BLOCK_CODE, navReview.blockCode)
            }.let { startActivity(it) }
        }
        navigateToReviewDetail()
    }

    private fun navigateToReviewDetail() {
        val navReviewDetail =
            intent.getCompatibleParcelableExtra<SchemeState.NavReviewDetail>(SchemeKey.NAV_REVIEW_DETAIL)
        if (navReviewDetail != null) {
            Intent(this, StadiumDetailActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(SchemeKey.STADIUM_ID, navReviewDetail.stadiumId)
                putExtra(SchemeKey.BLOCK_CODE, navReviewDetail.blockCode)
                putExtra(SchemeKey.REVIEW_ID, navReviewDetail.reviewId)
                putExtra("IMAGE_UPLOAD", true)
            }.let { startActivity(it) }
        }
    }
}