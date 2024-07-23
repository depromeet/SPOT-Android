package com.depromeet.data.model.response.viewfinder

import com.depromeet.domain.entity.response.viewfinder.BlockRowResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockRowResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,
    @SerialName("rowInfo")
    val rowInfo: List<RowInfoResponseDto>,
) {
    @Serializable
    data class RowInfoResponseDto(
        @SerialName("id")
        val id: Int,
        @SerialName("number")
        val number: Int,
        @SerialName("seatNumList")
        val seatNumList: List<Int>
    ) {
        fun toRowInfoResponse() = BlockRowResponse.RowInfoResponse(
            id  = id,
            number = number,
            seatNumList = seatNumList
        )
    }
}