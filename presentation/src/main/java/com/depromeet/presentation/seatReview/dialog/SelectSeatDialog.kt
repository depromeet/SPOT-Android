package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.core.state.UiState
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.seatReview.ReviewViewModel
import com.depromeet.presentation.seatReview.adapter.SectionListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSeatDialog : BindingBottomSheetDialog<FragmentSelectSeatBottomSheetBinding>(
    R.layout.fragment_select_seat_bottom_sheet,
    FragmentSelectSeatBottomSheetBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    private lateinit var adapter: SectionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetHeight(view)
        setupZoneRecyclerView()
        setupButtons()
        initBlockSpinner()
        setupEditTextListeners()
        setupStadiumSection()
        observeReviewViewModel()
    }

    private fun observeReviewViewModel() {
        viewModel.selectedSeatZone.asLiveData().observe(this) { adapter.notifyDataSetChanged() }
        viewModel.selectedBlock.asLiveData().observe(this) { updateCompleteBtnState() }
        viewModel.selectedColumn.asLiveData().observe(this) { updateCompleteBtnState() }
        viewModel.selectedNumber.asLiveData().observe(this) { updateCompleteBtnState() }
    }

    private fun setupStadiumSection() {
        viewModel.stadiumSectionState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data.sectionList)
                    binding.ivSeatAgain.load(state.data.seatChart)
                }

                is UiState.Failure -> {
                    toast("오류가 발생했습니다")
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                    toast("오류가 발생했습니다")
                }

                else -> {}
            }
        }
    }

    private fun setupZoneRecyclerView() {
        adapter = SectionListAdapter { position ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.setSelectedSeatZone(selectedSeatInfo.name)
            updateNextBtnState()
        }
        binding.rvSelectSeatZone.adapter = adapter
    }

    private fun initBlockSpinner() {
        val blockItems = listOf("107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blockItems)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

        with(binding.spinnerBlock) {
            this.adapter = adapter
            this.setSelection(Adapter.NO_SELECTION, false)
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val selectedBlock = blockItems[position]
                    viewModel.setSelectedBlock(selectedBlock)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.setSelectedBlock("")
                }
            }
        }
    }

    private fun setupEditTextListeners() {
        binding.etColumn.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedColumn(text.toString())
        }

        binding.etNumber.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedNumber(text.toString())
        }
    }

    private fun setupButtons() {
        with(binding) {
            layoutSeatAgain.setOnSingleClickListener {
                ivSeatAgain.isVisible = !ivSeatAgain.isVisible
            }
            layoutColumnNumberDescription.setOnSingleClickListener {
                layoutColumnDescription.isGone = !layoutColumnDescription.isGone
            }
            tvWhatColumn.setOnSingleClickListener {
                layoutColumnDescription.visibility = View.VISIBLE
            }
            tvNextBtn.setOnSingleClickListener {
                svSelectSeat.visibility = View.INVISIBLE
                layoutSeatNumber.visibility = View.VISIBLE
                tvSelectSeatLine.visibility = View.INVISIBLE
                tvSelectNumberLine.visibility = View.VISIBLE
                tvCompleteBtn.visibility = View.VISIBLE
                tvNextBtn.visibility = View.GONE
            }
            tvCompleteBtn.setOnSingleClickListener { dismiss() }
        }
    }

    private fun updateNextBtnState() {
        with(binding.tvNextBtn) {
            setBackgroundResource(R.drawable.rect_gray900_fill_6)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            isEnabled = true
        }
    }

    private fun updateCompleteBtnState() {
        val isBlockSelected = viewModel.selectedBlock.value.isNotEmpty()
        val isColumnFilled = viewModel.selectedColumn.value.isNotEmpty()
        val isNumberFilled = viewModel.selectedNumber.value.isNotEmpty()

        with(binding.tvCompleteBtn) {
            isEnabled = isBlockSelected == true && isColumnFilled == true && isNumberFilled == true
            if (isEnabled) {
                setBackgroundResource(R.drawable.rect_gray900_fill_6)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                setBackgroundResource(R.drawable.rect_gray200_fill_6)
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
