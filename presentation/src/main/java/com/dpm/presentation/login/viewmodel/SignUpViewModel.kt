package com.dpm.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.request.signup.PostSignupModel
import com.dpm.domain.entity.response.home.ResponseBaseballTeam
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.HomeRepository
import com.dpm.domain.repository.SignupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignupUiState {
    object Initial : SignupUiState()
    object Loading : SignupUiState()
    object SignUpSuccess : SignupUiState()
    object Failure : SignupUiState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signupRepository: SignupRepository,
    private val homeRepository: HomeRepository,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    private val _teamSelectUiState = MutableStateFlow<SignupUiState>(SignupUiState.Initial)
    val teamSelectUiState: StateFlow<SignupUiState> = _teamSelectUiState.asStateFlow()

    private val _initKakaoLoginFragment = MutableStateFlow<Boolean>(true)
    val initKakaoLoginFragment: StateFlow<Boolean> = _initKakaoLoginFragment.asStateFlow()

    private val _team = MutableStateFlow<UiState<List<ResponseBaseballTeam>>>(UiState.Loading)
    val team = _team.asStateFlow()

    private val _cheerTeam = MutableStateFlow(0)
    val cheerTeam = _cheerTeam.asStateFlow()

    fun getBaseballTeam() {
        viewModelScope.launch {
            homeRepository.getBaseballTeam()
                .onSuccess { teams ->
                    val updatedTeams = teams.map { team ->
                        team.copy(isClicked = team.id == cheerTeam.value)
                    }
                    _team.value = UiState.Success(updatedTeams)
                }.onFailure {
                    _team.value = UiState.Failure(it.message ?: "실패")
                }
        }
    }

    fun signUp(
        kakaoToken: String,
        currentNickName: String
    ) {
        viewModelScope.launch {
            signupRepository.postSignup(
                PostSignupModel(
                    idCode = kakaoToken,
                    nickname = currentNickName,
                    teamId = cheerTeam.value
                )
            ).onSuccess {
                sharedPreference.token = it.jwtToken
                sharedPreference.nickname = currentNickName
                _teamSelectUiState.emit(SignupUiState.SignUpSuccess)
            }.onFailure {
                _teamSelectUiState.emit(SignupUiState.Failure)
            }
        }
    }

    fun setClickedBaseballTeam(id: Int) {
        val currentState = team.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map { team ->
                team.copy(isClicked = team.id == id)
            }
            _team.value = UiState.Success(updatedList)
            _cheerTeam.value = id
        }
    }

    fun initKakaoLoginFragment(isInit : Boolean) {
        _initKakaoLoginFragment.tryEmit(isInit)
    }
}

enum class NicknameInputState {
    EMPTY,
    VALID,
    INVALID_LENGTH,
    INVALID_CHARACTER,
    DUPLICATE
}