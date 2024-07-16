package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.SeatBlockModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSeatBlockDto(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,

) {
    fun toSeatBlock(): SeatBlockModel {
        return SeatBlockModel(
            id = id,
            code = code,
        )
    }
}
