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
import com.depromeet.designsystem.SpotSpinner
import com.depromeet.designsystem.extension.dpToPx
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.mockdata.MonthData
import com.depromeet.presentation.seatrecord.mockdata.ProfileDetailData
import com.depromeet.presentation.seatrecord.mockdata.ReviewMockData
import com.depromeet.presentation.seatrecord.mockdata.groupByMonth
import com.depromeet.presentation.seatrecord.mockdata.monthList
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatRecordActivity : BaseActivity<ActivitySeatRecordBinding>(
    ActivitySeatRecordBinding::inflate
) {
    companion object {
        private const val START_SPACING_DP = 20
        private const val BETWEEN_SPACING_DP = 8
    }

    private lateinit var dateMonthAdapter: DateMonthAdapter
    private lateinit var monthRecordAdapter: MonthRecordAdapter
    private val viewModel: SeatRecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDateSpinner()
        initMonthAdapter()

        viewModel.getSeatRecords()

        viewModel.uiState.asLiveData().observe(this) {
            setProfile(it.profileDetailData)
            initReviewExist(it.reviews)
        }
        viewModel.selectedMonth.asLiveData().observe(this) {
            val updatedMonthList = monthList.map { monthData ->
                monthData.copy(isClicked = monthData.month == it)
            }
            dateMonthAdapter.submitList(updatedMonthList)
        }

        setClickListener()
    }

    private fun setClickListener() {
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
//        val adapter = ArrayAdapter(this, R.layout.item_custom_month_spinner_view, year)
//        adapter.setDropDownViewResource(R.layout.item_custom_month_spinner_dropdown)

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

    private fun initReviewExist(reviews: List<ReviewMockData>) {
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
            monthRecordAdapter.submitList(reviews.groupByMonth())

            monthRecordAdapter.itemRecordClickListener =
                object : MonthRecordAdapter.OnItemRecordClickListener {
                    override fun onItemRecordClick(item: ReviewMockData) {
                        Intent(
                            this@SeatRecordActivity,
                            SeatDetailRecordActivity::class.java
                        ).apply {
                            startActivity(this)
                        }
                    }
                }

        }
    }
}