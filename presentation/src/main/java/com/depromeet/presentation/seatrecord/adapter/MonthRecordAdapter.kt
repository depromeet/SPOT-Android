package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.databinding.ItemRecentMonthBinding
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.util.ItemDiffCallback


class MonthRecordAdapter() :
    ListAdapter<MonthReviewData, MonthRecordViewHolder>(
        ItemDiffCallback(
            onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
            onContentsTheSame = { oldItem, newItem -> oldItem.reviews == newItem.reviews }
        )
    ) {
    interface OnItemRecordClickListener {
        fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse)
        fun onMoreRecordClick(reviewId: Int)
    }

    var itemRecordClickListener: OnItemRecordClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthRecordViewHolder {
        return MonthRecordViewHolder(
            ItemRecentMonthBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
            itemRecordClickListener
        )
    }

    override fun onBindViewHolder(holder: MonthRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MonthRecordViewHolder(
    private val binding: ItemRecentMonthBinding,
    private val itemRecordClickListener: MonthRecordAdapter.OnItemRecordClickListener?,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var adapter: RecentRecordAdapter

    fun bind(item: MonthReviewData) {
        with(binding) {
            adapter = RecentRecordAdapter()
            "${item.month}월".also { tvRecentMonth.text = it }
            rvRecentPost.adapter = adapter
            adapter.submitList(item.reviews)
            adapter.itemRecordClickListener =
                object : RecentRecordAdapter.OnItemRecordClickListener {
                    override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {
                        itemRecordClickListener?.onItemRecordClick(item)
                    }

                    override fun onItemMoreClick(item: MySeatRecordResponse.ReviewResponse) {
                        itemRecordClickListener?.onMoreRecordClick(item.id)
                    }
                }
        }
    }
}