package com.dpm.presentation.seatreview.dialog.feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentFeedUploadDialogBinding
import com.dpm.core.base.BindingDialogFragment
import com.dpm.presentation.seatreview.dialog.view.SeatShareDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedUploadDialog : BindingDialogFragment<FragmentFeedUploadDialogBinding>(
    R.layout.fragment_feed_upload_dialog,
    FragmentFeedUploadDialogBinding::inflate,
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
        lifecycleScope.launch {
            delay(2000)
            dismiss()
            navigateToShareDialog()
        }
    }

    private fun navigateToShareDialog() {
        val selectedImageUris = arguments?.getStringArrayList("SELECTED_IMAGES") ?: arrayListOf()
        SeatShareDialog().apply {
            arguments = Bundle().apply {
                putStringArrayList("SELECTED_IMAGES", ArrayList(selectedImageUris))
            }
        }.show(parentFragmentManager, this.tag)
    }
}
