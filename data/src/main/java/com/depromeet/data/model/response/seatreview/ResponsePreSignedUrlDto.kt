package com.depromeet.data.model.response.seatreview

import com.depromeet.domain.entity.response.seatreview.ResponsePresignedUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePreSignedUrlDto(
    @SerialName("presignedUrl")
    val presignedUrl: String,
) {

    fun toResponsePreSignedUrl(): ResponsePresignedUrl {
        return ResponsePresignedUrl(presignedUrl)
    }
}
