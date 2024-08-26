package com.dpm.presentation.scrap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemScrapImageBinding
import com.dpm.presentation.extension.loadAndClip
import com.dpm.presentation.util.ItemDiffCallback

class ScrapImageAdapter : ListAdapter<String, ScrapImageViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem == newItem },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapImageViewHolder {
        return ScrapImageViewHolder(
            ItemScrapImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScrapImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ScrapImageViewHolder(
    private val binding: ItemScrapImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String) {
        binding.ivImage.loadAndClip(item)
    }
}