package com.depromeet.presentation.seatReview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
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
    private val viewModel by activityViewModels<ReviewViewModel>()
    private lateinit var adapter: SelectSeatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SelectSeatAdapter()
        binding.rvSelectSeat.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvSelectSeat.adapter = adapter

        binding.tvNextBtn.setOnSingleClickListener {
            binding.tvCompleteBtn.visibility = View.VISIBLE
            binding.tvNextBtn.visibility = View.GONE
            binding.svSelectSeat.visibility = View.GONE
            binding.layoutSeatNumber.visibility = View.VISIBLE
        }

        binding.tvCompleteBtn.setOnSingleClickListener {
            dismiss()
        }

        binding.layoutSeatAgain.setOnSingleClickListener {
            binding.ivSeatAgain.visibility = View.VISIBLE
        }

        binding.tvWhatColumn.setOnSingleClickListener {
            binding.layoutColumnDescription.visibility = View.VISIBLE
        }
        adapter.submitList(getSeatSample())
    }

    private fun getSeatSample(): List<String> {
        return List(9) { index -> "Seat ${index + 1}" }
    }
}
