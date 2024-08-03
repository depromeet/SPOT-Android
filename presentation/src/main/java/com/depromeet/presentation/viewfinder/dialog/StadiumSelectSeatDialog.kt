package com.depromeet.presentation.viewfinder.dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.domain.model.viewfinder.Seat
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentStadiumSelectSeatDialogBinding
import com.depromeet.presentation.viewfinder.viewmodel.StadiumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StadiumSelectSeatDialog : BindingBottomSheetDialog<FragmentStadiumSelectSeatDialogBinding>(
    R.layout.fragment_stadium_select_seat_dialog,
    FragmentStadiumSelectSeatDialogBinding::inflate
) {
    companion object {
        const val TAG = "StadiumSelectSeatDialog"

        fun newInstance(): StadiumSelectSeatDialog {
            val args = Bundle()

            val fragment = StadiumSelectSeatDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val stadiumDetailViewModel: StadiumDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutSizeRatio(heightPercent = 0.7f, widthPercent = 1f)
        initView()
        initEvent()

    }

    private fun initView() {
        initEditText()

        binding.etOnlyColumn.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            binding.etOnlyColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_8)
            if (binding.clOnlyColumn.isVisible) {
                binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
            }
            if (editText?.length == 0) {
                binding.ivOnlyColumnCancel.visibility = View.INVISIBLE
            } else {
                binding.ivOnlyColumnCancel.visibility = View.VISIBLE
            }
        }

        binding.etColumn.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            binding.etColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_8)
            binding.etNumber.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_8)
            if (binding.clColumnNumber.isVisible) {
                if (binding.etNumber.text.isNotEmpty()) {
                    binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
                }
            }

            if (editText?.length == 0) {
                binding.ivColumnCancel.visibility = View.INVISIBLE
            } else {
                binding.ivColumnCancel.visibility = View.VISIBLE
            }
        }

        binding.etNumber.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            binding.etColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_8)
            binding.etNumber.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_8)
            if (binding.clColumnNumber.isVisible) {
                if (binding.etColumn.text.isNotEmpty()) {
                    binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
                }
            }

            if (editText?.length == 0) {
                binding.ivNumberCancel.visibility = View.INVISIBLE
            } else {
                binding.ivNumberCancel.visibility = View.VISIBLE
            }
        }
    }

    private fun initEvent() {
        readDescription()
        onClickCheckOnlyColumn()
        onClickSelectSeat()
        onClickEditTextClear()
    }

    private fun initEditText() {
        if (stadiumDetailViewModel.reviewFilter.value.rowNumber == null && stadiumDetailViewModel.reviewFilter.value.seatNumber == null) return

        if (stadiumDetailViewModel.reviewFilter.value.rowNumber != null && stadiumDetailViewModel.reviewFilter.value.seatNumber != null) {
            binding.etColumn.setText(stadiumDetailViewModel.reviewFilter.value.rowNumber.toString())
            binding.etNumber.setText(stadiumDetailViewModel.reviewFilter.value.seatNumber.toString())
            return
        }

        if (stadiumDetailViewModel.reviewFilter.value.rowNumber != null && stadiumDetailViewModel.reviewFilter.value.seatNumber == null) {
            binding.etOnlyColumn.setText(stadiumDetailViewModel.reviewFilter.value.rowNumber.toString())
            binding.btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_spot_green_fill_4)
            binding.clColumnNumber.visibility = View.INVISIBLE
            binding.clOnlyColumn.visibility = View.VISIBLE
            return
        }
    }

    private fun readDescription() {
        binding.clColumnNumberDescription.setOnClickListener {
            if (binding.layoutColumnDescription.isVisible) {
                binding.layoutColumnDescription.visibility = View.GONE
                binding.ivUpDown.setImageResource(R.drawable.ic_chevron_down)
            } else {
                binding.layoutColumnDescription.visibility = View.VISIBLE
                binding.ivUpDown.setImageResource(R.drawable.ic_chevron_up)
            }
        }
    }

    private fun onClickCheckOnlyColumn() {
        binding.btnCheckColumn.setOnClickListener {
            if (binding.clColumnNumber.isVisible) {
                binding.btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_spot_green_fill_4)
                binding.clColumnNumber.visibility = View.INVISIBLE
                binding.clOnlyColumn.visibility = View.VISIBLE
                binding.etColumn.setText("")
                binding.etNumber.setText("")
            } else {
                binding.btnCheckColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_primary_fill_4)
                binding.clOnlyColumn.visibility = View.INVISIBLE
                binding.clColumnNumber.visibility = View.VISIBLE
                binding.etOnlyColumn.setText("")
            }
            binding.btnAdapt.isEnabled = false
            binding.tvWarning.visibility = View.INVISIBLE
        }
    }

    private fun onClickSelectSeat() {
        binding.btnAdapt.setOnClickListener {
            if (binding.clColumnNumber.isVisible) {
                stadiumDetailViewModel.handleColumNumber(
                    binding.etColumn.text.toString().toInt(),
                    binding.etNumber.text.toString().toInt()
                ) { isSuccess, seat ->
                    when (seat) {
                        Seat.COLUMN_NUMBER -> {
                            binding.tvWarning.visibility = View.VISIBLE
                            binding.tvWarning.text = "존재하지 않는 열과 번입니다."
                            binding.etColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_error_primary_line_8)
                            binding.etNumber.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_error_primary_line_8)
                        }

                        Seat.COLUMN -> {
                            binding.tvWarning.visibility = View.VISIBLE
                            binding.tvWarning.text = "존재하지 않는 열이에요"
                            binding.etColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_error_primary_line_8)
                        }

                        Seat.NUMBER -> {
                            binding.tvWarning.visibility = View.VISIBLE
                            binding.tvWarning.text = "존재하지 않는 번이에요"
                            binding.etNumber.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_error_primary_line_8)
                        }

                        Seat.CHECK -> {
                            if (isSuccess) {
                                stadiumDetailViewModel.updateSeat(
                                    binding.etColumn.text.toString().toInt(),
                                    binding.etNumber.text.toString().toInt()
                                )
                                dismiss()
                            } else {
                                binding.tvWarning.visibility = View.VISIBLE
                                binding.tvWarning.text = "존재하지 않는 번이에요"
                                binding.etNumber.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_error_primary_line_8)
                            }
                        }
                    }
                }
            }

            if (binding.clOnlyColumn.isVisible) {
                stadiumDetailViewModel.handleColumn(
                    binding.etOnlyColumn.text.toString().toInt()
                ) { isSuccess, seat ->
                    if (isSuccess) {
                        stadiumDetailViewModel.updateSeat(
                            column = binding.etOnlyColumn.text.toString().toInt()
                        )
                        dismiss()
                    } else {
                        binding.tvWarning.visibility = View.VISIBLE
                        binding.tvWarning.text = "존재하지 않는 열이에요"
                        binding.etOnlyColumn.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_secondary_fill_error_primary_line_8)
                    }
                }
            }
        }
    }

    private fun onClickEditTextClear() {
        binding.ivOnlyColumnCancel.setOnClickListener {
            binding.etOnlyColumn.setText("")
            binding.ivOnlyColumnCancel.visibility = View.INVISIBLE
        }

        binding.ivColumnCancel.setOnClickListener {
            binding.etColumn.setText("")
            binding.ivColumnCancel.visibility = View.INVISIBLE
        }

        binding.ivNumberCancel.setOnClickListener {
            binding.etNumber.setText("")
            binding.ivNumberCancel.visibility = View.INVISIBLE
        }
    }
}