package com.depromeet.presentation.home.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ItemBaseballTeamBinding
import com.depromeet.presentation.home.mockdata.TeamData
import com.depromeet.presentation.util.ItemDiffCallback


class ProfileEditTeamAdapter : ListAdapter<TeamData, ProfileEditTeamViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
        onContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    interface OnItemClubClickListener {
        fun onItemClubClick(item: TeamData)
    }

    var itemClubClickListener: OnItemClubClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileEditTeamViewHolder {
        return ProfileEditTeamViewHolder(
            ItemBaseballTeamBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileEditTeamViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            itemClubClickListener?.onItemClubClick(currentList[position])
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

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position: Int = parent.getChildAdapterPosition(view)

        if (position >= 0) {
            val column = position % spanCount
            val row = position / spanCount

            with(outRect) {
                left = if (column == 0) 0 else spacing / 2
                right = if (column == spanCount - 1) 0 else spacing / 2
                top = if (row == 0) 0 else spacing
                //bottom = spacing
            }
        } else {
            with(outRect) {
                left = 0
                right = 0
                top = 0
                bottom = 0
            }
        }
    }
}