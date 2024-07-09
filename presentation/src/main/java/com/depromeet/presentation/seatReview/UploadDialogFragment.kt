package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentUploadBottomSheetBinding

class UploadDialogFragment : BindingBottomSheetDialog<FragmentUploadBottomSheetBinding>(
    R.layout.fragment_upload_bottom_sheet,
    FragmentUploadBottomSheetBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
