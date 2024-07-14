package com.depromeet.presentation.viewfinder.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumFilterMonthsDialogBinding
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel

class StadiumFilterMonthsDialog :
    BindingBottomSheetDialog<FragmentStadiumFilterMonthsDialogBinding>(
        R.layout.fragment_stadium_filter_months_dialog,
        FragmentStadiumFilterMonthsDialogBinding::inflate
    ) {
    companion object {
        const val TAG = "StadiumFilterMonthsDialog"

        fun newInstance(): StadiumFilterMonthsDialog {
            val args = Bundle()

            val fragment = StadiumFilterMonthsDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val months = arrayOf<String>(
        "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"
    )

    private val viewModel: StadiumDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetNumberPickerDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutSizeRatio(widthPercent = 1f, heightPercent = 0.44f)

        binding.npMonths.minValue = 0
        binding.npMonths.maxValue = months.size - 1
        binding.npMonths.value = viewModel.month.value
        binding.npMonths.displayedValues = months
        binding.npMonths.wrapSelectorWheel = false

        binding.npMonths.setOnValueChangedListener { picker, oldVal, newVal ->
            viewModel.updateMonth(newVal)
        }
    }

    private fun setLayoutSizeRatio(widthPercent: Float, heightPercent: Float) {
        context?.resources?.displayMetrics?.let { metrics ->
            binding.root.layoutParams.apply {
                width = ((metrics.widthPixels * widthPercent).toInt())
                height = ((metrics.heightPixels * heightPercent).toInt())
            }
        }
    }
}