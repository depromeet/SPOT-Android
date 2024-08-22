package com.dpm.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpm.domain.entity.request.viewfinder.RequestBlockReviewQuery
import com.dpm.domain.entity.response.viewfinder.ResponseBlockRow
import com.dpm.domain.model.viewfinder.Seat
import com.dpm.domain.repository.ViewfinderRepository
import com.dpm.presentation.viewfinder.uistate.StadiumDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Sort {
    DATE_TIME, LIKES_COUNT;
}

@HiltViewModel
class StadiumDetailViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository
) : ViewModel() {
    var blockCode: String = ""
    var stadiumId: Int = 0
    private var reset: Boolean = true
    private var blockRow: ResponseBlockRow? = null

    private val _detailUiState =
        MutableStateFlow<StadiumDetailUiState>(StadiumDetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    private val _scrollState = MutableStateFlow(false)
    val scrollState = _scrollState.asStateFlow()

    private val _bottomPadding = MutableStateFlow(0f)
    val bottomPadding = _bottomPadding.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _reviewFilter = MutableStateFlow<RequestBlockReviewQuery>(
        RequestBlockReviewQuery(
            rowNumber = null,
            seatNumber = null,
            year = null,
            month = null,
            cursor = null,
            sortBy = Sort.DATE_TIME.name,
            size = 20
        )
    )
    val reviewFilter = _reviewFilter.asStateFlow()

    fun updateCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    fun updateBottomPadding(padding: Float) {
        _bottomPadding.value = padding
    }

    fun updateScrollState(state: Boolean) {
        _scrollState.value = state
    }

    fun updateMonth(month: Int?) {
        if (month != _reviewFilter.value.month) {
            reset = true
            _reviewFilter.value = _reviewFilter.value.copy(month = month, cursor = null)
            getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
        }
    }

    fun updateSeat(column: Int, number: Int? = null) {
        reset = true
        _reviewFilter.value =
            _reviewFilter.value.copy(rowNumber = column, seatNumber = number, cursor = null)
        getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
    }

    fun updateSort(sortBy: String) {
        if (_reviewFilter.value.sortBy != sortBy) {
            reset = true
            _reviewFilter.value = _reviewFilter.value.copy(sortBy = sortBy, cursor = null)
            getBlockReviews(query = _reviewFilter.value)
        }
    }

    fun updateRequestPathVariable(stadiumId: Int, blockCode: String) {
        this.stadiumId = stadiumId
        this.blockCode = blockCode
    }

    fun getBlockReviews(
        stadiumId: Int = this.stadiumId,
        blockCode: String = this.blockCode,
        query: RequestBlockReviewQuery = this._reviewFilter.value,
    ) {
        viewModelScope.launch {
            viewfinderRepository.getBlockReviews(stadiumId, blockCode, query)
                .onSuccess { blockReviews ->
                    _reviewFilter.value = _reviewFilter.value.copy(
                        cursor = blockReviews.nextCursor
                    )
                    if (blockReviews.topReviewImages.isEmpty() && blockReviews.reviews.isEmpty()) {
                        _detailUiState.value = StadiumDetailUiState.Empty
                    } else {
                        if (_detailUiState.value is StadiumDetailUiState.ReviewsData) {
                            val reviewsData =
                                (_detailUiState.value as StadiumDetailUiState.ReviewsData)

                            if (reset) {
                                reset = false
                                _detailUiState.value = reviewsData.copy(
                                    reviews = blockReviews.reviews,
                                    hasNext = blockReviews.hasNext,
                                    total = blockReviews.totalElements,
                                    cursor = blockReviews.nextCursor
                                )
                            } else {
                                val uReviewsData = reviewsData.reviews.toMutableList()
                                uReviewsData.addAll(blockReviews.reviews)
                                _detailUiState.value = reviewsData.copy(
                                    reviews = uReviewsData,
                                    hasNext = blockReviews.hasNext,
                                    cursor = blockReviews.nextCursor
                                )
                            }

                        } else {
                            reset = false
                            _detailUiState.value = StadiumDetailUiState.ReviewsData(
                                topReviewImages = blockReviews.topReviewImages,
                                stadiumContent = blockReviews.location,
                                keywords = blockReviews.keywords,
                                total = blockReviews.totalElements,
                                reviews = blockReviews.reviews,
                                cursor = blockReviews.nextCursor,
                                hasNext = blockReviews.hasNext
                            )
                        }
                    }
                }.onFailure { e ->
                    _detailUiState.value = StadiumDetailUiState.Failed(e.message ?: "failed")
                }
        }
    }

    fun getBlockRow(stadiumId: Int, blockCode: String) {
        viewModelScope.launch {
            viewfinderRepository.getBlockRow(stadiumId, blockCode).onSuccess { blockRow ->
                this@StadiumDetailViewModel.blockRow = blockRow
            }.onFailure { e ->
                this@StadiumDetailViewModel.blockRow = null
            }
        }
    }

    fun handleColumNumber(
        column: Int,
        number: Int,
        response: (isSuccess: Boolean, seat: Seat) -> Unit
    ) {
        when (blockRow?.isCheck(column, number)) {
            Seat.COLUMN -> response(false, Seat.COLUMN)
            Seat.NUMBER -> response(false, Seat.NUMBER)
            Seat.COLUMN_NUMBER -> response(false, Seat.COLUMN_NUMBER)
            Seat.CHECK -> {
                if (blockRow?.checkNumberRangeByColumn(column, number) == false) {
                    response(false, Seat.CHECK)
                } else {
                    response(true, Seat.CHECK)
                }
            }

            else -> Unit
        }
    }

    fun handleColumn(column: Int, response: (isSuccess: Boolean, seat: Seat) -> Unit) {
        if (blockRow?.checkColumnRange(column) == true) {
            response(true, Seat.COLUMN)
            return
        }

        response(false, Seat.COLUMN)
    }

    fun handleNumber(number: Int, response: (isSuccess: Boolean, seat: Seat) -> Unit) {
        if (blockRow?.checkNumberRange(number) == true) {
            response(true, Seat.NUMBER)
            return
        }

        response(false, Seat.NUMBER)
    }

    fun clearSeat() {
        reset = true
        _reviewFilter.value = _reviewFilter.value.copy(rowNumber = null, seatNumber = null)
        getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
    }

    fun clearMonth() {
        reset = true
        _reviewFilter.value = _reviewFilter.value.copy(month = null)
        getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
    }
}