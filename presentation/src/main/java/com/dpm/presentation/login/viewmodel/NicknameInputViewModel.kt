package com.dpm.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _nicknameInputState = MutableLiveData<NicknameInputState>(NicknameInputState.EMPTY)
    val nicknameInputState: LiveData<NicknameInputState> = _nicknameInputState

    private val _currentNickName = MutableStateFlow<String>("")
    val currentNickName: StateFlow<String> = _currentNickName

    fun validateNickname(nickname: String) {
        when {
            nickname.isEmpty() -> _nicknameInputState.value = NicknameInputState.EMPTY
            nickname.length < 2 || nickname.length > 10 -> _nicknameInputState.value =(
                NicknameInputState.INVALID_LENGTH
            )
            !nickname.matches(Regex(NICKNAME_PATTERN)) -> _nicknameInputState.value = (
                NicknameInputState.INVALID_CHARACTER
            )
            else -> { _nicknameInputState.value = (NicknameInputState.VALID) }
        }
        _currentNickName.tryEmit(nickname)
    }

    fun checkDuplicateNickname(nickname: String) {
        viewModelScope.launch {
            homeRepository.getDuplicateNickname(nickname)
                .onSuccess {
                    _nicknameInputState.value = (NicknameInputState.NICKNAME_SUCCESS)
                }
                .onFailure {
                    _nicknameInputState.value = (NicknameInputState.DUPLICATE)
                }
        }
    }
}