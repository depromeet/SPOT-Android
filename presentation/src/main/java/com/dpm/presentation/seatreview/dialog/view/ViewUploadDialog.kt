package com.dpm.presentation.seatreview.dialog.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentViewUploadDialogBinding
import com.dpm.core.base.BindingDialogFragment
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.viewfinder.StadiumDetailActivity

class ViewUploadDialog : BindingDialogFragment<FragmentViewUploadDialogBinding>(
    R.layout.fragment_view_upload_dialog,
    FragmentViewUploadDialogBinding::inflate,
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentMaterialDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        binding.btnCancel.setOnSingleClickListener {
            dismiss()
        }
        binding.btnConfirmReview.setOnSingleClickListener {
            // TODO : 방금 작성한 시야 후기 상세페이지 게시물 화면으로 이동
            startActivity(Intent(requireContext(), StadiumDetailActivity::class.java))
        }
    }
}
