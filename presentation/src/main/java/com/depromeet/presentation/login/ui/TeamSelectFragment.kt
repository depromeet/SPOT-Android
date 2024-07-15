package com.depromeet.presentation.login.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentTeamSelectBinding
import com.depromeet.presentation.login.TeamSelectAdapter
import com.depromeet.presentation.login.TeamSelectViewType
import com.depromeet.presentation.login.selectStadiums

class TeamSelectFragment: BindingFragment<FragmentTeamSelectBinding>(
    R.layout.fragment_team_select, { inflater, container, attachToRoot ->
        FragmentTeamSelectBinding.inflate(inflater, container, attachToRoot)
    }
) {
    private lateinit var adapter: TeamSelectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = TeamSelectAdapter()
        binding.rvTeamSelectStadium.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == TeamSelectViewType.BUTTON_ITEM.ordinal) {
                    2
                } else {
                    1
                }
            }
        }

        binding.rvTeamSelectStadium.layoutManager = layoutManager
        binding.rvTeamSelectStadium.addItemDecoration(
            com.depromeet.presentation.home.adapter.GridSpacingItemDecoration(
                2,
                40
            )
        )

        adapter.submitList(selectStadiums)
    }

}