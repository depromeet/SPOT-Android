package com.dpm.presentation.seatreview.dialog.feed

import android.os.Bundle
import android.view.View
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentFeedUploadDialogBinding
import com.dpm.core.base.BindingDialogFragment

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
    }
}
