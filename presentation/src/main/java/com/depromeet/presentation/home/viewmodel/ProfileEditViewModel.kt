package com.depromeet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.BaseballTeamResponse
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.presentation.extension.NickNameError
import com.depromeet.presentation.extension.validateNickName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _team = MutableStateFlow<UiState<List<BaseballTeamResponse>>>(UiState.Loading)
    val team = _team.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickName = _nickName.asStateFlow()

    private val _nickNameError = MutableStateFlow<NickNameError>(NickNameError.NoError)
    val nickNameError = _nickNameError.asStateFlow()

    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

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

    fun setProfileImage(uri: String) {
        _profileImage.value = uri
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

    fun deleteCheerTeam() {
        val currentState = team.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map { team ->
                team.copy(isClicked = false)
            }
            _team.value = UiState.Success(updatedList)
            _cheerTeam.value = 0
        }
    }

    fun setProfile(name: String, image: String, cheerTeam: Int) {
        _nickName.value = name
        _profileImage.value = image
        _cheerTeam.value = cheerTeam
    }

    fun updateNickName(nickName: String) {
        _nickName.value = nickName
        _nickNameError.value = nickName.validateNickName()
    }
}