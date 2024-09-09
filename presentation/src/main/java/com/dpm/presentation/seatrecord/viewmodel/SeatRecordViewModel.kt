package com.dpm.presentation.seatrecord.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.core.state.UiState
import com.dpm.domain.entity.request.home.RequestEditReview
import com.dpm.domain.entity.request.home.RequestMySeatRecord
import com.dpm.domain.entity.response.home.ResponseMySeatRecord
import com.dpm.domain.entity.response.home.ResponseReviewDate
import com.dpm.domain.entity.response.home.ResponseUserInfo
import com.dpm.domain.entity.response.seatreview.ResponseSeatBlock
import com.dpm.domain.entity.response.seatreview.ResponseSeatRange
import com.dpm.domain.entity.response.seatreview.ResponseStadiumSection
import com.dpm.domain.model.seatrecord.RecordReviewType
import com.dpm.domain.model.seatreview.ValidSeat
import com.dpm.domain.preference.SharedPreference
import com.dpm.domain.repository.HomeRepository
import com.dpm.domain.repository.SeatReviewRepository
import com.dpm.domain.repository.ViewfinderRepository
import com.dpm.presentation.util.CalendarUtil.formatToDateFormat
import com.dpm.presentation.util.CalendarUtil.getMonthFromDateFormat
import com.dpm.presentation.util.CalendarUtil.getYearFromDateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SeatRecordViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val viewfinderRepository: ViewfinderRepository,
    private val seatReviewRepository: SeatReviewRepository,
    private val sharedPreference: SharedPreference,
    private val s3Url: String,
) : ViewModel() {

    companion object {
        const val MAX_IMAGE_CNT = 3
    }

    enum class ReviewType {
        SEAT_REVIEW,
        INTUITIVE_REVIEW
    }

    private val _seatReviews = MutableStateFlow<UiState<ResponseMySeatRecord>>(UiState.Loading)
    val seatReviews = _seatReviews.asStateFlow()

    private val _intuitiveReviews = MutableStateFlow<UiState<ResponseMySeatRecord>>(UiState.Loading)
    val intuitiveReviews = _intuitiveReviews.asStateFlow()

    private val _seatDate = MutableStateFlow<UiState<ResponseReviewDate>>(UiState.Loading)
    val seatDate = _seatDate.asStateFlow()

    private val _intuitiveDate = MutableStateFlow<UiState<ResponseReviewDate>>(UiState.Loading)
    val intuitiveDate = _intuitiveDate.asStateFlow()

    private val _profile =
        MutableStateFlow(ResponseUserInfo())
    val profile = _profile.asStateFlow()

    private val _deleteClickedEvent = MutableStateFlow(EditUi.NONE)
    val deleteClickedEvent = _deleteClickedEvent.asStateFlow()

    private val _editClickedEvent = MutableStateFlow(EditUi.NONE)
    val editClickedEvent = _editClickedEvent.asStateFlow()

    private val _editReviewId = MutableStateFlow(0)
    val editReviewId = _editReviewId.asStateFlow()

    private val _clickedReviewId = MutableStateFlow(0)
    val clickedReviewId = _clickedReviewId.asStateFlow()

    private val _currentReviewState = MutableStateFlow(ReviewType.SEAT_REVIEW)
    val currentReviewState = _currentReviewState.asStateFlow()

    /** 리뷰 수정 관련 데이터 **/
    private val _editReview =
        MutableStateFlow(ResponseMySeatRecord.ReviewResponse(id = 0, stadiumId = 0))
    val editReview = _editReview.asStateFlow()

    private val _stadiumSectionState =
        MutableStateFlow<UiState<ResponseStadiumSection>>(UiState.Empty)
    val stadiumSectionState: StateFlow<UiState<ResponseStadiumSection>> = _stadiumSectionState

    private val _seatBlockState = MutableStateFlow<UiState<List<ResponseSeatBlock>>>(UiState.Empty)
    val seatBlockState: StateFlow<UiState<List<ResponseSeatBlock>>> = _seatBlockState

    private val _seatRangeState = MutableStateFlow<UiState<List<ResponseSeatRange>>>(UiState.Empty)
    val seatRangeState: StateFlow<UiState<List<ResponseSeatRange>>> = _seatRangeState

    private val _selectedStadiumId = MutableStateFlow(0)
    val selectedStadiumId = _selectedStadiumId.asStateFlow()

    private val _selectedSectionId = MutableStateFlow(0)
    val selectedSectionId = _selectedSectionId.asStateFlow()

    private val _selectedBlockId = MutableStateFlow(0)
    val selectedBlockId: StateFlow<Int> = _selectedBlockId.asStateFlow()

    private val _selectedBlockName = MutableStateFlow("")
    val selectedBlockName: StateFlow<String> = _selectedBlockName.asStateFlow()

    private val _selectedColumn = MutableStateFlow("")
    val selectedColumn: StateFlow<String> = _selectedColumn.asStateFlow()

    private val _selectedNumber = MutableStateFlow("")
    val selectedNumber: StateFlow<String> = _selectedNumber.asStateFlow()

    private val _selectedSectionName = MutableStateFlow("")
    val selectedSectionName = _selectedSectionName.asStateFlow()

    val userSeatState = MutableLiveData<ValidSeat>()

    private val _selectedGoodReview = MutableStateFlow<List<String>>(emptyList())
    val selectedGoodReview: StateFlow<List<String>> = _selectedGoodReview.asStateFlow()

    private val _selectedBadReview = MutableStateFlow<List<String>>(emptyList())
    val selectedBadReview: StateFlow<List<String>> = _selectedBadReview.asStateFlow()

    private val _detailReviewText = MutableStateFlow("")
    val detailReviewText: StateFlow<String> = _detailReviewText.asStateFlow()

    private val _putReviewState =
        MutableStateFlow<UiState<ResponseMySeatRecord.ReviewResponse>>(UiState.Empty)
    val putReviewState = _putReviewState.asStateFlow()

    private val _uploadImageCount = MutableStateFlow(0)
    val uploadImageCount = _uploadImageCount.asStateFlow()

    private var presignedUrls = MutableList(MAX_IMAGE_CNT) { "" }

    fun getSeatReviewDate() {
        viewModelScope.launch {
            homeRepository.getReviewDate(
                reviewType = RecordReviewType.VIEW.name
            )
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
            homeRepository.getReviewDate(
                reviewType = RecordReviewType.FEED.name
            )
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

    fun getProfile() {
        viewModelScope.launch {
            homeRepository.getMyUserInfo()
                .onSuccess { data ->
                    _profile.value = data
                    saveLocalProfile()
                }
                .onFailure {
                    getLocalProfile()
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
                    sortBy = MySeatRecordSortBy.DATE_TIME.name,
                    reviewType = RecordReviewType.VIEW
                )
            ).onSuccess { data ->
                Timber.d("GET_SEAT_RECORDS_TEST SUCCESS : $data")
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
                    sortBy = MySeatRecordSortBy.DATE_TIME.name,
                    reviewType = RecordReviewType.FEED
                )
            ).onSuccess { data ->
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
        val profile = _profile.value
        sharedPreference.level = profile.level
        sharedPreference.profileImage = profile.profileImage
        sharedPreference.nickname = profile.nickname
        sharedPreference.teamId = profile.teamId ?: 0
        sharedPreference.teamName = profile.teamName ?: ""
        sharedPreference.levelTitle = profile.levelTitle

    }

    fun getNextSeatReviews() {
        viewModelScope.launch {
            val year =
                (seatDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
            val month =
                (seatDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = (seatReviews.value as UiState.Success).data.nextCursor,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 10,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name,
                    reviewType = RecordReviewType.VIEW
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
            val year =
                (intuitiveDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.year
            val month =
                (intuitiveDate.value as UiState.Success).data.yearMonths.first { it.isClicked }.months.first { it.isClicked }.month
            homeRepository.getMySeatRecord(
                RequestMySeatRecord(
                    cursor = (intuitiveReviews.value as UiState.Success).data.nextCursor,
                    year = year,
                    month = month.takeIf { it != 0 },
                    size = 10,
                    sortBy = MySeatRecordSortBy.DATE_TIME.name,
                    reviewType = RecordReviewType.FEED
                )
            ).onSuccess { data ->
                Timber.d("NEXT_SEAT_RECORDS_SUCCESS : $data")
                val updatedReviewList =
                    (_intuitiveReviews.value as UiState.Success).data.reviews + data.reviews
                _intuitiveReviews.value = UiState.Success(data.copy(reviews = updatedReviewList))
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
            _seatDate.value =
                UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }

    fun setIntuitiveSelectedYear(year: Int) {
        val currentState = intuitiveDate.value
        if (currentState is UiState.Success) {
            val updatedYearMonths = currentState.data.yearMonths.map { yearMonth ->
                yearMonth.copy(
                    isClicked = yearMonth.year == year
                )
            }
            _intuitiveDate.value =
                UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
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
            _seatDate.value =
                UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }

    fun setIntuitiveSelectedMonth(month: Int) {
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
            _intuitiveDate.value =
                UiState.Success(currentState.data.copy(yearMonths = updatedYearMonths))
        }
    }


    /** 직관 후기는 스크랩, 좋아요 없음 -> 좌석시야만 **/
    fun updateLike(id: Int) {
        viewModelScope.launch {
            viewfinderRepository.updateLike(id).onSuccess {
                val currentState = (_seatReviews.value as? UiState.Success)?.data
                if (currentState != null) {
                    val updatedList = currentState.reviews.map { review ->
                        if (review.id == id) {
                            review.copy(
                                isLiked = !review.isLiked,
                                likesCount = if (review.isLiked) {
                                    review.likesCount - 1
                                } else {
                                    review.likesCount + 1
                                }
                            )
                        } else {
                            review
                        }
                    }

                    _seatReviews.value = UiState.Success(
                        data = currentState.copy(updatedList)
                    )
                }
            }.onFailure {
                Timber.d("좋아요 실패 $it")
            }
        }
    }

    fun updateScrap(id: Int) {
        viewModelScope.launch {
            viewfinderRepository.updateScrap(id).onSuccess {
                val currentState = (_seatReviews.value as? UiState.Success)?.data
                if (currentState != null) {
                    val updatedList = currentState.reviews.map { review ->
                        if (review.id == id) {
                            review.copy(
                                isScrapped = !review.isScrapped,
                                scrapsCount = if (review.isScrapped) {
                                    review.scrapsCount - 1
                                } else {
                                    review.scrapsCount + 1
                                }
                            )
                        } else {
                            review
                        }
                    }
                    _seatReviews.value = UiState.Success(
                        data = currentState.copy(reviews = updatedList)
                    )
                }
            }.onFailure {
                Timber.d("스크랩 실패 $it")
            }
        }
    }

    fun setReviewState(reviewType: ReviewType) {
        _currentReviewState.value = reviewType
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
        _profile.value = profile.value.copy(
            nickname = nickname, profileImage = profileImage, teamId = teamId, teamName = teamName
        )
    }

    fun removeReview() {
        when (currentReviewState.value) {
            ReviewType.SEAT_REVIEW -> {
                removeSeatReviewData()
            }

            ReviewType.INTUITIVE_REVIEW -> {
                removeIntuitiveReviewData()
            }
        }
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

    fun removeIntuitiveReviewData() {
        val currentState = intuitiveReviews.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                homeRepository.deleteReview(editReviewId.value)
                    .onSuccess {
                        if (it.reviewId == editReviewId.value) {
                            val updatedList = currentState.data.reviews.filter { review ->
                                review.id != editReviewId.value
                            }
                            if (updatedList.isEmpty()) {
                                val dateState = intuitiveDate.value as UiState.Success
                                val clickedYear = dateState.data.yearMonths
                                    .find { year -> year.isClicked }?.year

                                val clickedMonth = dateState.data.yearMonths
                                    .find { year -> year.isClicked }
                                    ?.months
                                    ?.find { month -> month.isClicked }
                                    ?.month

                                Timber.d("test 년/월 : $clickedYear / $clickedMonth")

                                if (clickedYear != null && clickedMonth != null)
                                    removeIntuitiveEmptyDate(clickedYear, clickedMonth)
                            } else {
                                _intuitiveReviews.value =
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
                if (updatedYearMonths.isEmpty()) {
                    _seatDate.value = UiState.Empty
                    _seatReviews.value = UiState.Empty
                } else {
                    _seatDate.value =
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
                if (updatedYearMonths.isEmpty()) {
                    _seatDate.value = UiState.Empty
                    _seatReviews.value = UiState.Empty
                } else {
                    Timber.d("test updatedYearMonthsWithClicked -> $updatedYearMonthsWithClicked")
                    _seatDate.value =
                        UiState.Success(dateState.data.copy(yearMonths = updatedYearMonthsWithClicked))
                }
            }
        }
    }

    private fun removeIntuitiveEmptyDate(year: Int, month: Int) {
        val dateState = _intuitiveDate.value as? UiState.Success<ResponseReviewDate>
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
                if (updatedYearMonths.isEmpty()) {
                    _intuitiveDate.value = UiState.Empty
                    _intuitiveReviews.value = UiState.Empty
                } else {
                    _intuitiveDate.value =
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
                if (updatedYearMonths.isEmpty()) {
                    _intuitiveDate.value = UiState.Empty
                    _intuitiveReviews.value = UiState.Empty
                } else {
                    _intuitiveDate.value =
                        UiState.Success(dateState.data.copy(yearMonths = updatedYearMonthsWithClicked))
                }
            }
        }
    }

    /** 게시물 수정 관련 로직 */
    fun setEditReview(reviewId: Int) {
        _editReview.value = when (currentReviewState.value) {
            ReviewType.SEAT_REVIEW -> {
                (_seatReviews.value as UiState.Success).data.reviews.first {
                    it.id == reviewId
                }
            }

            ReviewType.INTUITIVE_REVIEW -> {
                (_intuitiveReviews.value as UiState.Success).data.reviews.first {
                    it.id == reviewId
                }
            }
        }
        removeEditValue()
    }

    private fun removeEditValue() {
        _stadiumSectionState.value = UiState.Empty
        _seatBlockState.value = UiState.Empty
        _seatRangeState.value = UiState.Empty
        _selectedStadiumId.value = 0
        _selectedSectionId.value = 0
        _selectedBlockId.value = 0
        _selectedBlockName.value = ""
        _selectedColumn.value = ""
        _selectedNumber.value = ""
        _selectedSectionName.value = ""
        userSeatState.value = ValidSeat.VALID
        _selectedGoodReview.value = emptyList()
        _selectedBadReview.value = emptyList()
        _detailReviewText.value = ""
        _putReviewState.value = UiState.Empty
        _uploadImageCount.value = 0
        presignedUrls = MutableList(MAX_IMAGE_CNT) { "" }
    }

    fun updateEditSelectedDate(date: String) {
        _editReview.value = editReview.value.copy(date = date)
    }

    fun addEditSelectedImages(images: List<String>) {
        _editReview.value = editReview.value.copy(images = editReview.value.images + images.map {
            ResponseMySeatRecord.ReviewResponse.ReviewImageResponse(id = 0, url = it)
        })
    }

    fun removeEditImage(index: Int) {
        if (index < editReview.value.images.size) {
            val updatedImages = editReview.value.images.toMutableList().apply { removeAt(index) }
            _editReview.value = editReview.value.copy(images = updatedImages)
        }
    }

    /***
     * TODO : !!!!
     * 추후에 수정 업로드 진행할때 -> 사진에 대해서 기존에 INITIAL이랑 구분해서 PRESIGNED 받아와야함
     * 즉, 기존 URL이랑 같으면 -> 패스 / 다르면 -> presignedurl 받아와서 업로드 진행
     */


    fun getStadiumSection(stadiumId: Int) {
        viewModelScope.launch {
            _stadiumSectionState.value = UiState.Loading
            seatReviewRepository.getStadiumSection(
                stadiumId
            ).onSuccess { section ->
                if (section == null) {
                    _stadiumSectionState.value = UiState.Empty
                    return@launch
                }
                _stadiumSectionState.value = when {
                    section.sectionList.isEmpty() -> UiState.Empty
                    else -> UiState.Success(section)
                }
            }.onFailure { it ->
                _stadiumSectionState.value = UiState.Failure(it.toString())
            }
        }
    }


    fun initSelectedSeatValue() {
        _selectedStadiumId.value = 0
        _selectedSectionId.value = 0
        _selectedBlockId.value = 0
        _selectedBlockName.value = ""
        _selectedColumn.value = ""
        _selectedNumber.value = ""
        _selectedSectionName.value = ""
        _stadiumSectionState.value = UiState.Empty
        _seatBlockState.value = UiState.Empty
        _seatRangeState.value = UiState.Empty
    }

    fun updateEditReviewSeat() {
        _editReview.value = editReview.value.copy(
            stadiumId = selectedStadiumId.value,
            stadiumName = editReview.value.stadiumName,
            /** -> 추후 수정*/
            blockId = selectedBlockId.value,
            blockCode = selectedBlockName.value,
            rowNumber = selectedColumn.value.toIntOrNull() ?: 1,
            seatNumber = selectedNumber.value.toIntOrNull(),
            sectionId = selectedSectionId.value,
            sectionName = selectedSectionName.value
        )
    }

    fun getSeatBlock() {
        if (selectedStadiumId.value == 0 || selectedSectionId.value == 0) return
        viewModelScope.launch {
            _seatBlockState.value = UiState.Loading
            seatReviewRepository.getSeatBlock(
                selectedStadiumId.value,
                selectedSectionId.value
            ).onSuccess { blocks ->
                if (blocks.isEmpty()) {
                    _seatBlockState.value = UiState.Empty
                } else {
                    _seatBlockState.value = UiState.Success(blocks)
                }
            }.onFailure { message ->
                _seatBlockState.value = UiState.Failure(message.toString())
            }
        }
    }

    fun getBlockListName(blockCode: String): String {
        return when {
            selectedSectionId.value == 10 && blockCode.endsWith("w") -> {
                when (val codeWithoutW = blockCode.removeSuffix("w")) {
                    "101", "102", "121", "122" -> "레드-$codeWithoutW"
                    "109", "114" -> "블루-$codeWithoutW"
                    else -> codeWithoutW
                }
            }

            selectedSectionId.value == 8 && blockCode.startsWith("exciting") -> {
                when (blockCode) {
                    "exciting1" -> "1루 익사이팅석"
                    "exciting3" -> "3루 익사이팅석"
                    else -> blockCode
                }
            }

            selectedSectionId.value == 1 && blockCode.startsWith("premium") -> {
                when (blockCode) {
                    "premium" -> "프리미엄석"
                    else -> blockCode
                }
            }

            else -> blockCode
        }
    }

    fun setSelectedBlock(block: String) {
        _selectedBlockName.value = block
    }

    fun updateSelectedBlockId(blockId: Int) {
        _selectedBlockId.value = blockId
    }

    fun updateSelectedSectionId(sectionId: Int) {
        _selectedSectionId.value = sectionId
    }

    fun updateSelectedStadiumId(stadiumId: Int) {
        _selectedStadiumId.value = stadiumId
    }

    fun setSelectedColumn(column: String) {
        _selectedColumn.value = column
    }

    fun setSelectedNumber(number: String) {
        _selectedNumber.value = number
    }

    fun setSelectedSeatZone(name: String) {
        _selectedSectionName.value = name
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
    }

    fun setSelectedGoodReview(buttonTexts: List<String>) {
        _selectedGoodReview.value = buttonTexts
    }

    fun setSelectedBadReview(buttonTexts: List<String>) {
        _selectedBadReview.value = buttonTexts
    }

    fun updateEditReviewDetail() {
        val goodKeywords = _selectedGoodReview.value.map {
            ResponseMySeatRecord.ReviewResponse.ReviewKeywordResponse(
                content = it,
                isPositive = true
            )
        }
        val badKeywords = _selectedBadReview.value.map {
            ResponseMySeatRecord.ReviewResponse.ReviewKeywordResponse(
                content = it,
                isPositive = false
            )
        }
        _editReview.value = editReview.value.copy(
            keywords = goodKeywords + badKeywords,
            content = detailReviewText.value
        )
    }


    fun getSeatRange() {
        if (selectedStadiumId.value == 0 || selectedSectionId.value == 0) return
        viewModelScope.launch {
            _seatRangeState.value = UiState.Loading
            seatReviewRepository.getSeatRange(
                selectedStadiumId.value,
                selectedSectionId.value
            ).onSuccess { data ->
                if (data.isNotEmpty()) {
                    _seatRangeState.value = UiState.Success(data)
                } else {
                    _seatRangeState.value = UiState.Empty
                }
            }.onFailure { message ->
                _seatRangeState.value = UiState.Failure(message.toString())
            }
        }
    }

    fun requestPresignedUrl(index: Int, fileExtension: String, imageData: ByteArray) {
        viewModelScope.launch {
            seatReviewRepository.postReviewImagePresigned(fileExtension)
                .onSuccess { response ->
                    uploadReviewImageToPresignedUrl(response.presignedUrl, imageData, index)
                }
                .onFailure { t ->
                    Timber.e("presignedUrl 받아오기 실패 : $t")
                }
        }
    }

    fun updatePresignedUrl(index: Int, imageUrl: String) {
        presignedUrls[index] = imageUrl
        plusUploadImageCount()
    }

    private fun uploadReviewImageToPresignedUrl(
        presignedUrl: String,
        imageData: ByteArray,
        index: Int,
    ) {
        viewModelScope.launch {
            seatReviewRepository.putImagePreSignedUrl(presignedUrl, imageData)
                .onSuccess {
                    presignedUrls[index] = removeQueryParameters(presignedUrl)
                    plusUploadImageCount()
                }
                .onFailure { t ->
                    Timber.e("presignedUrl 업로드 실패 : $presignedUrl / $t")
                }
        }
    }

    private fun plusUploadImageCount() {
        _uploadImageCount.value = uploadImageCount.value + 1
    }

    fun putEditReview() {
        viewModelScope.launch {
            val requestEditReview = RequestEditReview(
                stadiumId = editReview.value.stadiumId,
                blockId = editReview.value.blockId,
                rowNumber = if (editReview.value.rowNumber == 0) null else editReview.value.rowNumber,
                seatNumber = if (editReview.value.seatNumber == 0) null else editReview.value.seatNumber,
                images = presignedUrls.filter { it.isNotEmpty() },
                dateTime = formatToDateFormat(editReview.value.date),
                good = editReview.value.keywords.filter { it.isPositive }.map { it.content },
                bad = editReview.value.keywords.filter { !it.isPositive }.map { it.content },
                content = editReview.value.content,
                reviewType = if (currentReviewState.value == ReviewType.SEAT_REVIEW) RecordReviewType.VIEW.name else RecordReviewType.FEED.name
            )

            Timber.d("editReview -> Selected Images: ${presignedUrls.filter { it.isNotEmpty() }}")
            Timber.d("editReview -> Selected Date: ${formatToDateFormat(editReview.value.date)}")
            Timber.d(
                "editReview -> Good Review: ${
                    editReview.value.keywords.filter { it.isPositive }.map { it.content }
                }"
            )
            Timber.d(
                "editReview -> etail Review Text: ${
                    editReview.value.keywords.filter { !it.isPositive }.map { it.content }
                }"
            )
            Timber.d("editReview -> Selected Stadium ID: ${editReview.value.stadiumId}")
            Timber.d("editReview -> Selected Block ID: ${editReview.value.blockId}")
            Timber.d("editReview -> Selected seatColumn(row): ${editReview.value.rowNumber}")
            Timber.d("editReview -> Selected seatNumber: ${editReview.value.seatNumber}")
            Timber.d("editReview -> Selected reviewType: ${if (currentReviewState.value == ReviewType.SEAT_REVIEW) RecordReviewType.VIEW.name else RecordReviewType.FEED.name}")

            _putReviewState.value = UiState.Loading

            /**
             * TODO : 만약에 날짜가 달라지면 또 어덯게 처리를 해야하나?
             * 1. success를 하였을 때
             *   a.날짜 년도만 달라진다면 -> 현재 보여주는 날짜에서 지워주고 그 해당 년도로 업데이트를 해야할지?
             *   b.달만 달라진다면 -> 현재 해당상태가 달인지(전체 달) 아니면 특정 달인지에 대해서 케이스 처리
             *   c.년도, 달이 달라진다면 -> 음..
             *     case 1) 달라졌을때 존재하지 않는다면 -> 년도,월 데이터를 업데이트를 해줘야함
             *     case 2) 달라졌지만 존재한다면 -> 현재 클릭한 것이 아니라면 없애주고 처리를 해줘야할듯?
             *   d. 달라지지 않는 경우 -> 그대로 진행 (해당 id에 대한 리스트 업데이트만 해주면 됨)
             * 2. 통신 실패 -> 다시 처음부터 진행?
             *    FAILURE -> 메세지 -> 다시 클릭 -> FAIL 체크 -> 통신만 진행
             */
            homeRepository.putEditReview(
                editReview.value.id,
                requestEditReview
            ).onSuccess { review ->
                val dateState = seatDate.value as? UiState.Success<ResponseReviewDate>
                val selectedYear = dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.year
                val selectedMonth =
                    dateState?.data?.yearMonths?.firstOrNull { it.isClicked }?.months?.firstOrNull { it.isClicked }?.month
                val editYear = getYearFromDateFormat(review.date)
                val editMonth = getMonthFromDateFormat(review.date)
                Timber.d("date 비교 : selectedYear : $selectedYear, selectedMonth : $selectedMonth, editYear : $editYear, editMonth : $editMonth")
                if(selectedYear != editYear || selectedMonth != editMonth) {
                    when (currentReviewState.value) {
                        ReviewType.SEAT_REVIEW -> {
                            val currentState = _seatReviews.value
                            if (currentState is UiState.Success) {
                                val reviewList = currentState.data.reviews.toMutableList()
                                val index = reviewList.indexOfFirst { it.id == review.id }

                                if (index != -1) {
                                    if (selectedMonth == 0 || selectedMonth == editMonth) {
                                        reviewList[index] = review
                                    } else {
                                        updateDate(editYear,editMonth)
                                        reviewList.removeAt(index)
                                    }
                                }

                                _seatReviews.value = currentState.copy(
                                    data = currentState.data.copy(reviews = reviewList)
                                )
                            }
                        }

                        ReviewType.INTUITIVE_REVIEW -> {
                            val currentState = _intuitiveReviews.value
                            if (currentState is UiState.Success) {
                                val reviewList = currentState.data.reviews.toMutableList()
                                val index = reviewList.indexOfFirst { it.id == review.id }

                                if (index != -1) {
                                    if (selectedMonth == 0 || selectedMonth == editMonth) {
                                        reviewList[index] = review
                                    } else {
                                        updateDate(editYear,editMonth)
                                        reviewList.removeAt(index)
                                    }
                                }

                                _intuitiveReviews.value = currentState.copy(
                                    data = currentState.data.copy(reviews = reviewList)
                                )
                            }
                        }
                    }
                }else {
                    when (currentReviewState.value) {
                        ReviewType.SEAT_REVIEW -> {
                            val currentState = _seatReviews.value
                            if (currentState is UiState.Success) {
                                val reviewList = currentState.data.reviews.toMutableList()
                                val index = reviewList.indexOfFirst { it.id == review.id }
                                if (index != -1) {
                                    reviewList[index] = review
                                }
                                _seatReviews.value = currentState.copy(
                                    data = currentState.data.copy(reviews = reviewList)
                                )
                            }
                        }

                        ReviewType.INTUITIVE_REVIEW -> {
                            val currentState = _intuitiveReviews.value
                            if (currentState is UiState.Success) {
                                val reviewList = currentState.data.reviews.toMutableList()
                                val index = reviewList.indexOfFirst { it.id == review.id }
                                if (index != -1) {
                                    reviewList[index] = review
                                }
                                _intuitiveReviews.value = currentState.copy(
                                    data = currentState.data.copy(reviews = reviewList)
                                )
                            }
                        }
                    }
                }
                _putReviewState.value = UiState.Success(review)
            }.onFailure {
                _putReviewState.value = UiState.Failure(it.message.toString())
            }
        }


    }

    private fun updateDate(newYear: Int, newMonth: Int) {
        when (currentReviewState.value) {
            ReviewType.SEAT_REVIEW -> {
                val exists = (_seatDate.value as UiState.Success).data.yearMonths
                    .find { it.year == newYear }
                    ?.months
                    ?.any { it.month == newMonth } == true
            }

            ReviewType.INTUITIVE_REVIEW -> {
                val exists = (_intuitiveDate.value as UiState.Success).data.yearMonths
                    .find { it.year == newYear }
                    ?.months
                    ?.any { it.month == newMonth } == true
            }
        }
    }

    private fun removeQueryParameters(url: String): String {
        val uri = Uri.parse(url)
        return uri.buildUpon().clearQuery().build().toString()
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