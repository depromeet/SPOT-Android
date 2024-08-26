package com.dpm.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dpm.core.base.BindingFragment
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.dpm.presentation.seatrecord.adapter.DetailRecordAdapter
import com.dpm.presentation.seatrecord.dialog.ConfirmDeleteDialog
import com.dpm.presentation.seatrecord.dialog.RecordEditDialog
import com.dpm.presentation.seatrecord.viewmodel.EditUi
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.util.Utils
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
    private lateinit var detailRecordAdapter: DetailRecordAdapter
    private var isLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()
        initObserver()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utils(requireContext()).apply {
            requireActivity().apply {
                setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_tertiary)
                setBlackSystemBarIconColor(window)
            }
        }
    }

    private fun initView() {
        initViewStatusBar()
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
        viewModel.seatReviews.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    detailRecordAdapter.submitList(state.data.reviews)
                    isLoading = false
                }

                is UiState.Failure -> {}
                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }

        }

        viewModel.seatDate.asLiveData().observe(viewLifecycleOwner){ state ->
            if (state is UiState.Empty)
                detailRecordAdapter.submitList(emptyList())
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

    private fun initViewStatusBar() {
        Utils(requireContext()).apply {
            requireActivity().apply {
                setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_white)
                setBlackSystemBarIconColor(window)
            }
        }
    }

    private fun setDetailRecordAdapter() {
        detailRecordAdapter = DetailRecordAdapter(
            moreClick = { id ->
                viewModel.setEditReviewId(id)
                RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                    .show(parentFragmentManager, RecordEditDialog.TAG)
            },
            likeClick = { id ->
                //TODO 좋아요
            },
            scrapClick = { id ->
                //TODO 스크랩
            },
            shareClick = { review ->
                //TODO 공유
            }

        )

        with(binding) {
            rvDetailRecord.adapter = detailRecordAdapter

            val position =
                (viewModel.seatReviews.value as UiState.Success).data.reviews.indexOfFirst { it.id == viewModel.clickedReviewId.value }
            rvDetailRecord.scrollToPosition(position)
            rvDetailRecord.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val scrollBottom = !rvDetailRecord.canScrollVertically(1)
                    val hasNextPage = (viewModel.seatReviews.value as UiState.Success).data.hasNext
                    if (scrollBottom && hasNextPage && !isLoading) {
                        isLoading = true
                        viewModel.getNextSeatReviews()
                    }
                }
            })
        }

    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .show(parentFragmentManager, ConfirmDeleteDialog.TAG)
    }

    private fun moveEditReview() {
        makeSpotImageAppbar("수정은 추후에 열릴 예정입니다!")
    }

    private fun makeSpotImageAppbar(message: String) {
        SpotImageSnackBar.make(
            view = binding.root,
            message = message,
            messageColor = com.depromeet.designsystem.R.color.color_foreground_white,
            icon = com.depromeet.designsystem.R.drawable.ic_alert_circle,
            iconColor = com.depromeet.designsystem.R.color.color_error_secondary,
            marginBottom = 20
        ).show()
    }

}