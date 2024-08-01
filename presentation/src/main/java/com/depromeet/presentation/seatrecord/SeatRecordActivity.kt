package com.depromeet.presentation.seatrecord

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.designsystem.SpotSpinner
import com.depromeet.designsystem.extension.dpToPx
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.seatReview.ReviewActivity
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.seatrecord.uiMapper.MonthUiData
import com.depromeet.presentation.seatrecord.viewmodel.EditUi
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.depromeet.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SeatRecordActivity : BaseActivity<ActivitySeatRecordBinding>(
    ActivitySeatRecordBinding::inflate
) {
    companion object {
        const val SEAT_RECORD_TAG = "seatRecord"
        private const val START_SPACING_DP = 20
        private const val BETWEEN_SPACING_DP = 8
    }

    private lateinit var dateMonthAdapter: DateMonthAdapter
    private lateinit var monthRecordAdapter: MonthRecordAdapter
    private val viewModel: SeatRecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        initMonthAdapter()
        viewModel.getReviewDate()
    }

    private fun initEvent() {
        with(binding) {
            recordSpotAppbar.setNavigationOnClickListener {
                finish()
            }
            fabRecordUp.setOnClickListener {
                ssvRecord.smoothScrollTo(0, 0)
            }
            fabRecordPlus.setOnClickListener {
                Intent(this@SeatRecordActivity, ReviewActivity::class.java).apply {
                    startActivity(
                        this
                    )
                }
            }
            btRecordFailResfresh.setOnClickListener {
                viewModel.getSeatRecords()
            }
        }
    }

    private fun initObserver() {
        observeDates()
        observeReviews()
        observeEvents()
    }

    private fun observeDates() {
        viewModel.selectedYear.asLiveData().observe(this) {
            viewModel.initMonths()
        }
        viewModel.months.asLiveData().observe(this) {
            dateMonthAdapter.submitList(it)
            viewModel.getSeatRecords()
        }
        viewModel.date.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setReviewsVisibility(isExist = true)
                    val year = state.data.yearMonths.map { it.year }
                    initYearSpinner(year)
                }

                is UiState.Empty -> {
                    setReviewsVisibility(isExist = false)
                }

                is UiState.Loading -> {
                    setReviewsVisibility(isExist = true)
                }

                is UiState.Failure -> {
                    setReviewsVisibility(isExist = true)
                }

            }
        }
    }

    private fun observeReviews() {
        viewModel.reviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setProfile(state.data.profile)
                    setReviewList(state.data.reviews)
                    binding.clHomeFail.visibility = GONE
                    binding.rvRecordMonthDetail.visibility = VISIBLE
                }

                is UiState.Loading -> {
                    toast("로딩중")
                }

                is UiState.Empty -> {
                    toast("오류 발생 빈값")
                }

                is UiState.Failure -> {
                    binding.clHomeFail.visibility = VISIBLE
                    binding.rvRecordMonthDetail.visibility = GONE
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                Timber.d("test seatdetail")
                moveConfirmationDialog()
            }
        }

        viewModel.editClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveEditReview()
            }
        }
    }

    private fun setProfile(data: MySeatRecordResponse.MyProfileResponse) {
        with(binding) {
            //TODO : 여기 서버 API 바뀌면 "{team}의 ~ lV.{level} {title}" 로 바꿔야함
            "Lv.${data.level} ${data.levelTitle}".also { csbvRecordTitle.setText(it) }
            tvRecordNickname.text = data.nickname
            tvRecordCount.text = data.reviewCount.toString()
            ivRecordProfile.load(data.profileImage) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_default_profile)
            }
        }
    }

    private fun initYearSpinner(years: List<Int>) {
        viewModel.setSelectedYear(years[0])
        val yearList = years.map { "${it}년" }

        val adapter = SpotSpinner(
            this,
            R.layout.item_custom_month_spinner_view,
            R.layout.item_custom_month_spinner_dropdown,
            yearList,
            true,
            R.color.gray900
        )
        with(binding.spinnerRecordYear) {
            this.adapter = adapter
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        adapter.setSelectedItemPosition(position)
                        val selectedYear = yearList[position].filter { it.isDigit() }.toInt()
                        viewModel.setSelectedYear(selectedYear)
                        binding.ssvRecord.smoothScrollTo(0, 0)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun initMonthAdapter() {
        dateMonthAdapter = DateMonthAdapter()
        binding.rvRecordMonth.adapter = dateMonthAdapter
        binding.rvRecordMonth.addItemDecoration(
            LinearSpacingItemDecoration(
                START_SPACING_DP.dpToPx(this),
                BETWEEN_SPACING_DP.dpToPx(this)
            )
        )
        dateMonthAdapter.itemMonthClickListener =
            object : DateMonthAdapter.OnItemMonthClickListener {
                override fun onItemMonthClick(item: MonthUiData) {
                    val selectedMonth = item.month
                    viewModel.setSelectedMonth(selectedMonth)
                    binding.ssvRecord.smoothScrollTo(0, 0)
                }
            }
    }

    private fun setReviewsVisibility(isExist: Boolean) {
        if (!isExist) {
            with(binding) {
                "${CalendarUtil.getCurrentYear()}년".also { tvRecordYear.text = it }
                clRecordNone.visibility = VISIBLE
                clRecordStickyHeader.visibility = GONE
                rvRecordMonthDetail.visibility = GONE
            }
        } else {
            with(binding) {
                clRecordNone.visibility = GONE
                clRecordStickyHeader.visibility = VISIBLE
                rvRecordMonthDetail.visibility = VISIBLE

                ssvRecord.header = binding.clRecordStickyHeader
            }
        }
    }

    private fun setReviewList(reviews: List<MySeatRecordResponse.ReviewResponse>) {
        val groupList =
            reviews.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        monthRecordAdapter = MonthRecordAdapter()
        binding.rvRecordMonthDetail.adapter = monthRecordAdapter
        monthRecordAdapter.submitList(groupList)

        monthRecordAdapter.itemRecordClickListener =
            object : MonthRecordAdapter.OnItemRecordClickListener {

                override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {
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
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }

    private fun moveEditReview() {

    }
}