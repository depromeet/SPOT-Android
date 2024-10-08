package com.dpm.presentation.viewfinder.uistate

import com.dpm.domain.entity.response.viewfinder.ResponseBlockReview

sealed class StadiumDetailUiState {
    data class ReviewsData(
        val topReviewImages: List<ResponseBlockReview.ResponseReview>,
        val stadiumContent: ResponseBlockReview.ResponseLocation,
        val keywords: List<ResponseBlockReview.ResponseKeyword>,
        val total: Long,
        val reviews: List<ResponseBlockReview.ResponseReview>,
        val cursor: String,
        val hasNext: Boolean
    ) : StadiumDetailUiState()

    data class Failed(
        val message: String
    ) : StadiumDetailUiState()
    object Empty : StadiumDetailUiState()
    object Loading : StadiumDetailUiState()
}
