package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentDatePickerBottomSheetBinding
import com.depromeet.presentation.seatReview.ReviewViewModel
import com.depromeet.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DatePickerDialog : BindingBottomSheetDialog<FragmentDatePickerBottomSheetBinding>(
    R.layout.fragment_date_picker_bottom_sheet,
    FragmentDatePickerBottomSheetBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    var onDateSelected: ((year: Int, month: Int, day: Int) -> Unit)? = null

    private val calendarInstance: Calendar by lazy { CalendarUtil.getCurrentCalendar() }

    private var selectedYear = CalendarUtil.getYear(calendarInstance)
    private var selectedMonth = CalendarUtil.getMonth(calendarInstance)
    private var selectedDay = CalendarUtil.getDay(calendarInstance)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)

        with(binding.dpDatePicker) {
            init(selectedYear, selectedMonth, selectedDay) { _, year, month, day ->
                selectedYear = year
                selectedMonth = month
                selectedDay = day
                val selectedCalendar = CalendarUtil.getCalendar(year, month, day)
                val selectedDate = CalendarUtil.formatCalendarDate(selectedCalendar)
                viewModel.updateSelectedDate(selectedDate)
                onDateSelected?.invoke(year, month, day)
            }
        }
    }
}
