package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.ResponseSeatBlock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSeatBlockDto(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,

) {
    fun toSeatBlock(): ResponseSeatBlock {
        return ResponseSeatBlock(
            id = id,
            code = code,
        )
    }
}
