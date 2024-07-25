package com.depromeet.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BindingFragment
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.depromeet.presentation.seatrecord.adapter.TestDetailRecordAdapter
import com.depromeet.presentation.seatrecord.viewmodel.DeleteUi
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatDetailRecordFragment : BindingFragment<ActivitySeatDetailRecordBinding>(
    layoutResId = R.layout.activity_seat_detail_record,
    bindingInflater = ActivitySeatDetailRecordBinding::inflate
) {
    companion object {
        const val SEAT_RECORD_TAG = "seatRecord"
    }

    private val viewModel: SeatRecordViewModel by activityViewModels()
    private lateinit var testDetailRecordAdapter: TestDetailRecordAdapter

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
            fabDetailUp.setOnClickListener {
                rvDetailRecord.smoothScrollToPosition(0)
            }
        }
    }

    private fun initObserver() {
        viewModel.reviews.asLiveData().observe(viewLifecycleOwner) { state ->
            if (state is UiState.Success) {
                testDetailRecordAdapter.submitList(state.data.reviews)
            }
        }

        viewModel.deleteClickedEvent.asLiveData().observe(viewLifecycleOwner) { state ->
            if (state == DeleteUi.SEAT_DETAIL) moveConfirmationDialog()
        }
    }

    private fun setDetailRecordAdapter() {
        testDetailRecordAdapter = TestDetailRecordAdapter(
            (viewModel.reviews.value as UiState.Success).data.profile
        )

        binding.rvDetailRecord.adapter = testDetailRecordAdapter

        testDetailRecordAdapter.itemMoreClickListener =
            object : TestDetailRecordAdapter.OnDetailItemClickListener {
                override fun onItemMoreClickListener(item: MySeatRecordResponse.ReviewResponse) {
                    viewModel.setEditReviewId(item.id)
                    RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                        .show(parentFragmentManager, RecordEditDialog.TAG)
                }
            }
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .show(parentFragmentManager, ConfirmDeleteDialog.TAG)
    }
}