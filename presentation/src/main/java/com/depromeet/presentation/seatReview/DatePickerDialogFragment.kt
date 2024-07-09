package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentDatePickerBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DatePickerDialogFragment : BindingBottomSheetDialog<FragmentDatePickerBottomSheetBinding>(
    R.layout.fragment_date_picker_bottom_sheet,
    FragmentDatePickerBottomSheetBinding::inflate,
) {
    var onDateSelected: ((year: Int, month: Int, day: Int) -> Unit)? = null
    private var selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var selectedDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.datePicker.init(selectedYear, selectedMonth, selectedDay,) { _, year, month, day ->
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
