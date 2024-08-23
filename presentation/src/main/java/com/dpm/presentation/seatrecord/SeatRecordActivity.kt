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
import com.dpm.presentation.seatreview.ReviewActivity
import com.dpm.presentation.util.CalendarUtil
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

    private lateinit var dateMonthAdapter: DateMonthAdapter
    private lateinit var monthRecordAdapter: MonthRecordAdapter
    private lateinit var yearAdapter: SpotDropDownSpinner<String>
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
        initMonthAdapter()
        initReviewList()
        viewModel.getReviewDate()
        viewModel.getLocalProfile()
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
                if (viewModel.reviews.value is UiState.Failure || viewModel.date.value is UiState.Failure) {
                    makeSpotImageAppbar("리뷰를 불러오는데 실패하였습니다.")
                }
                viewModel.getReviewDate()
            }
            ivRecordHelpInfo.setOnClickListener {
                csbvHelpInfo.visibility = if (csbvHelpInfo.visibility == GONE) VISIBLE else GONE
            }
            csbvHelpInfo.setOnClickListener {
                csbvHelpInfo.visibility = GONE
            }
            tvSeatView.setOnSingleClickListener {
                vSeatViewDivider.visibility = VISIBLE
                vIntuitiveReviewDivider.visibility = GONE
            }
            tvIntuitiveReview.setOnSingleClickListener {
                vSeatViewDivider.visibility = GONE
                vIntuitiveReviewDivider.visibility = VISIBLE
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
        viewModel.date.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setErrorVisibility(SeatRecordErrorType.NONE)
                    setYearSpinner(state.data)
                    dateMonthAdapter.submitList(state.data.yearMonths.first { it.isClicked }.months)
                    viewModel.getSeatRecords()
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

                    binding.rvRecordMonthDetail.visibility = GONE
                    setShimmer(false)
                }

            }
        }
    }

    private fun observeReviews() {
        viewModel.reviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setShimmer(false)
                    setProfile(state.data.profile)
                    updateReviewList(state.data.reviews)
                    isLoading = false
                    setErrorVisibility(SeatRecordErrorType.NONE)
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                    setShimmer(false)
                    monthRecordAdapter.submitList(emptyList())
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

    private fun setProfile(data: ResponseMySeatRecord.MyProfileResponse) {
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
            tvRecordCount.text = "0"
            tvRecordCount.text = data.reviewCount.toString()
        }
    }

    private fun setYearSpinner(data: ResponseReviewDate) {
        val years = data.yearMonths.map { it.year }
        val yearList = years.map { "${it}년" }
        val selectedYear = data.yearMonths.firstOrNull { it.isClicked }?.year
        val selectedPosition = years.indexOf(selectedYear).takeIf { it >= 0 } ?: 0


        if (!::yearAdapter.isInitialized) {
            yearAdapter = SpotDropDownSpinner(yearList, selectedPosition)
            binding.spinnerRecordYear.adapter = yearAdapter


            binding.spinnerRecordYear.setSelection(selectedPosition)
            binding.spinnerRecordYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        yearAdapter.setSelectedItemPosition(position)
                        viewModel.setSelectedYear(years[position])
                        binding.ssvRecord.smoothScrollTo(0, 0)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

        } else {
            yearAdapter.updateData(yearList, selectedPosition)
            binding.spinnerRecordYear.setSelection(selectedPosition)
        }
    }

    private fun initMonthAdapter() {
        if (!::dateMonthAdapter.isInitialized) {
            dateMonthAdapter = DateMonthAdapter(
                monthClick = { month ->
                    viewModel.setSelectedMonth(month)
                    binding.ssvRecord.smoothScrollTo(0, 0)
                }
            )
            binding.rvRecordMonth.adapter = dateMonthAdapter
        }
    }

    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) VISIBLE else GONE
    }

    private fun setErrorVisibility(errorType: SeatRecordErrorType) {
        with(binding) {
            rvRecordMonthDetail.setVisible(errorType == SeatRecordErrorType.NONE)
            tvErrorMonth.setVisible(errorType != SeatRecordErrorType.NONE)
            tvErrorYear.setVisible(errorType != SeatRecordErrorType.NONE)
            "${CalendarUtil.getCurrentYear()}년".also { tvErrorYear.text = it }
            clRecordEmpty.setVisible(errorType == SeatRecordErrorType.EMPTY)
            clRecordFail.setVisible(errorType == SeatRecordErrorType.FAIL)
            fabRecordUp.visibility = GONE
            if (errorType == SeatRecordErrorType.NONE) ssvRecord.header =
                binding.clRecordStickyHeader
        }
    }

    private fun initReviewList() {
        monthRecordAdapter = MonthRecordAdapter()
        binding.rvRecordMonthDetail.adapter = monthRecordAdapter
        binding.rvRecordMonthDetail.itemAnimator = null

        monthRecordAdapter.itemRecordClickListener =
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

            }


        //TODO 이렇게 무한스크롤 하면 되네..
        binding.ssvRecord.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                Timber.d("test 끝 도달")
                val hasNextPage =
                    (viewModel.reviews.value as? UiState.Success)?.data?.hasNext == true
                if (hasNextPage && !isLoading) {
                    isLoading = true
                    viewModel.loadNextSeatRecords()
                }
            }
            binding.fabRecordUp.visibility = if (scrollY == 0) GONE else VISIBLE
        })
    }


    private fun updateReviewList(reviews: List<ResponseMySeatRecord.ReviewResponse>) {
        val groupList =
            reviews.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        monthRecordAdapter.submitList(groupList)
    }

    private fun navigateToProfileEditActivity() {
        val currentState = viewModel.reviews.value
        if (currentState is UiState.Success) {
            editProfileLauncher.launch(Intent(this, ProfileEditActivity::class.java).apply {
                with(currentState.data) {
                    putExtra(PROFILE_NAME, this.profile.nickname)
                    putExtra(PROFILE_IMAGE, this.profile.profileImage)
                    putExtra(PROFILE_CHEER_TEAM, this.profile.teamId)
                }
            })
        }
    }

    private fun navigateToReviewActivity() {
        Intent(this@SeatRecordActivity, ReviewActivity::class.java).apply {
            startActivity(
                this
            )
        }
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