package com.dpm.presentation.scrap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemScrapFilterBinding
import com.depromeet.presentation.databinding.ItemScrapFilterSelectedBinding
import com.dpm.presentation.scrap.viewmodel.FilterTestData
import com.dpm.presentation.util.ItemDiffCallback
import timber.log.Timber

enum class ScrapFilterViewType {
    FILTER_ITEM,
    SELECTED_ITEM
}

class ScrapFilterAdapter(
    private val filterClick: () -> Unit,
    private val selectedClick: (FilterTestData) -> Unit,
) : ListAdapter<FilterTestData, RecyclerView.ViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ScrapFilterViewType.FILTER_ITEM.ordinal
        } else {
            ScrapFilterViewType.SELECTED_ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ScrapFilterViewType.values()[viewType]) {
            ScrapFilterViewType.FILTER_ITEM -> {
                ScrapFilterViewHolder(
                    ItemScrapFilterBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    ), filterClick = filterClick
                )
            }

            ScrapFilterViewType.SELECTED_ITEM -> {
                ScrapFilterSelectedViewHolder(
                    ItemScrapFilterSelectedBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    ), selectedClick = selectedClick
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScrapFilterViewHolder -> {
                holder.bind()
            }

            is ScrapFilterSelectedViewHolder -> {
                Timber.d("test pos -> $position")
                holder.bind(currentList[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }
}

class ScrapFilterViewHolder(
    private val binding: ItemScrapFilterBinding,
    private val filterClick: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.root.setOnClickListener {
            filterClick()
        }
    }
}

class ScrapFilterSelectedViewHolder(
    private val binding: ItemScrapFilterSelectedBinding,
    private val selectedClick: (FilterTestData) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FilterTestData) = with(binding) {
        root.setOnClickListener { selectedClick(item) }
        tvScrapFilter.text = item.name
    }
}
