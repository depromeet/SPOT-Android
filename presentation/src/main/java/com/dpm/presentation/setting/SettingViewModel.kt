package com.dpm.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.domain.entity.response.home.ResponseUserInfo
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.HomeRepository
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
    private val homeRepository : HomeRepository,
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

    private val _profile =
        MutableStateFlow(ResponseUserInfo())
    val profile = _profile.asStateFlow()

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

    fun getProfile(){
        viewModelScope.launch {
            homeRepository.getMyUserInfo()
                .onSuccess {data ->
                    _profile.value = data
                    saveLocalProfile()
                }
                .onFailure {
                    getLocalProfile()
                }
        }
    }

    private fun saveLocalProfile() {
        val profile = _profile.value
        sharedPreference.level = profile.level
        sharedPreference.profileImage = profile.profileImage
        sharedPreference.nickname = profile.nickname
        sharedPreference.teamId = profile.teamId ?: 0
        sharedPreference.teamName = profile.teamName ?: ""
        sharedPreference.levelTitle = profile.levelTitle

    }

    fun getLocalProfile() {
        _profile.value = profile.value.copy(
            level = sharedPreference.level,
            levelTitle = sharedPreference.levelTitle,
            nickname = sharedPreference.nickname,
            teamId = sharedPreference.teamId,
            teamName = sharedPreference.teamName,
            profileImage = sharedPreference.profileImage
        )
    }

    fun updateProfile(nickname: String, profileImage: String, teamId: Int, teamName: String?) {
        _profile.value = profile.value.copy(
            nickname = nickname, profileImage = profileImage, teamId = teamId, teamName = teamName
        )
        sharedPreference.nickname = nickname
        sharedPreference.profileImage = profileImage
        sharedPreference.teamId = teamId
        sharedPreference.teamName = teamName ?: ""
    }
}