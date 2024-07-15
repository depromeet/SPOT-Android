package com.depromeet.presentation.seatrecord.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemDateMonthBinding
import com.depromeet.presentation.seatrecord.mockdata.MonthData
import com.depromeet.presentation.util.ItemDiffCallback

class DateMonthAdapter : ListAdapter<MonthData, DateMonthViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnItemMonthClickListener {
        fun onItemMonthClick(item: MonthData)
    }

    var itemMonthClickListener: OnItemMonthClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateMonthViewHolder {
        return DateMonthViewHolder(
            ItemDateMonthBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DateMonthViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            itemMonthClickListener?.onItemMonthClick(currentList[position])
        }
    }
}

class DateMonthViewHolder(
    private val binding: ItemDateMonthBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MonthData) {
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
                    setBackgroundResource(R.drawable.rect_gray900_fill_6)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                false -> {
                    setBackgroundResource(R.drawable.rect_white_fill_gray100_line_8)
                    setTextColor(ContextCompat.getColor(context, R.color.gray900))
                }
            }
        }

    }
}

internal class LinearSpacingItemDecoration(
    private val startSpacing: Int,
    private val betweenSpacing: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val lastPosition = (parent.adapter?.itemCount ?: 0) - 1

        if (position >= 0) {
            when (position) {
                0 -> {
                    outRect.left = startSpacing
                    outRect.right = betweenSpacing
                }

                lastPosition -> {}
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