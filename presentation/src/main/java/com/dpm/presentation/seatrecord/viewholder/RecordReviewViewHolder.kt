package com.dpm.presentation.seatrecord.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.depromeet.presentation.databinding.ItemRecordReviewBinding
import com.dpm.presentation.seatrecord.adapter.MonthRecordAdapter
import com.dpm.presentation.seatrecord.uiMapper.MonthReviewData
import com.dpm.presentation.util.CalendarUtil

class RecordReviewViewHolder(
    internal val binding: ItemRecordReviewBinding,
    private val reviewClick: (ResponseMySeatRecord.ReviewResponse) -> Unit,
    private val editClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var adapter: MonthRecordAdapter


    fun bind(data: List<ResponseMySeatRecord.ReviewResponse>) {
        initMonthAdapter()

        val groupList =
            data.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
                .sortedByDescending { it.month }
        adapter.submitList(groupList)
    }

    private fun initMonthAdapter() {
        if (!::adapter.isInitialized) {
            adapter = MonthRecordAdapter()
            binding.rvRecordReview.adapter = adapter
            adapter.itemRecordClickListener =
                object : MonthRecordAdapter.OnItemRecordClickListener {
                    override fun onItemRecordClick(item: ResponseMySeatRecord.ReviewResponse) {
                        reviewClick(item)
                    }

                    override fun onMoreRecordClick(reviewId: Int) {
                        editClick(reviewId)
                    }
                }
        }
    }
}