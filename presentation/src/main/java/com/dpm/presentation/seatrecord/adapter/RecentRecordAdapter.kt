package com.dpm.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemRecentRecordBinding
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatrecord.uiMapper.toUiKeyword
import com.dpm.presentation.util.CalendarUtil
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.compose.KeywordFlowRow

class RecentRecordAdapter(
) : ListAdapter<ResponseMySeatRecord.ReviewResponse, RecentRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnItemRecordClickListener {
        fun onItemRecordClick(item: ResponseMySeatRecord.ReviewResponse)
        fun onItemMoreClick(item: ResponseMySeatRecord.ReviewResponse)
        fun onLikeClick(reviewId : Int)
        fun onScrapClick(reviewId : Int)
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
            binding.ivRecordScrap.setOnSingleClickListener {
                itemRecordClickListener?.onScrapClick(getItem(position).id)
            }
            binding.ivRecordLike.setOnSingleClickListener {
                itemRecordClickListener?.onLikeClick(getItem(position).id)
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


    fun bind(item: ResponseMySeatRecord.ReviewResponse) {
        with(binding) {
            if (item.images.isNotEmpty()) {
                ivRecentImage.loadAndClip(item.images[0].url)
            } else {
                ivRecentImage.loadAndClip(R.drawable.ic_image_placeholder)
            }
            tvRecentDateDay.text = CalendarUtil.getDayOfMonthFromDateFormat(item.date).toString()
            tvRecentDay.text = CalendarUtil.getDayOfWeekFromDateFormat(item.date)
            tvRecentBlockName.text = item.formattedSeatName()
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
            //TODO : 추후 서버 통신 바뀌면 -> 스크랩, 좋아요 갱신 진행하기
            tvRecordLikeCount.text = "0"
            tvRecordScrapCount.text = "0"

        }
    }

}
