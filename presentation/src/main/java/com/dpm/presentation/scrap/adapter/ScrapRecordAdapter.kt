package com.dpm.presentation.scrap.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.presentation.databinding.ItemScrapRecordBinding
import com.dpm.domain.entity.response.home.ResponseScrap
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.extension.setOnSingleClickListener
import com.dpm.presentation.util.ItemDiffCallback


class ScrapRecordAdapter(
    private val scrapClick: (ResponseScrap.ResponseReviewWrapper) -> Unit,
    private val recordClick: (Int) -> Unit,
) : ListAdapter<ResponseScrap.ResponseReviewWrapper, ScrapRecordViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.baseReview.id == newItem.baseReview.id },
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
        holder.bind(getItem(position), position)
    }
}

class ScrapRecordViewHolder(
    private val binding: ItemScrapRecordBinding,
    private val scrapClick: (ResponseScrap.ResponseReviewWrapper) -> Unit,
    private val recordClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ResponseScrap.ResponseReviewWrapper, position: Int) = with(binding) {
        ivScrap.setOnSingleClickListener {
            scrapClick(item)
        }
        root.setOnClickListener {
            recordClick(position)
        }


        if (item.baseReview.isScrapped) {
            ivScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_active)
        } else {
            ivScrap.load(com.depromeet.designsystem.R.drawable.ic_scrap_inactive)
        }
        root.clipToOutline = true
        ivScrapImage.loadAndClip(item.baseReview.images[0].url)
        tvScrapStadium.text = item.stadiumName
        tvScrapSeat.text = item.baseReview.formattedBaseToBlock()
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