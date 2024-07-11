package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
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
        binding.tvCompleteBtn.setOnSingleClickListener {
            dismiss()
        }

        binding.rvSelectSeat.layoutManager = GridLayoutManager(this.requireContext(), 3)
        binding.rvSelectSeat.adapter = SelectSeatAdapter(getSampleData())
    }

    private fun getSampleData(): List<String> {
        // Replace with your actual data source
        return List(9) { index -> "Seat ${index + 1}" }
    }
}

