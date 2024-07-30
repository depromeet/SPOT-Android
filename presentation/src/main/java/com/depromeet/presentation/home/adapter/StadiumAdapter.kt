package com.depromeet.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemStadiumSearchBinding
import com.depromeet.presentation.databinding.ItemStadiumSmallSelectionBinding
import com.depromeet.presentation.extension.loadAndClip
import com.depromeet.presentation.util.ItemDiffCallback

enum class StadiumSelectViewType {
    SEARCH_ITEM,
    TEAM_ITEM
}

class StadiumAdapter(
    private val searchClick: () -> Unit,
    private val stadiumClick: (StadiumsResponse) -> Unit,
) : ListAdapter<StadiumsResponse, RecyclerView.ViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            StadiumSelectViewType.SEARCH_ITEM.ordinal
        } else {
            StadiumSelectViewType.TEAM_ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (StadiumSelectViewType.values()[viewType]) {
            StadiumSelectViewType.SEARCH_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_stadium_search, parent, false)
                StadiumSearchViewHolder(
                    ItemStadiumSearchBinding.bind(view),
                    searchClick
                )
            }

            StadiumSelectViewType.TEAM_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_stadium_small_selection, parent, false)
                StadiumSmallSelectionViewHolder(
                    ItemStadiumSmallSelectionBinding.bind(view),
                    stadiumClick
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StadiumSearchViewHolder -> {
                holder.bind()
            }

            is StadiumSmallSelectionViewHolder -> {
                holder.bind(currentList[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }
}

class StadiumSearchViewHolder(
    private val binding: ItemStadiumSearchBinding,
    private val searchClick: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.root.setOnClickListener {
            searchClick
        }
    }
}

class StadiumSmallSelectionViewHolder(
    private val binding: ItemStadiumSmallSelectionBinding,
    private val teamClick: (StadiumsResponse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: StadiumsResponse) {
        binding.root.setOnClickListener {
            teamClick
        }
        binding.ivStadium.loadAndClip(item.thumbnail)
        binding.tvStadiumName.text = item.name
        if (item.isActive) {
            binding.ivStadiumLock.visibility = GONE
        }else{
            binding.ivStadiumLock.visibility = VISIBLE
        }
    }
}