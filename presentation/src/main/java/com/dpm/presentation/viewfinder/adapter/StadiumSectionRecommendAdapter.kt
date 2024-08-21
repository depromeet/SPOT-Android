package com.dpm.presentation.viewfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.depromeet.presentation.databinding.ItemBlockRecommendBinding
import com.dpm.domain.entity.response.viewfinder.ResponseStadium
import com.dpm.presentation.util.ItemDiffCallback
import com.dpm.presentation.viewfinder.viewholder.StadiumSectionRecommendViewHolder

class StadiumSectionRecommendAdapter : ListAdapter<ResponseStadium.ResponseBlockTags, StadiumSectionRecommendViewHolder>(
    ItemDiffCallback<ResponseStadium.ResponseBlockTags>(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentsTheSame = { old, new -> old == new }
    )
) {

    interface OnItemFilterClickListener {
        fun onItemFilterClick(recommend: ResponseStadium.ResponseBlockTags)
    }

    var itemFilterClickListener: OnItemFilterClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StadiumSectionRecommendViewHolder {
        return StadiumSectionRecommendViewHolder(
            ItemBlockRecommendBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StadiumSectionRecommendViewHolder, position: Int) {
        holder.bind(currentList[position], position == currentList.size - 1)
        holder.itemView.setOnClickListener {
            itemFilterClickListener?.onItemFilterClick(currentList[position])
        }
    }
}
