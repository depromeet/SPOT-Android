package com.depromeet.presentation.viewfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.core.state.UiState
import com.depromeet.domain.entity.request.viewfinder.BlockReviewRequestQuery
import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse
import com.depromeet.domain.entity.response.viewfinder.BlockRowResponse
import com.depromeet.domain.model.viewfinder.Seat
import com.depromeet.domain.repository.ViewfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _blockReviews = MutableStateFlow<UiState<BlockReviewResponse>>(UiState.Loading)
    val blockReviews: StateFlow<UiState<BlockReviewResponse>> = _blockReviews.asStateFlow()

    private val _scrollState = MutableStateFlow(false)
    val scrollState = _scrollState.asStateFlow()

    private val _month = MutableStateFlow(0)
    val month = _month.asStateFlow()

    private val _seat = MutableStateFlow("")
    val seat = _seat.asStateFlow()

    fun updateScrollState(state: Boolean) {
        _scrollState.value = state
    }

    fun updateMonth(month: Int) {
        _month.value = month
    }

    fun getBlockReviews(
        stadiumId: Int,
        blockCode: String,
        query: BlockReviewRequestQuery = BlockReviewRequestQuery(),
    ) {
        viewModelScope.launch {
            viewfinderRepository.getBlockReviews(stadiumId, blockCode, query)
                .onSuccess { blockReviews ->
                    if (blockReviews.topReviewImages.isEmpty() && blockReviews.reviews.isEmpty()) {
                        _blockReviews.value = UiState.Empty
                    } else {
                        _blockReviews.value = UiState.Success(blockReviews)
                    }
                }.onFailure { e ->
                    _blockReviews.value = UiState.Failure(e.message ?: "실패")
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

    fun updateSeat(seat: String) {
        _seat.value = seat
    }

    fun updateRequestPathVariable(stadiumId: Int, blockCode: String) {
        this.stadiumId = stadiumId
        this.blockCode = blockCode
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
}