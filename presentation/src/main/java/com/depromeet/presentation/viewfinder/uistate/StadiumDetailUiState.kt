package com.depromeet.presentation.viewfinder.uistate

import com.depromeet.domain.entity.response.viewfinder.ResponseBlockReview

sealed class StadiumDetailUiState {
    data class ReviewsData(
        val topReviewImages: List<ResponseBlockReview.ResponseTopReviewImages>,
        val stadiumContent: ResponseBlockReview.ResponseLocation,
        val keywords: List<ResponseBlockReview.ResponseKeyword>,
        val total: Long,
        val reviews: List<ResponseBlockReview.ResponseReview>,
        val pageState: Boolean
    ) : StadiumDetailUiState()

    data class Failed(
        val message: String
    ) : StadiumDetailUiState()
    object Empty : StadiumDetailUiState()
    object Loading : StadiumDetailUiState()
}
