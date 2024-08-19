package com.dpm.presentation.viewfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.viewholder.StadiumSectionViewHolder

data class MockSection(
    val id: Int,
    val title: String,
    val alias: String,
    val color: String,
    val isActive: Boolean = false
)

val mockSections = listOf(
    MockSection(1, "프리미엄석", "켈리존", "#7EAEFF"),
    MockSection(2, "테이블석", "", "#D24074"),
    MockSection(3, "오렌지석", "응원석", "#FF9900"),
    MockSection(4, "블루석", "", "#0E5EAD"),
    MockSection(5, "레드석", "", "#D62030"),
    MockSection(6, "네이비석", "", "#1B4963"),
    MockSection(7, "외야그린석", "", "#80C161"),
    MockSection(8, "익사이팅석", "", "#FDCE29"),
    MockSection(9, "휠체어석", "", "#212124"),
)

class StadiumSectionAdapter : ListAdapter<MockSection, StadiumSectionViewHolder>(
    ItemDiffCallback<MockSection>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new },
    )
) {
    interface OnItemSectionClickListener {
        fun onItemSectionClick(mockSection: MockSection)
    }

    var itemSectionClickListener: OnItemSectionClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StadiumSectionViewHolder {
        return StadiumSectionViewHolder(
            ItemSelectSeatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StadiumSectionViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            itemSectionClickListener?.onItemSectionClick(currentList[position])
        }
    }
}