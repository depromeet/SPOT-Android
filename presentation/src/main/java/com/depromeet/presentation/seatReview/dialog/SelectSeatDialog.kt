package com.depromeet.presentation.seatReview.dialog

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.core.base.BindingBottomSheetDialog
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.seatReview.ReviewViewModel
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
        setupButtons()
        adapter.submitList(getSeatSample())
    }

    private fun setupRecyclerView() {
        adapter = SelectSeatAdapter { position ->
            adapter.setItemSelected(position)
            updateNextButtonState()
        }

        binding.rvSelectSeat.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@SelectSeatDialog.adapter
        }
    }

    private fun setupButtons() {
        with(binding) {
            tvNextBtn.setOnSingleClickListener {
                tvCompleteBtn.visibility = View.VISIBLE
                tvNextBtn.visibility = View.GONE
                svSelectSeat.visibility = View.GONE
                tvSelectSeatLine.visibility = View.INVISIBLE
                tvSelectNumberLine.visibility = View.VISIBLE
                layoutSeatNumber.visibility = View.VISIBLE
            }

            tvCompleteBtn.setOnSingleClickListener {
                dismiss()
            }

            layoutSeatAgain.setOnSingleClickListener {
                ivSeatAgain.isVisible = !ivSeatAgain.isVisible
            }

            tvWhatColumn.setOnSingleClickListener {
                layoutColumnDescription.visibility = View.VISIBLE
            }
        }
    }

    private fun getSeatSample(): List<SeatInfo> {
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

    private fun updateNextButtonState() {
        with(binding.tvNextBtn) {
            setBackgroundResource(R.drawable.rect_gray900_fill_6)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            isEnabled = true
        }
    }
}
