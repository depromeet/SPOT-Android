package com.depromeet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.domain.repository.ViewfinderRepository
import com.depromeet.presentation.home.adapter.LevelDescriptionTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeGuiViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository,
) : ViewModel() {
    private val _stadiums = MutableStateFlow<UiState<List<StadiumsResponse>>>(UiState.Loading)
    val stadiums = _stadiums.asStateFlow()

    private val _levelDescriptions =
        MutableStateFlow<UiState<List<LevelDescriptionTest>>>(UiState.Loading)
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
        _levelDescriptions.value = UiState.Success(
            listOf(
                LevelDescriptionTest(0, "직관 꿈나무", Pair(0, 0)),
                LevelDescriptionTest(1, "직관 첫 걸음", Pair(1, 2)),
                LevelDescriptionTest(2, "경기장 탐험가", Pair(3, 4)),
                LevelDescriptionTest(3, "직관의 여유", Pair(5, 7)),
                LevelDescriptionTest(4, "응원 단장", Pair(8, 13)),
                LevelDescriptionTest(5, "야구장 VIP", Pair(14, 20)),
                LevelDescriptionTest(6, "전설의 직관러", Pair(21, Int.MAX_VALUE))
            )
        )
    }
}