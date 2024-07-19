package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.presentation.seatrecord.mockdata.ReviewDetailMockResult
import com.depromeet.presentation.seatrecord.mockdata.mockReviewDetailListData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SeatDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewDetailMockResult())
    val uiState = _uiState.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(false)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()


    fun getReviewData() {
        mockReviewDetailListData().onEach {
            _uiState.value = _uiState.value.copy(list = it)
        }.launchIn(viewModelScope)
    }


    fun removeReviewData() {
        val currentList = _uiState.value.list
        val updatedList = currentList.filter { review ->
            review.reviewId != _editReviewId.value
        }
        _uiState.value = _uiState.value.copy(list = updatedList)
        _deleteClickedEvent.value = false
    }

    fun setEditReviewId(id: Int) {
        _editReviewId.value = id
    }

    fun setDeleteEvent() {
        _deleteClickedEvent.value = true
    }
}