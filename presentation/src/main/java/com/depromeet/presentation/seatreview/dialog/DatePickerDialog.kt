package com.depromeet.presentation.seatreview.dialog

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.CustomDatepickerBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatreview.ReviewViewModel
import com.depromeet.presentation.util.CalendarUtil
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

            updateDateValue()

            npMonth.setOnValueChangedListener { _, _, newVal ->
                calendarInstance.set(Calendar.MONTH, newVal - 1)
                npDay.maxValue = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
                updateDateValue()
            }
        }
    }

    private fun initEvent() {
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

    private fun updateDateValue() {
        with(binding) {
            npYear.displayedValues = Array(npYear.maxValue - npYear.minValue + 1) { i -> "${i + npYear.minValue}년" }
            npMonth.displayedValues = Array(12) { i -> "${i + 1}월" }
            npDay.displayedValues = Array(npDay.maxValue) { i -> "${i + 1}일" }
        }
    }
}
