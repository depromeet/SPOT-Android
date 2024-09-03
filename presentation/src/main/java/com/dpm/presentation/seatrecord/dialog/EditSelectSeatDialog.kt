package com.dpm.presentation.seatrecord.dialog

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentSelectSeatBottomSheetBinding
import com.dpm.core.base.BindingBottomSheetDialog
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
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
            adapter.setItemSelected(position)
        }
        binding.tvNextBtn.setBackgroundResource(R.drawable.rect_action_enabled_fill_8)
        binding.tvNextBtn.isEnabled = true
        binding.rvSelectSeatZone.adapter = adapter
        viewModel.getStadiumSection(viewModel.editReview.value.stadiumId)
    }

    private fun initEvent() {
        onClickToggleSectionVisibility()
    }

    private fun initObserve() {
        initObserveSection()
    }


    private fun initObserveSection() {
        viewModel.stadiumSectionState.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data.sectionList) {
                        state.data.sectionList.indexOfFirst { it.id == viewModel.editReview.value.sectionId }
                            .takeIf { it != -1 }
                            ?.let { adapter.setItemSelected(it) }
                    }
                    binding.ivSeatAgain.load(state.data.seatChart)
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("구역을 불러오는데 오류가 발생하였습니다.")
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {
                    makeSpotImageAppbar("구역을 불러오는데 오류가 발생하였습니다.")
                }

                else -> {}
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


}