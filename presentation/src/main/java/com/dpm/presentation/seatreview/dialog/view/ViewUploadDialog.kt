package com.dpm.presentation.seatreview.dialog.view

import android.os.Bundle
import android.view.View
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentViewUploadDialogBinding
import com.dpm.core.base.BindingDialogFragment

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
    }
}
