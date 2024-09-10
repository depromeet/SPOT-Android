package com.dpm.presentation.seatrecord.dialog

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.CustomDatepickerBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class EditDatePickerDialog : BindingBottomSheetDialog<CustomDatepickerBinding>(
    R.layout.custom_datepicker,
    CustomDatepickerBinding::inflate
) {
    companion object {
        val TAG = "editDatePickerDialog"
    }

    private val viewModel: SeatRecordViewModel by activityViewModels()
    private val calendarInstance: Calendar by lazy { CalendarUtil.getCurrentCalendar() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
    }

    private fun initView() = with(binding) {
        val date = viewModel.editReview.value.date

        npYear.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        npMonth.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        npDay.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        npYear.minValue = 2000
        npYear.maxValue = currentYear
        npYear.value = CalendarUtil.getYearFromDateFormat(date)
        npYear.wrapSelectorWheel = false

        npMonth.minValue = 1
        npMonth.maxValue = 12
        npMonth.value = CalendarUtil.getMonthFromDateFormat(date)
        npMonth.wrapSelectorWheel = false

        calendarInstance.set(Calendar.MONTH, CalendarUtil.getMonthFromDateFormat(date) - 1)
        calendarInstance.set(Calendar.YEAR, CalendarUtil.getYearFromDateFormat(date))

        npDay.minValue = 1
        npDay.maxValue = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
        npDay.value = CalendarUtil.getDayOfMonthFromDateFormat(date)
        npDay.wrapSelectorWheel = false

        updateDisplayedValues(npDay.maxValue)

    }

    private fun initEvent() = with(binding) {
        npMonth.setOnValueChangedListener { _, _, newVal ->
            calendarInstance.set(Calendar.MONTH, newVal - 1)
            val maxDay = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)

            if (npDay.value > maxDay) {
                npDay.value = maxDay
            }
            updateDisplayedValues(maxDay)

        }

        tvCancel.setOnClickListener { dismiss() }

        tvDone.setOnClickListener {
            val selectedYear = npYear.value
            val selectedMonth = npMonth.value - 1
            val selectedDay = npDay.value
            val selectedCalendar =
                CalendarUtil.getCalendar(selectedYear, selectedMonth, selectedDay)
            val selectedDate = CalendarUtil.formatCalendarDate(selectedCalendar)
            viewModel.updateEditSelectedDate(selectedDate)
            dismiss()
        }
    }

    private fun updateDisplayedValues(maxDay: Int = 0) {
        binding.npYear.displayedValues =
            Array(binding.npYear.maxValue - binding.npYear.minValue + 1) { i -> "${i + binding.npYear.minValue}년" }
        binding.npMonth.displayedValues = Array(12) { i -> "${i + 1}월" }
        binding.npDay.displayedValues = Array(maxDay + 1) { i -> "${i + 1}일" }
        binding.npDay.maxValue = maxDay
    }

}