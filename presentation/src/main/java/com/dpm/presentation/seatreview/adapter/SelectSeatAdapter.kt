package com.dpm.presentation.seatreview.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import com.dpm.presentation.util.ItemDiffCallback

class SelectSeatAdapter(
    private val onItemClick: (Int, Int) -> Unit,
) : ListAdapter<ResponseStadiumSection.SectionListDto, SelectSeatAdapter.SectionViewHolder>(diffUtil) {

    private var selectedPosition = RecyclerView.NO_POSITION

    private val sectionColorList = listOf(
        com.depromeet.designsystem.R.drawable.oval_section_premium,
        com.depromeet.designsystem.R.drawable.oval_section_table,
        com.depromeet.designsystem.R.drawable.oval_section_blue,
        com.depromeet.designsystem.R.drawable.oval_section_orange,
        com.depromeet.designsystem.R.drawable.oval_section_red,
        com.depromeet.designsystem.R.drawable.oval_section_navy,
        com.depromeet.designsystem.R.drawable.oval_exciting_blue,
        com.depromeet.designsystem.R.drawable.oval_section_green,
        com.depromeet.designsystem.R.drawable.ic_wheelchair,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSelectSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding, sectionColorList)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = getItem(position)
        val isSelected = position == selectedPosition
        holder.bind(section, isSelected, position)
        holder.itemView.setOnClickListener {
            onItemClick(position, section.id)
            setItemSelected(position)
        }
    }

    class SectionViewHolder(
        private val binding: ItemSelectSeatBinding,
        private val sectionColorList: List<Int>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            section: ResponseStadiumSection.SectionListDto,
            isSelected: Boolean,
            position: Int,
        ) {
            binding.tvSeatName.text = section.name
            binding.tvSubName.text = section.alias

            if (section.alias.isNullOrEmpty()) {
                binding.tvSubName.visibility = GONE
            } else {
                binding.tvSubName.visibility = VISIBLE
                binding.tvSubName.text = section.alias
                binding.tvSubName.setBackgroundResource(
                    if (isSelected) {
                        R.drawable.rect_green_fill_40
                    } else {
                        R.drawable.rect_foreground_caption_fill_40
                    },
                )
            }
            val colorResource = getColorResourceForPosition(position)
            binding.ivSeatColor.setImageResource(colorResource)

            binding.layoutSelectSeat.setBackgroundResource(
                if (isSelected) {
                    R.drawable.rect_background_positive_fill_secondary_line_8
                } else {
                    R.drawable.rect_background_tertiary_fill_8
                },
            )
        }

        private fun getColorResourceForPosition(position: Int): Int {
            return if (position in sectionColorList.indices) {
                sectionColorList[position]
            } else {
                R.drawable.rect_green_fill_40
            }
        }
    }

    fun setItemSelected(position: Int) {
        if (selectedPosition != position) {
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<ResponseStadiumSection.SectionListDto>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
