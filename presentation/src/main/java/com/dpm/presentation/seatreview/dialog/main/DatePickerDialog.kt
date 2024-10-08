package com.dpm.presentation.seatreview.dialog.main

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.CustomDatepickerBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel
import com.dpm.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DatePickerDialog : BindingBottomSheetDialog<CustomDatepickerBinding>(
    R.layout.custom_datepicker,
    CustomDatepickerBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    private val calendarInstance: Calendar by lazy { CalendarUtil.getCurrentCalendar() }

    private var onDateSelected: ((year: Int, month: Int, day: Int) -> Unit)? = null
    private var selectedYear = CalendarUtil.getYear(calendarInstance)
    private var selectedMonth = CalendarUtil.getMonth(calendarInstance)
    private var selectedDay = CalendarUtil.getDay(calendarInstance)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
    }

    private fun initView() {
        with(binding) {
            npYear.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npMonth.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npDay.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            npYear.minValue = 2000
            npYear.maxValue = currentYear
            npYear.value = selectedYear
            npYear.wrapSelectorWheel = false

            npMonth.minValue = 1
            npMonth.maxValue = 12
            npMonth.value = selectedMonth + 1
            npMonth.wrapSelectorWheel = false

            npDay.minValue = 1
            npDay.maxValue = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
            npDay.value = selectedDay
            npDay.wrapSelectorWheel = false

            updateDateValue(npDay.maxValue)
        }
    }

    private fun initEvent() {
        binding.npMonth.setOnValueChangedListener { _, _, newVal ->
            calendarInstance.set(Calendar.MONTH, newVal - 1)
            val maxDay = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)

            if (binding.npDay.value > maxDay) {
                binding.npDay.value = maxDay
            }
            updateDateValue(maxDay)
        }

        binding.tvCancel.setOnSingleClickListener { dismiss() }
        binding.tvDone.setOnSingleClickListener {
            val selectedYear = binding.npYear.value
            val selectedMonth = binding.npMonth.value - 1
            val selectedDay = binding.npDay.value
            val selectedCalendar =
                CalendarUtil.getCalendar(selectedYear, selectedMonth, selectedDay)
            val selectedDate = CalendarUtil.formatCalendarDate(selectedCalendar)
            viewModel.updateSelectedDate(selectedDate)
            onDateSelected?.invoke(selectedYear, selectedMonth, selectedDay)
            dismiss()
        }
    }

    private fun updateDateValue(maxDay: Int = 0) {
        with(binding) {
            npYear.displayedValues = Array(npYear.maxValue - npYear.minValue + 1) { i -> "${i + npYear.minValue}년" }
            npMonth.displayedValues = Array(12) { i -> "${i + 1}월" }
            npDay.displayedValues = Array(maxDay + 1) { i -> "${i + 1}일" }
            binding.npDay.maxValue = maxDay
        }
    }
}
