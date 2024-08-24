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

    private val _seatReviews = MutableStateFlow<UiState<ResponseMySeatRecord>>(UiState.Loading)
    val seatReviews = _seatReviews.asStateFlow()

    private val _intuitiveReviews = MutableStateFlow<UiState<ResponseMySeatRecord>>(UiState.Loading)
    val intuitiveReviews = _intuitiveReviews.asStateFlow()

    private val _seatDate = MutableStateFlow<UiState<ResponseReviewDate>>(UiState.Loading)
    val seatDate = _seatDate.asStateFlow()

    private val _intuitiveDate = MutableStateFlow<UiState<ResponseReviewDate>>(UiState.Loading)
    val intuitiveDate = _intuitiveDate.asStateFlow()

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

    private val _editReview =
        MutableStateFlow(ResponseMySeatRecord.ReviewResponse(id = 0, stadiumId = 0))
    val editReview = _editReview.asStateFlow()


    fun getSeatReviewDate() {
        viewModelScope.launch {
            homeRepository.getReviewDate()
                .onSuccess { data ->
                    if (data.yearMonths.isNotEmpty()) {
                        _seatDate.value = UiState.Success(data)
                    } else {
                        _seatDate.value = UiState.Empty
                    }
                }
                .onFailure { e ->
                    _seatDate.value = UiState.Failure(e.message ?: "실패")
                }
        }
    }

    fun getIntuitiveReviewDate() {
        viewModelScope.launch {
            homeRepository.getReviewDate()
                .onSuccess { data ->
                    if (data.yearMonths.isNotEmpty()) {
                        _intuitiveDate.value = UiState.Success(data)
                    } else {
                        _intuitiveDate.value = UiState.Empty
                    }
                }
                .onFailure { e ->
                    _intuitiveDate.value = UiState.Failure(e.message ?: "실패")
                }
        }
    }


    fun getSeatReviews() {
        val dateState = seatDate.value as? UiState.Success<ResponseReviewDate>

        val year = dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.year
        val month =
            dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.months?.firstOrNull { it.isClicked }?.month
        if (year == null || month == null) {
            _seatReviews.value = UiState.Failure("유효하지 않은 날짜입니다.")
        }


        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = null,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 10,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name
                )
            ).onSuccess { data ->
                Timber.d("GET_SEAT_RECORDS_TEST SUCCESS : $data")
                _profile.value = data.profile
                saveLocalProfile()
                if (data.reviews.isNotEmpty()) {
                    _seatReviews.value = UiState.Success(data)
                } else {
                    _seatReviews.value = UiState.Empty
                }
            }.onFailure {
                Timber.d("GET_SEAT_RECORDS_TEST FAIL : $it")
                _seatReviews.value = UiState.Failure(it.message ?: "실패")
            }
        }
    }

    fun getIntuitiveReviews() {
        val dateState = intuitiveDate.value as? UiState.Success<ResponseReviewDate>

        val year = dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.year
        val month =
            dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.months?.firstOrNull { it.isClicked }?.month
        if (year == null || month == null) {
            _intuitiveReviews.value = UiState.Failure("유효하지 않은 날짜입니다.")
        }


        viewModelScope.launch {
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = null,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 10,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name
                )
            ).onSuccess { data ->
                saveLocalProfile()
                if (data.reviews.isNotEmpty()) {
                    _intuitiveReviews.value = UiState.Success(data)
                } else {
                    _intuitiveReviews.value = UiState.Empty
                }
            }.onFailure {
                _intuitiveReviews.value = UiState.Failure(it.message ?: "실패")
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

    private fun saveLocalProfile() {
        val currentState = _seatReviews.value
        if(currentState is UiState.Success) {
            val profile = currentState.data.profile
            sharedPreference.level = profile.level
            sharedPreference.profileImage = profile.profileImage
            sharedPreference.nickname = profile.nickname
            sharedPreference.teamId = profile.teamId!!
            sharedPreference.teamName = profile.teamName!!
            sharedPreference.levelTitle = profile.levelTitle
        }
    }

    fun getNextSeatReviews() {
        viewModelScope.launch {
            val year = (seatDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
            val month =
                (seatDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = (seatReviews.value as UiState.Success).data.nextCursor,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 10,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name
                )
            ).onSuccess { data ->
                Timber.d("NEXT_SEAT_RECORDS_SUCCESS : $data")
                val updatedReviewList =
                    (_seatReviews.value as UiState.Success).data.reviews + data.reviews
                _seatReviews.value = UiState.Success(data.copy(reviews = updatedReviewList))
            }.onFailure {
                Timber.d("NEXT_SEAT_RECORDS_FAIL : $it")
            }
        }
    }

    fun getNextIntuitiveReviews() {
        viewModelScope.launch {
            val year = (seatDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
            val month =
                (seatDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = (seatReviews.value as UiState.Success).data.nextCursor,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 10,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name
                )
            ).onSuccess { data ->
                Timber.d("NEXT_SEAT_RECORDS_SUCCESS : $data")
                val updatedReviewList =
                    (_seatReviews.value as UiState.Success).data.reviews + data.reviews
                _seatReviews.value = UiState.Success(data.copy(reviews = updatedReviewList))
            }.onFailure {
                Timber.d("NEXT_SEAT_RECORDS_FAIL : $it")
            }
        }
    }

    fun setSeatSelectedYear(year: Int) {
        val currentState = seatDate.value
        if (currentState is UiState.Success) {
            val updatedYearMonths = currentState.data.yearMonths.map { yearMonth ->
                yearMonth.copy(
                    isClicked = yearMonth.year == year
                )
            }
            _seatDate.value = UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }

    fun setIntuitiveSelectedYear(year : Int){
        val currentState = intuitiveDate.value
        if (currentState is UiState.Success) {
            val updatedYearMonths = currentState.data.yearMonths.map { yearMonth ->
                yearMonth.copy(
                    isClicked = yearMonth.year == year
                )
            }
            _intuitiveDate.value = UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }

    fun setSeatSelectedMonth(month: Int) {
        val currentState = _seatDate.value
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
            _seatDate.value = UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }

    fun setIntuitiveSelectedMonth(month : Int){
        val currentState = _intuitiveDate.value
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
            _intuitiveDate.value = UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
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
        val currentState = _seatReviews.value
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

            _seatReviews.value = UiState.Success(updatedData)
        }
        _profile.value = profile.value.copy(
            nickname = nickname, profileImage = profileImage, teamId = teamId, teamName = teamName
        )
    }

    fun removeSeatReviewData() {
        val currentState = seatReviews.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                homeRepository.deleteReview(editReviewId.value)
                    .onSuccess {
                        if (it.reviewId == editReviewId.value) {
                            val updatedList = currentState.data.reviews.filter { review ->
                                review.id != editReviewId.value
                            }
                            if (updatedList.isEmpty()) {
                                val dateState = seatDate.value as UiState.Success
                                val clickedYear = dateState.data.yearMonths
                                    .find { year -> year.isClicked }?.year

                                val clickedMonth = dateState.data.yearMonths
                                    .find { year -> year.isClicked }
                                    ?.months
                                    ?.find { month -> month.isClicked }
                                    ?.month

                                Timber.d("test 년/월 : $clickedYear / $clickedMonth")

                                if (clickedYear != null && clickedMonth != null)
                                    removeSeatEmptyDate(clickedYear, clickedMonth)
                            } else {
                                _seatReviews.value =
                                    UiState.Success(currentState.data.copy(reviews = updatedList))
                            }
                            _profile.value = profile.value.copy(
                                reviewCount = maxOf(0, profile.value.reviewCount - 1)
                            )
                        }
                    }
                    .onFailure {
                        Timber.d("삭제 실패 : $it")
                    }
            }
            _deleteClickedEvent.value = EditUi.NONE
        }
    }

    private fun removeSeatEmptyDate(year: Int, month: Int) {
        val dateState = _seatDate.value as? UiState.Success<ResponseReviewDate>
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
                _seatDate.value = if (updatedYearMonths.isEmpty()) {
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
                _seatDate.value = if (updatedYearMonths.isEmpty()) {
                    UiState.Empty
                } else {
                    Timber.d("test updatedYearMonthsWithClicked -> $updatedYearMonthsWithClicked")
                    UiState.Success(dateState.data.copy(yearMonths = updatedYearMonthsWithClicked))
                }
            }
        }
    }

    fun setEditReview(reviewId: Int) {
        _editReview.value = (_seatReviews.value as UiState.Success).data.reviews.first {
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