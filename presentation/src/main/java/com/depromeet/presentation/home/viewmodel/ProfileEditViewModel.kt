package com.depromeet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.presentation.extension.NickNameError
import com.depromeet.presentation.extension.validateNickName
import com.depromeet.presentation.home.mockdata.TeamData
import com.depromeet.presentation.home.mockdata.mockDataProfileEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class EditUiState(
    val image: String = "",
    val nickName: String = "",
    val teamList: List<TeamData> = emptyList(),
)


@HiltViewModel
class ProfileEditViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickName = _nickName.asStateFlow()

    private val _nickNameError = MutableStateFlow<NickNameError>(NickNameError.NoError)
    val nickNameError = _nickNameError.asStateFlow()

    fun getInformation() {
        mockDataProfileEdit().onEach {
            _uiState.value = it
        }.launchIn(viewModelScope)
    }

    fun setProfileImage(uri: String) {
        val newState = _uiState.value.copy(image = uri)
        _uiState.value = newState
    }

    fun updateClickTeam(team: TeamData) {
        val currentTeamList = uiState.value.teamList.toMutableList()
        val newCheckedTeamIndex = currentTeamList.indexOfFirst { it.id == team.id }.apply {
            if (this == -1) return
        }
        val beforeCheckedTeamIndex = currentTeamList.indexOfFirst { it.isClicked }

        if (newCheckedTeamIndex == beforeCheckedTeamIndex) {
            return
        }

        currentTeamList[newCheckedTeamIndex] =
            currentTeamList[newCheckedTeamIndex].copy(isClicked = true)
        if (beforeCheckedTeamIndex != -1) {
            currentTeamList[beforeCheckedTeamIndex] =
                currentTeamList[beforeCheckedTeamIndex].copy(isClicked = false)
        }

        _uiState.value = _uiState.value.copy(teamList = currentTeamList)
    }

    fun deleteCheerTeam() {
        val currentTeamList = uiState.value.teamList.map { it.copy(isClicked = false) }
        _uiState.value = _uiState.value.copy(teamList = currentTeamList)
    }

    fun updateNickName(nickName: String) {
        _nickName.value = nickName
        _nickNameError.value = nickName.validateNickName()
    }
}