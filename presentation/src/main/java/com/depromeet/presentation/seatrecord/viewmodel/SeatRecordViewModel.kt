package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.presentation.seatrecord.uiMapper.MonthUiData
import com.depromeet.presentation.seatrecord.uiMapper.ReviewUiData
import com.depromeet.presentation.seatrecord.uiMapper.toUiModel
import com.depromeet.presentation.util.CalendarUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeatRecordViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _reviews = MutableStateFlow<UiState<MySeatRecordResponse>>(UiState.Loading)
    val reviews = _reviews.asStateFlow()

    private val _date = MutableStateFlow<UiState<ReviewDateResponse>>(UiState.Loading)
    val date = _date.asStateFlow()

    private val _months =
        MutableStateFlow<List<MonthUiData>>(emptyList())
    val months = _months.asStateFlow()

    private val _selectedYear = MutableStateFlow(CalendarUtil.getCurrentYear())
    val selectedYear = _selectedYear.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(false)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()

    fun getReviewDate() {
        viewModelScope.launch {
            homeRepository.getReviewDate()
                .onSuccess { data ->
                    if (data.yearMonths.isNotEmpty()) {
                        _date.value = UiState.Success(data)
                        _selectedYear.value = data.yearMonths[0].year
                    } else {
                        _date.value = UiState.Empty
                    }
                }
                .onFailure { e ->
                    _date.value = UiState.Failure(e.message ?: "실패")
                }
        }
    }


    fun getSeatRecords() {
        val month = months.value.find { it.isClicked }?.takeIf { it.month != 0 }?.month
        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    year = selectedYear.value,
                    month = month
                )
            ).onSuccess { data ->
                _reviews.value = UiState.Success(data)
            }.onFailure {
                _reviews.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun initMonths() {
        val currentState = date.value
        if (currentState is UiState.Success) {
            val selectedYearMonths =
                currentState.data.yearMonths.first { it.year == selectedYear.value }.months
            _months.value = selectedYearMonths.mapIndexed { index, month ->
                MonthUiData(month = month, isClicked = index == 0)
            }
        }
    }

    fun setSelectedMonth(month: Int) {
        _months.value = months.value.map {
            it.copy(isClicked = it.month == month)
        }
        getSeatRecords()
    }

    fun setEditReviewId(id: Int) {
        _editReviewId.value = id
    }

    fun setDeleteEvent() {
        _deleteClickedEvent.value = true
    }

    fun removeReviewData() {
        val currentState = reviews.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.reviews.filter { review ->
                review.id != editReviewId.value
            }
            /** 여기서 서버에서 통신이 성공 -> uistate업로드 (api 명세 아직 없음)*/
            _reviews.value = UiState.Success(currentState.data.copy(reviews = updatedList))
            _deleteClickedEvent.value = false
        }

    }

    fun getUiReviewsData(): ArrayList<ReviewUiData> {
        val currentState = reviews.value
        return if (currentState is UiState.Success) {
            ArrayList(currentState.data.reviews.map { it.toUiModel() })
        } else {
            arrayListOf()
        }
    }
}