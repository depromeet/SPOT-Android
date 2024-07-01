package com.depromeet.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExampleRequestDto(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
)
