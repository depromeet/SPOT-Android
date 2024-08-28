package com.dpm.presentation.seatreview.dialog.view

import ReviewData
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentViewUploadDialogBinding
import com.dpm.core.base.BindingDialogFragment
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.scheme.SchemeKey
import toNavReviewDetail

class ViewUploadDialog : BindingDialogFragment<FragmentViewUploadDialogBinding>(
    R.layout.fragment_view_upload_dialog,
    FragmentViewUploadDialogBinding::inflate,
) {

    companion object {
        private const val REVIEW_DATA = "REVIEW_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        val reviewData = arguments?.getParcelable<ReviewData>(REVIEW_DATA)
        binding.btnCancel.setOnSingleClickListener {
            dismiss()
        }
        binding.btnConfirmReview.setOnSingleClickListener {
            if (reviewData != null) {
                val navReviewDetail = reviewData.toNavReviewDetail()
                Intent(requireContext(), HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra(SchemeKey.NAV_REVIEW_DETAIL, navReviewDetail)
                }.let { startActivity(it) }
            }
        }
    }
}
