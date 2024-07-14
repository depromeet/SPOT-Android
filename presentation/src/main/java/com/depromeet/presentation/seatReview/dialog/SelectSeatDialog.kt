package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatReview.ReviewViewModel
import com.depromeet.presentation.seatReview.adapter.SeatInfo
import com.depromeet.presentation.seatReview.adapter.SelectSeatAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        adapter.submitList(getSeatMockData())
        setupButtons()
        initSpinner()
        setupEditTextListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.selectedBlock.observe(this) { updateCompleteButtonState() }
        viewModel.selectedColumn.observe(this) { updateCompleteButtonState() }
        viewModel.selectedNumber.observe(this) { updateCompleteButtonState() }
        viewModel.selectedSeatName.observe(this) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun initSpinner() {
        val blockItems = listOf("107", "108", "109", "110", "111", "112", "113", "114","115","116","117","118")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blockItems)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        with(binding.spinnerBlock) {
            this.adapter = adapter
            this.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        val selectedBlock = blockItems[position]
                        viewModel.setSelectedBlock(selectedBlock)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    private fun setupRecyclerView() {
        adapter = SelectSeatAdapter { position ->
            val selectedSeatInfo = adapter.currentList[position]
            adapter.setItemSelected(position)
            viewModel.setSelectedSeatName(selectedSeatInfo.seatName)
            updateNextButtonState()
        }
        binding.rvSelectSeat.adapter = adapter
    }

    private fun setupButtons() {
        with(binding) {
            tvNextBtn.setOnSingleClickListener {
                tvCompleteBtn.visibility = View.VISIBLE
                tvNextBtn.visibility = View.GONE
                svSelectSeat.visibility = View.INVISIBLE
                layoutSeatNumber.visibility = View.VISIBLE
                tvSelectSeatLine.visibility = View.INVISIBLE
                tvSelectNumberLine.visibility = View.VISIBLE
            }
            tvCompleteBtn.setOnSingleClickListener { dismiss() }
            layoutSeatAgain.setOnSingleClickListener { ivSeatAgain.isVisible = !ivSeatAgain.isVisible }
            layoutColumnNumberDescription.setOnSingleClickListener { layoutColumnDescription.isGone = !layoutColumnDescription.isGone }
            tvWhatColumn.setOnSingleClickListener { layoutColumnDescription.visibility = View.VISIBLE }
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

    private fun updateNextButtonState() {
        with(binding.tvNextBtn) {
            setBackgroundResource(R.drawable.rect_gray900_fill_6)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            isEnabled = true
        }
    }

    private fun updateCompleteButtonState() {
        val isBlockSelected = viewModel.selectedBlock.value?.isNotEmpty()
        val isColumnFilled = viewModel.selectedColumn.value?.isNotEmpty()
        val isNumberFilled = viewModel.selectedNumber.value?.isNotEmpty()

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

    private fun getSeatMockData(): List<SeatInfo> {
        return listOf(
            SeatInfo("프리미엄석", "켈리존", "#FF5733"),
            SeatInfo("테이블석", "", "#3366FF"),
            SeatInfo("오렌지석", "응원석", "#33FF33"),
            SeatInfo("블루석", "", "#FFFF33"),
            SeatInfo("레드석", "", "#FF33FF"),
            SeatInfo("네이비석", "", "#33FFFF"),
            SeatInfo("익사이팅석", "", "#FF6633"),
            SeatInfo("외야그린석", "", "#6633FF"),
            SeatInfo("휠체어석", "", "#33FF99"),
        )
    }
}
