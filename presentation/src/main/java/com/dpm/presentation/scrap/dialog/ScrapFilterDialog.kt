package com.dpm.presentation.scrap.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentScrapFilterDialogBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.designsystem.extension.dpToPx
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.scrap.adapter.ScrapMonthAdapter
import com.dpm.presentation.scrap.adapter.ScrapMonthGridSpacingItemDecoration
import com.dpm.presentation.scrap.viewmodel.ScrapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScrapFilterDialog : BindingBottomSheetDialog<FragmentScrapFilterDialogBinding>(
    layoutRes = R.layout.fragment_scrap_filter_dialog,
    bindingInflater = FragmentScrapFilterDialogBinding::inflate
) {
    companion object {
        const val TAG = "ScrapFilterDialog"
    }

    private val viewModel: ScrapViewModel by activityViewModels()
    private lateinit var monthAdapter: ScrapMonthAdapter
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
            binding.tvBadSeven
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
        initObserve()
    }

    private fun initView() {
        initBottomSheetHeight()
        initMonthAdapter()
    }

    private fun initEvent() {
        selectedGoodBtn.forEach { button ->
            button.setOnClickListener {
                button.isSelected = !button.isSelected
                val selectedGoodButtonText =
                    selectedGoodBtn.filter { it.isSelected }.map { it.text.toString() }

                viewModel.setSelectedGoodReview(selectedGoodButtonText)
            }
        }

        selectedBadBtn.forEach { button ->
            button.setOnClickListener {
                button.isSelected = !button.isSelected
                val selectedBadButtonText =
                    selectedBadBtn.filter { it.isSelected }.map { it.text.toString() }
                viewModel.setSelectedBadReview(selectedBadButtonText)
            }
        }

        binding.btScrapSearch.setOnSingleClickListener {
            viewModel.updateAllFilter()
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.resetAllFilter()
    }

    private fun initObserve() {
        viewModel.months.asLiveData().observe(viewLifecycleOwner) {
            monthAdapter.submitList(it)
        }

        viewModel.selectedGoodReview.asLiveData().observe(viewLifecycleOwner) { reviews ->
            updateGoodReviewBtnState(reviews.map { it.name })
        }

        viewModel.selectedBadReview.asLiveData().observe(viewLifecycleOwner) { reviews ->
            updateBadReviewBtnState(reviews.map { it.name })
        }
    }

    private fun initBottomSheetHeight() {
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        val screenHeight = (resources.displayMetrics.heightPixels * 0.85).toInt()
        behavior.peekHeight = screenHeight

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.maxHeight = screenHeight

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (behavior.state == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset < -0.15) {
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }
        })
    }


    private fun initMonthAdapter() {
        viewModel.getMonths()
        monthAdapter = ScrapMonthAdapter(
            monthClick = {
                viewModel.updateMonth(it.month)
            }
        )
        binding.rvScrapMonth.adapter = monthAdapter
        binding.rvScrapMonth.addItemDecoration(
            ScrapMonthGridSpacingItemDecoration(
                spanCount = 5,
                spacing = 6.dpToPx(requireContext())
            )
        )

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

}