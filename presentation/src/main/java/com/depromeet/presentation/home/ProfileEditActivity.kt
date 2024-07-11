package com.depromeet.presentation.home

import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityProfileEditBinding
import com.depromeet.presentation.home.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.home.adapter.ProfileEditTeamAdapter
import com.depromeet.presentation.home.adapter.UITeamData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditActivity : BaseActivity<ActivityProfileEditBinding>(
    ActivityProfileEditBinding::inflate
) {

    private lateinit var adapter: ProfileEditTeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRecyclerView()
        val testData = mutableListOf<UITeamData>()
        for (i in 1..20) {
            testData.add(UITeamData("LG 트윈스", R.drawable.ic_lg_team, false))
        }
        adapter.submitList(testData)
    }

    private fun setRecyclerView() {
        adapter = ProfileEditTeamAdapter()
        binding.rvProfileEditTeam.adapter = adapter
        binding.rvProfileEditTeam.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount = 2,
                spacing = 40
            )
        )
    }

}