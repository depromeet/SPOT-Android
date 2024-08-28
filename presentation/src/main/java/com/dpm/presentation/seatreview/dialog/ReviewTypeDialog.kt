package com.dpm.presentation.seatreview.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectReviewTypeDialogBinding
import com.dpm.core.base.BindingDialogFragment
import com.dpm.domain.model.seatreview.ReviewMethod
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatreview.ReviewActivity
import com.dpm.presentation.seatreview.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewTypeDialog : BindingDialogFragment<FragmentSelectReviewTypeDialogBinding>(
    R.layout.fragment_select_review_type_dialog,
    FragmentSelectReviewTypeDialogBinding::inflate,
) {
    companion object {
        private const val METHOD_KEY = "METHOD_KEY"
    }
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
            binding.clUploadView.setBackgroundResource(R.drawable.rect_background_positive_fill_secondary_line_12)
            binding.clUploadFeed.setBackgroundResource(R.drawable.rect_background_secondary_fill_12)
            viewModel.setReviewMethod(ReviewMethod.VIEW)
            navigateToReviewActivity()
        }
        binding.clUploadFeed.setOnSingleClickListener {
            binding.clUploadView.setBackgroundResource(R.drawable.rect_background_secondary_fill_12)
            binding.clUploadFeed.setBackgroundResource(R.drawable.rect_background_positive_fill_secondary_line_12)
            viewModel.setReviewMethod(ReviewMethod.FEED)
            navigateToReviewActivity()
        }
    }

    private fun navigateToReviewActivity() {
        lifecycleScope.launch {
            delay(500)
            startActivity(
                Intent(requireContext(), ReviewActivity::class.java).apply {
                    putExtra(METHOD_KEY, viewModel.reviewMethod.value?.name)
                },
            )
            dismiss()
        }
    }
}
