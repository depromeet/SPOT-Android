package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
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
    private val selectedGoodBtn by lazy { listOf(binding.tvGoodOne, binding.tvGoodTwo, binding.tvGoodThree, binding.tvGoodFour, binding.tvGoodFive) }
    private val selectedBadBtn by lazy { listOf(binding.tvBadOne, binding.tvBadTwo, binding.tvBadThree, binding.tvBadFour, binding.tvBadFive, binding.tvBadSix) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetHeight(view)
        observeReviewViewModel()

        setupReviewBtnClickListeners()
        setupDetailReviewEditText()
        setupCompleteButton()
    }

    private fun observeReviewViewModel() {
        viewModel.selectedGoodReview.asLiveData().observe(viewLifecycleOwner) { goodReviews ->
            viewModel.selectedBadReview.asLiveData().observe(viewLifecycleOwner) { badReviews ->
                updateGoodReviewBtnState(goodReviews)
                updateBadReviewBtnState(badReviews)
                updateCompleteBtnState(goodReviews.isNotEmpty() || badReviews.isNotEmpty())
            }
        }
    }

    private fun setupReviewBtnClickListeners() {
        selectedGoodBtn.forEach { button ->
            button.setOnSingleClickListener {
                if (selectedGoodBtn.filter { it.isSelected }.size < 3 || button.isSelected) {
                    button.isSelected = !button.isSelected
                    button.setTextColor(colorOf(if (button.isSelected) R.color.white else R.color.gray900))

                    val selectedGoodButtonText =
                        selectedGoodBtn.filter { it.isSelected }.map { it.text.toString() }
                    val selectedBadButtonText =
                        selectedBadBtn.filter { it.isSelected }.map { it.text.toString() }

                    val totalSelectedCount =
                        selectedGoodButtonText.size + selectedBadButtonText.size
                    viewModel.setReviewCount(totalSelectedCount)
                    viewModel.setSelectedGoodReview(selectedGoodButtonText)
                }
            }
        }

        selectedBadBtn.forEach { button ->
            button.setOnSingleClickListener {
                if (selectedBadBtn.filter { it.isSelected }.size < 3 || button.isSelected) {
                    button.isSelected = !button.isSelected
                    button.setTextColor(colorOf(if (button.isSelected) R.color.white else R.color.gray900))

                    val selectedBadButtonText =
                        selectedBadBtn.filter { it.isSelected }.map { it.text.toString() }
                    val selectedGoodButtonText =
                        selectedGoodBtn.filter { it.isSelected }.map { it.text.toString() }

                    val totalSelectedCount =
                        selectedGoodButtonText.size + selectedBadButtonText.size
                    viewModel.setReviewCount(totalSelectedCount)
                    viewModel.setSelectedBadReview(selectedBadButtonText)
                }
            }
        }
    }

    private fun setupDetailReviewEditText() {
        with(binding) {
            btnDetailCheck.setOnSingleClickListener {
                btnDetailCheck.isSelected = !btnDetailCheck.isSelected
                etDetailView.isVisible = btnDetailCheck.isSelected
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
                viewModel.setDetailReviewText(text.toString())
            }
        }
    }

    private fun setupCompleteButton() {
        binding.tvCompleteBtn.setOnSingleClickListener {
            if (binding.tvCompleteBtn.isEnabled) {
                dismiss()
            }
        }
    }

    private fun updateGoodReviewBtnState(selectedButtonText: List<String>) {
        selectedGoodBtn.forEach { button ->
            button.isSelected = selectedButtonText.contains(button.text.toString())
            button.setTextColor(colorOf(if (button.isSelected) R.color.white else R.color.gray900))
        }
    }

    private fun updateBadReviewBtnState(selectedButtonText: List<String>) {
        selectedBadBtn.forEach { button ->
            button.isSelected = selectedButtonText.contains(button.text.toString())
            button.setTextColor(colorOf(if (button.isSelected) R.color.white else R.color.gray900))
        }
    }

    private fun updateCompleteBtnState(enable: Boolean) {
        with(binding.tvCompleteBtn) {
            isEnabled = enable
            setBackgroundResource(if (isEnabled) R.drawable.rect_gray900_fill_6 else R.drawable.rect_gray200_fill_6)
            setTextColor(colorOf(if (isEnabled) R.color.white else R.color.gray900))
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
