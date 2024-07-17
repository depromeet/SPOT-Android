package com.depromeet.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.viewfinder.StadiumsResponse
import com.depromeet.domain.repository.ViewfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumSelectionViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository
) : ViewModel() {
    private val _stadiums = MutableStateFlow<UiState<List<StadiumsResponse>>>(UiState.Loading)
    val stadiums = _stadiums.asStateFlow()

    fun getStadiums() {
        viewModelScope.launch {
            _stadiums.value = viewfinderRepository.getStadiums().let { stadiums ->
                if (stadiums.isEmpty()) {
                    UiState.Failure("exception")
                } else {
                    UiState.Success(stadiums)
                }
            }
        }
    }
}