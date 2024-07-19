package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.ResponsePresignedUrlModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePreSignedUrlDto(
    @SerialName("presignedUrl")
    val presignedUrl: String,
) {

    fun toResponsePreSignedUrl(): ResponsePresignedUrlModel {
        return ResponsePresignedUrlModel(presignedUrl)
    }
}
