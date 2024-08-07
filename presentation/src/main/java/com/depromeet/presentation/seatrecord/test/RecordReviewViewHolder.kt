package com.depromeet.presentation.seatrecord.test

import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.databinding.ItemRecordReviewBinding
import com.depromeet.presentation.seatrecord.adapter.MonthRecordAdapter
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.util.CalendarUtil

class RecordReviewViewHolder(
    internal val binding: ItemRecordReviewBinding,
    private val reviewClick: (MySeatRecordResponse.ReviewResponse) -> Unit,
    private val editClick : (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var adapter: MonthRecordAdapter


    fun bind(data: List<MySeatRecordResponse.ReviewResponse>) {
        initMonthAdapter()

        val groupList =
            data.groupBy { CalendarUtil.getMonthFromDateFormat(it.date) }
                .map { (month, reviews) ->
                    MonthReviewData(month, reviews)
                }
        adapter.submitList(groupList)
    }

    private fun initMonthAdapter(){
        if(!::adapter.isInitialized){
            adapter = MonthRecordAdapter()
            binding.rvRecordReview.adapter = adapter
            adapter.itemRecordClickListener = object : MonthRecordAdapter.OnItemRecordClickListener {
                override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {
                    reviewClick(item)
                }

                override fun onMoreRecordClick(reviewId: Int) {
                    editClick(reviewId)
                }
            }
        }
    }

}