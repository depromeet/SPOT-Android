package com.depromeet.presentation.setting

import androidx.lifecycle.ViewModel
import com.depromeet.domain.preference.SharedPreference
import com.depromeet.presentation.home.viewmodel.ProfileEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreference: SharedPreference
) : ViewModel() {

    private val _logoutEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    fun logout() {
        sharedPreference.clear()
        _logoutEvent.tryEmit(Unit)
    }
}