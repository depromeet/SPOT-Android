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
import com.dpm.domain.model.seatreview.ValidSeat
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
    }

    private fun initView() {
        adapter = SelectSeatAdapter { position, sectionId ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.updateItemSelected(true)
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
        onClickTabVisibility()
        onClickToggleSectionVisibility()
        onClickCheckOnlyColumn()
        onClickDeleteEditText()
        onClickNextBtnVisibility()
    }

    private fun initObserve() {
        initObserveSection()
        initObserveSeatBlock()
        initObserveSeatRange()
    }

    private fun onClickTabVisibility() {
        with(binding) {
            binding.llTabSelectSeat.setOnSingleClickListener {
                if (!viewModel.sectionItemSelected.value) {
                    // TODO : 스낵바 교체
                    toast("'구역'을 먼저 선택해주세요")
                }
                viewModel.updateSelectedSectionId(viewModel.selectedSectionId.value)
                if (viewModel.selectedSectionId.value == 10) {
                    clColumnNumber.visibility = INVISIBLE
                    clOnlyNumber.visibility = VISIBLE
                    clOnlyColumnBtn.visibility = GONE
                    clOnlyColumn.visibility = GONE
                    val scale = context?.resources?.displayMetrics?.density
                    val paddingInPx = (31 * scale!! + 0.5f).toInt()
                    tvNoneColumnWarning.setPaddingRelative(paddingInPx, tvNoneColumnWarning.paddingTop, tvNoneColumnWarning.paddingEnd, tvNoneColumnWarning.paddingBottom)
                } else {
                    clColumnNumber.visibility = VISIBLE
                    clOnlyNumber.visibility = INVISIBLE
                    clOnlyColumn.visibility = INVISIBLE
                    clOnlyColumnBtn.visibility = VISIBLE
                    tvNoneColumnWarning.setPaddingRelative(tvNoneColumnWarning.paddingStart, tvNoneColumnWarning.paddingTop, tvNoneColumnWarning.paddingEnd, tvNoneColumnWarning.paddingBottom)
                }
                svSelectSeat.visibility = INVISIBLE
                svSeatNumber.visibility = VISIBLE
                tvSelectSeatLine.visibility = INVISIBLE
                tvSelectNumberLine.visibility = VISIBLE
                tvCompleteBtn.visibility = VISIBLE
                tvNextBtn.visibility = GONE
                clSeatBlock.visibility = VISIBLE
                tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
            }
            binding.llTabSelectSection.setOnSingleClickListener {
                viewModel.selectedSeatZone.asLiveData().observe(viewLifecycleOwner) { zone ->
                    if (zone.isNotEmpty()) {
                        svSelectSeat.visibility = VISIBLE
                        clSeatBlock.visibility = INVISIBLE
                        svSeatNumber.visibility = INVISIBLE
                        tvSelectSeatLine.visibility = VISIBLE
                        tvSelectNumberLine.visibility = INVISIBLE
                        tvCompleteBtn.visibility = INVISIBLE
                        tvNextBtn.visibility = VISIBLE
                        tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
                        tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                    }
                }
                if (viewModel.selectedBlock.value.isNotEmpty() && (viewModel.selectedColumn.value.isNotEmpty() || viewModel.selectedNumber.value.isNotEmpty())) {
                    svSelectSeat.visibility = VISIBLE
                    clSeatBlock.visibility = INVISIBLE
                    svSeatNumber.visibility = INVISIBLE
                    tvSelectSeatLine.visibility = VISIBLE
                    tvSelectNumberLine.visibility = INVISIBLE
                    tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
                    tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                    tvCompleteBtn.visibility = VISIBLE
                    tvNextBtn.visibility = INVISIBLE
                } else {
                    svSelectSeat.visibility = VISIBLE
                    clSeatBlock.visibility = INVISIBLE
                    svSeatNumber.visibility = INVISIBLE
                    tvSelectSeatLine.visibility = VISIBLE
                    tvSelectNumberLine.visibility = INVISIBLE
                    tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
                    tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                    tvCompleteBtn.visibility = INVISIBLE
                    tvNextBtn.visibility = VISIBLE
                }
            }
        }
    }

    private fun onClickCheckOnlyColumn() {
        binding.btnCheckColumn.setOnSingleClickListener {
            with(binding) {
                if (isColumnCheckEnabled) {
                    btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_primary_fill_4)
                    clOnlyColumn.visibility = INVISIBLE
                    clColumnNumber.visibility = VISIBLE
                    tvCompleteBtn.isEnabled = false
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    etColumn.setText("")
                    etNumber.setText("")
                } else {
                    btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_spot_green_fill_4)
                    clColumnNumber.visibility = INVISIBLE
                    clOnlyColumn.visibility = VISIBLE
                    tvCompleteBtn.isEnabled = false
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    etOnlyColumn.setText("")
                }
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
        binding.ivDeleteOnlyNumber.setOnSingleClickListener {
            binding.etOnlyNumber.text.clear()
            binding.ivDeleteOnlyNumber.visibility = GONE
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
                viewModel.updateSelectedSectionId(viewModel.selectedSectionId.value)
                if (viewModel.selectedSectionId.value == 10) {
                    clColumnNumber.visibility = INVISIBLE
                    clOnlyNumber.visibility = VISIBLE
                    clOnlyColumnBtn.visibility = GONE
                    clOnlyColumn.visibility = GONE
                    val scale = context?.resources?.displayMetrics?.density
                    val paddingInPx = (31 * scale!! + 0.5f).toInt()
                    tvNoneColumnWarning.setPaddingRelative(paddingInPx, tvNoneColumnWarning.paddingTop, tvNoneColumnWarning.paddingEnd, tvNoneColumnWarning.paddingBottom)
                } else {
                    clColumnNumber.visibility = VISIBLE
                    clOnlyNumber.visibility = INVISIBLE
                    clOnlyColumn.visibility = INVISIBLE
                    clOnlyColumnBtn.visibility = VISIBLE
                    tvNoneColumnWarning.setPaddingRelative(tvNoneColumnWarning.paddingStart, tvNoneColumnWarning.paddingTop, tvNoneColumnWarning.paddingEnd, tvNoneColumnWarning.paddingBottom)
                }
                svSelectSeat.visibility = INVISIBLE
                svSeatNumber.visibility = VISIBLE
                tvSelectSeatLine.visibility = INVISIBLE
                tvSelectNumberLine.visibility = VISIBLE
                tvCompleteBtn.visibility = VISIBLE
                tvNextBtn.visibility = GONE
                clSeatBlock.visibility = VISIBLE
                tvSelectZone.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_caption))
                tvSelectNumber.setTextColor(binding.root.context.colorOf(com.depromeet.designsystem.R.color.color_foreground_heading))
            }
            tvCompleteBtn.setOnSingleClickListener {
                val selectedSeatZone = viewModel.selectedSeatZone.value
                val selectedBlock = viewModel.selectedBlock.value
                val selectedColumn = viewModel.selectedColumn.value
                val selectedNumber = viewModel.selectedNumber.value
                val sectionId = viewModel.selectedSectionId.value
                val result = Bundle().apply {
                    putString("seatZone", selectedSeatZone)
                    putString("block", selectedBlock)
                    putString("column", selectedColumn)
                    putString("number", selectedNumber)
                    putInt("sectionId", sectionId)
                    putBoolean("isColumnCheckEnabled", isColumnCheckEnabled)
                }
                parentFragmentManager.setFragmentResult("selectSeatResult", result)
                viewModel.updateItemSelected(false)
                dismiss()
            }
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
        binding.etOnlyColumn.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteOnlyColumn.visibility =
                if (binding.etOnlyColumn.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
        }
        binding.etOnlyNumber.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteOnlyNumber.visibility =
                if (binding.etOnlyNumber.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
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
        binding.etOnlyNumber.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedNumber(text.toString())
            binding.ivDeleteOnlyNumber.visibility = if (text.toString().isNotEmpty() && binding.etOnlyNumber.hasFocus())VISIBLE else GONE
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
        val matchingRowInfo = range.rowInfo.find { it.number.toString() == viewModel.selectedColumn.value }
        if (viewModel.selectedColumn.value.isEmpty() && viewModel.selectedNumber.value.isEmpty()) {
            viewModel.userSeatState.value = ValidSeat.NONE
            updateSeatEditTextUI()
            return
        }
        if (range.code == viewModel.selectedBlock.value) {
            if (viewModel.selectedColumn.value.isEmpty() || viewModel.selectedNumber.value.isEmpty()) {
                viewModel.userSeatState.value = ValidSeat.NONE
            }
            when {
                matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty() -> {
                    viewModel.userSeatState.value = if (viewModel.selectedNumber.value.isNotEmpty()) {
                        ValidSeat.INVALID_COLUMN_NUMBER
                    } else {
                        ValidSeat.INVALID_COLUMN
                    }
                }
                matchingRowInfo != null && viewModel.selectedNumber.value.isNotEmpty() -> {
                    if (!matchingRowInfo.seatNumList.contains(viewModel.selectedNumber.value.toInt())) {
                        viewModel.userSeatState.value = ValidSeat.INVALID_NUMBER
                    } else {
                        viewModel.userSeatState.value = ValidSeat.VALID
                    }
                }
            }
        }
        if (binding.clOnlyColumn.isVisible) {
            viewModel.userSeatState.value = ValidSeat.NONE
            if (matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty()) {
                viewModel.userSeatState.value = ValidSeat.INVALID_COLUMN
            } else if (matchingRowInfo != null) {
                viewModel.userSeatState.value = ValidSeat.VALID
            } else {
                viewModel.userSeatState.value = ValidSeat.NONE
            }
        }
        if (binding.clOnlyNumber.isVisible) {
            val matchingCode = range.rowInfo.find { range.code == viewModel.selectedBlock.value }
            val selectedSeatNumber = viewModel.selectedNumber.value.toInt()
            if (matchingCode != null && viewModel.selectedNumber.value.isNotEmpty()) {
                if (matchingCode.seatNumList.contains(selectedSeatNumber)) {
                    viewModel.userSeatState.value = ValidSeat.VALID
                } else {
                    viewModel.userSeatState.value = ValidSeat.INVALID_NUMBER
                }
            }
        }
        updateSeatEditTextUI()
    }

    private fun updateSeatEditTextUI() {
        when (viewModel.userSeatState.value) {
            ValidSeat.INVALID_COLUMN -> {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etOnlyColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    tvNoneColumnWarning.text = "존재하지 않는 열이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    tvCompleteBtn.isEnabled = false
                }
            }
            ValidSeat.INVALID_NUMBER -> {
                with(binding) {
                    etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    etOnlyNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    tvNoneColumnWarning.text = "존재하지 않는 번이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    tvCompleteBtn.isEnabled = false
                }
            }
            ValidSeat.INVALID_COLUMN_NUMBER -> {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_red1_line_12)
                    tvNoneColumnWarning.text = "존재하지 않는 열과 번이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    tvCompleteBtn.isEnabled = false
                }
            }
            ValidSeat.VALID -> {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etOnlyColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etOnlyNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    tvNoneColumnWarning.visibility = INVISIBLE
                    tvCompleteBtn.isEnabled = true
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
                }
            }
            ValidSeat.NONE -> {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etOnlyColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etOnlyNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    tvNoneColumnWarning.visibility = INVISIBLE
                    tvCompleteBtn.isEnabled = false
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                }
            }

            else -> {}
        }
    }
    private fun observeSuccessSeatBlock(blockItems: List<ResponseSeatBlock>) {
        val blockCodes = mutableListOf("블록을 선택해주세요")
        val blockDisplayNameToCodeMap = mutableMapOf<String, String>()

        blockItems.forEach { block ->
            val displayName = viewModel.getBlockListName(block.code)
            blockCodes.add(displayName)
            blockDisplayNameToCodeMap[displayName] = block.code
        }

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
                        binding.etColumn.setText("")
                        binding.etNumber.setText("")
                        binding.etOnlyColumn.setText("")
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                        binding.tvCompleteBtn.isEnabled = false
                    }

                    if (position > 0) {
                        val selectedDisplayName = blockCodes[position]
                        val selectedBlockCode = blockDisplayNameToCodeMap[selectedDisplayName] ?: ""
                        viewModel.setSelectedBlock(selectedBlockCode)
                        val selectedBlockId = blockItems.find { it.code == selectedBlockCode }?.id ?: 0
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
                    binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    binding.tvCompleteBtn.isEnabled = false
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
