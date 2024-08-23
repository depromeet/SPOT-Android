package com.dpm.presentation.seatreview.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectReviewMethodDialogBinding
import com.dpm.core.base.BindingDialogFragment
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatreview.ReviewActivity
import com.dpm.domain.model.seatreview.ReviewMethod
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewMethodDialog : BindingDialogFragment<FragmentSelectReviewMethodDialogBinding>(
    R.layout.fragment_select_review_method_dialog,
    FragmentSelectReviewMethodDialogBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }
    private fun initEvent() {
        binding.clUploadView.setOnSingleClickListener {
            viewModel.setReviewMethod(ReviewMethod.VIEW)
            navigateToReviewActivity()
        }
        binding.clUploadFeed.setOnSingleClickListener {
            viewModel.setReviewMethod(ReviewMethod.FEED)
            navigateToReviewActivity()
        }
    }

    private fun navigateToReviewActivity() {
        startActivity(
            Intent(requireContext(), ReviewActivity::class.java).apply {
                putExtra("METHOD_KEY", viewModel.reviewMethod.value?.name)
            },
        )
    }
}
