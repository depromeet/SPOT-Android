package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSeatDialog : BindingBottomSheetDialog<FragmentSelectSeatBottomSheetBinding>(
    R.layout.fragment_select_seat_bottom_sheet,
    FragmentSelectSeatBottomSheetBinding::inflate,
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNextBtn.setOnSingleClickListener {
            binding.tvCompleteBtn.visibility = View.VISIBLE
            binding.tvNextBtn.visibility = View.GONE
            binding.svSelectSeat.visibility = View.GONE
            binding.layoutSeatNumber.visibility = View.VISIBLE
        }
        binding.tvCompleteBtn.setOnSingleClickListener{
            dismiss()
        }
    }
}
