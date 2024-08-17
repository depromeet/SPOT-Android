package com.dpm.presentation.seatrecord.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemSeatImageBinding
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.util.ItemDiffCallback

class SeatImageAdapter : ListAdapter<String, SeatImageViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem == newItem },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatImageViewHolder {
        return SeatImageViewHolder(
            ItemSeatImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeatImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SeatImageViewHolder(
    private val binding: ItemSeatImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String) {
        binding.ivTest.loadAndClip(item)
    }
}