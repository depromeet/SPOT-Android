package com.depromeet.presentation.viewfinder.uistate

import com.depromeet.domain.entity.response.viewfinder.BlockReviewResponse

sealed class StadiumDetailUiState {
    data class ReviewsData(
        val topReviewImages: List<BlockReviewResponse.TopReviewImagesResponse>,
        val stadiumContent: BlockReviewResponse.LocationResponse,
        val keywords: List<BlockReviewResponse.KeywordResponse>,
        val total: Long,
        val reviews: List<BlockReviewResponse.ReviewResponse>,
        val pageState: Boolean
    ) : StadiumDetailUiState()

    data class Failed(
        val message: String
    ) : StadiumDetailUiState()
    object Empty : StadiumDetailUiState()
    object Loading : StadiumDetailUiState()
}
