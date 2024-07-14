package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.presentation.seatrecord.mockdata.ReviewDetailMockResult
import com.depromeet.presentation.seatrecord.mockdata.mockReviewDetailListData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SeatDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewDetailMockResult())
    val uiState = _uiState.asLiveData()

    fun getReviewData() {
        mockReviewDetailListData().onEach {
            _uiState.value = _uiState.value.copy(list = it)
        }.launchIn(viewModelScope)
    }


    fun removeReviewData(id: Int) {
        val currentList = _uiState.value.list
        val updatedList = currentList.filter { review ->
            review.reviewId != id
        }
        _uiState.value = _uiState.value.copy(list = updatedList)
    }

}