package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemRecentRecordBinding
import com.depromeet.presentation.extension.extractDay
import com.depromeet.presentation.extension.getDayOfWeek
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.seatrecord.mockdata.ReviewMockData
import com.depromeet.presentation.util.ItemDiffCallback
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow

class RecentRecordAdapter(
) : ListAdapter<ReviewMockData, RecentRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnItemRecordClickListener {
        fun onItemRecordClick(item: ReviewMockData)
    }

    var itemRecordClickListener: OnItemRecordClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentRecordViewHolder {
        return RecentRecordViewHolder(
            ItemRecentRecordBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            itemRecordClickListener?.onItemRecordClick(getItem(position))
        }
    }
}

class RecentRecordViewHolder(
    internal val binding: ItemRecentRecordBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val MAX_VISIBLE_CHIPS = 2
    }


    fun bind(item: ReviewMockData) {
        with(binding) {
            ivRecentImage.loadAndClip(item.image)
            tvRecentDateDay.text = item.date.extractDay()
            tvRecentDay.text = item.date.getDayOfWeek()
            tvRecentBlockName.text = item.blockName
            tvRecentStadiumName.text = item.stadiumName
            cvDetailKeyword.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        KeywordFlowRow(
                            keywords = item.keyword,
                            overflowIndex = MAX_VISIBLE_CHIPS
                        )
                    }
                }
            }
        }
    }
}