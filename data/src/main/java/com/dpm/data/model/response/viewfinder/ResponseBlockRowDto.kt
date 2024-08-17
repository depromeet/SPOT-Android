package com.dpm.data.model.response.viewfinder

import com.dpm.domain.entity.response.viewfinder.ResponseBlockRow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBlockRowDto(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,
    @SerialName("rowInfo")
    val rowInfo: List<ResponseRowInfoDto>,
) {
    @Serializable
    data class ResponseRowInfoDto(
        @SerialName("id")
        val id: Int,
        @SerialName("number")
        val number: Int,
        @SerialName("seatNumList")
        val seatNumList: List<Int>
    )
}

fun ResponseBlockRowDto.toBlockRowResponse() = ResponseBlockRow(
    id = id,
    code = code,
    rowInfo = rowInfo.map { it.toRowInfoResponse() }
)

fun ResponseBlockRowDto.ResponseRowInfoDto.toRowInfoResponse() = ResponseBlockRow.ResponseRowInfo(
    id = id,
    number = number,
    seatNumList = seatNumList
)