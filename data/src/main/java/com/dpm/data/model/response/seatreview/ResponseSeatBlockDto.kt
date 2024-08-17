package com.dpm.data.model.response.seatreview

import com.dpm.domain.entity.response.seatreview.ResponseSeatBlock
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
