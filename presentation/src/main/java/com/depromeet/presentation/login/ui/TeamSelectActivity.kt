package com.depromeet.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.depromeet.core.base.BaseActivity
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.ResponseBaseballTeam
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentTeamSelectBinding
import com.depromeet.presentation.extension.toast
import com.depromeet.presentation.home.adapter.BaseballTeamAdapter
import com.depromeet.presentation.home.adapter.GridSpacingItemDecoration
import com.depromeet.presentation.login.viewmodel.SignUpViewModel
import com.depromeet.presentation.login.viewmodel.SignupUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamSelectActivity: BaseActivity<FragmentTeamSelectBinding>(
    {
        FragmentTeamSelectBinding.inflate(it)
    }
) {
    private val signupViewModel by viewModels<SignUpViewModel>()
    private lateinit var adapter: BaseballTeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserver()
    }

    private fun initObserver() {
        signupViewModel.getBaseballTeam()
        signupViewModel.teamSelectUiState.asLiveData().observe(this) {
            when (it) {
                SignupUiState.Failure -> { }
                SignupUiState.Initial -> {
                    initRecyclerView()
                    initClickListener()
                }
                SignupUiState.Loading -> { }
                SignupUiState.SignUpSuccess -> {
                    Intent(this, SignUpCompleteActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
            }
        }

        signupViewModel.team.asLiveData().observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    if (!state.data.any { it.isClicked }) {
                        binding.tvSelectedTeamNextBtn.setBackgroundResource(R.drawable.rect_action_disabled_fill_8)
                    }
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
            finish()
        }

        binding.tvSelectedTeamNextBtn.setOnClickListener {
            signupViewModel.signUp(
                intent.getStringExtra("kakaoToken") ?: "",
                intent.getStringExtra("nickname") ?: ""
            )
        }
    }

    private fun initRecyclerView() {
        adapter = BaseballTeamAdapter()
        binding.rvTeamSelectStadium.adapter = adapter
        binding.rvTeamSelectStadium.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                40
            )
        )
        adapter.itemClubClickListener = object : BaseballTeamAdapter.OnItemClubClickListener {
            override fun onItemClubClick(item: ResponseBaseballTeam) {
                signupViewModel.setClickedBaseballTeam(item.id)
                binding.tvSelectedTeamNextBtn.setBackgroundResource(R.drawable.rect_main_fill_6)
            }
        }
    }
}