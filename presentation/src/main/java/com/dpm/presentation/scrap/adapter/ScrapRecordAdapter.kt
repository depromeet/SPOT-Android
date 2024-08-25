package com.dpm.presentation.scrap.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemScrapRecordBinding
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.scrap.viewmodel.ScrapData
import com.dpm.presentation.util.ItemDiffCallback


class ScrapRecordAdapter(
    private val scrapClick: (ScrapData) -> Unit,
    private val recordClick: (ScrapData) -> Unit,
) : ListAdapter<ScrapData, ScrapRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItme -> oldItem.id == newItme.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapRecordViewHolder {
        return ScrapRecordViewHolder(
            ItemScrapRecordBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), scrapClick, recordClick
        )
    }

    override fun onBindViewHolder(holder: ScrapRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ScrapRecordViewHolder(
    private val binding: ItemScrapRecordBinding,
    private val scrapClick: (ScrapData) -> Unit,
    private val recordClick: (ScrapData) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ScrapData) = with(binding) {
        ivScrap.setOnSingleClickListener {
            scrapClick(item)
        }
        root.setOnClickListener {
            recordClick(item)
        }

        root.clipToOutline = true
        ivScrapImage.loadAndClip(item.image)
        tvScrapStadium.text = item.stadiumName
        tvScrapSeat.text = item.seatName
    }
}

class ScrapGridSpacingItemDecoration(
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
                left = if (column == 0) 3 else spacing / 2
                right = if (column == spanCount - 1) 3 else spacing / 2
                top = if (row == 0) 3 else spacing

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