package com.dpm.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.SignupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class WithdrawState {
    object Init : WithdrawState()
    object Loading : WithdrawState()
    data class Success(val data: Unit) : WithdrawState()
    data class Error(val message: String) : WithdrawState()
}

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val signupRepository: SignupRepository
) : ViewModel() {

    private val _logoutEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    private val _withdrawState = MutableStateFlow<WithdrawState>(WithdrawState.Init)
    val withdrawState : StateFlow<WithdrawState> = _withdrawState.asStateFlow()

    fun logout() {
        sharedPreference.clear()
        _logoutEvent.tryEmit(Unit)
    }

    fun withdraw() {
        viewModelScope.launch {
            _withdrawState.emit(WithdrawState.Loading)
            signupRepository.deleteWithdraw().onSuccess {
                sharedPreference.clear()
                _withdrawState.emit(WithdrawState.Success(it))
            }.onFailure {
                _withdrawState.emit(WithdrawState.Error(it.message ?: "알 수 없는 오류가 발생했습니다."))
            }
        }
    }
}