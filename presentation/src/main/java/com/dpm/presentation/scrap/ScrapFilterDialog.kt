package com.dpm.presentation.scrap

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
import com.dpm.presentation.extension.toast
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
                val selectedCount = selectedGoodBtn.count { it.isSelected }

                if (selectedCount < 3 || button.isSelected) {
                    button.isSelected = !button.isSelected
                    val selectedGoodButtonText =
                        selectedGoodBtn.filter { it.isSelected }.map { it.text.toString() }

                    viewModel.setSelectedGoodReview(selectedGoodButtonText)
                } else {
                    /** 추후 추가 되는지 안되는지에 따라서 수정 -> BAD도 */
                    toast("후기 키워드는 각각 3개까지 선택할 수 있어요")
                }
            }
        }

        selectedBadBtn.forEach { button ->
            button.setOnSingleClickListener {
                val selectedCount = selectedBadBtn.count { it.isSelected }

                if (selectedCount < 3 || button.isSelected) {
                    button.isSelected = !button.isSelected
                    val selectedBadButtonText =
                        selectedBadBtn.filter { it.isSelected }.map { it.text.toString() }

                    viewModel.setSelectedBadReview(selectedBadButtonText)
                } else {
                    /** 추후 추가 되는지 안되는지에 따라서 수정 -> GOOD도 */
                    toast("후기 키워드는 각각 3개까지 선택할 수 있어요")
                }
            }
        }
    }

    private fun initObserve() {
        viewModel.months.asLiveData().observe(this) {
            monthAdapter.submitList(it)
        }

        viewModel.selectedGoodReview.asLiveData().observe(viewLifecycleOwner) { reviews ->
            updateGoodReviewBtnState(reviews)
        }

        viewModel.selectedBadReview.asLiveData().observe(viewLifecycleOwner) { reviews ->
            updateBadReviewBtnState(reviews)
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