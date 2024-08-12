package com.depromeet.data.model.response.seatreview

import com.depromeet.domain.entity.response.seatreview.ResponseStadiumName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStadiumNameDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("isActive")
    val isActive: Boolean,
) {
    fun toStadiumName(): ResponseStadiumName {
        return ResponseStadiumName(
            id = id,
            name = name,
            isActive = isActive,
        )
    }
}
