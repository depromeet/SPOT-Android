package com.depromeet.data.model.response.seatReview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSeatBlockDto(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,

)
