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
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.seatreview.ResponseSeatBlock
import com.dpm.domain.entity.response.seatreview.ResponseSeatRange
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
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
        initObserve()
    }

    private fun initObserve() {
        initObserveSection()
        initObserveSeatBlock()
        initObserveSeatRange()
    }

    // 구역 선택뷰
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

    private fun initView() {
        adapter = SelectSeatAdapter { position, sectionId ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.setSelectedSeatZone(selectedSeatInfo.name)
            viewModel.getSeatBlock(viewModel.selectedStadiumId.value, sectionId)
            viewModel.updateSelectedSectionId(sectionId)
            updateNextBtnState()
        }
        binding.rvSelectSeatZone.adapter = adapter

        setupTransactionSelectSeat()
        setupEditTextListeners()
    }

    private fun toggleDescriptionVisibility() {
        binding.layoutColumnDescription.isGone = !binding.layoutColumnDescription.isGone
        if (binding.layoutColumnDescription.isVisible) {
            binding.ivWhatColumnChevron.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_chevron_up,
                ),
            )
        } else {
            binding.ivWhatColumnChevron.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_chevron_down,
                ),
            )
        }
    }

    private fun setupTransactionSelectSeat() {
        with(binding) {
            layoutSeatAgain.setOnSingleClickListener {
                ivSeatAgain.isVisible = !ivSeatAgain.isVisible
                if (ivSeatAgain.isVisible) {
                    svSelectSeat.post {
                        svSelectSeat.fullScroll(FOCUS_DOWN)
                    }
                    binding.ivChevronDown.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_chevron_up,
                        ),
                    )
                } else {
                    binding.ivChevronDown.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_chevron_down,
                        ),
                    )
                }
            }
            layoutColumnNumberDescription.setOnSingleClickListener {
                toggleDescriptionVisibility()
            }
            ivHelpCircle.setOnSingleClickListener {
                toggleDescriptionVisibility()
            }
            ivWhatColumnChevron.setOnSingleClickListener {
                toggleDescriptionVisibility()
            }
            tvWhatColumn.setOnSingleClickListener {
                toggleDescriptionVisibility()
            }
            tvNextBtn.setOnSingleClickListener {
                svSelectSeat.visibility = INVISIBLE
                layoutSeatNumber.visibility = VISIBLE
                svSeatNumber.visibility = VISIBLE
                tvSelectSeatLine.visibility = INVISIBLE
                tvSelectNumberLine.visibility = VISIBLE
                tvCompleteBtn.visibility = VISIBLE
                tvNextBtn.visibility = GONE
                tvSelectZone.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_foreground_caption,
                    ),
                )
                tvSelectNumber.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_foreground_heading,
                    ),
                )
            }

            layoutTabSelectSection.setOnSingleClickListener {
                svSelectSeat.visibility = VISIBLE
                layoutSeatNumber.visibility = INVISIBLE
                svSeatNumber.visibility = INVISIBLE
                tvSelectSeatLine.visibility = VISIBLE
                tvSelectNumberLine.visibility = INVISIBLE
                tvCompleteBtn.visibility = INVISIBLE
                tvNextBtn.visibility = VISIBLE
                tvSelectZone.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_foreground_heading,
                    ),
                )
                tvSelectNumber.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_foreground_caption,
                    ),
                )
            }
            tvCompleteBtn.setOnSingleClickListener { dismiss() }
        }
    }

    private fun updateNextBtnState() {
        with(binding.tvNextBtn) {
            setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
            isEnabled = true
        }
    }

    // 좌석 번호
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

    private fun setupEditTextListeners() {
        binding.etColumn.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteColumn.visibility = if (binding.etColumn.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
        }
        binding.etColumn.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedColumn(text.toString())
            binding.ivDeleteColumn.visibility = if (text.toString().isNotEmpty() && binding.etColumn.hasFocus()) VISIBLE else GONE
            viewModel.seatRangeState.value.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }
        binding.etNumber.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteNumber.visibility = if (binding.etNumber.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
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
        binding.ivDeleteColumn.setOnSingleClickListener {
            binding.etColumn.text.clear()
            binding.ivDeleteColumn.visibility = GONE
        }
        binding.ivDeleteNumber.setOnSingleClickListener {
            binding.etNumber.text.clear()
            binding.ivDeleteNumber.visibility = GONE
        }
    }

    // TODO : 추후 코드 개선 예정 (if else 이슈 ㅠㅠ)
    private fun updateColumnNumberUI(range: ResponseSeatRange) {
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
                        binding.tvCompleteBtn.isEnabled = false
                    }
                } else {
                    with(binding) {
                        etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
                        etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
                        tvNoneColumnWarning.visibility = GONE
                        binding.tvCompleteBtn.isEnabled = true
                        binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
                    }
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
                            view.setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.designsystem.R.color.color_foreground_caption))
                            binding.tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                            binding.tvCompleteBtn.isEnabled = false
                        } else {
                        }
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
}