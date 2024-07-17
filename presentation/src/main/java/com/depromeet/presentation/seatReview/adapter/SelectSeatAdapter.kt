package com.depromeet.presentation.seatReview.adapter

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.seatReview.StadiumSectionModel
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.depromeet.presentation.util.ItemDiffCallback

class SectionListAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<StadiumSectionModel.SectionListDto, SectionListAdapter.SectionViewHolder>(diffUtil) {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding =
            ItemSelectSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    class SectionViewHolder(private val binding: ItemSelectSeatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: StadiumSectionModel.SectionListDto, isSelected: Boolean) {
            binding.tvSeatName.text = section.name
            binding.tvSubName.text = section.alias

            val shapeDrawable = ShapeDrawable(OvalShape())
            binding.tvSeatColor.background = shapeDrawable
            shapeDrawable.paint.color = Color.rgb(
                section.color.red,
                section.color.green,
                section.color.blue,
            )
            binding.tvSeatColor.background = shapeDrawable
            binding.root.setBackgroundResource(if (isSelected) R.drawable.rect_gray100_fill_gray800_line_8 else R.drawable.rect_white_fill_gray100_line_8)
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
