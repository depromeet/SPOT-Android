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
    FragmentDatePickerBottomSheetBinding::inflate
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    var onDateSelected: ((year: Int, month: Int, day: Int) -> Unit)? = null

    companion object {
        private val calendarInstance: Calendar by lazy { Calendar.getInstance() }
        private const val DATE_FORMAT = "yyyy.MM.dd"
    }

    private var selectedYear = calendarInstance.get(Calendar.YEAR)
    private var selectedMonth = calendarInstance.get(Calendar.MONTH)
    private var selectedDay = calendarInstance.get(Calendar.DAY_OF_MONTH)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)

        with(binding.dpDatePicker) {
            init(selectedYear, selectedMonth, selectedDay) { _, year, month, day ->
                selectedYear = year
                selectedMonth = month
                selectedDay = day
                val selectedDate = formatUserDate(year, month, day)
                viewModel.updateSelectedDate(selectedDate)
                onDateSelected?.invoke(year, month, day)
            }
        }
    }

    private fun formatUserDate(year: Int, month: Int, day: Int): String {
        return Calendar.getInstance().apply {
            set(year, month, day)
        }.let { calendar ->
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.time)
        }
    }
}
