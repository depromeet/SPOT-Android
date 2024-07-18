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

    fun getBaseballTeam() {
        viewModelScope.launch {
            homeRepository.getBaseballTeam()
                .onSuccess {
                    _team.value = UiState.Success(it)
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
        }
    }

    fun deleteCheerTeam() {
        val currentState = team.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map { team ->
                team.copy(isClicked = false)
            }
            _team.value = UiState.Success(updatedList)
        }
    }

    fun updateNickName(nickName: String) {
        _nickName.value = nickName
        _nickNameError.value = nickName.validateNickName()
    }
}