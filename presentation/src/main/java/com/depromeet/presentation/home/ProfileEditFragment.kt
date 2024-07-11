package com.depromeet.presentation.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.depromeet.core.base.BindingFragment
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentProfileEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : BindingFragment<FragmentProfileEditBinding>(
    layoutResId = R.layout.fragment_profile_edit,
    bindingInflater = FragmentProfileEditBinding::inflate
) {

    private lateinit var adapter: ProfileEditTeamAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecyclerView()
        val testData = mutableListOf<UITeamData>()
        for (i in 1..20) {
            testData.add(UITeamData("LG 트윈스", R.drawable.ic_lg_team, false))
        }
        adapter.submitList(testData)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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