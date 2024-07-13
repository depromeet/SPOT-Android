package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.depromeet.core.base.BaseActivity
import com.depromeet.designsystem.extension.dpToPx
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.mockdata.MonthData
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

    private lateinit var monthAdapter: DateMonthAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recordSpotAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.recordSpotAppbar.setMenuOnClickListener {
            //셋팅 이동
        }
        binding.ssvRecord.header = binding.clRecordStickyHeader
        binding.fabRecordUp.setOnClickListener {
            binding.ssvRecord.smoothScrollTo(0, 0)
        }

        initDateSpinner()
        setMonthAdapter()

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

    private fun setMonthAdapter() {
        monthAdapter = DateMonthAdapter()
        binding.rvRecordMonth.adapter = monthAdapter
        binding.rvRecordMonth.addItemDecoration(
            LinearSpacingItemDecoration(
                START_SPACING_DP.dpToPx(this),
                BETWEEN_SPACING_DP.dpToPx(this)
            )
        )
        monthAdapter.submitList(monthList)
        monthAdapter.itemMonthClickListener = object : DateMonthAdapter.OnItemMonthClickListener {
            override fun onItemMonthClick(item: MonthData) {
                //뷰모델 처리
            }
        }
    }
}