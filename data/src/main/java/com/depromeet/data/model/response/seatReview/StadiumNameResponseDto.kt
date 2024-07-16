package com.depromeet.data.model.response.seatReview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StadiumNameResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
