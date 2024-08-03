package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.core.base.BindingFragment
import com.depromeet.core.state.UiState
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentTeamSelectBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.ProfileEditActivity
import com.depromeet.presentation.home.adapter.BaseballTeamAdapter
import com.depromeet.presentation.home.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.login.TeamSelectAdapter
import com.depromeet.presentation.login.TeamSelectViewType
import com.depromeet.presentation.login.selectStadiums
import com.depromeet.presentation.login.viewmodel.SignUpViewModel
import com.depromeet.presentation.login.viewmodel.SignupUiState

class TeamSelectFragment: BindingFragment<FragmentTeamSelectBinding>(
    R.layout.fragment_team_select, { inflater, container, attachToRoot ->
        FragmentTeamSelectBinding.inflate(inflater, container, attachToRoot)
    }
) {
    private val signupViewModel: SignUpViewModel by activityViewModels()
    private lateinit var adapter: BaseballTeamAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupViewModel.getBaseballTeam()
        initObserver()
    }

    private fun initObserver() {
        signupViewModel.teamSelectUiState.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                SignupUiState.Failure -> { }
                SignupUiState.Initial -> {
                    initRecyclerView()
                    initClickListener()
                }
                SignupUiState.Loading -> { }
                SignupUiState.SignUpSuccess -> {
                    Intent(requireContext(), SignUpCompleteActivity::class.java).apply {
                        startActivity(this)
                        requireActivity().finish()
                    }
                }
            }
        }

        signupViewModel.team.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
//                    if (!state.data.any { it.isClicked }) {
//                        binding.tvTeamSelectTitle.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_positive_fill_positive_secondary_stroke_8)
//                    }
                    adapter.submitList(state.data)
                }

                is UiState.Loading -> {
                    toast("로딩 중")
                }

                is UiState.Empty -> {
                    toast("빈값 에러")
                }

                is UiState.Failure -> {
                    toast("통신 실패")
                }
            }
        }
    }

    private fun initClickListener() {
        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initRecyclerView() {
        adapter = BaseballTeamAdapter()
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
            GridSpacingItemDecoration(
                2,
                40
            )
        )
    }
}