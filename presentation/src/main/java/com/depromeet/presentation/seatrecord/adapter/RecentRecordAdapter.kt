package com.depromeet.presentation.seatrecord.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemRecentRecordBinding
import com.depromeet.presentation.extension.extractDay
import com.depromeet.presentation.extension.getDayOfWeek
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.seatrecord.mockdata.ReviewMockData
import com.depromeet.presentation.util.ItemDiffCallback
import com.google.android.material.chip.Chip

class RecentRecordAdapter : ListAdapter<ReviewMockData, RecentRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
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
            setChipGroup(item.keyword)
        }
    }

    private fun setChipGroup(items: List<String>) {
        with(binding.cgRecentKeyword) {
            val otherCount = items.size - MAX_VISIBLE_CHIPS

            items.take(MAX_VISIBLE_CHIPS).forEachIndexed { index, item ->
                val chip = createChip(item)
                addView(chip)

                if (index == MAX_VISIBLE_CHIPS - 1 && items.size > MAX_VISIBLE_CHIPS) {
                    val remainingCount = items.size - MAX_VISIBLE_CHIPS
                    val otherChip = createChip(count = remainingCount)
                    addView(otherChip)
                }
            }
        }
    }

    private fun createChip(text: String? = "", count: Int? = null): Chip {
        val chipText = count?.let { "+${it}개" } ?: text

        return Chip(itemView.context).apply {
            this.text = chipText
            isClickable = false
            textSize = 12f
            setBackgroundResource(R.drawable.rect_gray50_fill_4)
            setTextColor(ContextCompat.getColor(context, R.color.gray800))
            setPadding(0, 0, 0, 0)
//            val paddingStartEnd = 8 //.dpToPx(itemView.context)
//            val paddingTopBottom = 4 //.dpToPx(itemView.context)
//            setPadding(paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom)
        }
    }
}