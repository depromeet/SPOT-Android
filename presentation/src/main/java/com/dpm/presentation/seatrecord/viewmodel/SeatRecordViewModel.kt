package com.dpm.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.request.home.RequestMySeatRecord
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.domain.entity.response.home.ResponseReviewDate
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SeatRecordViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val sharedPreference: SharedPreference,
) : ViewModel() {

    private val _reviews = MutableStateFlow<UiState<ResponseMySeatRecord>>(UiState.Loading)
    val reviews = _reviews.asStateFlow()

    private val _date = MutableStateFlow<UiState<ResponseReviewDate>>(UiState.Loading)
    val date = _date.asStateFlow()

    private val _profile =
        MutableStateFlow<ResponseMySeatRecord.MyProfileResponse>(ResponseMySeatRecord.MyProfileResponse())
    val profile = _profile.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(EditUi.NONE)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editClickedEvent = MutableStateFlow(EditUi.NONE)
    val editClickedEvent = _editClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()

    private val _clickedReviewId = MutableStateFlow(0)
    val clickedReviewId = _clickedReviewId.asStateFlow()

    private val page = MutableStateFlow(0)

    private val _editReview =
        MutableStateFlow(ResponseMySeatRecord.ReviewResponse(id = 0, stadiumId = 0))
    val editReview = _editReview.asStateFlow()

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
        val dateState = date.value as? UiState.Success<ResponseReviewDate>

        val year = dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.year
        val month =
            dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.months?.firstOrNull { it.isClicked }?.month
        page.value = 0
        if (year == null || month == null) {
            _reviews.value = UiState.Failure("유효하지 않은 날짜입니다.")
        }


        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = null,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 100,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name
                )
            ).onSuccess { data ->
                Timber.d("GET_SEAT_RECORDS_TEST SUCCESS : $data")
                _profile.value = data.profile
                if (data.reviews.isNotEmpty()) {
                    page.value += 1
                    _reviews.value = UiState.Success(data)
                } else {
                    _reviews.value = UiState.Empty
                }
            }.onFailure {
                Timber.d("GET_SEAT_RECORDS_TEST FAIL : $it")
                _reviews.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }

    fun getLocalProfile() {
        _profile.value = profile.value.copy(
            level = sharedPreference.level,
            levelTitle = sharedPreference.levelTitle,
            nickname = sharedPreference.nickname,
            teamId = sharedPreference.teamId,
            teamName = sharedPreference.teamName,
            profileImage = sharedPreference.profileImage
        )
    }

    fun loadNextSeatRecords() {
        viewModelScope.launch {
            val year = (date.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
            val month =
                (date.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = (reviews.value as UiState.Success).data.nextCursor,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 100,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name
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
                    Timber.d("test year/month -> $selectedYear / $month")
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

    fun cancelEditEvent() {
        _editClickedEvent.value = EditUi.NONE
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
        _profile.value = profile.value.copy(
            nickname = nickname, profileImage = profileImage, teamId = teamId, teamName = teamName
        )
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
                            if (updatedList.isEmpty()) {
                                val dateState = date.value as UiState.Success
                                val clickedYear = dateState.data.yearMonths
                                    .find { year -> year.isClicked }?.year

                                val clickedMonth = dateState.data.yearMonths
                                    .find { year -> year.isClicked }
                                    ?.months
                                    ?.find { month -> month.isClicked }
                                    ?.month

                                Timber.d("test 년/월 : $clickedYear / $clickedMonth")

                                if (clickedYear != null && clickedMonth != null)
                                    removeEmptyDate(clickedYear, clickedMonth)
                            } else {
                                _reviews.value =
                                    UiState.Success(currentState.data.copy(reviews = updatedList))
                            }

                        }
                    }
                    .onFailure {
                        Timber.d("삭제 실패 : $it")
                    }
            }
            _deleteClickedEvent.value = EditUi.NONE
        }
    }

    private fun removeEmptyDate(year: Int, month: Int) {
        val dateState = _date.value as? UiState.Success<ResponseReviewDate>
        if (dateState != null) {
            val updatedYearMonths = dateState.data.yearMonths.mapNotNull { yearMonth ->
                if (yearMonth.year == year) {
                    if (month == 0 || yearMonth.months.size <= 2) {
                        null
                    } else {
                        val updatedMonths: List<ResponseReviewDate.MonthData> =
                            yearMonth.months.filter { it.month != month }.map { monthData ->
                                if (monthData.month == 0) {
                                    monthData.copy(isClicked = true)
                                } else {
                                    monthData
                                }
                            }
                        yearMonth.copy(months = updatedMonths)
                    }
                } else {
                    yearMonth
                }
            }
            val existingClickedYear = updatedYearMonths.find { it.isClicked }?.year
            if (existingClickedYear != null) {
                _date.value = if (updatedYearMonths.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(dateState.data.copy(yearMonths = updatedYearMonths))
                }
            } else {
                val highestYear = updatedYearMonths.maxOfOrNull { it.year }

                val updatedYearMonthsWithClicked = updatedYearMonths.map { yearMonth ->
                    if (yearMonth.year == highestYear) {
                        val updatedMonths = yearMonth.months.map { monthData ->
                            if (monthData.month == 0) {
                                monthData.copy(isClicked = true)
                            } else {
                                monthData.copy(isClicked = false)
                            }
                        }
                        yearMonth.copy(months = updatedMonths, isClicked = true)
                    } else {
                        yearMonth
                    }
                }
                _date.value = if (updatedYearMonths.isEmpty()) {
                    UiState.Empty
                } else {
                    Timber.d("test updatedYearMonthsWithClicked -> $updatedYearMonthsWithClicked")
                    UiState.Success(dateState.data.copy(yearMonths = updatedYearMonthsWithClicked))
                }
            }
        }
    }

    fun setEditReview(reviewId: Int) {
        _editReview.value = (_reviews.value as UiState.Success).data.reviews.first {
            it.id == reviewId
        }
    }


}

enum class EditUi {
    NONE,
    SEAT_RECORD,
    SEAT_DETAIL
}

enum class MySeatRecordSortBy {
    DATE_TIME
}