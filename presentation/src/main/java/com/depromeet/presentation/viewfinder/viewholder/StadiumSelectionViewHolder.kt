package com.depromeet.presentation.viewfinder.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.designsystem.SpotTeamLabel
import com.depromeet.presentation.databinding.ItemStadiumSelectionBinding
import com.depromeet.presentation.viewfinder.sample.Stadium

class StadiumSelectionViewHolder(
    private val binding: ItemStadiumSelectionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Stadium) {
        with(binding) {
            tvStadiumTitle.text = item.title
            ivStadium.load(item.imageUrl)
            if (item.lock) {
                binding.cvStadiumLock.visibility = View.VISIBLE
            }
            item.team.forEach { teamName ->
                binding.llStadiumTeamLabel.addView(
                    SpotTeamLabel(binding.root.context).apply {
                        teamType = teamName
                    }
                )
            }
        }
    }
}