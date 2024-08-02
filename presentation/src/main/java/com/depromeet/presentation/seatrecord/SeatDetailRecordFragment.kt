package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BindingFragment
import com.depromeet.core.state.UiState
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.depromeet.presentation.seatrecord.adapter.DetailRecordAdapter
import com.depromeet.presentation.seatrecord.viewmodel.EditUi
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SeatDetailRecordFragment : BindingFragment<ActivitySeatDetailRecordBinding>(
    layoutResId = R.layout.activity_seat_detail_record,
    bindingInflater = ActivitySeatDetailRecordBinding::inflate
) {
    companion object {
        const val SEAT_RECORD_TAG = "seatRecord"
    }

    private val viewModel: SeatRecordViewModel by activityViewModels()
    private lateinit var detailRecordAdapter: DetailRecordAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
        initObserver()

    }

    private fun initView() {
        setDetailRecordAdapter()
    }

    private fun initEvent() {
        with(binding) {
            detailRecordAppbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            detailRecordAppbar.setMenuOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun initObserver() {
        viewModel.reviews.asLiveData().observe(viewLifecycleOwner) { state ->
            if (state is UiState.Success) {
                detailRecordAdapter.submitList(state.data.reviews)
                Timber.d("test 여기 불러와지나?")
            }
        }

        viewModel.deleteClickedEvent.asLiveData().observe(viewLifecycleOwner) { state ->
            if (state == EditUi.SEAT_DETAIL) {
                moveConfirmationDialog()
            }
        }

        viewModel.editClickedEvent.asLiveData().observe(viewLifecycleOwner) { state ->
            if (state == EditUi.SEAT_DETAIL) {
                moveEditReview()
            }
        }
    }

    private fun setDetailRecordAdapter() {
        detailRecordAdapter = DetailRecordAdapter(
            (viewModel.reviews.value as UiState.Success).data.profile,
            moreClick = { id ->
                viewModel.setEditReviewId(id)
                RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                    .show(parentFragmentManager, RecordEditDialog.TAG)
            }
        )

        binding.rvDetailRecord.adapter = detailRecordAdapter

        val position =
            (viewModel.reviews.value as UiState.Success).data.reviews.indexOfFirst { it.id == viewModel.clickedReviewId.value }
        binding.rvDetailRecord.scrollToPosition(position)
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .show(parentFragmentManager, ConfirmDeleteDialog.TAG)
    }

    private fun moveEditReview() {

    }
}