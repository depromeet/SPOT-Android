package com.depromeet.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemLevelDescriptionBinding
import com.depromeet.presentation.util.ItemDiffCallback

data class LevelDescriptionTest(
    val level: Int,
    val title: String,
    val count: Pair<Int, Int>,
)

class LevelDescriptionAdapter : ListAdapter<LevelDescriptionTest, LevelDescriptionViewHolder>(
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
    fun bind(item: LevelDescriptionTest) {
        with(binding) {
            "Lv.${item.level}".also { tvLevel.text = it }
            tvTitle.text = item.title
            tvCount.text = countToString(item.count)
        }
    }

    private fun countToString(count: Pair<Int, Int>): String = when (count.second) {
        0 -> "0회"
        Int.MAX_VALUE -> "${count.first}회 이상"
        else -> "${count.first}~${count.second}회"
    }

}