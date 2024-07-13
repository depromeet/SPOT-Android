package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemRecentMonthBinding
import com.depromeet.presentation.extension.extractMonth
import com.depromeet.presentation.seatrecord.mockdata.MonthReviewData
import com.depromeet.presentation.util.ItemDiffCallback

class MonthRecordAdapter : ListAdapter<MonthReviewData, MonthRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthRecordViewHolder {
        return MonthRecordViewHolder(
            ItemRecentMonthBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MonthRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MonthRecordViewHolder(
    private val binding: ItemRecentMonthBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var adapter: RecentRecordAdapter

    fun bind(item: MonthReviewData) {
        with(binding) {
            adapter = RecentRecordAdapter()
            tvRecentMonth.text = item.month.extractMonth(true)
            rvRecentPost.adapter = adapter
            adapter.submitList(item.reviews)
        }
    }
}