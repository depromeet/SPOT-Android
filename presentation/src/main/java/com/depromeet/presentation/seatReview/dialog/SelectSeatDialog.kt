package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import com.depromeet.domain.entity.response.seatReview.SeatRangeModel
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
        observeReviewViewModel()
        observeStadiumSection()
        observeSeatBLock()
        observeSeatRange()
        setupSectionRecyclerView()
        setupTransactionSelectSeat()
        setupEditTextListeners()
    }

    private fun observeReviewViewModel() {
        viewModel.selectedSeatZone.asLiveData().observe(this) { adapter.notifyDataSetChanged() }
        viewModel.selectedBlock.asLiveData().observe(this) { updateCompleteBtnState() }
        viewModel.selectedColumn.asLiveData().observe(this) { updateCompleteBtnState() }
        viewModel.selectedNumber.asLiveData().observe(this) { updateCompleteBtnState() }
    }

    private fun observeStadiumSection() {
        viewModel.stadiumSectionState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data.sectionList)
                    binding.ivSeatAgain.load(state.data.seatChart)
                }

                is UiState.Failure -> {
                    toast("오류가 발생했습니다")
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {
                    toast("오류가 발생했습니다")
                }

                else -> {}
            }
        }
    }

    private fun observeSeatBLock() {
        viewModel.seatBlockState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    observeSuccessSeatBlock(state.data)
                }

                is UiState.Failure -> {
                    toast("오류가 발생했습니다")
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
                else -> {}
            }
        }
    }

    private fun observeSeatRange() {
        viewModel.seatRangeState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    state.data.forEach { range ->
                        updateIsExistedColumnUI(range.rowInfo)
                        updateColumnNUmberUI(range)
                    }
                }

                is UiState.Failure -> {
                    toast("오류가 발생했습니다")
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
                else -> {}
            }
        }
    }

    // TODO : 에러 처리 다시 하기

    private fun updateColumnNUmberUI(range: SeatRangeModel) {
        val selectedNumber = viewModel.selectedNumber.value.toIntOrNull()
        val selectedBlock = viewModel.selectedBlock.value
        val selectedColumn = viewModel.selectedColumn.value

        if (range.code == selectedBlock) {
            val matchingRowInfo = range.rowInfo.find { it.number.toString() == selectedColumn }
            if (matchingRowInfo == null) {
                updateColumnWarning("존재하지 않는 열이에요")
            } else {
                if (selectedNumber != null && (selectedNumber < matchingRowInfo.minSeatNum || selectedNumber > matchingRowInfo.maxSeatNum)) {
                    updateNumberWarning("존재하지 않는 번이에요")
                } else {
                    updateBackWarnings()
                }
            }
        } else {
            if (selectedNumber == null || range.rowInfo.none { it.minSeatNum <= selectedNumber && it.maxSeatNum >= selectedNumber }) {
                updateBothWarnings("존재하지 않는 열과 번이에요")
            } else {
                updateBackWarnings()
            }
        }
    }

    private fun updateIsExistedColumnUI(rowInfoList: List<SeatRangeModel.RowInfo>) {
        if (rowInfoList.size == 1) {
            with(binding) {
                etColumn.visibility = INVISIBLE
                tvColumn.visibility = INVISIBLE
                etNumber.visibility = INVISIBLE
                etNonColumnNumber.visibility = VISIBLE
            }
        } else {
            with(binding) {
                etColumn.visibility = VISIBLE
                tvColumn.visibility = VISIBLE
                etNumber.visibility = VISIBLE
                etNonColumnNumber.visibility = INVISIBLE
            }
        }
    }

    private fun observeSuccessSeatBlock(blockItems: List<SeatBlockModel>) {
        val blockCodes = blockItems.map { it.code }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blockCodes)
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
                    val selectedBlock = blockCodes[position]
                    viewModel.setSelectedBlock(selectedBlock)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.setSelectedBlock("")
                }
            }
        }
    }

    private fun setupSectionRecyclerView() {
        adapter = SectionListAdapter { position, sectionId ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.setSelectedSeatZone(selectedSeatInfo.name)
            viewModel.getSeatBlock(viewModel.selectedStadiumId.value, sectionId)
            viewModel.getSeatRange(viewModel.selectedStadiumId.value, sectionId)
            updateNextBtnState()
        }
        binding.rvSelectSeatZone.adapter = adapter
    }

    private fun setupEditTextListeners() {
        binding.etColumn.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedColumn(text.toString())
        }

        binding.etNumber.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedNumber(text.toString())
        }
    }

    private fun setupTransactionSelectSeat() {
        with(binding) {
            layoutSeatAgain.setOnSingleClickListener {
                ivSeatAgain.isVisible = !ivSeatAgain.isVisible
            }
            layoutColumnNumberDescription.setOnSingleClickListener {
                layoutColumnDescription.isGone = !layoutColumnDescription.isGone
            }
            tvWhatColumn.setOnSingleClickListener {
                layoutColumnDescription.visibility = VISIBLE
            }
            tvNextBtn.setOnSingleClickListener {
                svSelectSeat.visibility = INVISIBLE
                layoutSeatNumber.visibility = VISIBLE
                tvSelectSeatLine.visibility = INVISIBLE
                tvSelectNumberLine.visibility = VISIBLE
                tvCompleteBtn.visibility = VISIBLE
                tvNextBtn.visibility = GONE
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
            isEnabled = isBlockSelected && isColumnFilled && isNumberFilled
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

    private fun updateColumnWarning(message: String) {
        with(binding) {
            etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
            tvNoneColumnWarning.text = message
            tvNoneColumnWarning.visibility = VISIBLE
        }
    }

    private fun updateNumberWarning(message: String) {
        with(binding) {
            etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
            tvNoneColumnWarning.text = message
            tvNoneColumnWarning.visibility = VISIBLE
        }
    }

    private fun updateBothWarnings(message: String) {
        with(binding) {
            etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
            etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
            tvNoneColumnWarning.text = message
            tvNoneColumnWarning.visibility = VISIBLE
        }
    }

    private fun updateBackWarnings() {
        with(binding) {
            etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            tvNoneColumnWarning.visibility = GONE
        }
    }
}
