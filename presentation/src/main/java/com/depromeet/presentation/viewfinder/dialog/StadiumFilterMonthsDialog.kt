package com.depromeet.presentation.viewfinder.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumFilterMonthsDialogBinding
import com.depromeet.presentation.util.Utils
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
    private var month: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Month_NumberPicker)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutSizeRatio(widthPercent = 1f, heightPercent = 0.51f)

        initView()
        initEvent()
    }

    private fun initView() {
        configureMonthPickerSetting()
    }

    private fun initEvent() {
        setOnValueChangedMonthListener()
        setOnClickCancel()
        setOnClickCompleted()
    }

    private fun configureMonthPickerSetting() {
        with(binding.npMonths) {
            minValue = 0
            maxValue = months.size - 1
            value = viewModel.reviewFilter.value.month?.minus(1) ?: 0
            displayedValues = months
            wrapSelectorWheel = false
        }
    }

    private fun setOnValueChangedMonthListener() {
        binding.npMonths.setOnValueChangedListener { picker, oldVal, newVal ->
            month = newVal
        }
    }

    private fun setOnClickCancel() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setOnClickCompleted() {
        binding.tvCompleted.setOnClickListener {
            viewModel.updateMonth(month?.plus(1) ?: viewModel.reviewFilter.value.month ?: 1)
            dismiss()
        }
    }
}