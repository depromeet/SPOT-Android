package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SeatRecordViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MySeatRecordResponse>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedMonth = MutableStateFlow(0)
    val selectedMonth = _selectedMonth.asStateFlow()

    private val _selectedYear = MutableStateFlow(2024)
    val selectedYear = _selectedYear.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(false)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()


    fun getSeatRecords() {
        val month = if (selectedMonth.value == 0) null else selectedMonth.value
        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    selectedYear.value,
                    month
                )
            ).onSuccess { data ->
                _uiState.value = UiState.Success(data)
            }.onFailure {
                _uiState.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun setSelectedMonth(month: Int) {
        _selectedMonth.value = month
    }

    fun setEditReviewId(id: Int) {
        _editReviewId.value = id
    }

    fun setDeleteEvent() {
        _deleteClickedEvent.value = true
    }

    fun removeReviewData() {
        val currentState = uiState.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.reviews.filter { review ->
                review.id != editReviewId.value
            }
            /** 여기서 서버에서 통신이 성공 -> uistate업로드 (api 명세 아직 없음)*/
            _uiState.value = UiState.Success(currentState.data.copy(reviews = updatedList))
            _deleteClickedEvent.value = false
        }

    }
}