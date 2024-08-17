package com.dpm.presentation.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemBaseballTeamBinding
import com.depromeet.presentation.databinding.ItemSelectTeamBinding
import com.dpm.presentation.home.mockdata.TeamData
import com.dpm.presentation.util.ItemDiffCallback

enum class TeamSelectViewType {
    TEAM_ITEM,
    BUTTON_ITEM
}

class TeamSelectAdapter(
    private val itemClubClick: (TeamData) -> Unit,
    private val noTeamClick: () -> Unit,
    private val nextClick: () -> Unit
) : ListAdapter<TeamData, RecyclerView.ViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
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
                ButtonViewHolder(
                    binding = ItemSelectTeamBinding.bind(view),
                    noTeamClick = noTeamClick,
                    nextClick = nextClick
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileEditTeamViewHolder -> {
                holder.bind(currentList[position])
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
    private val binding: ItemSelectTeamBinding,
    private val noTeamClick: () -> Unit,
    private val nextClick: () -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.tvSelectTeamNoTeam.setOnClickListener {
            noTeamClick()
        }
        binding.tvSelectedTeamNextBtn.setOnClickListener {
            nextClick()
        }
    }
}

class ProfileEditTeamViewHolder(
    private val binding: ItemBaseballTeamBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TeamData) {
        with(binding) {
            ivTeamImage.load(item.image) {
                placeholder(R.drawable.ic_lg_team)
                error(R.drawable.ic_x_close)
            }
            tvTeamName.text = item.name
            updateSelectState(item.isClicked)
        }
    }

    private fun updateSelectState(isSelected: Boolean) {
        val backgroundRes = when (isSelected) {
            true -> R.drawable.rect_gray50_fill_gray900_line_10
            false -> R.drawable.rect_gray100_line_10
        }
        binding.root.setBackgroundResource(backgroundRes)
    }
}