package com.depromeet.presentation.home.viewmodel

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.presentation.home.mockdata.Profile
import com.depromeet.presentation.home.mockdata.RecentSight
import com.depromeet.presentation.home.mockdata.mockDataProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class HomeUiState(
    val profile: Profile = Profile(),
    val recentSight: RecentSight = RecentSight(),
) : Parcelable

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeUiState>> = _uiState.asStateFlow()

    fun getInformation() {
        mockDataProfile().onEach {
            _uiState.value = UiState.Success(it)
        }.launchIn(viewModelScope)
    }
}