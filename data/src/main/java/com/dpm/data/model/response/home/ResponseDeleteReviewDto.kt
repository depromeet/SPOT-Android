package com.dpm.data.model.response.home

import com.dpm.domain.entity.response.home.ResponseDeleteReview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDeleteReviewDto(
    @SerialName("deletedReviewId")
    val deleteReviewId: Int,
) {
    companion object {
        fun ResponseDeleteReviewDto.toDeleteReviewResponse() = ResponseDeleteReview(
            reviewId = deleteReviewId
        )
    }
}