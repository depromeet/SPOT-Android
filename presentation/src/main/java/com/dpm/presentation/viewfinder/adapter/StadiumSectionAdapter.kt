package com.dpm.presentation.viewfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.dpm.domain.entity.response.viewfinder.Section
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.viewholder.StadiumSectionViewHolder

class StadiumSectionAdapter : ListAdapter<Section, StadiumSectionViewHolder>(
    ItemDiffCallback<Section>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new },
    )
) {
    interface OnItemSectionClickListener {
        fun onItemSectionClick(mockSection: Section)
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