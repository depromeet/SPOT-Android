package com.depromeet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.HomeFeedResponse
import com.depromeet.domain.entity.response.home.LevelByPostResponse
import com.depromeet.domain.entity.response.home.LevelUpInfoResponse
import com.depromeet.domain.entity.response.viewfinder.ResponseStadiums
import com.depromeet.domain.preference.SharedPreference
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.domain.repository.ViewfinderRepository
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
        MutableStateFlow<UiState<List<LevelByPostResponse>>>(UiState.Loading)
    val levelDescriptions = _levelDescriptions.asStateFlow()

    private val _homeFeed = MutableStateFlow<UiState<HomeFeedResponse>>(UiState.Loading)
    val homeFeed = _homeFeed.asStateFlow()

    private val _level = MutableStateFlow(0)
    val level = _level.asStateFlow()

    private val _levelUpInfo = MutableStateFlow<UiState<LevelUpInfoResponse>>(UiState.Loading)
    val levelUpInfo = _levelUpInfo.asStateFlow()

    val levelState = MutableStateFlow(false)

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

    fun getHomeFeed() {
        viewModelScope.launch {
            homeRepository.getHomeFeed().onSuccess {
                _homeFeed.value = UiState.Success(it)
                putProfileInfo(it)

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

    private fun checkLevelUp(level: Int) {
        if (sharedPreference.level < level) {
            levelState.value = true
        }
        sharedPreference.level = level
    }

    private fun putProfileInfo(data : HomeFeedResponse){
        checkLevelUp(data.level)
        sharedPreference.teamId = data.teamId ?: 0
        sharedPreference.levelTitle = data.levelTitle
        sharedPreference.teamName = data.teamName ?: ""
        sharedPreference.level = data.level
    }
}