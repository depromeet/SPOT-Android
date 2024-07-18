package com.depromeet.presentation.seatrecord.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.depromeet.presentation.databinding.ItemSeatReviewDetailBinding
import com.depromeet.presentation.seatrecord.uiMapper.ReviewUiData
import com.depromeet.presentation.seatrecord.uiMapper.toUiKeyword
import com.depromeet.presentation.util.ItemDiffCallback
import com.depromeet.presentation.util.applyBoldSpan
import com.depromeet.presentation.viewfinder.compose.KeywordFlowRow

class DetailRecordAdapter() : ListAdapter<ReviewUiData, ReviewDetailViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnDetailItemClickListener {
        fun onItemMoreClickListener(item: ReviewUiData)
    }

    var itemMoreClickListener: OnDetailItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewDetailViewHolder {
        return ReviewDetailViewHolder(
            ItemSeatReviewDetailBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.ivDetailMore.setOnClickListener {
            itemMoreClickListener?.onItemMoreClickListener(getItem(position))
        }
    }
}


class ReviewDetailViewHolder(
    internal val binding: ItemSeatReviewDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val MAX_VISIBLE_CHIPS = 3
    }

    fun bind(item: ReviewUiData) {
        with(binding) {
            //TODO : 서버 데이터 바뀔거니 기억해두기
//            ivDetailProfileImage.load(item.profileImage) {
//                transformations(CircleCropTransformation())
//            }
//            tvDetailNickname.text = item.nickName
//            "Lv.${item.level}".also { tvDetailLevel.text = it }
            tvDetailStadium.text = item.stadiumName
            tvDetailBlock.text = item.blockName
            tvDetailDate.text = item.date
            tvDetailContent.text = item.content
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
            val text = "${position + 1}/${vpDetailImage.adapter?.itemCount ?: 0}"
            tvDetailImageCount.text = SpannableStringBuilder(text).apply {
                applyBoldSpan(this, 0, (position + 1).toString().length)
            }
        }
    }


}