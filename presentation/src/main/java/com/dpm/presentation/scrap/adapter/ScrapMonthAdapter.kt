package com.dpm.presentation.scrap.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemScrapMonthBinding
import com.dpm.presentation.scrap.viewmodel.ScrapMonth
import com.dpm.presentation.util.ItemDiffCallback


class ScrapMonthAdapter(
    private val monthClick: (ScrapMonth) -> Unit,
) : ListAdapter<ScrapMonth, ScrapMonthViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.month == newItem.month },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapMonthViewHolder {
        return ScrapMonthViewHolder(
            ItemScrapMonthBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), monthClick = monthClick
        )
    }

    override fun onBindViewHolder(holder: ScrapMonthViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}

class ScrapMonthViewHolder(
    private val binding: ItemScrapMonthBinding,
    private val monthClick: (ScrapMonth) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ScrapMonth) = with(binding) {
        tvScrapMonth.setOnClickListener { monthClick(item) }
        tvScrapMonth.text = item.formattedMonth()

        updatedMonths(item.isSelected)
    }

    private fun updatedMonths(isSelected: Boolean) {
        with(binding.tvScrapMonth) {
            if (isSelected) {
                setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_spot_green_fill_999)
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.depromeet.designsystem.R.color.color_foreground_white
                    )
                )
                setTextAppearance(com.depromeet.designsystem.R.style.TextAppearance_Spot_Label07)
            } else {
                setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_tertiary_fill_999)
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.depromeet.designsystem.R.color.color_foreground_body_subtitle
                    )
                )
                setTextAppearance(com.depromeet.designsystem.R.style.TextAppearance_Spot_Label08)
            }
        }
    }
}

class ScrapMonthGridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int = 0,
    private val bottomSpacing: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val itemCount: Int = parent.adapter?.itemCount ?: 0


        if (position >= 0) {
            val column = position % spanCount
            val totalRows = (itemCount + spanCount - 1) / spanCount
            val row = position / spanCount

            with(outRect) {
                left = if (column == 0) 0 else spacing / 2
                right = if (column == spanCount - 1) 0 else spacing / 2
                top = if (row == 0) 0 else spacing
                bottom = if (row == totalRows - 1) bottomSpacing else 0
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