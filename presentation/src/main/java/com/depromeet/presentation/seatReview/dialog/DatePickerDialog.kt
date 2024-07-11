package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentDatePickerBottomSheetBinding
import com.depromeet.presentation.seatReview.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DatePickerDialog : BindingBottomSheetDialog<FragmentDatePickerBottomSheetBinding>(
    R.layout.fragment_date_picker_bottom_sheet,
    FragmentDatePickerBottomSheetBinding::inflate,
) {
    var onDateSelected: ((year: Int, month: Int, day: Int) -> Unit)? = null
    private val viewModel by activityViewModels<ReviewViewModel>()

    companion object {
        private val calendarInstance: Calendar by lazy {
            Calendar.getInstance()
        }
        private const val DATE_FORMAT = "yyyy.MM.dd"
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
            val selectedDate = formatDate(year, month, day)
            viewModel.updateSelectedDate(selectedDate)
            onDateSelected?.invoke(year, month, day)
        }
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
