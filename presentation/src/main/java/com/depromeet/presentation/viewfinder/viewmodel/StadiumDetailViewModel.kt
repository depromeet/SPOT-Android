package com.depromeet.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.domain.repository.ViewfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumDetailViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository
) : ViewModel() {
    private val _blockReviews = MutableStateFlow<UiState<BlockReviewResponse>>(UiState.Loading)
    val blockReviews: StateFlow<UiState<BlockReviewResponse>> = _blockReviews.asStateFlow()

    private val _scrollState = MutableStateFlow(false)
    val scrollState = _scrollState.asStateFlow()

    private val _month = MutableStateFlow(0)
    val month = _month.asStateFlow()

    fun updateScrollState(state: Boolean) {
        _scrollState.value = state
    }

    fun updateMonth(month: Int) {
        _month.value = month
    }

    fun getBlockReviews(stadiumId: Int, blockId: String) {
        viewModelScope.launch {
            viewfinderRepository.getBlockReviews(stadiumId, blockId).onSuccess { blockReviews ->
                _blockReviews.value = UiState.Success(blockReviews)
            }.onFailure { e ->
                _blockReviews.value = UiState.Failure(e.message ?: "실패")
            }
        }
    }
}