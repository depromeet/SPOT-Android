package com.dpm.data.model.request.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestFileExtensionDto(
    @SerialName("fileExtension")
    val fileExtension: String,
)
