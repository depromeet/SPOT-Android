package com.dpm.presentation.seatrecord

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.designsystem.SpotImageSnackBar
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.presentation.extension.loadAndCircleProfile
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.home.ProfileEditActivity
import com.dpm.presentation.seatrecord.adapter.RecordListItem
import com.dpm.presentation.seatrecord.adapter.SeatRecordAdapter
import com.dpm.presentation.seatrecord.dialog.ConfirmDeleteDialog
import com.dpm.presentation.seatrecord.dialog.RecordEditDialog
import com.dpm.presentation.seatrecord.viewmodel.EditUi
import com.dpm.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.dpm.presentation.seatreview.ReviewActivity
import com.dpm.presentation.util.CalendarUtil
import com.dpm.presentation.util.Utils
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatRecordActivity : BaseActivity<ActivitySeatRecordBinding>(
    ActivitySeatRecordBinding::inflate
) {
    companion object {
        const val SEAT_RECORD_TAG = "seatRecord"
        const val PROFILE_NAME = "profile_name"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_CHEER_TEAM = "profile_cheer_team"
    }

    private lateinit var adapter: SeatRecordAdapter
    private val viewModel: SeatRecordViewModel by viewModels()
    private var isLoading: Boolean = false


    private val editProfileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val nickname = data?.getStringExtra(ProfileEditActivity.PROFILE_NAME) ?: ""
                val profileImage = data?.getStringExtra(ProfileEditActivity.PROFILE_IMAGE) ?: ""
                val teamId = data?.getIntExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_ID, 0) ?: 0
                val teamName =
                    data?.getStringExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_NAME) ?: ""

                viewModel.updateProfile(nickname, profileImage, teamId, teamName)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        initViewStatusBar()
        viewModel.getReviewDate()
        viewModel.getLocalProfile()
        initRecordAdapter()
    }

    private fun initEvent() {
        with(binding) {
            recordSpotAppbar.setNavigationOnClickListener {
                finish()
            }
            fabRecordUp.setOnClickListener {
                rvSeatRecord.smoothScrollToPosition(0)
            }
            fabRecordPlus.setOnClickListener {
                navigateToReviewActivity()
            }

            /** 게시물 없을 때 **/
            ivRecordProfile.setOnSingleClickListener { navigateToProfileEditActivity() }
            ivRecordEdit.setOnSingleClickListener { navigateToProfileEditActivity() }
            btRecordWriteRecord.setOnSingleClickListener { navigateToReviewActivity() }

            /** 로딩 실패 **/
            btRecordFailRefresh.setOnSingleClickListener {
                if (viewModel.reviews.value is UiState.Failure || viewModel.date.value is UiState.Failure) {
                    makeSpotImageAppbar("리뷰를 불러오는데 실패하였습니다.")
                }
                viewModel.getReviewDate()
            }
        }
    }

    private fun initObserver() {
        observeDates()
        observeReviews()
        observeEvents()
        observeProfile()
    }

    private fun initViewStatusBar() {
        Utils(this).apply {
            setStatusBarColor(window, com.depromeet.designsystem.R.color.color_background_tertiary)
            setBlackSystemBarIconColor(window)
        }
    }

    private fun initRecordAdapter() {
        adapter = SeatRecordAdapter(
            reviewClick = {
                viewModel.setClickedReviewId(it.id)
                supportFragmentManager.commit {
                    replace(
                        R.id.fcv_record,
                        SeatDetailRecordFragment(),
                        SeatDetailRecordFragment.SEAT_RECORD_TAG
                    )
                    addToBackStack(null)
                }
            },
            monthClick = { month ->
                viewModel.setSelectedMonth(month)
                binding.rvSeatRecord.smoothScrollToPosition(0)
            },
            yearClick = { year ->
                viewModel.setSelectedYear(year)
                binding.rvSeatRecord.smoothScrollToPosition(0)
            },
            profileEditClick = {
                navigateToProfileEditActivity()
            },
            reviewEditClick = { reviewId ->
                viewModel.setEditReviewId(reviewId)
                RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                    .apply { show(supportFragmentManager, this.tag) }
            }
        )

        with(binding) {
            rvSeatRecord.adapter = adapter
            /*
            rvSeatRecord.addItemDecoration(
                HeaderItemDecoration(
                    binding.rvSeatRecord,
                    shouldFadeOutHeader = true,
                    isHeader = { position -> (rvSeatRecord.adapter as SeatRecordAdapter).isHeader(position) }
                )
            )
             */

            rvSeatRecord.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val scrollTop = !binding.rvSeatRecord.canScrollVertically(-1)
                    val scrollBottom = !binding.rvSeatRecord.canScrollVertically(1)
                    if (scrollBottom && !isLoading && viewModel.reviews.value is UiState.Success) {
                        if ((viewModel.reviews.value as UiState.Success).data.hasNext) {
                            isLoading = true
                            viewModel.loadNextSeatRecords()
                        }
                    }
                    binding.fabRecordUp.visibility = if (scrollTop) GONE else VISIBLE
                }
            })
        }
    }

    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) VISIBLE else GONE
    }

    private fun setErrorVisibility(recordErrorType: RecordErrorType) {
        with(binding) {
            rvSeatRecord.setVisible(recordErrorType == RecordErrorType.NONE)
            clRecordError.setVisible(recordErrorType != RecordErrorType.NONE)
            clRecordEmpty.setVisible(recordErrorType == RecordErrorType.EMPTY)
            clRecordFail.setVisible(recordErrorType == RecordErrorType.FAIL)
            fabRecordUp.visibility = GONE
        }
    }


    private fun observeDates() {
        viewModel.date.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setErrorVisibility(RecordErrorType.NONE)
                    stopDateShimmer()
                    val reviewState = viewModel.reviews.value
                    if (reviewState is UiState.Success) {
                        adapter.updateItemAt(1, RecordListItem.Date(state.data.yearMonths))
                    } else {
                        adapter.submitList(
                            listOf(
                                RecordListItem.Profile(ResponseMySeatRecord.MyProfileResponse()),
                                RecordListItem.Date(state.data.yearMonths),
                                RecordListItem.Record(emptyList())
                            )
                        )
                    }
                    viewModel.getSeatRecords()
                }

                is UiState.Empty -> {
                    setErrorVisibility(RecordErrorType.EMPTY)
                    stopAllShimmer()
                }

                is UiState.Loading -> {
                    startAllShimmer()
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("리뷰를 불러오는데 실패하였습니다.")
                    setErrorVisibility(RecordErrorType.FAIL)
                    stopAllShimmer()
                }

            }
        }
    }

    private fun observeReviews() {
        viewModel.reviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    stopReviewShimmer()
                    val newProfileItem = RecordListItem.Profile(state.data.profile)
                    val newDateItem =
                        RecordListItem.Date((viewModel.date.value as UiState.Success).data.yearMonths)
                    val newRecordItem = RecordListItem.Record(state.data.reviews)
                    val newList = adapter.currentList.toMutableList()
                    newList[0] = newProfileItem
                    newList[1] = newDateItem
                    newList[2] = newRecordItem

                    adapter.submitList(newList)

                    isLoading = false
                    setErrorVisibility(RecordErrorType.NONE)
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                    makeSpotImageAppbar("해당 날짜에 작성한 글이 없습니다.")
                }

                is UiState.Failure -> {
                    makeSpotImageAppbar("리뷰를 불러오는데 실패하였습니다.")
                    setErrorVisibility(RecordErrorType.FAIL)
                    stopReviewShimmer()
                }
            }
        }
    }

    private fun observeProfile() {
        viewModel.profile.asLiveData().observe(this) {
            profileNone(it)
            //TODO : 리사이클러뷰 프로필
        }
    }

    private fun profileNone(profile: ResponseMySeatRecord.MyProfileResponse) {
        with(binding) {
            if (profile.teamId != null && profile.teamId != 0) {
                csbvRecordTitle.setTextPart(
                    "${profile.teamName}의 Lv.", profile.level,
                    " ${profile.levelTitle}"
                )

            } else {
                csbvRecordTitle.setTextPart(
                    "모두를 응원하는 Lv.", profile.level, " ${profile.levelTitle}"
                )
            }
            ivRecordProfile.loadAndCircleProfile(profile.profileImage)
            tvRecordNickname.text = profile.nickname
            tvRecordCount.text = "0"
            "${CalendarUtil.getCurrentYear()}년".also { tvRecordYear.text = it }
        }
    }


    private fun observeEvents() {
        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveConfirmationDialog()
                viewModel.cancelDeleteEvent()
            }
        }

        viewModel.editClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveEditReview()
                viewModel.cancelEditEvent()
            }
        }
    }

    private fun navigateToProfileEditActivity() {
        editProfileLauncher.launch(Intent(this, ProfileEditActivity::class.java).apply {
            putExtra(PROFILE_NAME, viewModel.profile.value.nickname)
            putExtra(PROFILE_IMAGE, viewModel.profile.value.profileImage)
            putExtra(PROFILE_CHEER_TEAM, viewModel.profile.value.teamId)
        })
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }


    private fun moveEditReview() {
        makeSpotImageAppbar("수정은 추후에 열릴 예정입니다!")
//        viewModel.setEditReview(viewModel.editReviewId.value)
//        supportFragmentManager.commit {
//            replace(
//                R.id.fcv_record,
//                EditReviewFragment(),
//                EditReviewFragment.EDIT_REIVIEW_TAG
//            )
//            addToBackStack(null)
//        }
    }

    private fun navigateToReviewActivity() {
        Intent(this@SeatRecordActivity, ReviewActivity::class.java).apply {
            startActivity(
                this
            )
        }
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

    private fun startShimmerWithVisibility(shimmerView: ShimmerFrameLayout) {
        shimmerView.apply {
            visibility = VISIBLE
            startShimmer()
        }
    }

    private fun stopShimmerWithVisibility(shimmerView: ShimmerFrameLayout) {
        shimmerView.apply {
            stopShimmer()
            visibility = INVISIBLE
        }
    }

    private fun startAllShimmer() {
        with(binding) {
            startShimmerWithVisibility(shimmerProfile)
            startShimmerWithVisibility(shimmerDate)
            startShimmerWithVisibility(shimmerReview)
        }
    }

    private fun stopAllShimmer() {
        with(binding) {
            stopShimmerWithVisibility(shimmerProfile)
            stopShimmerWithVisibility(shimmerDate)
            stopShimmerWithVisibility(shimmerReview)
        }
    }

    private fun stopDateShimmer() {
        stopShimmerWithVisibility(binding.shimmerDate)
    }

    private fun stopReviewShimmer() {
        stopShimmerWithVisibility(binding.shimmerProfile)
        stopShimmerWithVisibility(binding.shimmerReview)
    }

    enum class RecordErrorType {
        EMPTY,
        FAIL,
        NONE
    }
}