package com.dpm.presentation.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.home.ResponseHomeFeed
import com.dpm.domain.entity.response.home.ResponseLevelByPost
import com.dpm.domain.entity.response.home.ResponseLevelUpInfo
import com.dpm.domain.entity.response.viewfinder.ResponseStadiums
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.HomeRepository
import com.dpm.domain.repository.ViewfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeGuiViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository,
    private val homeRepository: HomeRepository,
    private val sharedPreference: SharedPreference,
) : ViewModel() {
    private val _stadiums = MutableStateFlow<UiState<List<ResponseStadiums>>>(UiState.Loading)
    val stadiums = _stadiums.asStateFlow()

    private val _levelDescriptions =
        MutableStateFlow<UiState<List<ResponseLevelByPost>>>(UiState.Loading)
    val levelDescriptions = _levelDescriptions.asStateFlow()

    private val _homeFeed = MutableStateFlow<UiState<ResponseHomeFeed>>(UiState.Loading)
    val homeFeed = _homeFeed.asStateFlow()

    private val _level = MutableStateFlow(0)
    val level = _level.asStateFlow()

    private val _levelUpInfo = MutableStateFlow<UiState<ResponseLevelUpInfo>>(UiState.Loading)
    val levelUpInfo = _levelUpInfo.asStateFlow()

    val levelState = MutableLiveData(false)

    fun getStadiums() {
        viewModelScope.launch {
            viewfinderRepository.getStadiums().onSuccess {
                _stadiums.value = UiState.Success(it)
            }.onFailure {
                _stadiums.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun getLevelDescription() {
        viewModelScope.launch {
            homeRepository.getLevelByPost().onSuccess {
                _levelDescriptions.value = UiState.Success(it)
            }.onFailure {
                _levelDescriptions.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun getHomeFeed(
        isVisibleDialog: Boolean = false,
        onSuccess : (ResponseHomeFeed) -> Unit = {}
    ) {
        viewModelScope.launch {
            homeRepository.getHomeFeed().onSuccess {
                _homeFeed.value = UiState.Success(it)
                if (isVisibleDialog) {
                    onSuccess(it)
                } else {
                    putProfileInfo(it)
                }
            }.onFailure {
                _homeFeed.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun getLevelUpInfo(nextLevel : Int) {
        viewModelScope.launch {
            homeRepository.getLevelUpInfo(nextLevel).onSuccess {
                _levelUpInfo.value = UiState.Success(it)
            }.onFailure {
                _levelUpInfo.value = UiState.Failure(it.message.toString())
            }
        }
    }

    private fun putProfileInfo(data : ResponseHomeFeed){
        sharedPreference.teamId = data.teamId ?: 0
        sharedPreference.levelTitle = data.levelTitle
        sharedPreference.teamName = data.teamName ?: ""
    }
}