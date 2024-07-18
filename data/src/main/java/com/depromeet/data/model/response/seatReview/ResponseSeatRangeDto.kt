package com.depromeet.data.model.response.seatReview

import com.depromeet.domain.entity.response.seatReview.SeatRangeModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSeatRangeDto(
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
    ) {
        fun toRowInfo(): SeatRangeModel.RowInfo {
            return SeatRangeModel.RowInfo(
                id = id,
                number = number,
                minSeatNum = minSeatNum,
                maxSeatNum = maxSeatNum,
            )
        }
    }

    fun toSeatRange(): SeatRangeModel {
        return SeatRangeModel(
            id = id,
            code = code,
            rowInfo = rowInfo.map { it.toRowInfo() },
        )
    }
}
