package com.depromeet.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.domain.entity.request.signup.PostSignupModel
import com.depromeet.domain.entity.response.signup.SignupTokenModel
import com.depromeet.domain.repository.SignupRepository
import com.depromeet.presentation.extension.NICKNAME_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val signupRepository: SignupRepository
) : ViewModel() {
    private val _nicknameInputState = MutableStateFlow<NicknameInputState>(NicknameInputState.EMPTY)
    val nicknameInputState: StateFlow<NicknameInputState> = _nicknameInputState

    private val _currentNickName = MutableStateFlow<String>("")
    val currentNickName: StateFlow<String> = _currentNickName

    private val _kakaoToken = MutableStateFlow<String>("")
    val kakaoToken: StateFlow<String> = _kakaoToken

    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Initial)
    val uiState: StateFlow<SignupUiState> = _uiState

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

    fun updateKakaoToken(token: String) {
        _kakaoToken.tryEmit(token)
    }

    fun signUp(teamId: Int) {
        viewModelScope.launch {
            signupRepository.postSignup(
                PostSignupModel(
                    idCode = kakaoToken.value,
                    nickname = currentNickName.value,
                    teamId = teamId
                )
            ).onSuccess {
                _uiState.emit(SignupUiState.SignUpSuccess)
            }.onFailure {
                _uiState.emit(SignupUiState.Failure)
            }
        }
    }
}

enum class NicknameInputState {
    EMPTY,
    VALID,
    INVALID_LENGTH,
    INVALID_CHARACTER
}