package com.depromeet.presentation.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemBaseballTeamBinding
import com.depromeet.presentation.databinding.ItemSelectTeamBinding
import com.depromeet.presentation.home.adapter.ProfileEditTeamViewHolder
import com.depromeet.presentation.home.mockdata.TeamData
import com.depromeet.presentation.util.ItemDiffCallback

enum class TeamSelectViewType {
    TEAM_ITEM,
    BUTTON_ITEM
}

class TeamSelectAdapter : ListAdapter<TeamData, RecyclerView.ViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnItemClubClickListener {
        fun onItemClubClick(item: TeamData)
    }

    var itemClubClickListener: OnItemClubClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) {
            TeamSelectViewType.BUTTON_ITEM.ordinal
        } else {
            TeamSelectViewType.TEAM_ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (TeamSelectViewType.values()[viewType]) {
            TeamSelectViewType.TEAM_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_baseball_team, parent, false)
                ProfileEditTeamViewHolder(ItemBaseballTeamBinding.bind(view))
            }
            TeamSelectViewType.BUTTON_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_team, parent, false)
                ButtonViewHolder(ItemSelectTeamBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileEditTeamViewHolder -> {
                holder.bind(currentList[position])
                holder.itemView.setOnClickListener {
                    itemClubClickListener?.onItemClubClick(currentList[position])
                }
            }
            is ButtonViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }
}

class ButtonViewHolder(
    private val binding: ItemSelectTeamBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.tvSelectTeamNoTeam.setOnClickListener {

        }
        binding.tvSelectedTeamNextBtn.setOnClickListener {

        }
    }
}