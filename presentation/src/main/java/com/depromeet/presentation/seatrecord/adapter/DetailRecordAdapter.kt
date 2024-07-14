package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.depromeet.presentation.databinding.ItemSeatReviewDetailBinding
import com.depromeet.presentation.seatrecord.mockdata.ReviewDetailMockData
import com.depromeet.presentation.util.ItemDiffCallback

class DetailRecordAdapter() : ListAdapter<ReviewDetailMockData, ReviewDetailViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.reviewId == newItem.reviewId },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnDetailItemClickListener {
        fun onItemMoreClickListener(item: ReviewDetailMockData)
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
    fun bind(item: ReviewDetailMockData) {
        with(binding) {
            ivDetailProfileImage.load(item.profileImage) {
                transformations(CircleCropTransformation())
            }
            tvDetailNickname.text = item.nickName
            "Lv.${item.level}".also { tvDetailLevel.text = it }
            tvDetailStadium.text = item.stadiumName
            tvDetailBlock.text = item.stadiumName
            tvDetailDate.text = item.createdAt
            tvDetailContent.text = item.content

            ivTest.load(item.profileImage) { scale(Scale.FILL) } // 테스트 후 삭제
            //뷰페이저 연결하기
        }
    }
}