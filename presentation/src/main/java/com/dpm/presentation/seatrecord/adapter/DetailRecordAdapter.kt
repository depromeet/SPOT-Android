package com.dpm.presentation.seatrecord.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSeatReviewDetailBinding
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.domain.model.seatrecord.RecordReviewType
import com.dpm.domain.model.seatrecord.toTypeName
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.seatrecord.uiMapper.toUiKeyword
import com.dpm.presentation.util.CalendarUtil
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.util.applyBoldSpan
import com.dpm.presentation.viewfinder.compose.KeywordFlowRow

class DetailRecordAdapter(
    private val moreClick: (Int) -> Unit,
    private val likeClick: (Int) -> Unit,
    private val scrapClick: (Int) -> Unit,
    private val shareClick: (ResponseMySeatRecord.ReviewResponse, Int) -> Unit,
) : ListAdapter<ResponseMySeatRecord.ReviewResponse, ReviewDetailViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewDetailViewHolder {
        return ReviewDetailViewHolder(
            ItemSeatReviewDetailBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
            moreClick = moreClick,
            likeClick = likeClick,
            scrapClick = scrapClick,
            shareClick = shareClick
        )
    }

    override fun onBindViewHolder(holder: ReviewDetailViewHolder, position: Int) {
        holder.bind(getItem(position))

        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (position == itemCount - 1) {
            params.setMargins(0, 0, 0, 115)
        }

        holder.itemView.layoutParams = params
    }
}


class ReviewDetailViewHolder(
    internal val binding: ItemSeatReviewDetailBinding,
    private val moreClick: (Int) -> Unit,
    private val likeClick: (Int) -> Unit,
    private val scrapClick: (Int) -> Unit,
    private val shareClick: (ResponseMySeatRecord.ReviewResponse, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val MAX_VISIBLE_CHIPS = Int.MAX_VALUE
    }

    fun bind(
        item: ResponseMySeatRecord.ReviewResponse,
    ) {
        with(binding) {
            setInteraction(item)

            ivDetailMore.setOnClickListener {
                moreClick(item.id)
            }
            ivRecordScrap.setOnSingleClickListener {
                scrapClick(item.id)
            }
            ivRecordLike.setOnSingleClickListener {
                likeClick(item.id)
            }
            ivRecordShare.setOnClickListener {
                shareClick(item, vpDetailImage.currentItem)
            }



            ivDetailProfileImage.load(item.member.profileImage) {
                transformations(CircleCropTransformation())
                error(com.depromeet.designsystem.R.drawable.ic_default_profile)
            }
            tvDetailNickname.text = item.member.nickname
            tvDetailLevel.text = item.formattedLevel()
            tvDetailStadium.text = item.stadiumName
            tvDetailBlock.text = item.formattedSeatName()
            tvDetailDate.text = CalendarUtil.getFormattedDate(item.date)
            if (item.content.isEmpty()) {
                tvDetailContent.visibility = GONE
            } else {
                tvDetailContent.text = item.content
            }
            initImageViewPager(item.images.map { it.url })
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
                    ivRecordShare.visibility = GONE
                }

                else -> {}
            }
        }
    }

    private fun setInteraction(item: ResponseMySeatRecord.ReviewResponse) {
        with(binding) {
            tvRecordScrapCount.text = item.scrapsCount.toString()
            tvRecordLikeCount.text = item.likesCount.toString()
            ivRecordLike.load(
                if (item.isLiked) com.depromeet.designsystem.R.drawable.ic_like_active
                else com.depromeet.designsystem.R.drawable.ic_like_inactive
            )
            if (item.isScrapped) {
                ivRecordScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_active)
            } else {
                ivRecordScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_inactive)
                ivRecordScrap.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.depromeet.designsystem.R.color.color_foreground_caption
                    )
                )
            }
        }
    }

    private fun initImageViewPager(imageList: List<String>) {
        val adapter = SeatImageAdapter()
        with(binding) {
            vpDetailImage.adapter = adapter
            adapter.submitList(imageList)
            setViewPagerCountText(0)

            vpDetailImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setViewPagerCountText(position)
                }
            })
        }
    }

    private fun setViewPagerCountText(position: Int) {
        with(binding) {
            val itemCount = vpDetailImage.adapter?.itemCount ?: 0
            if (itemCount <= 1) {
                tvDetailImageCount.visibility = GONE
            } else {
                val text = "${position + 1}/${vpDetailImage.adapter?.itemCount ?: 0}"
                tvDetailImageCount.text = text
            }

        }
    }


}