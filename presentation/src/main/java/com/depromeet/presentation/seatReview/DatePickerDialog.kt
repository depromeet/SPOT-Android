package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentDatePickerBottomSheetBinding
import java.util.Calendar

class DatePickerDialog : BindingBottomSheetDialog<FragmentDatePickerBottomSheetBinding>(
    R.layout.fragment_date_picker_bottom_sheet,
    FragmentDatePickerBottomSheetBinding::inflate,
) {
    var onDateSelected: ((year: Int, month: Int, day: Int) -> Unit)? = null

    companion object {
        private val calendarInstance: Calendar by lazy {
            Calendar.getInstance()
        }
    }

    private var selectedYear: Int = calendarInstance.get(Calendar.YEAR)
    private var selectedMonth: Int = calendarInstance.get(Calendar.MONTH)
    private var selectedDay: Int = calendarInstance.get(Calendar.DAY_OF_MONTH)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dpDatePicker.init(selectedYear, selectedMonth, selectedDay) { _, year, month, day ->
            selectedYear = year
            selectedMonth = month
            selectedDay = day
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDateSelected?.invoke(selectedYear, selectedMonth, selectedDay)
    }
}
