package com.dpm.presentation.seatreview.dialog.main

import android.os.Bundle
import android.view.View
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUnsavedDataDialogBinding
import com.dpm.core.base.BindingDialogFragment
import com.dpm.presentation.extension.setOnSingleClickListener

class UnSavedDataDialog : BindingDialogFragment<FragmentUnsavedDataDialogBinding>(
    R.layout.fragment_unsaved_data_dialog,
    FragmentUnsavedDataDialogBinding::inflate,
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
        binding.btnDismiss.setOnSingleClickListener {
            activity?.finish()
        }
    }
}
