package com.dpm.presentation.viewfinder.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.dpm.domain.entity.response.viewfinder.Section

class StadiumSectionViewHolder(
    private val binding: ItemSelectSeatBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Section) {
        binding.tvSeatName.text = item.name
        binding.tvSubName.text = item.alias

        if (item.alias.isNullOrEmpty()) {
            binding.tvSubName.visibility = View.GONE
        } else {
            binding.tvSubName.visibility = View.VISIBLE
        }
        if (item.isActive) {
            binding.tvSubName.setBackgroundResource(R.drawable.rect_green_fill_40)
            binding.layoutSelectSeat.setBackgroundResource(R.drawable.rect_background_positive_fill_secondary_line_8)
        } else {
            binding.tvSubName.setBackgroundResource(R.drawable.rect_foreground_caption_fill_40)
            binding.layoutSelectSeat.setBackgroundResource(R.drawable.rect_background_tertiary_fill_8)
        }
        when (item.id) {
            1 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_premium)
            2 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_table)
            4 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_blue)
            5 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_orange)
            6 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_red)
            7 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_navy)
            8 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_exciting)
            9 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_green)
            10 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.ic_wheelchair)
        }
    }
}