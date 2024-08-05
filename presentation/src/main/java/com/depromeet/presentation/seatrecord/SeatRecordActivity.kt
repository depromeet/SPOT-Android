package com.depromeet.presentation.seatrecord

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.designsystem.SpotSpinner
import com.depromeet.designsystem.extension.dpToPx
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivitySeatRecordBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.ProfileEditActivity
import com.depromeet.presentation.seatReview.ReviewActivity
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.seatrecord.uiMapper.MonthUiData
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
        private const val START_SPACING_DP = 20
        private const val BETWEEN_SPACING_DP = 8
        const val PROFILE_NAME = "profile_name"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_CHEER_TEAM = "profile_cheer_team"
    }

    private lateinit var dateMonthAdapter: DateMonthAdapter
    private lateinit var monthRecordAdapter: MonthRecordAdapter
    private val viewModel: SeatRecordViewModel by viewModels()
    private var isLoading: Boolean = false

    private val editProfileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val nickname = data?.getStringExtra(ProfileEditActivity.PROFILE_NAME) ?: ""
                val profileImage = data?.getStringExtra(ProfileEditActivity.PROFILE_IMAGE) ?: ""
                val teamId = data?.getIntExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_ID, 0) ?: 0
                val teamName = data?.getStringExtra(ProfileEditActivity.PROFILE_CHEER_TEAM_NAME)

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
        initMonthAdapter()
        setRefreshClicked()
        initReviewList()
        viewModel.getReviewDate()
    }

    private fun initEvent() {
        with(binding) {
            recordSpotAppbar.setNavigationOnClickListener {
                finish()
            }
            fabRecordUp.setOnClickListener {
                ssvRecord.smoothScrollTo(0, 0)
            }
            fabRecordPlus.setOnClickListener {
                Intent(this@SeatRecordActivity, ReviewActivity::class.java).apply {
                    startActivity(
                        this
                    )
                }
            }
            btRecordFailResfresh.setOnClickListener {
                viewModel.getSeatRecords()
            }
            ivRecordEdit.setOnClickListener {
                navigateToProfileEditActivity()
            }
            btRecordWriteRecord.setOnClickListener {
                Intent(this@SeatRecordActivity, ReviewActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun initObserver() {
        observeDates()
        observeReviews()
        observeEvents()
    }

    private fun setRefreshClicked() {
        binding.btRecordFailResfresh.setOnClickListener {
            if (viewModel.date.value is UiState.Success) {
                viewModel.getSeatRecords()
            } else {
                viewModel.getReviewDate()
            }
        }
    }

    private fun observeDates() {
        viewModel.selectedYear.asLiveData().observe(this) {
            viewModel.initMonths()
        }
        viewModel.months.asLiveData().observe(this) {
            dateMonthAdapter.submitList(it)
            viewModel.getSeatRecords()
        }
        viewModel.date.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setReviewsVisibility(isExist = true)
                    val year = state.data.yearMonths.map { it.year }
                    initYearSpinner(year)
                }

                is UiState.Empty -> {
                    setReviewsVisibility(isExist = false)
                    binding.shimmerRecord.visibility = GONE
                }

                is UiState.Loading -> {
                    setReviewsVisibility(isExist = true)
                }

                is UiState.Failure -> {
                    setReviewsVisibility(isExist = true)
                    binding.clHomeFail.visibility = VISIBLE
                    binding.rvRecordMonthDetail.visibility = GONE
                }

            }
        }
    }

    private fun observeReviews() {
        viewModel.reviews.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    setProfile(state.data.profile)
                    updateReviewList(state.data.reviews)
                    binding.clHomeFail.visibility = GONE
                    binding.rvRecordMonthDetail.visibility = VISIBLE
                    isLoading = false
                    setShimmer(false)
                }

                is UiState.Loading -> {
                    setShimmer(true)
                }

                is UiState.Empty -> {
                    monthRecordAdapter.submitList(emptyList())
                    toast("해당 월에 작성한 글이 없습니다.")
                }

                is UiState.Failure -> {
                    setShimmer(false)
                    binding.clHomeFail.visibility = VISIBLE
                    binding.rvRecordMonthDetail.visibility = GONE
                }
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

    private fun setProfile(data: MySeatRecordResponse.MyProfileResponse) {
        with(binding) {
            if (data.teamId != null) {
                "${data.teamName}의 Lv.${data.level} ${data.levelTitle}".also {
                    csbvRecordTitle.setText(
                        it
                    )
                }
            } else {
                "모두를 응원하는 Lv.${data.level} ${data.levelTitle}".also { csbvRecordTitle.setText(it) }
            }
            tvRecordNickname.text = data.nickname
            tvRecordCount.text = data.reviewCount.toString()
            ivRecordProfile.load(data.profileImage) {
                transformations(CircleCropTransformation())
                error(com.depromeet.designsystem.R.drawable.ic_default_profile)
            }
        }
    }

    private fun createColoredLevelString(data: MySeatRecordResponse.MyProfileResponse): SpannableString {
        /** 커스텀뷰에 또 spannable을 적용해야한다니... */
        val fullText = if (data.teamId != null) {
            "${data.teamName}의 Lv.${data.level} ${data.levelTitle}"
        } else {
            "모두를 응원하는 Lv.${data.level} ${data.levelTitle}"
        }
        val spannableString = SpannableString(fullText)

        val levelStartIndex = fullText.indexOf(data.level.toString())
        val levelEndIndex = levelStartIndex + data.level.toString().length

        spannableString.setSpan(
            ForegroundColorSpan(getColor(com.depromeet.designsystem.R.color.color_action_enabled)),
            levelStartIndex,
            levelEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    private fun initYearSpinner(years: List<Int>) {
        viewModel.setSelectedYear(years[0])
        val yearList = years.map { "${it}년" }

        val adapter = SpotSpinner(
            this,
            R.layout.item_custom_month_spinner_view,
            R.layout.item_custom_month_spinner_dropdown,
            yearList,
            true,
            R.color.gray900
        )
        with(binding.spinnerRecordYear) {
            this.adapter = adapter
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        adapter.setSelectedItemPosition(position)
                        val selectedYear = yearList[position].filter { it.isDigit() }.toInt()
                        viewModel.setSelectedYear(selectedYear)
                        binding.ssvRecord.smoothScrollTo(0, 0)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun initMonthAdapter() {
        dateMonthAdapter = DateMonthAdapter()
        binding.rvRecordMonth.adapter = dateMonthAdapter
        binding.rvRecordMonth.addItemDecoration(
            LinearSpacingItemDecoration(
                START_SPACING_DP.dpToPx(this),
                BETWEEN_SPACING_DP.dpToPx(this)
            )
        )
        dateMonthAdapter.itemMonthClickListener =
            object : DateMonthAdapter.OnItemMonthClickListener {
                override fun onItemMonthClick(item: MonthUiData) {
                    val selectedMonth = item.month
                    viewModel.setSelectedMonth(selectedMonth)
                    binding.ssvRecord.smoothScrollTo(0, 0)
                }
            }
    }

    private fun setReviewsVisibility(isExist: Boolean) {
        if (!isExist) {
            with(binding) {
                "${CalendarUtil.getCurrentYear()}년".also { tvRecordYear.text = it }
                clRecordNone.visibility = VISIBLE
                clRecordStickyHeader.visibility = GONE
                rvRecordMonthDetail.visibility = GONE
            }
        } else {
            with(binding) {
                clRecordNone.visibility = GONE
                clRecordStickyHeader.visibility = VISIBLE
                rvRecordMonthDetail.visibility = VISIBLE

                ssvRecord.header = binding.clRecordStickyHeader
            }
        }
    }

    private fun initReviewList(){
        monthRecordAdapter = MonthRecordAdapter()
        binding.rvRecordMonthDetail.adapter = monthRecordAdapter
        binding.rvRecordMonthDetail.itemAnimator = null

        monthRecordAdapter.itemRecordClickListener =
            object : MonthRecordAdapter.OnItemRecordClickListener {

                override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {
                    viewModel.setClickedReviewId(item.id)
                    supportFragmentManager.commit {
                        replace(
                            R.id.fcv_record,
                            SeatDetailRecordFragment(),
                            SeatDetailRecordFragment.SEAT_RECORD_TAG
                        )
                        addToBackStack(null)
                    }
                }

                override fun onMoreRecordClick(reviewId: Int) {
                    viewModel.setEditReviewId(reviewId)
                    RecordEditDialog.newInstance(SEAT_RECORD_TAG)
                        .apply { show(supportFragmentManager, this.tag) }
                }

            }

        binding.rvRecordMonthDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val scrollBottom = !binding.rvRecordMonthDetail.canScrollVertically(1)

                val layoutManager = binding.rvRecordMonthDetail.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val itemCount = layoutManager.itemCount - 1

                Timber.d("test 마지막 포지션 : $lastVisibleItemPosition 아이템 : $itemCount")

//                val hasNextPage = (viewModel.reviews.value as? UiState.Success)?.data?.last == false
//                if (lastVisibleItemPosition == itemCount && hasNextPage && !isLoading) {
//                    isLoading = true
//                    viewModel.loadNextSeatRecords()
//                }
            }
        })
    }

    private fun updateReviewList(reviews: List<MySeatRecordResponse.ReviewResponse>) {
        val groupList =
            reviews.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        monthRecordAdapter.submitList(groupList)
    }

    private fun navigateToProfileEditActivity() {
        val currentState = viewModel.reviews.value
        if (currentState is UiState.Success) {
            editProfileLauncher.launch(Intent(this, ProfileEditActivity::class.java).apply {
                with(currentState.data) {
                    putExtra(PROFILE_NAME, this.profile.nickname)
                    putExtra(PROFILE_IMAGE, this.profile.profileImage)
                    putExtra(PROFILE_CHEER_TEAM, this.profile.teamId)
                }
            })
        }
    }

    private fun moveConfirmationDialog() {
        ConfirmDeleteDialog.newInstance(SEAT_RECORD_TAG)
            .apply { show(supportFragmentManager, this.tag) }
    }


    private fun moveEditReview() {

    }

    private fun setShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            shimmerRecord.startShimmer()
            shimmerRecord.visibility = VISIBLE
            shimmerRecordProfile.visibility = VISIBLE
        } else {
            shimmerRecord.stopShimmer()
            shimmerRecord.visibility = GONE
            shimmerRecordProfile.visibility = GONE
        }
    }
}