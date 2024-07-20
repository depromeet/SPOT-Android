package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.StadiumNameModel
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
    fun toStadiumName(): StadiumNameModel {
        return StadiumNameModel(
            id = id,
            name = name,
            isActive = isActive,
        )
    }
}
