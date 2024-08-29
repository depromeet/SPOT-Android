package com.dpm.presentation.scrap.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.databinding.ItemScrapFilterBinding
import com.depromeet.presentation.databinding.ItemScrapFilterSelectedBinding
import com.dpm.presentation.scrap.viewmodel.FilterNameData
import com.dpm.presentation.util.ItemDiffCallback
import timber.log.Timber

enum class ScrapFilterViewType {
    FILTER_ITEM,
    SELECTED_ITEM
}

class ScrapFilterAdapter(
    private val filterClick: () -> Unit,
    private val selectedClick: (FilterNameData) -> Unit,
) : ListAdapter<FilterNameData, RecyclerView.ViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> Timber.d("test items -> ${newItem.filterType  == oldItem.filterType}")
            oldItem.filterType == newItem.filterType},
        onContentsTheSame = { oldItem, newItem -> Timber.d("test contents ->${newItem == oldItem}")
            oldItem == newItem }
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
                holder.bind(currentList[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(){
        notifyDataSetChanged()
    }
}

class ScrapFilterViewHolder(
    private val binding: ItemScrapFilterBinding,
    private val filterClick: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.root.clipToOutline = true
        binding.root.setOnClickListener {
            filterClick()
        }
    }
}

class ScrapFilterSelectedViewHolder(
    private val binding: ItemScrapFilterSelectedBinding,
    private val selectedClick: (FilterNameData) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FilterNameData) = with(binding) {
        root.clipToOutline = true
        ivClose.setOnClickListener { selectedClick(item) }
        tvScrapFilter.text = item.name
    }
}
