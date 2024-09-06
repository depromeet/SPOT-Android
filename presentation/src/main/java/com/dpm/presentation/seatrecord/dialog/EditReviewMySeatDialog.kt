package com.dpm.presentation.seatrecord.dialog

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.View
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
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditReviewMySeatDialog : BindingBottomSheetDialog<FragmentReviewMySeatBottomSheetBinding>(
    R.layout.fragment_review_my_seat_bottom_sheet,
    FragmentReviewMySeatBottomSheetBinding::inflate,
) {
    private val viewModel by activityViewModels<SeatRecordViewModel>()
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
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.85).toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        initObserve()
        initCompleteEvent()
        setupDetailReviewEditText()
        initMethodNaming()
    }

    private fun initView() {
        val goodReviewKeywords = mutableListOf<String>()
        val badReviewKeywords = mutableListOf<String>()

        viewModel.editReview.value.keywords.forEach { keyword ->
            if (keyword.isPositive) {
                goodReviewKeywords.add(keyword.content)
            } else {
                badReviewKeywords.add(keyword.content)
            }

            selectedGoodBtn.forEach { button ->
                if (button.text.toString() == keyword.content) {
                    button.isSelected = true
                }
            }

            selectedBadBtn.forEach { button ->
                if (button.text.toString() == keyword.content) {
                    button.isSelected = true
                }
            }
        }

        viewModel.setSelectedGoodReview(goodReviewKeywords)
        viewModel.setSelectedBadReview(badReviewKeywords)

        if (viewModel.editReview.value.content.isNotEmpty()) {
            with(binding) {
                btnDetailCheck.isSelected = true
                etDetailReview.isVisible = true
                tvTextCount.isVisible = true
                tvTextTotal.isVisible = true
                binding.etDetailReview.setText(viewModel.editReview.value.content)
                viewModel.setDetailReviewText(viewModel.editReview.value.content)
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
                    viewModel.setSelectedBadReview(selectedBadButtonText)
                } else {
                    makeSpotImageAppbar("후기 키워드는 각각 3개까지 선택할 수 있어요")
                }
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


    private fun initMethodNaming() {
        when (viewModel.currentReviewState.value) {
            SeatRecordViewModel.ReviewType.SEAT_REVIEW -> {
                binding.tvReviewMySeat.text = "시야 후기"
                binding.tvWriteDetailReview.text = "더 자세한 시야 후기를 남길래요!"
                binding.etDetailReview.hint = "시야에 대한 구체적인 후기를 남기면 다른 사람들이 참고하기에 좋아요!"
            }

            SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW -> {
                binding.tvReviewMySeat.text = "직관 후기"
                binding.tvWriteDetailReview.text = "더 자세한 직관 후기를 남길래요!"
                binding.etDetailReview.hint = "나의 경기 직관 후기를 작성해주세요! 그날의 날씨나 소소한 감상도 좋아요!"
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
                    tvBlank.visibility = View.VISIBLE
                    svSeatReview.post {
                        svSeatReview.fullScroll(View.FOCUS_DOWN)
                    }
                } else {
                    svSeatReview.post {
                        svSeatReview.fullScroll(View.FOCUS_UP)
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
                        tvDetailReviewWarning.visibility = View.VISIBLE
                        ivTextWarning.visibility = View.VISIBLE
                        tvTextCount.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_error_primary))
                    } else {
                        etDetailReview.setBackgroundResource(R.drawable.rect_gray200_fill_12)
                        tvDetailReviewWarning.visibility = View.GONE
                        ivTextWarning.visibility = View.GONE
                    }
                }
                viewModel.setDetailReviewText(text.toString())
            }
        }
    }

    private fun initCompleteEvent() {
        binding.tvCompleteBtn.setOnSingleClickListener {
            if (binding.tvCompleteBtn.isEnabled) {
                viewModel.updateEditReviewDetail()
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