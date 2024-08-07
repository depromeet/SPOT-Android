package com.depromeet.presentation.seatrecord.test

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.designsystem.SpotTestSpinner
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.presentation.databinding.ItemRecordDateBinding
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration

class RecordDateViewHolder(
    internal val binding: ItemRecordDateBinding,
    private val yearClick: (Int) -> Unit,
    private val monthClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val START_SPACING_DP = 20
        private const val BETWEEN_SPACING_DP = 8
    }

    private lateinit var dateMonthAdapter: DateMonthAdapter

    fun bind(data: List<ReviewDateResponse.YearMonths>) {
        initMonthAdapter()
        initYearSpinner(data)
        dateMonthAdapter.submitList(data.first { it.isClicked }.months)
    }


    private fun initYearSpinner(data: List<ReviewDateResponse.YearMonths>) {
        val years = data.map { it.year }
        val yearList = years.map { "${it}년" }

        val adapter = SpotTestSpinner(
            yearList,
            selectedTextView = { item ->
                //TODO : 선택되었을 때
            },
            dropDownTextView = { item ->
                //TODO : 드롭다운 메뉴들 로그 확인해보기
            }
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
                        yearClick(selectedYear)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun initMonthAdapter() {
        if (!::dateMonthAdapter.isInitialized) {
            dateMonthAdapter = DateMonthAdapter(
                monthClick = { month ->
                    monthClick(month)
                }
            )
            binding.rvRecordMonth.adapter = dateMonthAdapter
            binding.rvRecordMonth.addItemDecoration(
                LinearSpacingItemDecoration(
                    START_SPACING_DP, //.dpToPx(this),
                    BETWEEN_SPACING_DP, //.dpToPx(this)
                )
            )
        }
    }
}