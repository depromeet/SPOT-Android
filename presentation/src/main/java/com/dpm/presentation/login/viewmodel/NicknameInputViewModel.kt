package com.dpm.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.domain.repository.HomeRepository
import com.dpm.presentation.extension.NICKNAME_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameInputViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _nicknameInputState = MutableStateFlow<NicknameInputState>(NicknameInputState.EMPTY)
    val nicknameInputState: StateFlow<NicknameInputState> = _nicknameInputState

    private val _currentNickName = MutableStateFlow<String>("")
    val currentNickName: StateFlow<String> = _currentNickName

    fun validateNickname(nickname: String) {
        when {
            nickname.isEmpty() -> _nicknameInputState.tryEmit(NicknameInputState.EMPTY)
            nickname.length < 2 || nickname.length > 10 -> _nicknameInputState.tryEmit(
                NicknameInputState.INVALID_LENGTH
            )
            !nickname.matches(Regex(NICKNAME_PATTERN)) -> _nicknameInputState.tryEmit(
                NicknameInputState.INVALID_CHARACTER
            )
            else -> { _nicknameInputState.tryEmit(NicknameInputState.VALID) }
        }
        _currentNickName.tryEmit(nickname)
    }

    fun checkDuplicateNickname(nickname: String) {
        viewModelScope.launch {
            homeRepository.getDuplicateNickname(nickname)
                .onSuccess {
                    _nicknameInputState.tryEmit(NicknameInputState.NICKNAME_SUCCESS)
                }
                .onFailure {
                    _nicknameInputState.tryEmit(NicknameInputState.DUPLICATE)
                }
        }
    }
}