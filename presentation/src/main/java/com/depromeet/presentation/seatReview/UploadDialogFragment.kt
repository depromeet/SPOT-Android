package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUploadMethodBottomSheetBinding

class UploadDialogFragment : BindingBottomSheetDialog<FragmentUploadMethodBottomSheetBinding>(
    R.layout.fragment_upload_method_bottom_sheet,
    FragmentUploadMethodBottomSheetBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
