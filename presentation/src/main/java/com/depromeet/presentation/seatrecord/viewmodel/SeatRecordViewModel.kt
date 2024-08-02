package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.domain.repository.HomeRepository
import com.depromeet.presentation.seatrecord.uiMapper.MonthUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val selectedMonth = MutableStateFlow(0)

    private val _selectedYear = MutableStateFlow(0)
    val selectedYear = _selectedYear.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(EditUi.NONE)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editClickedEvent = MutableStateFlow(EditUi.NONE)
    val editClickedEvent = _editClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()

    private val _clickedReviewId = MutableStateFlow(0)
    val clickedReviewId = _clickedReviewId.asStateFlow()

    private val _pagingData =
        MutableStateFlow<PagingData<MySeatRecordResponse.ReviewResponse>>(PagingData.empty())
    val pagingData = _pagingData.asStateFlow()

    private val page = MutableStateFlow(0)

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
        if (selectedYear.value == 0) return

        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    year = selectedYear.value,
                    month = month,
                    page = page.value
                )
            ).onSuccess { data ->
                Timber.d("GET_SEAT_RECORDS_TEST SUCCESS : $data")
                _reviews.value = UiState.Success(data)
                page.value += 1
            }.onFailure {
                Timber.d("GET_SEAT_RECORDS_TEST FAIL : $it")
                _reviews.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }

    fun loadNextSeatRecords() {
        val month = months.value.find { it.isClicked }?.takeIf { it.month != 0 }?.month
        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    year = selectedYear.value,
                    month = month,
                    page = page.value
                )
            ).onSuccess {data ->
                Timber.d("NEXT_SEAT_RECORDS_SUCCESS : $data")
                val updatedReviewList = (_reviews.value as UiState.Success).data.reviews + data.reviews
                page.value += 1
                _reviews.value = UiState.Success(data.copy(reviews = updatedReviewList))
            }.onFailure {
                Timber.d("NEXT_SEAT_RECORDS_FAIL : $it")
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
            selectedMonth.value = 0
            page.value = 0
        }
    }

    fun setSelectedMonth(month: Int) {
        _months.value = months.value.map {
            it.copy(isClicked = it.month == month)
        }
        selectedMonth.value = month
        page.value = 0
    }

    fun setEditReviewId(id: Int) {
        _editReviewId.value = id
    }

    fun setEditEvent(editUi: EditUi) {
        _editClickedEvent.value = editUi
    }

    fun setDeleteEvent(editUi: EditUi) {
        _deleteClickedEvent.value = editUi
    }

    fun setClickedReviewId(id: Int) {
        _clickedReviewId.value = id
    }

    fun cancelDeleteEvent() {
        _deleteClickedEvent.value = EditUi.NONE
    }

    fun removeReviewData() {
        val currentState = reviews.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                Timber.d("test -> 서버 통신 전  : ${editReviewId.value}")
                homeRepository.deleteReview(editReviewId.value)
                    .onSuccess {
                        if (it.reviewId == editReviewId.value) {
                            val updatedList = currentState.data.reviews.filter { review ->
                                review.id != editReviewId.value
                            }
                            Timber.d("test -> 서버 통신 성공후 비교  : ${editReviewId.value}")
                            _reviews.value =
                                UiState.Success(currentState.data.copy(reviews = updatedList))
                        }
                    }
                    .onFailure {
                        Timber.d("삭제 실패 : $it")
                    }
            }
            _deleteClickedEvent.value = EditUi.NONE
        }

    }

}

enum class EditUi {
    NONE,
    SEAT_RECORD,
    SEAT_DETAIL
}