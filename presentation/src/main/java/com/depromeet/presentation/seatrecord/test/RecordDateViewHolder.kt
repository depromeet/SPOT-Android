package com.depromeet.presentation.seatrecord.test

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.designsystem.SpotDropDownSpinner
import com.depromeet.domain.entity.response.home.ResponseReviewDate
import com.depromeet.presentation.databinding.ItemRecordDateBinding
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import timber.log.Timber

class RecordDateViewHolder(
    internal val binding: ItemRecordDateBinding,
    private val yearClick: (Int) -> Unit,
    private val monthClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var dateMonthAdapter: DateMonthAdapter
    private lateinit var yearAdapter: SpotDropDownSpinner<String>
    private var isYearInitialized : Boolean = false
    private var isSettingSelectedItem = false


    fun bind(data: List<ResponseReviewDate.YearMonths>) {
        initMonthAdapter()
        setYearSpinner(data)
        val clickedYearMonths = data.firstOrNull { it.isClicked }?.months

        if (clickedYearMonths != null) {
            dateMonthAdapter.submitList(clickedYearMonths)
        } else {
            dateMonthAdapter.submitList(emptyList())
        }
    }

    private fun setYearSpinner(data: List<ResponseReviewDate.YearMonths>) {
        val years = data.map { it.year }
        val yearList = years.map { "${it}년" }
        val selectedYear = data.firstOrNull { it.isClicked }?.year
        val selectedPosition = years.indexOf(selectedYear).takeIf { it >= 0 } ?: 0

        if (!isYearInitialized) {
            Timber.d("------------\ntest -> 초기화 안됨!!!")
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
                        if (isSettingSelectedItem) {
                            isSettingSelectedItem = false
                            return
                        }
                        yearAdapter.setSelectedItemPosition(position)
                        val selectedYear = years[position]
                        yearClick(selectedYear)

                        Timber.d("test 테스트 포지션 : $position  //// $selectedYear")
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

            isSettingSelectedItem = true
        }
        isYearInitialized = true
    }


    private fun initMonthAdapter() {
        if (!::dateMonthAdapter.isInitialized) {
            dateMonthAdapter = DateMonthAdapter(
                monthClick = { month ->
                    monthClick(month)
                }
            )
            binding.rvRecordMonth.adapter = dateMonthAdapter
        }
    }
}