package com.depromeet.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestFileExtensionDto(
    @SerialName("fileExtension")
    val fileExtension: String,
)
