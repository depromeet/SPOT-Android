package com.dpm.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemRecentMonthBinding
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.presentation.seatrecord.uiMapper.MonthReviewData
import com.dpm.presentation.util.ItemDiffCallback


class MonthRecordAdapter() :
    ListAdapter<MonthReviewData, MonthRecordViewHolder>(
        ItemDiffCallback(
            onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
            onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    ) {
    interface OnItemRecordClickListener {
        fun onItemRecordClick(item: ResponseMySeatRecord.ReviewResponse)
        fun onMoreRecordClick(reviewId: Int)
        fun onLikeClick(reviewId: Int)
        fun onScrapClick(reviewId: Int)
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

    private lateinit var adapter : RecentRecordAdapter

    fun bind(item: MonthReviewData) {
        with(binding) {
            initReviewAdapter(item)
            "${item.month}월".also { tvRecentMonth.text = it }
        }
    }


    private fun initReviewAdapter(item : MonthReviewData) {
        adapter = RecentRecordAdapter()
        binding.rvRecentPost.adapter = adapter
        adapter.submitList(item.reviews.toList())
        adapter.itemRecordClickListener =
            object : RecentRecordAdapter.OnItemRecordClickListener {
                override fun onItemRecordClick(item: ResponseMySeatRecord.ReviewResponse) {
                    itemRecordClickListener?.onItemRecordClick(item)
                }

                override fun onItemMoreClick(item: ResponseMySeatRecord.ReviewResponse) {
                    itemRecordClickListener?.onMoreRecordClick(item.id)
                }

                override fun onLikeClick(reviewId: Int) {
                    itemRecordClickListener?.onLikeClick(reviewId)
                }

                override fun onScrapClick(reviewId: Int) {
                    itemRecordClickListener?.onScrapClick(reviewId)
                }
            }
    }
}