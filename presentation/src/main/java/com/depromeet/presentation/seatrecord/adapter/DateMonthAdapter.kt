package com.depromeet.presentation.seatrecord.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.presentation.databinding.ItemDateMonthBinding
import com.depromeet.presentation.util.ItemDiffCallback

class DateMonthAdapter(
    private val monthClick: (Int) -> Unit,
) : ListAdapter<ReviewDateResponse.MonthData, DateMonthViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateMonthViewHolder {
        return DateMonthViewHolder(
            ItemDateMonthBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), monthClick
        )
    }

    override fun onBindViewHolder(holder: DateMonthViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DateMonthViewHolder(
    private val binding: ItemDateMonthBinding,
    private val monthClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReviewDateResponse.MonthData) {
        binding.root.setOnClickListener {
            monthClick(item.month)
        }
        binding.tvMonth.text = when (item.month) {
            0 -> "전체"
            else -> "${item.month}월"
        }
        updateSelectedItem(item.isClicked)
    }

    private fun updateSelectedItem(isClicked: Boolean) {
        with(binding.tvMonth) {
            when (isClicked) {
                true -> {
                    setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_spot_green_fill_999)
                    setTextAppearance(com.depromeet.designsystem.R.style.TextAppearance_Spot_Subtitle03)
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.depromeet.designsystem.R.color.color_foreground_white
                        )
                    )

                }

                false -> {
                    setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_foreground_white_fill_999)
                    setTextAppearance(com.depromeet.designsystem.R.style.TextAppearance_Spot_Label06)
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.depromeet.designsystem.R.color.color_foreground_body_sebtext
                        )
                    )
                }
            }
        }

    }
}

internal class LinearSpacingItemDecoration(
    private val startSpacing: Int= 0,
    private val betweenSpacing: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position: Int = parent.getChildAdapterPosition(view)

        if (position >= 0) {
            when (position) {
                0 -> {
                    outRect.left = startSpacing
                    outRect.right = betweenSpacing
                }

                else -> {
                    outRect.right = betweenSpacing
                }
            }
        } else {
            with(outRect) {
                left = 0
                right = 0
                top = 0
                bottom = 0
            }
        }
    }
}