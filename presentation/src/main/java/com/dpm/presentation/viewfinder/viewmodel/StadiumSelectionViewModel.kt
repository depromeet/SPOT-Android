package com.dpm.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.response.viewfinder.ResponseStadiums
import com.dpm.domain.repository.ViewfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumSelectionViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository
) : ViewModel() {
    private val _stadiums = MutableStateFlow<UiState<List<ResponseStadiums>>>(UiState.Loading)
    val stadiums = _stadiums.asStateFlow()

    fun getStadiums() {
        viewModelScope.launch {
            viewfinderRepository.getStadiums().onSuccess {
                _stadiums.value = UiState.Success(it)
            }.onFailure {
                _stadiums.value = UiState.Failure(it.message.toString())
            }
        }
    }
}