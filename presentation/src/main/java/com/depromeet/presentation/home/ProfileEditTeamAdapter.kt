package com.depromeet.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemProfileEditTeamBinding
import com.depromeet.presentation.util.ItemDiffCallback

//테스트용 UiData
data class UITeamData(
    val name: String,
    val image: Int,
    val onItemClicked: (String) -> Unit,
)

class ProfileEditTeamAdapter : ListAdapter<UITeamData, ProfileEditTeamViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileEditTeamViewHolder {
        return ProfileEditTeamViewHolder(
            ItemProfileEditTeamBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileEditTeamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ProfileEditTeamViewHolder(
    private val binding: ItemProfileEditTeamBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UITeamData) {
        with(binding) {
            ivTeamImage.load(item.image) {
                placeholder(R.drawable.ic_lg_team)
                error(R.drawable.ic_x_close)
            }
            tvTeamName.text = item.name
            root.setOnClickListener {
                item.onItemClicked(item.name)
            }
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