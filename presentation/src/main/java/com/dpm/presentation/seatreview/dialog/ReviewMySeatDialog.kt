package com.dpm.presentation.seatreview.dialog

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.View.FOCUS_UP
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentReviewMySeatBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.presentation.extension.colorOf
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatreview.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
            binding.tvBadSeven,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }
    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.85).toInt()
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
                } else {
                    makeSpotImageAppbar("후기 키워드는 각각 3개까지 선택할 수 있어요")
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
                } else {
                    makeSpotImageAppbar("후기 키워드는 각각 3개까지 선택할 수 있어요")
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

                if (btnDetailCheck.isSelected) {
                    tvBlank.visibility = VISIBLE
                    svSeatReview.post {
                        svSeatReview.fullScroll(FOCUS_DOWN)
                    }
                } else {
                    tvBlank.visibility = GONE
                    svSeatReview.post {
                        svSeatReview.fullScroll(FOCUS_UP)
                    }
                }
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

    private fun makeSpotImageAppbar(message: String) {
        val parentView = binding.root.rootView
        parentView?.let {
            SpotImageSnackBar.make(
                it,
                message = message,
                messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
                icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
                iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
                marginBottom = 96,
            ).show()
        }
    }
}
