package com.dpm.presentation.seatrecord

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.dpm.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.CustomDatepickerBinding
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class EditDatePickerDialog : BindingBottomSheetDialog<CustomDatepickerBinding>(
    R.layout.custom_datepicker,
    CustomDatepickerBinding::inflate
) {
    private val viewModel : SeatRecordViewModel by activityViewModels()
    private val calendarInstance : Calendar by lazy { CalendarUtil.getCurrentCalendar() }

    private var onDateSelected : ((year : Int, month : Int, day : Int) -> Unit)? = null
    private var selectedYear = CalendarUtil.getYear(calendarInstance)
    private var selectedMonth = CalendarUtil.getMonth(calendarInstance)
    private var selectedDay = CalendarUtil.getDay(calendarInstance)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            npYear.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npMonth.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npDay.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            npYear.minValue = 2000
            npYear.maxValue = currentYear
            npYear.value = CalendarUtil.getYearFromDateFormat(viewModel.editReview.value.date)
            npYear.wrapSelectorWheel = false

            npMonth.minValue = 1
            npMonth.maxValue = 12
            npMonth.value = CalendarUtil.getMonthFromDateFormat(viewModel.editReview.value.date)
            npMonth.wrapSelectorWheel = false

            npDay.minValue = 1
            npDay.maxValue = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
            npDay.value = CalendarUtil.getDayOfMonthFromDateFormat(viewModel.editReview.value.date)
            npDay.wrapSelectorWheel = false

            updateDisplayedValues()

            npMonth.setOnValueChangedListener { _, _, newVal ->
                calendarInstance.set(Calendar.MONTH, newVal - 1)
                npDay.maxValue = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
                updateDisplayedValues()
            }

            tvCancel.setOnClickListener { dismiss() }

            tvDone.setOnClickListener {
                val selectedYear = npYear.value
                val selectedMonth = npMonth.value - 1
                val selectedDay = npDay.value
                val selectedCalendar =
                    CalendarUtil.getCalendar(selectedYear, selectedMonth, selectedDay)
                val selectedDate = CalendarUtil.formatCalendarDate(selectedCalendar)
//                viewModel.updateSelectedDate(selectedDate)
                onDateSelected?.invoke(selectedYear, selectedMonth, selectedDay)
                dismiss()
            }
        }
    }

    private fun updateDisplayedValues() {
        binding.npYear.displayedValues = Array(binding.npYear.maxValue - binding.npYear.minValue + 1) { i -> "${i + binding.npYear.minValue}년" }
        binding.npMonth.displayedValues = Array(12) { i -> "${i + 1}월" }
        binding.npDay.displayedValues = Array(binding.npDay.maxValue) { i -> "${i + 1}일" }
    }

}