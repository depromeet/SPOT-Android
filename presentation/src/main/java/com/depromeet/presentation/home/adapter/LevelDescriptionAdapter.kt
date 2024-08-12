package com.depromeet.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.home.ResponseLevelByPost
import com.depromeet.presentation.databinding.ItemLevelDescriptionBinding
import com.depromeet.presentation.util.ItemDiffCallback

class LevelDescriptionAdapter : ListAdapter<ResponseLevelByPost, LevelDescriptionViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.level == newItem.level },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelDescriptionViewHolder {
        return LevelDescriptionViewHolder(
            ItemLevelDescriptionBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LevelDescriptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class LevelDescriptionViewHolder(
    private val binding: ItemLevelDescriptionBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ResponseLevelByPost) {
        with(binding) {
            "Lv.${item.level}".also { tvLevel.text = it }
            tvTitle.text = item.title
            tvCount.text = countToString(item.minimum, item.maximum)
        }
    }

    private fun countToString(minimum: Int, maximum: Int?): String = when (maximum) {
        0 -> "0회"
        null -> "${minimum}회 이상"
        else -> "${minimum}~${maximum}회"
    }

}