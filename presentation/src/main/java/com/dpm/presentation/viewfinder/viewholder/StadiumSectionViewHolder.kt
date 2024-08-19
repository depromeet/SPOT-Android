package com.dpm.presentation.viewfinder.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.dpm.presentation.viewfinder.adapter.MockSection

class StadiumSectionViewHolder(
    private val binding: ItemSelectSeatBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MockSection) {
        binding.tvSeatName.text = item.title
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
            3 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_orange)
            4 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_blue)
            5 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_red)
            6 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_navy)
            7 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_section_green)
            8 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.oval_exciting_blue)
            9 -> binding.ivSeatColor.setImageResource(com.depromeet.designsystem.R.drawable.ic_wheelchair)
        }
    }
}