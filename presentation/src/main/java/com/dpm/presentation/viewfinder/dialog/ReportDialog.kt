package com.dpm.presentation.viewfinder.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dpm.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentReportDialogBinding
import com.dpm.presentation.viewfinder.WebViewActivity

class ReportDialog : BindingBottomSheetDialog<FragmentReportDialogBinding>(
    R.layout.fragment_report_dialog,
    FragmentReportDialogBinding::inflate
) {
    companion object {
        const val TAG = "ReportDialog"
        private const val GoogleReportFormUrl =
            "https://docs.google.com/forms/d/e/1FAIpQLSeVCiFapLIvR3ZdbOE056CImlxohxryUUXISOlYoIodtOwZHQ/viewform"

        fun newInstance(): ReportDialog {
            val args = Bundle()

            val fragment = ReportDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Widget_AppTheme_BottomSheet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutSizeRatio(widthPercent = 1f, heightPercent = 0.2f)
        initEvent()
    }

    private fun initEvent() {
        goToGoogleForm()
    }

    private fun goToGoogleForm() {
        binding.layerReport.setOnClickListener {
            Intent(requireContext(), WebViewActivity::class.java).apply {
                putExtra(WebViewActivity.WEB_VIEW_URL, GoogleReportFormUrl)
            }.let(::startActivity)
        }
    }
}