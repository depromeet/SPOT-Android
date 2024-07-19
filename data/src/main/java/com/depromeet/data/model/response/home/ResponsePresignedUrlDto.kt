package com.depromeet.data.model.response.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePresignedUrlDto(
    @SerialName("presignedUrl")
    val presignedUrl : String
)
