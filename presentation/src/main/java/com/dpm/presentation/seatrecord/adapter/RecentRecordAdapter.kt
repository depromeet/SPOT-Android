package com.dpm.presentation.seatrecord.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemRecentRecordBinding
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.domain.model.seatrecord.RecordReviewType
import com.dpm.domain.model.seatrecord.toTypeName
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatrecord.uiMapper.toUiKeyword
import com.dpm.presentation.util.CalendarUtil
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.compose.KeywordFlowRow
import timber.log.Timber

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
        fun onLikeClick(reviewId: Int)
        fun onScrapClick(reviewId: Int)
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
            itemView.setOnSingleClickListener {
                itemRecordClickListener?.onItemRecordClick(getItem(position))
            }
            binding.ibRecentStadiumMore.setOnSingleClickListener {
                itemRecordClickListener?.onItemMoreClick(getItem(position))
            }
            binding.ivRecordScrap.setOnSingleClickListener {
                itemRecordClickListener?.onScrapClick(getItem(position).id)
            }
            binding.ivRecordLike.setOnSingleClickListener {
                if (!getItem(position).isLiked) {
                    binding.lottieLike.playAnimation()
                }
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
                setContent {
                    MaterialTheme {
                        KeywordFlowRow(
                            keywords = item.keywords.map { it.toUiKeyword() },
                            overflowIndex = MAX_VISIBLE_CHIPS
                        )
                    }
                }
            }
            tvRecordLikeCount.text = item.likesCount.toString()
            ivRecordLike.load(if (item.isLiked) com.depromeet.designsystem.R.drawable.ic_like_active else com.depromeet.designsystem.R.drawable.ic_like_inactive)
            tvRecordScrapCount.text = item.scrapsCount.toString()
            if (item.isScrapped) {
                ivRecordScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_active)
                ivRecordScrap.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_action_enabled
                    )
                )
            } else {
                ivRecordScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_inactive)
                ivRecordScrap.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_foreground_caption
                    )
                )
            }
            ivRecordScrap.load(if (item.isScrapped) com.depromeet.designsystem.R.drawable.ic_scrap_active else com.depromeet.designsystem.R.drawable.ic_scrap_inactive)
            when (item.reviewType) {
                RecordReviewType.VIEW.name -> {
                    tvReviewTag.text = RecordReviewType.VIEW.toTypeName()
                    tvReviewTag.setBackgroundResource(R.drawable.rect_stroke_positive_primary_stroke_35)

                }

                RecordReviewType.FEED.name -> {
                    tvReviewTag.text = RecordReviewType.FEED.toTypeName()
                    tvReviewTag.setBackgroundResource(R.drawable.rect_error_primary_stroke_35)
                    tvReviewTag.setTextColor(binding.root.context.getColor(com.depromeet.designsystem.R.color.color_error_primary))
                    tvRecordLikeCount.visibility = GONE
                    ivRecordLike.visibility = GONE
                    tvRecordScrapCount.visibility = GONE
                    ivRecordScrap.visibility = GONE
                }

                else -> {}
            }

        }
    }

}
