package com.dpm.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.FragmentTeamSelectBinding
import com.dpm.core.base.BaseActivity
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.home.ResponseBaseballTeam
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.adapter.BaseballTeamAdapter
import com.dpm.presentation.home.adapter.GridSpacingItemDecoration
import com.dpm.presentation.login.viewmodel.SignUpViewModel
import com.dpm.presentation.login.viewmodel.SignupUiState
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

        initObserve()
    }

    private fun initObserve() {
        signupViewModel.getBaseballTeam()
        signupViewModel.teamSelectUiState.asLiveData().observe(this) {
            when (it) {
                is SignupUiState.Failure -> { }
                is SignupUiState.Initial -> {
                    initView()
                    initEvent()
                }
                is SignupUiState.Loading -> { }
                is SignupUiState.SignUpSuccess -> {
                    Intent(this, SignUpCompleteActivity::class.java).apply {
                        putExtra("nickname", it.nickname)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

    private fun initEvent() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvSelectedTeamNextBtn.setOnClickListener {
            signupViewModel.signUp(
                intent.getStringExtra("kakaoToken") ?: "",
                intent.getStringExtra("nickname") ?: ""
            )
        }

        binding.tvProfileEditNoTeam.setOnClickListener {
            signupViewModel.setClickedBaseballTeam(0)
            binding.tvProfileEditNoTeam.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_positive_fill_positive_secondary_stroke_8)
            binding.tvSelectedTeamNextBtn.setBackgroundResource(R.drawable.rect_main_fill_6)
        }
    }

    private fun initView() {
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
                binding.tvProfileEditNoTeam.setBackgroundResource(com.depromeet.designsystem.R.drawable.rect_background_tertiary_fill_8)
                binding.tvSelectedTeamNextBtn.setBackgroundResource(R.drawable.rect_main_fill_6)
            }
        }
    }
}