package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.DeleteReviewResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDeleteReviewDto(
    @SerialName("deleteReviewId")
    val deleteReviewId: Int,
) {
    companion object {
        fun ResponseDeleteReviewDto.toDeleteReviewResponse() = DeleteReviewResponse(
            reviewId = deleteReviewId
        )
    }
}