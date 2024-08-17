package com.dpm.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import com.dpm.presentation.extension.NICKNAME_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NicknameInputViewModel @Inject constructor(

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
            else -> _nicknameInputState.tryEmit(NicknameInputState.VALID)
        }
        _currentNickName.tryEmit(nickname)
    }
}