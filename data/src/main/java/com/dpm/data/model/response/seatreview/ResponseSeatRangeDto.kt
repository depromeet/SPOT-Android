package com.dpm.data.model.response.seatreview

import com.dpm.domain.entity.response.seatreview.ResponseSeatRange
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
        @SerialName("seatNumList")
        val seatNumList: List<Int>,
    ) {
        fun toRowInfo(): ResponseSeatRange.RowInfo {
            return ResponseSeatRange.RowInfo(
                id = id,
                number = number,
                seatNumList = seatNumList,
            )
        }
    }

    fun toSeatRange(): ResponseSeatRange {
        return ResponseSeatRange(
            id = id,
            code = code,
            rowInfo = rowInfo.map { it.toRowInfo() },
        )
    }
}
