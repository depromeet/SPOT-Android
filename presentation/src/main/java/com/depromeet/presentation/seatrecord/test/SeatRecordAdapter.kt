package com.depromeet.presentation.seatrecord.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.designsystem.SpotTestSpinner
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.presentation.databinding.ItemRecordDateBinding
import com.depromeet.presentation.databinding.ItemRecordProfileBinding
import com.depromeet.presentation.databinding.ItemRecordReviewBinding
import com.depromeet.presentation.extension.loadAndCircle
import com.depromeet.presentation.seatrecord.adapter.DateMonthAdapter
import com.depromeet.presentation.seatrecord.adapter.LinearSpacingItemDecoration
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.util.CalendarUtil
import timber.log.Timber

enum class RecordViewType {
    PROFILE_ITEM,
    DATE_ITEM,
    RECORD_ITEM
}

sealed class RecordListItem {
    data class Profile(val profile: MySeatRecordResponse.MyProfileResponse) : RecordListItem()
    data class Date(val reviewDates: List<ReviewDateResponse.YearMonths>) : RecordListItem()
    data class Record(val reviews: List<MySeatRecordResponse.ReviewResponse>) : RecordListItem()
}

class SeatRecordAdapter(
    private val reviewClick: (MySeatRecordResponse.ReviewResponse) -> Unit,
    private val monthClick: (Int) -> Unit,
    private val yearClick: (Int) -> Unit,
    private val profileEditClick: (MySeatRecordResponse.MyProfileResponse) -> Unit,
) : ListAdapter<RecordListItem, RecyclerView.ViewHolder>(
    RecordItemDiffCallback()
) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecordListItem.Profile -> RecordViewType.PROFILE_ITEM.ordinal
            is RecordListItem.Date -> RecordViewType.DATE_ITEM.ordinal
            is RecordListItem.Record -> RecordViewType.RECORD_ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecordViewType.PROFILE_ITEM.ordinal -> {
                RecordProfileViewHolder(
                    ItemRecordProfileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    editClick = profileEditClick
                )
            }

            RecordViewType.DATE_ITEM.ordinal -> {
                RecordDateViewHolder(
                    ItemRecordDateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    monthClick = monthClick,
                    yearClick = yearClick
                )

            }

            RecordViewType.RECORD_ITEM.ordinal -> {
                RecordReviewViewHolder(
                    ItemRecordReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    reviewClick = reviewClick,

                    )
            }

            else -> throw IllegalArgumentException("옳지 않은 뷰타입")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecordProfileViewHolder -> {
                val item = getItem(position) as? RecordListItem.Profile
                item?.let {
                    holder.bind(it.profile)
                }
            }

            is RecordDateViewHolder -> {
                val item = getItem(position) as? RecordListItem.Date
                item?.let {
                    holder.bind(it.reviewDates)
                }
            }

            is RecordReviewViewHolder -> {
                val item = getItem(position) as? RecordListItem.Record
                item?.let {
                    holder.bind(it.reviews)
                }
            }
        }
    }
}

class RecordItemDiffCallback : DiffUtil.ItemCallback<RecordListItem>() {
    override fun areItemsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
        return when {
            oldItem is RecordListItem.Profile && newItem is RecordListItem.Profile ->
                oldItem == newItem

            oldItem is RecordListItem.Date && newItem is RecordListItem.Date ->
                oldItem == newItem

            oldItem is RecordListItem.Record && newItem is RecordListItem.Record ->
                oldItem.reviews.map { it.id } == newItem.reviews.map { it.id }

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
        return oldItem == newItem
    }
}

class RecordProfileViewHolder(
    internal val binding: ItemRecordProfileBinding,
    private val editClick: (MySeatRecordResponse.MyProfileResponse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: MySeatRecordResponse.MyProfileResponse) = with(binding) {
        initEvent(data)
        initView(data)
    }

    private fun initView(data: MySeatRecordResponse.MyProfileResponse) = with(binding) {
        if (data.teamId != null) {
            "${data.teamName}의 Lv.${data.level} ${data.levelTitle}".also {
                csbvRecordTitle.setText(
                    it
                )
            }
        } else {
            "모두를 응원하는 Lv.${data.level} ${data.levelTitle}".also {
                csbvRecordTitle.setText(
                    it
                )
            }
        }
        tvRecordNickname.text = data.nickname
        tvRecordCount.text = data.reviewCount.toString()
        ivRecordProfile.loadAndCircle(data.profileImage)
    }

    private fun initEvent(data: MySeatRecordResponse.MyProfileResponse) = with(binding) {
        ivRecordProfile.setOnClickListener { editClick(data) }
        ivRecordEdit.setOnClickListener { editClick(data) }
    }
}


class RecordDateViewHolder(
    internal val binding: ItemRecordDateBinding,
    private val yearClick: (Int) -> Unit,
    private val monthClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val START_SPACING_DP = 20
        private const val BETWEEN_SPACING_DP = 8
    }

    private lateinit var dateMonthAdapter: DateMonthAdapter

    fun bind(data: List<ReviewDateResponse.YearMonths>) {
        Timber.d("test bind 됨")
        initMonthAdapter()
        initYearSpinner(data)
        dateMonthAdapter.submitList(data.first { it.isClicked }.months)
    }


    private fun initYearSpinner(data: List<ReviewDateResponse.YearMonths>) {
        val years = data.map { it.year }
        val yearList = years.map { "${it}년" }

        val adapter = SpotTestSpinner(
            yearList,
            selectedTextView = {},
            dropDownTextView = {}
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
                        yearClick(selectedYear)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun initMonthAdapter() {
        dateMonthAdapter = DateMonthAdapter(
            monthClick = { month ->
                monthClick(month)
            }
        )
        binding.rvRecordMonth.adapter = dateMonthAdapter
        binding.rvRecordMonth.addItemDecoration(
            LinearSpacingItemDecoration(
                START_SPACING_DP, //.dpToPx(this),
                BETWEEN_SPACING_DP, //.dpToPx(this)
            )
        )
    }
}


class RecordReviewViewHolder(
    internal val binding: ItemRecordReviewBinding,
    private val reviewClick: (MySeatRecordResponse.ReviewResponse) -> Unit,
//    private val editClick : (MySeatRecordResponse.ReviewResponse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var adapter: MonthRecordAdapter


    fun bind(data: List<MySeatRecordResponse.ReviewResponse>) {
        adapter = MonthRecordAdapter()
        binding.rvRecordReview.adapter = adapter
        val groupList =
            data.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        adapter.submitList(groupList)
        adapter.itemRecordClickListener = object : MonthRecordAdapter.OnItemRecordClickListener {
            override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {

            }

            override fun onMoreRecordClick(reviewId: Int) {
            }
        }
    }

}
