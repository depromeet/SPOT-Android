package com.dpm.presentation.seatrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatDetailRecordBinding
import com.dpm.core.base.BindingFragment
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.seatrecord.adapter.DetailRecordAdapter
import com.dpm.presentation.seatrecord.dialog.ConfirmDeleteDialog
import com.dpm.presentation.seatrecord.dialog.RecordEditDialog
import com.dpm.presentation.seatrecord.viewmodel.EditUi
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.util.KakaoUtils
import com.dpm.presentation.util.Utils
import com.dpm.presentation.util.seatFeed
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
                setStatusBarColor(
                    window,
                    com.depromeet.designsystem.R.color.color_background_tertiary
                )
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
                    if (viewModel.currentReviewState.value == SeatRecordViewModel.ReviewType.SEAT_REVIEW) {
                        detailRecordAdapter.submitList(state.data.reviews)
                        isLoading = false
                    }
                }

                else -> {}
            }

        }

        viewModel.intuitiveReviews.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (viewModel.currentReviewState.value == SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW) {
                        detailRecordAdapter.submitList(state.data.reviews)
                        isLoading = false
                    }
                }

                else -> {}
            }
        }

        viewModel.seatDate.asLiveData().observe(viewLifecycleOwner) { state ->
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
                viewModel.updateLike(id)
            },
            scrapClick = { id ->
                viewModel.updateScrap(id)
            },
            shareClick = { review, position ->
                shareLink(review, position)
            }
        )

        with(binding) {
            rvDetailRecord.adapter = detailRecordAdapter

            val position = when (viewModel.currentReviewState.value) {
                SeatRecordViewModel.ReviewType.SEAT_REVIEW -> {
                    (viewModel.seatReviews.value as UiState.Success).data.reviews.indexOfFirst { it.id == viewModel.clickedReviewId.value }
                }

                SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW -> {
                    (viewModel.intuitiveReviews.value as UiState.Success).data.reviews.indexOfFirst { it.id == viewModel.clickedReviewId.value }
                }
            }

            rvDetailRecord.scrollToPosition(position)
            rvDetailRecord.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val scrollBottom = !rvDetailRecord.canScrollVertically(1)
                    val hasNextPage = when (viewModel.currentReviewState.value) {
                        SeatRecordViewModel.ReviewType.SEAT_REVIEW -> {
                            (viewModel.seatReviews.value as UiState.Success).data.hasNext
                        }

                        SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW -> {
                            (viewModel.intuitiveReviews.value as UiState.Success).data.hasNext
                        }
                    }
                    if (scrollBottom && hasNextPage && !isLoading) {
                        isLoading = true
                        when (viewModel.currentReviewState.value) {
                            SeatRecordViewModel.ReviewType.SEAT_REVIEW -> {
                                viewModel.getNextSeatReviews()
                            }

                            SeatRecordViewModel.ReviewType.INTUITIVE_REVIEW -> {
                                viewModel.getNextIntuitiveReviews()
                            }
                        }
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

    private fun shareLink(data: ResponseMySeatRecord.ReviewResponse, imagePosition: Int) {
        KakaoUtils().share(
            requireContext(),
            seatFeed(
                title = data.kakaoShareSeatFeedTitle(),
                description = "출처 : ${data.member.nickname}",
                imageUrl = data.images[imagePosition].url,
                queryParams = mapOf(
                    SchemeKey.STADIUM_ID to data.stadiumId.toString(),
                    SchemeKey.BLOCK_CODE to data.blockCode
                )
            ),
            onSuccess = { sharingIntent ->
                requireContext().startActivity(sharingIntent)
            },
            onFailure = {
                Timber.d("링크 공유 실패 : ${it.message}")
            }
        )
    }

}