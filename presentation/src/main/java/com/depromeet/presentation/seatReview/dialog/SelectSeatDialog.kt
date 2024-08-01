package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import com.depromeet.presentation.seatReview.adapter.SelectSeatAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSeatDialog : BindingBottomSheetDialog<FragmentSelectSeatBottomSheetBinding>(
    R.layout.fragment_select_seat_bottom_sheet,
    FragmentSelectSeatBottomSheetBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    private lateinit var adapter: SelectSeatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStadiumSection()
        observeSeatBlock()
        observeSeatRange()
        setupSectionRecyclerView()
        setupTransactionSelectSeat()
        setupEditTextListeners()
    }

    // 구역 선택뷰
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

    private fun setupSectionRecyclerView() {
        adapter = SelectSeatAdapter { position, sectionId ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.setSelectedSeatZone(selectedSeatInfo.name)
            viewModel.getSeatBlock(viewModel.selectedStadiumId.value, sectionId)
            viewModel.updateSelectedSectionId(sectionId)
            updateNextBtnState()
        }
        binding.rvSelectSeatZone.adapter = adapter
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

    // 좌석 번호 뷰
    private fun observeSeatBlock() {
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
                        updateColumnNumberUI(range)
                    }
                }

                is UiState.Failure -> {
                    toast("오류가 발생했습니다")
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                }

                else -> {}
            }
        }
    }

    private fun setupEditTextListeners() {
        binding.etColumn.addTextChangedListener { text: Editable? ->
            val newColumn = text.toString()
            viewModel.setSelectedColumn(newColumn)
            viewModel.seatRangeState.value?.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }

        binding.etNumber.addTextChangedListener { text: Editable? ->
            val newNumber = text.toString()
            viewModel.setSelectedNumber(newNumber)
            viewModel.seatRangeState.value?.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }
    }

    // TODO : 추후 코드 개선 예정 (if else 이슈 ㅠㅠ)
    private fun updateColumnNumberUI(range: SeatRangeModel) {
        if (viewModel.selectedColumn.value.isEmpty() && viewModel.selectedNumber.value.isEmpty()) {
            binding.etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            binding.etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            binding.tvNoneColumnWarning.visibility = GONE
        }
        if (range.code == viewModel.selectedBlock.value) {
            val matchingRowInfo =
                range.rowInfo.find { it.number.toString() == viewModel.selectedColumn.value }
            if (matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty() && viewModel.selectedColumn.value.isNotEmpty()) {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
                    tvNoneColumnWarning.text = "존재하지 않는 열이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    binding.tvCompleteBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.white,
                        ),
                    )
                    binding.tvCompleteBtn.isEnabled = false
                }
                if (viewModel.selectedNumber.value.isNotEmpty()) {
                    // 열과 번호 모두 오류인 경우
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                        etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                        tvNoneColumnWarning.text = "존재하지 않는 열과 번이에요"
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                        binding.tvCompleteBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.white,
                            ),
                        )
                        binding.tvCompleteBtn.isEnabled = false
                    }
                }
            } else if (matchingRowInfo != null && viewModel.selectedNumber.value.isNotEmpty() && viewModel.selectedNumber.value.isNotBlank()) {
                if (!matchingRowInfo.seatNumList.contains(viewModel.selectedNumber.value.toInt())) {
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
                        etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                        tvNoneColumnWarning.text = "존재하지 않는 번이에요"
                        tvNoneColumnWarning.visibility = VISIBLE
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                        binding.tvCompleteBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.white,
                            ),
                        )
                        binding.tvCompleteBtn.isEnabled = false
                    }
                } else {
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
                        etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
                        tvNoneColumnWarning.visibility = GONE
                        binding.tvCompleteBtn.isEnabled = true
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray900_fill_6)
                        binding.tvCompleteBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.white,
                            ),
                        )
                    }
                }
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
        val blockCodes = mutableListOf<String>().apply {
            add("")
            addAll(blockItems.map { it.code })
        }

        val blockCodeToIdMap = blockItems.associate { it.code to it.id }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blockCodes)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)

        with(binding.spinnerBlock) {
            this.adapter = adapter
            this.setSelection(0, false)

            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position == 0) {
                        viewModel.setSelectedBlock("")
                        viewModel.updateSelectedBlockId(0)
                    } else {
                        val selectedBlock = blockCodes[position]
                        viewModel.setSelectedBlock(selectedBlock)
                        val selectedBlockId = blockCodeToIdMap[selectedBlock] ?: 0
                        viewModel.updateSelectedBlockId(selectedBlockId)
                        viewModel.getSeatRange(
                            viewModel.selectedStadiumId.value,
                            viewModel.selectedSectionId.value,
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.setSelectedBlock("")
                    viewModel.updateSelectedBlockId(0)
                }
            }
        }
    }
}
