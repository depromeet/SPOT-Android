package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.databinding.ItemRecentRecordBinding
import com.depromeet.presentation.extension.extractDay
import com.depromeet.presentation.extension.getDayOfWeek
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.seatrecord.uiMapper.toUiKeyword
import com.depromeet.presentation.util.ItemDiffCallback
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow

class RecentRecordAdapter(
) : ListAdapter<MySeatRecordResponse.ReviewResponse, RecentRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnItemRecordClickListener {
        fun onItemRecordClick(item: MySeatRecordResponse.ReviewResponse)
        fun onItemMoreClick(item: MySeatRecordResponse.ReviewResponse)
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
        with(holder) {
            bind(getItem(position))
            itemView.setOnClickListener {
                itemRecordClickListener?.onItemRecordClick(getItem(position))
            }
            binding.ibRecentStadiumMore.setOnClickListener {
                itemRecordClickListener?.onItemMoreClick(getItem(position))
            }

        }
    }
}

class RecentRecordViewHolder(
    internal val binding: ItemRecentRecordBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val MAX_VISIBLE_CHIPS = 2
    }


    fun bind(item: MySeatRecordResponse.ReviewResponse) {
        with(binding) {
            ivRecentImage.loadAndClip(item.images[0].url)
            tvRecentDateDay.text = item.date.extractDay()
            tvRecentDay.text = item.date.getDayOfWeek()
            tvRecentBlockName.text = item.blockName
            tvRecentStadiumName.text = item.stadiumName
            cvDetailKeyword.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        KeywordFlowRow(
                            keywords = item.keywords.map { it.toUiKeyword() },
                            overflowIndex = MAX_VISIBLE_CHIPS
                        )
                    }
                }
            }
        }
    }

}
