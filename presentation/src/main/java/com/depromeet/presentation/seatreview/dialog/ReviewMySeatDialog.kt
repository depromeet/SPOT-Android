package com.depromeet.presentation.seatreview.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentReviewMySeatBottomSheetBinding
import com.depromeet.presentation.extension.colorOf
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatreview.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewMySeatDialog : BindingBottomSheetDialog<FragmentReviewMySeatBottomSheetBinding>(
    R.layout.fragment_review_my_seat_bottom_sheet,
    FragmentReviewMySeatBottomSheetBinding::inflate,
) {
    private val viewModel by activityViewModels<ReviewViewModel>()
    private val maxLength = 200
    private val selectedGoodBtn by lazy {
        listOf(
            binding.tvGoodOne,
            binding.tvGoodTwo,
            binding.tvGoodThree,
            binding.tvGoodFour,
            binding.tvGoodFive,
        )
    }
    private val selectedBadBtn by lazy {
        listOf(
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val bottomSheet = (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? View
                bottomSheet?.let { sheet ->
                    BottomSheetBehavior.from(sheet).apply {
                        state = BottomSheetBehavior.STATE_EXPANDED
                        skipCollapsed = true
                        peekHeight = 0
                    }
                }
                window?.findViewById<View>(com.google.android.material.R.id.touch_outside)?.setOnClickListener(null)
                window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.layoutParams = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreDetailReviewText()
        initObserve()
        initEvent()
        initCompleteEvent()
        setupDetailReviewEditText()
    }

    private fun restoreDetailReviewText() {
        val savedText = viewModel.detailReviewText.value
        binding.etDetailReview.setText(savedText)
        if (savedText.isNotEmpty()) {
            with(binding) {
                btnDetailCheck.isSelected = true
                etDetailReview.isVisible = true
                tvTextCount.isVisible = true
                tvTextTotal.isVisible = true
                tvTextCount.text = savedText.length.toString()
            }
        }
    }

    private fun initObserve() {
        viewModel.selectedGoodReview.asLiveData().observe(viewLifecycleOwner) { goodReviews ->
            viewModel.selectedBadReview.asLiveData().observe(viewLifecycleOwner) { badReviews ->
                updateGoodReviewBtnState(goodReviews)
                updateBadReviewBtnState(badReviews)
                updateCompleteBtnState(goodReviews.isNotEmpty() || badReviews.isNotEmpty())
            }
        }
    }

    private fun initEvent() {
        selectedGoodBtn.forEach { button ->
            button.setOnSingleClickListener {
                if (selectedGoodBtn.filter { it.isSelected }.size < 3 || button.isSelected) {
                    button.isSelected = !button.isSelected
                    val selectedGoodButtonText =
                        selectedGoodBtn.filter { it.isSelected }.map { it.text.toString() }
                    val selectedBadButtonText =
                        selectedBadBtn.filter { it.isSelected }.map { it.text.toString() }

                    val totalSelectedCount = selectedGoodButtonText.size + selectedBadButtonText.size
                    viewModel.setReviewCount(totalSelectedCount)
                    viewModel.setSelectedGoodReview(selectedGoodButtonText)
                }
            }
        }

        selectedBadBtn.forEach { button ->
            button.setOnSingleClickListener {
                if (selectedBadBtn.filter { it.isSelected }.size < 3 || button.isSelected) {
                    button.isSelected = !button.isSelected
                    val selectedBadButtonText =
                        selectedBadBtn.filter { it.isSelected }.map { it.text.toString() }
                    val selectedGoodButtonText =
                        selectedGoodBtn.filter { it.isSelected }.map { it.text.toString() }

                    val totalSelectedCount = selectedGoodButtonText.size + selectedBadButtonText.size
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
                etDetailReview.isVisible = btnDetailCheck.isSelected
                tvTextCount.isVisible = btnDetailCheck.isSelected
                tvTextTotal.isVisible = btnDetailCheck.isSelected
            }
            etDetailReview.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            etDetailReview.addTextChangedListener { text: Editable? ->
                text?.let {
                    tvTextCount.text = text.length.toString()
                    tvTextCount.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                    tvTextTotal.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                    if (it.length >= maxLength) {
                        etDetailReview.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                        tvDetailReviewWarning.visibility = VISIBLE
                        ivTextWarning.visibility = VISIBLE
                        tvTextCount.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_error_primary))
                    } else {
                        etDetailReview.setBackgroundResource(R.drawable.rect_gray200_fill_12)
                        tvDetailReviewWarning.visibility = GONE
                        ivTextWarning.visibility = GONE
                    }
                }
                viewModel.setDetailReviewText(text.toString())
            }
        }
    }

    private fun initCompleteEvent() {
        binding.tvCompleteBtn.setOnSingleClickListener {
            if (binding.tvCompleteBtn.isEnabled) {
                dismiss()
            }
        }
    }

    private fun updateGoodReviewBtnState(selectedButtonText: List<String>) {
        selectedGoodBtn.forEach { button ->
            button.isSelected = selectedButtonText.contains(button.text.toString())
        }
    }

    private fun updateBadReviewBtnState(selectedButtonText: List<String>) {
        selectedBadBtn.forEach { button ->
            button.isSelected = selectedButtonText.contains(button.text.toString())
        }
    }

    private fun updateCompleteBtnState(enable: Boolean) {
        with(binding.tvCompleteBtn) {
            isEnabled = enable
            setBackgroundResource(if (isEnabled) R.drawable.rect_action_enabled_fill_8 else R.drawable.rect_action_disabled_fill_8)
        }
    }
}
