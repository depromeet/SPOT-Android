package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.RecommendRequestModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUploadUrlDto(
    @SerialName("presignedUrl")
    val presignedUrl: String,
) {

    fun toResponseUploadUrl(): RecommendRequestModel {
        return RecommendRequestModel(presignedUrl)
    }
}
