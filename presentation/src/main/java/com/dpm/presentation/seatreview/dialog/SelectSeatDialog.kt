package com.dpm.presentation.seatreview.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.seatreview.ResponseSeatBlock
import com.dpm.domain.entity.response.seatreview.ResponseSeatRange
import com.dpm.presentation.extension.colorOf
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.extension.toast
import com.dpm.presentation.seatreview.ReviewViewModel
import com.dpm.presentation.seatreview.adapter.SelectSeatAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSeatDialog : BindingBottomSheetDialog<FragmentSelectSeatBottomSheetBinding>(
    R.layout.fragment_select_seat_bottom_sheet,
    FragmentSelectSeatBottomSheetBinding::inflate,
) {
    private val viewModel: ReviewViewModel by activityViewModels()
    private lateinit var adapter: SelectSeatAdapter
    private var isColumnCheckEnabled = false

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
        initView()
        initEvent()
        initObserve()
    }

    private fun initView() {
        adapter = SelectSeatAdapter { position, sectionId ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.setSelectedSeatZone(selectedSeatInfo.name)
            viewModel.getSeatBlock(viewModel.selectedStadiumId.value, sectionId)
            viewModel.updateSelectedSectionId(sectionId)
            binding.tvNextBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
            binding.tvNextBtn.isEnabled = true
        }
        binding.rvSelectSeatZone.adapter = adapter
        setupEditTextSeatInfo()
    }

    private fun initEvent() {
        binding.clColumnNumberDescription.setOnSingleClickListener { onClickToggleSeatVisibility() }
        binding.ivHelpCircle.setOnSingleClickListener { onClickToggleSeatVisibility() }
        binding.ivWhatColumnChevron.setOnSingleClickListener { onClickToggleSeatVisibility() }
        binding.tvWhatColumn.setOnSingleClickListener { onClickToggleSeatVisibility() }
        onClickToggleSectionVisibility()
        onClickNextBtnVisibility()
        onClickTabVisibility()
        onClickDeleteEditText()
        onClickCheckOnlyColumn()
    }

    private fun initObserve() {
        initObserveSection()
        initObserveSeatBlock()
        initObserveSeatRange()
    }

    private fun onClickTabVisibility() {
        with(binding) {
            llTabSelectSection.setOnSingleClickListener {
                svSelectSeat.visibility = VISIBLE
                clSeatNumber.visibility = INVISIBLE
                svSeatNumber.visibility = INVISIBLE
                tvSelectSeatLine.visibility = VISIBLE
                tvSelectNumberLine.visibility = INVISIBLE
                tvCompleteBtn.visibility = INVISIBLE
                tvNextBtn.visibility = VISIBLE
                tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
                tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
            }
        }
    }
    private fun onClickCheckOnlyColumn() {
        binding.btnCheckColumn.setOnSingleClickListener {
            if (isColumnCheckEnabled) {
                binding.btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_primary_fill_4)
                binding.clOnlyColumn.visibility = INVISIBLE
                binding.clColumnNumber.visibility = VISIBLE
                binding.tvCompleteBtn.isEnabled = false
                binding.etColumn.setText("")
                binding.etNumber.setText("")
            } else {
                binding.btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_spot_green_fill_4)
                binding.clColumnNumber.visibility = INVISIBLE
                binding.clOnlyColumn.visibility = VISIBLE
                binding.tvCompleteBtn.isEnabled = false
                binding.etOnlyColumn.setText("")
            }
            isColumnCheckEnabled = !isColumnCheckEnabled
        }
    }

    private fun onClickDeleteEditText() {
        binding.ivDeleteColumn.setOnSingleClickListener {
            binding.etColumn.text.clear()
            binding.ivDeleteColumn.visibility = GONE
        }
        binding.ivDeleteNumber.setOnSingleClickListener {
            binding.etNumber.text.clear()
            binding.ivDeleteNumber.visibility = GONE
        }
        binding.ivDeleteOnlyColumn.setOnSingleClickListener {
            binding.etOnlyColumn.text.clear()
            binding.ivDeleteOnlyColumn.visibility = GONE
        }
    }

    private fun onClickToggleSeatVisibility() {
        binding.llColumnDescription.isGone = !binding.llColumnDescription.isGone
        if (binding.llColumnDescription.isVisible) {
            binding.ivWhatColumnChevron.setImageResource(R.drawable.ic_chevron_up)
        } else {
            binding.ivWhatColumnChevron.setImageResource(R.drawable.ic_chevron_down)
        }
    }

    private fun onClickToggleSectionVisibility() {
        binding.clSeatAgain.setOnSingleClickListener {
            binding.ivSeatAgain.isVisible = !binding.ivSeatAgain.isVisible
            if (binding.ivSeatAgain.isVisible) {
                binding.svSelectSeat.post { binding.svSelectSeat.fullScroll(FOCUS_DOWN) }
                binding.ivChevronDown.setImageResource(R.drawable.ic_chevron_up)
            } else {
                binding.ivChevronDown.setImageResource(R.drawable.ic_chevron_down)
            }
        }
    }

    private fun onClickNextBtnVisibility() {
        with(binding) {
            tvNextBtn.setOnSingleClickListener {
                svSelectSeat.visibility = INVISIBLE
                clSeatNumber.visibility = VISIBLE
                svSeatNumber.visibility = VISIBLE
                tvSelectSeatLine.visibility = INVISIBLE
                tvSelectNumberLine.visibility = VISIBLE
                tvCompleteBtn.visibility = VISIBLE
                tvNextBtn.visibility = GONE
                tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
            }
            tvCompleteBtn.setOnSingleClickListener { dismiss() }
        }
    }

    private fun setupEditTextSeatInfo() {
        binding.etColumn.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteColumn.visibility =
                if (binding.etColumn.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
        }
        binding.etNumber.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteNumber.visibility =
                if (binding.etNumber.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
        }
        binding.etColumn.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedColumn(text.toString())
            binding.ivDeleteColumn.visibility =
                if (text.toString().isNotEmpty() && binding.etColumn.hasFocus()) VISIBLE else GONE
            viewModel.seatRangeState.value.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }
        binding.etNumber.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedNumber(text.toString())
            binding.ivDeleteNumber.visibility = if (text.toString().isNotEmpty()) VISIBLE else GONE
            viewModel.seatRangeState.value.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }
        binding.etOnlyColumn.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedColumn(text.toString())
            binding.ivDeleteOnlyColumn.visibility = if (text.toString().isNotEmpty() && binding.etOnlyColumn.hasFocus()) VISIBLE else GONE
            viewModel.seatRangeState.value.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }
    }
    private fun updateColumnNumberUI(range: ResponseSeatRange) {
        if (range.code == viewModel.selectedBlock.value) {
            val matchingRowInfo =
                range.rowInfo.find { it.number.toString() == viewModel.selectedColumn.value }
            if (matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty()) {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    tvNoneColumnWarning.text = "존재하지 않는 열이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    binding.tvCompleteBtn.setTextColor(binding.root.context.colorOf(android.R.color.white))
                    binding.tvCompleteBtn.isEnabled = false
                }
                if (viewModel.selectedNumber.value.isNotEmpty()) {
                    // 열과 번호 모두 오류인 경우
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                        etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                        tvNoneColumnWarning.text = "존재하지 않는 열과 번이에요"
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                        binding.tvCompleteBtn.setTextColor(binding.root.context.colorOf(android.R.color.white))
                        binding.tvCompleteBtn.isEnabled = false
                    }
                }
            } else if (matchingRowInfo != null && viewModel.selectedNumber.value.isNotEmpty() && viewModel.selectedNumber.value.isNotBlank()) {
                if (!matchingRowInfo.seatNumList.contains(viewModel.selectedNumber.value.toInt())) {
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                        etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                        tvNoneColumnWarning.text = "존재하지 않는 번이에요"
                        tvNoneColumnWarning.visibility = VISIBLE
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                        binding.tvCompleteBtn.isEnabled = false
                    }
                } else {
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                        etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                        tvNoneColumnWarning.visibility = INVISIBLE
                        binding.tvCompleteBtn.isEnabled = true
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
                    }
                }
            } else {
                binding.etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                binding.etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                binding.etOnlyColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                binding.tvCompleteBtn.isEnabled = false
            }
        }
        if (binding.clOnlyColumn.isVisible) {
            binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
            binding.tvCompleteBtn.isEnabled = false
            val matchingRowInfo =
                range.rowInfo.find { it.number.toString() == viewModel.selectedColumn.value }
            if (matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty()) {
                with(binding) {
                    etOnlyColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    tvNoneColumnWarning.text = "존재하지 않는 열이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    binding.tvCompleteBtn.setTextColor(binding.root.context.colorOf(android.R.color.white))
                    binding.tvCompleteBtn.isEnabled = false
                }
            } else if (matchingRowInfo != null && viewModel.selectedColumn.value.isNotEmpty()) {
                with(binding) {
                    etOnlyColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    tvNoneColumnWarning.visibility = INVISIBLE
                    binding.tvCompleteBtn.isEnabled = true
                    binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
                }
            }
        }
    }

    private fun observeSuccessSeatBlock(blockItems: List<ResponseSeatBlock>) {
        val blockCodes = mutableListOf("블록을 선택해주세요")
        blockCodes.addAll(blockItems.map { it.code })
        val blockCodeToIdMap = blockItems.associate { it.code to it.id }

        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_block_item, blockCodes)
        adapter.setDropDownViewResource(R.layout.custom_spinner_block_dropdown_item)

        with(binding.spinnerBlock) {
            this.adapter = adapter
            this.setSelection(0)
            binding.ivSelectBlockChevron.setImageResource(R.drawable.ic_chevron_down)
            this.setOnTouchListener { _, _ ->
                binding.ivSelectBlockChevron.setImageResource(R.drawable.ic_chevron_up)
                false
            }
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (view is TextView) {
                        if (position == 0) {
                            view.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                            binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                            binding.tvCompleteBtn.isEnabled = false
                        }
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                        binding.tvCompleteBtn.isEnabled = false
                        binding.etColumn.setText("")
                        binding.etNumber.setText("")
                        binding.etOnlyColumn.setText("")
                    }

                    if (position > 0) {
                        val selectedBlock = blockCodes[position]
                        viewModel.setSelectedBlock(selectedBlock)
                        val selectedBlockId = blockCodeToIdMap[selectedBlock] ?: 0
                        viewModel.updateSelectedBlockId(selectedBlockId)
                        viewModel.getSeatRange(
                            viewModel.selectedStadiumId.value,
                            viewModel.selectedSectionId.value,
                        )
                    }
                    binding.ivSelectBlockChevron.setImageResource(R.drawable.ic_chevron_down)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.ivSelectBlockChevron.setImageResource(R.drawable.ic_chevron_down)
                }
            }
        }
    }

    private fun initObserveSection() {
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

    private fun initObserveSeatBlock() {
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

    private fun initObserveSeatRange() {
        viewModel.seatRangeState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    state.data.forEach { range ->
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
}
