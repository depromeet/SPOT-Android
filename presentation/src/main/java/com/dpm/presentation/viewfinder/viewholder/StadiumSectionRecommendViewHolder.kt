package com.dpm.presentation.viewfinder.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.designsystem.R
import com.depromeet.presentation.databinding.ItemBlockRecommendBinding
import com.dpm.presentation.extension.dpToPx
import com.dpm.presentation.extension.setMargins
import com.dpm.presentation.viewfinder.adapter.Recommend

class StadiumSectionRecommendViewHolder(
    private val binding: ItemBlockRecommendBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Recommend, last: Boolean) {
        if (!last) binding.root.setMargins(
            right = 6.dpToPx(binding.root.context),
            left = 0,
            top = 0,
            bottom = 0
        )
        binding.tvTitle.text = item.title
        if (item.isActive) {
            binding.root.setBackgroundResource(R.drawable.rect_spot_green_fill_999)
            binding.tvTitle.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.color_background_white
                )
            )
        } else {
            binding.root.setBackgroundResource(R.drawable.rect_background_white_fill_999)
            binding.tvTitle.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.black
                )
            )
        }
    }
}