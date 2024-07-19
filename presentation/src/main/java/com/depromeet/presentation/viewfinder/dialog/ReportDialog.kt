package com.depromeet.presentation.viewfinder.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentReportDialogBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.viewfinder.WebViewActivity

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
        initEvent()
    }

    private fun initEvent() {
        binding.tvReport.setOnClickListener {
            Intent(requireContext(), WebViewActivity::class.java).apply {
                putExtra(WebViewActivity.WEB_VIEW_URL, "https://www.naver.com/")
            }.let(::startActivity)
        }
    }
}