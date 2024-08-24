package com.dpm.presentation.seatreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemSelectViewKeywordBinding
import com.dpm.presentation.util.ItemDiffCallback

class SelectKeywordAdapter : ListAdapter<String, SelectKeywordAdapter.KeywordViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = ItemSelectViewKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class KeywordViewHolder(
        private val binding: ItemSelectViewKeywordBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.tvKeywordName.text = item
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<String>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
