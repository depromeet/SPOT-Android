package com.dpm.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.SignupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    object LoginSuccess : LoginUiState()
}

@HiltViewModel
class KakaoSignupViewModel @Inject constructor(
    private val signupRepository: SignupRepository,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    private val _kakaoToken = MutableLiveData<String>()
    val kakaoToken: LiveData<String> = _kakaoToken

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun updateKakaoToken(token: String) {
        // 해당 토큰으로 로그인 시도
        // JwtToken 이 정상적으로 발급되면 SharedPreference 에 저장하고 메인화면으로 이동
        // 그게 아니고 신규 유저면 _kakaoToken 에 저장하고 닉네임 입력 화면으로 이동
        viewModelScope.launch {
            _loginUiState.emit(LoginUiState.Loading)
            signupRepository.getSignup(token)
                .onSuccess {
                    if (it.jwtToken.isEmpty()) {
                        _kakaoToken.value = token
                    } else {
                        sharedPreference.token = it.jwtToken
                        _loginUiState.emit(LoginUiState.LoginSuccess)
                    }
                }.onFailure {
                    _kakaoToken.value = token
                }
        }
    }

    fun updateGoogleToken(token: String) {
        viewModelScope.launch {
            _loginUiState.emit(LoginUiState.Loading)
            signupRepository.getSignupV2("GOOGLE", token)
                .onSuccess {
                    if (it.jwtToken.isEmpty()) {
                        _kakaoToken.value = token
                    } else {
                        sharedPreference.token = it.jwtToken
                        _loginUiState.emit(LoginUiState.LoginSuccess)
                    }
                }.onFailure {
                    _kakaoToken.value = token
                }
        }
    }

}