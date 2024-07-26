package com.depromeet.presentation.viewfinder.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
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
            binding.etOnlyColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            if (binding.clOnlyColumn.isVisible) {
                binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
            }
        }

        binding.etColumn.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            binding.etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            binding.etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            if (binding.clColumnNumber.isVisible) {
                if (binding.etNumber.text.isNotEmpty()) {
                    binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
                }
            }
        }

        binding.etNumber.addTextChangedListener { editText ->
            binding.tvWarning.visibility = View.INVISIBLE
            binding.etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            binding.etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_gray200_line_12)
            if (binding.clColumnNumber.isVisible) {
                if (binding.etColumn.text.isNotEmpty()) {
                    binding.btnAdapt.isEnabled = editText?.isNotEmpty() == true
                }
            }
        }
    }

    private fun initEvent() {
        readDescription()
        onClickCheckOnlyColumn()
        onClickSelectSeat()
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
            binding.btnCheckColumn.setBackgroundResource(R.drawable.ic_check)
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
                binding.btnCheckColumn.setBackgroundResource(R.drawable.ic_check)
                binding.clColumnNumber.visibility = View.INVISIBLE
                binding.clOnlyColumn.visibility = View.VISIBLE
                binding.etColumn.setText("")
                binding.etNumber.setText("")
            } else {
                binding.btnCheckColumn.setBackgroundResource(R.drawable.ic_uncheck)
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
                        Seat.COLUMN -> {
                            if (!isSuccess) {
                                binding.tvWarning.visibility = View.VISIBLE
                                binding.tvWarning.text = "존재하지 않는 열이에요"
                                binding.etColumn.setBackgroundResource(R.drawable.rect_gray50_fill_warning01red_line_12)
                            }
                        }

                        Seat.NUMBER -> {
                            if (isSuccess) {
                                stadiumDetailViewModel.updateSeat(
                                    binding.etColumn.text.toString().toInt(),
                                    binding.etNumber.text.toString().toInt()
                                )
                                dismiss()
                            } else {
                                binding.tvWarning.visibility = View.VISIBLE
                                binding.tvWarning.text = "존재하지 않는 번이에요"
                                binding.etNumber.setBackgroundResource(R.drawable.rect_gray50_fill_warning01red_line_12)
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
                        binding.etOnlyColumn.setBackgroundResource(R.drawable.rect_gray50_fill_warning01red_line_12)
                    }
                }
            }
        }
    }
}