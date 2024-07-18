package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import com.depromeet.presentation.seatrecord.uiMapper.ReviewUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SeatDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(emptyList<ReviewUiData>())
    val uiState = _uiState.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(false)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()

    fun setReviewData(id: Int, reviews: List<ReviewUiData>) {
        _uiState.value = reviews
    }

    fun removeReviewData() {
        val currentList = _uiState.value
        val updatedList = currentList.filter { review ->
            review.id != _editReviewId.value
        }

        /** 투두 : 삭제  api 연동 **/
        _uiState.value = updatedList
        _deleteClickedEvent.value = false
    }

    fun setEditReviewId(id: Int) {
        _editReviewId.value = id
    }

    fun setDeleteEvent() {
        _deleteClickedEvent.value = true
    }
}