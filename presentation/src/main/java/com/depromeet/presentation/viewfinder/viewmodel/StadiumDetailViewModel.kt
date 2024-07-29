package com.depromeet.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.domain.entity.response.viewfinder.BlockRowResponse
import com.depromeet.domain.model.viewfinder.Seat
import com.depromeet.domain.repository.ViewfinderRepository
import com.depromeet.presentation.viewfinder.uistate.StadiumDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumDetailViewModel @Inject constructor(
    private val viewfinderRepository: ViewfinderRepository
) : ViewModel() {
    var stadiumId: Int = 0
    var blockCode: String = ""
    private var blockRow: BlockRowResponse? = null

    private val _detailUiState =
        MutableStateFlow<StadiumDetailUiState>(StadiumDetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    private val _scrollState = MutableStateFlow(false)
    val scrollState = _scrollState.asStateFlow()

    private val _reviewFilter = MutableStateFlow<BlockReviewRequestQuery>(BlockReviewRequestQuery(
        rowNumber = null,
        seatNumber = null,
        year = null,
        month = null,
        page = 0,
        size = 10
    ))
    val reviewFilter = _reviewFilter.asStateFlow()


    fun updateScrollState(state: Boolean) {
        _scrollState.value = state
    }

    fun updateMonth(month: Int?) {
        if (month != _reviewFilter.value.month) {
            _reviewFilter.value = _reviewFilter.value.copy(year = 2024, month = month, page = 0)
            getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
        }
    }

    fun updateSeat(column: Int, number: Int? = null) {
        _reviewFilter.value =
            _reviewFilter.value.copy(rowNumber = column, seatNumber = number, page = 0)
        getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
    }

    fun updateRequestPathVariable(stadiumId: Int, blockCode: String) {
        this.stadiumId = stadiumId
        this.blockCode = blockCode
    }

    fun updateQueryPage(callback: (query: BlockReviewRequestQuery) -> Unit) {
        _reviewFilter.value = _reviewFilter.value.copy(page = _reviewFilter.value.page + 1)
        callback(_reviewFilter.value)
    }

    fun getBlockReviews(
        stadiumId: Int = this.stadiumId,
        blockCode: String = this.blockCode,
        query: BlockReviewRequestQuery = this._reviewFilter.value,
    ) {
        viewModelScope.launch {
            viewfinderRepository.getBlockReviews(stadiumId, blockCode, query)
                .onSuccess { blockReviews ->
                    if (blockReviews.topReviewImages.isEmpty() && blockReviews.reviews.isEmpty()) {
                        _detailUiState.value = StadiumDetailUiState.Empty
                    } else {
                        if (_detailUiState.value is StadiumDetailUiState.ReviewsData) {
                            val reviewsData =
                                (_detailUiState.value as StadiumDetailUiState.ReviewsData)
                            if (blockReviews.first) {
                                _detailUiState.value = reviewsData.copy(
                                    reviews = blockReviews.reviews,
                                    pageState = blockReviews.last,
                                    total = blockReviews.totalElements
                                )
                            } else {
                                val uReviewsData = reviewsData.reviews.toMutableList()
                                uReviewsData.addAll(blockReviews.reviews)
                                _detailUiState.value = reviewsData.copy(
                                    reviews = uReviewsData,
                                    pageState = blockReviews.last
                                )
                            }

                        } else {
                            _detailUiState.value = StadiumDetailUiState.ReviewsData(
                                topReviewImages = blockReviews.topReviewImages,
                                stadiumContent = blockReviews.location,
                                keywords = blockReviews.keywords,
                                total = blockReviews.totalElements,
                                reviews = blockReviews.reviews,
                                pageState = blockReviews.last
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
        if (blockRow?.checkColumnRange(column) == true) {
            if (blockRow?.checkNumberRange(column, number) == true) {
                response(true, Seat.NUMBER)
                return
            } else {
                response(false, Seat.NUMBER)
                return
            }
        }

        response(false, Seat.COLUMN)
    }

    fun handleColumn(column: Int, response: (isSuccess: Boolean, seat: Seat) -> Unit) {
        if (blockRow?.checkColumnRange(column) == true) {
            response(true, Seat.COLUMN)
            return
        }

        response(false, Seat.COLUMN)
    }

    fun clearSeat() {
        _reviewFilter.value = _reviewFilter.value.copy(rowNumber = null, seatNumber = null)
        getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
    }

    fun clearMonth() {
        _reviewFilter.value = _reviewFilter.value.copy(month = null)
        getBlockReviews(stadiumId, blockCode, _reviewFilter.value)
    }
}