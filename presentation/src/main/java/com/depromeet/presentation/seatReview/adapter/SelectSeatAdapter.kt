package com.depromeet.presentation.seatReview.adapter

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemSelectSeatBinding
import com.depromeet.presentation.util.ItemDiffCallback

data class SeatInfo(
    val seatName: String,
    val subName: String,
    val seatColor: String,
)

class SelectSeatAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<SeatInfo, SelectSeatAdapter.selectSeatViewHolder>(diffUtil) {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectSeatViewHolder {
        val binding =
            ItemSelectSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return selectSeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: selectSeatViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    class selectSeatViewHolder(
        private val binding: ItemSelectSeatBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SeatInfo, isSelected: Boolean) {
            with(binding) {
                tvSeatName.text = item.seatName
                tvSubName.text = item.subName
                val shapeDrawable = ShapeDrawable(OvalShape())
                shapeDrawable.paint.color = Color.parseColor(item.seatColor)
                tvSeatColor.background = shapeDrawable

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

    companion object {
        private val diffUtil = ItemDiffCallback<SeatInfo>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
