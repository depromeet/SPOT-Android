package com.depromeet.presentation.seatrecord

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.depromeet.presentation.extension.loadAndCircle
import com.depromeet.presentation.extension.setOnSingleClickListener
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.ProfileEditActivity
import com.depromeet.presentation.seatReview.ReviewActivity
import com.depromeet.presentation.seatrecord.test.RecordListItem
import com.depromeet.presentation.seatrecord.test.SeatRecordAdapter
import com.depromeet.presentation.seatrecord.viewmodel.EditUi
import com.depromeet.presentation.seatrecord.viewmodel.SeatRecordViewModel
import com.depromeet.presentation.util.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
                val teamName = data?.getStringExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_NAME) ?: ""

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
        }
    }

    private fun initObserver() {
        observeDates()
        observeReviews()
        observeEvents()
        observeNoneProfile()
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
            rvSeatRecord.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val scrollBottom = !binding.rvSeatRecord.canScrollVertically(1)
                    if (scrollBottom && !isLoading && viewModel.reviews.value is UiState.Success) {
                        if (!(viewModel.reviews.value as UiState.Success).data.last) {
                            isLoading = true
                            viewModel.loadNextSeatRecords()
                        }
                    }
                }
            })
        }
    }

    private fun setNoneVisibility(isEmpty: Boolean){
        with(binding){
            if (isEmpty) {
                rvSeatRecord.visibility = GONE
                clRecordNone.visibility = VISIBLE
            } else {
                rvSeatRecord.visibility = VISIBLE
                clRecordNone.visibility = GONE
            }
        }
    }

    private fun observeDates() {
        viewModel.date.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setNoneVisibility(false)
                    val reviewState = viewModel.reviews.value
                    if (reviewState is UiState.Success) {
                        adapter.updateItemAt(1, RecordListItem.Date(state.data.yearMonths))
                    } else {
                        adapter.submitList(
                            listOf(
                                RecordListItem.Profile(MySeatRecordResponse.MyProfileResponse()),
                                RecordListItem.Date(state.data.yearMonths),
                                RecordListItem.Record(emptyList())
                            )
                        )
                    }
                    viewModel.getSeatRecords()
                }

                is UiState.Empty -> {
                    setNoneVisibility(isEmpty = true)
                }

                is UiState.Loading -> {
                    //TODO : 여기서 SHIMMER 보여주기
                    Timber.d("test loading")
                }

                is UiState.Failure -> {
                    setNoneVisibility(true)
                    //TODO : 기록 SHIMMER 처리 GONE + 실패 LAYOUT 보여줘야함
                    Timber.d("test fail")
                }

            }
        }
    }

    private fun observeReviews() {
        viewModel.reviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val newProfileItem = RecordListItem.Profile(state.data.profile)
                    val newRecordItem = RecordListItem.Record(state.data.reviews)
                    val newList = adapter.currentList.toMutableList()
                    newList[0] = newProfileItem
                    newList[2] = newRecordItem

                    adapter.submitList(newList)

                    isLoading = false
                    //TODO : 실패 VISIBILITY GONE + 리사이클러뷰 보여줘야함
                }

                is UiState.Loading -> {
                }

                is UiState.Empty -> {
                    //TODO monthadapter에 비어있는 리스트 보내줘야함
                    toast("해당 월에 작성한 글이 없습니다.")
                }

                is UiState.Failure -> {
                    //TODO 실패 보여줘야하고 리사이클러뷰 사라지게?
                }
            }
        }
    }

    private fun observeNoneProfile() {
        viewModel.profile.asLiveData().observe(this) {
            with(binding) {
                val bubbleText = if (it.teamId == null || it.teamId == 0) {
                    "모두를 응원하는 Lv.${it.level} ${it.levelTitle}"
                } else {
                    "${it.teamName}의 Lv.${it.level} ${it.levelTitle}"
                }
                csbvRecordTitle.setText(bubbleText)
                ivRecordProfile.loadAndCircle(it.profileImage)
                tvRecordNickname.text = it.nickname
                tvRecordCount.text = "0"
                "${CalendarUtil.getCurrentYear()}년".also { tvRecordYear.text = it }
            }
        }
    }

    private fun observeEvents() {
        viewModel.deleteClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveConfirmationDialog()
            }
        }

        viewModel.editClickedEvent.asLiveData().observe(this) { state ->
            if (state == EditUi.SEAT_RECORD) {
                moveEditReview()
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
        viewModel.setEditReview(viewModel.editReviewId.value)
        supportFragmentManager.commit {
            replace(
                R.id.fcv_record,
                EditReviewFragment(),
                EditReviewFragment.EDIT_REIVIEW_TAG
            )
            addToBackStack(null)
        }
    }

    private fun navigateToReviewActivity() {
        Intent(this@SeatRecordActivity, ReviewActivity::class.java).apply {
            startActivity(
                this
            )
        }
    }

}