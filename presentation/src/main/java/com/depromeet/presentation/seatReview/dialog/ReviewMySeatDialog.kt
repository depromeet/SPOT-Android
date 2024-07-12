package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentReviewMySeatBottomSheetBinding
import com.depromeet.presentation.extension.colorOf
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatReview.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewMySeatDialog : BindingBottomSheetDialog<FragmentReviewMySeatBottomSheetBinding>(
    R.layout.fragment_review_my_seat_bottom_sheet,
    FragmentReviewMySeatBottomSheetBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    private val maxLength = 150

    private val selectedButton by lazy {
        listOf(
            binding.tvGoodOne,
            binding.tvGoodTwo,
            binding.tvGoodThree,
            binding.tvGoodFour,
            binding.tvGoodFive,
            binding.tvBadOne,
            binding.tvBadTwo,
            binding.tvBadThree,
            binding.tvBadFour,
            binding.tvBadFive,
            binding.tvBadSix,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetHeight(view)
        setupDetailReviewEditText()
        setupReviewButtons()
        setupCompleteButton()
    }

    private fun setupDetailReviewEditText() {
        binding.apply {
            btnDetailCheck.setOnSingleClickListener {
                btnDetailCheck.isSelected = !btnDetailCheck.isSelected
                etDetailReview.isVisible = btnDetailCheck.isSelected
            }
            etDetailReview.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            etDetailReview.addTextChangedListener { text: Editable? ->
                text?.let {
                    if (it.length >= maxLength) {
                        etDetailReview.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                        tvDetailReviewWarning.visibility = View.VISIBLE
                    } else {
                        etDetailReview.setBackgroundResource(R.drawable.rect_gray200_fill_12)
                        tvDetailReviewWarning.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupReviewButtons() {
        selectedButton.forEach { button ->
            button.setOnSingleClickListener {
                button.isSelected = !button.isSelected
                updateButtonState(button.isSelected)
                viewModel.setSelectedButtons(getSelectedButtonTexts())
            }
        }
    }

    private fun updateButtonState(isSelected: Boolean) {
        val textColorRes = if (isSelected) R.color.white else R.color.gray900
        val backgroundColorRes =
            if (isSelected) R.drawable.rect_gray900_fill_6 else R.drawable.rect_gray200_fill_6

        binding.tvCompleteBtn.apply {
            isEnabled = getSelectedButtonTexts().isNotEmpty()
            setBackgroundResource(backgroundColorRes)
            setTextColor(colorOf(textColorRes))
        }
    }

    private fun getSelectedButtonTexts(): List<String> {
        return selectedButton.filter { it.isSelected }.map { it.text.toString() }
    }

    private fun setupCompleteButton() {
        binding.tvCompleteBtn.setOnSingleClickListener {
            if (binding.tvCompleteBtn.isEnabled) {
                viewModel.setDetailReviewText(binding.etDetailReview.text.toString())
                dismiss()
            }
        }
    }

    private fun setBottomSheetHeight(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                view.layoutParams.apply {
                    height = (resources.displayMetrics.heightPixels * 0.8).toInt()
                }
                view.requestLayout()
            }
        })
    }
}
