package com.depromeet.presentation.seatReview.adapter

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.depromeet.presentation.util.ItemDiffCallback

class SelectSeatAdapter(
    private val onItemClick: (Int, Int) -> Unit,
) : ListAdapter<StadiumSectionModel.SectionListDto, SelectSeatAdapter.SectionViewHolder>(diffUtil) {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding =
            ItemSelectSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = getItem(position)
        val isSelected = position == selectedPosition
        holder.bind(section, isSelected)
        holder.itemView.setOnClickListener {
            onItemClick(position, section.id)
            setItemSelected(position)
        }
    }

    class SectionViewHolder(private val binding: ItemSelectSeatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: StadiumSectionModel.SectionListDto, isSelected: Boolean) {
            binding.tvSeatName.text = section.name
            binding.tvSubName.text = section.alias

            if (section.alias.isNullOrEmpty()) {
                binding.tvSubName.visibility = GONE
            } else {
                binding.tvSubName.visibility = VISIBLE
                binding.tvSubName.text = section.alias
                binding.tvSubName.setBackgroundResource(if (isSelected) { R.drawable.rect_green_fill_40 } else { R.drawable.rect_foreground_caption_fill_40 })
            }
            val shapeDrawable = ShapeDrawable(OvalShape())
            binding.tvSeatColor.background = shapeDrawable
            shapeDrawable.paint.color = Color.parseColor(section.color)
            binding.layoutSelectSeat.setBackgroundResource(if (isSelected) R.drawable.rect_background_positive_fill_secondary_line_8 else R.drawable.rect_background_tertiary_fill_8)
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
        private val diffUtil = ItemDiffCallback<StadiumSectionModel.SectionListDto>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
