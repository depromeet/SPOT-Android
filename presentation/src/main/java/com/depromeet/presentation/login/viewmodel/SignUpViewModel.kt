package com.depromeet.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.presentation.extension.NICKNAME_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : ViewModel() {
    private val _nicknameInputState = MutableStateFlow<NicknameInputState>(NicknameInputState.EMPTY)
    val nicknameInputState: StateFlow<NicknameInputState> = _nicknameInputState

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
    }
}

enum class NicknameInputState {
    EMPTY,
    VALID,
    INVALID_LENGTH,
    INVALID_CHARACTER
}