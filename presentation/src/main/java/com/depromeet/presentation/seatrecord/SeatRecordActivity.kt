package com.depromeet.presentation.seatrecord

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
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
import com.depromeet.presentation.extension.extractMonth
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.mockdata.MonthData
import com.depromeet.presentation.seatrecord.mockdata.ProfileDetailData
import com.depromeet.presentation.seatrecord.mockdata.monthList
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SeatRecordActivity : BaseActivity<ActivitySeatRecordBinding>(
    ActivitySeatRecordBinding::inflate
) {
    companion object {
        const val SEAT_RECORD_TAG = "seatRecord"
        const val RECORD_ID = "record_id"
        const val RECORD_LIST = "record_list"
        private const val START_SPACING_DP = 20
        private const val BETWEEN_SPACING_DP = 8
    }

    private lateinit var dateMonthAdapter: DateMonthAdapter
    private lateinit var monthRecordAdapter: MonthRecordAdapter
    private val viewModel: SeatRecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSeatRecords()
        initView()
        initEvent()
        initObserver()


    }

    private fun initView() {
        initDateSpinner()
        initMonthAdapter()
    }

    private fun initEvent() {
        with(binding) {
            recordSpotAppbar.setNavigationOnClickListener {
                finish()
            }
            recordSpotAppbar.setMenuOnClickListener {
                //셋팅 이동
            }
            fabRecordUp.setOnClickListener {
                ssvRecord.smoothScrollTo(0, 0)
            }
        }
    }

    private fun initObserver() {
        viewModel.selectedMonth.asLiveData().observe(this) {
            val updatedMonthList = monthList.map { monthData ->
                monthData.copy(isClicked = monthData.month == it)
            }
            dateMonthAdapter.submitList(updatedMonthList)
        }

        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state) moveConfirmationDialog()
        }
        viewModel.uiState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    /*** setProfile() ->사용자 프로필 데이터 아직 서버 api명세서에 없음 */
                    setReviewList(state.data.reviews)
                }

                is UiState.Loading -> {
                    toast("로딩중")
                }

                is UiState.Empty -> {
                    toast("오류 발생 빈값")
                }

                is UiState.Failure -> {
                    Timber.d("test : ${state.msg}")
                }
            }
        }
    }

    private fun setProfile(profileData: ProfileDetailData) {
        with(binding) {
            "Lv.${profileData.level} ${profileData.titleName}".also { tvRecordLevel.text = it }
            tvRecordNickname.text = profileData.nickName
            tvRecordCount.text = profileData.recordCount.toString()
            ivRecordProfile.load(profileData.profileImage) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initDateSpinner() {
        val year = listOf("2024년", "2023년", "2022년", "2021년")

        val adapter = SpotSpinner(
            this,
            R.layout.item_custom_month_spinner_view,
            R.layout.item_custom_month_spinner_dropdown,
            year,
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
                        val selectedYear = year[position].filter { it.isDigit() }.toInt()
                        viewModel.setSelectedYear(selectedYear)
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
                override fun onItemMonthClick(item: MonthData) {
                    val selectedMonth = item.month
                    viewModel.setSelectedMonth(selectedMonth)
                }
            }
    }

    private fun setReviewList(reviews: List<MySeatRecordResponse.ReviewResponse>) {
        if (reviews.isEmpty()) {
            with(binding) {
                "${viewModel.selectedMonth.value}년".also { tvRecordYear.text = it }
                clRecordNone.visibility = View.VISIBLE
                clRecordStickyHeader.visibility = View.GONE
                rvRecordMonthDetail.visibility = View.GONE
            }
        } else {
            with(binding) {
                clRecordNone.visibility = View.GONE
                clRecordStickyHeader.visibility = View.VISIBLE
                rvRecordMonthDetail.visibility = View.VISIBLE

                monthRecordAdapter = MonthRecordAdapter()
                rvRecordMonthDetail.adapter = monthRecordAdapter
                ssvRecord.header = binding.clRecordStickyHeader
            }
            val groupList =
                reviews.groupBy { it.date.extractMonth(false) }.map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
            monthRecordAdapter.submitList(groupList)

            monthRecordAdapter.itemRecordClickListener =
                object : MonthRecordAdapter.OnItemRecordClickListener {

                    override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {
                        Intent(
                            this@SeatRecordActivity,
                            SeatDetailRecordActivity::class.java
                        ).apply {
                            putExtra(RECORD_ID, item.id)
                            putParcelableArrayListExtra(RECORD_LIST, viewModel.getUiReviewsData())
                        }.let(::startActivity)
                    }

                    override fun onMoreRecordClick(reviewId: Int) {
                        viewModel.setEditReviewId(reviewId)
                        RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                            .apply { show(supportFragmentManager, this.tag) }
                    }

                }

        }
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }
}