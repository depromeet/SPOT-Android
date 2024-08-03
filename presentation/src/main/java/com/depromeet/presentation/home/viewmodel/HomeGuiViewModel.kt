package com.depromeet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.home.LevelByPostResponse
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
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
) : ViewModel() {
    private val _stadiums = MutableStateFlow<UiState<List<StadiumsResponse>>>(UiState.Loading)
    val stadiums = _stadiums.asStateFlow()

    private val _levelDescriptions =
        MutableStateFlow<UiState<List<LevelByPostResponse>>>(UiState.Loading)
    val levelDescriptions = _levelDescriptions.asStateFlow()

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
}