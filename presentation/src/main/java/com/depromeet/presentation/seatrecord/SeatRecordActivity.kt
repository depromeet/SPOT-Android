package com.depromeet.presentation.seatrecord

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.designsystem.extension.dpToPx
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.mockdata.MonthData
import com.depromeet.presentation.seatrecord.mockdata.ReviewMockData
import com.depromeet.presentation.seatrecord.mockdata.groupByMonth
import com.depromeet.presentation.seatrecord.mockdata.makeSeatRecordData
import com.depromeet.presentation.seatrecord.mockdata.monthList
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

    private val testData = makeSeatRecordData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(binding) {
            recordSpotAppbar.setNavigationOnClickListener {
                finish()
            }

            recordSpotAppbar.setMenuOnClickListener {
                //셋팅 이동
            }

            ssvRecord.header = binding.clRecordStickyHeader

            fabRecordUp.setOnClickListener {
                ssvRecord.smoothScrollTo(0, 0)
            }
        }


        setProfile()
        initDateSpinner()
        initMonthAdapter()
        initRecentRecordAdapter()

    }

    private fun setProfile() {
        with(binding) {
            val data = testData.profileDetailData
            "Lv.${data.level} ${data.titleName}".also { tvRecordLevel.text = it }
            tvRecordNickname.text = data.nickName
            tvRecordCount.text = data.recordCount.toString()
            ivRecordProfile.load(data.profileImage) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initDateSpinner() {
        val year = listOf("2024년", "2023년", "2022년", "2021년")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, year)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                        val selectedYear = year[position]
                        //뷰모델 갱신
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
        dateMonthAdapter.submitList(monthList)
        dateMonthAdapter.itemMonthClickListener =
            object : DateMonthAdapter.OnItemMonthClickListener {
                override fun onItemMonthClick(item: MonthData) {
                    //뷰모델 처리
                }
            }
    }

    private fun initRecentRecordAdapter() {
        monthRecordAdapter = MonthRecordAdapter()

        with(binding) {
            rvRecordMonthDetail.adapter = monthRecordAdapter
        }
        monthRecordAdapter.submitList(testData.reviews.groupByMonth())

        monthRecordAdapter.itemRecordClickListener =
            object : MonthRecordAdapter.OnItemRecordClickListener {
                override fun onItemRecordClick(item: ReviewMockData) {
                    Intent(this@SeatRecordActivity, SeatDetailRecordActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }


    }
}