package com.depromeet.presentation.viewfinder.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.depromeet.designsystem.SpotTeamLabel
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.presentation.databinding.ItemStadiumSelectionBinding

class StadiumSelectionViewHolder(
    private val binding: ItemStadiumSelectionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(stadiums: StadiumsResponse) {
        with(binding) {
            tvStadiumTitle.text = stadiums.name
            ivStadium.load(stadiums.thumbnail)
            if (!stadiums.isActive) {
                binding.cvStadiumLock.visibility = View.VISIBLE
            }
            stadiums.homeTeams.forEach { teamName ->
                binding.llStadiumTeamLabel.addView(
                    SpotTeamLabel(binding.root.context).apply {
                        teamType = teamName.alias
                    }
                )
            }
        }
    }
}