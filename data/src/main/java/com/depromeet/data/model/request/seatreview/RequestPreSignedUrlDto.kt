package com.depromeet.data.model.request.seatReview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPreSignedUrlDto(
    @SerialName("fileExtension")
    val fileExtension: String,
)
