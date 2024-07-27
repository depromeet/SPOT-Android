package com.depromeet.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.domain.entity.request.signup.PostSignupModel
import com.depromeet.domain.preference.SharedPreference
import com.depromeet.domain.repository.SignupRepository
import com.depromeet.presentation.extension.NICKNAME_PATTERN
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

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    object LoginSuccess : LoginUiState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signupRepository: SignupRepository,
    private val sharedPreference: SharedPreference
) : ViewModel() {
    private val _nicknameInputState = MutableStateFlow<NicknameInputState>(NicknameInputState.EMPTY)
    val nicknameInputState: StateFlow<NicknameInputState> = _nicknameInputState

    private val _currentNickName = MutableStateFlow<String>("")
    val currentNickName: StateFlow<String> = _currentNickName

    private val _kakaoToken = MutableStateFlow<String>("")
    val kakaoToken: StateFlow<String> = _kakaoToken

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _teamSelectUiState = MutableStateFlow<SignupUiState>(SignupUiState.Initial)
    val teamSelectUiState: StateFlow<SignupUiState> = _teamSelectUiState.asStateFlow()

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
        // 해당 토큰으로 로그인 시도
        // JwtToken 이 정상적으로 발급되면 SharedPreference 에 저장하고 메인화면으로 이동
        // 그게 아니고 신규 유저면 _kakaoToken 에 저장하고 닉네임 입력 화면으로 이동
        viewModelScope.launch {
            _loginUiState.emit(LoginUiState.Loading)
            signupRepository.getSignup(token)
                .onSuccess {
                    if (it.jwtToken.isEmpty()) {
                        _kakaoToken.tryEmit(token)
                    } else {
                        sharedPreference.token = it.jwtToken
                        _loginUiState.emit(LoginUiState.LoginSuccess)
                    }
                }.onFailure {
                    _kakaoToken.tryEmit(token)
                }
        }
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
                sharedPreference.token = it.jwtToken
                _teamSelectUiState.emit(SignupUiState.SignUpSuccess)
            }.onFailure {
                _teamSelectUiState.emit(SignupUiState.Failure)
            }
        }
    }
}

enum class NicknameInputState {
    EMPTY,
    VALID,
    INVALID_LENGTH,
    INVALID_CHARACTER,
    DUPLICATE
}