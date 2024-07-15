package com.depromeet.presentation.viewfinder.dialog

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentReportDialogBinding
import com.depromeet.presentation.extension.toast

class ReportDialog : BindingBottomSheetDialog<FragmentReportDialogBinding>(
    R.layout.fragment_report_dialog,
    FragmentReportDialogBinding::inflate
) {
    companion object {
        const val TAG = "ReportDialog"

        fun newInstance(): ReportDialog {
            val args = Bundle()

            val fragment = ReportDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvReport.setOnClickListener {
            toast("구글폼 드가자")
        }
    }
}