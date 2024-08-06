package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.entity.response.home.ReviewDateResponse
import com.depromeet.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SeatRecordViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _reviews = MutableStateFlow<UiState<MySeatRecordResponse>>(UiState.Loading)
    val reviews = _reviews.asStateFlow()

    private val _date = MutableStateFlow<UiState<ReviewDateResponse>>(UiState.Loading)
    val date = _date.asStateFlow()

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
        val year = (date.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
        val month =
            (date.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
        page.value = 0

        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    year = year,
                    month = month.takeIf { it != 0 },
                    page = page.value
                )
            ).onSuccess { data ->
                Timber.d("GET_SEAT_RECORDS_TEST SUCCESS : $data")
                if (data.reviews.isEmpty()) {
                    _reviews.value = UiState.Empty
                } else {
                    _reviews.value = UiState.Success(data)
                    page.value += 1
                }

            }.onFailure {
                Timber.d("GET_SEAT_RECORDS_TEST FAIL : $it")
                _reviews.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }

    fun loadNextSeatRecords() {
        viewModelScope.launch {
            val year = (date.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
            val month =
                (date.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
            homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    year = year,
                    month = month,
                    page = page.value
                )
            ).onSuccess { data ->
                Timber.d("NEXT_SEAT_RECORDS_SUCCESS : $data")
                val updatedReviewList =
                    (_reviews.value as UiState.Success).data.reviews + data.reviews
                page.value += 1
                _reviews.value = UiState.Success(data.copy(reviews = updatedReviewList))
            }.onFailure {
                Timber.d("NEXT_SEAT_RECORDS_FAIL : $it")
            }
        }
    }

    fun setSelectedYear(year: Int) {
        val currentState = date.value
        if (currentState is UiState.Success) {

            val updatedYearMonths = currentState.data.yearMonths.map { yearMonth ->
                yearMonth.copy(
                    isClicked = yearMonth.year == year
                )
            }
            _date.value = UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }

    fun setSelectedMonth(month: Int) {
        val currentState = _date.value
        if (currentState is UiState.Success) {
            val selectedYear = currentState.data.yearMonths.find { it.isClicked }?.year

            val updatedYearMonths = currentState.data.yearMonths.map { yearMonth ->
                if (yearMonth.year == selectedYear) {
                    yearMonth.copy(
                        months = yearMonth.months.map { monthData ->
                            monthData.copy(isClicked = monthData.month == month)
                        }
                    )
                } else {
                    yearMonth.copy()
                }
            }
            _date.value = UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
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

    fun updateProfile(nickname: String, profileImage: String, teamId: Int, teamName: String?) {
        val currentState = _reviews.value
        if (currentState is UiState.Success) {
            val updatedProfile = currentState.data.profile.copy(
                nickname = nickname,
                profileImage = profileImage,
                teamId = teamId,
                teamName = teamName
            )

            val updatedData = currentState.data.copy(
                profile = updatedProfile
            )

            _reviews.value = UiState.Success(updatedData)
        }
    }

    fun removeReviewData() {
        val currentState = reviews.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                homeRepository.deleteReview(editReviewId.value)
                    .onSuccess {
                        if (it.reviewId == editReviewId.value) {
                            val updatedList = currentState.data.reviews.filter { review ->
                                review.id != editReviewId.value
                            }
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