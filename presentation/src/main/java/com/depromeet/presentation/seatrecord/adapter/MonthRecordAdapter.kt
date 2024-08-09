package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.databinding.ItemRecentMonthBinding
import com.depromeet.presentation.seatrecord.uiMapper.MonthReviewData
import com.depromeet.presentation.util.ItemDiffCallback
import timber.log.Timber


class MonthRecordAdapter() :
    ListAdapter<MonthReviewData, MonthRecordViewHolder>(
        ItemDiffCallback(
            onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
            onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
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
            initReviewAdapter()
            "${item.month}월".also { tvRecentMonth.text = it }
            adapter.submitList(item.reviews)
            binding.root.alpha = 0f
            binding.root.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }


    private fun initReviewAdapter() {
        if (!::adapter.isInitialized) {
            adapter = RecentRecordAdapter()
            binding.rvRecentPost.adapter = adapter
            adapter.itemRecordClickListener =
                object : RecentRecordAdapter.OnItemRecordClickListener {
                    override fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse) {
                        itemRecordClickListener?.onItemRecordClick(item)
                    }

                    override fun onItemMoreClick(item: MySeatRecordResponse.ReviewResponse) {
                        itemRecordClickListener?.onMoreRecordClick(item.id)
                    }
                }
            binding.rvRecentPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val scrollBottom = !binding.rvRecentPost.canScrollVertically(1)
                    val layoutManager = binding.rvRecentPost.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val itemCount = layoutManager.itemCount - 1

                    Timber.d("test scroll $scrollBottom / $lastVisibleItemPosition / $itemCount")
                }
            })
        }
    }
}