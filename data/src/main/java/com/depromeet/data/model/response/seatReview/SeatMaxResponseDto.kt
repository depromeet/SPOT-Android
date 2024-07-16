package com.depromeet.data.model.response.seatReview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeatMaxResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,
    @SerialName("rowInfo")
    val rowInfo: List<RowInfoDto>,
) {
    @Serializable
    data class RowInfoDto(
        @SerialName("id")
        val id: Int,
        @SerialName("number")
        val number: Int,
        @SerialName("minSeatNum")
        val minSeatNum: Int,
        @SerialName("maxSeatNum")
        val maxSeatNum: Int,
    )
}
