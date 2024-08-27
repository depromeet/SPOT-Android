package com.dpm.presentation.seatrecord

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotDropDownSpinner
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.domain.entity.response.home.ResponseReviewDate
import com.dpm.domain.entity.response.home.ResponseUserInfo
import com.dpm.presentation.extension.loadAndCircleProfile
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.home.ProfileEditActivity
import com.dpm.presentation.seatrecord.adapter.DateMonthAdapter
import com.dpm.presentation.seatrecord.adapter.MonthRecordAdapter
import com.dpm.presentation.seatrecord.dialog.ConfirmDeleteDialog
import com.dpm.presentation.seatrecord.dialog.RecordEditDialog
import com.dpm.presentation.seatrecord.uiMapper.MonthReviewData
import com.dpm.presentation.seatrecord.viewmodel.EditUi
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.seatreview.dialog.ReviewTypeDialog
import com.dpm.presentation.util.CalendarUtil
import com.dpm.presentation.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SeatRecordActivity : BaseActivity<ActivitySeatRecordBinding>(
    ActivitySeatRecordBinding::inflate
) {
    companion object {
        const val SEAT_RECORD_TAG = "seatRecord"
        const val PROFILE_NAME = "profile_name"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_CHEER_TEAM = "profile_cheer_team"
    }

    private lateinit var seatReviewMonthAdapter: DateMonthAdapter
    private lateinit var intuitiveReviewMonthAdapter: DateMonthAdapter
    private lateinit var monthSeatReviewAdapter: MonthRecordAdapter
    private lateinit var monthIntuitiveReviewAdapter: MonthRecordAdapter
    private lateinit var seatReviewYearAdapter: SpotDropDownSpinner<String>
    private lateinit var intuitiveReviewYearAdapter: SpotDropDownSpinner<String>
    private val viewModel: SeatRecordViewModel by viewModels()
    private var isLoading: Boolean = false


    private val editProfileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val nickname = data?.getStringExtra(ProfileEditActivity.PROFILE_NAME) ?: ""
                val profileImage = data?.getStringExtra(ProfileEditActivity.PROFILE_IMAGE) ?: ""
                val teamId = data?.getIntExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_ID, 0) ?: 0
                val teamName = data?.getStringExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_NAME)

                viewModel.updateProfile(nickname, profileImage, teamId, teamName)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        initViewStatusBar()
        initSeatMonthAdapter()
        initIntuitiveMonthAdapter()
        initReviewList()
        viewModel.getSeatReviewDate()
        viewModel.getProfile()
    }

    private fun initEvent() {
        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }
            fabRecordUp.setOnClickListener {
                ssvRecord.smoothScrollTo(0, 0)
            }
            fabRecordPlus.setOnClickListener {
                navigateToReviewActivity()
            }
            ivRecordEdit.setOnClickListener {
                navigateToProfileEditActivity()
            }
            ivRecordProfile.setOnClickListener {
                navigateToProfileEditActivity()
            }
            btRecordWriteRecord.setOnClickListener {
                navigateToReviewActivity()
            }
            btRecordFailRefresh.setOnSingleClickListener {
                if (viewModel.currentReviewState.value == SeatRecordViewModel.ReviewType.SEAT_REVIEW) {
                    viewModel.getSeatReviewDate()
                } else {
                    viewModel.getIntuitiveReviewDate()
                }
                viewModel.getLocalProfile()
            }
            ivRecordHelpInfo.setOnClickListener {
                csbvHelpInfo.visibility = if (csbvHelpInfo.visibility == GONE) VISIBLE else GONE
            }
            csbvHelpInfo.setOnClickListener {
                csbvHelpInfo.visibility = GONE
            }
            tvSeatView.setOnSingleClickListener {
                tvSeatView.isSelected = true
                tvIntuitiveReview.isSelected = false

                vSeatViewDivider.visibility = VISIBLE
                vIntuitiveReviewDivider.visibility = GONE

                when (viewModel.seatDate.value) {
                    is UiState.Success -> {
                        rvSeatReview.visibility = VISIBLE
                        rvSeatReviewMonth.visibility = VISIBLE
                        spinnerSeatReviewYear.visibility = VISIBLE
                        clRecordFail.visibility = GONE
                        clRecordEmpty.visibility = GONE
                        tvErrorYear.visibility = GONE
                        tvErrorMonth.visibility = GONE
                    }

                    is UiState.Failure -> {
                        rvSeatReview.visibility = GONE
                        rvSeatReviewMonth.visibility = GONE
                        spinnerSeatReviewYear.visibility = GONE
                        clRecordFail.visibility = VISIBLE
                        clRecordEmpty.visibility = GONE
                        tvErrorYear.visibility = VISIBLE
                        tvErrorMonth.visibility = VISIBLE
                    }

                    is UiState.Empty -> {
                        rvSeatReview.visibility = GONE
                        rvSeatReviewMonth.visibility = GONE
                        spinnerSeatReviewYear.visibility = GONE
                        clRecordFail.visibility = GONE
                        clRecordEmpty.visibility = VISIBLE
                        tvErrorYear.visibility = VISIBLE
                        tvErrorMonth.visibility = VISIBLE
                    }

                    else -> {}
                }


                rvIntuitiveReview.visibility = GONE
                rvIntuitiveReviewMonth.visibility = GONE
                spinnerIntuitiveReviewYear.visibility = GONE


                viewModel.setReviewState(SeatRecordViewModel.ReviewType.SEAT_REVIEW)
                if (viewModel.seatDate.value !is UiState.Success) {
                    viewModel.getSeatReviewDate()
                }
            }
            tvIntuitiveReview.setOnSingleClickListener {
                tvSeatView.isSelected = false
                tvIntuitiveReview.isSelected = true

                vSeatViewDivider.visibility = GONE
                vIntuitiveReviewDivider.visibility = VISIBLE

                rvSeatReview.visibility = GONE
                rvSeatReviewMonth.visibility = GONE
                spinnerSeatReviewYear.visibility = GONE

                when (viewModel.intuitiveDate.value) {
                    is UiState.Success -> {
                        rvIntuitiveReview.visibility = VISIBLE
                        rvIntuitiveReviewMonth.visibility = VISIBLE
                        spinnerIntuitiveReviewYear.visibility = VISIBLE
                        clRecordFail.visibility = GONE
                        clRecordEmpty.visibility = GONE
                        tvErrorYear.visibility = GONE
                        tvErrorMonth.visibility = GONE
                    }

                    is UiState.Failure -> {
                        rvIntuitiveReview.visibility = GONE
                        rvIntuitiveReviewMonth.visibility = GONE
                        spinnerIntuitiveReviewYear.visibility = GONE
                        clRecordFail.visibility = VISIBLE
                        clRecordEmpty.visibility = GONE
                        tvErrorYear.visibility = VISIBLE
                        tvErrorMonth.visibility = VISIBLE
                    }

                    is UiState.Empty -> {
                        rvIntuitiveReview.visibility = GONE
                        rvIntuitiveReviewMonth.visibility = GONE
                        spinnerIntuitiveReviewYear.visibility = GONE
                        clRecordFail.visibility = GONE
                        clRecordEmpty.visibility = VISIBLE
                        tvErrorYear.visibility = VISIBLE
                        tvErrorMonth.visibility = VISIBLE
                    }

                    else -> {}
                }

                viewModel.setReviewState(SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW)
                if (viewModel.intuitiveDate.value !is UiState.Success) {
                    viewModel.getIntuitiveReviewDate()
                }
            }
        }
    }

    private fun initObserver() {
        observeDates()
        observeReviews()
        observeProfile()
        observeEvents()
    }

    private fun observeDates() {
        viewModel.seatDate.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setErrorVisibility(SeatRecordErrorType.NONE)
                    setSeatYearSpinner(state.data)
                    seatReviewMonthAdapter.submitList(state.data.yearMonths.first { it.isClicked }.months)
                    viewModel.getSeatReviews()
                    Timber.d("test seat---- > ${state.data.yearMonths}")
                }

                is UiState.Empty -> {
                    setErrorVisibility(SeatRecordErrorType.EMPTY)
                    setShimmer(false)
                }

                is UiState.Loading -> {
                    setShimmer(true)
                }

                is UiState.Failure -> {
                    setErrorVisibility(SeatRecordErrorType.FAIL)

                    binding.rvSeatReview.visibility = GONE
                    setShimmer(false)
                }

            }
        }

        viewModel.intuitiveDate.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setErrorVisibility(SeatRecordErrorType.NONE)
                    setIntuitiveYearSpinner(state.data)
                    intuitiveReviewMonthAdapter.submitList(state.data.yearMonths.first { it.isClicked }.months)
                    viewModel.getIntuitiveReviews()
                    Timber.d("test intuitive ---- > ${state.data.yearMonths}")
                }

                is UiState.Empty -> {
                    setErrorVisibility(SeatRecordErrorType.EMPTY)
                    setShimmer(false)
                }

                is UiState.Loading -> {
                    //TODO : shimmer도 두개 분리할지 고민하기
                }

                is UiState.Failure -> {
                    setErrorVisibility(SeatRecordErrorType.FAIL)

                    binding.rvIntuitiveReview.visibility = GONE
                    setShimmer(false)
                }
            }
        }
    }

    private fun observeReviews() {
        viewModel.seatReviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setShimmer(false)
                    updateSeatReviewList(state.data.reviews)
                    isLoading = false
                    setErrorVisibility(SeatRecordErrorType.NONE)
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                    setShimmer(false)
                    monthSeatReviewAdapter.submitList(emptyList())
                    makeSpotImageAppbar("해당 날짜에 작성한 글이 없습니다.")
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("리뷰를 불러오는데 실패하였습니다.")
                    setShimmer(false)
                    setErrorVisibility(SeatRecordErrorType.FAIL)
                }
            }
        }

        viewModel.intuitiveReviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setShimmer(false)
                    updateIntuitiveReviewList(state.data.reviews)
                    isLoading = false
                    setErrorVisibility(SeatRecordErrorType.NONE)
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                    setShimmer(false)
                    monthSeatReviewAdapter.submitList(emptyList())
                    makeSpotImageAppbar("해당 날짜에 작성한 글이 없습니다.")
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("리뷰를 불러오는데 실패하였습니다.")
                    setShimmer(false)
                    setErrorVisibility(SeatRecordErrorType.FAIL)
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveConfirmationDialog()
                viewModel.cancelDeleteEvent()
            }
        }

        viewModel.editClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveEditReview()
                viewModel.cancelEditEvent()
            }
        }
    }

    private fun observeProfile() {
        viewModel.profile.asLiveData().observe(this) {
            setProfile(it)
        }
    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_tertiary)
            setBlackSystemBarIconColor(window)
        }
    }

    private fun setProfile(data: ResponseUserInfo) {
        with(binding) {
            if (data.teamId != null && data.teamId != 0) {
                csbvRecordTitle.setTextPart(
                    "${data.teamName}의 Lv.", data.level,
                    " ${data.levelTitle}"
                )

            } else {
                csbvRecordTitle.setTextPart(
                    "모두를 응원하는 Lv.", data.level, " ${data.levelTitle}"
                )
            }
            csbvHelpInfo.setText("내 기록이 야구팬들에게 도움된 횟수에요!")
            ivRecordProfile.loadAndCircleProfile(data.profileImage)
            tvRecordNickname.text = data.nickname
            tvRecordCount.text = data.reviewCount.toString()
            tvRecordHelpCount.text = data.totalLikes.toString()
        }
    }

    private fun setSeatYearSpinner(data: ResponseReviewDate) {
        val years = data.yearMonths.map { it.year }
        val yearList = years.map { "${it}년" }
        val selectedYear = data.yearMonths.firstOrNull { it.isClicked }?.year
        val selectedPosition = years.indexOf(selectedYear).takeIf { it >= 0 } ?: 0


        if (!::seatReviewYearAdapter.isInitialized) {
            seatReviewYearAdapter = SpotDropDownSpinner(yearList, selectedPosition)
            binding.spinnerSeatReviewYear.adapter = seatReviewYearAdapter


            binding.spinnerSeatReviewYear.setSelection(selectedPosition)
            binding.spinnerSeatReviewYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        seatReviewYearAdapter.setSelectedItemPosition(position)
                        viewModel.setSeatSelectedYear(years[position])
                        binding.ssvRecord.smoothScrollTo(0, 0)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

        } else {
            seatReviewYearAdapter.updateData(yearList, selectedPosition)
            binding.spinnerSeatReviewYear.setSelection(selectedPosition)
        }
    }

    private fun setIntuitiveYearSpinner(data: ResponseReviewDate) {
        val years = data.yearMonths.map { it.year }
        val yearList = years.map { "${it}년" }
        val selectedYear = data.yearMonths.firstOrNull { it.isClicked }?.year
        val selectedPosition = years.indexOf(selectedYear).takeIf { it >= 0 } ?: 0


        if (!::intuitiveReviewYearAdapter.isInitialized) {
            intuitiveReviewYearAdapter = SpotDropDownSpinner(yearList, selectedPosition)
            binding.spinnerIntuitiveReviewYear.adapter = intuitiveReviewYearAdapter


            binding.spinnerIntuitiveReviewYear.setSelection(selectedPosition)
            binding.spinnerIntuitiveReviewYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        intuitiveReviewYearAdapter.setSelectedItemPosition(position)
                        viewModel.setIntuitiveSelectedYear(years[position])
                        binding.ssvRecord.smoothScrollTo(0, 0)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

        } else {
            intuitiveReviewYearAdapter.updateData(yearList, selectedPosition)
            binding.spinnerIntuitiveReviewYear.setSelection(selectedPosition)
        }
    }

    private fun initSeatMonthAdapter() {
        if (!::seatReviewMonthAdapter.isInitialized) {
            seatReviewMonthAdapter = DateMonthAdapter(
                monthClick = { month ->
                    viewModel.setSeatSelectedMonth(month)
                    binding.ssvRecord.smoothScrollTo(0, 0)
                }
            )
            binding.rvSeatReviewMonth.adapter = seatReviewMonthAdapter
        }
    }

    private fun initIntuitiveMonthAdapter() {
        if (!::intuitiveReviewMonthAdapter.isInitialized) {
            intuitiveReviewMonthAdapter = DateMonthAdapter(
                monthClick = { month ->
                    viewModel.setIntuitiveSelectedMonth(month)
                    binding.ssvRecord.smoothScrollTo(0, 0)
                }
            )
            binding.rvIntuitiveReviewMonth.adapter = intuitiveReviewMonthAdapter
        }
    }

    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) VISIBLE else GONE
    }

    private fun setErrorVisibility(errorType: SeatRecordErrorType) {
        with(binding) {
            //TODO : 아직 시야아카이빙한 게시물이 없습니다 고민하기
            if (tvSeatView.isSelected) {
                rvSeatReview.setVisible(errorType == SeatRecordErrorType.NONE)
                spinnerSeatReviewYear.setVisible(errorType == SeatRecordErrorType.NONE)
                rvSeatReviewMonth.setVisible(errorType == SeatRecordErrorType.NONE)
            }
            if (tvIntuitiveReview.isSelected) {
                rvIntuitiveReview.setVisible(errorType == SeatRecordErrorType.NONE)
                spinnerIntuitiveReviewYear.setVisible(errorType == SeatRecordErrorType.NONE)
                rvIntuitiveReviewMonth.setVisible(errorType == SeatRecordErrorType.NONE)
            }
            tvErrorMonth.setVisible(errorType != SeatRecordErrorType.NONE)
            tvErrorYear.setVisible(errorType != SeatRecordErrorType.NONE)
            "${CalendarUtil.getCurrentYear()}년".also { tvErrorYear.text = it }
            clRecordEmpty.setVisible(errorType == SeatRecordErrorType.EMPTY)
            clRecordFail.setVisible(errorType == SeatRecordErrorType.FAIL)
            if (errorType == SeatRecordErrorType.NONE) ssvRecord.header =
                binding.clRecordStickyHeader
        }
    }

    private fun initReviewList() {
        monthSeatReviewAdapter = MonthRecordAdapter()
        binding.rvSeatReview.adapter = monthSeatReviewAdapter
        binding.rvSeatReview.itemAnimator = null

        monthSeatReviewAdapter.itemRecordClickListener =
            object : MonthRecordAdapter.OnItemRecordClickListener {

                override fun onItemRecordClick(item: ResponseMySeatRecord.ReviewResponse) {
                    viewModel.setClickedReviewId(item.id)
                    supportFragmentManager.commit {
                        replace(
                            R.id.fcv_record,
                            SeatDetailRecordFragment(),
                            SeatDetailRecordFragment.SEAT_RECORD_TAG
                        )
                        addToBackStack(null)
                    }
                }

                override fun onMoreRecordClick(reviewId: Int) {
                    viewModel.setEditReviewId(reviewId)
                    RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                        .apply { show(supportFragmentManager, this.tag) }
                }

                override fun onLikeClick(reviewId: Int) {
                    viewModel.updateLike(reviewId)
                }

                override fun onScrapClick(reviewId: Int) {
                    viewModel.updateScrap(reviewId)
                }
            }

        monthIntuitiveReviewAdapter = MonthRecordAdapter()
        binding.rvIntuitiveReview.adapter = monthIntuitiveReviewAdapter
        binding.rvIntuitiveReview.itemAnimator = null

        monthIntuitiveReviewAdapter.itemRecordClickListener =
            object : MonthRecordAdapter.OnItemRecordClickListener {

                override fun onItemRecordClick(item: ResponseMySeatRecord.ReviewResponse) {
                    viewModel.setClickedReviewId(item.id)
                    supportFragmentManager.commit {
                        replace(
                            R.id.fcv_record,
                            SeatDetailRecordFragment(),
                            SeatDetailRecordFragment.SEAT_RECORD_TAG
                        )
                        addToBackStack(null)
                    }
                }

                override fun onMoreRecordClick(reviewId: Int) {
                    viewModel.setEditReviewId(reviewId)
                    RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                        .apply { show(supportFragmentManager, this.tag) }
                }

                override fun onLikeClick(reviewId: Int) {
                    //TODO : 좋아요 클릭
                }

                override fun onScrapClick(reviewId: Int) {
                    //TODO : 스크랩 클릭
                }
            }


        binding.ssvRecord.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                if (!isLoading) {
                    isLoading = true
                    if (viewModel.currentReviewState.value == SeatRecordViewModel.ReviewType.SEAT_REVIEW
                        && (viewModel.seatReviews.value as? UiState.Success)?.data?.hasNext == true
                    ) {
                        viewModel.getNextSeatReviews()
                    } else if (viewModel.currentReviewState.value == SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW
                        && (viewModel.intuitiveReviews.value as? UiState.Success)?.data?.hasNext == true
                    ) {
                        viewModel.getNextIntuitiveReviews()
                    }
                }
            }
            binding.fabRecordUp.visibility = if (scrollY == 0) GONE else VISIBLE
        })
    }


    private fun updateSeatReviewList(reviews: List<ResponseMySeatRecord.ReviewResponse>) {
        val groupList =
            reviews.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        monthSeatReviewAdapter.submitList(groupList)
    }

    private fun updateIntuitiveReviewList(reviews: List<ResponseMySeatRecord.ReviewResponse>) {
        val groupList =
            reviews.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        monthIntuitiveReviewAdapter.submitList(groupList)
    }

    private fun navigateToProfileEditActivity() {
        editProfileLauncher.launch(Intent(this, ProfileEditActivity::class.java).apply {
            putExtra(PROFILE_NAME, viewModel.profile.value.nickname)
            putExtra(PROFILE_IMAGE, viewModel.profile.value.profileImage)
            putExtra(PROFILE_CHEER_TEAM, viewModel.profile.value.teamId)
        })
    }

    private fun navigateToReviewActivity() {
        ReviewTypeDialog().show(supportFragmentManager, "MyDialog")
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }


    private fun moveEditReview() {

    }

    private fun setShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            shimmerRecord.startShimmer()
            shimmerRecord.visibility = VISIBLE
        } else {
            shimmerRecord.stopShimmer()
            shimmerRecord.visibility = GONE
        }
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 20
        ).show()
    }

    enum class SeatRecordErrorType {
        EMPTY,
        FAIL,
        NONE
    }
}