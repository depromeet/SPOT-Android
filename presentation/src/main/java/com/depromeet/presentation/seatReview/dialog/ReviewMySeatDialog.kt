package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
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
    private val viewModel by activityViewModels<ReviewViewModel>()
    private val maxLength = 150
    private val buttons by lazy {
        listOf(binding.tvGoodOne, binding.tvGoodTwo, binding.tvGoodThree, binding.tvGoodFour, binding.tvGoodFive, binding.tvBadOne, binding.tvBadTwo, binding.tvBadThree, binding.tvBadFour, binding.tvBadFive, binding.tvBadSix,)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetHeight(view)
        setupDetailReviewEditText()
        changeReviewButton()
        setupCompleteButton()
    }

    private fun setupDetailReviewEditText() {
        with(binding) {
            btnDetailCheck.setOnSingleClickListener {
                btnDetailCheck.isSelected = !btnDetailCheck.isSelected
                etDetailReview.isVisible = btnDetailCheck.isSelected }
            etDetailReview.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            etDetailReview.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        if (it.length >= maxLength) {
                            etDetailReview.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                            tvDetailReviewWarning.visibility = View.VISIBLE
                        } else {
                            etDetailReview.setBackgroundResource(R.drawable.rect_gray200_fill_12)
                            tvDetailReviewWarning.visibility = View.GONE
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun changeReviewButton() {
        buttons.forEach { button ->
            button.setOnSingleClickListener {
                button.isSelected = !button.isSelected
                button.setTextColor(colorOf(if (button.isSelected) R.color.white else R.color.gray900))
                val selectedButtonTexts = buttons.filter { it.isSelected }.map { it.text.toString() }
                viewModel.setSelectedButtons(selectedButtonTexts)
                with(binding.tvCompleteBtn) {
                    isEnabled = selectedButtonTexts.isNotEmpty()
                    setBackgroundResource(if (isEnabled) R.drawable.rect_gray900_fill_6 else R.drawable.rect_gray200_fill_6)
                    setTextColor(colorOf(if (isEnabled) R.color.white else R.color.gray900))
                }
            }
        }
    }

    private fun setupCompleteButton() {
        binding.tvCompleteBtn.setOnSingleClickListener {
            if (binding.tvCompleteBtn.isEnabled) {
                val detailReviewText = binding.etDetailReview.text.toString()
                viewModel.setDetailReviewText(detailReviewText)
                dismiss()
            }
        }
    }
    private fun setBottomSheetHeight(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                view.layoutParams = view.layoutParams.apply {
                    height = (resources.displayMetrics.heightPixels * 0.8).toInt()
                }
                view.requestLayout()
            }
        })
    }
}
