package com.depromeet.presentation.seatrecord.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSeatReviewDetailBinding
import com.depromeet.presentation.seatrecord.uiMapper.toUiKeyword
import com.depromeet.presentation.util.CalendarUtil
import com.depromeet.presentation.util.ItemDiffCallback
import com.depromeet.presentation.util.applyBoldSpan
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow

class DetailRecordAdapter(
    private val myProfile: MySeatRecordResponse.MyProfileResponse,
    private val moreClick : (Int) -> Unit

) :
    ListAdapter<MySeatRecordResponse.ReviewResponse, ReviewDetailViewHolder>(
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
            moreClick
        )
    }

    override fun onBindViewHolder(holder: ReviewDetailViewHolder, position: Int) {
        holder.bind(getItem(position), myProfile)
    }
}


class ReviewDetailViewHolder(
    internal val binding: ItemSeatReviewDetailBinding,
    private val moreClick : (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val MAX_VISIBLE_CHIPS = Int.MAX_VALUE
    }

    fun bind(
        item: MySeatRecordResponse.ReviewResponse,
        profile: MySeatRecordResponse.MyProfileResponse,
    ) {
        with(binding) {
            binding.ivDetailMore.setOnClickListener {
                moreClick(item.id)
            }

            ivDetailProfileImage.load(profile.profileImage) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_default_profile)
            }
            tvDetailNickname.text = profile.nickname
            "Lv.${profile.level}".also { tvDetailLevel.text = it }
            tvDetailStadium.text = item.stadiumName
            "${item.sectionName} ${item.blockName}블록".also { tvDetailBlock.text = it }
            tvDetailDate.text = CalendarUtil.getFormattedDate(item.date)
            if (item.content.isBlank()) {
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
                tvDetailImageCount.text = SpannableStringBuilder(text).apply {
                    applyBoldSpan(this, 0, (position + 1).toString().length)
                }
            }

        }
    }


}