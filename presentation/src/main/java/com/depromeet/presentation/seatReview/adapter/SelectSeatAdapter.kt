package com.depromeet.presentation.seatReview.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding

data class SeatInfo(
    val seatName: String,
    val subName: String,
    val seatColor: String,
)

class SelectSeatAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<SeatInfo, SelectSeatAdapter.ViewHolder>(DiffCallback()) {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    inner class ViewHolder(private val binding: ItemSelectSeatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SeatInfo, isSelected: Boolean) {
            with(binding) {
                tvSeatName.text = item.seatName
                tvSubName.text = item.subName
                tvSeatColor.setBackgroundColor(Color.parseColor(item.seatColor))
                root.setBackgroundResource(if (isSelected) R.drawable.rect_gray100_fill_gray800_line_8 else R.drawable.rect_white_fill_gray100_line_8)
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

    class DiffCallback : DiffUtil.ItemCallback<SeatInfo>() {
        override fun areItemsTheSame(oldItem: SeatInfo, newItem: SeatInfo): Boolean =
            oldItem.seatName == newItem.seatName

        override fun areContentsTheSame(oldItem: SeatInfo, newItem: SeatInfo): Boolean =
            oldItem == newItem
    }
}
