package com.dpm.presentation.seatrecord.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.seatreview.ResponseSeatBlock
import com.dpm.domain.entity.response.seatreview.ResponseSeatRange
import com.dpm.domain.model.seatreview.ValidSeat
import com.dpm.presentation.extension.colorOf
import com.dpm.presentation.extension.setMargins
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.seatreview.adapter.SelectSeatAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSelectSeatDialog : BindingBottomSheetDialog<FragmentSelectSeatBottomSheetBinding>(
    R.layout.fragment_select_seat_bottom_sheet,
    FragmentSelectSeatBottomSheetBinding::inflate,
) {
    private val viewModel: SeatRecordViewModel by activityViewModels()
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
        viewModel.initSelectedValue()
        updatedState()
        viewModel.getStadiumSection(viewModel.editReview.value.stadiumId)
        adapter = SelectSeatAdapter { position, sectionId ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.updateSelectedSectionId(sectionId)
            viewModel.setSelectedSeatZone(selectedSeatInfo.name)
            viewModel.getSeatBlock()
            binding.tvNextBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
            binding.tvNextBtn.isEnabled = true
        }
        binding.rvSelectSeatZone.adapter = adapter
        setupEditTextSeatInfo()

    }

    private fun initEvent() {
        onClickToggleSectionVisibility()
        onClickToggleSeatVisibility()
        onClickTabVisibility()
        onClickCheckOnlyColumn()
        onClickStadiumName()
        onClickDeleteEditText()
        onClickFinish()
    }

    private fun initObserve() {
        initObserveSection()
        initObserveSeatBlock()
        initObserveSeatRange()
    }

    private fun updatedState() {
        val data = viewModel.editReview.value
        viewModel.updateSelectedSectionId(data.sectionId)
        viewModel.setSelectedSeatZone(data.sectionName)
        viewModel.updateSelectedStadiumId(data.stadiumId)
    }

    private fun initObserveSection() {
        viewModel.stadiumSectionState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data.sectionList) {
                        state.data.sectionList.indexOfFirst { it.id == viewModel.selectedSectionId.value }
                            .takeIf { it != -1 }
                            ?.let {
                                adapter.setItemSelected(it)
                                viewModel.getSeatBlock()
                                binding.tvNextBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
                                binding.tvNextBtn.isEnabled = true
                            }
                    }
                    binding.ivSeatAgain.load(state.data.seatChart)
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("구역을 불러오는데 오류가 발생하였습니다.")
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
                    makeSpotImageAppbar("해당하는 블록을 불러오지 못했습니다.")
                }

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
                    makeSpotImageAppbar("해당 블록에 대한 열,번 범위 불러오기 실패")
                }

                else -> {}
            }
        }
    }

    private fun observeSuccessSeatBlock(blocks: List<ResponseSeatBlock>) {
        val blockCodes = mutableListOf("블록을 선택해주세요")
        val blockDisplayNameToCodeMap = mutableMapOf<String, String>()

        blocks.forEach { block ->
            val displayName = viewModel.getBlockListName(block.code)
            blockCodes.add(displayName)
            blockDisplayNameToCodeMap[displayName] = block.code
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_block_item, blockCodes)
        adapter.setDropDownViewResource(R.layout.custom_spinner_block_dropdown_item)

        with(binding.spinnerBlock) {
            this.adapter = adapter
            this.setSelection(0)
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val selectedDisplayName = blockCodes[position]
                    val selectedBlockCode = blockDisplayNameToCodeMap[selectedDisplayName] ?: ""
                    viewModel.setSelectedBlock(selectedBlockCode)
                    val selectedBlockId = blocks.find { it.code == selectedBlockCode }?.id ?: 0
                    viewModel.updateSelectedBlockId(selectedBlockId)
                    viewModel.getSeatRange()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun onClickToggleSectionVisibility() {
        binding.clSeatAgain.setOnSingleClickListener {
            binding.ivSeatAgain.isVisible = !binding.ivSeatAgain.isVisible
            if (binding.ivSeatAgain.isVisible) {
                binding.svSelectSeat.post { binding.svSelectSeat.fullScroll(View.FOCUS_DOWN) }
                binding.ivChevronDown.setImageResource(R.drawable.ic_chevron_up)
            } else {
                binding.ivChevronDown.setImageResource(R.drawable.ic_chevron_down)
            }
        }
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root.rootView,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 96
        ).show()
    }

    private fun onClickTabVisibility() = with(binding) {
        llTabSelectSeat.setOnSingleClickListener {
            moveToSeatView()
        }
        tvNextBtn.setOnSingleClickListener {
            moveToSeatView()
        }


        llTabSelectSection.setOnSingleClickListener {
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

    private fun moveToSeatView() = with(binding) {
        if (viewModel.selectedSectionId.value == 10) {
            clColumnNumber.visibility = INVISIBLE
            clOnlyNumber.visibility = VISIBLE
            clOnlyColumnBtn.visibility = GONE
            clOnlyColumn.visibility = GONE
            val scale = context?.resources?.displayMetrics?.density
            val paddingInPx = (31 * scale!! + 0.5f).toInt()
            tvNoneColumnWarning.setPaddingRelative(
                paddingInPx,
                tvNoneColumnWarning.paddingTop,
                tvNoneColumnWarning.paddingEnd,
                tvNoneColumnWarning.paddingBottom,
            )
            val marginInPx = (20 * scale!! + 0.5f).toInt()
            clColumnNumberDescription.setMargins(
                clColumnNumberDescription.marginLeft,
                marginInPx,
                clColumnNumberDescription.marginRight,
                clColumnNumberDescription.marginBottom
            )
            etOnlyTable.setText("")
            clOnlyTable.visibility = INVISIBLE
        } else if (viewModel.selectedSectionId.value == 2) {
            clColumnNumber.visibility = INVISIBLE
            clOnlyTable.visibility = VISIBLE
            clOnlyColumnBtn.visibility = GONE
            clOnlyColumn.visibility = GONE
            val scale = context?.resources?.displayMetrics?.density
            val marginInPx = (20 * scale!! + 0.5f).toInt()
            clColumnNumberDescription.setMargins(
                clColumnNumberDescription.marginLeft,
                marginInPx,
                clColumnNumberDescription.marginRight,
                clColumnNumberDescription.marginBottom
            )
            clOnlyNumber.visibility = GONE
            tvNoneColumnWarning.setPaddingRelative(
                0,
                tvNoneColumnWarning.paddingTop,
                tvNoneColumnWarning.paddingEnd,
                tvNoneColumnWarning.paddingBottom,
            )
            etOnlyNumber.setText("")
        } else {
            clColumnNumber.visibility = VISIBLE
            clOnlyTable.visibility = INVISIBLE
            clOnlyNumber.visibility = INVISIBLE
            clOnlyColumn.visibility = INVISIBLE
            clOnlyColumnBtn.visibility = VISIBLE
            tvNoneColumnWarning.setPaddingRelative(
                10,
                tvNoneColumnWarning.paddingTop,
                tvNoneColumnWarning.paddingEnd,
                tvNoneColumnWarning.paddingBottom,
            )
        }
        val scale = context?.resources?.displayMetrics?.density
        val marginInPx = (30 * scale!! + 0.5f).toInt()
        clColumnNumberDescription.setMargins(
            clColumnNumberDescription.marginLeft,
            marginInPx,
            clColumnNumberDescription.marginRight,
            clColumnNumberDescription.marginBottom
        )
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

    private fun onClickCheckOnlyColumn() {
        binding.clOnlyColumnBtn.setOnClickListener {
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
                    etOnlyColumn.setText("")
                }
            }
            isColumnCheckEnabled = !isColumnCheckEnabled
        }
    }

    private fun onClickStadiumName() {
        binding.clSelectStadium.setOnSingleClickListener {
            makeSpotImageAppbar("나중에 다른 구장도 추가될 예정이에요!")
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
        binding.ivDeleteOnlyTable.setOnSingleClickListener {
            binding.etOnlyTable.text.clear()
            binding.ivDeleteOnlyTable.visibility = GONE
        }
    }

    private fun onClickToggleSeatVisibility() {
        binding.clColumnNumberDescription.setOnClickListener {
            binding.llColumnDescription.isGone = !binding.llColumnDescription.isGone
            if (binding.llColumnDescription.isVisible) {
                binding.ivWhatColumnChevron.setImageResource(R.drawable.ic_chevron_up)
            } else {
                binding.ivWhatColumnChevron.setImageResource(R.drawable.ic_chevron_down)
            }
        }
    }

    private fun onClickFinish() {
        binding.tvCompleteBtn.setOnSingleClickListener {
            viewModel.updateEditReview()
            dismiss()
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
        binding.etOnlyTable.setOnFocusChangeListener { _, hasFocus ->
            binding.ivDeleteOnlyTable.visibility =
                if (binding.etOnlyTable.text.toString().isNotEmpty() && hasFocus) VISIBLE else GONE
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
            binding.ivDeleteOnlyColumn.visibility = if (text.toString()
                    .isNotEmpty() && binding.etOnlyColumn.hasFocus()
            ) VISIBLE else GONE
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
            binding.ivDeleteOnlyNumber.visibility = if (text.toString()
                    .isNotEmpty() && binding.etOnlyNumber.hasFocus()
            ) {
                VISIBLE
            } else {
                GONE
            }
            viewModel.seatRangeState.value.let { state ->
                if (state is UiState.Success) {
                    state.data.forEach { range ->
                        updateColumnNumberUI(range)
                    }
                }
            }
        }
        binding.etOnlyTable.addTextChangedListener { text: Editable? ->
            viewModel.setSelectedNumber(text.toString())
            binding.ivDeleteOnlyTable.visibility = if (text.toString()
                    .isNotEmpty() && binding.etOnlyTable.hasFocus()
            ) VISIBLE else GONE
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
        val matchingRowInfo =
            range.rowInfo.find { it.number.toString() == viewModel.selectedColumn.value }
        if (viewModel.selectedColumn.value.isEmpty() && viewModel.selectedNumber.value.isEmpty()) {
            viewModel.userSeatState.value = ValidSeat.NONE
            updateSeatEditTextUI()
            return
        }
        if (range.code == viewModel.selectedBlockName.value) {
            if (viewModel.selectedColumn.value.isEmpty() || viewModel.selectedNumber.value.isEmpty()) {
                viewModel.userSeatState.value = ValidSeat.NONE
            }
            when {
                matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty() -> {
                    viewModel.userSeatState.value =
                        if (viewModel.selectedNumber.value.isNotEmpty()) {
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
            if (range.code == viewModel.selectedBlockName.value) {
                viewModel.userSeatState.value = ValidSeat.NONE
                if (matchingRowInfo == null && viewModel.selectedColumn.value.isNotEmpty()) {
                    viewModel.userSeatState.value = ValidSeat.INVALID_COLUMN
                } else if (matchingRowInfo != null) {
                    viewModel.userSeatState.value = ValidSeat.VALID
                } else {
                    viewModel.userSeatState.value = ValidSeat.NONE
                }
            }
        }
        if (binding.clOnlyNumber.isVisible) {
            val matchingCode =
                range.rowInfo.find { range.code == viewModel.selectedBlockName.value }
            val selectedSeatNumber = viewModel.selectedNumber.value.toInt()
            if (matchingCode != null && viewModel.selectedNumber.value.isNotEmpty()) {
                if (matchingCode.seatNumList.contains(selectedSeatNumber)) {
                    viewModel.userSeatState.value = ValidSeat.VALID
                } else {
                    viewModel.userSeatState.value = ValidSeat.INVALID_NUMBER
                }
            }
        }
        if (binding.clOnlyTable.isVisible) {
            val matchingCode =
                range.rowInfo.find { range.code == viewModel.selectedBlockName.value }
            val selectedSeatNumber = viewModel.selectedNumber.value.toInt()
            if (matchingCode != null && viewModel.selectedNumber.value.isNotEmpty()) {
                val allSeatNumbers = range.rowInfo.flatMap { it.seatNumList }
                if (allSeatNumbers.contains(selectedSeatNumber)) {
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
                    etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    etOnlyColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    tvNoneColumnWarning.text = "존재하지 않는 열이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    tvCompleteBtn.isEnabled = false
                }
            }

            ValidSeat.INVALID_NUMBER -> {
                with(binding) {
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    etOnlyNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    etOnlyTable.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    tvNoneColumnWarning.text = "존재하지 않는 번이에요"
                    tvNoneColumnWarning.visibility = VISIBLE
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                    tvCompleteBtn.isEnabled = false
                }
            }

            ValidSeat.INVALID_COLUMN_NUMBER -> {
                with(binding) {
                    etColumn.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    etNumber.setBackgroundResource(R.drawable.rect_background_secondary_fill_error_primary_line_8)
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
                    etOnlyTable.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    tvNoneColumnWarning.visibility = GONE
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
                    etOnlyTable.setBackgroundResource(R.drawable.rect_background_secondary_fill_8)
                    tvNoneColumnWarning.visibility = GONE
                    tvCompleteBtn.isEnabled = false
                    tvCompleteBtn.setBackgroundResource(R.drawable.rect_gray200_fill_6)
                }
            }

            else -> {}
        }
    }


}